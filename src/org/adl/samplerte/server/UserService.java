
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

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.adl.samplerte.util.LMSDBHandler;
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
   private final String SRTEFILESDIR = "SCORM4EDSampleRTE111Files";
   
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
    	  sqlSelectUser = sqlSelectUser + " WHERE Active = 1";
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
      String sqlSetUserInfo = "UPDATE UserInfo SET "
            + "AudioLevel = ?, AudioCaptioning = ?,"
            + "DeliverySpeed = ?, Language = ?, Admin = ? " 
            + "WHERE UserID = ?";  

      try
      {
         stmtSetUserInfo = conn.prepareStatement(sqlSetUserInfo);

         synchronized(stmtSetUserInfo)
         {
            stmtSetUserInfo.setString(1, iUser.mAudioLevel);
            stmtSetUserInfo.setString(2, iUser.mAudioCaptioning);
            stmtSetUserInfo.setString(3, iUser.mDeliverySpeed);
            stmtSetUserInfo.setString(4, iUser.mLanguage);
            stmtSetUserInfo.setBoolean(5, iUser.mAdmin);
            stmtSetUserInfo.setString(6, iUser.mUserID);
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
   public String addUser(UserProfile iUser, String pwd)
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
            stmtInsertUserInfo.setString(5, PasswordHash.createHash(pwd));
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
         String sqlUpdateUser = "delete from UserInfo where UserID = ?";

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
      
      CourseService cs = new CourseService();
      // get user's registered courses
      List<String> courseIds = cs.getRegCourses(iUser);
      // unregister for course
      removeDBRefs(iUser, courseIds);
      
      removeUserFolders(iUser);
      
      return result;
   }

   public boolean loginUser(HttpServletRequest ioRequest, HttpServletResponse ioResponse)
   {
      Connection conn;
      PreparedStatement stmtSelectUser;
      String sqlSelectUser = "SELECT * FROM UserInfo Where UserID = ?";
      String action = null;
      try
      {
         String UserName = "";
         String Password = "";
         String fullName = "";
         String loginName = "";
         String firstName = "";
         String lastName = "";

         UserName = ioRequest.getParameter("uname");
         Password = ioRequest.getParameter("pwd");
         conn = LMSDatabaseHandler.getConnection();
         stmtSelectUser = conn.prepareStatement( sqlSelectUser );

         ResultSet userRS = null;

         synchronized( stmtSelectUser )
         {
            stmtSelectUser.setString( 1, UserName);
            userRS = stmtSelectUser.executeQuery();
         }

         // Verifies that the username was found by checking to see if the result
         // set 'userRS' is empty.  If the username was found, it checks to see if 
         // the entered password is correct.  If the username was not found, the 
         // variable 'action' is changed to indicate this.
         if( (userRS != null) && (userRS.next()) )
         {
            String userID = userRS.getString("UserID");

            if(userID.equals(UserName))
            {
               String passwd = userRS.getString("Password");
               boolean active = userRS.getBoolean("Active");
               firstName = userRS.getString("FirstName");
               lastName = userRS.getString("LastName");
               fullName = lastName + ", " + firstName;
               loginName = firstName + ' ' + lastName;

               if (! active )
               {
                  action = "deactivated";
               }

               // Verifies that the password that was entered is not blank and that 
               // it matches the password found to belong to the username.  If either
               // of these conditions is incorrect, the variable 'action' is changed
               // to indicate this.
               if( ! PasswordHash.validatePassword(Password, passwd) )
               {
                  action = "invalidpwd";      
               }
            }
            else
            {
               action = "invaliduname";
            }

         }
         else
         {
            action = "invaliduname";
         }

         // Verifies that no errors were found with the login by checking to see 
         // if the action variable has been assigned anything.  If 'action' is
         //  null, no errors were found and the session variables 'USERID' and
         //  'RTEADMIN' are set.  
         if ( action == null )
         {
            ioRequest.getSession().setAttribute("USERID", UserName);
            ioRequest.getSession().setAttribute("USERNAME", fullName);
            ioRequest.getSession().setAttribute("LOGINNAME", loginName);

            String admin = userRS.getString("Admin"); 

            // Checks to see if the user has admin rights and sets the 'RTEADMIN'
            // variable accordingly.
            if ( (admin != null) && (admin.equals("1")) ) 
            {
               ioRequest.getSession().setAttribute("RTEADMIN", new String("true"));
            }
            else
            {
               ioRequest.getSession().setAttribute("RTEADMIN", new String("false"));
            }
         }

         userRS.close();
         stmtSelectUser.close();
         conn.close();
      }
      catch(SQLException e)
      {
         System.out.println("login sql exception ");e.printStackTrace();  
         action = "fail";
      }
      catch(Exception e)
      { 
         e.printStackTrace();
         action = "fail";
      } 
      return action == null;
   }
   
   private void removeUserFolders(String userid)
   {
      removeDirectory(new File(File.separator + SRTEFILESDIR + File.separator + userid));
   }
   
   private boolean removeDirectory(File directory)
   {
      if (directory == null) return false;
      if (!directory.exists()) return true;
      if (!directory.isDirectory()) return false;

      String[] list = directory.list();

      // Some JVMs return null for File.list() when the
      // directory is empty.
      if (list != null)
      {
         for (int i = 0; i < list.length; i++)
         {
            File entry = new File(directory, list[i]);

            if (entry.isDirectory())
            {
               if (!removeDirectory(entry)) return false;
            }
            else
            {
               if (!entry.delete()) return false;
            }
         }
      }

      return directory.delete();
   }
   
   private synchronized void removeDBRefs(String iUser, List<String> courseIds)
   {
      Connection conn = LMSDatabaseHandler.getConnection();
      Connection csConn = LMSDBHandler.getConnection();
      PreparedStatement stmtDeleteUserCourse = null;
      PreparedStatement stmtDeleteCourseStatus = null;
      PreparedStatement stmtDeleteCourseObjectives = null;
      try 
      {
         stmtDeleteUserCourse = conn.prepareStatement("DELETE FROM UserCourseInfo WHERE UserID = ?");
         stmtDeleteCourseStatus = csConn.prepareStatement("DELETE FROM CourseStatus WHERE learnerID = ?");
         stmtDeleteCourseObjectives = csConn.prepareStatement("DELETE FROM Objectives WHERE learnerID = ?");

         stmtDeleteUserCourse.setString(1, iUser);
         stmtDeleteUserCourse.executeUpdate();
         
         stmtDeleteCourseObjectives.setString(1, iUser);
         stmtDeleteCourseObjectives.executeUpdate();
         
         stmtDeleteCourseStatus.setString(1, iUser);
         stmtDeleteCourseStatus.executeUpdate();
           
      }
      catch (SQLException sqle)
      {
         System.out.println("error UserService removeDBRefs try");
         sqle.printStackTrace();
      }
      finally 
      {
         try 
         {
            stmtDeleteUserCourse.close();
            stmtDeleteCourseStatus.close();
            stmtDeleteCourseObjectives.close();
         } 
         catch (SQLException e) 
         {
            System.out.println("error UserService removeDBRefs finally");
            e.printStackTrace();
         }
      }
   }

   public String changePassword(String userid, String password) 
   {
      Connection connection = LMSDatabaseHandler.getConnection();
      String result = "true";
      PreparedStatement psnewpwd = null;
      try 
      {
         psnewpwd = connection.prepareStatement(
               "update UserInfo set Password = ? where UserID = ?");
      
      
      synchronized (psnewpwd)
      {
       psnewpwd.setString(1, PasswordHash.createHash(password));
       psnewpwd.setString(2, userid);
       psnewpwd.executeUpdate();
      }
      
      psnewpwd.close();
      } 
      catch (SQLException e) 
      {
         result = "false";
         e.printStackTrace();
      } 
      catch (NoSuchAlgorithmException e) 
      {
         result = "false";
         e.printStackTrace();
      }
      catch (InvalidKeySpecException e) 
      {
         result = "false";
         e.printStackTrace();
      } 
      finally 
      {
         try 
         {
            if (psnewpwd != null) psnewpwd.close();
         } 
         catch (SQLException e) 
         {
            System.out.println("error UserService changePassword finally");
            e.printStackTrace();
            result = "false";
         }
      }
      return result;
   }

   public boolean prepareForDirectLaunch(String userid, String courseid, String webpath) 
   {
      CourseService cs = new CourseService();
      // test if user is registered for course id
      List<String> reg = cs.getRegCourses(userid);
      if (! reg.contains(courseid))
      {
         Vector c = new Vector();
         c.add("UN_" + courseid);
         return Boolean.parseBoolean(cs.updateRegCourses(c, webpath, userid));
      }
      return true;
   }

   public UserAgentInfo updateLRSAccountInfo(HttpServletRequest iRequest) 
   {
      Connection conn = LMSDatabaseHandler.getConnection();
      UserAgentInfo info = new UserAgentInfo();
      
      String userid = iRequest.getParameter("userID");
      String mbox = iRequest.getParameter("mbox");
      mbox = ("".equals(mbox.trim()) || mbox.startsWith("mailto:") ? mbox : "mailto:" + mbox);
      String homepage = iRequest.getParameter("homepage");
      String name = iRequest.getParameter("name");
      String alias = iRequest.getParameter("alias");
      alias = (alias == null || "".equals(alias)) ? UserAgentInfo.AGENT_ALIAS_DEFAULT : alias;
      
      PreparedStatement getAccountInfo = null;
      PreparedStatement insertUserAgentInfo = null;
      PreparedStatement updateUserAgentInfo = null;
      
      ResultSet accInfo = null;
      // if mbox is null or if homepage or name are null.. it's a problem 
      if ( mbox == null || "".equals(mbox.trim()) == ((homepage == null || "".equals(homepage.trim())) || (name == null || "".equals(name.trim()))) )
      {
         iRequest.setAttribute("lrs-error-message", "You need to set an email or homepage and name");
         return info;
      }
      try
      {
         getAccountInfo = conn.prepareStatement("select * from UserAgentInfo where UserID = ? AND AgentAlias = ?");
         insertUserAgentInfo = conn.prepareStatement("insert into UserAgentInfo(UserID, AgentAlias, Mbox, HomePage, AccName) values (?,?,?,?,?)");
         updateUserAgentInfo = conn.prepareStatement("update UserAgentInfo set Mbox = ?, HomePage = ?, AccName = ? where UserID = ? AND AgentAlias = ?");
         
         getAccountInfo.setString(1, userid);
         getAccountInfo.setString(2, alias);
         synchronized (getAccountInfo) 
         {
            accInfo = getAccountInfo.executeQuery();
         }
         
         if (accInfo.next())
         {
            //update
            updateUserAgentInfo.setString(1, mbox);
            updateUserAgentInfo.setString(2, homepage);
            updateUserAgentInfo.setString(3, name);
            updateUserAgentInfo.setString(4, userid);
            updateUserAgentInfo.setString(5, alias);
            synchronized (updateUserAgentInfo) 
            {
               updateUserAgentInfo.executeUpdate();
            }
         }
         else
         {
            //insert -- using just ADL LRS info for now.. 
            // if that changes, this would change
            insertUserAgentInfo.setString(1, userid);
            insertUserAgentInfo.setString(2, alias);
            insertUserAgentInfo.setString(3, mbox);
            insertUserAgentInfo.setString(4, homepage);
            insertUserAgentInfo.setString(5, name);
            synchronized (insertUserAgentInfo) 
            {
               insertUserAgentInfo.executeUpdate();
            }
         }
         // didn't set earlier because i'm just returning info.. 
         // if we got this far, the values should be saved in the db
         info = new UserAgentInfo(userid, alias, mbox, homepage, name);
         iRequest.setAttribute("lrs-ok-message", "LRS Account info successfully saved");
      } 
      catch (SQLException e) 
      {
         iRequest.setAttribute("lrs-error-message", "Error saving your account info. Please try again. If this error presists, please contact us");
         System.out.println("UserService.updateLRSAccountInfo()");
         e.printStackTrace();
      } 
      finally 
      {
         try 
         {
            if (getAccountInfo != null) getAccountInfo.close();
            if (updateUserAgentInfo != null) updateUserAgentInfo.close();
            if (insertUserAgentInfo != null) insertUserAgentInfo.close();
            if (conn != null) conn.close();
         } catch (SQLException e) { }
      }

      return info;
   }
   
   public List<UserAgentInfo> getUserAgentInfos(String userid)
   {
      PreparedStatement getAccountInfo = null;
      Connection conn = LMSDatabaseHandler.getConnection();
      ResultSet accInfo;
      List<UserAgentInfo> infos = new ArrayList<UserAgentInfo>();
      try 
      {
         getAccountInfo = conn.prepareStatement("select * from UserAgentInfo where UserID = ?");
         getAccountInfo.setString(1, userid);
         synchronized (getAccountInfo) 
         { 
            accInfo = getAccountInfo.executeQuery();
         }
         
         while(accInfo.next())
         {
            infos.add(new UserAgentInfo(accInfo.getString("UserID"), accInfo.getString("AgentAlias"), 
                  accInfo.getString("Mbox"), accInfo.getString("HomePage"), accInfo.getString("AccName")));
         }
         
      } 
      catch (SQLException e) 
      {
         System.out.println("UserService.getUserAgentInfos() - sql exception");
         e.printStackTrace();
      } 
      finally 
      {
         try 
         {
            if (getAccountInfo != null) getAccountInfo.close();
            if (conn != null) conn.close();
         } catch (SQLException e) { }
      }
      return infos;
   }
   
   public UserAgentInfo getUserAgentInfo(String userid)
   {
      return getUserAgentInfo(userid, UserAgentInfo.AGENT_ALIAS_DEFAULT);
   }
   
   private UserAgentInfo getUserAgentInfo(String userid, String alias)
   {
      PreparedStatement getAccountInfo = null;
      Connection conn = LMSDatabaseHandler.getConnection();
      ResultSet accInfo;
      UserAgentInfo info = new UserAgentInfo();
      try 
      {
         getAccountInfo = conn.prepareStatement("select * from UserAgentInfo where UserID = ? AND AgentAlias = ?");
         
         getAccountInfo.setString(1, userid);
         getAccountInfo.setString(2, alias);
         synchronized (getAccountInfo) 
         {
            accInfo = getAccountInfo.executeQuery();
         }
         
         if (accInfo.next())
         {
            info.Mbox = accInfo.getString("Mbox");
            info.HomePage = accInfo.getString("HomePage");
            info.AccName = accInfo.getString("AccName");
            info.UserID = userid;
         }
      } 
      catch (SQLException e) 
      {
         System.out.println("UserService.getLRSInfo()");
         e.printStackTrace();
      } 
      finally 
      {
         try
         {
            if (getAccountInfo != null) getAccountInfo.close();
            if (conn != null) conn.close();
         } catch (SQLException e) { }
      }
      
      return info;
   }
}