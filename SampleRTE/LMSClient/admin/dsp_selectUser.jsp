<%@ page contentType="text/html;charset=utf-8" %>

<%@page import = "java.util.Vector, 
                  org.adl.samplerte.server.*" %>
<%
   /***************************************************************************
   **
   ** Filename:  dsp_selectUser.jsp
   **
   ** File Description:     
   ** 
   ** This file defines the selectUser.jsp that shows an administrator which 
   ** users are available to administer global objectives on.
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
<SCRIPT LANGUAGE="javascript">
   <!--
   /*************************************************************************
   * Method: newWindow()
   * Input: pageName
   * Output: none
   *
   * Description: Opens the page input by 'pageName' in a new browser window.
   *************************************************************************/
   function newWindow(pageName)
   {
      window.open(pageName, 'Help', "toolbar=no,location=no,directories=no," +
                  "status=no,menubar=no,scrollbars=yes,resizable=yes," +
                  "width=500,height=500");
   }
   // -->
</SCRIPT>
<%
   Vector users = (Vector)request.getAttribute("users");
   String setOption = (String)request.getAttribute("setOption");

   // Loops through the set of all users and the body of the table is  
   // formed by assigning it to the string 'formBody'.
   String formBody = "";
   String color = "#FFFFFF";
   String userID = "";
   String lastName = "";
   String firstName = "";
   String checked = "checked";
   UserProfile up = new UserProfile();

   int valueInt = 0;
   String action = "";
   String helpLocation = "";
   
   if( setOption.equals("create") )
   {
      valueInt = ServletRequestTypes.NEW_OBJ;
      action = "/adl/LMSCourseAdmin";
   }
   else if ( setOption.equals("modify") )
   {
      valueInt = ServletRequestTypes.COURSE_OBJ;
      action = "/adl/LMSCourseAdmin";
   }
	
   for( int i = 0; i < users.size(); i++ )
   {
      up = (UserProfile)users.elementAt(i);
      userID = up.mUserID;
      lastName = up.mLastName;
      firstName = up.mFirstName;
   
      formBody += 
         "<tr bgcolor='" + color + "'><td width='10%'>" +
         "<input type='radio' name='user' id='" + userID + "' value='" + 
         userID +"'" + checked + "/>&nbsp;&nbsp;&nbsp;</td><td><p>" +
         "<label for='" + userID + 
         "'>" + firstName + " " + lastName + "</label></p></td></tr>";
   
      if(color.equals("#FFFFFF"))
      {
         color = "#CDCDCD";
      }
      else
      {
         color = "#FFFFFF";
      }
      checked = "";
   }
%>

<html>
<head>
    <title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - 
        Global Objectives User Selection
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <meta http-equiv="expires" CONTENT="Tue, 05 DEC 2000 01:00:00 GMT">
    <meta http-equiv="Pragma" CONTENT="no-cache">
    <link href="/adl/includes/sampleRTE_style.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="#FFFFFF" style="margin-top: 4.5em;">

    <jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
       <jsp:param value="/adl/help/globalObjectivesHelp.htm" name="helpURL"/>
   </jsp:include>


    <p class="font_header">
    <b>
    Global Objectives Administration User Selection
    </b>
    </p>
    <form name="UserSelectForm" method="POST" 
        action="<%=action%>?type=<%=valueInt%>" accept-charset="utf-8">
        <table width="450" border="0">
            <tr> 
                <td colspan="2"> 
                    <hr>
                </td>
            </tr>
            <tr>
                <td colspan="2" bgcolor="#5E60BD" CLASS="white_text">
                    <b>
                        &nbsp;User:
                    </b>
                </td>
            </tr>
                <%= formBody%>
        </table>
        <table width="450" border="0">
            <tr>
                <td COLSPAN="2">
                    <hr>
                </td>
            </tr>
            <tr> 
                <td width="45%">&nbsp;</td>
                <td width="55%"> 
                    <input type="submit" name="submit" value="Submit">
                </td>
            </tr>
        </table>
    </form>
</body>
</html>
