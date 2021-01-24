package name.bychkov.ocr.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.bychkov.ocr.service.OcrService;

@WebServlet("/ocr")
@MultipartConfig
public class OcrServlet extends HttpServlet
{
	private static final long serialVersionUID = 8833405479576633645L;
	private static final Logger logger = LoggerFactory.getLogger(OcrServlet.class);
	
	private String uploadDir;
	
	@Override
	public void init()
	{
		String uploadFilePath = System.getProperty("user.home") + File.separator + "uploads";
		
		File fileSaveDir = new File(uploadFilePath);
		if (!fileSaveDir.exists())
		{
			fileSaveDir.mkdirs();
		}
		uploadDir = fileSaveDir.getAbsolutePath();
		logger.debug("Upload File Directory=", fileSaveDir.getAbsolutePath());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String filename = null;
		String language = null;
		for (Part part : request.getParts())
		{
			switch (part.getName())
			{
				case "file":
					String originalFilename = part.getSubmittedFileName();
					filename = uploadDir + File.separator + UUID.randomUUID().toString();
					part.write(filename);
					logger.debug("Uploaded File {} saved as {}", originalFilename, filename);
					break;
				case "language":
					language = IOUtils.toString(part.getInputStream(), StandardCharsets.UTF_8);
					logger.debug("language = " + language);
					break;
			}
		}
		
		try
		{
			OcrService.doOcr(new File(filename), language);
		}
		catch (IOException | InterruptedException e)
		{
			throw new ServletException(e);
		}
		try(InputStream is = new FileInputStream(new File(filename + ".resp.txt"))) {
			response.getWriter().println(IOUtils.toString(is, StandardCharsets.UTF_8));
		}
	}
}