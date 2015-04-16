# SCORM-2004-4ed-SampleRTE

## For Linux

1. Download apache-ant-1.7.1-bin.tar.bz2
2. Install openjdk-6-jdk (or 7, or oracle jdk8)
3. Download and install tomcat per these instructions - https://www.mulesoft.com/tcat/tomcat-60 - then move to CATALINA_HOME folder (from /usr/share/tomcat6/*)
4. sudo nano /etc/environment (add JAVA_HOME="/path/to/java-1.6.0-openjdk-amd64" (or jdk7 or jdk8), ANT_HOME="path/to/ant", SCORM4ED_SRTE111_HOME="path/to/created/dir/SCORM_2004_4th_Ed_Sample_RTE_V1.1.1", CATALINA_HOME="path/to/created/dir/SCORM_2004_4th_Ed_Sample_RTE_V1.1.1/Sample_RTE/apache-tomcat")
5. Add ${JAVA_HOME}/bin:${ANT_HOME}/bin to the end of PATH in /etc/environment
6. Anytime a terminal is opened, be sure to run `. /etc/environment`
7. Unzip SRTE.zip, extract api, bin, css, RTE_Readme folders and place them in SCORM4ED_SRTE111_HOME/Sample_RTE folder (simulates running the installer)
8. To run navigate to CATALINA_HOME/bin and run sh startup.sh....to stop sh shutdown.sh

## For Windows

1. Install ant-1.7.1
2. Install java jdk6, 7, or 8
3. Set JAVA_HOME env var to jdk path, ANT_HOME to ant path, SCORM4ED_SRTE111_HOME to /path/to/SCORM_2004_4TH_Ed_sample_RTE_V1.1.1, CATALINA_HOME to /path/to/SCORM_2004_4TH_Ed_sample_RTE_V1.1.1/Sample_RTE/apache-tomcat
4. Add JAVA_HOME\bin and ANT_HOME\bin to Path
5. Run the installer (make sure the install path matches the paths you provided in the env var SCORM4ED_SRTE111_HOME)
6. Replace the SCORM4ED_SRTE111_HOME/Sample_RTE/source directory with this repo and rename the folder, source