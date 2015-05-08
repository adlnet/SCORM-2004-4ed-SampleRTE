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
<!doctype html>
<html lang="en">
<head>
   <title> Add New User</title>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   
   <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
   <link href="includes/sampleRTE_style.css" rel="stylesheet" type="text/css" />
   <script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
   <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>


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
   
   function goBack()
   {
	   var backurl = "/adl/runtime/LMSLogin.htm";
	   <% if ( (String)request.getParameter("courseID") != null && !"".equals((String)request.getParameter("courseID")) )
       {
	    %>
	          backurl = backurl + "?courseID=<%= (String)request.getParameter("courseID") %>&path=<%= getServletConfig().getServletContext().getRealPath( "/" ) %>"
	    <% 
	       }
	    %>
	    window.parent.frames['Content'].document.location.href = backurl;
	    return false;
   }

   </script>
</head>

	<% if ((String)session.getAttribute( "USERID" ) != null)
	   {
	%>
<body bgcolor="#FFFFFF" style="margin-top: 4.5em;">
		<jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
		    <jsp:param value="/adl/help/newUserHelp.htm" name="helpURL"/>
		</jsp:include>
		
		<div class="container">
		   <h2>Add a New User</h2>
	<%
	   }
	else
	{
	%>
	<body bgcolor="#FFFFFF">
		<div class="container">
		<p>
		    <% if ( (String)request.getParameter("courseID") != null && !"".equals((String)request.getParameter("courseID")) )
	       {
	       %>
	             <a href="/adl/runtime/LMSLogin.htm?courseID=<%= (String)request.getParameter("courseID") %>&path=<%= getServletConfig().getServletContext().getRealPath( "/" ) %>">Go Back</a>
	       <% 
	          } else {
	       %>
	           <a href="/adl/runtime/LMSLogin.htm">Go Back</a>
	       <% } %>
		</p>
		   <h2>Sign Up</h2>
	<%
	}
	%>
	   <div class="row">
	      <div class="col-md-12">
	         <%= bodyText %>
	      </div>
	   </div>   
	   <form class="form-horizontal" method="post" action="/adl/LMSUserAdmin" name="newUser" onSubmit="return checkData()" accept-charset="utf-8">
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
			
			<div class="form-group" style="margin-right: .5em; margin-left: .5em;">
	         <label for="userID">User ID</label>
	         <%
				   if ( userID != null )
				   {
				%>
				      <input type="text" class="form-control" id="userID" name="userID" value="<%= userID %>" />
				<%
				   }
				   else
				   {
				%>
				      <input type="text" class="form-control" id="userID" name="userID" placeholder="user name" /> 
				<%
				   }
				%>
	      </div>
	      
	      <div class="form-group" style="margin-right: .5em; margin-left: .5em;">
	         <label for="firstName">First Name</label>
	         <%
				  if ( firstName != null )
				  {
				%>
					  <input type="text" class="form-control" name="firstName" id="firstName" value="<%= firstName %>">
				
				<%
				  }
				  else
				  {   
				%>
				    <input type="text" class="form-control" name="firstName" id="firstName" placeholder="first name">
				
				<%
				  }
				%>
	      </div>
	      
	      <div class="form-group" style="margin-right: .5em; margin-left: .5em;">
	         <label for="lastName">Last Name</label>
	         <%
	           if ( lastName != null )
	           {
	         %>
	              <input type="text" class="form-control" name="lastName" id="lastName" value="<%= lastName %>">
	         
	         <%
	           }
	           else
	           {
	         %>
	             <input type="text" class="form-control" name="lastName" id="lastName" placeholder="last name">
	         
	         <%
	           }
	         %>
	      </div>
	      
	      <div class="form-group" style="margin-right: .5em;margin-left: .5em;">
	         <label for="password">Password</label>
	         <input class="form-control" type="password" name="password" id="password" />
	      </div>
			
			<div class="form-group" style="margin-right: .5em;margin-left: .5em;">
	         <label for="cPassword">Confirm Password</label>
	         <input class="form-control" type="password" name="cPassword" id="cPassword" />
	      </div>
	      
	      <%
				if ( (! (is_admin == null)) && ( is_admin.equals("true")) )
				{
			%>
		         <div class="form-group" style="margin-right: .5em;margin-left: .5em;">
		            <label for="admin">User Role</label>
		            <select class="form-control" name="admin" id="admin">
		               <option value="false">User</option>
		               <option value="true">Admin</option>
		            </select>
		         </div>
			<%
				} 
				else
				{
			%>
				         <input type="hidden" name="admin" id="admin" value="false" />
			<%
				}
			%>
			
			<% if ( (String)request.getParameter("courseID") != null && !"".equals((String)request.getParameter("courseID")) )
				{
			%>
				   <input type="hidden" name=courseID id="courseID" value="<%= (String)request.getParameter("courseID") %>" />
				   <input type="hidden" name="path" value="<%=getServletConfig().getServletContext().getRealPath( "/" )%>" />
			<% 
				}
			%>
	      
	      <button type="submit" name="submit" class="btn btn-primary">Save</button>
	     
	   </form>
	</div>
</body>
</html>