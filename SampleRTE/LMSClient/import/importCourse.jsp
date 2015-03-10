
<%
   String is_admin = (String)session.getAttribute( "AdminCheck" );
   String id = session.getId();
   // Condition statement to determine if the Session Variable has been
   // properly set.
   if ( (! (is_admin == null)) && ( is_admin.equals("true")) )
   {
%>

<%@page import = "java.util.*, org.adl.samplerte.util.*,
                  org.adl.samplerte.server.*" %>
                  
<%
   /***************************************************************************
   **
   ** Filename:  importCourse.jsp
   **
   ** File Description:  This file allows the user to enter a name for a new
   **                    course, and select the zip
   **                    file that contains the manifest and course content.
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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
   <title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - 
    Import Course</title>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

   <link href="../includes/sampleRTE_style.css" rel="stylesheet" type="text/css" />

   <script type="text/javascript">   
//<!-- [CDATA[

   /****************************************************************************
   **
   ** Function:  checkValues()
   ** Input:   none
   ** Output:  boolean
   **
   ** Description:  This function ensures that there are values in each text
   **               box before submitting
   **
   ***************************************************************************/
   function checkValues()
   {
      if (document.getElementById("coursezipfile").value == "" )
      {
         alert( "You must enter a value for all items" );
         return false;
      }
      else
      {
         file = document.getElementById("coursezipfile").value;
         index = file.length;
         start = index - 4;
         extension = file.substring(start, index);
         extension = extension.toLowerCase();

         if (extension != ".zip")
         {
            alert( "The file you have selected is not a Zip file, please " +
                   "select a Zip file" );
            return false;
         }
      }

      document.getElementById("theZipFile").value = document.getElementById("coursezipfile").value;
      return true;
   }

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
      "scrollbars=yes,resizable=yes,width=500,height=500");
   }
   /***************************************************************************
   **
   ** Function:  disableElements()
   ** Input:   none
   ** Output:  boolean
   **
   ** Description: This makes the call to checkValues, if it returns true then this disables 
   ** the submit button and the link to return to the main menu. It also removes the underline 
   ** from the link.
   **
   ***************************************************************************/
   function disableElements()
   {
      if (checkValues())
      {
         var subButton = document.getElementById('submitButton');
         
         subButton.disabled = true;
         //document.getElementById('homelink').disabled = true;
         document.getElementById('homelink').style.visibility = "hidden";
         return true;
      }
      else
      {
         return false;
      }   
   }
   //]]-->
   </script>
</head>

<body bgcolor="#FFFFFF">

<jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
    <jsp:param value="/adl/help/importHelp.htm" name="helpURL"/>
</jsp:include>


<form method="post" action="/adl/LMSCourseAdmin?type=23&sessID=<%=id%>" name="courseInfo"
      onsubmit="return disableElements();" enctype="multipart/form-data">

   <p class="font_header">
   <b>
      Course Import
   </b>
   </p>


   <table width="450" border="0" align="left">
      <tr>
         <td colspan="2">
            <hr />
         </td>
      </tr>
      <tr>
         <td bgcolor="#5E60BD" colspan="2" class="white_text">
            <b>
               &nbsp;Please provide the following course information:
            </b>
         </td>
      </tr>
      <tr>
         <td>&nbsp;</td>
      </tr>
      <tr>
         <td>
            Enter the file path of the Zip file containing the course
            content you want to import or select "Browse".
         </td>
      </tr>
      <tr>
         <td>
            <label for="coursezipfile">Zip File:&nbsp;</label>
            <input id="coursezipfile" name="coursezipfile" type="file" />
         </td>
      </tr>
      <tr>
         <td>&nbsp;</td>
      </tr>
      <tr>
         <td>
            Select <strong>Full</strong> or <strong>Partial</strong> course validation below 
            and click <strong>Submit</strong>.
            <br />&nbsp;<br />
            <b>NOTE: Choosing Full validation for a package during import does not
               imply SCORM Conformance.  Packages must be tested in the
               latest SCORM Test Suite to ensure complete
               SCORM Conformance.</b>
         </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <tr>
         <td>
            <input id="validateYES" name="validate" type="radio" value="1" checked="checked" /> 
            <label for="validateYES">Full (Requires Internet Connection)</label>
         </td>
      </tr>
      <tr>
        <td>
            <input id="validateNO" name="validate" type="radio" value="0" /> 
            <label for="validateNO">Partial (No Internet Connection Required)</label>
        </td>
      </tr>
      <tr>
         <td colspan="2">
            <hr />
         </td>
      </tr>
      <tr>
         <td>&nbsp;</td>
      </tr>
      <tr>
         <td width="100%" colspan="2" align="center">
            <input id="submitButton" type="submit" name="Submit" value="Submit" />
         </td>
      </tr>
   </table>

   <input type="hidden" name="theManifest" />
   <input type="hidden" id="theZipFile" value="" />
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
