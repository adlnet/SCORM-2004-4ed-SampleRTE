<%@ page contentType="text/html;charset=utf-8" %>


<%@page import = "java.sql.*, org.adl.samplerte.util.*,
                  java.util.Vector,
                  org.adl.samplerte.server.*"%>
<%
   /***************************************************************************
   **
   ** Filename:  selectCourseObjectives.jsp
   **
   ** File Description:     
   ** 
   ** This file defines the courseRegister.jsp that shows a user which courses 
   ** they may register for and allows them to select ones to register for.
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

   String formBody = "";
   Vector courses = (Vector)request.getAttribute("courses");
   String user = (String)request.getAttribute("user");
   String checked = "";

      // Loops through the result set of all courses and if the current 
      // course is in the string 'userCourses', the string 'checked' is 
      // assigned the value "checked".  The body of the table is then 
      // formed by assigning it to the string 'formBody'.  The 'checked' 
      // string is output in the checkbox tag. If the course was in the 
      // 'userCourses' string, the checkbox will be checked.
      for( int i = 0; i < courses.size(); i++ )
      {
         CourseData cd = (CourseData)courses.elementAt(i);
         String courseID = cd.mCourseID;
         String courseTitle = cd.mCourseTitle;
      
         
         formBody += "<tr><td size='5%'>" +
                     "<input type='radio' name='course' id='" + courseID + 
                     "' value='" + courseID +"'/></td><td><p><label for='" + 
                     courseID + "'>" + courseTitle + 
                     "</label></p></td></tr>";
         
      }

      formBody += "<tr><td size='5%'>" +
                  "<input type='radio' name='course' id='notAssociated' " +
                  "value=''" + " checked /></td><td><p>" +
                  "<label for='notAssociated'>All Objectives not " +
                  "associated with a Course</label></p></td></tr>";
      formBody += "<tr><td colspan=2><hr></td></tr>";

%>

<html>
<head>
   <script language=javascript>

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
                  "width=500,height=500");
   }

 </script>
    <title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - 
        Global Objectives Course Selection</title>
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
        Global Objectives Administration Course Selection
    </b>
    </p>
    <form name="courseRegForm" 
    		 method="POST" 
    		 action="/adl/LMSCourseAdmin">
      <input type="HIDDEN" name="type" value="<%= ServletRequestTypes.OBJ_ADMIN %>">
      <input type="HIDDEN" name="user" value="<%=user%>">
        <table width="450" border="0">
            <tr> 
                <td colspan="3" height="71">
                    <p>
                        Please select a course whose 
                        Global Objectives information you
                        would like to view or modify.
                    </p>
                </td>
            </tr>
            <tr> 
                <td colspan="3"> 
                    <hr>
                </td>
            </tr>
            <tr>
                <td colspan="6" bgcolor="#5E60BD" CLASS="white_text">
                    <b>
                        &nbsp;Available Courses:
                    </b>
                </td>
            </tr>
                <%= formBody%>
        </table>
        <table width="450" border="0">
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
