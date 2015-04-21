
<%@ page contentType="text/html;charset=utf-8" %>

<%@page import = "java.sql.*, 
                  java.util.*, 
                  java.io.*, 
                  org.adl.samplerte.util.*,
                  org.adl.samplerte.server.CourseData" %>
<%
   /***************************************************************************
   **
   ** Filename:  dsp_viewStatus.jsp 
   **
   ** File Description: This file displays Course Status information.
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
   ** Design Issues: None
   **
   ** Implementation Issues: None
   ** Known Problems: None
   ** Side Effects: None
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
   String name = (String)request.getAttribute("name");
   CourseData cd = (CourseData)request.getAttribute("status");
   String satisfied = cd.mSatisfied;
   String measure = cd.mMeasure;
   String completed = cd.mCompleted;
   String progmeasure = cd.mProgMeasure;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>

   <title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - 
    Course Status
   </title> 
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
   <link href="/adl/includes/sampleRTE_style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">

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
       <jsp:param value="" name="helpURL"/>
   </jsp:include>

   <p class="font_header">
   <b>
      Course Status
   </b>
   </p>
   <form action="" method="post"  name="viewStatus">
      <table width="458">
         <tr>
            <td>
               <hr />
            </td>
         </tr>
      </table>
      <table width="458" border="1" >
         <tr class="white_text">
           <th scope="col">
             <b> User </b>
           </th>
           <th scope="col">
             <b> Satisfied </b>
           </th>
           <th scope="col">
             <b> Measure </b>            
           </th>
           <th scope="col">
             <b> Completed </b>            
           </th>
           <th scope="col">
             <b> Progress Measure </b>            
           </th>
         </tr>

         <tr>
            <td>
               <%= name %> 
            </td>
            <td>
               <%= satisfied %>
            </td>
            <td>
               <%= measure %>
            </td>
             <td>
               <%= completed %>
            </td>
            <td>
               <%= progmeasure %>
            </td>

         </tr>

      </table>
      
   </form>
</body>
</html>
     
