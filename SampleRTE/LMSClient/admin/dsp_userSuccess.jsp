
<%
   String is_admin = (String)session.getAttribute( "AdminCheck" );

   // Condition statement to determine if the Session Variable has been
   // properly set.
   if ( (! (is_admin == null)) && ( is_admin.equals("true")) )
   {
%>

<%@page import = "java.util.*, org.adl.samplerte.util.*,
                  org.adl.samplerte.server.*" %>

<%@page import = "java.util.*, org.adl.samplerte.util.*" %>
<%
   /***************************************************************************
   **
   ** Filename:  dsp_userSuccess.jsp
   **
   ** File Description: This file displays a message to the user to indicate
   **                   that they have successfully updated a user's
   **                   preferences.
   **
   **
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
%>
<%
	String bodyText = "";
	if ( ((String)request.getAttribute("result")).equals("true") )
   {
    	bodyText = "The user preferences for this user have been successfully updated.";
   }
   else
   {
   	bodyText = "There was an error updating the database.";
   }
%>
<html>
<head>
   <title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - User 
   Preferences Admin</title>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

   <link href="includes/sampleRTE_style.css" rel="stylesheet" type="text/css">

</head>

   <body bgcolor="#FFFFFF" style="margin-top: 4.5em;">

   <jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
       <jsp:param value="" name="helpURL"/>
   </jsp:include>

   <table width="458" border="0">
      <tr>
         <td class="font_header">
            <b>

 					<%= bodyText %>           

            </b>
         </td>
      </tr>
   </table>
   </body>
</html>

<%
   }
   else
   {
      // Redirect the user to the RTE Home page.
      response.sendRedirect( "/adl/LMSCourseAdmin?type=" + ServletRequestTypes.GO_HOME + "&userID=");
   }
%>
