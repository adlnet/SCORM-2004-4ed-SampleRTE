<%
   String is_admin = (String)session.getAttribute( "AdminCheck" );
   
   // Condition statement to determine if the Session Variable has been
   // properly set.
   if ( (! (is_admin == null)) && ( is_admin.equals("true")) )
   {
%>
<%@page import = "java.sql.*, java.util.*, 
                  java.io.*, org.adl.samplerte.server.*" %>
<!--
   /***************************************************************************
   **
   ** Filename:  objectivesAdmin.jsp 
   **
   ** File Description: This file allows an administrator to update or delete
   **                   Global Objective values.
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
   ***************************************************************************
   
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
<%
      Vector courseObjs = (Vector)request.getAttribute("objs");

      String objID = null;
      String learnerID = null;
      String satisfied = null;
      String measure = null;
      String rawScore = null;
      String minScore = null;
      String maxScore = null;
      String progress = null;
      String completion = null;
	  String bodyString = "<tr>";
      ObjectivesData od = null;
      ObjectivesData odG = null;
           
      // Loops through all of the global objectives and outputs them in the
      // table with a radio button for selection of delete or reset.
      for ( int i = 0; i < courseObjs.size(); i++ )
      {  
         od = (ObjectivesData)courseObjs.elementAt(i);
            
         objID = od.mObjectiveID;
         learnerID = od.mUserID;
         satisfied = od.mSatisfied;
         measure = od.mMeasure;
         rawScore = od.mRawScore;
         minScore = od.mMinScore;
         maxScore = od.mMaxScore;
         progress = od.mProgressMeasure;
         completion = od.mCompletionStatus;
         
         if ( learnerID.indexOf("\'") > -1 )
         {
            learnerID = learnerID.replaceAll("\'", "&#39;");
         }
         if ( objID.indexOf("\'") > -1 )
         {
            objID = objID.replaceAll("\'", "&#39;");         
         }
         
         bodyString += "<td scope='row'><input type='radio' name='" + i + "~" + objID + 
                       ";" + learnerID + "' value='reset'/></td>";
         bodyString += "<td><input type='radio' name='" + i + "~" + objID + ";" +
					   learnerID + "' value='delete'/></td>";
         bodyString += "<td><input type='radio' name='" + i + "~" + objID + ";" +
					   learnerID + "'/></td>";
         bodyString += "<td>" + objID + "</td>";
         bodyString += "<td>" + learnerID + "</td>";
         bodyString += "<td>" + satisfied + "</td>";
         bodyString += "<td>" + measure + "</td>";
         bodyString += "<td>" + rawScore + "</td>";
         bodyString += "<td>" + minScore + "</td>";
         bodyString += "<td>" + maxScore + "</td>";
         bodyString += "<td>" + progress + "</td>";
         bodyString += "<td>" + completion + "</td>";
			bodyString += "</tr>";
      }
%>
<html>
<head>
   <title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - 
    Global Objectives Admin
   </title> 
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<script language=javascript>

  /**********************************************************************
   **  Function: resetAll()
   **  Description: This function selects all the reset buttons.
   **    
   **********************************************************************/
   function resetAll( )
   {
       var numRadioButtons = 3;
       var form = window.document.forms[0];
       for (var i=0; i<window.document.compAdmin.elements.length; i++)  
       {  
          if ( form.elements[i].type == "radio") 
         {    if ( ( i % numRadioButtons ) == 0)
              form.elements[i].checked = true;
          }   
       }
       document.forms[0].selectAll[0].checked = false;
    }

   /**********************************************************************
   **  Function: deleteAll()
   **  Description: This function selects all the delete buttons.
   **    
   **********************************************************************/
   function deleteAll( )
   {
       var numRadioButtons = 3;
       var form = window.document.forms[0];
       for (var i=0; i<window.document.compAdmin.elements.length; i++)  
       { 
          if ( form.elements[i].type == "radio") 
          {   
              if ( ( i % numRadioButtons ) == 1)
              form.elements[i].checked = true;
          }   
       }
       document.forms[0].selectAll[1].checked = false;

    }
    
   /**********************************************************************
   **  Function: clearAll()
   **  Description: This function selects all the delete buttons.
   **    
   **********************************************************************/
   function clearAll( )
   {
       var form = window.document.forms[0];
       for (var i=0; i<window.document.compAdmin.elements.length; i++)  
       { 
          if ( form.elements[i].type == "radio") 
          {   
              
              form.elements[i].checked = false;
          }   
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

   <link href="/adl/includes/sampleRTE_style.css" rel="stylesheet" type="text/css">
   
</head>
   
<body bgcolor="#FFFFFF" style="margin-top: 4.5em;">
  
   <jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
       <jsp:param value="/adl/help/globalObjectivesHelp.htm" name="helpURL"/>
   </jsp:include>
   
   <p class="font_header">
   <b>Edit Objectives</b>
   </p>
   <div width="1000"><hr /></div>
   <form method="post"
         action="/adl/LMSCourseAdmin?type=<%= ServletRequestTypes.EDIT_OBJ %>" 
         name="compAdmin">
      <table border="1" border="0" cellpadding="5px">         
         <tr>
            <td bgcolor="#5E60BD" colspan="12" class="white_text">
			   <b>
                  &nbsp;Please select the global objectives you would like to 
				                                           delete or reset:
               </b>
            </td>
         </tr>     
         <tr bgcolor="gray" class="white_text">
           <th scope="col">
             <b> Reset </b>
           </th>
           <th scope="col">
             <b> Delete </b>
           </th>
           <th scope="col">
             <b> Clear<br />Selection </b>
           </th>
           <th scope="col">
             <b>Target Objective&nbsp;ID</b>
           </th>
           <th scope="col">
             <b> Learner </b>
           </th>
           <th scope="col">
             <b> Satisfaction <br />
                 Status </b>
           </th>
           <th scope="col">
             <b> Measure </b>
           </th>
           <th scope="col">
               <b> Raw Score </b>
           </th>
           <th scope="col">
               <b> Min Score </b>
           </th>
           <th scope="col">
               <b> Max Score </b>
           </th>
           <th scope="col">
               <b> Progress<br />Measure </b>
           </th>
           <th scope="col">
               <b> Completion<br />Status </b>
           </th>
         </tr>

		<%= bodyString %>

         <tr>
           <td scope="row">
              <input type='radio' name='selectAll' value='reset' 
                                                   onClick="resetAll()" />
           </td>
           <td>
              <input type='radio' name='selectAll' value='delete' 
                                                   onClick="deleteAll()" />
           </td>
           <td>
              <input type='radio' name='selectAll' value='clear' 
                                                   onClick="clearAll()" />  
           </td> 
           <td>
              Select All
           </td>
           <td>&nbsp;</td>
           <td>&nbsp;</td>
           <td>&nbsp;</td>
           <td>&nbsp;</td>
           <td>&nbsp;</td>
           <td>&nbsp;</td>
           <td>&nbsp;</td>
           <td>&nbsp;</td>
         </tr>
      </table>
      <div width="1000"><hr /></div>
      <table width="450">
         <tr>
           <td></td>
         </tr>
         <tr >
           <td align="center">
              <input type="submit" name="submit" value="Submit" />
           </td>
         </tr>
      </table>
   </form>
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

