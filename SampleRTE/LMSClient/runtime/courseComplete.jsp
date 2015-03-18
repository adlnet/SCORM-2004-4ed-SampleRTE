<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<%@page import = "java.util.*, org.adl.samplerte.util.*,
                  org.adl.samplerte.server.*" %>

<%
   /***************************************************************************
    **
    ** Filename:  courseComplete.jsp
    **
    ** File Description:     
    **
    ** This file displays a Course Complete message when a user completes
    ** a course or when a user selects a course which they have 
    ** already completed.
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
   String userid = (String) session.getAttribute("USERID");
   String admin = (String) session.getAttribute("RTEADMIN");
   String contrl = (String) session.getAttribute("CONTROL");
%>
	<head>
		<title>SCORM 2004 4th Edition Sample Run-Time Environment
			Version 1.1.1 - Course Complete</title>
	<meta http-equiv="Content-Type"	content="text/html; charset=iso-8859-1" />
		<link href="../includes/sampleRTE_style.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript">
   //<!-- [CDATA[
   
   /****************************************************************************
   **
   ** Function:  handleAuto()
   ** Input:   none
   ** Output:  none
   **
   ** Description: Handles navigation back to the main menu page. 
   **
   ** Issues:  
   **
   ***************************************************************************/  
   function handleAuto()
   {
      window.opener.top.frames[2].location.href = "/adl/LMSCourseAdmin?type=<%= ServletRequestTypes.GO_HOME %>&userID=<%= userid %>";
      window.close();
   }
   ]]-->
   </script>
	
   </head>

	<body>
      
		<script type="text/javascript" src="BrowserDetect.js"></script>
				
		<script type="text/javascript">
		//<!-- [CDATA[
        var scoWinType = typeof(window.opener)
		var codeLoc = 'http://'+ window.document.location.host + '/adl/runtime/' + 'code.jsp';
		var _Debug = false;
		

		if ( scoWinType != "undefined" && scoWinType != "unknown" && !Firefox ) 
		{  
		   ctrl = window.opener.top.frames['LMSFrame'].document.forms['buttonform'].control.value;
		}
		else
		{
		   ctrl = window.top.frames['LMSFrame'].document.forms['buttonform'].control.value;
		}
		
		if (ctrl == "auto")
		{
			document.writeln("<p><a href='javascript:handleAuto();'>Go Back To Main Menu</a></p>");
		}
		else
		{
		   document.writeln("<p><a href='/adl/LMSCourseAdmin?type=<%= ServletRequestTypes.GO_HOME %>&userID=<%= userid %>'>Go Back To Main Menu</a></p>");
		}
			window.top.frames['LMSFrame'].document.forms['buttonform'].next.style.visibility = "hidden";
			window.top.frames['LMSFrame'].document.forms['buttonform'].previous.style.visibility = "hidden";
			window.top.frames['LMSFrame'].document.forms['buttonform'].suspend.disabled = false;
			window.top.frames['LMSFrame'].document.forms['buttonform'].suspend.style.visibility = "hidden";	
	
			window.parent.frames['code'].document.location.href = codeLoc;
		
		window.top.frames['LMSFrame'].document.forms['buttonform'].quit.disabled = false;
		window.top.frames['LMSFrame'].document.forms['buttonform'].quit.style.visibility = "hidden";
         ]]-->
      </script>

		<div id="step_1" class="font_header">
			You Have Completed This	Course
			
		</div>
		 <p>
			&nbsp;
		    <br />
            <br />
         </p>
		
		<table width="400" border="1" cellpadding="25">
			<tr>
				<td>
					NOTES:
					<br />
					This page indicates that the ADL Sequencer has determined that you
					have fulfilled all the requirements necessary to complete this
					course.
				</td>
			</tr>
		</table>

	</body>
</html>
