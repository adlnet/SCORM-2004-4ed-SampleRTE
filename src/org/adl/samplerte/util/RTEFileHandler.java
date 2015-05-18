/*******************************************************************************

ADL SCORM 2004 4th Edition Sample Run-Time Environment

The ADL SCORM 2004 4th Ed. Sample Run-Time Environment is licensed under
Creative Commons Attribution-Noncommercial-Share Alike 3.0 United States.

The Advanced Distributed Learning Initiative allows you to:
  *  Share - to copy, distribute and transmit the work.
  *  Remix - to adapt the work. 

Under the following conditions:
  *  Attribution. You must attribute the work in the manner specified by the author or
     licensor (but not in any way that suggests that they endorse you or your use
     of the work).
  *  Noncommercial. You may not use this work for commercial purposes. 
  *  Share Alike. If you alter, transform, or build upon this work, you may distribute
     the resulting work only under the same or similar license to this one. 

For any reuse or distribution, you must make clear to others the license terms of this work. 

Any of the above conditions can be waived if you get permission from the ADL Initiative. 
Nothing in this license impairs or restricts the author's moral rights.

*******************************************************************************/
package org.adl.samplerte.util;

//Native java imports
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.adl.datamodels.DMFactory;
import org.adl.datamodels.DMInterface;
import org.adl.datamodels.SCODataManager;
import org.adl.util.debug.DebugIndicator;

/**
 * <strong>Filename:</strong> RTEFilehandler.java<br><br>
 *
 * <strong>Description:</strong><br>
 * <code>RTEFilehandler</code> class is a utility class.  It contains methods 
 * to create a state file used to store datamodel information and to inititalize
 * datamodel elements based on elements in the imsmanifest.xml file.  It 
 * contains a method that queries teh database for initialized datamodel values 
 * and stores those values in String array.  In  addition, this class contains 
 * logic to delete any course files and temporary uploaded packages after a 
 * successful import.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM 2004 4th Edition Sample 
 * Run-Time Environment Version 1.1.1.<br>
 * <br>
 * 
 * <strong>Implementation Issues:</strong><br><br>
 * 
 * <strong>Known Problems:</strong><br><br>
 * 
 * <strong>Side Effects:</strong><br><br>
 * 
 * @author ADL Technical Team
 */
public class RTEFileHandler
{
   /**
    * Debug indicator boolean
    */
   private static boolean _Debug = DebugIndicator.ON; 
   
   /**
    * The Sample RTE flat file root directory
    */
   private String mSampleRTERoot;

   

   /**
    * Default Constructor for the <code>RTEFileHandler</code>
    */
   public RTEFileHandler()
   {
      mSampleRTERoot = System.getProperty("user.home") + File.separator + "SCORM4EDSampleRTE111Files";
   }

   /**
    * This method is used to create a state file for an item.  It is called
    * by <code>LMSCMIServlet</code>when a item is entered for the first time.
    * 
    * @param iNumAttempt  Number of the current attempt on the item
    * @param iUserID  The Sample RTE's unique user identifier for
    *        a learner 
    * @param iUserName  The name of the user 
    * @param iCourseID  The unique course identifier
    * @param iItemID  The item's identifier
    * @param iDbID  The item's unique identifier (may be the same as the iItemID)
    */
   public void initializeStateFile(String iNumAttempt, String iUserID,
                                   String iUserName, String iCourseID,
                                   String iItemID, String iDbID)
   {
      try
      {
         if ( _Debug )
         {
            System.out.println("**** IN INITIALIZESTATEFILE****");
         }
         
         String userDir = mSampleRTERoot + File.separator + iUserID +
                           File.separator + iCourseID;

         File scoDataDir = new File( userDir );

         // The course directory should not exist yet
         if ( !scoDataDir.isDirectory() )
         {                              
            if ( _Debug )
            {
               System.out.println("User directory does not exist");
            }
            scoDataDir.mkdirs();
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("In RTEFileHandler user directory " + 
                                  "already exists.");
            }
         }

         if ( _Debug )
         {
            System.out.println("In RTEFileHandler - scoID is " + iItemID);
         }

         // Now create a SCODataManager object, initialize values, and 
         // serialize to file for SCO
         String scoDataPath = userDir + File.separator + iItemID + "__" 
                                  + iNumAttempt;
         SCODataManager scoData = new SCODataManager();
         
         //  Add a SCORM 2004 Data Model
         scoData.addDM(DMFactory.DM_SCORM_2004);

         //  Add a SCORM 2004 Nav Data Model
         scoData.addDM(DMFactory.DM_SCORM_NAV);

         initSCOData(scoData, iUserID, iUserName, iCourseID, iDbID);

         File scoDataFile = new File(scoDataPath);

         // Create a new file if and only if it doesn't already exist
         boolean mNoFileExists = scoDataFile.createNewFile();

         //new file was created
         if ( mNoFileExists )
         {
            // Write out the data to disk using serialization
            FileOutputStream fos = new FileOutputStream(scoDataFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(scoData);
            oos.close();
            fos.close();

            if ( _Debug )
            {
               System.out.println("RTEFileHandler created State file for: " 
                                   + iItemID);
            }
         }
      }
      catch(Exception e)
      {
         e.printStackTrace();
       
      }
   }


   /**
    * This method deletes the course files for a student/course when a 
    * registration is removed.
    * 
    * @param iDeleteCourseID  The courseId of course to delete course files
    * 
    * @param iUserID  The user identifier associated with the files to delete
    */                                                      
   public void deleteCourseFiles(String iDeleteCourseID, String iUserID)
   {
      try
      {
         String userDir = mSampleRTERoot + File.separator + iUserID +
                          File.separator + iDeleteCourseID;

         if ( _Debug )
         {
            System.out.println("path  " + userDir);
         }

         File scoDataDir = new File(userDir);

         File scoFiles[] = scoDataDir.listFiles();

         for ( int i = 0; i < scoFiles.length; i++ )
         {
            scoFiles[i].delete();
         }

         scoDataDir.delete();
      }
      catch(Exception e)
      {            
         // files missing or none to begin with.. do nothing
      }
   }

   /**
    * This function deletes the files in the SCORM4EDSampleRTE111Files\tempUploads 
    * directory.
    * 
    */
   public void deleteTempUloadFiles()
   {
      try
      {
         String tempDir = mSampleRTERoot + File.separator + "tempUploads";
         File tempUploadDir = new File(tempDir);

         File tempDirs[] = tempUploadDir.listFiles();

         for ( int i = 0; i < tempDirs.length; i++ )
         {
            File tempFiles[] = tempDirs[i].listFiles();

            for ( int j = 0; j < tempFiles.length; j++ )
            {
               tempFiles[j].delete();
            }
            tempDirs[i].delete();
         }
      }
      catch(Exception e)
      {
         
         System.out.println("Error deleting files in the " + 
                               "tempUploads directory");              
         e.printStackTrace();
      }

   }

   /**
    * This method is used to get initialized data model element values
    * from the database.  The initialized values are returned in a String 
    * array in the following order: cmi.scaled_passing_score, cmi.launch_data,
    * cmi.max_time_allowed, cmi.time_limit_action, 
    * cmi.completion_threshold
    * 
    * @param ioSCOData The manager of SCO Data   
    * @param iCourseID  The id of the course
    * @param iItemID    The item ID
    * @param iUserID   The user ID
    */
   public void initPersistedData(SCODataManager ioSCOData, String iCourseID, 
                                 String iItemID, String iUserID)
   {
      try
      {  
         
         // Get some information from the database
         Connection conn = LMSDatabaseHandler.getConnection();

         PreparedStatement stmtSelectItem;
         PreparedStatement stmtSelectComments;
         PreparedStatement stmtSelectUser;

         String sqlSelectItem = "SELECT * FROM ItemInfo WHERE CourseID " + 
                                "= ? AND ItemIdentifier = ?";
         String sqlSelectComments = "SELECT * FROM SCOComments WHERE " +
                                    "ActivityID = ?";
         String sqlSelectUser = "SELECT * FROM UserInfo WHERE UserID = ?";


         stmtSelectItem = conn.prepareStatement(sqlSelectItem);
         stmtSelectComments = conn.prepareStatement(sqlSelectComments);
         stmtSelectUser = conn.prepareStatement(sqlSelectUser);
         
         ResultSet rsItem = null;
         ResultSet rsUser = null;

         synchronized(stmtSelectItem)
         {
            stmtSelectItem.setString(1, iCourseID);
            stmtSelectItem.setString(2, iItemID);
            rsItem = stmtSelectItem.executeQuery();
         }

         synchronized(stmtSelectUser)
         {
            stmtSelectUser.setString(1, iUserID);
            rsUser = stmtSelectUser.executeQuery();
         }

         String masteryScore = new String();
         String dataFromLMS = new String();
         String maxTime = new String();
         String timeLimitAction = new String();
         String completionThreshold = new String();
         String audLev = new String();
         String audCap = new String();
         String delSpd = new String();
         String lang = new String();
         int activityID;

         // Get the learner preference values from the database
         if ( rsUser.next() )
         {
            audLev = rsUser.getString("AudioLevel");
            audCap = rsUser.getString("AudioCaptioning");
            delSpd = rsUser.getString("DeliverySpeed");
            lang = rsUser.getString("Language");
         }
         

         while( rsItem.next() )
         {
            String type = rsItem.getString("Type");

            if ( type.equals("sco") || type.equals("asset") )
            {
               masteryScore = rsItem.getString("MinNormalizedMeasure");
               dataFromLMS = rsItem.getString("DataFromLMS");
               maxTime = rsItem.getString("AttemptAbsoluteDurationLimit");
               timeLimitAction = rsItem.getString("TimeLimitAction");
               completionThreshold = rsItem.getString("CompletionThreshold");
               activityID = rsItem.getInt("ActivityID");

               ResultSet rsComments = null;

               // Get the comments associated with an activity if any
               synchronized(stmtSelectComments)
               {
                  stmtSelectComments.setInt(1, activityID);
                  rsComments = stmtSelectComments.executeQuery();
               }

               // Loop through the comments and initialize the SCO data\
               int idx = 0;
               while ( rsComments.next() )
               {
                  String cmt = rsComments.getString("Comment");
                  String elem = "cmi.comments_from_lms." + idx + ".comment";
                  
                  DMInterface.processSetValue(elem, cmt, true, ioSCOData);

                  String cmtDT = rsComments.getString("CommentDateTime");
                  elem = "cmi.comments_from_lms." + idx + ".timestamp";
                  
                  DMInterface.processSetValue(elem, cmtDT, true, ioSCOData); 

                  String cmtLoc = rsComments.getString("CommentLocation");
                  elem = "cmi.comments_from_lms." + idx + ".location";
                  
                  DMInterface.processSetValue(elem, cmtLoc, true, ioSCOData);
                  
                  idx++;
               }

            }
         }

         stmtSelectItem.close();
         stmtSelectComments.close();
         conn.close();


         String element = new String();

         // Initialize the cmi.credit value
         element = "cmi.credit";
         DMInterface.processSetValue(element, "credit", true, ioSCOData);

         // Initialize the mode 
         element = "cmi.mode";
         DMInterface.processSetValue(element, "normal", true, ioSCOData);

         // Initialize any launch data 
         if ( dataFromLMS != null && ! dataFromLMS.equals("") )
         {
            element = "cmi.launch_data";
            DMInterface.processSetValue(element, dataFromLMS, true, ioSCOData);
         }
         
         // Initialize the scaled passing score 
         if ( masteryScore != null && ! masteryScore.equals("") )
         {
            element = "cmi.scaled_passing_score";
            DMInterface.processSetValue(element, masteryScore, true, ioSCOData);
         }

         // Initialize the time limit action 
         if ( timeLimitAction != null && ! timeLimitAction.equals("") )
         {
            element = "cmi.time_limit_action";
            DMInterface.processSetValue(element, timeLimitAction, true, 
                                        ioSCOData);
         }

         // Initialize the completion_threshold
         if ( completionThreshold != null && ! completionThreshold.equals("") )
         {
            element = "cmi.completion_threshold";
            DMInterface.processSetValue(element, completionThreshold, 
                                        true, ioSCOData);
         }

         // Initialize the max time allowed 
         if ( maxTime != null && !  maxTime.equals("") )
         {
            element = "cmi.max_time_allowed";
            DMInterface.processSetValue(element, maxTime, true, ioSCOData);
         }

         // Initialize the learner preferences based on the SRTE 
         // learner profile information

         // audio_level
         element = "cmi.learner_preference.audio_level";
         DMInterface.processSetValue(element, audLev, true, ioSCOData);

         // audio_captioning
         element = "cmi.learner_preference.audio_captioning";
         DMInterface.processSetValue(element, audCap, true, ioSCOData);

         // delivery_speed
         element = "cmi.learner_preference.delivery_speed";
         DMInterface.processSetValue(element, delSpd, true, ioSCOData);
         
         // language
         element = "cmi.learner_preference.language";
         DMInterface.processSetValue(element, lang, true, ioSCOData);
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
   }


    /**
    * initSCOData
    * 
    * @param ioSCOData  The SCODataManager whose values are initialized.
    * @param iUserID    The user ID
    * @param iUserName  The user name
    * @param iCourseID  The id of the course
    * @param iItemID    The item ID
    * 
    */
   private void initSCOData(SCODataManager ioSCOData, String iUserID,
                            String iUserName, String iCourseID, String iItemID)
   {
      try
      {
         // Get some information from the database
         Connection conn = LMSDatabaseHandler.getConnection();

         PreparedStatement stmtSelectItem;
         PreparedStatement stmtSelectComments;
         PreparedStatement stmtSelectUser;

         String sqlSelectItem = "SELECT * FROM ItemInfo WHERE CourseID " + 
                                "= ? AND ItemIdentifier = ?";
         String sqlSelectComments = "SELECT * FROM SCOComments WHERE " +
                                    "ActivityID = ?";
         String sqlSelectUser = "SELECT * FROM UserInfo WHERE UserID = ?";


         stmtSelectItem = conn.prepareStatement(sqlSelectItem);
         stmtSelectComments = conn.prepareStatement(sqlSelectComments);
         stmtSelectUser = conn.prepareStatement(sqlSelectUser);

         if ( _Debug )
         {
            System.out.println("about to call item in RTEFile");
            System.out.println("userID: " + iUserID);
            System.out.println("courseID: " + iCourseID);
            System.out.println("scoID: " + iItemID);
         }

         ResultSet rsItem = null;
         ResultSet rsUser = null;

         synchronized(stmtSelectItem)
         {
            stmtSelectItem.setString(1, iCourseID);
            stmtSelectItem.setString(2, iItemID);
            rsItem = stmtSelectItem.executeQuery();
         }

         synchronized(stmtSelectUser)
         {
            stmtSelectUser.setString(1, iUserID);
            rsUser = stmtSelectUser.executeQuery();
         }


         if ( _Debug )
         {
            System.out.println("call to itemRS is complete");
         }

         String masteryScore = new String();
         String dataFromLMS = new String();
         String maxTime = new String();
         String timeLimitAction = new String();
         String completionThreshold = new String();
         String audLev = new String();
         String audCap = new String();
         String delSpd = new String();
         String lang = new String();
         int activityID;

         // Get the learner preference values from the database
         if ( rsUser.next() )
         {
            audLev = rsUser.getString("AudioLevel");
            audCap = rsUser.getString("AudioCaptioning");
            delSpd = rsUser.getString("DeliverySpeed");
            lang = rsUser.getString("Language");
         }

         // loop through the result set until we find the correct record
         while ( rsItem.next() )
         {
            /* 
             * checks the item identifier from the record set against the
             * item id to make sure that the id is exactly the same (including
             * case sensitivity) since the SQL call will ignores case
             */
            if ( rsItem.getString("ItemIdentifier").equals(iItemID) )
            {
               String type = rsItem.getString("Type");

               if ( type.equals("sco") || type.equals("asset") )
               {
                  masteryScore = rsItem.getString("MinNormalizedMeasure");
                  dataFromLMS = rsItem.getString("DataFromLMS");
                  maxTime = rsItem.getString("AttemptAbsoluteDurationLimit");
                  timeLimitAction = rsItem.getString("TimeLimitAction");
                  completionThreshold = rsItem.getString("CompletionThreshold");
                  activityID = rsItem.getInt("ActivityID");

                  ResultSet rsComments = null;

                  // Get the comments associated with an activity if any
                  synchronized(stmtSelectComments)
                  {
                     stmtSelectComments.setInt(1, activityID);
                     rsComments = stmtSelectComments.executeQuery();
                  }

                  // Loop through the comments and initialize the SCO data\
                  int idx = 0;
                  while ( rsComments.next() )
                  {
                     String cmt = rsComments.getString("Comment");
                     String elem = "cmi.comments_from_lms." + idx + ".comment";
                     
                     DMInterface.processSetValue(elem, cmt, true, ioSCOData); 
                     String cmtDT = rsComments.getString("CommentDateTime");
                     elem = "cmi.comments_from_lms." + idx + ".timestamp";
                     
                     DMInterface.processSetValue(elem, cmtDT, true, ioSCOData);

                     String cmtLoc = rsComments.getString("CommentLocation");
                     elem = "cmi.comments_from_lms." + idx + ".location";
                     
                     DMInterface.processSetValue(elem, cmtLoc, true, ioSCOData);                                    

                     idx++;
                  }
               }
               // breaking out of the loop if we hit the right ID
               break;
            }
         }

         stmtSelectItem.close();
         conn.close();

         String element = new String();

         // Initialize the learner id
         element = "cmi.learner_id";
         DMInterface.processSetValue(element, iUserID, true, ioSCOData);

         // Initialize the learner name
         element = "cmi.learner_name";
         DMInterface.processSetValue(element, iUserName, true, ioSCOData);

         // Initialize the cmi.credit value
         element = "cmi.credit";
         DMInterface.processSetValue(element, "credit", true, ioSCOData);

         // Initialize the mode 
         element = "cmi.mode";
         DMInterface.processSetValue(element, "normal", true, ioSCOData);

         // Initialize any launch data 
         if ( dataFromLMS != null && ! dataFromLMS.equals("") )
         {
            element = "cmi.launch_data";
            DMInterface.processSetValue(element, dataFromLMS, true, ioSCOData);
         }
         
         // Initialize the scaled passing score 
         if ( masteryScore != null && ! masteryScore.equals("") )
         {
            element = "cmi.scaled_passing_score";
            DMInterface.processSetValue(element, masteryScore, true, ioSCOData);
         }

         // Initialize the time limit action 
         if ( timeLimitAction != null && ! timeLimitAction.equals("") )
         {
            element = "cmi.time_limit_action";
            DMInterface.processSetValue(element, timeLimitAction, true, 
                                        ioSCOData);
         }

         // Initialize the completion_threshold
         if ( completionThreshold != null && ! completionThreshold.equals("") )
         {
            element = "cmi.completion_threshold";
            DMInterface.processSetValue(element, completionThreshold, 
                                        true, ioSCOData);
         }

         // Initialize the max time allowed 
         if ( maxTime != null && !  maxTime.equals("") )
         {
            element = "cmi.max_time_allowed";
            DMInterface.processSetValue(element,  maxTime, true, ioSCOData);
         }

         // Initialize the learner preferences based on the SRTE 
         // learner profile information

         // audio_level
         element = "cmi.learner_preference.audio_level";
         DMInterface.processSetValue(element, audLev, true, ioSCOData);

         // audio_captioning
         element = "cmi.learner_preference.audio_captioning";
         DMInterface.processSetValue(element, audCap, true, ioSCOData);

         // delivery_speed
         element = "cmi.learner_preference.delivery_speed";
         DMInterface.processSetValue(element, delSpd, true, ioSCOData);
         
         // language
         element = "cmi.learner_preference.language";
         DMInterface.processSetValue(element, lang, true, ioSCOData);

      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
   }
}
