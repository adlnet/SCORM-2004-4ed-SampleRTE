<%@ page contentType="text/html;charset=utf-8" %>

<%@page import = "java.util.*, org.adl.samplerte.util.*,
                  org.adl.samplerte.server.*" %>
<%
    String userid = (String)session.getAttribute( "USERID" );
%>


<!doctype html>
  
 <html lang="en">
<!--
/*******************************************************************************
**
** Filename: endsession.jsp
**
** File Description: This page is displayed when the sequencer returns an 
**                   endsession code.
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
<meta http-equiv="expires" content="Tue, 20 Aug 1999 01:00:00 GMT" />
<meta http-equiv="Pragma" content="no-cache" />
<title>Special State End Session Event</title>
   
   <link href="../includes/sampleRTE_style.css" rel="stylesheet" type="text/css" />
   
</head>
<body>
<!--<script type="text/javascript" src="../runtime/codebase.js"></script>-->
 <script type="text/javascript">
 //<!-- [CDATA[
 	var _Debug = false;
	var scoWinType = typeof(window.opener)
	var codeLoc = 'http://'+ window.document.location.host + '/adl/runtime/' + 'code.jsp';
   
	window.top.frames['LMSFrame'].document.forms['buttonform'].next.style.visibility = "hidden";
	window.top.frames['LMSFrame'].document.forms['buttonform'].previous.style.visibility = "hidden";
	window.top.frames['LMSFrame'].document.forms['buttonform'].suspend.disabled = false;
	window.top.frames['LMSFrame'].document.forms['buttonform'].suspend.style.visibility = "hidden";
	
	if(_Debug && !IE){java.lang.System.out.println("code location is :" + codeLoc); }
	window.parent.frames['code'].document.location.href = codeLoc;
	
	window.top.frames['LMSFrame'].document.forms['buttonform'].quit.disabled = false;
	window.top.frames['LMSFrame'].document.forms['buttonform'].quit.style.visibility = "hidden";
//]]-->
</script>

<jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
    <jsp:param value="" name="helpURL"/>
</jsp:include>
<div class="container" style="margin-top:4.5em;">
   <div class="row">
		<div class="col-md-12">
		   <h2>The content has ended</h2>
		   <p>
		       The content has ended. You may go back to the course list using the 
		       navigation at the top of the page. 
		   </p>
		</div>           
	</div>
</div>
</body>
</html> 
