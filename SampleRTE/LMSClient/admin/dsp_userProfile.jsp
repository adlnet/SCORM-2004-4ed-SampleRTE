<%@page import="gov.adlnet.xapi.model.Agent"%>
<%@ page contentType="text/html;charset=utf-8" %>

<%@page import = "java.util.*, java.io.*, org.adl.samplerte.server.*"%>

<%
   /***************************************************************************
   **
   ** Filename:  dsp_userProfile.jsp
   **
   ** File Description:  This file provides an interface for the admin to select
   **					 a user profile to update/modify/view.
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

   UserProfile user = new UserProfile();
   
   request.setCharacterEncoding("utf-8");
   
   user = (UserProfile)request.getAttribute("userProfile");
   String userID = user.mUserID;
   String firstName = user.mFirstName;
   String lastName = user.mLastName;
   String audioLevel = user.mAudioLevel;
   String language = "";
   boolean isAdmin = user.mAdmin;
   String isUserAdmin = (String)session.getAttribute( "RTEADMIN" );
   String caller = (String)request.getAttribute("caller");
   String helpPage = "/adl/help/changeProfileHelp.htm";
   
   List<UserAgentInfo> agentinfo = (List<UserAgentInfo>)request.getAttribute("agentinfos");
   agentinfo = (agentinfo == null) ? new ArrayList<UserAgentInfo>() : agentinfo;
   String lrserrmsg = (String) request.getAttribute("lrs-error-message");
   String lrsokmsg = (String) request.getAttribute("lrs-ok-message");
   
   //System.out.println("dsp_userProfile : " + caller);

   if (caller != null && caller.equals("adminUserPref"))
   {
      helpPage = "/adl/help/manageUserHelp.htm";
   }
   
   
   if (  !(user.mLanguage == null) && !(user.mLanguage.length() == 0) )
   {
      language = user.mLanguage;
   }

   String deliverySpeed = user.mDeliverySpeed;
   String audioCaptioning = user.mAudioCaptioning;

   String errorHeader = (String)request.getAttribute("errorHeader");
   String errorString = (String)request.getAttribute("errorMsg");
%>
<!doctype html>
<html lang="en">
<head>
   <title>Edit User Profile</title>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
	<link href="includes/sampleRTE_style.css" rel="stylesheet" type="text/css" />
	<script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
	<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

   <script type="text/javascript">
     function openHelp(page)
     {
        window.open(page,"CommentsLMS",
                                "HEIGHT=550,WIDTH=600 status=no location=no scrollbars=yes");
     }
     
     function validatePassword(form) {
    	 if (form.oldPassword.value == "" || 
    			 form.newPassword.value == "" || 
    			 form.newPassword2.value == "")
  		 {
    		  alert("Password Fields cannot be blank.");
    		  return false;
  		 }
    	 if (form.newPassword.value !== form.newPassword2.value)
    	 {
    		 alert("New passwords do not match.");
    		 return false;
    	 }
     }
     
     function validateAccountInputs(form) {
    	 if (form.mbox.value != "" && form.homepage.value != "" && form.name.value != "")
  		 {
          alert("Only fill in email, or homepage and name");
          return false
  		 }
    	 return true;
     }
   </script>
</head>
<body style="margin-top: 4.5em; margin-left: 1em;">

	<jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
	    <jsp:param value="<%=helpPage %>" name="helpURL"/>
	</jsp:include>

   <div class="container">
   
	   <h2>Edit User Profile</h2>
	   
	   <%
	      if ( (errorString != null) && (errorHeader != null) )
		   {
		%>
	   <div class="row">
	      <div class="col-md-12">
		      <p class="font_two">
					<b><%=errorHeader%></b>
					<%=errorString%>
				</p>
	      </div>
	   </div>
		<%
		   }
		%>
		
		<div class="row">
		    <div class="col-md-5">
		       <form class="form-horizontal" method="post" action="/adl/LMSUserAdmin" name="userProfile" accept-charset="utf-8">
		         <input type="hidden" name="type" value="<%= ServletRequestTypes.UPDATE_PREF %>" />
		         <input type="hidden" name="userID" value="<%=userID%>" />
		         <input type="hidden" name="firstName" value="<%=firstName%>" />
		         <input type="hidden" name="lastName" value="<%=lastName%>" />
		         <input type="hidden" name="caller" value="<%=caller%>" />
		         <div class="form-group" style="margin-right: .5em;margin-left: .5em;">
		            <label for="fullname">User Name</label>
		            <input readonly type="text" class="form-control" id="fullname" name="fullname" value="<%=firstName%> <%=lastName%>" />
		         </div>
		         <div class="form-group" style="margin-right: .5em;margin-left: .5em;">
		            <label for="audioLevel">Audio Level</label>
		            <input type="text" class="form-control" name="audioLevel" id="audioLevel" value="<%=audioLevel%>" />
		         </div>
		         <div class="form-group" style="margin-right: .5em;margin-left: .5em;">
		            <label for="language">Language</label>
		            <input type="text" class="form-control" name="language" id="language" value="<%=language%>" />
		         </div>
		         <div class="form-group" style="margin-right: .5em;margin-left: .5em;">
		            <label for="deliverySpeed">Delivery Speed</label>
		            <input type="text" class="form-control" name="deliverySpeed" id="deliverySpeed" value="<%=deliverySpeed%>" />
		         </div>
		         <div class="form-group" style="margin-right: .5em;margin-left: .5em;">
		            <label for="audioCaptioning">Audio Captioning</label>
		            <input type="text" class="form-control" name="audioCaptioning" id="audioCaptioning" value="<%=audioCaptioning%>" />
		         </div>
		         <%
		            if( "true".equals(isUserAdmin) )
		            {
		         %>
		         <div class="form-group" style="margin-right: .5em;margin-left: .5em;">
		            <label for="admin">User Role</label>
		            <select class="form-control" name="admin" id="admin">
		               <option value="true" <%if( isAdmin ){%>SELECTED<%}%>>Admin
		               </option>
		               <option value="false" <%if( !isAdmin ){%>SELECTED<%}%>>User
		               </option>
		            </select>
		         </div>
		         <%
		            }
		            else
		            { 
		         %>
		               <input type="hidden" name="admin" value="<%=isUserAdmin%>" />
		         <% 
		            }
		         %>
		         <button type="submit" name="submit" class="btn btn-primary">Save</button>
		      </form>
		    </div>
		    <div class="col-md-5 col-md-offset-1">
		      <h2>Change Password</h2>
		      <form class="form-horizontal" method="post" action="/adl/LMSUserAdmin" name="userProfile" accept-charset="utf-8" onSubmit="return validatePassword(this);">
		         <input type="hidden" name="type" value="<%= ServletRequestTypes.CHANGE_PASSWORD %>" />
		         <input type="hidden" name="userID" value="<%=userID%>" />
		         
		         <div class="form-group" style="margin-right: .5em;margin-left: .5em;">
		            <label for="oldPassword">Password</label>
		            <input class="form-control" type="password" name="oldPassword" id="oldPassword" />
		         </div>
		         
		         <div class="form-group" style="margin-right: .5em;margin-left: .5em;">
		            <label for="newPassword1">New Password:</label>
		            <input class="form-control" type="password" name="newPassword" id="newPassword" />
		         </div>
		         
		         <div class="form-group" style="margin-right: .5em;margin-left: .5em;">
		            <label for="newPassword2">New Password:</label>
		            <input class="form-control"type="password" name="newPassword2" id="newPassword2" />
		         </div>
		         <button type="submit" name="Submit" class="btn btn-info">Update</button>
		      </form>
		    </div>
		</div>
		<div class="row">
		    <div class="col-md-12" style="border-bottom: 1px solid #ddd; margin-bottom: 1em;">
            <h2>LRS Account Information</h2>
            <p>
               This LRS account information is used to identify you in the ADL LRS.
               If you have already registered an account in this system, it will 
               be listed below. You can select an account from that list to edit
               it.
            </p>
            <p> 
               If there are no known accounts for you, please register one using 
               the form below. You may use an existing one, if you know an xAPI 
               account already being used for you. Currently this system can 
               accept Agents with mbox or account ids. 
            </p>
            <p>
               If you do not have a known account within the ADL LRS, you may go to 
               <a href="https://lrs.adlnet.gov/xapi/" target="_blank">the ADL LRS</a> 
               and sign up - the email you use during sign up will be your account. 
               Or you may click the 'Generate xAPI Account' button below. The system 
               will automatically fill in values for the Account Homepage and Name.
            </p>
            <p>
               Make sure to only fill in Email, or Account Homepage and Account Name. 
            </p>
            <% if (lrserrmsg != null && ! "".equals(lrserrmsg)) { %>
               <div class="alert alert-danger" role="alert"><%= lrserrmsg %></div>
            <% } else if (lrsokmsg != null && ! "".equals(lrsokmsg)) { %>
               <div class="alert alert-success" role="alert"><%= lrsokmsg %></div>
            <% } %>
            <% if (agentinfo.size() > 0) { %>
            <table id="acctable" class="table table-hover">
               <thead><tr><th>Alias</th><th>Mbox</th><th>Homepage</th><th>Name</th></tr></thead>
               <%            
                  for (UserAgentInfo info : agentinfo) {
                     String accmbox = (info.Mbox != null) ? info.Mbox : "";
                     String acchomepage = (info.HomePage != null) ? info.HomePage : "";
                     String accname = (info.AccName != null) ? info.AccName : "";
                     String accalias = (info.AgentAlias != null) ? info.AgentAlias : "";
               %>
		            <tr>
		               <td><%= accalias %></td>
		               <td><%= accmbox %></td>
		               <td><%= acchomepage %></td>
		               <td><%= accname %></td>
		            </tr>
               <% } %>
            </table>
            <% } %>
            </div>
         </div>
         <div class="row">
            <div class="col-md-12">
            <form class="form-horizontal" method="post" action="/adl/LMSUserAdmin" name="userLRSInfo" accept-charset="utf-8" onSubmit="return validateAccountInputs(this)">
               <input type="hidden" name="type" value="<%= ServletRequestTypes.SET_LRS_INFO %>" />
               <input type="hidden" name="userID" value="<%= userID %>" />
               <div class="form-group" style="margin-right: .5em;margin-left: .5em;">
                  <label for="alias">Alias for this account</label>
                  <input class="form-control" type="text" name="alias" id="alias" value="" />
               </div>
	            <div class="panel-info">
	               <div class="panel-heading">
	                 Agent mbox
	               </div>
                  <div class="panel-body">
	                  <div class="form-group" style="margin-right: .5em;margin-left: .5em;">
	                     <label for="mbox">Email</label>
	                     <input class="form-control" type="text" name="mbox" id="mbox" value="" />
	                  </div>
	               </div>
               </div>
               <p>
                  - or -
               </p>
               <div class="panel-info">
                  <div class="panel-heading">
                     Agent account
                  </div>
                  <div class="panel-body">
		               <div class="form-group" style="margin-right: .5em;margin-left: .5em;">
		                  <label for="homepage">Account Homepage</label>
		                  <input class="form-control" type="text" name="homepage" id="homepage" value="" />
		               </div>
		               
		               <div class="form-group" style="margin-right: .5em;margin-left: .5em;">
		                  <label for="name">Account Name</label>
		                  <input class="form-control" type="text" name="name" id="name" value="" />
		               </div>
		            </div>
               </div>
               <button type="submit" name="Submit" class="btn btn-info">New / Update</button>
               <button type="button" id="createxapi" name="createxapi" class="btn btn-default">Generate xAPI Account</button>
            </form>
          </div>
		</div>
		<script>
		   $('#createxapi').on('click', function(){
			   $('#alias').val("<%= UserAgentInfo.AGENT_ALIAS_DEFAULT %>");
			   $('#mbox').val("");
			   $('#homepage').val("<%= UserAgentInfo.LRS_HOMEPAGE_DEFAULT %>");
			   $('#name').val("<%= userID %>");
		   });
		   
		   $('#acctable > tbody > tr').click(function(e, f, g) {
			    // there are 4 cells in the table row
			    $('#alias').val(e.currentTarget.children[0].innerText);
             $('#mbox').val(e.currentTarget.children[1].innerText);
             $('#homepage').val(e.currentTarget.children[2].innerText);
             $('#name').val(e.currentTarget.children[3].innerText);
		   });
		</script>
</body>
</html>

