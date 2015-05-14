package org.adl.samplerte.server;

public class LRSInfo {
   public static final String LRS_DEFAULT = "ADL LRS";
   public int id;
   public String UserID;
   public String LRSName;
   public String Endpoint;
   public String UserName;
   public String Password;
   
   public LRSInfo() {}
   
   public LRSInfo(String userid, String lrsname, String endpoint, String user, String password) {
      UserID = userid;
      LRSName = lrsname;
      Endpoint = endpoint;
      UserName = user;
      Password = password;
   }
   
   public String toString() {
      return id + "(" + LRSName + "): for " + UserID + "\nendpoint: " + Endpoint + "\nusername: " + UserName;
   }
}
