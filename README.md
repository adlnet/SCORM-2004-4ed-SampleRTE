# SCORM-2004-4ed-SampleRTE

## For Linux

1. Download apache-ant-1.7.1-bin.tar.bz2 (http://archive.apache.org/dist/ant/binaries/) and unzip (put this anywhere, just remember the path)
2. Install openjdk-6-jdk (or 7, or oracle jdk8) (`sudo apt-get install openjdk-<6 or 7>-jdk`)
3. Download the core tar.gz file and unzip tomcat (we'll be moving this later) - https://tomcat.apache.org/download-60.cgi
4. Create a directory named `SCORM_2004_4th_Ed_Sample_RTE_V1.1.1`. Inside of that directory create another directory named `Sample_RTE`.
5. Inside of `Sample_RTE` clone this repository (`git clone https://github.com/adlnet/SCORM-2004-4ed-SampleRTE.git`) and rename this folder `source`. Also don't forget to pull down this branch (enhancements)
6. Rename your `apache-tomcat-<version>` unpackaged download from earlier to 'apache-tomcat' and move it inside `Sample_RTE`.
7. sudo nano /etc/environment and add:
  
  ```
  JAVA_HOME="/path/to/jvm/java-<version #>-openjdk-amd64"
  ANT_HOME="/path/to/apache-ant-1.7.1"
  SCORM4ED_SRTE111_HOME="/path/to/SCORM_2004_4th_Ed_Sample_RTE_V1.1.1"
  SCORM4ED_TS111_HOME="/path/to/SCORM_2004_4th_Ed_Sample_RTE_V1.1.1"
  CATALINA_HOME="/path/to/SCORM_2004_4th_Ed_Sample_RTE_V1.1.1/Sample_RTE/apache-tomcat"
  
  PATH="...:${JAVA_HOME}/bin:${ANT_HOME}/bin"
  ```

8. Run `. /etc/environment` to save it. Run this command whenever you open a new terminal and intend to use the SRTE
9. Unzip SRTE.zip, extract the api, bin, css, RTE_Readme folders and place them in the SCORM4ED_SRTE111_HOME/Sample_RTE folder (simulates running the installer)
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
11. Run the command `ant` in the `source` directory so it builds the project. (If you ever need to clean it or want to rerun these instructions, always run `ant cleanRTE` to remove stale files and reset the db)
12. To run, navigate to CATALINA_HOME/bin and run `sh startup.sh` (`sh shutdown.sh` to stop)
13. Go to `localhost:8080`

## For Windows

1. Download apache-ant-1.7.1-bin.zip (http://archive.apache.org/dist/ant/binaries/) and unzip (put this anywhere, just remember the path)
2. Install java jdk6, 7, or 8
3. Download the core 64-bit Windows.zip file and unzip tomcat (we'll be moving this later) - https://tomcat.apache.org/download-60.cgi
4. Create a directory named `SCORM_2004_4th_Ed_Sample_RTE_V1.1.1`. Inside of that directory create another directory named `Sample_RTE`.
5. Inside of `Sample_RTE` clone this repository (`git clone https://github.com/adlnet/SCORM-2004-4ed-SampleRTE.git`) and rename this folder `source`. Also don't forget to pull down this branch (enhancements)
6. Rename your `apache-tomcat-<version>` unzipped download from earlier to 'apache-tomcat' and move it inside `Sample_RTE`.
7. Add these system variables:

  ```
  JAVA_HOME="<Drive>:\path\to\Java\jdk<version>"
  ANT_HOME="<Drive>:\path\to\apache-ant-1.7.1"
  SCORM4ED_SRTE111_HOME="<Drive>:\path\to\SCORM_2004_4th_Ed_Sample_RTE_V1.1.1"
  SCORM4ED_TS111_HOME="<Drive>:\path\to\SCORM_2004_4th_Ed_Sample_RTE_V1.1.1"
  CATALINA_HOME="<Drive>:\path\to\SCORM_2004_4th_Ed_Sample_RTE_V1.1.1\Sample_RTE\apache-tomcat" 
  ```
8. Add these to your system Path:

  ```
  %JAVA_HOME%\bin
  %ANT_HOME%\bin
  ```
9. Unzip SRTE.zip, extract the api, bin, css, RTE_Readme folders and place them in the SCORM4ED_SRTE111_HOME/Sample_RTE folder (simulates running the installer)
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
11. Run the command `ant` in the `source` directory so it builds the project. (If you ever need to clean it or want to rerun these instructions, always run `ant cleanRTE` to remove stale files and reset the db)
12. To run, navigate to CATALINA_HOME/bin and run `catalina.bat start` (`catalina.bat stop` to stop)
13. Go to `localhost:8080`


#### Admin Login
By default the admin credentials are:
  
  ```
  username: admin
  password:admin
  ```
Once you login as the admin you can change your password
