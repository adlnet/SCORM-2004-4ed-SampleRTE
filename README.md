# SCORM-2004-4ed-SampleRTE

## For Linux

1. Download apache-ant-1.7.1-bin.tar.bz2 and unzip
2. Install openjdk-6-jdk (or 7, or oracle jdk8)
3. Download the core tar.gz file and unzip tomcat (we'll be moving this later) - https://tomcat.apache.org/download-60.cgi
4. Create a directory named `SCORM_2004_4th_Ed_Sample_RTE_V1.1.1`. Inside of that directory create another directory named `Sample_RTE`.
5. Inside of `Sample_RTE` clone this repository (git clone https://github.com/adlnet/SCORM-2004-4ed-SampleRTE.git) and rename the folder `source`.
6. Rename your `apache-tomcat-<version>` download to 'apache-tomcat' and move it inside `Sample_RTE`.
7. sudo nano /etc/environment and add:
  
  ```
  JAVA_HOME="/path/to/jvm/java-<version #>-openjdk-amd64"
  ANT_HOME="/path/to/apache-ant-1.7.1"
  SCORM4ED_SRTE111_HOME="/path/to/SCORM_2004_4th_Ed_Sample_RTE_V1.1.1"
  SCORM4ED_TS111_HOME="/path/to/SCORM_2004_4th_Ed_Sample_RTE_V1.1.1"
  CATALINA_HOME="/path/to/SCORM_2004_4th_Ed_Sample_RTE_V1.1.1/Sample_RTE/apache-tomcat"
  ```
Also add `${JAVA_HOME}/bin:${ANT_HOME}/bin` to the end of your PATH in `/etc/environment`
8. Run `. /etc/environment` to save it. Run this command whenever you open a new terminal and intend to use the SRTE
9. Unzip SRTE.zip, extract api, bin, css, RTE_Readme folders and place them in SCORM4ED_SRTE111_HOME/Sample_RTE folder (simulates running the installer)
10. Replace the insides of `CATALINA_HOME/webapps/ROOT/index.html` with:
  
  ```
  <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
        <head>
                <title>Redirecting to /adl</title>
        </head>
        <body onLoad="javascript:window.location='adl/runtime/LMSMain.htm';">
        </body>
  </html>
  ```
10. Run the command `ant` in the `source` directory so it builds the project. (If you ever need to clean it or rerun these instructions always run `ant cleanRTE` to remove stale files and reset the db)
10. To run navigate to CATALINA_HOME/bin and run `sh startup.sh`....to stop `sh shutdown.sh`
11. Go to `localhost:8080`

## For Windows

1. Install ant-1.7.1
2. Install java jdk6, 7, or 8
3. Set JAVA_HOME env var to jdk path, ANT_HOME to ant path, SCORM4ED_SRTE111_HOME to /path/to/SCORM_2004_4TH_Ed_sample_RTE_V1.1.1, CATALINA_HOME to /path/to/SCORM_2004_4TH_Ed_sample_RTE_V1.1.1/Sample_RTE/apache-tomcat
4. Add JAVA_HOME\bin and ANT_HOME\bin to Path
5. Run the installer (make sure the install path matches the paths you provided in the env var SCORM4ED_SRTE111_HOME)
6. Replace the SCORM4ED_SRTE111_HOME/Sample_RTE/source directory with this repo and rename the folder, source
