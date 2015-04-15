<%@ page contentType="text/html;charset=utf-8" %>

<%@page import = "java.util.*, java.io.*, org.adl.samplerte.server.*"%>

<%
   /***************************************************************************
   **
   ** Filename:  dsp_userProfile.jsp
   **
   ** File Description:  This file provides an interface for the admin to select
   **					 a user profile to update/modify/view.
   **
   **
   ** Author: ADL Technical Team
   **
   ** Contract Number:
   ** Company Name: CTC
   **
   ** Module/Package Name:
   ** Module/Package Description:
   **
   ** Design Issues:
   **
   ** Implementation Issues:
   ** Known Problems:
   ** Side Effects:
   **
   ** References: ADL SCORM
   **
   /***************************************************************************
      
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

   ***************************************************************************/

   UserProfile user = new UserProfile();
   
   request.setCharacterEncoding("utf-8");
   
   user = (UserProfile)request.getAttribute("userProfile");
   String userID = user.mUserID;
   String firstName = user.mFirstName;
   String lastName = user.mLastName;
   String audioLevel = user.mAudioLevel;
   String language = "";
   boolean isAdmin = user.mAdmin;
   String isUserAdmin = (String)session.getAttribute( "RTEADMIN" );
   String caller = (String)request.getAttribute("caller");
   String helpPage = "/adl/help/changeProfileHelp.htm";
   
   //System.out.println("dsp_userProfile : " + caller);

   if (caller != null && caller.equals("adminUserPref"))
   {
      helpPage = "/adl/help/manageUserHelp.htm";
   }
   
   
   if (  !(user.mLanguage == null) && !(user.mLanguage.length() == 0) )
   {
      language = user.mLanguage;
   }

   String deliverySpeed = user.mDeliverySpeed;
   String audioCaptioning = user.mAudioCaptioning;

   String errorHeader = (String)request.getAttribute("errorHeader");
   String errorString = (String)request.getAttribute("errorMsg");
%>
<html>
<head>
   <title>
      SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - User
       Preferences Aministration - Edit User Profile
   </title>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <link href='includes/sampleRTE_style.css' rel='stylesheet'
   type='text/css'>

   <script type="text/javascript">
     function openHelp(page)
     {
        window.open(page,"CommentsLMS",
                                "HEIGHT=550,WIDTH=600 status=no location=no scrollbars=yes");
     }
     
     function validatePassword(form) {
    	 if (form.oldPassword.value == "" || 
    			 form.newPassword.value == "" || 
    			 form.newPassword2.value == "")
  		 {
    		  alert("Password Fields cannot be blank.");
    		  return false;
  		 }
    	 if (form.newPassword.value !== form.newPassword2.value)
    	 {
    		 alert("New passwords do not match.");
    		 return false;
    	 }
     }
   </script>


</head>
<body bgcolor='FFFFFF'>

<jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
    <jsp:param value="<%=helpPage %>" name="helpURL"/>
</jsp:include>

<p><a href="javascript:history.go(-1)">Back</a></p>

<p class="font_header">
<b>
User Preferences Administration - Edit User Profile
</b>
</p>
<%
 if ( (errorString != null) && (errorHeader != null) )
   {
%>
<p class="font_two">
<b>
<%=errorHeader%>
</b>
<%=errorString%>
<br>
</p>

<%
   }
%>
<form method="post" action="/adl/LMSUserAdmin" name="userProfile" accept-charset="utf-8">
   <input type="hidden" name="type" value="<%= ServletRequestTypes.UPDATE_PREF %>" />
   <input type="hidden" name="userID" value="<%=userID%>" />
   <input type="hidden" name="firstName" value="<%=firstName%>" />
   <input type="hidden" name="lastName" value="<%=lastName%>" />
   <input type="hidden" name="caller" value="<%=caller%>" />
   <table width="450">
      <tr>
         <td colspan="2">
            <hr>
         </td>
      </tr>
      <tr>
         <td bgcolor="#5E60BD" colspan="2" class="white_text">
            <b>
               &nbsp;Please edit any user information you would like to change:
         </b>
         </td>
      </tr>
      <tr>
         <td>
            Username:
         </td>
         <td>
            <%=firstName%>&nbsp;<%=lastName%>
         </td>
      </tr>
      <tr>
         <td>
            <label for="audioLevel">cmi.learner_preference.audio_level:</label> 
         </td>
         <td>
            <input type="text" name="audioLevel" id="audioLevel" 
               value="<%=audioLevel%>" />
         </td>
      </tr>
      <tr>
         <td>
            <label for="language">cmi.learner_preference.language:</label>
         </td>
         <td>
            <input type="text" name="language" id="language" 
               value="<%=language%>" />
         </td>
      </tr>
      <tr>
         <td>
            <label for="deliverySpeed">cmi.learner_preference.delivery_speed:
            </label>
         </td>
         <td>
            <input type="text" name="deliverySpeed" id="deliverySpeed" 
               value="<%=deliverySpeed%>" />
         </td>
      </tr>
      <tr>
         <td>
            <label for="audioCaptioning">
               cmi.learner_preference.audio_captioning:</label>
         </td>
         <td>
            <input type="text" name="audioCaptioning" id="audioCaptioning"
             value="<%=audioCaptioning%>" />
         </td>
      </tr>
      <%
         if( isUserAdmin.equals( "true" ) )
         {
      %>
      <tr>
         <td>
            <label for="admin">User Rights:</label>
         </td>
         <td>
            <select name="admin" id="admin">
               <option value="true" <%if( isAdmin ){%>SELECTED<%}%>>Admin
               </option>
               <option value="false" <%if( !isAdmin ){%>SELECTED<%}%>>User
               </option>
            </select>
         </td>
      </tr>
      <%
         }
         else
         { %>
            <input type="hidden" name="admin" value="<%=isUserAdmin%>" />
      <% }

      %>
      <tr>
         <td colspan=2 align="center">
            <input type="submit" name="Submit" value="Submit" />
         </td>
      </tr>
      <tr>
         <td colspan="2">
            <hr>
         </td>
      </tr>
   </table>
</form>
<form method="post" action="/adl/LMSUserAdmin" name="userProfile" accept-charset="utf-8" onSubmit="return validatePassword(this);">
   <input type="hidden" name="type" value="<%= ServletRequestTypes.CHANGE_PASSWORD %>" />
   <input type="hidden" name="userID" value="<%=userID%>" />
   <table>
      <tr>
         <td>
            <label for="oldPassword">
               Password:</label>
         </td>
         <td>
            <input type="password" name="oldPassword" id="oldPassword" />
         </td>
      </tr>
      <tr>
         <td>
            <label for="newPassword1">
               New Password:</label>
         </td>
         <td>
            <input type="password" name="newPassword" id="newPassword" />
         </td>
      </tr>
      <tr>
         <td>
            <label for="newPassword2">
               New Password:</label>
         </td>
         <td>
            <input type="password" name="newPassword2" id="newPassword2" />
         </td>
      </tr>
      <tr>
         <td colspan=2 align="center">
            <input type="submit" name="Submit" value="Submit" />
         </td>
      </tr>
   </table>
</form>
</body>
</html>

