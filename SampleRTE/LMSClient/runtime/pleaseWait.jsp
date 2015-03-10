
<%@ page contentType="text/html;charset=utf-8" %>

<%@page import = "java.util.*, java.io.*, java.net.*" %>

<%
   /****************************************************************************
   **
   ** Filename:  pleaseWait.jsp
   **
   ** File Description:   This file determines which item should be launched in
   **                     the current course.  It responds to the following 
   **                     events
   **                     Next - Launch the next sco or asset
   **                     Previous - Launch the previous sco or asset
   **                     Menu - Launch the selected item
   **
   ** Author: ADL Technical Team 
   **
   ** Contract Number:
   ** Company Name: CTC
   **
   ** Module/Package Name:
   ** Module/Package Description:
   **
   ** Design Issues: This is a proprietary solution for a sequencing engine.  
   **                This version will most likely be replaced when the SCORM
   **                adopts the current draft sequencing specification. 
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


   //  Booleans for a completed course and request type
   boolean courseComplete = true;
   boolean wasAMenuRequest = false;
   boolean wasANextRequest = false;
   boolean wasAPrevRequest = false;
   boolean wasFirstSession = false;
   boolean empty_block = false;

   //  The type of controls shown
   String control = new String();
   //  The type of button request if its a button request
   String buttonType = new String();
   String request_type = new String();


   //  Get the requested sco if its a menu request
   // Encode the sco to UTF-8 to ensure all non-Latin
   // characters are correctly used
   request.setCharacterEncoding("UTF-8");
   String requestedSCO = request.getParameter( "scoID" );   
   
   String requestedJump = request.getParameter( "jump" );
   
   //  Get the button that was pushed if its a button request
   buttonType = (String)request.getParameter( "button" );

   // Set boolean for the type of navigation request
   if ( (! (requestedSCO == null)) && (! requestedSCO.equals("") ))
   {
      if ( (! (requestedJump == null)) && (! requestedJump.equals("") ))
      {
         request_type = "sequencingEngine.jsp?scoID="+requestedSCO + "&jump=" + requestedJump;
      }
      else
      {
         request_type = "sequencingEngine.jsp?scoID="+requestedSCO;
      }
      
   } 
   else if ( (! (buttonType == null) ) && ( buttonType.equals("next") ) )
   {       
      request_type = "sequencingEngine.jsp?button=next";
   }
   else if ( (! (buttonType == null) ) && ( buttonType.equals("prev") ) )
   {
      request_type = "sequencingEngine.jsp?button=prev";
   }
%>


<!-- ****************************************************************
**   Build the html 'please wait' page that sets the client side 
**   variables and refreshes to the appropriate course page
*******************************************************************-->  

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  
  <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
   
   <head>
    <title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - 
       Please Wait</title>
    <!-- **********************************************************
    **  This value is determined by the JSP database queries
    **  that are located above in this file
    **  Refresh the html page to the next item to launch  
    ***************************************************************-->
    <script type="text/javascript">       
       //<!-- [CDATA[
       function launchItem(launch)
	   {	      		      
	   		window.top.frames.Content.location.href = (encodeURI(launch));
	   }
      //]]-->
    </script>
   
   </head>
 <body onload="launchItem('<%= request_type %>')">
  
  <p><br /></p>
 </body>
</html>