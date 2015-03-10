<%@ page contentType="text/html;charset=utf-8" %>

<%@page import = "java.sql.*,java.util.*,java.io.*, org.adl.samplerte.util.*, org.adl.samplerte.server.*"%>

<%
   /***************************************************************************
   **
   ** Filename:  LMSLogin.jsp
   **
   ** File Description:   This file implements the login function.  It checks 
   ** username and password that were entered by user against those in 
   ** database and redirects to login2.htm if values were not found, else 
   ** sends to menu page.  
   **
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

<%
   Connection conn;
   PreparedStatement stmtSelectUser;
   LMSDatabaseHandler myDatabaseHandler = new LMSDatabaseHandler();
   String sqlSelectUser
             = "SELECT * FROM UserInfo Where UserID = ?";
   

   try
   {
      String UserName = new String("");
      String Password = new String("");
      String action = new String("");
      String fullName = new String("");
      String loginName = new String("");
      String firstName = new String("");
      String lastName = new String("");
      
      UserName = request.getParameter("uname");
      Password = request.getParameter("pwd");
      action = null;
      conn = myDatabaseHandler.getConnection();
      stmtSelectUser = conn.prepareStatement( sqlSelectUser );
    
      ResultSet userRS = null;
   
      synchronized( stmtSelectUser )
      {
         stmtSelectUser.setString( 1, UserName);
         userRS = stmtSelectUser.executeQuery();
   	  }
   
      // Verifies that the username was found by checking to see if the result
      // set 'userRS' is empty.  If the username was found, it checks to see if 
      // the entered password is correct.  If the username was not found, the 
      // variable 'action' is changed to indicate this.
      if( (userRS != null) && (userRS.next()) )
      {
         String userID = userRS.getString("UserID");
         
         if(userID.equals(UserName))
         {
            String passwd = userRS.getString("Password");
            boolean active = userRS.getBoolean("Active");
            firstName = userRS.getString("FirstName");
            lastName = userRS.getString("LastName");
            fullName = lastName + ", " + firstName;
            loginName = firstName + ' ' + lastName;

            if (! active )
            {
               action = "deactivated";
            }
         
            // Verifies that the password that was entered is not blank and that 
            // it matches the password found to belong to the username.  If either
            // of these conditions is incorrect, the variable 'action' is changed
            // to indicate this.
            if( (Password != null) && (!Password.equals(passwd)) )
            {
               action = "invalidpwd";      
            }
         }
         else
         {
            action = "invaliduname";
         }
         
      }
      else
      {
         action = "invaliduname";
      }
   	
      // Verifies that no errors were found with the login by checking to see 
      // if the action variable has been assigned anything.  If 'action' is
      //  null, no errors were found and the session variables 'USERID' and
      //  'RTEADMIN' are set.  
      if ( action == null )
      {
         session.putValue("USERID", UserName);
         session.putValue("USERNAME", fullName);
         session.putValue("LOGINNAME", loginName);

         String admin = userRS.getString("Admin"); 
         
         // Checks to see if the user has admin rights and sets the 'RTEADMIN'
         // variable accordingly.
         if ( (admin != null) && (admin.equals("1")) ) 
         {
           session.putValue("RTEADMIN", new String("true"));
         }
         else
         {
           session.putValue("RTEADMIN", new String("false"));
         }
      }
   
      // checks to see if the action variable is null.  If it is then no error
      // was found and the page is redirected to the menu page, otherwise it
      // is redirected to the second login page.
      if ( action != null )
      {
         response.sendRedirect( "LMSLogin2.htm" );
      }
      else
      {
         response.sendRedirect( "/adl/LMSCourseAdmin?type=" + ServletRequestTypes.GO_HOME + "&userID=" + UserName );
      }
      userRS.close();
      stmtSelectUser.close();
      conn.close();


   }
   catch(SQLException e)
   {
      System.out.println("login sql exception ");e.printStackTrace();   
   }
   catch(Exception e)
   {  
%>
   Caught exception <%= e %>
<%
   } 
%>
