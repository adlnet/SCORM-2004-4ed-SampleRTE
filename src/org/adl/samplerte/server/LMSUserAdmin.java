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

import org.adl.api.ecmascript.APIErrorManager;
import org.adl.datamodels.datatypes.LangStringValidator;
import org.adl.datamodels.datatypes.RealRangeValidator;


/**
 * The LMSUserAdmin class handles the administration of user information.  This 
 * inforamtion includes the users password and the cmi.learner_preferences 
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
            
            // Send the results to the JSP view
            iRequest.setAttribute("userProfile", userProfile);
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
            result = "";
            reqOp = "Add User";            
            Vector currentProfiles = new Vector();
            currentProfiles = userService.getUsers(false);                       
            boolean duplicate = false;            
            userProfile = new UserProfile();
            userProfile.mAdmin = 
                (iRequest.getParameter("admin")).equalsIgnoreCase("true");
            userProfile.mFirstName = iRequest.getParameter("firstName");
            userProfile.mLastName = iRequest.getParameter("lastName");
            userProfile.mPassword = iRequest.getParameter("password");
            userProfile.mUserID = iRequest.getParameter("userID");
            
            // Compare new user to current list of active users
            int i = 0; 
            String oldID = new String();
            UserProfile iUserProfile = new UserProfile();
            while ( ! duplicate && i < currentProfiles.size() )             
            {                                    
                iUserProfile = (UserProfile)currentProfiles.elementAt(i++);
                oldID = iUserProfile.mUserID;
                if ( oldID.equals( userProfile.mUserID) ) 
                {
                    duplicate = true;
                }
            }            
            
            if ( duplicate == true) 
            {   
                reqOp = "duplicate_user";
                iRequest.setAttribute("result","false");
                iRequest.setAttribute("reqOp",reqOp);
                iRequest.setAttribute("userProfile", userProfile);
                launchView(DSP_NEWUSER, iRequest, oResponse);
            }
            else
            {
                result = userService.addUser(userProfile);
                iRequest.setAttribute("result",result);
                iRequest.setAttribute("reqOp",reqOp);
                launchView(DSP_OUTCOME, iRequest, oResponse);
            }
            break;

         case ServletRequestTypes.UPDATE_PREF:
            boolean validationError = false;
            result = "false";
            String errorHeader = "Please correct the following fields:  ";
            String errorMsg = "";
            userProfile = new UserProfile();
            reqOp = "Update Profile";
            userProfile.mFirstName = iRequest.getParameter("firstName");
            userProfile.mLastName = iRequest.getParameter("lastName");
            userProfile.mUserID = iRequest.getParameter("userID");
            userProfile.mAudioLevel = iRequest.getParameter("audioLevel");
            userProfile.mAudioCaptioning = iRequest.getParameter("audioCaptioning");
            userProfile.mDeliverySpeed = iRequest.getParameter("deliverySpeed");
            userProfile.mLanguage = iRequest.getParameter("language");             
            userProfile.mPassword = iRequest.getParameter("password");
            String mAdminString = iRequest.getParameter("admin");
            userProfile.mAdmin = mAdminString.equals("true");
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

            if ( (userProfile.mPassword == null) || 
                 (userProfile.mPassword.length() == 0) ||  
                 (userProfile.mPassword.trim().equals("")) )
            {
               validationError = true;
               errorMsg += "<br>Password cannot be empty";
            }

            if ( validationError )
            {
               iRequest.setAttribute("errorMsg", errorMsg);
               iRequest.setAttribute("errorHeader", errorHeader);
               iRequest.setAttribute("userProfile", userProfile);
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

         case ServletRequestTypes.DELETE_USERS:
            userService = new UserService();
            reqOp = "Delete user";
            String uID = iRequest.getParameter("userId");
            String delRes = userService.deleteUser(uID);
            iRequest.setAttribute("reqOp", reqOp);
            iRequest.setAttribute("result", delRes);
            launchView(DSP_OUTCOME, iRequest, oResponse);
            break;
            
         default: 
            // Todo -- put in the error page.
            System.out.println("Default Case -- LMSUserAdmin.java -- Error");
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
}