<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<%@ page contentType="text/html;charset=utf-8" %>

<%@page import = "java.sql.*, java.util.Vector, 
                  org.adl.samplerte.util.*,
                   org.adl.samplerte.server.CourseData,
                  org.adl.samplerte.server.ServletRequestTypes" %>
<%
   /***************************************************************************
   **
   ** Filename:  dsp_seletCourse.jsp
   **
   ** File Description:     
   ** 
   ** This file shows a user which courses 
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
%>
<head>
<script type="text/javascript">
<!--[CDATA[
/****************************************************************************
**
** Function:  checkValues()
** Input:   none
** Output:  boolean
**
** Description:  This function ensures that a course is selected
**               before submitting.
**
***************************************************************************/
function checkValues()
{  
    var course_selected = "false";

    for (i=0; i<document.courseSelectForm.courseID.length; i++)
    {
    	if (document.courseSelectForm.courseID[i].checked)
    	{
    		course_selected = "true";
    	}
    }

   if ( course_selected == "false" )
   {
       alert('Please Select a Course.');
       return false;
   }

   return true;
}


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
//]]-->
</script>
<%
    String formBody = "";
    String userID = (String)request.getAttribute( "userId" );
    Vector courses = (Vector)request.getAttribute("courses");
    String checked = "checked";
    String courseID = "";
    String caller = (String)request.getAttribute("caller");
    String helpPage="/adl/help/viewStatusHelp.htm";
    
    
    if( caller.equals("adminCourseStatus") ) 
    {
       helpPage = "/adl/help/userCourseStatusHelp.htm";       
    }
    
    int i;
    for( i = 0; i < courses.size(); i++ )
    {
       CourseData cd = (CourseData)courses.elementAt(i);
       courseID = cd.mCourseID;
       String courseTitle = cd.mCourseTitle;
       String importDateTime = cd.mImportDateTime;
      

       formBody += "<tr><td size='5%'>" +
                   "<input type='radio' name='courseID' id='" + courseID + 
                   "' value='" + courseID +"'" + checked + 
                   "/></td><td><label for='" + courseID + "'><p>" +   
                   courseTitle + "<br>Imported on: " + importDateTime +
                   "</p></td></tr>";
       checked = "";

    }
    
    if ( courses.size() <= 0 )
    {
       formBody += "<tr><td><br /><p class='font_header'>No Courses Found!</p></td></tr>";
    }
    
        formBody += "<tr><td colspan=\"6\"><hr></td></tr>";
%>
   <title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - Course Selection</title>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
   <meta http-equiv="expires" content="Tue, 05 DEC 2000 01:00:00 GMT" />
   <meta http-equiv="Pragma" content="no-cache" />
   <link href="/adl/includes/sampleRTE_style.css" rel="stylesheet" type="text/css" />
</head>
   <body style="margin-top: 4.5em;">

   <jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
       <jsp:param value="<%= helpPage%>" name="helpURL"/>
   </jsp:include>

   <p class="font_header">
   <b>Course Status Selection</b>
   </p>
 
      <form id="courseSelectForm" method="post" action="/adl/LMSCourseAdmin">
      <div>
         <input type="hidden" name="type" value="<%= ServletRequestTypes.VIEW_MY_STATUS %>" />
         <input type="hidden" name="userID" value="<%=userID%>" />
      </div>
      <table width="548" border="0">
         <tr>
            <td colspan="6">
               <hr />
            </td>
         </tr>
         <tr>
            <td colspan="6" class="white_text"><b>
                &nbsp;To review course status information, select a course below.
             </b></td>
         </tr>
     
         <%= formBody%>
      
      </table>
      <% if ( courses.size() > 0 ) 
         {%>
         <table width="547" border="0">
            <tr>
               <td class="course45">&nbsp;</td>
               <td >
                  <input type="submit" id="submit" value="Submit" />
               </td>
            </tr>
         </table>
      <% } %>
   </form>
   </body>
</html>
