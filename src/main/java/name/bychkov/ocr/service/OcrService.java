package name.bychkov.ocr.service;

import java.io.File;
import java.io.IOException;

public class OcrService
{
	public static void doOcr(File file, String language) throws IOException, InterruptedException
	{
		ProcessBuilder pb = new ProcessBuilder("tesseract", "-l", "eng", file.getAbsolutePath(), file.getAbsolutePath() + ".resp");
		Process process = pb.start();
		process.waitFor();
		process.destroy();
	}
}