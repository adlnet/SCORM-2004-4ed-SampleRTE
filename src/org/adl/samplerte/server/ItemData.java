package org.adl.samplerte.server;

public class ItemData {
   public String itemID;
   public String itemTitle;
   public int activityID; 
   public String itemLaunch;
   
   // status stuff
   public float scaled = 0f;
   public float raw = 0f;
   public float min = 0f;
   public float max = 0f;
   
   public boolean success = false;
   public boolean completion = false;
   
   public String response = null;
   public String duration = null;

   public String refStmtID = null;
   public String statement = null;
   
   public ItemData(int id, String itemid, String title)
   {
      activityID = id;
      itemID = itemid;
      itemTitle = title;
      itemLaunch = "";
   }
   
   public ItemData(int id, String itemid, String title, String launch)
   {
      activityID = id;
      itemID = itemid;
      itemTitle = title;
      itemLaunch = launch;
   }
   
   public String toString() 
   {
      return activityID + ": " + itemID + " - " + itemTitle;
   }
}
