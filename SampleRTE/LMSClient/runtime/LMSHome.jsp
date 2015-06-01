<%@page import = "java.sql.*, java.util.*, org.adl.samplerte.util.*,
    org.adl.samplerte.server.*" %>
<%
   /***************************************************************************
   **
   ** Filename:  LMSHome.jsp
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

<%
   String actionType = "";       
   int typeVar = 0;
   String theWebPath = getServletConfig().getServletContext().
                       getRealPath( "/" );

   // Get course data
   Vector courses = (Vector)request.getAttribute("courses");
   String userID = (String)session.getAttribute( "USERID" );
   String isUserAdmin = (String)session.getAttribute( "RTEADMIN" );
   
   // Set admin session information
   if ( (! (isUserAdmin == null)) && ( isUserAdmin.equals("true")) )
   {
      // Sets a new Session Variable to Secure Admin pages
      session.putValue("AdminCheck", new String("true"));
   }
   
   String showAdminTable = (String)request.getAttribute( "showAdminTable" );
   String showRegTable = (String)request.getAttribute( "showRegTable" );
   String showUnregTable = (String)request.getAttribute( "showUnregTable" );

   String adminTableDisplayVal = showAdminTable.equals("true") ? "float:left" : "display:none";
   String regTableDisplayVal = showRegTable.equals("true") ? "float:left" : "display:none";
   String unregTableDisplayVal = showUnregTable.equals("true") ? "float:left" : "display:none";
   
   // Get results data if available
   String reqOp = (String)request.getAttribute("reqOp");
   String result = (String)request.getAttribute("result");
   
   String resultBody = "";
   String regformBody = "";
   String unregformBody = "";

   String regColor = "#FFFFFF";
   String unregColor = "#FFFFFF";
     
   if ( reqOp != null && result != null )
   {
      resultBody = (result.equals("true")) ? (reqOp + " was successful.") : (reqOp + " was not successful."); 
   }
   
   int regCount = 0;
   int unregCount = 0;
   
   for( int i = 0; i < courses.size(); i++ )
   {
      CourseData cd = (CourseData)courses.elementAt(i);
      String courseID = cd.mCourseID;
           
      String courseTitle = cd.mCourseTitle;            
      String importDateTime = cd.mImportDateTime; 
      String name = (String)request.getAttribute("name");
      
      boolean start = cd.mStart;
      boolean TOC = cd.mTOC;
      boolean resume = cd.mSuspend ;
      
      boolean registered = cd.mRegistered;
      
      String status = "";
      
      String checked = "";
      
      String launch = "";
      
      if (!start && !resume && !TOC)
      {
         launch = "<td colspan='2' align='center'>" + 
		               "<br/>" + "<a href='LMSCourseAdmin?type=44&courseID=" + courseID + "&userID=" + userID +"'>View Details</a>" +
		            "</td>";
      }
      else
      {
	      String startCourse = (start) ? "<a href='runtime/sequencingEngine.jsp?courseID="
	            + courseID + "&courseTitle=" + courseTitle +"'>Start Course</a>" : "Start Course";
	            
		  startCourse = (resume) ? "<a href='runtime/sequencingEngine.jsp?courseID="
	            + courseID + "&courseTitle=" + courseTitle +"'>Resume Course</a>" : startCourse;           
	            
		  String toc = (TOC) ? "<a href='runtime/sequencingEngine.jsp?courseID="
	            + courseID + "&courseTitle=" + courseTitle + "&viewTOC=true" +"'>View Table Of Contents</a>" : "View Table Of Contents";
	     launch = "<td colspan='2' align='center'>" + 
		              "<br/>" + startCourse + "&nbsp;&nbsp;|&nbsp;&nbsp;" + toc +  
		           "</td>";
      }
	  // Registered Courses      
      if(registered)
      {
         status = "'status" + i + "'";
         
         regformBody += 
            "<tr bgcolor='" + regColor + "'>" + 
            	"<td>" +
         			"<input type='checkbox' name='RE_" + courseID + "' id='" + courseID + "' value='0'" + "/>" +
         		"</td>" + 
         		"<td colspan='2'>" +
         			"<b><label for='" + courseID+ "'>" + courseTitle + "</label></b>" +
         			"<br/>Imported On: " + importDateTime +         			
         		"</td>" +         	 
         		launch +
         	"</tr>" + 
         	((!start && !resume && !TOC) ?
        	   "<tr bgcolor='" + regColor + "'>" + 
               "<td>&nbsp;</td>" +
               "<td colspan='4'>" +
                  "<em>View Details to see course status</em>" +
               "</td>" +
            "</tr>" 
	      	:
	      	"<tr bgcolor='" + regColor + "'>" + 
               "<td>&nbsp;</td>" +
               "<td colspan='4'>" +
                  "<a href=\"javascript:showHideStatus(" + status + ", '" + courseID + "')\">Show/Hide Course Status</a>" +
               "</td>" +
            "</tr>"
            ) +
	      	"<tr class = " + status + " style='display: none' bgcolor='" + regColor + "'>" +
	      		"<td>&nbsp;</td>" +       	 
	      		"<td><b><i>Satisfied</i></b></td>" +
      			"<td><b><i>Measure</i></b></td>" +
   				"<td><b><i>Completed</i></b></td>" +
   				"<td><b><i>Progress Measure</i></b></td>" +
      		"</tr>" +
		    "<tr class = " + status + " id = 'values' style='display: none' bgcolor='" + regColor + "'>" +
		    	"<td>&nbsp;</td>" +         	 
		      	"<td id = '" + courseID + "satisfied'></td>" +
		   		"<td id = '" + courseID + "measure'></td>" +
				   "<td id = '" + courseID + "completed'></td>" +
				   "<td id = '" + courseID + "progmeasure'></td>" +
		   	"</tr>";
      		
         	
         	if(regColor.equals("#FFFFFF"))
            {
         	   regColor = "#ececec";
            }
            else
            {
               regColor = "#FFFFFF";
            }
            regCount++;
      }
	  // Un-Registred Courses
      else
      {
         unregformBody += 
            "<tr bgcolor='" + unregColor + "'>" + 
            	"<td>" +
         			"<input type='checkbox' name='UN_" + courseID + "' id='" + courseID + "' value='0'" + "/>" +
         		"</td>" + 
         		"<td colspan='4'>" +
         			"<b><label for='" + courseID+ "'>" + courseTitle + "</label></b>" +
         			"<br/>Imported On: " + importDateTime +         			
         		"</td>" +
         	"</tr>";
         	
         	if(unregColor.equals("#FFFFFF"))
            {
         	   unregColor = "#ececec";
            }
            else
            {
               unregColor = "#FFFFFF";
            }
            unregCount++;
      }

   }
   
   if ( regCount <= 0 )
   {
      regformBody +=
         "<tr bgcolor='#FFFFFF'>" + 
             "<td height='30px' colspan='5' align='center'>" +
                "No Registered Courses Found<br />" +                     
             "</td>" +            
         "</tr>";
   }
   
   if ( unregCount <= 0 )
   {
      unregformBody += 
         "<tr bgcolor='#FFFFFF'>" + 
             "<td height='30px' colspan='5' align='center'>" +
                 "No Available Courses Found<br />" +                     
             "</td>" +            
         "</tr>";
   }
   
   // Add sort options
   
   if ( regCount >= 2 )
   {
      regformBody = 
         "<tr>" +
             "<td colspan=\"5\" align=\"right\">" +
                 "<font size='1'>Sort By:&nbsp;" + 
                    "<a href=\"/adl/LMSCourseAdmin?type=" + ServletRequestTypes.PROC_SORT_COURSE + "&userID=" + userID + "&sortType=RE_timestamp\">Timestamp</a>" + 
                    " | " + 
                    "<a href=\"/adl/LMSCourseAdmin?type=" + ServletRequestTypes.PROC_SORT_COURSE + "&userID=" + userID + "&sortType=RE_name\">Name</a>" + 
                 "</font>" +
             "</td>" +
         "</tr>" + regformBody;
   }
   
   if ( unregCount >= 2 )
   {
      unregformBody = 
         "<tr>" +
             "<td colspan=\"5\" align=\"right\">" +
                "<font size='1'>Sort By:&nbsp;" + 
                   "<a href=\"/adl/LMSCourseAdmin?type=" + ServletRequestTypes.PROC_SORT_COURSE + "&userID=" + userID + "&sortType=UN_timestamp\">Timestamp</a>" + 
                   " | " + 
                   "<a href=\"/adl/LMSCourseAdmin?type=" + ServletRequestTypes.PROC_SORT_COURSE + "&userID=" + userID + "&sortType=UN_name\">Name</a>" + 
                "</font>" +
             "</td>" +
         "</tr>" + unregformBody;
   }

%>
<!doctype html">

<html lang="en">
<head>
<title>Home</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta http-equiv="expires" content="Tue, 05 DEC 2000 01:00:00 GMT" />
<meta http-equiv="Pragma" content="no-cache" />
<link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
<link href="includes/sampleRTE_style.css" rel="stylesheet" type="text/css" />
<script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

<style type="text/css">
   .container
   {
      margin-left: 15px;
   }

	table.sep
	{
		border-collapse: collapse
	}
	
</style>


<script type="text/javascript">
//<!-- [CDATA[

var codeLoc = 'http://'+ window.document.location.host + '/adl/runtime/' + 'code.jsp';

/****************************************************************************
**
** Function:   newWindow()
** Input:   pageName - String - The page that will be launched in the new
**                              window.  At this time, only the help page.
** Output:  none
**
** Description:  Launches a new window with additional user help
**
***************************************************************************/  
function newWindow(pageName)
{
   window.open(pageName, 'Help', 
   "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes," +
   "resizable=yes,width=500,height=500");
}

/****************************************************************************
**
** Function:   regCheck()
** Output:  none
** Description:  selects or deselects all registered courses
**
***************************************************************************/
function regCheck()
{
	var checks = document.getElementsByTagName("input");
	var currStatus = document.getElementById("registeredCheck").checked;

	for ( var i = 0; i < checks.length; i++ )
	{
		if ( (checks[i].type == "checkbox") && checks[i].name.substring(0,3) == 'RE_')
		{
			checks[i].checked = currStatus;
		}
	}	
}

/****************************************************************************
**
** Function:   unregCheck()
** Output:  none
** Description:  selects or deselects all available courses
**
***************************************************************************/
function unregCheck()
{
	var checks = document.getElementsByTagName("input");
	var currStatus = document.getElementById("unregisteredCheck").checked;

	for ( var i = 0; i < checks.length; i++ )
	{
		if ( (checks[i].type == "checkbox") && checks[i].name.substring(0,3) == 'UN_')
		{
			checks[i].checked = currStatus;
		}
	}	
}

var rowVisible = false;

/****************************************************************************
**
** Function:   showHideStatus()
** Input: status - id of course status to be hidden or shown
** Output:  none
** Description:  hides or shows the given status
**
***************************************************************************/
function showHideStatus(status, courseid) 
{
	var rows = registeredCoursesTable.rows;
	for (i = 0; i < rows.length; i++) 
	{		
	   if (rows[i].className == status) 
	   {
         if ( rows[i].id == "values" )
         {
           getStatus(rowVisible, courseid);
         }

		   rows[i].style.display = (rowVisible) ? "none" : "";
	   }
      
	}
    rowVisible = !rowVisible;
}

/****************************************************************************
**
** Function:   getStatus()
** Input: visible - value indicating whether or not the status is shown
** Input: courseId - Id of course whose status should be displayed
** Output:  none
** Description:  gets the given status
**
***************************************************************************/
function getStatus(visible, courseid)
{
   var therequest = getTheRequestObj();
   if ( therequest )
   {
      therequest.onreadystatechange=function()
      {
         if ( therequest.readyState==4 )
         {
            if ( ! visible )
            {
            	var xmlDoc = therequest.responseXML.documentElement;
               document.getElementById(courseid + "satisfied").innerHTML =  xmlDoc.getElementsByTagName("satisfied")[0].childNodes[0].nodeValue;
               document.getElementById(courseid + "measure").innerHTML =  xmlDoc.getElementsByTagName("measure")[0].childNodes[0].nodeValue;
               document.getElementById(courseid + "completed").innerHTML =  xmlDoc.getElementsByTagName("completed")[0].childNodes[0].nodeValue;
               document.getElementById(courseid + "progmeasure").innerHTML =  xmlDoc.getElementsByTagName("progmeasure")[0].childNodes[0].nodeValue;               
            }
         }
      }
      therequest.open("Get","/adl/LMSCourseAdmin?type=status&userID=<%= userID %>&courseID="+courseid, false);
      therequest.send(null);
   }
} 

/****************************************************************************
**
** Function:   setSessionInfo()
** Input: varName - name of variable to be set
** Input: varValue - value of variable to be set
** Output:  none
** Description:  sets variables for current session
**
***************************************************************************/
function setSessionInfo(varName, varValue)
{
    var therequest = getTheRequestObj();
    if ( therequest )
	{
        therequest.open("Get","/adl/LMSCourseAdmin?type=sessionInfo&" + varName + "=" + varValue + "&userID=<%=userID %>", true);
	    therequest.send(null);
	}
}

/****************************************************************************
**
** Function:   getTheRequestObj()
** Output:  none
** Description:  creates a generic request object
**
***************************************************************************/
function getTheRequestObj()
{
    try
    {
        //firefox
        return new XMLHttpRequest();
    }
    catch (e)
    {
        try
        {
            //IE
            return new ActiveXObject("Msxml2.XMLHTTP");
        }
        catch (e)
        {
            alert("no ajax support");
            return false;
        }
    }
}

/****************************************************************************
**
** Function:   showHideAdminTable()
** Output:  none
** Description:  shows or hides Admin options table and sets corresponding variables
**
***************************************************************************/
function showHideAdminTable()
{
	var tableVisible = (document.getElementById("adminOptionsTable").style.display != "none");
    document.getElementById("adminOptionsTable").style.display = 
        tableVisible ? "none" : "";
    tableVisible = !tableVisible;

    setSessionInfo("adminDisplayStatus", tableVisible);
}

/****************************************************************************
**
** Function:   showHideRegTable()
** Output:  none
** Description:  shows or hides reg courses table and sets corresponding variables
**
***************************************************************************/
function showHideRegTable()
{
    var tableVisible = (document.getElementById("registeredCoursesTable").style.display != "none");
    document.getElementById("registeredCoursesTable").style.display = 
        tableVisible ? "none" : "";
    tableVisible = !tableVisible;

    setSessionInfo("regDisplayStatus", tableVisible);
}

/****************************************************************************
**
** Function:   showHideUnregTable()
** Output:  none
** Description:  shows or hides reg courses table and sets corresponding variables
**
***************************************************************************/
function showHideUnregTable()
{
    var tableVisible = (document.getElementById("unregisteredCoursesTable").style.display != "none");
    document.getElementById("unregisteredCoursesTable").style.display = 
        tableVisible ? "none" : "";
    tableVisible = !tableVisible;

    setSessionInfo("unregDisplayStatus", tableVisible);
}

/****************************************************************************
**
** Function:   setType()
** Input: buttonName - name of button pressed
** Output:  none
** Description:  sets type of registration operation to be performed based on
** the button pressed
**
***************************************************************************/
function setType(process /*buttonName*/)
{
	typeVar = process;
	
	document.getElementById("type").value = typeVar;
}

/****************************************************************************
**
** Function:   clearTopFrame()
** Input: none
** Output:  none
** Description:  clears all UI controls from the top frame
**
***************************************************************************/
function clearTopFrame()
{
   //Hide or display the relevant controls
   //if( document.all != null && 
   //      window.parent.frames.LMSFrame != null && 
   //      window.top.frames.LMSFrame.document != null && 
   //      window.parent.frames.code.document != null)
   try {

      window.parent.frames.LMSFrame.document.forms.buttonform.suspend.style.visibility="hidden";
      window.parent.frames.code.document.location.href = codeLoc;
      window.top.frames.LMSFrame.document.forms.buttonform.next.style.visibility = "hidden";
      window.top.frames.LMSFrame.document.forms.buttonform.previous.style.visibility = "hidden";
      window.top.frames.LMSFrame.document.forms.buttonform.quit.style.visibility = "hidden";
   } catch (e) {}
}

/**************************************************************************
*  function to confirm that user really wants to clear the database
*
***************************************************************************/
function confirmClearDatabase()
{
    if( confirm("Do you really want to remove all the course information?") )
    {
       var path = document.getElementById("path").value;
       window.parent.frames.Content.document.location.href = 
          "/adl/LMSCourseAdmin?type=<%= ServletRequestTypes.CLEAR_DB %>&path=" + path + "&userID=<%= userID %>";
    }
}

/**************************************************************************/

function checkForm()
{
   var prefix = "";

   if ( typeVar == <%=ServletRequestTypes.PROC_REG_COURSE%> || 
         typeVar == <%=ServletRequestTypes.PROC_REG_DELETE%> )
   {
      prefix = "UN_";
   }
   else if ( typeVar == <%=ServletRequestTypes.PROC_UNREG_COURSE%> ||
		     typeVar == <%=ServletRequestTypes.PROC_UNREG_DELETE%> ||
             typeVar == <%=ServletRequestTypes.PROC_RESET_COURSE%> )
   {
      prefix = "RE_";
   }

   var checks = document.getElementsByTagName("input");
	   
   for ( var i = 0; i < checks.length; i++ )
   {
       if ( (checks[i].type == "checkbox") && checks[i].name.substring(0,3) == prefix)
       {
           if ( checks[i].checked == true )
           {
                return true;
           }
       }
   }

   alert( "No Courses Selected!" );
   return false;
}

/****************************************************************************
**
** Function:   doRefresh()
** Input: none
** Output:  none
** Description:  refreshes page if page was loaded from back button call
**
***************************************************************************/
function doRefresh()
{
   var e = document.getElementById("refreshed");

   if( e.value == "no" )
   {
      e.value = "yes";
   }
   else
   {
      e.value = "no";
      location.reload();
   }
}

//]]-->
</script>
</head>

<body bgcolor="#FFFFFF" onload="clearTopFrame();doRefresh();" style="width:700px; margin-left:15px" >
   <div class="container">
    <jsp:include page="LMSNavigation.jsp" flush="true">
        <jsp:param value="" name="helpURL"/>
    </jsp:include>

    <div class="row" style="margin-top:4.5em;">
	    <div class="col-md-12" id="results">
	        <p class="font_header">
	            <%= resultBody %>
	        </p>
	    </div>
	 </div> 

   <%
   if ( (! (isUserAdmin == null)) && ( isUserAdmin.equals("true")) )
   {
       // Sets a new Session Variable to Secure Admin pages
    session.putValue("AdminCheck", new String("true"));
    %>

    <div class="row" id="adminOptions">
       <div class="col-md-12">
          <b><font size='3'>Administrative Options</font></b><font size='1'>&nbsp;<a href="javascript:showHideAdminTable()">Show/Hide</a>&nbsp;</font>
       </div>
       <table class="table" id="adminOptionsTable" width="700" class="sep" border="0" style="<%=adminTableDisplayVal %>">
          
          <tr>
             <td width="250" colspan="3">
                <a href="/adl/import/importCourse.jsp">Import Course</a>
             </td>
          </tr>  
          <tr>
             <td width="250">
                <a href="/adl/import/createCourse.jsp">Create Course</a>
             </td>
             <td width="200">
                <a href="/adl/LMSUserAdmin?type=<%= ServletRequestTypes.NEW_USER %>&setProcess=pref">Add Users</a>
             </td>
             <td>
                <a href="/adl/LMSUserAdmin?type=<%= ServletRequestTypes.GET_USERS %>&setProcess=allCourse&caller=adminCourseStatus">All User's Course Status</a>
             </td>
          </tr>
          <tr>
             <td>
               <a href="/adl/LMSCourseAdmin?type=<%= ServletRequestTypes.GET_EDIT_COURSES %>">Edit Course</a>
             </td>            
             <!-- <td>
                <a href="/adl/import/choseMultipleFilesImport.jsp">Import Multiple Courses</a>
             </td> -->
             <td>
                <a href="/adl/LMSUserAdmin?type=<%= ServletRequestTypes.GET_USERS %>&setProcess=delete">Delete Users</a>
             </td>
             <td>
                <a href="/adl/admin/selectAction.jsp">Global Objectives Administration</a>
             </td>             
          </tr>
          <tr>
             <td>
                <a href="/adl/LMSCourseAdmin?type=<%= ServletRequestTypes.GET_COURSES %>&setProcess=manage">Manage Courses</a>
             </td>
             <td>
                <a href="/adl/LMSUserAdmin?type=<%= ServletRequestTypes.GET_USERS %>&setProcess=pref&caller=adminUserPref">Manage Users</a>
             </td>     
             <td>
                <a href="javascript:confirmClearDatabase()">Clear Database</a>
             </td>
          </tr>
       </table>    
    </div>

    <%
        }
    %>

	 <form name="courseRegForm" method="post" action="/adl/LMSCourseAdmin" onSubmit="return checkForm()">

   		<input type="hidden" id="type" name="type" value="" />
   		<input type="hidden" name="path" value="<%=theWebPath%>" />
   		<input type="hidden" name="userID" value="<%=userID%>" />
         <input type="hidden" id="refreshed" value="no" />

		<div class="row" id="registeredCourses">
			<div class="col-md-12">
            <b><font size='3'>Registered Courses</font></b><font size='1'>&nbsp;<a href="javascript:showHideRegTable()">Show/Hide</a>&nbsp;</font>
         </div>
			<table class="table" id="registeredCoursesTable" width="700" class="sep" border="0" style="<%=regTableDisplayVal %>">
   			<tr>
   				<td style="border-right-style: none;" align="left">
   					<!-- Checkbox for select/deselect all -->
					<input id="registeredCheck" type="checkbox" value="0" onclick="javascript:regCheck();"/> All
   				</td>
   				   <td style="border-left-style: none;" colspan="4" align="right"> 
                    <input type="submit" id="reset" name="reset" value="Reset" onClick="javascript:setType(<%=ServletRequestTypes.PROC_RESET_COURSE%>);" />
    					<input type="submit" id="submit" name="submit" value="UnRegister" onClick="javascript:setType(<%=ServletRequestTypes.PROC_UNREG_COURSE%>);" />
    					<% if ( (! (isUserAdmin == null)) && ( isUserAdmin.equals("true")) )
    				       {
                        %>                  
                              <input type="submit" id="submit" name="submit" value="Delete" onClick="javascript:setType(<%=ServletRequestTypes.PROC_UNREG_DELETE%>);return confirm('Are you sure you want to delete?')" />
                        <%
                           }
                        %>                  
    				</td>
   			</tr>
   			<div id="registeredBody">
    				<%= regformBody%>     
   			</div>
			</table>	
		</div>
	
      <div id="spacer2">
          &nbsp;<br />
      </div>
   
		<div class="row" id="unregisteredCourses">
			<div class="col-md-12">
            <b><font size='3'>Available Courses</font></b><font size='1'>&nbsp;<a href="javascript:showHideUnregTable()">Show/Hide</a>&nbsp;</font>
         </div>
			<table class="table" id="unregisteredCoursesTable" class="sep" width="700" style="<%=unregTableDisplayVal %>">
            <tr>
					<td style="border-right-style: none;" align="left">
						<!-- Checkbox for select/deselect all -->
						<input id="unregisteredCheck" type="checkbox" value="0" onclick="javascript:unregCheck();"/> All
					</td>
    				<td style="border-left-style: none;" colspan="4" align="right"> 
    					<input type="submit" id="submit" name="submit" value="Register" onClick="javascript:setType(<%=ServletRequestTypes.PROC_REG_COURSE%>);"/>
    					<% if ( (! (isUserAdmin == null)) && ( isUserAdmin.equals("true")) )
                     {
                  %>                  
                        <input type="submit" id="submit" name="submit" value="Delete" onClick="javascript:setType(<%=ServletRequestTypes.PROC_REG_DELETE%>);return confirm('Are you sure you want to delete?')" />
                  <%
                     }
                  %>
    				</td>
    			</tr>		
	   			<div id="unregisteredBody">
     				<%= unregformBody%>     
    			</div>
			</table>
	
		</div>
		<br/>
   	</form>
   </div>
</body>
</html>
