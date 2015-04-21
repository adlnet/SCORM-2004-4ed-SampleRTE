
<%
   String is_admin = (String)session.getAttribute( "AdminCheck" );

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
   ** Filename:  dsp_scos.jsp
   **
   ** File Description: This file allows an administrator to update Comments
   **                   from LMS for selected scos.
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
<script>
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
</script>
<html>
<head>
   <title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - 
   Comments From LMS Admin</title>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

   <link href="includes/sampleRTE_style.css" rel="stylesheet" type="text/css">

</head>

   <body bgcolor="#FFFFFF" style="margin-top: 4.5em;">

   <jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
       <jsp:param value="/adl/help/manageCoursesHelp.htm" name="helpURL"/>
   </jsp:include>
   
   <p>
   <a href="javascript:history.go(-1)">Back to Courses View</a>
   </p>

   <p class="font_header">
   <b>
      Item List
   </b>
   </p>

   <form method="post" action="/adl/LMSCourseAdmin" name="scoAdmin">
      <input type="hidden" name="type" value="3" />

      <table width="450">
         <tr>
            <td colspan="2">
               <hr>
            </td>
         </tr>
         <tr>
            <td bgcolor="#5E60BD" colspan="2" class="white_text"><b>Please
            select the item you would like to update the comments in:</b>
            </td>
         </tr>
      </table>

      <table width="450">
      <%
          Vector scoList = new Vector();
          scoList = (Vector)request.getAttribute("scos");
          
          if (scoList.size() > 0)
          {
      
      %>
      
         <tr>
            <td colspan="3" align="left">
               <label for="scoID"><b>Item Title </b></label>
            </td>
         </tr>
         <!-- Loop Through the Particular SCO data of a selected Course -->
         <%
           	 String color = "#FFFFFF";
             for ( int i=0; i< scoList.size(); i++)
             {
                SCOData scoItem = (SCOData)scoList.elementAt(i);
                if ( i == 0 )
                {
				   %>
				      <tr bgcolor = "<%=color%>">
				      <td>
			          	<input type='radio' name='scoID' value='<%= scoItem.mActivityID %>' checked>
			              <%= scoItem.mItemTitle %>
			          </td>
				      </tr>
			       <%
			     }
			     else
			     {
				   %>
			          <tr bgcolor = "<%=color%>">
				      <td>
			          <input type='radio' name='scoID' value='<%= scoItem.mActivityID %>'>
			             <%= scoItem.mItemTitle %>
			          </input>
			          </td>
				      </tr>
			       <%
			     }
			     if(color.equals("#FFFFFF"))
      			 {
         		    color = "#CDCDCD";
			     }
			     else
			     {
		            color = "#FFFFFF";
		         } // end if
             } // end for
          } // end if
          else
          {
        %>
        <tr>
            <td width="450" height="30" align="center" bgcolor="#FFFFFF" colspan="3">
                No Items referencing SCO Resources <br /> were found in the selected course
            </td>
         </tr>
         
         <%
         } 
         %>
         <tr>
            <td colspan="3">
               <hr>
            </td>
         </tr>
         <tr>
            <td colspan="2"></td>
         </tr>
         <%if ( scoList.size() > 0 ) 
         {
         %> 
         
         <tr >
            <td  colspan="3" align="center">
               <input type="submit" name="submit" value="Submit" />
            </td>
         </tr>
         <%
         }
         %>
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