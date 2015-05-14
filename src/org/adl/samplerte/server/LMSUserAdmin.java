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
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.adl.datamodels.datatypes.LangStringValidator;
import org.adl.datamodels.datatypes.RealRangeValidator;


/**
 * The LMSUserAdmin class handles the administration of user information.  This 
 * information includes the users password and the cmi.learner_preferences 
 * data model elements, audio_level, audio_captioning, delivery_speed, and
 * language.<br><br>
 * 
 * <strong>Filename:</strong> LMSUserAdmin.java<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM 2004 4th Edition Sample RTE <br>
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
public class LMSUserAdmin extends HttpServlet
{
   /**
    * String Constant for the display users view
    */ 
   private static final String DSP_USERS = "/admin/dsp_users.jsp";

   /**
    * String Constant for the display user information view
    */ 
   private static final String DSP_USERPROFILE = "/admin/dsp_userProfile.jsp";

   /**
    * String Constant for the new user page view
    */ 
   private static final String DSP_NEWUSER = "/admin/newUser.jsp";

   /**
    * String Constant for the success page view
    */ 
   private static final String DSP_OUTCOME = "/admin/dsp_outcome.jsp";

   /**
    * This sets up Java Logging
    */
   private Logger mLogger = Logger.getLogger("org.adl.util.debug.samplerte");
   
   /**
    * This method handles the 'POST' message sent to the servlet.  This servlet
    * will handle determining the appropriate service to invoke based on the
    * request type.  
    * 
    * @param iRequest  The request 'POST'ed to the servlet.
    * 
    * @param oResponse The response returned by the servlet.
    * 
    */
   public void doPost(HttpServletRequest iRequest, HttpServletResponse oResponse) 
   {
      mLogger.entering("---LMSAdmin", "doPost()");
      mLogger.info("POST received by LMSAdmin");      
      processRequest(iRequest, oResponse);
   }

   /**
    * This method handles the 'GET' message sent to the servlet.  This servlet
    * will handle determining the appropriate service to invoke based on the
    * request type.  
    * 
    * @param iRequest  The request - 'GET' - to the servlet.
    * 
    * @param oResponse The response returned by the servlet.
    */
   public void doGet(HttpServletRequest iRequest, 
                     HttpServletResponse oResponse)
   {	   
      processRequest(iRequest, oResponse);
   }

   /**
    * Processes the request sent to the servlet.
    * 
    * @param iRequest The request posted to the servlet
    * @param oResponse the response returned by the servlet
    */
   private void processRequest(HttpServletRequest iRequest, HttpServletResponse oResponse)
   {
      mLogger.info("LMSAdmin - Entering processRequest()");
      String reqOp = "";
      String result = "";
      UserService userService;
      UserProfile userProfile;
      
      try
      {
         iRequest.setCharacterEncoding("utf-8");
         oResponse.setCharacterEncoding("utf-8");
      }
      catch (Exception e)
      {
         System.out.println("LMSUserAdmin:processRequest - encoding exception");
         e.printStackTrace();
      }
      
      // Determine the request type coming into the Servlet
      String sType = iRequest.getParameter("type");
      String caller = iRequest.getParameter("caller");
      if ( sType == null )
      {
         sType = "999";   
      }
      int type = Integer.parseInt(sType);
      switch ( type )
      {
         case ServletRequestTypes.GET_PREF: 
            userService = new UserService();
            userProfile = new UserProfile();
            userProfile = userService.getUser(iRequest.getParameter("userId"));  
            List<UserAgentInfo> infos = userService.getUserAgentInfos(iRequest.getParameter("userId"));
            // Send the results to the JSP view
            iRequest.setAttribute("userProfile", userProfile);
            iRequest.setAttribute("agentinfos", infos);
            iRequest.setAttribute("caller",caller);
            launchView(DSP_USERPROFILE, iRequest, oResponse);
            break;

         case ServletRequestTypes.GET_USERS:
            userService = new UserService();
            Vector userProfiles = new Vector();
            userProfiles = userService.getUsers(true);
            String setProcess = iRequest.getParameter("setProcess");            
            iRequest.setAttribute("setProcess",setProcess);            
            iRequest.setAttribute("caller",caller);
            iRequest.setAttribute("userProfiles", userProfiles);
            launchView(DSP_USERS, iRequest, oResponse);
            break;

          case ServletRequestTypes.NEW_USER:
             reqOp = "new_user";
             iRequest.setAttribute("result","true");
             iRequest.setAttribute("reqOp",reqOp);
             launchView(DSP_NEWUSER, iRequest, oResponse);
             break;


         case ServletRequestTypes.ADD_USERS:
            userService = new UserService();          
            userProfile = createUserProfile(iRequest);           
            
            if ( isProfileDuplicate(userProfile, userService.getUsers(false)) ) 
            {   
                iRequest.setAttribute("result","false");
                iRequest.setAttribute("reqOp","duplicate_user");
                iRequest.setAttribute("userProfile", userProfile);
                launchView(DSP_NEWUSER, iRequest, oResponse);
            }
            else
            {
                iRequest.setAttribute("result", userService.addUser(userProfile,iRequest.getParameter("password")));
                iRequest.setAttribute("reqOp","Add User");
                launchView(DSP_OUTCOME, iRequest, oResponse);
            }
            break;
         
         case ServletRequestTypes.NEW_SIGN_UP:
            userService = new UserService();          
            userProfile = createUserProfile(iRequest);           
            
            if ( isProfileDuplicate(userProfile, userService.getUsers(false)) ) 
            {   
                iRequest.setAttribute("result","false");
                iRequest.setAttribute("reqOp","duplicate_user");
                iRequest.setAttribute("userProfile", userProfile);
                launchView(DSP_NEWUSER, iRequest, oResponse);
            }
            else
            {
               userService.addUser(userProfile, iRequest.getParameter("password"));
               String courseid = iRequest.getParameter("courseID");
               courseid = (courseid != null && !"".equals(courseid)) ? "&courseID=" + courseid : "";
               launchView("/LMSUserAdmin?type=" + ServletRequestTypes.LOG_IN + 
                      "&uname=" + iRequest.getParameter("userID") + 
                      "&pwd=" + iRequest.getParameter("password") + courseid, iRequest, oResponse);
            }
            break;

         case ServletRequestTypes.UPDATE_PREF:
            boolean validationError = false;
            result = "false";
            String errorHeader = "Please correct the following fields:  ";
            String errorMsg = "";
            userProfile = createUserProfile(iRequest);
            userService = new UserService();
            infos = userService.getUserAgentInfos(iRequest.getParameter("userId"));
            reqOp = "Update Profile";
            RealRangeValidator rrv = 
                                  new RealRangeValidator(new Double(0.0), null);
            if ( (userProfile.mAudioLevel == null) || 
                 (userProfile.mAudioLevel.length() == 0) ||
                 (!(rrv.validate(userProfile.mAudioLevel) == 0)) )
            {
               validationError = true;
               errorMsg += "<br>cmi.learner_preference.audio_level must be "; 
               errorMsg += "a real number greater than or equal to 0.";  
            }

            if ( (userProfile.mDeliverySpeed == null) || 
                 (userProfile.mDeliverySpeed.length() == 0) ||
                 (!(rrv.validate(userProfile.mDeliverySpeed) == 0)) )
            {
               validationError = true;
               errorMsg += "<br>cmi.learner_preference.delivery_speed must"; 
               errorMsg += " be a real number greater than or equal to 0.";
            }

            LangStringValidator lsv = new LangStringValidator();
            if ( !(userProfile.mLanguage.trim().equals("")) &&
                 (!(lsv.validate(userProfile.mLanguage) == 0 )) )
            {
               validationError = true;
               errorMsg += "<br>cmi.learner_preference.language must be "; 
               errorMsg += "a valid SCORM 2004 4th Edition language type or blank.";    
            }

            if ( (userProfile.mAudioCaptioning == null) || 
                 (userProfile.mAudioCaptioning.length() == 0) ||
                 ( (userProfile.mAudioCaptioning.compareTo("0")!= 0) &&
                   (userProfile.mAudioCaptioning.compareTo("1")!= 0) &&
                   (userProfile.mAudioCaptioning.compareTo("-1")!= 0) ) )
            {
               validationError = true;
               errorMsg += "<br>cmi.learner_preference.audio_captioning "; 
               errorMsg += "can only contain the values -1, 0, 1.";       
            }

            if ( validationError )
            {
               iRequest.setAttribute("errorMsg", errorMsg);
               iRequest.setAttribute("errorHeader", errorHeader);
               iRequest.setAttribute("userProfile", userProfile);
               iRequest.setAttribute("agentinfos", infos);
               launchView(DSP_USERPROFILE, iRequest, oResponse);
            }
            else
            {
               userService = new UserService();
               result = userService.updateUser(userProfile);
               iRequest.setAttribute("reqOp", reqOp);
               iRequest.setAttribute("result", result);
               launchView(DSP_OUTCOME, iRequest, oResponse);
            }
            break;
            
         case ServletRequestTypes.CHANGE_PASSWORD:
            userService = new UserService();
            result = userService.changePassword(iRequest.getParameter("userID"),
                                                iRequest.getParameter("newPassword"));
            iRequest.setAttribute("reqOp", "Change password");
            iRequest.setAttribute("result", result);
            launchView(DSP_OUTCOME, iRequest, oResponse);

         case ServletRequestTypes.DELETE_USERS:
            userService = new UserService();
            reqOp = "Delete user";
            String uID = iRequest.getParameter("userId");
            String delRes = userService.deleteUser(uID);
            iRequest.setAttribute("reqOp", reqOp);
            iRequest.setAttribute("result", delRes);
            launchView(DSP_OUTCOME, iRequest, oResponse);
            break;
         
         case ServletRequestTypes.LOG_IN:
            userService = new UserService();
            String courseid = iRequest.getParameter("courseID");
            if ( userService.loginUser(iRequest, oResponse) )
            {
               if (courseid != null && ! "".equals(courseid) && 
                     new UserService().prepareForDirectLaunch(iRequest.getParameter("uname"), courseid, iRequest.getParameter("path"))) {
                  String title = "";
                  boolean start = false;
                  boolean toc = false;
                  for (Object o : new CourseService().getCourses(iRequest.getParameter("uname"), "timestamp", "DESC")) 
                  {
                     CourseData cd = (CourseData)o;
                     if (cd.mCourseID.equals(courseid))
                     {
                        title = cd.mCourseTitle;
                        start = cd.mStart;
                        toc = cd.mTOC;
                        break;
                     }
                  }
                  iRequest.setAttribute("courseTitle", title);
                  try {
                     if (start)
                        oResponse.sendRedirect("/adl/runtime/sequencingEngine.jsp?courseID=" + courseid + "&courseTitle=" + title);
                     else if (toc)
                        oResponse.sendRedirect("/adl/runtime/sequencingEngine.jsp?courseID=" + courseid + "&courseTitle=" + title + "&viewTOC=true");
                     else
                        launchView("/LMSCourseAdmin?type=" + ServletRequestTypes.GO_HOME +"&userID=" + iRequest.getSession().getAttribute("USERID"), iRequest, oResponse);
                  } catch (IOException e) {
                     launchView("/LMSCourseAdmin?type=" + ServletRequestTypes.GO_HOME + 
                           "&userID=" + iRequest.getSession().getAttribute("USERID"), iRequest, oResponse);
                  }
               }
               else {
                  launchView("/LMSCourseAdmin?type=" + ServletRequestTypes.GO_HOME + 
                                          "&userID=" + iRequest.getSession().getAttribute("USERID"), iRequest, oResponse);
               }
            }
            else
            {
               launchView("/runtime/LMSLogin2.jsp", iRequest, oResponse);
            }
            break;
         
         case ServletRequestTypes.SET_LRS_INFO:
            // clear out messages
            iRequest.setAttribute("lrs-ok-message", "");
            iRequest.setAttribute("lrs-error-message", "");
            
            userService = new UserService();
            String userid = iRequest.getParameter("userID");
            userService.updateLRSAccountInfo(iRequest);
            infos = userService.getUserAgentInfos(userid);
            userProfile = userService.getUser(userid);
            
            iRequest.setAttribute("userProfile", userProfile);
            iRequest.setAttribute("agentinfos", infos);
            iRequest.setAttribute("caller",caller);
            
            launchView(DSP_USERPROFILE, iRequest, oResponse);
            break;
            
         default: 
            // Todo -- put in the error page.
            System.out.println("Default Case -- LMSUserAdmin.java -- Error");
      }
   }

   private boolean isProfileDuplicate(UserProfile userProfile, Vector currentProfiles) {
      // Compare new user to current list of active users
      boolean duplicate = false;
      for (int i = 0; ! duplicate && i < currentProfiles.size(); i++)             
      {                                    
          duplicate = ((UserProfile)currentProfiles.elementAt(i)).mUserID.equals( userProfile.mUserID);
      }
      return duplicate;
   }

   private UserProfile createUserProfile(HttpServletRequest iRequest) {
      UserProfile userProfile = new UserProfile();
      
      userProfile.mFirstName = iRequest.getParameter("firstName");
      userProfile.mLastName = iRequest.getParameter("lastName");
      userProfile.mUserID = iRequest.getParameter("userID");
      userProfile.mAudioLevel = iRequest.getParameter("audioLevel");
      userProfile.mAudioCaptioning = iRequest.getParameter("audioCaptioning");
      userProfile.mDeliverySpeed = iRequest.getParameter("deliverySpeed");
      userProfile.mLanguage = iRequest.getParameter("language");
      userProfile.mAdmin = Boolean.parseBoolean(iRequest.getParameter("admin"));
      return userProfile;
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
}