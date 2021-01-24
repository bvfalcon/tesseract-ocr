FROM centos:centos7
MAINTAINER Vladimir V. Bychkov <mail@bychkov.name>

RUN yum install -y epel-release && yum repolist -y
RUN yum install -y leptonica tesseract tesseract-langpack-eng tesseract-langpack-ger tesseract-langpack-rus

ENTRYPOINT ["tesseract"]
CMD ["--version"]