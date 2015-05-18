package org.adl.samplerte.server;

public class UserAgentInfo {
   public static final String LRS_HOMEPAGE_DEFAULT = "http://purl.org/xapi/adl/accounts";
   public static final String AGENT_ALIAS_DEFAULT = "(default)";
   public int id;
   public String UserID;
   public String AgentAlias;
   public String Mbox;
   public String HomePage;
   public String AccName;
   
   public UserAgentInfo() {}
   
   public UserAgentInfo(String userid, String alias, String mbox, String homepage, String accname)
   {
      UserID = userid;
      AgentAlias = alias;
      Mbox = mbox;
      HomePage = homepage;
      AccName = accname;
   }

   @Override
   public String toString() {
      return "UserAgentInfo [id=" + id + ", UserID=" + UserID + ", AgentAlias=" + AgentAlias + 
            ", Mbox=" + Mbox + ", HomePage=" + HomePage + ", AccName=" + AccName + "]";
   }
}
