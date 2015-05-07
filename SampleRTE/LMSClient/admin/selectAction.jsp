<%@page import = "java.util.*, java.io.*, org.adl.samplerte.server.*" %>
<!--
/*******************************************************************************
**
** Filename: selectAction.jsp
**
** File Description: This page provides links for user to select desired action.
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
-->

<html>
<head>
<title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - 
    Global Objectives Action Selection</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<META http-equiv="expires" CONTENT="Tue, 05 DEC 2000 01:00:00 GMT">
<META http-equiv="Pragma" CONTENT="no-cache">
<link href="/adl/includes/sampleRTE_style.css" rel="stylesheet" type="text/css">
<script language="JavaScript">
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
</head>


<body bgcolor="#FFFFFF" style="margin-top: 4.5em;">

<jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
    <jsp:param value="/adl/help/globalObjectivesHelp.htm" name="helpURL"/>
</jsp:include>


<p class="font_header">
<b>
Global Objectives Administration Action Selection
</b>
</p>
<table width="450" border="0">
   <tr>
      <td colspan="2">
         <hr>
      </td>
   </tr>
   <tr>
      <td colspan="2" bgcolor="#5E60BD" CLASS="white_text">
         <b>
            &nbsp;Global Objectives Administration Actions:
         </b>
      </td>
   </tr>
   <tr>
      <td>
         <a href="/adl/LMSCourseAdmin?type=<%= ServletRequestTypes.SELECT_OBJ_USER %>&setOption=create">
         			Create a new global objective
         </a>
      </td>
   </tr>
   <tr>
      <td>
         <a href="/adl/LMSCourseAdmin?type=<%= ServletRequestTypes.SELECT_OBJ_USER %>&setOption=modify">
         			View or modify an existing global objective
         </a>
      </td>
   </tr>
</table>
<table width="450" border="0">
   <tr>
      <td COLSPAN="2">
         <hr>
      </td>
   </tr>
   </table>
</body>
</html>

