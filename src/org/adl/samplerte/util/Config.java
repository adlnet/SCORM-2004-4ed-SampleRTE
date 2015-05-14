package org.adl.samplerte.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.adl.samplerte.server.LRSInfo;

public class Config {
   private static final String SRTE_HOME_ENV_VAR = "SCORM4ED_SRTE111_HOME";
   //\Sample_RTE\source\config
   private static final String CONFIG_PATH = System.getenv(SRTE_HOME_ENV_VAR)+ File.separator + "Sample_RTE" + File.separator + "source" + File.separator + "config" + File.separator + "config.properties";
   
   private Properties config;
   private static Config instance;
   
   private Config(Properties in)
   {
      config = in;
   }
   
   private Properties getConfig() {
      return config;
   }
   
   private static Config getInstance() {
      if (instance == null)
      {
         instance = new Config(getPropFile());
      }
      return instance;
   }
   
   private static Properties getPropFile() {
      Properties properties = new Properties();
      try {
        properties.load(new FileInputStream(CONFIG_PATH));
      } 
      catch (IOException e) {
        System.out.println("Error loading config properties file\n  srte env var: " + SRTE_HOME_ENV_VAR + "\n  config path: " + CONFIG_PATH);
      }
      return properties;
   }
   
   public static String getProp(String prop)
   {
      return getInstance().getConfig().getProperty(prop, "");
   }
   
   public static LRSInfo getLRSInfo()
   {
      Properties p = getInstance().getConfig();
      return new LRSInfo(p.getProperty("rte.userid", "__default__"), 
                         p.getProperty("lrs.name", LRSInfo.LRS_DEFAULT), 
                         p.getProperty("lrs.endpoint", "https://lrs.adlnet.gov/xapi/"), 
                         p.getProperty("lrs.auth.userid", "tom"),
                         p.getProperty("lrs.auth.password", "1234"));
   }
}
