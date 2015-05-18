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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.io.File;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.adl.samplerte.util.Config;
import org.adl.util.decode.decodeHandler;
import org.adl.validator.util.ResultCollection;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.google.gson.Gson;

/**
 * <strong>Filename: </strong> LMSCourseAdmin <br>
 * <br>
 * 
 * <strong>Description: </strong> <br>
 * This servlet handles course administration features. <br>
 * <br>
 * 
 * <strong>Design Issues: </strong> <br>
 * This implementation is intended to be used by the SCORM Sample RTE <br>
 * <br>
 * 
 * <strong>References: </strong> <br>
 * <ul>
 * <li>IMS Simple Sequencing Specification
 * <li>SCORM 2004
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class LMSCourseAdmin extends HttpServlet
{
   /**
    * String Constant for the display courses view
    */
   private static final String DSP_COURSES = "/admin/dsp_courses.jsp";

   /**
    * String Constant for the display scos view
    */
   private static final String DSP_SCOS = "/admin/dsp_scos.jsp";

   /**
    * String Constant for the display scos view
    */
   private static final String DSP_SCO = "/admin/dsp_comments.jsp";

   /**
    * String Constant for the success page view
    */
   private static final String DSP_OUTCOME = "/admin/dsp_outcome.jsp";

   /**
    * String Constant for the register course page view
    */
   private static final String DSP_MNG_COURSE = "/runtime/LMSHome.jsp";

   /**
    * String Constant for the select course page view
    */
   private static final String DSP_SELECTCOURSE = "/runtime/dsp_selectCourse.jsp";

   /**
    * String Constant for the view status page view
    */
   private static final String DSP_VIEWSTATUS = "/runtime/dsp_viewStatus.jsp";

   /**
    * String Constant for the create objective page view
    */
   private static final String DSP_CREATEOBJ = "/admin/dsp_createObjective.jsp";

   /**
    * String Constant for the select user page view
    */
   private static final String DSP_SELECTUSER = "/admin/dsp_selectUser.jsp";

   /**
    * String Constant for the select course obj page view
    */
   private static final String DSP_SELECTCOURSEOBJ = "/admin/dsp_selectCourseObj.jsp";

   /**
    * String Constant for the select course obj page view
    */
   private static final String DSP_OBJECTIVEADMIN = "/admin/dsp_objectivesAdmin.jsp";
   
   /**
    * String Constant for the import results page
    */
   private static final String IMPORT_RESULTS = "/import/dsp_importResults.jsp"; 
   
   /**
    * Path to edit external course jsp 
    */
   private static final String EDIT_EXT_COURSE = "/admin/edit_ext_course.jsp";
   
   /**
    * Path to view external course jsp
    */
   private static final String VIEW_EXT_COURSE = "/runtime/view_ext_course.jsp";
   
   private static final String VIEW_EDITABLE_COURSES = "/admin/view_editable_courses.jsp";
   
   /**
    * List of settings for each user that has used the system
    */
   private List mUserSettings = new ArrayList();

   /**
    * Handles the 'POST' message sent to the servlet. This servlet will handle
    * deterimining the appropriate service to invoke based on the request type.
    * 
    * @param iRequest
    *            The request 'POST'ed to the servlet.
    * 
    * @param oResponse
    *            The response returned by the servlet.
    */
   public void doPost(HttpServletRequest iRequest,
                      HttpServletResponse oResponse)
   {
      processRequest(iRequest, oResponse);
   }

   /**
    * Handles the 'GET' message sent to the servlet. This servlet will handle
    * deterimining the appropriate service to invoke based on the request type.
    * 
    * @param iRequest
    *            The request - 'GET' - to the servlet.
    * 
    * @param oResponse
    *            The response returned by the servlet.
    * 
    */
   public void doGet(HttpServletRequest iRequest, HttpServletResponse oResponse)
   {
      if (iRequest.getParameter("type").equals("status"))
      {
         CourseData cd = new CourseService().showCourseStatus(iRequest.getParameter("courseID"), iRequest.getParameter("userID"));
         try
         {
            oResponse.setContentType("text/xml");
            
            Element root = new Element("courseData");
            Element satisfied = new Element("satisfied").setText((cd.mSatisfied == null)? "unknown" : cd.mSatisfied);
            Element measure = new Element("measure").setText((cd.mMeasure == null)? "unknown" : cd.mMeasure);
            Element completed = new Element("completed").setText((cd.mCompleted == null)? "unknown" : cd.mCompleted);
            Element progmeasure = new Element("progmeasure").setText((cd.mProgMeasure == null)? "unknown" : cd.mProgMeasure);
            
            root.addContent(satisfied);
            root.addContent(measure);
            root.addContent(completed);
            root.addContent(progmeasure);
            
            Document cdxml = new Document(root);
            XMLOutputter out = new XMLOutputter();
            out.output(cdxml, oResponse.getWriter());
         }
         catch ( IOException e )
         {
            e.printStackTrace();
         }
      }
      else if (iRequest.getParameter("type").equals("sessionInfo"))
      {
         String userID = iRequest.getParameter("userID");
         if ( userID == null )
         {
            return;
         }
         if ( iRequest.getParameter("adminDisplayStatus") != null )
         {            
            updateAdminTableSettings(userID, iRequest.getParameter("adminDisplayStatus"));
         }
         else if ( iRequest.getParameter("regDisplayStatus") != null )
         {            
            updateRegTableSettings(userID, iRequest.getParameter("regDisplayStatus"));
         }
         else if ( iRequest.getParameter("unregDisplayStatus") != null )
         {            
            updateUnregTableSettings(userID, iRequest.getParameter("unregDisplayStatus"));
         }
      }
      else
      {
         processRequest(iRequest, oResponse);
      }
   }

   /**
    * Handles determining the type of request and invoking the appropriate
    * service.
    * 
    * @param iRequest
    *            The request - 'GET' - to the servlet.
    * 
    * @param oResponse
    *            The response returned by the servlet.
    * 
    */
   public void processRequest(HttpServletRequest iRequest,
                              HttpServletResponse oResponse) 
   {
      String userID = "";
      String courseID = "";
      String user = "";
      String reqOp = "";
      String result = "";
      
      CourseService courseService;
      UserService userService;
      ObjectivesData objData;
      Vector courses;
      Vector courseStatuses;
      
      try
      {
         iRequest.setCharacterEncoding("utf-8");
         oResponse.setCharacterEncoding("utf-8");
      }
      catch (Exception e)
      {
         System.out.println("LMSCourseAdmin:processRequest - encoding exception");
         e.printStackTrace();
      }
      
      // Determine the request type coming into the Servlet
      String sType = iRequest.getParameter("type");
      if ( sType == null )
      {
         sType = "999";
      }
      int type = Integer.parseInt(sType); 
      
      switch ( type )
      {
         case ServletRequestTypes.GET_COURSES:
            courseService = new CourseService();
            courses = new Vector(); 
            
            courses = courseService.getCourses("timestamp", "DESC");
            
            String setProcess = iRequest.getParameter("setProcess");
            iRequest.setAttribute("setProcess", setProcess);
            iRequest.setAttribute("courses", courses);
            launchView(DSP_COURSES, iRequest, oResponse);
            break;

         case ServletRequestTypes.GET_SCOS:
            courseService = new CourseService();
            Vector scos = new Vector();
            scos = courseService.getSCOs(iRequest.getParameter("courseID"));
            iRequest.setAttribute("scos", scos);
            launchView(DSP_SCOS, iRequest, oResponse);
            break;

         case ServletRequestTypes.GET_COMMENTS:
            courseService = new CourseService();
            SCOData sco = new SCOData();
            int id = Integer.parseInt(iRequest.getParameter("scoID"));
            sco = courseService.getSCO(id);
            iRequest.setAttribute("sco", sco);
            launchView(DSP_SCO, iRequest, oResponse);
            break;

         case ServletRequestTypes.UPDATE_SCO:
            courseService = new CourseService();
            result = "false";
            reqOp = "Updating Comments from LMS";
            int act = Integer.parseInt(iRequest.getParameter("scoID"));
            String txt = iRequest.getParameter("comments");
            String update = iRequest.getParameter("update");
            String locations = iRequest.getParameter("locations");
            result = courseService.updateSCO(act, txt, update, locations);
            iRequest.setAttribute("reqOp", reqOp);
            iRequest.setAttribute("result", result);
            launchView(DSP_OUTCOME, iRequest, oResponse);
            break;

         case ServletRequestTypes.PROC_REG_DELETE:
         case ServletRequestTypes.PROC_UNREG_DELETE:
        	 
            courseService = new CourseService();
            result = "false";
            reqOp = "Delete course";
            Vector thiscourseList = new Vector();
            String prefix = (type == ServletRequestTypes.PROC_REG_DELETE) ? "UN_" : "RE_";
            
            Enumeration enumCourseList = iRequest.getParameterNames();
            while (enumCourseList.hasMoreElements())
            {
               String temp = enumCourseList.nextElement().toString();
               if ( temp.startsWith(prefix) )
               {
                  thiscourseList.add(temp);
               }
            }
                        
            result = courseService.deleteCourse(thiscourseList);
            
            courses = new Vector();
            userID = iRequest.getParameter("userID");
            
            UserSettings userSettings = getUserSettings(userID);
            
            courses = courseService.getManagedCourses(userID, userSettings.mRegSortType, 
                  userSettings.mRegSortOrder, userSettings.mUnregSortType, userSettings.mUnregSortOrder);
                        
            iRequest.setAttribute("courses", courses);
            iRequest.setAttribute("reqOp", reqOp);
            iRequest.setAttribute("result", result);
            iRequest.setAttribute("showAdminTable", userSettings.mAdminDisplayStatus);
            iRequest.setAttribute("showRegTable", userSettings.mRegDisplayStatus);
            iRequest.setAttribute("showUnregTable", userSettings.mUnregDisplayStatus);
                        
            launchView(DSP_MNG_COURSE, iRequest, oResponse);
            break;

         case ServletRequestTypes.GO_HOME:
            courseService = new CourseService();
            
            userID = iRequest.getParameter("userID");
            courses = new Vector();
            
            userSettings = getUserSettings(userID);
            
            courses = courseService.getManagedCourses(userID, userSettings.mRegSortType, 
                  userSettings.mRegSortOrder, userSettings.mUnregSortType, userSettings.mUnregSortOrder);
            
            iRequest.setAttribute("courses", courses);
            iRequest.setAttribute("showAdminTable", userSettings.mAdminDisplayStatus);
            iRequest.setAttribute("showRegTable", userSettings.mRegDisplayStatus);
            iRequest.setAttribute("showUnregTable", userSettings.mUnregDisplayStatus);
            
            launchView(DSP_MNG_COURSE, iRequest, oResponse);
            
            break;

         case ServletRequestTypes.PROC_REG_COURSE:
         case ServletRequestTypes.PROC_UNREG_COURSE:
            result = "false";
            reqOp = "Register Course";
            courseService = new CourseService();
            Vector courseList = new Vector();
            prefix = (type == ServletRequestTypes.PROC_REG_COURSE) ? "UN_" : "RE_";
            Enumeration enumCourses = iRequest.getParameterNames();
            
            String path = iRequest.getParameter("path");
            userID = iRequest.getParameter("userID");
            while (enumCourses.hasMoreElements())
            {
               String temp = enumCourses.nextElement().toString();
               if ( temp.startsWith(prefix) )
               {
                  courseList.add(temp);
               }
            }
            result = courseService.updateRegCourses(courseList, path, userID);
            
            // We are going back to the management page, setup the parameters
            courses = new Vector();

            userSettings = getUserSettings(userID);
            
            courses = courseService.getManagedCourses(userID, userSettings.mRegSortType, 
                  userSettings.mRegSortOrder, userSettings.mUnregSortType, userSettings.mUnregSortOrder);
            
            iRequest.setAttribute("courses", courses);
            iRequest.setAttribute("reqOp", reqOp);
            iRequest.setAttribute("result", result);
            iRequest.setAttribute("showAdminTable", userSettings.mAdminDisplayStatus);
            iRequest.setAttribute("showRegTable", userSettings.mRegDisplayStatus);
            iRequest.setAttribute("showUnregTable", userSettings.mUnregDisplayStatus);
            
            launchView(DSP_MNG_COURSE, iRequest, oResponse);
            break;
            
         case ServletRequestTypes.PROC_RESET_COURSE:
            
            result = "false";
            reqOp = "Reset Course";
            courseService = new CourseService();
            courseList = new Vector();
            
            // We only want the registered courses table
            prefix = "RE_";
            enumCourses = iRequest.getParameterNames();
            
            path = iRequest.getParameter("path");
            userID = iRequest.getParameter("userID");
            while (enumCourses.hasMoreElements())
            {
               String temp = enumCourses.nextElement().toString();
               if ( temp.startsWith(prefix) )
               {
                  courseList.add(temp);
               }
            }
            result = courseService.resetCourses(courseList, path, userID);
            
            // We are going back to the management page, setup the parameters
            courses = new Vector();
            
            userSettings = getUserSettings(userID);
            
            courses = courseService.getManagedCourses(userID, userSettings.mRegSortType, 
                  userSettings.mRegSortOrder, userSettings.mUnregSortType, userSettings.mUnregSortOrder);
            
            iRequest.setAttribute("reqOp", reqOp);
            iRequest.setAttribute("result", result);
            iRequest.setAttribute("courses", courses);
            iRequest.setAttribute("showAdminTable", userSettings.mAdminDisplayStatus);
            iRequest.setAttribute("showRegTable", userSettings.mRegDisplayStatus);
            iRequest.setAttribute("showUnregTable", userSettings.mUnregDisplayStatus);
            
            launchView(DSP_MNG_COURSE, iRequest, oResponse);
            break;

         case ServletRequestTypes.PROC_SORT_COURSE:
            
            courseService = new CourseService();
            courseList = new Vector();
            
            String sortType = iRequest.getParameter("sortType");
            userID = iRequest.getParameter("userID");
            
            updateSortSettings(userID, sortType);            
            
            // We are going back to the management page, setup the parameters
            courses = new Vector();
            
            userSettings = getUserSettings(userID);
            
            courses = courseService.getManagedCourses(userID, userSettings.mRegSortType, 
                  userSettings.mRegSortOrder, userSettings.mUnregSortType, userSettings.mUnregSortOrder);
            
            iRequest.setAttribute("courses", courses);
            iRequest.setAttribute("showAdminTable", userSettings.mAdminDisplayStatus);
            iRequest.setAttribute("showRegTable", userSettings.mRegDisplayStatus);
            iRequest.setAttribute("showUnregTable", userSettings.mUnregDisplayStatus);
            
            launchView(DSP_MNG_COURSE, iRequest, oResponse);
            break;
            
         case ServletRequestTypes.SELECT_MY_COURSE:
            courseService = new CourseService();
            userID = iRequest.getParameter("userId");
            String caller = iRequest.getParameter("caller");            
            courses = new Vector();
            
            courses = courseService.getCourses(userID, "timestamp", "DESC");
            
            iRequest.setAttribute("courses", courses);
            iRequest.setAttribute("userId", userID);
            iRequest.setAttribute("caller", caller);
            launchView(DSP_SELECTCOURSE, iRequest, oResponse);
            break;

         case ServletRequestTypes.VIEW_MY_STATUS:
            courseService = new CourseService();
            courseID = iRequest.getParameter("courseID");
            userID = iRequest.getParameter("userID");
            CourseData cd = courseService.showCourseStatus(courseID, userID);
            String name = courseService.getName(userID);
            iRequest.setAttribute("name", name);
            iRequest.setAttribute("status", cd);
            launchView(DSP_VIEWSTATUS, iRequest, oResponse);
            break;

         case ServletRequestTypes.CLEAR_DB:
            courseService = new CourseService();
            reqOp = "Clear Database";
            path = iRequest.getParameter("path");
            userID = iRequest.getParameter("userID");
            
            result = courseService.clearDatabase(path);
            
            // We are going back to the management page, setup the parameters
            courses = new Vector();
            
            userSettings = getUserSettings(userID);
            
            courses = courseService.getManagedCourses(userID, userSettings.mRegSortType, 
                  userSettings.mRegSortOrder, userSettings.mUnregSortType, userSettings.mUnregSortOrder);
            
            iRequest.setAttribute("result", result);
            iRequest.setAttribute("reqOp", reqOp);
            iRequest.setAttribute("courses", courses);
            iRequest.setAttribute("showAdminTable", userSettings.mAdminDisplayStatus);
            iRequest.setAttribute("showRegTable", userSettings.mRegDisplayStatus);
            iRequest.setAttribute("showUnregTable", userSettings.mUnregDisplayStatus);
            
            launchView(DSP_MNG_COURSE, iRequest, oResponse);
            break;

         case ServletRequestTypes.NEW_OBJ:
            user = iRequest.getParameter("user");
            userService = new UserService();
            Vector users = userService.getUsers(true);
            String objErr = "";
            iRequest.setAttribute("objErr", objErr);
            iRequest.setAttribute("users", users);
            iRequest.setAttribute("user", user);
            launchView(DSP_CREATEOBJ, iRequest, oResponse);
            break;
            
         case ServletRequestTypes.SELECT_OBJ_USER:
            userService = new UserService();
            users = userService.getUsers(true);
            String setOption = iRequest.getParameter("setOption");
            iRequest.setAttribute("setOption", setOption);
            iRequest.setAttribute("users", users);
            launchView(DSP_SELECTUSER, iRequest, oResponse);
            break;

         case ServletRequestTypes.ADD_OBJ:
            reqOp = "Add Objective";
            userService = new UserService();
            courseService = new CourseService();
            objData = new ObjectivesData();
            objData.mUserID = iRequest.getParameter("userID");
            // Process the whitespace of the value because it is of type ID, xs:ID, or IDREF
            objData.mObjectiveID = decodeHandler.processWhitespace(iRequest.getParameter("objectiveID"));
            objData.mSatisfied = iRequest.getParameter("satisfied");
            objData.mMeasure = iRequest.getParameter("measure");
            objData.mRawScore = iRequest.getParameter("rawscore");
            objData.mMinScore = iRequest.getParameter("minscore");
            objData.mMaxScore = iRequest.getParameter("maxscore");
            objData.mProgressMeasure = iRequest.getParameter("progressmeasure");
            objData.mCompletionStatus = iRequest.getParameter("completion");
            
            result = courseService.addObj(objData);
            users = userService.getUsers(true);
            if ( result.equalsIgnoreCase("true"))
            {
               iRequest.setAttribute("result", result);
               iRequest.setAttribute("reqOp", reqOp);
               launchView(DSP_OUTCOME, iRequest, oResponse);
            }
            else
            {
               iRequest.setAttribute("users", users);
               iRequest.setAttribute("userID", objData.mUserID);
               iRequest.setAttribute("objID", objData.mObjectiveID);
               iRequest.setAttribute("satisfied", objData.mSatisfied);
               iRequest.setAttribute("measure", objData.mMeasure);
               iRequest.setAttribute("rawscore", objData.mRawScore);
               iRequest.setAttribute("minscore", objData.mMinScore);
               iRequest.setAttribute("maxscore", objData.mMaxScore);
               iRequest.setAttribute("progressmeasure", objData.mProgressMeasure);
               iRequest.setAttribute("completion", objData.mCompletionStatus);
               iRequest.setAttribute("objErr", "dupobjid");
               launchView(DSP_CREATEOBJ, iRequest, oResponse);
            }
            break;

         case ServletRequestTypes.COURSE_OBJ:
            user = iRequest.getParameter("user");
            courseService = new CourseService();
            courses = new Vector();
            
            courses = courseService.getCourses(user, "timestamp", "DESC");
            
            iRequest.setAttribute("courses", courses);
            iRequest.setAttribute("user", user);
            launchView(DSP_SELECTCOURSEOBJ, iRequest, oResponse);
            break;

         case ServletRequestTypes.OBJ_ADMIN:
            String course = iRequest.getParameter("course");
            user = iRequest.getParameter("user");
            courseService = new CourseService();
            Vector objectives = courseService.getObjs(course, user);
            objectives = courseService.getGlobalObjs(user, objectives);
            iRequest.setAttribute("objs", objectives);
            launchView(DSP_OBJECTIVEADMIN, iRequest, oResponse);
            break;

         case ServletRequestTypes.EDIT_OBJ:
            reqOp = "Edit Objectives";
            Vector requestList = new Vector();
            Enumeration requestNames = iRequest.getParameterNames();
            while (requestNames.hasMoreElements())
            {
               String paramName = (String)requestNames.nextElement();
               // If the parameter is not the submit button
               if ( !(paramName.equals("submit")) &&
                    !(paramName.equals("type")) )
               {
                  String paramValue = iRequest.getParameter(paramName);
                  String param = paramName + ":" + paramValue;
                  requestList.add(param);
               }
            }
            courseService = new CourseService();
            result = courseService.editObjs(requestList);
            iRequest.setAttribute("result", result);
            iRequest.setAttribute("reqOp", reqOp);
            launchView(DSP_OUTCOME, iRequest, oResponse);
            break;

         case ServletRequestTypes.IMPORT_COURSE:
            String sessionID = iRequest.getParameter("sessID");
            List resultList = new ArrayList();
            String courseFileName = "";
            
            String webPath = 
               this.getServletConfig().getServletContext().getRealPath("/");
            courseService = new CourseService();
                                    

            ResultCollection validationResult = 
                     courseService.importCourse(iRequest, webPath, sessionID);
            
            // Add the package name to the result list            
            courseFileName = courseService.getCourseFileName();
            resultList.add(courseFileName);
            
            // Add the ResultCollection to the result list
            resultList.add(validationResult);
            
            //mDspImportStatus = validationResult.getRedirectView();
            
            iRequest.setAttribute("result", resultList);
            iRequest.setAttribute("importAttempted", "true");
            iRequest.setAttribute("onlineValidation", Boolean.valueOf(courseService.isOnlineValidation()));
            
            
            launchView(IMPORT_RESULTS, iRequest, oResponse);
            break;

      case ServletRequestTypes.IMPORT_MULTIPLE_COURSES:
 
            String mSessionID = iRequest.getParameter("sessionId"); 
            String validation = iRequest.getParameter("validate"); 
            String mWebPath = 
               this.getServletConfig().getServletContext().getRealPath("/");
            String mDirectoryString = iRequest.getParameter("importfolder");
            String[] uploadFiles = null;
            String errorMsg = null;
            int numZipFiles = 0;
            boolean notADirectory = false;
            boolean onlineValidation = true;
            
            // Variables to hold validation results
            List importResultList = new ArrayList();
            String importAttempted = "false";
            
            File mDirectoryPath = new File(mDirectoryString);
            
            if ( ! mDirectoryPath.isDirectory() )
            {
                notADirectory = true;
            }
            else
            {           
                uploadFiles = mDirectoryPath.list();
                if (uploadFiles.length == 0) 
                {
                     errorMsg = "The Folder That You Entered Did Not Contain Any Files.";                       
                     iRequest.setAttribute("errorMsg", errorMsg);
                     
                }
                else for (int i = 0; i < uploadFiles.length; i++) 
                {                     
                     String filename = uploadFiles[i];
                     if ( filename.length() > 4 ) 
                     {                    
                         int index = filename.length();
                         int start = index - 4;
                         String extension = filename.substring(start, index);
                         extension = extension.toLowerCase();
                
                         if ( extension.equals(".zip") )                            
                         {
                             importAttempted = "true";
                             numZipFiles++;
                             String zipFile = mDirectoryPath + File.separator + uploadFiles[i];
                             courseService = new CourseService();
                             
                             ResultCollection multipleValidationResult = 
                                 courseService.importMultipleCourses(filename, zipFile, mDirectoryPath,
                                                mWebPath, mSessionID, validation);
                             
                             // Add package file name and validation result to list
                             importResultList.add(filename);
                             importResultList.add(multipleValidationResult); 
                             onlineValidation = courseService.isOnlineValidation();
                         }                         
                     }
                }
            }
            if ( notADirectory ) 
            {                
                errorMsg = "The Folder That You Entered Was Not Valid.";
                iRequest.setAttribute("errorMsg", errorMsg);
            }
            else if (numZipFiles == 0) 
            {
                 errorMsg = "The Folder That You Entered Did Not Contain Any Valid Course Files.";                   
                 iRequest.setAttribute("errorMsg", errorMsg);                 
            }            

            // Place results in request
            iRequest.setAttribute("importAttempted", importAttempted);
            iRequest.setAttribute("result", importResultList);    
            iRequest.setAttribute("onlineValidation", Boolean.valueOf(onlineValidation));
            launchView(IMPORT_RESULTS, iRequest, oResponse);
            
            break;
    
         case ServletRequestTypes.CREATE_NEW_COURSE:
            courseService = new CourseService();
            cd = courseService.createCourse(iRequest.getParameter("courseID"), iRequest.getParameter("courseTitle"));
            if (cd == null) {
               launchView("/import/createCourse.jsp", iRequest, oResponse);
            } else {
               iRequest.setAttribute("coursedata", cd);
               launchView(EDIT_EXT_COURSE, iRequest, oResponse);
            }          
            break;
         
         case ServletRequestTypes.UPDATE_EXT_COURSE:
            courseService = new CourseService();
            cd = courseService.updateCourse(iRequest.getParameter("courseID"),iRequest.getParameter("courseTitle"));
            
            iRequest.setAttribute("coursedata", (cd == null) ? new CourseData() : cd);
            launchView(EDIT_EXT_COURSE, iRequest, oResponse);
            
            break;
            
         case ServletRequestTypes.ADD_EXT_ITEM:
            courseService = new CourseService();
            cd = courseService.addCourseItem(iRequest.getParameter("courseID"), 
                  iRequest.getParameter("itemID"), iRequest.getParameter("itemTitle"), iRequest.getParameter("itemLaunch"));
            
            iRequest.setAttribute("coursedata", (cd == null) ? new CourseData() : cd);
            launchView(EDIT_EXT_COURSE, iRequest, oResponse);
            
            break;
            
         case ServletRequestTypes.UPDATE_EXT_ITEM:
            courseService = new CourseService();
            cd = courseService.updateCourseItem(iRequest.getParameter("activityID"), iRequest.getParameter("courseID"), 
                  iRequest.getParameter("itemID"), iRequest.getParameter("itemTitle"), iRequest.getParameter("itemLaunch"));
            
            iRequest.setAttribute("coursedata", (cd == null) ? new CourseData() : cd);
            launchView(EDIT_EXT_COURSE, iRequest, oResponse);
            break;
            
         case ServletRequestTypes.EXT_COURSE_DETAILS:
            courseService = new CourseService();
            cd = courseService.getCourseData(iRequest.getParameter("courseID"), iRequest.getParameter("userID"));
            
            iRequest.setAttribute("coursedata", (cd == null) ? new CourseData() : cd);
            launchView(VIEW_EXT_COURSE, iRequest, oResponse);
            break;
            
         case ServletRequestTypes.UPDATE_EXT_COURSE_STATUS:
            courseService = new CourseService();
            courseService.updateCourseStatus(iRequest.getParameter("courseID"), iRequest.getParameter("userID"));
            cd = courseService.getCourseData(iRequest.getParameter("courseID"), iRequest.getParameter("userID"));
            cd = (cd == null) ? new CourseData() : cd;

            oResponse.setContentType("application/json; charset=utf-8");
            oResponse.setCharacterEncoding("UTF-8");
            // Get the printwriter object from response to write the required json object to the output stream      
            try 
            {
               PrintWriter out = oResponse.getWriter();
               Gson gsondm = new Gson();
              
               StringBuilder sb = new StringBuilder();
               sb.append("{\"coursedata\":");
               sb.append(gsondm.toJson(cd));
               sb.append("}");
               out.print(sb.toString());
            } 
            catch (IOException e) 
            {
               System.out.println("LMSCourseAdmin.processRequest() - EXT_COURSE_DETAILS - error getting response writer");
               e.printStackTrace();
            }
            break;
            
         case ServletRequestTypes.GET_EDIT_COURSES:
            courseService = new CourseService();
            iRequest.setAttribute("courses", courseService.getEditableCourses());
            launchView(VIEW_EDITABLE_COURSES, iRequest, oResponse);
            break;
            
         case ServletRequestTypes.PUBLISH_EXT_ITEM:
            courseService = new CourseService();
            courseService.updateCourseActiveStatus(iRequest.getParameter("courseID"),1);
            cd = courseService.getCourseData(iRequest.getParameter("courseID"));
            iRequest.setAttribute("coursedata", (cd == null) ? new CourseData() : cd);
            launchView(EDIT_EXT_COURSE, iRequest, oResponse);
            break;
            
         case ServletRequestTypes.UNPUBLISH_EXT_ITEM:
            courseService = new CourseService();
            courseService.updateCourseActiveStatus(iRequest.getParameter("courseID"),0);
            cd = courseService.getCourseData(iRequest.getParameter("courseID"));
            iRequest.setAttribute("coursedata", (cd == null) ? new CourseData() : cd);
            launchView(EDIT_EXT_COURSE, iRequest, oResponse);
            break;
            
         default:
            // Todo -- put in the error page.
            System.out.println("Default Case -- LMSCourseAdmin.java -- Error");
      }
   }

   /**
    * Private method used to centralize the jsp dispatch code
    * 
    * @param iJsp the path to the view to be displayed
    * @param iRequest the request object
    * @param iResponse the response object
    *
    * */
   private void launchView(String iJsp, HttpServletRequest iRequest, 
                           HttpServletResponse iResponse)
   {
      try
      {
         RequestDispatcher rd = getServletContext().getRequestDispatcher(iJsp);
         rd.forward(iRequest,iResponse);
      }
      catch(ServletException se)
      {
         System.out.println("LMSCourseAdmin:launchView - servlet exception");
         se.printStackTrace();
      }
      catch(IOException ioe)
      {
         System.out.println("LMSCourseAdmin:launchView - io exception");
         ioe.printStackTrace();
      }
   }
   
   /**
    * Updates the user settings with the new admin table display setting
    * 
    * @param iUserID - current user ID
    * @param iAdminDisplay - whether or not to display the table
    */
   private void updateAdminTableSettings(String iUserID, String iAdminDisplay )
   {
      UserSettings tempUserSettings = new UserSettings(iUserID, iAdminDisplay, null, null, null, null, null, null);
      for ( int i = 0; i < mUserSettings.size(); i++ )
      {
         if ( ((UserSettings)mUserSettings.get(i)).mUserID.equals(iUserID) )
         {
            tempUserSettings = (UserSettings)mUserSettings.get(i);
            mUserSettings.remove(i);
         }
      }
      mUserSettings.add( new UserSettings(iUserID, iAdminDisplay, tempUserSettings.mRegDisplayStatus, 
            tempUserSettings.mUnregDisplayStatus, 
            tempUserSettings.mRegSortType,
            tempUserSettings.mRegSortOrder,
            tempUserSettings.mUnregSortType,
            tempUserSettings.mUnregSortOrder) );
   }
   
   /**
    * Updates the user settings with the new registered courses table display setting
    * 
    * @param iUserID - current user ID
    * @param iRegDisplay - whether or not to display the table
    */
   private void updateRegTableSettings(String iUserID, String iRegDisplay )
   {
      UserSettings tempUserSettings = new UserSettings(iUserID, null, iRegDisplay, null, null, null, null, null);
      for ( int i = 0; i < mUserSettings.size(); i++ )
      {
         if ( ((UserSettings)mUserSettings.get(i)).mUserID.equals(iUserID) )
         {
            tempUserSettings = (UserSettings)mUserSettings.get(i);
            mUserSettings.remove(i);
         }
      }
      mUserSettings.add( new UserSettings(iUserID, 
            tempUserSettings.mAdminDisplayStatus, 
            iRegDisplay, 
            tempUserSettings.mUnregDisplayStatus,
            tempUserSettings.mRegSortType, 
            tempUserSettings.mRegSortOrder,
            tempUserSettings.mUnregSortType,
            tempUserSettings.mUnregSortOrder) );
   }
   
   /**
    * Updates the user settings with the new unregistered courses table display setting
    * 
    * @param iUserID - current user ID
    * @param iUnregDisplay - whether or not to display the table
    */
   private void updateUnregTableSettings(String iUserID, String iUnregDisplay )
   {
      UserSettings tempUserSettings = new UserSettings(iUserID, null, null, iUnregDisplay, null, null, null, null);
      for ( int i = 0; i < mUserSettings.size(); i++ )
      {
         if ( ((UserSettings)mUserSettings.get(i)).mUserID.equals(iUserID) )
         {
            tempUserSettings = (UserSettings)mUserSettings.get(i);
            mUserSettings.remove(i);
         }
      }
      mUserSettings.add( new UserSettings(iUserID, 
            tempUserSettings.mAdminDisplayStatus, 
            tempUserSettings.mRegDisplayStatus, 
            iUnregDisplay,
            tempUserSettings.mRegSortType,
            tempUserSettings.mRegSortOrder,
            tempUserSettings.mUnregSortType,
            tempUserSettings.mUnregSortOrder) );
   }

   private void updateSortSettings(String iUserID, String iSortType)
   {
      UserSettings tempUserSettings = new UserSettings(iUserID);
      
      for ( int i = 0; i < mUserSettings.size(); i++ )
      {
         if ( ((UserSettings)mUserSettings.get(i)).mUserID.equals(iUserID) )
         {
            tempUserSettings = (UserSettings)mUserSettings.get(i);
            mUserSettings.remove(i);
         }
      }
      
      String sortTableName = iSortType.split("_")[0];
      String sortType = iSortType.split("_")[1];
      
      if ( sortTableName.equals("RE") )
      {
         String sortOrder = (tempUserSettings.mRegSortOrder.equals("DESC")) ? "ASC" : "DESC";
         
         mUserSettings.add( new UserSettings(iUserID, 
               tempUserSettings.mAdminDisplayStatus, 
               tempUserSettings.mRegDisplayStatus, 
               tempUserSettings.mUnregDisplayStatus,
               sortType,
               sortOrder,
               tempUserSettings.mUnregSortType,
               tempUserSettings.mUnregSortOrder) );
      }
      else
      {
         String sortOrder = (tempUserSettings.mUnregSortOrder.equals("DESC")) ? "ASC" : "DESC";
         
         mUserSettings.add( new UserSettings(iUserID, 
               tempUserSettings.mAdminDisplayStatus, 
               tempUserSettings.mRegDisplayStatus, 
               tempUserSettings.mUnregDisplayStatus,               
               tempUserSettings.mRegSortType,
               tempUserSettings.mRegSortOrder,
               sortType,
               sortOrder) );
      }
   }
   
   /**
    * Gets user user settings for the given user
    * 
    * @param iUserID - current user
    * @return - the settings for the current user
    */
   private UserSettings getUserSettings(String iUserID)
   {
      for ( int i = 0; i < mUserSettings.size(); i++ )
      {
         if ( ((UserSettings)mUserSettings.get(i)).mUserID.equals(iUserID) )
         {
            return (UserSettings)mUserSettings.get(i);
         }
      }
      return new UserSettings(iUserID);
   }
   
   /**
    * Class to hold user settings for RTE Home
    */
   private class UserSettings
   {
      /**
       * Display admin table
       */
      protected String mAdminDisplayStatus = "true";
      
      /**
       * Display registered courses table
       */
      protected String mRegDisplayStatus = "true";
      
      /**
       * Display unregistered courses table
       */
      protected String mUnregDisplayStatus = "true";
      
      /**
       * current user ID
       */
      protected String mUserID = "";
      
      /**
       * current reg sort type
       */
      protected String mRegSortType = "timestamp";
      
      /**
       * current reg sort order
       */
      protected String mRegSortOrder = "DESC";
      
      /**
       * current unreg sort type
       */
      protected String mUnregSortType = "timestamp";
      
      /**
       * current unreg sort order
       */
      protected String mUnregSortOrder = "DESC";
      
      /**
       * Constructor
       * 
       * @param iUserID - current user ID
       */
      public UserSettings(String iUserID)
      {
         mUserID = iUserID;
         mAdminDisplayStatus = "true";
         mRegDisplayStatus = "true";
         mUnregDisplayStatus = "true";
         mRegSortOrder = "DESC";
         mRegSortType = "timestamp";
         mUnregSortOrder = "DESC";
         mUnregSortType = "timestamp";
      }
      
      /**
       * Constructor
       * 
       * @param iUserID - current user ID
       * @param iAdmin - display admin table (true or false)
       * @param iReg - display registered courses table (true or false)
       * @param iUnreg - display unregistered courses table (true or false)
       */
      public UserSettings(String iUserID, 
                           String iAdmin, 
                           String iReg, 
                           String iUnreg, 
                           String iRegSortType, 
                           String iRegSortOrder, 
                           String iUnregSortType, 
                           String iUnregSortOrder)
      {
         mUserID = iUserID;
         mAdminDisplayStatus = (iAdmin == null) ? "true" : iAdmin;
         mRegDisplayStatus = (iReg == null) ? "true" : iReg;
         mUnregDisplayStatus = (iUnreg == null) ? "true" : iUnreg;
         mRegSortOrder = (iRegSortOrder == null) ? "DESC" : iRegSortOrder;
         mRegSortType = (iRegSortType == null) ? "timestamp" : iRegSortType;
         mUnregSortOrder = (iUnregSortOrder == null) ? "DESC" : iUnregSortOrder;
         mUnregSortType = (iUnregSortType == null) ? "timestamp" : iUnregSortType;
      }
   }
}