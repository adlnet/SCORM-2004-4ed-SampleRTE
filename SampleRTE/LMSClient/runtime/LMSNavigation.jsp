<!-- 
   /***************************************************************************
   **
   ** Filename:  LMSNavigation.jsp
   **
   ** File Description: This file displays the LMS Nav Menu.
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
-->

<%@ page contentType="text/html;charset=utf-8" %>
<%@page import = "java.sql.*, java.util.*, java.io.*, org.adl.samplerte.util.*, org.adl.samplerte.server.*" %>
<% String theWebPath = getServletConfig().getServletContext().getRealPath( "/" ); %>
<% 
    String url = request.getParameter("helpURL");
    String helpLink = "";

    if (!url.equals(""))
    {
       helpLink = "|&nbsp;<a href=\"javascript:newWindow('" + url + "');\">Help</a>&nbsp;";
    }
    else
    {
       helpLink = "";
    }

%>

   <script type="text/javascript">   
//<!-- [CDATA[

   /****************************************************************************
   **
   ** Function:  newWindow()
   ** Input:   pageName
   ** Output:  none
   **
   ** Description:  This function opens the help window
   **
   ***************************************************************************/
   function newWindow( pageName )
   {
      window.open(pageName, 'Help',
      "toolbar=no,location=no,directories=no,status=no,menubar=no," +
      "scrollbars=yes,resizable=yes,width=500,height=575");
   }
   //]]-->
   </script>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
<meta http-equiv="expires" content="Tue, 20 Aug 1999 01:00:00 GMT" />
<meta http-equiv="Pragma" content="no-cache" />
<title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - 
    Navigation</title>

<link href="/adl/includes/sampleRTE_style.css" rel="stylesheet" type="text/css" />
</head>

<body>
<input type="hidden" id="path" value="<%=theWebPath%>" />

<%
   // Get the user's information and remove any course IDs
   String userid = (String)session.getAttribute( "USERID" );
   String admin = (String)session.getAttribute( "RTEADMIN" );
   String username = (String)session.getAttribute( "LOGINNAME" );
   session.removeAttribute( "COURSEID" );
   session.removeAttribute( "TOC" );
%>

    <table width="700">
       <tr>
          <td align="left">
             <a id="homelink" href="/adl/LMSCourseAdmin?type=<%= ServletRequestTypes.GO_HOME %>&userID=<%= userid %>">
             	Home
             </a>
          </td>
          <td align="right">
             Welcome, <%=(String)session.getAttribute( "LOGINNAME" )%>!
             &nbsp;|&nbsp;
             <a href="/adl/LMSUserAdmin?type=<%= ServletRequestTypes.GET_PREF %>&userId=<%= userid %>&caller=viewUserPref">
             	Account
             </a>
             &nbsp;
             <%= helpLink %>
             |&nbsp;
             <a href="/adl/runtime/logout.jsp">
                Logout
             </a>
          </td>          
       </tr>
       <tr>
          <td colspan="2">
             <hr />
          </td>
       </tr>
    </table>

</body>
</html>
