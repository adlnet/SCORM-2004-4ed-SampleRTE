<%@ page contentType="text/html;charset=utf-8" %>

<%@page import = "java.util.*, java.io.*, org.adl.samplerte.server.*" %>
<%
   /***************************************************************************
   **
   ** Filename:  newUser.jsp
   **
   ** File Description:   This file allows an admin to enter information to
   **                     create a new user account.
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
   String is_admin = (String)session.getAttribute( "AdminCheck" );
 
   String bodyText = "";
   String newUserMsg = (String)request.getAttribute("reqOp");
   String userID = "";
   String firstName = "";
   String lastName = "";
   UserProfile userProfile = new UserProfile();
   
   if ( newUserMsg != null )
   {
      if ( newUserMsg.equals("duplicate_user"))
      {

          bodyText = "User ID already exists, please choose another ID";
          userProfile = (UserProfile)request.getAttribute("userProfile");
          userID = userProfile.mUserID;
          firstName = userProfile.mFirstName;
          lastName = userProfile.mLastName;


      }

   }
%>
<html>
<head>
   <title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - 
    Add New User</title>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   
   <link href="includes/sampleRTE_style.css" rel="stylesheet" type="text/css">

   <script language="JavaScript">
   /****************************************************************************
   **
   ** Function:  checkData()
   ** Input:   none
   ** Output:  boolean
   **
   ** Description:  This function ensures that there are values in each text
   **               box before submitting
   **
   ***************************************************************************/
   function checkData()
   {
      if ( newUser.userID.value == "" || newUser.firstName.value == "" ||
           newUser.lastName.value == "" || newUser.password.value == "" ||
           newUser.cPassword.value == "" )
      {
         alert ( "You must provide a value for all fields!!" );
         return false;
      }

      if ( newUser.password.value != newUser.cPassword.value)
      {
         alert ( "Password and confirmed password are not the same!!" );
         return false;
      }
      
      if ( newUser.userID.value.indexOf(';') > -1 )
      {
         alert ( "User ID cannot contain ';'." );
         return false;
      }
      

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
   function newWindow(pageName)
   {
      window.open(pageName, 'Help',
      "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=500,height=500");
   }

   </script>
</head>

<body bgcolor="#FFFFFF">
<% if ((String)session.getAttribute( "USERID" ) != null)
   {
%>
<jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
    <jsp:param value="/adl/help/newUserHelp.htm" name="helpURL"/>
</jsp:include>


<p class="font_header">
<b>
   Add a New User
</b>
<%
   }
else
{
%>
<p class="font_header">
<b>
   Sign Up
</b>
<%
}
%>
</p>
   <b>
     <%= bodyText %>
   </b>   
<form method="post" action="/adl/LMSUserAdmin" name="newUser" 
                                                 onSubmit="return checkData()"
                                                 accept-charset="utf-8">
<% if ((String)session.getAttribute( "USERID" ) != null)
   {
%>
   <input type="hidden" name="type" value="8" />
<%
   }
else
{
%>  
   <input type="hidden" name="type" value="<%= ServletRequestTypes.NEW_SIGN_UP %>" />
<%
}
%>
   <table width="450" border="0" align="left">
      <tr>
         <td colspan="2">
            <hr>
         </td>
      </tr>
      <tr>
         <td bgcolor="#5E60BD" colspan="2" class="white_text">
            <b>
               &nbsp;Please provide the following new user information:
         </b>
         </td>
      </tr>
      <tr>
         <td width="37%">
            <label for="userID">User ID:</label>
         </td>
         <td width="63%">

<%
   if ( userID != null )
   {
%>
               <input type="text" name="userID" id="userID" value="<%= userID %>">
<%
   }
   else
   {
%>
               <input type="text" name="userID" id="userID"> 
<%
  }
%>

         </td>
      </tr>
      <tr>
         <td width="37%"><label for="firstName">First Name:</label></td>
            <td width="63%">

<%
  if ( firstName != null )
  {
%>

              <input type="text" name="firstName" id="firstName" value="<%= firstName %>">

<%
  }
  else
  {
%>

              <input type="text" name="firstName">

<%
  }
%>

           </td>
        </tr>
        <tr>
          <td width="37%"><label for="lastName">Last Name:</label></td>
             <td width="63%">

<%
  if ( lastName != null )
  {
%>

               <input type="text" name="lastName" id="lastName" value="<%= lastName %>">

<%
  }
  else
  {
%>

               <input type="text" name="lastName" id="lastName">

<%
   }
%>

             </td>
         </tr>
         <tr>
             <td width="37%">
                <label for="password">Password:</label>
             </td>
             <td width="63%">
                 <input type="password" name="password" id="password">
             </td>
         </tr>
         <tr>
             <td width="37%">
                <label for="cPassword">Password Confirmation:</label>
             </td>
             <td width="63%">
                 <input type="password" name="cPassword" id="cPassword">
             </td>
         </tr>
<%
if ( (! (is_admin == null)) && ( is_admin.equals("true")) )
{
%>
         <tr>
             <td width="37%">
                <label for="admin">Admin:</label>
             </td>
             <td width="63%">
                 <select name="admin" id="admin">
                     <option value="false">No</option> <option value="true">Yes</option>
                 </select>
             </td>
         </tr>
<%
} 
else
{
%>
         <input type="hidden" name="admin" id="admin" value="false" />
<%
}
%>
         <tr>
            <td colspan="2">
               <hr>
            </td>
         </tr>
         <tr>
             <td width="37%">
                &nbsp;
             </td>
             <td width="63%">
                &nbsp;
             </td>
         </tr>
         <tr>
            <td colspan="2" align="center">
               <input type="submit" name="Submit" value="Submit">
            </td>
         </tr>
     </table>
</form>
<p>
&nbsp;
</p>
</body>
</html>