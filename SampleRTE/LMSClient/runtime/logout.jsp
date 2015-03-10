
<%
   /***************************************************************************
   **
   ** Filename:  logout.jsp
   **
   ** File Description:   This file implements the logout function.
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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
   <title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1
   - Logout</title>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <script type="text/javascript">
	   window.parent.document.location.href = "LMSMain.htm";   
   </script>
</head>

<body bgcolor="#FFFFFF">

<%
   session.removeAttribute( "EXITFLAG" );
   session.removeAttribute( "COURSEID" );
   session.removeAttribute( "SCOID" );
   session.removeAttribute( "TOC" ); 
%>

</body>
</html>
