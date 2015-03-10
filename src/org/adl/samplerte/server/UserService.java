
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

package org.adl.samplerte.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import org.adl.samplerte.util.LMSDatabaseHandler;


/**
 * <strong>Filename:</strong> UserService.java<br><br>
 *
 * <strong>Description:</strong><br>
 * The UserService class handles user manipulation requests of the Sample RTE.
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM 2004 4th Edition Sample RTE<br>
 * <br>
 *  
 * <strong>References:</strong><br>
 * <ul>
 *     <li>IMS SS Specification
 *     <li>SCORM 2004 4th Edition
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class UserService 
{
   /**
    * This constructor creates a UserService object.
    */
   UserService() 
   {
      // Default constructor
   }
   
   /**
    * Returns a list of users registered in the Sample RTE
    * @param iOnlyActive - return a list of inactive or active users
    * @return List of users registered in the system
    */
   public Vector getUsers(boolean iOnlyActive)
   {
      Connection conn; 
      PreparedStatement stmtSelectUser;      
      Vector mUserVector = new Vector();
      String sqlSelectUser = "SELECT * FROM UserInfo";
            
      if (iOnlyActive == true)
      {
    	  sqlSelectUser = sqlSelectUser + " WHERE Active = yes";
      }
      
      try
      {
         conn = LMSDatabaseHandler.getConnection();
         stmtSelectUser = conn.prepareStatement(sqlSelectUser);
         ResultSet userRS = null;

         // returns a list of user information
         userRS = stmtSelectUser.executeQuery();

         // Loops through the result set of all users and adds them to the return
         // Vector.
         while (userRS.next())
         {
            UserProfile mUserProfile = new UserProfile();
            mUserProfile.mUserID = userRS.getString("UserID");
            mUserProfile.mLastName = userRS.getString("LastName");
            mUserProfile.mFirstName = userRS.getString("FirstName");
            mUserVector.add(mUserProfile);
         }

         userRS.close();
         stmtSelectUser.close();
         conn.close();
      }
      catch (Exception e)
      {
         System.out.println("This failed in UserService::getUsers()");
         e.printStackTrace();
      }

      return mUserVector;
   }
 
   /**
    * Returns a UserProfile for the desired user
    * @param iUserID - the ID of the desired user
    * @return UserProfile object for the desired user
    */
   public UserProfile getUser(String iUserID)
   {

      Connection conn; 
      PreparedStatement stmtSelectUser;
      String sqlSelectUser = "SELECT * FROM UserInfo WHERE UserId = ?";
      UserProfile mUserProfile = new UserProfile();

      try
      {
         conn = LMSDatabaseHandler.getConnection();
         stmtSelectUser = conn.prepareStatement(sqlSelectUser);
         ResultSet userRS = null;

         synchronized(stmtSelectUser)
         {
            stmtSelectUser.setString(1, iUserID);
            userRS = stmtSelectUser.executeQuery();
         }

         while (userRS.next())
         {
            mUserProfile.mUserID = userRS.getString("UserID");
            mUserProfile.mLastName = userRS.getString("LastName");
            mUserProfile.mFirstName = userRS.getString("FirstName");
            mUserProfile.mPassword = userRS.getString("Password");
            mUserProfile.mAudioLevel = userRS.getString("AudioLevel");
            mUserProfile.mAudioCaptioning = userRS.getString("AudioCaptioning");
            mUserProfile.mDeliverySpeed = userRS.getString("DeliverySpeed");
            mUserProfile.mLanguage = userRS.getString("Language");
            mUserProfile.mAdmin = userRS.getBoolean("Admin");
         }

         userRS.close();
         stmtSelectUser.close();
         conn.close();
      }
      catch (Exception e)
      {
         System.out.println("Error in UserService::getUser()");
         e.printStackTrace(); 
      }

      return mUserProfile;
   }

   /**
    * This method is used to update the user information.  
    * @param iUser - the UserProfile object of a specific user
    * @return String - indicates whether the update was successful
    */
   public String updateUser(UserProfile iUser)
   {
      String result = "true";
      Connection conn = LMSDatabaseHandler.getConnection();
      PreparedStatement stmtSetUserInfo;
      String sqlSetUserInfo = "UPDATE UserInfo SET Password = ?,"
                              + "AudioLevel = ?, AudioCaptioning = ?,"
                              + "DeliverySpeed = ?, Language = ?, Admin = ? " 
                              + "WHERE UserID = ?";  

      try
      {
         stmtSetUserInfo = conn.prepareStatement(sqlSetUserInfo);

         synchronized(stmtSetUserInfo)
         {
            stmtSetUserInfo.setString(1, iUser.mPassword);
            stmtSetUserInfo.setString(2, iUser.mAudioLevel);
            stmtSetUserInfo.setString(3, iUser.mAudioCaptioning);
            stmtSetUserInfo.setString(4, iUser.mDeliverySpeed);
            stmtSetUserInfo.setString(5, iUser.mLanguage);
            stmtSetUserInfo.setBoolean(6, iUser.mAdmin);
            stmtSetUserInfo.setString(7, iUser.mUserID);
            stmtSetUserInfo.executeUpdate();
         }

         conn.close();
      }
      catch (Exception e)
      {
         System.out.println("error in db update in UserService::updateUser()");
         result = "false";
         e.printStackTrace();
      }

      return result;
   }
   
   /**
    * Adds a user to the Sample Run Time Environment
    * @param iUser - UserProfile object of the desired user to be added to the system
    * @return String describing the success of the operation (true or false)
    */
   public String addUser(UserProfile iUser)
   {

      String result = "true";

      Connection conn; 
      PreparedStatement stmtInsertUserInfo;
      String sqlInsertUserInfo = 
               "INSERT INTO UserInfo VALUES (?, ?, ?, ?, ?,'1','1','0','1','')";

      try
      {
         conn = LMSDatabaseHandler.getConnection();
         stmtInsertUserInfo = conn.prepareStatement(sqlInsertUserInfo);

         synchronized(stmtInsertUserInfo)
         {
            stmtInsertUserInfo.setString(1, iUser.mUserID);
            stmtInsertUserInfo.setString(2, iUser.mLastName);
            stmtInsertUserInfo.setString(3, iUser.mFirstName);
            stmtInsertUserInfo.setBoolean(4, iUser.mAdmin);
            stmtInsertUserInfo.setString(5, iUser.mPassword);
            stmtInsertUserInfo.executeUpdate();
         }

         stmtInsertUserInfo.close();
         conn.close();
      }
      catch (Exception e)
      {
         System.out.println("error updating db in UserService::addUser()");
         result = "false";
         e.printStackTrace(); 
      }

      return result;
   }
   
   /**
    * Deletes the undesired user
    * @param iUser - ID of the user to be deleted from the Sample RTE
    * @return String representing the success of the operation (true or false)
    */
   public String deleteUser(String iUser)
   {
      String result = "true";
      try
      {
         Connection conn;
         PreparedStatement stmtUpdateUser;
         conn = LMSDatabaseHandler.getConnection();

         // The SQL string is created and converted to a prepared statement.
         String sqlUpdateUser = "UPDATE UserInfo set Active = no where UserID = ?";
         stmtUpdateUser = conn.prepareStatement(sqlUpdateUser);

         synchronized(stmtUpdateUser)
         {
            stmtUpdateUser.setString(1, iUser);
            stmtUpdateUser.executeUpdate();
         }

         stmtUpdateUser.close();
         conn.close();
      }
      catch (Exception e)
      {
         result = "false";
         System.out.println("error updating db in UserService::deleteUser()");
         e.printStackTrace();
      }

      return result;
   }
}