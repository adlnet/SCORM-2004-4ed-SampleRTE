<!doctype html>

<html lang="en">
   <!--
/*******************************************************************************
**
** Filename: LMSLogin2.jsp
**
** File Description: This page contains an HTML form that prompts the user 
**                   for a username and password.  This page is displayed after
**                   an unsuccessful login attempt.
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
/*******************************************************************************
   
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

*******************************************************************************/
-->
   <head>
      <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
      <meta http-equiv="expires" content="Tue, 20 Aug 1999 01:00:00 GMT" />
      <meta http-equiv="Pragma" content="no-cache" />
      <title>Login</title>

      <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
      <script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
      <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
      <link href="includes/sampleRTE_style.css" rel="stylesheet" type="text/css" />

      <script type="text/javascript">
		//<!-- [CDATA[
		/****************************************************************************
		**
		** Function:  submit1_onclick()
		** Input:   none
		** Output:  none
		**
		** Description:  This method checks to see if both text boxes, password and
		**               username have something populating them.
		**
		***************************************************************************/
		function submit1_onclick() 
		{
		   var username = new String(loginform.uname.value);
		   var password = new String(loginform.pwd.value);
		    
		   if (username == "" || password == "")
		   {
		      alert("you must enter a username and a password");
		      return false;
		   }
		   return true;
		}
		//]]-->
		</script>
   </head>
   
   <body id="main_page">
      <div class="container">
         <div class="row">
            <div class="col-md-12">
               <h2>Log in</h2>
            </div>
         </div>
         <div class="row">
            <div class="col-md-12 red_text">
	            <strong> An invalid username or password has been entered! <br />
	               Please try again. </strong>
	         </div>
         </div>
         
         <form class="form-horizontal" method="post" action="LMSUserAdmin" name="loginform" 
                     id="loginform" onsubmit="return submit1_onclick()" accept-charset="utf-8">
                  
           <div class="form-group">
				 <input type="hidden" name="type" value="26" />
				 <input type="hidden" name="path" value="<%=getServletConfig().getServletContext().getRealPath( "/" )%>" />
             <div class="col-sm-10">
             <label class="control-label" for="uname"> Username</label>
               <input class="form-control unpw" type="text" id="uname" name="uname" value="" placeholder="username" />
             </div>
           </div>
           <div class="form-group">
             <div class="col-sm-10">
             <label class="control-label" for="pwd">Password</label>
               <input class="form-control unpw" type="password" id="pwd" name="pwd" value="" placeholder="password" />
             </div>
           </div>
           
           <% if ( (String)request.getParameter("courseID") != null && !"".equals((String)request.getParameter("courseID")) )
				{
				%>
				   <input type="hidden" name=courseID id="courseID" value="<%= (String)request.getParameter("courseID") %>" />
				<% 
				}
				%>
           <div class="form-group">
             <div class="col-sm-10">
               <button type="submit" class="btn btn-default" value="Submit" id="submit1" name="submit1">Log in</button>
             </div>
           </div>
         </form>
         
         <div class="row">
            <div class="col-md-12" id="login_page">
               
               
               <div>
                  <a id="signuplink" href="/adl/LMSUserAdmin?type=24&setProcess=pref">Sign up</a>
               </div>
      
               <script type="text/javascript">
               //<!--[CDATA[
      
                  document.getElementById("uname").focus();
               //]]-->
      
               </script>
      
            </div>
         </div>
      </div>
   </body>
</html>
