<%@ page contentType="text/html;charset=utf-8" %>

<%@page import = "java.util.*, java.io.*, org.adl.samplerte.server.*" %>

<%
   /***************************************************************************
   **
   ** Filename:  dsp_users.jsp
   **
   ** File Description:  This file provides an interface for an admin to select
   **                     a user.
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
   String setProcess = (String)request.getAttribute("setProcess");
   Vector userList = new Vector();
   userList = (Vector)request.getAttribute("userProfiles");
   int size = userList.size();
   int valueInt = 0;
   String caller = (String)request.getAttribute("caller");
   String action = "";
   String helpLocation = "";
   
   if( setProcess.equals("pref") )
   {
      valueInt = ServletRequestTypes.GET_PREF;
      action = "/adl/LMSUserAdmin";
      helpLocation =  "/adl/help/manageUserHelp.htm";
   }
   else if ( setProcess.equals("delete") )
   {
      valueInt = ServletRequestTypes.DELETE_USERS;
      action = "/adl/LMSUserAdmin";
      helpLocation =  "/adl/help/deleteUserHelp.htm";
   }
   else
   {
      valueInt = ServletRequestTypes.SELECT_MY_COURSE;
      action = "/adl/LMSCourseAdmin";
      helpLocation =  "/adl/help/userCourseStatusHelp.htm";
   }

   String bodyText = ""; 
	
   UserProfile user = null;
	for( int i = 0; i < size; i++ )
    {
   	   user = (UserProfile)userList.elementAt(i);
   	   if ( i == 0 )
   	   {
   	      bodyText += "<tr><td><input type='radio' name='userId' id='userId' value='" 
   	      + user.mUserID + "' checked>" + user.mUserID + "</input></td></tr>";
   	   }
   	   else
   	   { 
	     bodyText += "<tr><td><input type='radio' name='userId' id='userId' value='" 
   	     + user.mUserID + "'>" + user.mUserID + "</input></td></tr>";
      }
   }
%>

<script>
/****************************************************************************
**
** Function:  newWindow()
** Input:   pageName = Name of the window
** Output:  none
**
** Description:  This method opens a window named <pageName>
**
***************************************************************************/
function newWindow(pageName)
{
   window.open(pageName, 'Help',"toolbar=no,location=no,directories=no," +
               "status=no,menubar=no,scrollbars=yes,resizable=yes," +
               "width=550,height=520");
}
</script>

<html>
<head>
    <title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - User 
    Preferences Administration - Select User</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <link href='includes/sampleRTE_style.css' rel='stylesheet' type='text/css'>
</head>
    <body bgcolor='FFFFFF' style="margin-top: 4.5em;">

    <jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
       <jsp:param value="<%= helpLocation %>" name="helpURL"/>
   </jsp:include>

    <p class="font_header">
    <b>User Preferences Administration - Select User</b>
    </p>
    <form method="post" action="<%=action%>" name="userSelection">
        <input type="hidden" name="type" value="<%=valueInt%>">
        <input type="hidden" name="caller" value="<%=caller%>">
        <table width="450">
            <tr>
                <td>
                    <hr>
                </td>
            </tr>
            <tr>
                <td bgcolor="#5E60BD" class="white_text"><b>&nbsp;Please select a user:</b></td>
            </tr>
            
			<%= bodyText %>
            
            <tr>
                <td>
                    <hr>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center">
                    <input type="submit" name="Submit" value="Submit">
                </td>
            </tr>  
        </table>
    </form>

</html>

