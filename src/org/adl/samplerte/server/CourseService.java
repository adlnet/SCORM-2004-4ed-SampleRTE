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

import gov.adlnet.xapi.client.StatementClient;
import gov.adlnet.xapi.model.Account;
import gov.adlnet.xapi.model.Agent;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementResult;
import gov.adlnet.xapi.model.Verbs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.adl.samplerte.util.Config;
import org.adl.samplerte.util.LMSDBHandler;
import org.adl.samplerte.util.LMSDatabaseHandler;
import org.adl.samplerte.util.RTEFileHandler;
import org.adl.sequencer.ADLSeqUtilities;
import org.adl.sequencer.SeqActivityTree;
import org.adl.util.decode.decodeHandler;
import org.adl.validator.util.ResultCollection;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory; 
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.joda.time.DateTime;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;

/**
 * <strong>Filename: </strong>CourseService.java <br>
 * <br>
 * 
 * <strong>Description: </strong> <br>
 * The CourseService class handles access to course information in the Sample
 * RTE database. In addition, it handles inserts and updates of new information.
 * <br>
 * <br>
 * 
 * <strong>Design Issues: </strong> <br>
 * This implementation is intended to be used by the SCORM 2004 Sample RTE <br>
 * <br>
 * 
 * <strong>References: </strong> <br>
 * <ul>
 * <li>IMS SS Specification</li>
 * <li>SCORM 2004</li>
 * </ul>
 * 
 * @author ADL Technical Team
 */
/**
 * @author creightt
 *
 */
public class CourseService
{
   private final class SortStmtByLatest implements Comparator<Statement> 
   {
      @Override
      public int compare(Statement o1, Statement o2) 
      {
         // i want reverse newest first
         return -1 * new DateTime(o1.getTimestamp()).compareTo(new DateTime(o2.getTimestamp()));
      }
   }

   /**
    * The string containing the name of the SampleRTEFiles directory.
    */
   private static final String SRTEFILESDIR = "SCORM4EDSampleRTE111Files";
   
   /**
    * The userID of the student.
    */
   String mUserID = "";

   /**
    * The boolean indicating whether or not to perfom online validation
    */
   boolean mOnlineValidation = true;
   
   /**
    * The string containing the name of the file to be imported. 
    */
   String mCourseFileName = "";
   
   /**
    * Default Constructor
    */
   CourseService()
   {
      // default constructor
   }

   /**
    * Returns a List of courses entered in the system
    * @param iSortType - field to sort by
    * @param iSortOrder - order to sort by
    * @return The list of courses
    */
   public Vector getCourses(String iSortType, String iSortOrder)
   {
      String tempUserID = "";
      Vector tempVector = getCourses(tempUserID, iSortType, iSortOrder);
      return tempVector;
   }
   
   /**
    *  This method will return a list of all active courses currently
    *  available in the RTE
    *  
    * @param iUserID - the current user
    * @param iRegSortType - type of sort for registered courses
    * @param iRegSortOrder - order of sort for registered courses
    * @param iUnregSortType - type of sort for registered courses
    * @param iUnregSortOrder - order of sort for registered courses
    * @return - a Vector of courses
    */
   public Vector getManagedCourses(String iUserID, String iRegSortType, String iRegSortOrder, String iUnregSortType, String iUnregSortOrder)
   {
      Vector courseList = new Vector();
      
      // Get all registered courses
      Vector courses = getCourses(iUserID, iRegSortType, iRegSortOrder);   
      Iterator courseIter = courses.iterator();
      while ( courseIter.hasNext() )
      {
         CourseData cd = (CourseData)courseIter.next();  
         
         CourseData statusCd = new CourseData();
         
         // Check registration status
         cd.mRegistered = true;
         
         courseList.add(cd);
      }
      
      // Get all unregistered courses
      Vector unregCourses = getUnregisteredCourses(iUserID, iUnregSortType, iUnregSortOrder);
      Iterator unregIter = unregCourses.iterator();
      while ( unregIter.hasNext() )
      {
         CourseData cd = (CourseData)unregIter.next();  
         
         cd.mRegistered = false;
         
         courseList.add(cd);         
      }
      
      return courseList;
   }
   
   /**
    * This method will return a vector of all unregistered courses
    * 
    * @param iUserID - the current user
    * @param iSortType - field to be sorted by
    * @param iSortOrder - order to be sorted by
    * @return a Vector or courses
    */
   public Vector getUnregisteredCourses(String iUserID, String iSortType, String iSortOrder)
   {
      Vector courses = new Vector();
      Vector allCourses = getCourses(iSortType, iSortOrder);
      List regCourses = getRegCourses(iUserID);
      
      Iterator iter = allCourses.iterator();
      while ( iter.hasNext() )
      {
         CourseData cd = (CourseData)iter.next();
         if ( !regCourses.contains(cd.mCourseID) )
         {
            courses.add(cd);
         }
      }
      return courses;
   }
   
   /**
    * This method is used to return a <code>Vector</code> of CourseData
    * objects. These objects correspond to the courses that are currently
    * imported into the Sample RTE course catalog
    * @param iUserID - The ID of the desired user
    * @param iSortType - field to sort by
    * @param iSortOrder - order to sort by
    * @return <code>Vector</code> of CourseData objects corresponding to the
    *         Sample RTE's course catalog
    */
   public Vector getCourses(String iUserID, String iSortType, String iSortOrder)
   {
      // Get all of the course information out of the database
      Connection conn = LMSDatabaseHandler.getConnection();

      String sortType = (iSortType.equals("timestamp")) ? "ImportDateTime" : "CourseTitle";

      PreparedStatement stmtGetCourses;
      String sqlGetCourses = "";
      mUserID = iUserID;
      if( mUserID.equals("") )
      {        
         sqlGetCourses = "SELECT * FROM CourseInfo WHERE Active = 1 ORDER BY " + sortType + " " + iSortOrder;
      }
      else
      {      
         sqlGetCourses = "SELECT CourseInfo.CourseID, CourseInfo.CourseTitle, CourseInfo.ImportDateTime, " 
            + "CourseInfo.Start, CourseInfo.TOC, UserCourseInfo.SuspendAll  FROM "
            + "CourseInfo, UserCourseInfo WHERE UserCourseInfo.UserID = ? AND "
            + "CourseInfo.CourseID = UserCourseInfo.CourseID AND CourseInfo.Active = 1 "
            + "ORDER BY CourseInfo." + sortType + " " + iSortOrder;   
      }
      Vector courses = new Vector();

      try
      {
         stmtGetCourses = conn.prepareStatement(sqlGetCourses);
         ResultSet rsCourses = null;

         synchronized( stmtGetCourses )
         {
            if( !( mUserID.equals("") ) )
            {

               stmtGetCourses.setString(1, mUserID);
            }            
            rsCourses = stmtGetCourses.executeQuery();
         }

         // Loop through the dataset and create CourseData Objects
         // Add them to a Vector that will be sent to a view.
         while( rsCourses.next() )
         {
            CourseData cd = new CourseData();
            cd.mCourseID = rsCourses.getString("CourseID");
            cd.mCourseTitle = rsCourses.getString("CourseTitle");
            cd.mImportDateTime = rsCourses.getString("ImportDateTime");
            cd.mStart = rsCourses.getBoolean("Start");
            cd.mTOC = rsCourses.getBoolean("TOC");
            if( !( mUserID.equals("") ) )
            {
               cd.mSuspend = rsCourses.getBoolean("SuspendAll");
            }
            
            courses.add(cd);
         }

         // Clean up the database handler connections
         rsCourses.close();
         stmtGetCourses.close();
         conn.close();
      }
      catch( Exception e )
      {
         e.printStackTrace();
      }

      return courses;
   }
   
   /**
    * This method is used to return a Vector of SCOData objects. These objects
    * correspond to the SCOs that are currently in the RTE's course catalog <br>
    * 
    * @param iCourseID -
    *           The String course identifier, internal to the Sample RTE. The
    *           SCOData object returned by this method will be the SCOs
    *           associated with this course identifier.
    * @return Vector SCOData objects corresponding to the Sample RTE's SCOs in
    *         the course catalog
    */
   public Vector getSCOs(String iCourseID)
   {
      // Get all of the course information out of the database
      Connection conn = LMSDatabaseHandler.getConnection();

      PreparedStatement stmtGetSCOs;

      String sqlGetSCOs = "SELECT * FROM ItemInfo WHERE CourseID = ? AND " + "Type = 'sco'";

      Vector scos = new Vector();

      try
      {
         stmtGetSCOs = conn.prepareStatement(sqlGetSCOs);
         ResultSet rsSCOs = null;

         synchronized( stmtGetSCOs )
         {
            stmtGetSCOs.setString(1, iCourseID);
            rsSCOs = stmtGetSCOs.executeQuery();
         }

         // Loop through the dataset and create SCOData Objects
         // Add them to a Vector that will be sent to a view.
         while( rsSCOs.next() )
         {
            SCOData sd = new SCOData();
            sd = getSCO(rsSCOs.getInt("ActivityID"));
            scos.add(sd);
         }

         // Clean up the database handler connections
         rsSCOs.close();
         stmtGetSCOs.close();
         conn.close();
      }
      catch( Exception e )
      {
         e.printStackTrace();
      }

      return scos;
   }

   /**
    * This method is used to return a SCOData object. This object corresponds to
    * the current SCO being modified. <br>
    * 
    * @param iActivityID -
    *           The unique identifier (internal to the Sample RTE) used to
    *           identify a single SCO in the Sample RTE.
    * @return SCOData - object corresponding to the current SCO being modified
    */
   public SCOData getSCO(int iActivityID)
   {
      // Get all of the course information out of the database
      Connection conn = LMSDatabaseHandler.getConnection();

      PreparedStatement stmtGetSCO;

      String sqlGetSCO = "SELECT * FROM ItemInfo WHERE ActivityID = ?";

      SCOData sco = new SCOData();

      try
      {
         stmtGetSCO = conn.prepareStatement(sqlGetSCO);
         ResultSet rsSCO = null;

         synchronized( stmtGetSCO )
         {
            stmtGetSCO.setInt(1, iActivityID);
            rsSCO = stmtGetSCO.executeQuery();
         }

         while( rsSCO.next() )
         {
            sco.mActivityID = rsSCO.getString("ActivityID");
            sco.mItemTitle = rsSCO.getString("Title");
            sco.mComment = new Vector();
            sco.mDateTime = new Vector();
            sco.mLocation = new Vector();

            String sqlGetComments = "SELECT * FROM SCOComments WHERE " + "ActivityID = " + sco.mActivityID;

            PreparedStatement stmtGetComments;
            stmtGetComments = conn.prepareStatement(sqlGetComments);
            ResultSet rsComments = null;

            synchronized( stmtGetComments )
            {
               rsComments = stmtGetComments.executeQuery();
            }

            while( rsComments.next() )
            {
               sco.mComment.add(rsComments.getString("Comment"));
               sco.mDateTime.add(rsComments.getString("CommentDateTime"));
               sco.mLocation.add(rsComments.getString("CommentLocation"));
            }

            // Cleanup the 'comments' connections
            rsComments.close();
            stmtGetComments.close();

         }

         // Cleanup the connections
         rsSCO.close();
         stmtGetSCO.close();
         conn.close();
      }
      catch( Exception e )
      {
         e.printStackTrace();
      }

      return sco;
   }

   /**
    * This method is used to update SCOData object and its associated comments.
    * <br>
    * 
    * @param iActivityID -
    *           The unique identifier (internal to the Sample RTE) used to
    *           identify a single SCO in the Sample RTE.
    * @param iComments -
    *           A '[.]' delimited list of comments_from_lms Strings to be used
    *           to initialize the SCO when it is launched
    * @param iUpdate - 
    *          String representation of whether this is an update (true or false)
    * @param iLocations - 
    *          The locations
    * @return String - indicates whether the update was successful
    */
   public String updateSCO(int iActivityID, String iComments, String iUpdate, String iLocations)
   {
      String result = "true";

      // Get all of the course information out of the database
      Connection conn = LMSDatabaseHandler.getConnection();

      PreparedStatement stmtDeleteComment;
      PreparedStatement stmtSetComment;

      String sqlSetComment = "INSERT INTO SCOComments(ActivityID, Comment,  "
         + "CommentDateTime, CommentLocation) VALUES(?,?,?,?)";

      String sqlDeleteComment = "DELETE FROM SCOComments WHERE ActivityID " + "= ?";

      Vector comments = new Vector();
      comments = getCommentVector(iComments);

      Vector locations = new Vector();
      locations = getCommentVector(iLocations);

      try
      {
         stmtSetComment = conn.prepareStatement(sqlSetComment);

         if( iUpdate.equals("false") )
         {
            // Clean out old comments then insert the new
            stmtDeleteComment = conn.prepareStatement(sqlDeleteComment);

            synchronized( stmtDeleteComment )
            {
               stmtDeleteComment.setInt(1, iActivityID);
               stmtDeleteComment.executeUpdate();
            }

            stmtDeleteComment.close();
         }

         DateFormat date = DateFormat.getDateTimeInstance();

         if( iComments != null && ( !iComments.equals("") ) )
         {

            for( int i = 0; i < comments.size(); i++ )
            {
               if( locations.elementAt(i).equals("") )
               {
                  synchronized( stmtSetComment )
                  {
                     stmtSetComment.setInt(1, iActivityID);
                     stmtSetComment.setString(2, (String)comments.elementAt(i));
                     stmtSetComment.setString(3, date.format(new Date()));
                     stmtSetComment.executeUpdate();
                  }
               }
               else
               {
                  synchronized (stmtSetComment)
                  {
                     stmtSetComment.setInt(1, iActivityID);
                     stmtSetComment.setString(2, (String)comments.elementAt(i));
                     stmtSetComment.setString(3, date.format(new Date()));
                     stmtSetComment.setString(4, (String)locations.elementAt(i));
                     stmtSetComment.executeUpdate();
                  }
               }
            }

            stmtSetComment.close();
         }

         conn.close();
      }
      catch (Exception e)
      {
         result = "false";
         e.printStackTrace();
      }

      return result;
   }

   /**
    * This method deletes courses.
    * @param iCourses  List of courses selected to be deleted
    * @return String representation of the success of the deletion (true or false)
    */
   public String deleteCourse(Vector iCourses)
   {
      String result = "true";
      String courseID = "";
      Connection conn;
      PreparedStatement stmtUpdateCourse;
      String sqlUpdateCourse = "UPDATE CourseInfo set Active = 0 where CourseID = ?";
      
      try
      {
         conn = LMSDatabaseHandler.getConnection();

         stmtUpdateCourse = conn.prepareStatement(sqlUpdateCourse);
         for( int i = 0; i < iCourses.size(); i++ )
         {
           	courseID = (String) iCourses.elementAt(i);
           	if (courseID.startsWith("RE_") || (courseID.startsWith("UN_")))
           	{
           		courseID = courseID.substring(3, courseID.length());
           		synchronized (stmtUpdateCourse)
                   {                  
                      stmtUpdateCourse.setString(1, courseID);
                      stmtUpdateCourse.executeUpdate();
                   }
           	}
         }
         stmtUpdateCourse.close();
         conn.close();

      }
      catch (Exception e)
      {
         result = "false";
         e.printStackTrace();
      }

      return result;
   }

   /**
    * This function will return the list of courses for which a user is 
    * registered.
    * @param iUserID ID of the user whose courses shall be returned
    * @return String list of the courses
    */
   public List<String> getRegCourses(String iUserID)
   {
      List<String> userCourses = new ArrayList<String>();
      Connection conn;
      PreparedStatement stmtRegSelectUserCourse;
      String sqlSelectUserCourse = "SELECT * FROM UserCourseInfo WHERE UserID = ?";

      try
      {
         conn = LMSDatabaseHandler.getConnection();
         stmtRegSelectUserCourse = conn.prepareStatement(sqlSelectUserCourse);

         mUserID = iUserID;

         ResultSet userCourseRS = null;

         // returns a list of all courses for which a user is registered
         synchronized (stmtRegSelectUserCourse)
         {
            stmtRegSelectUserCourse.setString(1, mUserID);
            userCourseRS = stmtRegSelectUserCourse.executeQuery();
         }

         while( userCourseRS.next() )
         {
            userCourses.add(userCourseRS.getString("CourseID"));
         }

         userCourseRS.close();
         stmtRegSelectUserCourse.close();
         conn.close();
      }
      catch( Exception e )
      {
         e.printStackTrace();
      }

      return userCourses;

   }

   /**
    * Updates the list of courses for which a chosen user is registered.
    * @param iCourseIDs The list of courses that are selected
    * @param iPath The web path
    * @param iUserID The ID of the user.
    * @return String representation of the success of this action (true or false)
    */
   public String updateRegCourses(Vector iCourseIDs, String iPath, String iUserID)
   {
      String result = "true";
      mUserID = iUserID;

      Connection conn;
      Connection csConn;

      PreparedStatement stmtSelectCourse;
      PreparedStatement stmtSelectUserCourse;
      PreparedStatement stmtInsertUserCourse;
      PreparedStatement stmtInsertCourseStatus;
      PreparedStatement stmtDeleteUserCourse;
      PreparedStatement stmtDeleteCourseStatus;
      PreparedStatement stmtDeleteCourseObjectives;
      PreparedStatement stmtInsertItemStatus;
      PreparedStatement stmtDeleteItemStatus;

      String sqlSelectUserCourse = "SELECT * FROM UserCourseInfo WHERE UserID = ? AND CourseID = ?";

      String sqlSelectCourse = "SELECT * FROM UserCourseInfo WHERE UserID = ?";

      String sqlInsertUserCourse = "INSERT INTO UserCourseInfo (UserID, CourseID) VALUES(?,?)";

      String sqlDeleteUserCourse = "DELETE FROM UserCourseInfo WHERE UserID = ? AND CourseID = ?";

      String sqlInsertCourseStatus = "INSERT INTO CourseStatus (learnerID, courseID) VALUES(?,?)";

      String sqlDeleteCourseStatus = "DELETE FROM CourseStatus WHERE learnerID = ? AND courseID = ?";
      
      String sqlInsertItemStatus = "INSERT INTO ItemStatus (learnerID, courseID, activityID) VALUES(?,?,?)";

      String sqlDeleteItemStatus = "DELETE FROM ItemStatus WHERE learnerID = ? AND courseID = ?";

      String sqlDeleteCourseObjectives = "DELETE FROM Objectives WHERE learnerID = ? AND scopeID = ?";
      
      try
      {
         conn = LMSDatabaseHandler.getConnection();
         csConn = LMSDatabaseHandler.getConnection(LMSDatabaseHandler.GLOBAL_OBJECTIVES);
         stmtSelectCourse = conn.prepareStatement(sqlSelectCourse);
         stmtSelectUserCourse = conn.prepareStatement(sqlSelectUserCourse);
         stmtInsertUserCourse = conn.prepareStatement(sqlInsertUserCourse);
         stmtDeleteUserCourse = conn.prepareStatement(sqlDeleteUserCourse);
         stmtInsertCourseStatus = csConn.prepareStatement(sqlInsertCourseStatus);
         stmtDeleteCourseStatus = csConn.prepareStatement(sqlDeleteCourseStatus);
         stmtInsertItemStatus = csConn.prepareStatement(sqlInsertItemStatus);
         stmtDeleteItemStatus = csConn.prepareStatement(sqlDeleteItemStatus);
         
         stmtDeleteCourseObjectives = csConn.prepareStatement(sqlDeleteCourseObjectives);
         SeqActivityTree mySeqActivityTree;

         //String selectedCourses = "|";
         
         List unregisterCourses = new ArrayList();
         

         RTEFileHandler fileHandler = new RTEFileHandler();

         String regTestString = "UN_";

         // Process the list of parameters and register for the course if applicable
         
         for( int i = 0; i < iCourseIDs.size(); i++ )
         {
            String paramName = (String)iCourseIDs.elementAt(i);
                       
            // This is an Unregister request, put this in the unregister list
            if ( paramName.indexOf("RE_") != -1 )
            {
               unregisterCourses.add(paramName.substring(3, paramName.length()));
            }
                        
            int locSkillId = paramName.indexOf(regTestString);

            if( locSkillId != -1 )
            {
               String courseID = paramName.substring(3, paramName.length());
               
               ResultSet userCourseRS = null;

               synchronized( stmtSelectUserCourse )
               {
                  stmtSelectUserCourse.setString(1, mUserID);
                  stmtSelectUserCourse.setString(2, courseID);
                  userCourseRS = stmtSelectUserCourse.executeQuery();
               }

               if( userCourseRS.next() == false )
               {
                  synchronized( stmtInsertUserCourse )
                  {
                     stmtInsertUserCourse.setString(1, mUserID);
                     stmtInsertUserCourse.setString(2, courseID);
                     stmtInsertUserCourse.executeUpdate();
                  }

                  synchronized( stmtInsertCourseStatus )
                  {
                     stmtInsertCourseStatus.setString(1, mUserID);
                     stmtInsertCourseStatus.setString(2, courseID);
                     stmtInsertCourseStatus.executeUpdate();
                  }
                  
                  // see if this is a fake course... if so we need to create item stati
                  synchronized( stmtInsertItemStatus )
                  {
                     CourseData cd = getCourseData(courseID);
                     if (! cd.mTOC && ! cd.mStart)
                     {
                        for (ItemData id : cd.items) {
                           stmtInsertItemStatus.setString(1, mUserID);
                           stmtInsertItemStatus.setString(2, courseID);
                           stmtInsertItemStatus.setInt(3, id.activityID);
                           stmtInsertItemStatus.executeUpdate();
                        }
                     }
                  }

                  String tree = iPath + "CourseImports" + File.separator + courseID + File.separator + "serialize.obj";
                  FileInputStream in = null;
                  ObjectInputStream ie = null;
                  boolean process_serialize = true;
                  try {
                     in = new FileInputStream(tree);
                     ie = new ObjectInputStream(in);
                     
                  }
                  catch (FileNotFoundException fnfe) {
                     process_serialize = false;
                     try {
                        ie.close();
                     } catch (Exception e) {}
                     try {
                        in.close();
                     } catch (Exception e) {}
                  }
                  
                  if (process_serialize)
                  {
                     mySeqActivityTree = (SeqActivityTree)ie.readObject();
                     ie.close();
                     in.close();
                     // Set the student ID
                     mySeqActivityTree.setLearnerID(mUserID);
   
                     String scope = mySeqActivityTree.getScopeID();
   
                     // Get any global objectives identified in the manifest
                     // from the activity tree.
                     Vector theGobalObjectiveList = mySeqActivityTree.getGlobalObjectives();
   
                     if( theGobalObjectiveList != null )
                     {
                        ADLSeqUtilities.createGlobalObjs(mUserID, scope, theGobalObjectiveList);
                     }
                     String userDir = System.getProperty("user.home") + File.separator + SRTEFILESDIR + File.separator + mUserID + File.separator
                        + courseID;
                     File theRTESCODataDir = new File(userDir);
   
                     // The course directory should not exist yet
                     if( !theRTESCODataDir.isDirectory() )
                     {
                        theRTESCODataDir.mkdirs();
                     }
   
                     //Serialize the activity tree out to the user directory
                     String sampleRTERoot = System.getProperty("user.home") + File.separator + SRTEFILESDIR;
   
                     String serializeFileName =  sampleRTERoot + File.separator + mUserID + File.separator + courseID
                        + File.separator + "serialize.obj";
                     FileOutputStream outFile = new FileOutputStream(serializeFileName);
                     ObjectOutputStream s = new ObjectOutputStream(outFile);
                     s.writeObject(mySeqActivityTree);
                     s.flush();
                     s.close();
                     outFile.close();
                  }

                  userCourseRS.close();
               }
            }
         }

         Iterator unregIter = unregisterCourses.iterator();
         while( unregIter.hasNext() )
         {
            String courseID = unregIter.next().toString();
            
            ResultSet userCourseRS = null;
            
            synchronized( stmtSelectUserCourse )
            {
               stmtSelectUserCourse.setString(1, mUserID);
               stmtSelectUserCourse.setString(2, courseID);
               userCourseRS = stmtSelectUserCourse.executeQuery();
            }

            // Look for courses that are not selected for the user
            if( userCourseRS.next() == true )
            {
               synchronized( stmtDeleteUserCourse )
               {
                  stmtDeleteUserCourse.setString(1, mUserID);
                  stmtDeleteUserCourse.setString(2, courseID);
                  stmtDeleteUserCourse.executeUpdate();
               }
               synchronized( stmtDeleteCourseStatus )
               {
                  // if adlseq:objectivesGlobalToSystem = "false" in the manifest related to this course
                  // scopeID will be == to courseID and should be removed upon deletion of that course
                  stmtDeleteCourseObjectives.setString(1, mUserID);
                  stmtDeleteCourseObjectives.setString(2, courseID);
                  stmtDeleteCourseObjectives.executeUpdate();

                  stmtDeleteCourseStatus.setString(1, mUserID);
                  stmtDeleteCourseStatus.setString(2, courseID);
                  stmtDeleteCourseStatus.executeUpdate();
                  
                  // remove item status if any existed (should work)
                  stmtDeleteItemStatus.setString(1, mUserID);
                  stmtDeleteItemStatus.setString(2, courseID);
                  stmtDeleteItemStatus.executeUpdate();
               }
               fileHandler.deleteCourseFiles(courseID, mUserID);
            }
         }
         stmtSelectCourse.close();
         stmtSelectUserCourse.close();
         stmtInsertUserCourse.close();
         stmtDeleteUserCourse.close();
         stmtInsertCourseStatus.close();
         stmtInsertItemStatus.close();
         stmtDeleteItemStatus.close();
         stmtDeleteCourseStatus.close();
         conn.close();
         csConn.close();
      }
      catch( Exception e )
      {
         e.printStackTrace();
         result = "false";
      }

      return result;
   }
   
   /**
    * Resets the list of courses for which a chosen user is registered.
    * @param iCourseIDs The list of courses that are selected
    * @param iPath The web path
    * @param iUserID The ID of the user.
    * @return String representation of the success of this action (true or false)
    */
   public String resetCourses(Vector iCourseIDs, String iPath, String iUserID)
   { 
      String result = "true";
      Vector tempIDs = new Vector();
      
      // Unregister for the course
      result = updateRegCourses(iCourseIDs, iPath, iUserID);
      
      // If unregistration failed, exit now
      if ( result.equals("false") )
      {
         return result;
      }
      
      // Switch status of courses in list from registered to unregistered
      for ( int i = 0; i < iCourseIDs.size(); i++ )
      {
         tempIDs.add(iCourseIDs.get(i).toString().replaceFirst("RE_", "UN_"));
      }
      
      // Register for the course
      result = updateRegCourses(tempIDs, iPath, iUserID);

      return result;
   }

   /**
    * Shows the status of a selected course for a specific user
    * @param iCourseID The course ID
    * @param iUserID ID of the user
    * @return CourseData info.
    */
   public CourseData showCourseStatus(String iCourseID, String iUserID)
   {
      String courseID = iCourseID;
      mUserID = iUserID;
      CourseData cs = new CourseData();

      try
      {
         Connection conn;
         PreparedStatement stmtSelectStatus;
         conn = LMSDBHandler.getConnection();

         //Query String to obtain Courses
         String sqlSelectStatus = "SELECT * FROM CourseStatus where " + "learnerID = ? AND courseID = ?";

         stmtSelectStatus = conn.prepareStatement(sqlSelectStatus);

         ResultSet statusRS;

         synchronized( stmtSelectStatus )
         {
            stmtSelectStatus.setString(1, mUserID);
            stmtSelectStatus.setString(2, courseID);
            statusRS = stmtSelectStatus.executeQuery();
         }

         while( statusRS.next() )
         {
            cs.mSatisfied = statusRS.getString("satisfied");
            cs.mMeasure = statusRS.getString("measure");
            cs.mCompleted = statusRS.getString("completed");
            cs.mProgMeasure = statusRS.getString("progmeasure");
         }

         statusRS.close();

         stmtSelectStatus.close();

         LMSDBHandler.closeConnection();
      }

      catch( Exception e )
      {
         e.printStackTrace();
      }
      return cs;
   }

   /**
    * This method return the name of the course being imported.
    * 
    * @return a String containing the name of the file being imported
    */
   public String getCourseFileName()
   {
      return mCourseFileName;
   }
   
   /**
    * This is to return a string representation of the name of the chosen user.
    * 
    * @param iUserID
    *           The user ID representing the name to be returned
    * 
    * @return String The name associated with the user ID passed in. Returned as
    *         "First Last".
    */
   public String getName(String iUserID)
   {
      mUserID = iUserID;
      String name = "";
      String lastName = "";
      String firstName = "";

      try
      {
         Connection conn;
         PreparedStatement stmtSelectUser;
         conn = LMSDatabaseHandler.getConnection();
         String sqlSelectUser = "SELECT * FROM UserInfo WHERE UserID = ?";
         stmtSelectUser = conn.prepareStatement(sqlSelectUser);
         ResultSet userRS;

         synchronized( stmtSelectUser )
         {
            stmtSelectUser.setString(1, mUserID);
            userRS = stmtSelectUser.executeQuery();
         }

         while( userRS.next() )
         {
            lastName = userRS.getString("LastName");
            firstName = userRS.getString("FirstName");
         }

         name += firstName + " " + lastName;

         userRS.close();
         stmtSelectUser.close();
         conn.close();
      }
      catch( Exception e )
      {
         e.printStackTrace();
      }
      return name;
   }

   /**
    * This method clears the courses from the database
    * 
    * @param iPath - The web path
    * 
    * @return String - indicates whether clearDatabase was successful
    */

   public String clearDatabase(String iPath)
   {
      String result = "true";

      try
      {
         Connection conn;
         PreparedStatement stmtDeleteCourseInfo;
         PreparedStatement stmtUpdateApplicationData;
         PreparedStatement stmtGetCourseList;
         PreparedStatement stmtGetUserIDList;
         PreparedStatement stmtGetCourses;
         Connection objConn;
         PreparedStatement stmtDeleteObj;
         PreparedStatement stmtDeleteStatus;

         // Set the driverName and connectionURL variables and establishes the
         // database connection. The SQL string are also assigned and converted
         // to a prepared statement.
         conn = LMSDatabaseHandler.getConnection();
         String sqlGetUserList = "SELECT * FROM UserInfo";
         String sqlGetCourseList = "SELECT * FROM UserCourseInfo WHERE UserId = ?";
         String sqlGetCourses = "SELECT * FROM CourseInfo";
         String sqlDeleteCourseInfo = "Delete FROM CourseInfo";
         String sqlUpdateApplicationData = "UPDATE ApplicationData "
            + "SET numberValue = '1' WHERE dataName = 'nextCourseID'";
         String sqlDeleteCourseObjs = "Delete FROM Objectives";
         String sqlDeleteCourseStatus = "Delete FROM CourseStatus";

         //get users and courses they are registered for and delete all the
         // course files
         stmtGetCourseList = conn.prepareStatement(sqlGetCourseList);
         stmtGetUserIDList = conn.prepareStatement(sqlGetUserList);
         stmtGetCourses = conn.prepareStatement(sqlGetCourses);
         stmtDeleteCourseInfo = conn.prepareStatement(sqlDeleteCourseInfo);
         stmtUpdateApplicationData = conn.prepareStatement(sqlUpdateApplicationData);

         
         
         RTEFileHandler fileHandler = new RTEFileHandler();

         ResultSet userRS = null;
         userRS = stmtGetUserIDList.executeQuery();
         ResultSet courseRS = null;
         String user = new String();

         while( userRS.next() )
         {
            user = userRS.getString("UserID");
            synchronized( stmtGetCourseList )
            {
               stmtGetCourseList.setString(1, user);
               courseRS = stmtGetCourseList.executeQuery();
            }
            while( courseRS.next() )
            {
               fileHandler.deleteCourseFiles(courseRS.getString("CourseID"), user);
            }
         }

         String theWebPath = iPath;
         String mCourseDir = theWebPath + "CourseImports" + File.separator;
         ResultSet courseListRS = null;
         courseListRS = stmtGetCourses.executeQuery();
         //delete the template course files from the CourseImports folder
         while( courseListRS.next() )
         {
            File mRTESCODataDir = new File(mCourseDir + File.separator + courseListRS.getString("CourseID"));
            File mScoFiles[] = mRTESCODataDir.listFiles();
            if (mScoFiles == null) continue;

            for( int i = 0; i < mScoFiles.length; i++ )
            {
               deleteCourseFiles(mScoFiles[i]);
            }
            mRTESCODataDir.delete();
         }

         // Execute the queries to delete all records in the CourseInfo table
         // and to update the ApplicationData table.
         stmtDeleteCourseInfo.executeUpdate();
         stmtUpdateApplicationData.executeUpdate();
         PreparedStatement delItemInfo = conn.prepareStatement("delete from ItemInfo");
         delItemInfo.executeUpdate();
         PreparedStatement delUserCourseInfo = conn.prepareStatement("delete from UserCourseInfo");
         delUserCourseInfo.executeUpdate();

         // Close the statement and the database connection.
         stmtDeleteCourseInfo.close();
         stmtUpdateApplicationData.close();
         delItemInfo.close();
         delUserCourseInfo.close();
         conn.close();

         // Delete global objectives
         objConn = LMSDBHandler.getConnection();
         stmtDeleteObj = objConn.prepareStatement(sqlDeleteCourseObjs);
         stmtDeleteObj.executeUpdate();
         stmtDeleteObj.close();
         stmtDeleteStatus = objConn.prepareStatement(sqlDeleteCourseStatus);
         stmtDeleteStatus.executeUpdate();
         stmtDeleteStatus.close();
         PreparedStatement delItemStatus = objConn.prepareStatement("delete from ItemStatus");
         delItemStatus.executeUpdate();
         delItemStatus.close();
         LMSDBHandler.closeConnection();
      }
      catch( SQLException e )
      {
         System.out.println("sql exception in");
         result = "false";
         e.printStackTrace();
      }
      catch( Exception e )
      {
         result = "false";
         e.printStackTrace();
      }

      return result;
   }

   /**
    * Deletes course files
    * @param iDeleteFile - file selected for deletion
    */
   public void deleteCourseFiles(File iDeleteFile)
   {
      try
      {
         if( iDeleteFile.isDirectory() )
         {
            File mScoFiles[] = iDeleteFile.listFiles();
            for( int i = 0; i < mScoFiles.length; i++ )
            {
               deleteCourseFiles(mScoFiles[i]);
            }
         }

         iDeleteFile.delete();
      }
      catch( Exception e )
      {
         e.printStackTrace();
      }
   }
   
   /**
    * This adds the ObjectivesData information
    * @param ioObject - ObjectivesData object to be added
    * @return String - success of the action
    */
   public String addObj(ObjectivesData ioObject)
   {
      String result = "true";
      mUserID = ioObject.mUserID;
      // To protect case sensitivity we encode the ID before storage
      String objectiveID = decodeHandler.encodeObjectiveID(ioObject.mObjectiveID);
      String satisfied = ioObject.mSatisfied;
      String satisfiedValue = new String(satisfied);
      
      String newCompletionStatus = new String(ioObject.mCompletionStatus);     
     
      if( satisfied.equals("not satisfied") )
      {
         satisfiedValue = "notSatisfied";
      }
      if( newCompletionStatus.equals("not attempted"))
      {
         newCompletionStatus = "notAttempted";
      }
      
      Connection conn;

      String sqlSelectObjectives = "SELECT * FROM Objectives WHERE objID = ? " + "AND learnerID = ? AND scopeID = ?";
      PreparedStatement stmtSelectObjectives;
      String sqlInsertObjective = "INSERT INTO Objectives VALUES(?,?,?,?,?,?,?,?,?,?)";
      PreparedStatement stmtInsertObjective;
      ResultSet objectivesRS = null;
      
      try
      {
         conn = LMSDBHandler.getConnection();
         stmtSelectObjectives = conn.prepareStatement(sqlSelectObjectives);

         synchronized( stmtSelectObjectives )
         {
            stmtSelectObjectives.setString(1, objectiveID);
            stmtSelectObjectives.setString(2, mUserID);
            stmtSelectObjectives.setString(3, "");
            objectivesRS = stmtSelectObjectives.executeQuery();
         }

         if( objectivesRS.next() )
         {
            ioObject.mObjErr = "dupobjid";
            result = "false";
         }         
         else
         {
            stmtInsertObjective = conn.prepareStatement(sqlInsertObjective);
            synchronized( stmtInsertObjective )
            {
               stmtInsertObjective.setString(1, objectiveID);
               stmtInsertObjective.setString(2, mUserID);
               stmtInsertObjective.setString(3, satisfiedValue);
               stmtInsertObjective.setString(4, ioObject.mMeasure);
               stmtInsertObjective.setString(5, "");
               stmtInsertObjective.setString(6, ioObject.mRawScore);
               stmtInsertObjective.setString(7, ioObject.mMinScore);
               stmtInsertObjective.setString(8, ioObject.mMaxScore);
               stmtInsertObjective.setString(9, ioObject.mProgressMeasure);
               stmtInsertObjective.setString(10, newCompletionStatus);
               stmtInsertObjective.executeUpdate();
            }
         }
      }
      catch( Exception e )
      {
         result = "false";
         e.printStackTrace();
      }

      return result;
   }

   /**
    * Returns a list (Vector) of global objectives for the desired user
    * @param iUserID - the desired user
    * @param iObjs - the course specific objectives for the desired user
    * @return List (Vector) of global objectives
    */
   public Vector getGlobalObjs(String iUserID, Vector iObjs)
   {
      Vector obj = iObjs;
      try
      {
         Connection conn;
         PreparedStatement stmtSelectGlobals;

         conn = LMSDBHandler.getConnection();

         String sqlSelectGlobals = "SELECT * FROM Objectives where " + "learnerID = ? and scopeID = ''";
         stmtSelectGlobals = conn.prepareStatement(sqlSelectGlobals);
         synchronized( stmtSelectGlobals )
         {
            stmtSelectGlobals.setString(1, iUserID);
         }

         ResultSet globalsRS;

         globalsRS = stmtSelectGlobals.executeQuery();

         boolean foundObjective = false;
         boolean firstQueryEmpty = true;
         while( globalsRS.next() )
         {
            ObjectivesData od = new ObjectivesData();
            // Process the whitespace of the value because it is of type ID, xs:ID, or IDREF
            od.mObjectiveID = 
               decodeHandler.decodeObjectiveID(decodeHandler.processWhitespace(globalsRS.getString("objID")));
            
            od.mUserID = globalsRS.getString("learnerID");
            od.mSatisfied = globalsRS.getString("satisfied");
            od.mMeasure = globalsRS.getString("measure");
            od.mRawScore = globalsRS.getString("rawscore");
            od.mMinScore = globalsRS.getString("minscore");
            od.mMaxScore = globalsRS.getString("maxscore");
            od.mProgressMeasure = globalsRS.getString("progressmeasure");
            od.mCompletionStatus = globalsRS.getString("completion");
            
            if( obj != null )
            {
               for( int i = 0; i < obj.size(); i++ )
               {
                  firstQueryEmpty = false;
                  ObjectivesData od2 = (ObjectivesData)obj.elementAt(i);
                  if( od.mObjectiveID.equals(od2.mObjectiveID) )
                  {
                     foundObjective = true;
                     break;
                  }
               }
            }
            if( firstQueryEmpty || !foundObjective )
            {
               obj.add(od);
            }
         }
         globalsRS.close();
         stmtSelectGlobals.close();
         LMSDBHandler.closeConnection();
      }
      catch( Exception e )
      {
         e.printStackTrace();
      }

      return obj;
   }

   /**
    * Gets a List of objectives associated with courses for which a user is registered
    * @param iCourseID - The ID of the course
    * @param iUserID - The ID of the user
    * @return List of objectives for these desired course
    */
   public Vector getObjs(String iCourseID, String iUserID)
   {
      mUserID = iUserID;
      String courseID = iCourseID;
      Vector obj = new Vector();
      try
      {

         Connection conn;
         PreparedStatement stmtSelectObjectives;

         conn = LMSDBHandler.getConnection();

         //Query String to obtain Courses
         String sqlSelectObjectives = "SELECT * FROM Objectives where " + "learnerID = ? AND scopeID = ?";

         stmtSelectObjectives = conn.prepareStatement(sqlSelectObjectives);

         synchronized( stmtSelectObjectives )
         {
            stmtSelectObjectives.setString(1, mUserID);
            stmtSelectObjectives.setString(2, courseID);
         }

         ResultSet objectivesRS = stmtSelectObjectives.executeQuery();

         if( !courseID.equals("") )
         {
            // Loops through all of the global objectives and outputs them in
            // the table with a radio button for selection of delete or reset.
            while( objectivesRS.next() )
            {
               ObjectivesData od = new ObjectivesData();
               // Process the whitespace of the value because it is of type ID, xs:ID, or IDREF
               od.mObjectiveID = 
                  decodeHandler.decodeObjectiveID(decodeHandler.processWhitespace(objectivesRS.getString("objID")));
               od.mUserID = objectivesRS.getString("learnerID");
               od.mSatisfied = objectivesRS.getString("satisfied");
               od.mMeasure = objectivesRS.getString("measure");
               od.mRawScore = objectivesRS.getString("rawscore");
               od.mMinScore = objectivesRS.getString("minscore");
               od.mMaxScore = objectivesRS.getString("maxscore");
               od.mProgressMeasure = objectivesRS.getString("progressmeasure");
               od.mCompletionStatus = objectivesRS.getString("completion");
               obj.add(od);
            }
         }
         objectivesRS.close();
         stmtSelectObjectives.close();
         LMSDBHandler.closeConnection();
      }
      catch( Exception e )
      {
         e.printStackTrace();
      }
      return obj;
   }

   /**
    * Edits the objectives based on the parameters being passed in
    * @param iParams - List of parameters
    * @return String representation of the success of this operation (true or false)
    */
   public String editObjs(Vector iParams)
   {
      String result = "true";
      Vector requestNames = iParams;
      try
      {
         Connection conn;
         conn = LMSDBHandler.getConnection();

         PreparedStatement stmtUpdateObjective;
         PreparedStatement stmtDeleteObjective;

         String sqlUpdateObjective = "UPDATE Objectives SET satisfied = 'unknown', measure = 'unknown' " +
         ", rawscore = 'unknown', minscore = 'unknown', maxscore = 'unknown', progressmeasure = 'unknown'" +
         ",completion = 'unknown' WHERE objID = ? AND learnerID = ?";

         String sqlDeleteObjective = "DELETE FROM Objectives WHERE objID = ? AND learnerID = ?";

         stmtUpdateObjective = conn.prepareStatement(sqlUpdateObjective);

         stmtDeleteObjective = conn.prepareStatement(sqlDeleteObjective);

         // loop through all of the parameters.
         for( int i = 0; i < requestNames.size(); i++ )
         {
            String param = (String)requestNames.elementAt(i);            
            
            // We must remove the number~ prefix added in the html to protect case
            param = param.substring(param.indexOf("~")+1, param.length());
            
            String paramName;
            String paramValue;
            int splitIndex;
            
            splitIndex = param.lastIndexOf(":");            
            paramName = param.substring(0,splitIndex);
            paramValue = param.substring(splitIndex+1, param.length());
                        
            String objID;
            String learnerID;

            // If the parameter is not the submit button
            if( !( paramName.equals("submit") ) )
            {
               splitIndex = paramName.lastIndexOf(";");               
               objID = 
                  decodeHandler.encodeObjectiveID(paramName.substring(0,splitIndex));
               learnerID = paramName.substring(splitIndex+1, paramName.length());

               if( paramValue.equals("reset") )
               {
                  synchronized( stmtUpdateObjective )
                  {
                     stmtUpdateObjective.setString(1, objID);
                     stmtUpdateObjective.setString(2, learnerID);
                     stmtUpdateObjective.executeUpdate();
                  }
               }
               else if( paramValue.equals("delete") )
               {
                  synchronized( stmtDeleteObjective )
                  {
                     stmtDeleteObjective.setString(1, objID);
                     stmtDeleteObjective.setString(2, learnerID);
                     stmtDeleteObjective.executeUpdate();
                  }
               }
            }
         }
         stmtUpdateObjective.close();
         stmtDeleteObjective.close();
         LMSDBHandler.closeConnection();
      }
      catch( Exception e )
      {
         result = "false";
         System.out.println(e);
      }
      return result;
   }

   /**
    * This method is used to convert a '\n' delimited string of comments to a
    * Vector. <br>
    * 
    * @param iComments -
    *           A String list of comments delimited by '[.]'
    * @return Vector - A <code>Vector</code> of Strings that correspond to the
    *         <code>cmi.comments_from_lms.n.comment</code> value of a SCO.
    */
   public Vector getCommentVector(String iComments)
   {
      String[] commentArray = iComments.split("\n");

      Vector commentVector = new Vector();

      for( int i = 0; i < commentArray.length; i++ )
      {
         String cmt = commentArray[i].trim();
         commentVector.add(cmt);
      }

      return commentVector;
   }

   /**
    * This method takes a course from the import page and uploads it to 
    * the server.
    *
    * @param iWebPath - A String representation of the path to the server.
    * @param iSessionID - The id of the server session.
    * @param iRequest - A HttpServletRequest containing information about the
    * course to be imported
    * @return ResultCollection - Encapsulates the information about 
    *   the validation of the course submitted to be imported to the Sample RTE.
    */

   public ResultCollection importCourse(HttpServletRequest iRequest, 
                                         String iWebPath, String iSessionID)
   {
      ResultCollection validationResults = new ResultCollection();
      String sessionID = "";
      String uploadDir = "";
      String fileName = "";
      String myFileName = "";
      String courseTitle = "";
      LMSManifestHandler myManifestHandler;

      try
      {
         sessionID = iSessionID;
         String fileSeparator = java.io.File.separator;
        
         String theWebPath = iWebPath;
         String mDrive = System.getProperty("user.home");
         uploadDir = mDrive + fileSeparator + SRTEFILESDIR + fileSeparator +
             "tempUploads" + fileSeparator + sessionID;
         File theRTEUploadDir = new File(uploadDir);
         boolean isMultipart = FileUpload.isMultipartContent(iRequest);
         
         // Create a factory for disk-based file items
         FileItemFactory factory = new DiskFileItemFactory();
         ServletFileUpload upload = new ServletFileUpload(factory);
         if ( !theRTEUploadDir.isDirectory() )
         {
             theRTEUploadDir.mkdirs();
         }
         // Parse the request
         List items = upload.parseRequest(iRequest);

         Iterator iter = items.iterator();

         FileItem item = (FileItem)iter.next();

         String name = item.getFieldName();
         if ( name.equals("coursezipfile") ) 
         {
             fileName = (new File(fileName)).getName();
             myFileName = item.getName().substring(item.getName().lastIndexOf(fileSeparator) + 1);
             File fNew= new File( uploadDir, myFileName );
             courseTitle = fileName;
             item.write(fNew);
         }
         FileItem item2 = (FileItem)iter.next();
         String validationValue = item2.getString();       
 
         // Set the name of the course being imported so it can be gotten from the outside
         mCourseFileName = myFileName;
         mOnlineValidation = validationValue.equals("1");    
 
         String zipFile = uploadDir + fileSeparator + myFileName;
         String theXSDPath = theWebPath.
                             substring(0, theWebPath.
                                          lastIndexOf(fileSeparator));
         // Create a manifest handler instance
         myManifestHandler = new LMSManifestHandler(theXSDPath);

         myManifestHandler.setCourseName(courseTitle);

         myManifestHandler.setWebPath(theWebPath);

         // Parse the manifest and fill up the object structure
         validationResults = myManifestHandler.processPackage(zipFile, mOnlineValidation);

      }
      catch( Exception e )
      {
         e.printStackTrace();
      }

      return validationResults;
   }
   
   /**
    * Returns if online validation is to be used
    * 
    * @return mOnlineValidation value
    */
   public boolean isOnlineValidation()
   {
      return mOnlineValidation;
   }

   /**  Utility Method to copy a file
    *
    * @param inFile - File to copy.
    * @param outFile - Destination file.
    * @return boolean stating success of copy
    */
   public boolean ZipCopy(File inFile, File outFile)
   {  
        
       boolean success = false;
       try
       {
           FileInputStream fis  = new FileInputStream(inFile);
           FileOutputStream fos = new FileOutputStream(outFile);
           byte[] buf = new byte[1024];
           int i = 0;
           while((i=fis.read(buf))!=-1) 
           {
              fos.write(buf, 0, i);
           }
           fis.close();
           fos.close();
           success = true;
        }
        catch( IOException ioe )
        {
            ioe.printStackTrace();
        }
        return success;
   }

   /**
    * This method takes a all courses in a specified directory and uploads them to 
    * the server. 
    * 
    * @param iFilename - A String representation of the name of the file.
    * @param iZipFile - A String representation of the path to the file.
    * @param iDirectory - A file representation of the path to the folder
    * @param iWebPath - A String representation used to determine the drive on which 
    *                   the Sample RTE is installed.
    * @param iSessionID - The id of the server session.
    * @param iValidation - String determining whether or not to perform validation.
    * @return ResultCollection - Encapsulates the information about 
    *   the validation of the course submitted to be imported to the Sample RTE.
    */

   public ResultCollection importMultipleCourses(String iFilename, String iZipFile, File iDirectory,
                                         String iWebPath, String iSessionID,
                                         String iValidation)
   {
      ResultCollection validationResults = new ResultCollection();
      String sessionID = "";
      String uploadDir = "";
      String fileName = "";
      String myFileName = "";
      String courseTitle = "";
      LMSManifestHandler myManifestHandler;

      boolean validation = true;
      
      validation = iValidation.equals("1");     

      try
      {
         sessionID = iSessionID;
         String fileSeparator = java.io.File.separator;
         String theWebPath = iWebPath;
         String mDrive = System.getProperty("user.home");
         uploadDir = mDrive + fileSeparator + SRTEFILESDIR + fileSeparator +
             "tempUploads" + fileSeparator + sessionID;
          File theRTEUploadDir = new File(uploadDir);

         if ( !theRTEUploadDir.isDirectory() )
         {
             theRTEUploadDir.mkdirs();
         }
             
         String zipFile = iZipFile;  
         myFileName = iFilename;    
         File fNew= new File( uploadDir, myFileName );
         courseTitle = fileName;
       
         File courseFile = new File(iZipFile);
         ZipCopy(courseFile, fNew);  
        
         String theXSDPath = theWebPath.substring(0, theWebPath.
                                          lastIndexOf(fileSeparator));
         // Create a manifest handler instance
         myManifestHandler = new LMSManifestHandler(theXSDPath);

         myManifestHandler.setCourseName(courseTitle);

         myManifestHandler.setWebPath(theWebPath);

         // Parse the manifest and fill up the object structure
         validationResults = myManifestHandler.processPackage(zipFile, validation);

      }
      catch( Exception e )
      {
         e.printStackTrace();
      }

      return validationResults;
   }
   
   public CourseData createCourse(String courseID, String courseTitle)
   {
      CourseData cd = new CourseData();
      cd.mCourseID = courseID;
      cd.mCourseTitle = courseTitle;
      cd.mImportDateTime = DateFormat.getDateTimeInstance().format(new Date());

      if (! updateCourse(cd, false))
      {
         return null;
      }

      return cd;
   }

   public CourseData updateCourse(String courseID, String courseTitle) 
   {
      if (! updateCourse(new CourseData(courseID, courseTitle), true)) 
      {
         return null;
      }
      
      return getCourseData(courseID);
   }

   public CourseData addCourseItem(String courseID, String itemID, String itemTitle, String itemLaunch) 
   {
      List<ItemData> items = getItems(courseID);
      
      Connection conn = LMSDatabaseHandler.getConnection();
      PreparedStatement stmtItemInfo = null;
      boolean fail = false;
      try 
      {
         stmtItemInfo = conn.prepareStatement("Insert into ItemInfo(CourseID, ItemIdentifier, Title, Launch, ItemOrder) values(?,?,?,?,?)");
         stmtItemInfo.setString(1, courseID);
         stmtItemInfo.setString(2, itemID);
         stmtItemInfo.setString(3, itemTitle);
         stmtItemInfo.setString(4, itemLaunch);
         stmtItemInfo.setInt(5, items.size());
         
         synchronized (stmtItemInfo) 
         {
            stmtItemInfo.execute();
         }
      } 
      catch (SQLException se) 
      {
         fail = true;
      } 
      finally
      {
         try 
         {
            if (stmtItemInfo != null) stmtItemInfo.close();
            if (conn != null) conn.close();
         } 
         catch (SQLException sse) {}
      }
      if (fail) return null;
      return getCourseData(courseID);
   }
   
   public CourseData updateCourseItem(String activityID, String courseID, String itemID, String itemTitle, String itemLaunch) 
   {
      Connection conn = LMSDatabaseHandler.getConnection();
      PreparedStatement stmtItemInfo = null;
      boolean fail = false;
      try 
      {
         stmtItemInfo = conn.prepareStatement("Update ItemInfo "
                                          + "set ItemIdentifier = ?, Title = ?, Launch = ? "
                                          + "where activityID = ?");
         stmtItemInfo.setString(1, itemID);
         stmtItemInfo.setString(2, itemTitle);
         stmtItemInfo.setString(3, itemLaunch);
         stmtItemInfo.setInt(4, Integer.parseInt(activityID));

         synchronized (stmtItemInfo) 
         {
            stmtItemInfo.execute();
         }
      }  
      catch (SQLException se) 
      {
         fail = true;
      } 
      finally 
      {
         try 
         {
            if (stmtItemInfo != null) stmtItemInfo.close();
            if (conn != null) conn.close();
         } 
         catch (SQLException sse) {}
      }

      if (fail) return null;
      return getCourseData(courseID);
   }

   public CourseData getCourseData(String courseID) 
   {
      CourseData cd = getCourseInfo(courseID);
      if (cd != null) cd.items = getItems(cd.mCourseID);
      return cd;
   }
   
   public CourseData getCourseData(String courseID, String userID)
   {
      CourseData cd = getCourseData(courseID);
      Connection objconn = LMSDatabaseHandler.getConnection(LMSDatabaseHandler.GLOBAL_OBJECTIVES);
      PreparedStatement stmtCourseStatus = null;
      ResultSet cstatus = null;
      
      PreparedStatement stmtItemStatus = null;
      ResultSet status = null;
      try {
         stmtCourseStatus = objconn.prepareStatement("select * from CourseStatus where learnerID = ? and courseID = ?");
         stmtCourseStatus.setString(1, userID);
         stmtCourseStatus.setString(2, courseID);
         
         synchronized (stmtCourseStatus) 
         {
            cstatus = stmtCourseStatus.executeQuery();
         }
         
         if (cstatus.next())
         {
            cd.mCompleted = cstatus.getString("completed");
            cd.mSatisfied = cstatus.getString("satisfied");
            cd.mMeasure = cstatus.getString("measure");
            cd.mProgMeasure = cstatus.getString("progmeasure");
         }
      } 
      catch (SQLException se) {} 
      finally 
      { 
         try 
         {
            if (stmtCourseStatus != null) stmtCourseStatus.close();
         } 
         catch (SQLException sse) {}
      }
      
      try 
      {
         for (ItemData id : cd.items)
         {
            stmtItemStatus = objconn.prepareStatement("select * from ItemStatus where learnerID = ? AND courseID = ? AND activityID = ?");
            stmtItemStatus.setString(1, userID);
            stmtItemStatus.setString(2, courseID);
            stmtItemStatus.setInt(3, id.activityID);
   
            synchronized (stmtItemStatus) 
            {
               status = stmtItemStatus.executeQuery();
            }
   
            if (status.next())
            {
               id.success = status.getBoolean("success");
               id.completion = status.getBoolean("completion");
               id.scaled = status.getFloat("scaled");
               id.raw = status.getFloat("raw");
               id.max = status.getFloat("max");
               id.min = status.getFloat("min");
               id.response = status.getString("response");
               id.duration = status.getString("duration");
               id.refStmtID = status.getString("refStmtID");
               id.statement = status.getString("statement");
            }
            
         }
      } 
      catch (SQLException se) {} 
      finally 
      { 
         try 
         {
            if (stmtCourseStatus != null) stmtCourseStatus.close();
            if (stmtItemStatus != null) stmtItemStatus.close();
            if (objconn != null) objconn.close();
         } 
         catch (SQLException sse) {}
      }
      return cd;
   }
   
   public void updateCourseStatus(String courseid, String userid) 
   {
      LRSInfo info = Config.getLRSInfo();
      if (info == null || "".equals(info.Endpoint)) return;
      
      updateCourseData(info, courseid, userid);
      updateItemsData(info, courseid, userid);
   }
   
   public List<CourseData> getEditableCourses() 
   {
      List<CourseData> courses = new ArrayList<CourseData>();
      Connection conn = LMSDatabaseHandler.getConnection();
      PreparedStatement getcourses = null;
      ResultSet cset = null;
      try 
      {
         getcourses = conn.prepareStatement("select CourseID, CourseTitle from CourseInfo where TOC = 0 and Start = 0;");
         synchronized (getcourses) 
         {
            cset = getcourses.executeQuery();
         }
         while (cset.next())
         {
            courses.add(new CourseData(cset.getString("CourseID"), cset.getString("CourseTitle")));
         }
      } 
      catch (SQLException se) {} 
      finally 
      { 
         try 
         {
            if (getcourses != null) getcourses.close();
            if (conn != null) conn.close();
         } 
         catch (SQLException sse) {}
      }
      return courses;
   }
   
   public void updateCourseActiveStatus(String courseid, int active) 
   {
      Connection conn = LMSDatabaseHandler.getConnection();
      PreparedStatement setactive = null;
      try 
      {
         setactive = conn.prepareStatement("update CourseInfo set Active = ? where CourseID = ?");
         
         synchronized (setactive)
         {
            setactive.setInt(1, (active == 1) ? 1 : 0);
            setactive.setString(2, courseid);
            setactive.executeUpdate();
         }
      } 
      catch (SQLException se) {} 
      finally 
      { 
         try 
         {
            if (setactive != null) setactive.close();
            if (conn != null) conn.close();
         } 
         catch (SQLException sse) {}
      }
   }
   
   private void updateCourseData(LRSInfo info, String courseid, String userid)
   {
      ArrayList<Statement> statements = new ArrayList<Statement>();
      List<UserAgentInfo> agents = new UserService().getUserAgentInfos(userid);
      for (UserAgentInfo ag : agents)
      {
         statements.addAll(getStatusStatements(info, getAgent(ag), courseid));
      }
      if (statements.isEmpty())
      {
         System.out.println("CourseService.updateCourseData() - no statements\nlrs: " + info.Endpoint + "\ncourseid: " + courseid + "\nuserid: " + userid);
         return;
      }
      Collections.sort(statements, new SortStmtByLatest());
      for (Statement s : statements)
      {
         if (s.getResult() != null)
         {
            setCourseStatus(s, courseid, userid);
            break;
         }
      }
   }
   
   /**
    * only gets the results from a statement for status
    * doesn't deal with progress measure (which is a different statement)
    * @param s
    * @param courseid
    * @param userid
    */
   private void setCourseStatus(Statement s, String courseid, String userid)
   {
      Connection conn = LMSDatabaseHandler.getConnection(LMSDatabaseHandler.GLOBAL_OBJECTIVES);
      PreparedStatement status = null;
      try 
      {
         status = conn.prepareStatement("update CourseStatus set satisfied = ?, measure = ?, completed = ?, progmeasure = ?, refStmtID = ? where courseID = ? and learnerID = ?");
         
         synchronized (status) {
            status.setString(1, Boolean.toString(s.getResult().isSuccess()));
            status.setString(2, (s.getResult().getScore() != null) ? s.getResult().getScore().getScaled()+"" : "unknown");
            status.setString(3, Boolean.toString(s.getResult().isCompletion()));
            status.setString(4, "unknown");
            status.setString(5, s.getId());
            status.setString(6, courseid);
            status.setString(7, userid);
            status.executeUpdate();
         }
      } 
      catch (SQLException e) {
         System.out.println("CourseService.setCourseStatus() - sqlexception\ncourseid: " + courseid + "\nuserid: " + userid);
         e.printStackTrace();
      } 
      finally 
      {
         try 
         {
            if (status != null) status.close();
            if (conn != null) conn.close();
         } 
         catch (SQLException e) { }
      }
   }
   
   private void updateItemsData(LRSInfo info, String courseid, String userid)
   {
      List<UserAgentInfo> agents = new UserService().getUserAgentInfos(userid);
      Connection srteconn = LMSDatabaseHandler.getConnection();
      PreparedStatement getItems = null;
      ResultSet items = null;
      
      try 
      {
         getItems = srteconn.prepareStatement("select * from ItemInfo where CourseID = ?");
         getItems.setString(1, courseid);
         synchronized (getItems) 
         {
            items = getItems.executeQuery();
         
            while (items.next())
            {
               String itemid = items.getString("ItemIdentifier");
               ArrayList<Statement> statements = new ArrayList<Statement>();
               for (UserAgentInfo ag : agents)
               {
                  statements.addAll(getStatusStatements(info, getAgent(ag), itemid));
               }
               if (statements.isEmpty())
               {
                  System.out.println("CourseService.updateCourseData() - no statements\nlrs: " + info.Endpoint + "\nitemid: " + itemid + "\nuserid: " + userid);
                  continue;
               }
               Collections.sort(statements, new SortStmtByLatest());
               for (Statement s : statements)
               {
                  if (s.getResult() != null)
                  {
                     synchronized (s) 
                     {                        
                        setItemStatus(info, s, courseid, items.getInt("ActivityID"), userid);
                        break;
                     }
                  }
               }
            }
         }
      } 
      catch (SQLException e) {
         e.printStackTrace();
      } 
      finally 
      {
         try 
         {
            if (getItems != null) getItems.close();
            if (srteconn != null) srteconn.close();
         } 
         catch (SQLException e) { }
      }
   }
   
   private void setItemStatus(LRSInfo info, Statement s, String courseid, int activityid, String userid)
   {
      Connection objconn = LMSDatabaseHandler.getConnection(LMSDatabaseHandler.GLOBAL_OBJECTIVES);
      PreparedStatement updateItem = null;
      try 
      {
         updateItem = objconn.prepareStatement("update ItemStatus set scaled = ?, raw = ?, min = ?, max = ?, success = ?, completion = ?, response = ?, "
               + "duration = ?, refStmtID = ?, statement = ? where activityID = ? and courseID = ? and learnerID = ?");
         updateItem.setFloat(1, (s.getResult().getScore() != null) ? s.getResult().getScore().getScaled() : 0f);
         updateItem.setFloat(2, (s.getResult().getScore() != null) ? s.getResult().getScore().getRaw() : 0f);
         updateItem.setFloat(3, (s.getResult().getScore() != null) ? s.getResult().getScore().getMin() : 0f);
         updateItem.setFloat(4, (s.getResult().getScore() != null) ? s.getResult().getScore().getMax() : 0f);
         updateItem.setBoolean(5, norm(s.getResult().isSuccess()));
         updateItem.setBoolean(6, norm(s.getResult().isCompletion()));
         updateItem.setString(7, norm(s.getResult().getResponse()));
         updateItem.setString(8, norm(s.getResult().getDuration()));
         updateItem.setString(9, s.getId());
         updateItem.setString(10, new Gson().toJson(s));
         updateItem.setInt(11, activityid);
         updateItem.setString(12, courseid);
         updateItem.setString(13, userid);
         updateItem.executeUpdate();
      } 
      catch (SQLException e)
      {
         System.out.println("CourseService.setItemStatus() - sql exception");
         e.printStackTrace();
      }
      finally 
      {
         try 
         {
            if (updateItem != null) updateItem.close();
            if (objconn != null) objconn.close();
         } 
         catch (SQLException e) { }
      }
   }

   private boolean norm(Boolean val) 
   {
      // why null Boolean why?
      return (val == null) ? false : val;
   }

   private String norm(String st)
   {
      return (st == null) ? "" : st;
   }

   /**
    * Get statements by userid, 'terminated', and activityID
    * @param userid
    * @param activityID
    * @return StatementResults or null
    */
   private ArrayList<Statement> getStatusStatements(LRSInfo info, Agent agent, String activityID)
   {
      StatementResult res = null;
      ArrayList<Statement> statements = new ArrayList<Statement>();
      try 
      {
         res =  new StatementClient(info.Endpoint, info.UserName, info.Password)
                     .filterByActor(agent)
                     .filterByVerb(Verbs.terminated())
                     .filterByActivity(activityID)
                     .getStatements();
         if (res != null && res.getStatements() != null) 
         {
            statements = res.getStatements();
            while (res.hasMore())
            {
               res = new StatementClient(info.Endpoint, info.UserName, info.Password)
                        .filterByActor(agent)
                        .filterByVerb(Verbs.terminated())
                        .filterByActivity(activityID)
                        .getStatements();
               statements.addAll(res.getStatements());
            }
            
         }
      } 
      catch (IOException e) 
      {
         System.out.println("CourseService.getStatusStatements() -- getting xapi statements threw an IO");
         e.printStackTrace();
      }
      return statements;
   }
   
   private Agent getAgent(UserAgentInfo agentInfo)
   {
      if (agentInfo.Mbox == null || "".equals(agentInfo.Mbox))
      {
         return new Agent("", new Account(agentInfo.AccName, agentInfo.HomePage));
      }
      return new Agent("", agentInfo.Mbox);
   }

   private CourseData getCourseInfo(String courseID) 
   {
      Connection conn = LMSDatabaseHandler.getConnection();
      CourseData cd = new CourseData();
      PreparedStatement stmtCourseInfo = null;
      ResultSet course = null;
      try 
      {
         stmtCourseInfo = conn.prepareStatement("select * from CourseInfo where CourseID = ?");
         stmtCourseInfo.setString(1, courseID);
         synchronized (stmtCourseInfo) 
         {
            course = stmtCourseInfo.executeQuery();
         }
         
         if (course.next())
         {
            cd.mCourseID = course.getString("CourseID");;
            cd.mCourseTitle = course.getString("CourseTitle");
            cd.mImportDateTime = course.getString("ImportDateTime");
            cd.mStart = course.getBoolean("Start");
            cd.mTOC = course.getBoolean("TOC");
            cd.active = course.getInt("Active");
         }
      }
      catch (SQLException se)
      {
         cd = null;
      } 
      finally 
      { 
         try 
         {
            if (stmtCourseInfo != null) stmtCourseInfo.close();
            if (conn != null) conn.close();
         } 
         catch (SQLException sse) {}
      }

      return cd;
   }
   
   private List<ItemData> getItems(String courseid) 
   {
      Connection conn = LMSDatabaseHandler.getConnection();
      Connection otherconn = LMSDatabaseHandler.getConnection(LMSDatabaseHandler.GLOBAL_OBJECTIVES);
      PreparedStatement stmtItemInfo = null;
      ResultSet items = null;
      List<ItemData> data = new ArrayList<ItemData>();
      try 
      {
         stmtItemInfo = conn.prepareStatement("select * from ItemInfo where CourseID = ? order by ItemOrder ASC");
         stmtItemInfo.setString(1, courseid);

         synchronized (stmtItemInfo) 
         {
            items = stmtItemInfo.executeQuery();
         }

         while (items.next())
         {
            data.add(new ItemData(items.getInt("ActivityID"), items.getString("ItemIdentifier"), 
                  items.getString("Title"), items.getString("Launch")));
         }
         
      } 
      catch (SQLException se) { } 
      finally 
      { 
         try 
         {
            if (stmtItemInfo != null) stmtItemInfo.close();
            if (conn != null) conn.close();
            if (otherconn != null) otherconn.close();
         } 
         catch (SQLException sse) {}
      }

      return data;
   }
   
   private boolean updateCourse(CourseData cd, boolean update)
   {
      Connection conn = LMSDatabaseHandler.getConnection();
      PreparedStatement stmtCourseInfo = null;
      boolean ok = true;
      try 
      {
         if (update) 
         {
            stmtCourseInfo = conn.prepareStatement("Update CourseInfo " + 
                     "set CourseTitle = ? where CourseID = ?");
            stmtCourseInfo.setString(1, cd.mCourseTitle);
            stmtCourseInfo.setString(2, cd.mCourseID);
         }
         else //insert
         {
            stmtCourseInfo = conn.prepareStatement("Insert into "
                  + "CourseInfo(CourseID, CourseTitle, ImportDateTime, Start, TOC) "
                  + "values(?,?,?,?,?)");
            stmtCourseInfo.setString(1, cd.mCourseID);
            stmtCourseInfo.setString(2, cd.mCourseTitle);
            stmtCourseInfo.setString(3, cd.mImportDateTime);
            stmtCourseInfo.setInt(4, (cd.mStart)?1:0);
            stmtCourseInfo.setInt(5, (cd.mTOC)?1:0);
         }
         
         synchronized (stmtCourseInfo) 
         {
            stmtCourseInfo.execute();
         }
      }  
      catch (SQLException se) {
         ok = false;   
      } 
      finally 
      { 
         try 
         {
            if (stmtCourseInfo != null) stmtCourseInfo.close();
            if (conn != null) conn.close();
         } 
         catch (SQLException sse) {}
      }   
      return ok;
   }

}
