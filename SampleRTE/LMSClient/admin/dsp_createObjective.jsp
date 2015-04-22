<%@ page contentType="text/html;charset=utf-8" %>

<%
   String is_admin = (String)session.getAttribute( "AdminCheck" );
   
   // Condition statement to determine if the Session Variable has been
   // properly set.
   if ( (! (is_admin == null)) && ( is_admin.equals("true")) )
   {
%>
<%@page import = "java.sql.*, java.util.Vector,
                  org.adl.samplerte.util.*,
                  org.adl.samplerte.server.*" %>
<%
   /***************************************************************************
   **
   ** Filename:  createObjective.jsp 
   **
   ** File Description:   This file allows an admin to enter information to
   **                     create a new global objective.
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
<html>
<head>
   <title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - 
   Add a Global Objective</title>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

   <link href="/adl/includes/sampleRTE_style.css" rel="stylesheet" type="text/css">
   <script language="javascript">


   /**************************************************************************
   **
   ** Function:  checkData()
   ** Input:   none
   ** Output:  none
   **
   ** Description:  This method checks to be sure data was entered.    
   ***************************************************************************/
      function checkData()
      {      
         var noErrors = true;
         var errorMessage = "";
         if ( createObjective.objectiveID.value == "" || 
              createObjective.measure.value == "" ||
              createObjective.progressmeasure.value == "")
         {
            errorMessage = errorMessage + 
                           "Please enter a value for the ObjectiveID, Measure and Progress fields.\n";
            noErrors = false;
         }
         else
         {
            if ( isNaN(createObjective.measure.value) &&
                      !(createObjective.measure.value == "unknown") )
            {
               errorMessage += "Please enter a measure value between -1.0 and 1.0 or unknown.\n";
               noErrors = false;
            }
            else if ( createObjective.measure.value < -1 ||
            			 createObjective.measure.value > 1 )
            {
            	errorMessage += "Please enter a measure value between -1.0 and 1.0 or unknown.\n";
            	noErrors = false;
            }
            if ( (isNaN(createObjective.rawscore.value) && createObjective.rawscore.value != "unknown") || 
                 (isNaN(createObjective.minscore.value) && createObjective.minscore.value != "unknown") ||
                 (isNaN(createObjective.maxscore.value) && createObjective.maxscore.value != "unknown") )
            {
               errorMessage += "Please enter a numeric value for Raw, Min and Max Scores or unknown.\n";
               noErrors = false;
            }          
            if ( isNaN(createObjective.progressmeasure.value) &&
                      !(createObjective.progressmeasure.value == "unknown") )
            {
               errorMessage += "Please enter a progress value between 0.0 and 1.0 or unknown.\n";
               noErrors = false;
            }
            else if ( createObjective.progressmeasure.value < 0 ||                   
                   createObjective.progressmeasure.value > 1 )
            {            
               errorMessage += "Please enter a progress value between 0.0 and 1.0 or unknown.\n";
               noErrors = false;
            }
            
            //if someone enters -0.0 for some reason...
            if ( createObjective.progressmeasure.value == 0.0 )
            {
               createObjective.progressmeasure.value = 0.0;
            }
         }         
         if ( !noErrors )
         {
            alert(errorMessage);
         }
         else
         {
            //good to go, submit the form
            document.getElementById('createObjective').submit();
         }
   }


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
   <body style="margin-top: 4.5em;">

   <jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
       <jsp:param value="/adl/help/globalObjectivesHelp.htm" name="helpURL"/>
   </jsp:include>

<%   
   String createObjError = (String)request.getAttribute("objErr");
   String objectiveID = "";
   String ObjUserID = "";
   String ObjSatisfied = "";
   String ObjMeasure = null; 
   String user = (String)request.getAttribute("user");
   String ObjRawScore = "unknown";
   String ObjMinScore = "unknown";
   String ObjMaxScore = "unknown";
   String ObjProgress = "unknown";
   String ObjCompletion = "";
      
   if ( !createObjError.equals("") )
   {
      //ObjectivesData od = (ObjectivesData)request.getAttribute("objData");
      objectiveID = (String)request.getAttribute("objID");
      ObjUserID = (String)request.getAttribute("userID");
      ObjSatisfied = (String)request.getAttribute("satisfied");
      ObjMeasure = (String)request.getAttribute("measure");
      ObjRawScore = (String)request.getAttribute("rawscore");
      ObjMinScore = (String)request.getAttribute("minscore");
      ObjMaxScore = (String)request.getAttribute("maxscore");
      ObjProgress = (String)request.getAttribute("progressmeasure");
      ObjCompletion = (String)request.getAttribute("completion");
            
      if( createObjError.equals( "dupobjid" ) )
      {
         // We must reset the user because we came from java, not html
         user = ObjUserID;
%>
         <h2>The following error was caught</h2>
         <p>
         
         This Objective ID already exists for this user, please choose another 
         Objective ID
         </p>
<%
      }
   }
%>

   <p class="font_header">
   <b>Add a Global Objective</b>
   </p>
   <form method="post" 
   		name="createObjective" id="createObjective"
   		action="/adl/LMSCourseAdmin?type=<%= ServletRequestTypes.ADD_OBJ %>"
        onsubmit="">
      <table width="450" border="0" align="left">
         <tr>
            <td colspan="2">
               <hr>
            </td>
         </tr>
         <tr>
            <td bgcolor="#5E60BD" colspan="2" class="white_text"><b>&nbsp;  
            Please provide the following new objective information:</b></td>
         </tr>
         <tr>
            <td width="20%"><label for="userID">User ID:</label></td>
            <td>
               <%=user%><input type="hidden" name="userID" id="userID" value="<%=user%>" />
            </td>
         </tr>
         <tr>
            <td><label for="objectiveID">Objective ID:</label></td>
            <td>
<%
            if( objectiveID != null )
            {
%>               
               <input type="text" name="objectiveID" id="objectiveID" 
                  value="<%=objectiveID%>">
<%
            }
            else
            {
%>               
               <input type="text" name="objectiveID" id="objectiveID">
<%
            }
%>
         </td>
         <tr>
            <td><label for="satisfied">Satisfaction<br />
            	 status:</label></td>
            <td>
               <select name="satisfied" id="satisfied">
                  <option selected>unknown</option><option>satisfied</option>  
                  <option>not satisfied</option>
               </select>
            </td>
         </tr>
         <tr>
            <td><label for="measure">Measure:</label></td>
            <td>
<%
            if( ObjMeasure != null)
            {
%>               
               <input type="text" name="measure" id="measure" 
                  value="<%=ObjMeasure%>">
<%
            }
            else
            {
%>               
               <input type="text" name="measure" id="measure" value="unknown">
<%
            }
%>
         </td>
         </tr>
         <tr>
            <td>&nbsp;</td>
            <td>(A decimal value between -1.0 and 1.0, or "unknown")

            </td>
         </tr>
         <tr>
            <td><label for="rawscore">Raw Score:</label></td>
            <td>
<%
            if( ObjRawScore != null)
            {
%>
               
               <input type="text" name="rawscore" id="rawscore" 
                  value="<%=ObjRawScore%>">
<%
            }
            else
            {
%>               
               <input type="text" name="rawscore" id="rawscore" value="unknown">
<%
            }
%>
         </td>
         </tr>
         <tr>
            <td><label for="minscore">Min Score:</label></td>
            <td>
<%
            if( ObjMinScore != null)
            {
%>               
               <input type="text" name="minscore" id="minscore" 
                  value="<%=ObjMinScore%>">
<%
            }
            else
            {
%>               
               <input type="text" name="minscore" id="minscore" value="unknown">
<%
            }
%>
         </td>
         </tr>
         <tr>
            <td><label for="maxscore">Max Score:</label></td>
            <td>
<%
            if( ObjMaxScore != null)
            {
%>               
               <input type="text" name="maxscore" id="maxscore" 
                  value="<%=ObjMaxScore%>">
<%
            }
            else
            {
%>               
               <input type="text" name="maxscore" id="maxscore" value="unknown">
<%
            }
%>
         </td>
         </tr>
         <tr>
            <td><label for="progressmeasure">Progress:</label></td>
            <td>
<%
            if( ObjProgress != null)
            {
%>               
               <input type="text" name="progressmeasure" id="progressmeasure" 
                  value="<%=ObjProgress%>">
<%
            }
            else
            {
%>               
               <input type="text" name="progressmeasure" id="progressmeasure" value="unknown">
<%
            }
%>
         </td>         
         </tr>
         <tr>
            <td>&nbsp;</td>
            <td>(A decimal value between 0.0 and 1.0 or "unknown")
            </td>
         </tr>
                  <tr>
            <td><label for="completion">Completion<br />
                status:</label></td>
            <td>
               <select name="completion" id="completion">
                  <option>not attempted</option><option>incomplete</option>
                  <option>completed</option><option selected>unknown</option>
               </select>
            </td>
         </tr>         
         <tr>
            <td colspan="2">
               <hr>
            </td>
         </tr>
         <tr>
            <td>&nbsp;</td>
         </tr>
         <tr>
            <td colspan="2" align="center">
               <input type="button" value="Submit" onclick="javascript:checkData();" >
            </td>
         </tr>
      </table>
   </form>
   </body>
   <%
   }
   else
   {
      // Redirect the user to the RTE Home page.
      response.sendRedirect( "/adl/LMSCourseAdmin?type=" + ServletRequestTypes.GO_HOME + "&userID=");
   }
%>
</html>