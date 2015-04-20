<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
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
      <title>SCORM 2004 4th Edition Sample Run-Time Environment
         Version 1.1.1 - Login</title>

      <link href="includes/sampleRTE_style.css" rel="stylesheet"
         type="text/css" />

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
   <body>
      <div id="login_page">
         <div class="red_text">
            <b> An invalid username or password has been entered! <br />
               Please try again. </b>
         </div>
         <form method="post" action="LMSUserAdmin" name="loginform" 
         id="loginform" onsubmit="return submit1_onclick()" accept-charset="utf-8">
         <input type="hidden" name="type" value="26" />
         <input type="hidden" name="path" value="<%=getServletConfig().getServletContext().getRealPath( "/" )%>" />
            <table>
               <tr>
                  <td>
                     <div id="login_col1">
                        <div id="login_col1a_user">
                           <label for="uname">
                              Username
                           </label>
                           <br />

                        </div>
                     </div>
                  </td>
                  <td>
                     <div id="login_col2">
                        <input class="unpw" type="text" id="uname" name="uname" value="" />
                     </div>
                  </td>

               </tr>
               <tr>
                  <td>
                     <div id="login_col1a_pwd">
                        <label for="pwd">
                           Password
                        </label>
                        <br />
                     </div>
                  </td>
                  <td>
                     <div id="login_col3">
                        <input class="unpw" type="password" id="pwd" name="pwd" value="" />
                     </div>
                  </td>
               </tr>
            </table>
<% if ( (String)request.getParameter("courseID") != null && !"".equals((String)request.getParameter("courseID")) )
{
%>
   <input type="hidden" name=courseID id="courseID" value="<%= (String)request.getParameter("courseID") %>" />
<% 
}
%>

            <div>

               <br />
               <input type="submit" value="Submit" id="submit1"
                  name="submit1" />
            </div>

         </form>
      </div>
   </body>
</html>