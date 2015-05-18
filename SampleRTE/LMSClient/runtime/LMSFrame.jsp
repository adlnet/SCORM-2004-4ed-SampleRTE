<!DOCTYPE html>

<%@page
	import="org.adl.util.EnvironmentVariable,org.adl.util.debug.LogConfig,java.io.*,java.util.logging.*"%>
<%
   /***************************************************************************
    **
    ** Filename: LMSFrame.jsp
    **
    ** File Description: This page contains the API Adapter applet.  The API 
    **                   Adapter applet has no visual display elements and is 
    **                   therefore invisible to the user.  Note that the API 
    **                   Adapter object is exposed to SCOs via the LMSMain.htm 
    **                   page.  The SCOs communicate with the Run-time 
    **                   Environment through this API.  This page also contains
    **                   the Run-time Environment login button and 
    **                   the button for Next, Previous, Suspend, and Quit.
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
   session.invalidate();

   Logger mLogger = Logger.getLogger("org.adl.util.debug.samplerte");
   String adlHome = "";
   String escpath = "";
   try
   {
      mLogger.entering("-=-LMSFrame", "try()");
      adlHome = getServletConfig().getServletContext().getRealPath("/");
      escpath = adlHome.replace("\\", "\\\\");
      adlHome = adlHome.substring(0, adlHome.lastIndexOf(File.separator));

      LogConfig logConfig = new LogConfig();
      logConfig.configure(adlHome, true);

   }
   catch ( Exception e )
   {
      mLogger.severe("-=-Caught exception " + e);
   }
%>


<html lang="en">
	<head>
		<meta http-equiv="expires" content="Tue, 20 Aug 1999 01:00:00 GMT" />
		<meta http-equiv="Pragma" content="no-cache" />
		<title>ADL E-Learning Library</title>

      <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
		<link href="../includes/sampleRTE_style.css" rel="stylesheet" type="text/css" />
      <script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
      <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="BrowserDetect.js"></script>
		
		<script type="text/javascript">
               //<!-- [CDATA[
            
            // variable to contain string of API and Datamodel calls made by current SCO
            var mlog = "API and Data Model Calls";
            var codeLoc = 'http://'+ window.document.location.host + '/adl/runtime/' + 'code.jsp';
            
            var _Debug = false;
            
            var API_1484_11 = top.API_1484_11;
            var SRTE_CLIENT = top.SRTE_CLIENT;
            
            /****************************************************************************
            **
            ** Function: login_onclick()
            ** Input:   none
            ** Output:  none
            **
            ** Description:  This function changes the content frame to the login page,
            **               and "hides" the login button.
            **
            ***************************************************************************/
            function login_onclick() 
            {
               DetectBrowser();
               var courseID = getParameterByName('courseID', top);
               var path = "";
               if (courseID !== "") {
            	   courseID = "?courseID=" + courseID;
            	   path = "&path=" + encodeURIComponent('<%= escpath %>');
               }
               window.parent.frames['Content'].document.location.href = "LMSLogin.htm" + courseID + path;
               document.getElementById('login').style.visibility = "hidden";
               /*if ( document.layers != null )
               {
                  swapLayers();
               }
               else if (Netscape || document.all != null || Firefox)
               {
                  window.document.forms[0].login.style.visibility = "hidden";
               }
               else
               {
                  //Niether IE, Firefox or Netscape is being used
                  //alert("your browser may not be supported");
               }*/
            }
            
            function getParameterByName(name, frame) {
                name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
                var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
                    results = regex.exec(frame.location.search);
                return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
            }
            
            
            /****************************************************************************
            **
            ** Function: refreshMenu()
            ** Input:   none
            ** Output:  none
            **
            ** Description:  This function is called by the API after an LMSCommit.  It
            **               causes the menu page to load the latest UI state and update
            **               itself.
            **
            ***************************************************************************/
            function refreshMenu()
            {
            	if (_Debug && !IE) 
            	{
            		java.lang.System.out.println("LMSFrame code frame was: "
									+ window.parent.frames['code'].document.location.href);
					java.lang.System.out.println("code location is :" + codeLoc);
				}
				
            	window.parent.frames['code'].document.location.href = codeLoc;
				
            	if (_Debug && !IE) {
					java.lang.System.out.println("code frame is now: "
									+ window.parent.frames['code'].document.location.href);
				}

			}

			/****************************************************************************
			 **
			 ** Function: setUIState()
			 ** Input:   boolean - state
			 ** Output:  none
			 **
			 ** Description:  This function is called twice during an LMSCommit.  It
			 **               disables the navigation buttons while the commit is active
			 **               and re-enables the buttons when the commit is finished. 
			 **
			 ***************************************************************************/
			function setUIState(state) {
				if (!state) {
					document.buttonform.quit.disabled = true;
					document.buttonform.previous.disabled = true;
					document.buttonform.next.disabled = true;
					document.buttonform.suspend.disabled = true;
				} else {
					document.buttonform.quit.disabled = false;
					document.buttonform.previous.disabled = false;
					document.buttonform.next.disabled = false;
					document.buttonform.suspend.disabled = false;
				}
			}
			/****************************************************************************
			 **
			 ** Function: nextSCO()
			 ** Input:   none
			 ** Output:  none
			 **
			 ** Description:  This function is called when the user clicks the "next"
			 **               button.  The Sequencing Engine is called, and all relevant
			 **               controls are affected. 
			 **
			 ***************************************************************************/
			function nextSCO() {
				// Disable the button controls
				document.forms['buttonform'].next.disabled = true;
				document.forms['buttonform'].previous.disabled = true;
				document.forms['buttonform'].quit.disabled = true;
				document.forms['buttonform'].suspend.disabled = true;

				// This is the launch line for the next SCO...
				// The Sequencing Engine determines which to launch and
				// serves it up into the LMS's content frame or child window - depending
				//on the method that was used to launch the content in the first place.
				var scoWinType = typeof (window.parent.frames['Content'].scoWindow);
				var theURL = 'http://' + window.document.location.host
						+ '/adl/runtime/pleaseWait.jsp?button=next';

				SRTE_CLIENT.setWasNextButtonPushed(true);

				if (scoWinType != "undefined" && scoWinType != "unknown") {
					if (window.parent.frames['Content'].scoWindow != null) {
						// there is a child content window so display the sco there.
						window.parent.frames['Content'].scoWindow.document.location.href = theURL;
					} else {
						window.parent.frames['Content'].document.location.href = theURL;

					}
				} else {
					window.parent.frames['Content'].document.location.href = theURL;

				}
			}

			/****************************************************************************
			 **
			 ** Function: display_log(String)
			 ** Input:   none
			 ** Output:  none
			 **
			 ** Description:  This function writes information from API and datamodel calls to        
			 ** a logging window.  The window is scrolled to the bottom to allow the most
			 ** recent log entries to be visible.
			 **
			 ***************************************************************************/
			function display_log(call_string) {
				mlog += "<br>";
				mlog += call_string;
				top.frames['log'].document.getElementById('log_span').innerHTML = mlog;

				top.frames['log'].location.hash = '#bottom';
			}
			/****************************************************************************
			 **
			 ** Function: reset_log_string()
			 ** Input:   none
			 ** Output:  none
			 **
			 ** Description:  This function clears the logging information from API 
			 ** and datamodel calls when a new course is launched.       
			 ** 
			 **               
			 **
			 ***************************************************************************/
			function reset_log_string() {
				mlog = "API and Datamodel Calls";
				top.frames['log'].document.getElementById('log_span').innerHTML = mlog;
			}
			/****************************************************************************
			 **
			 ** Function: reset_logging()
			 ** Input:   none
			 ** Output:  none
			 **
			 ** Description:  This function clears the logging information from API 
			 ** and datamodel calls when a new course is launched.       
			 ** 
			 **               
			 **
			 ***************************************************************************/
			function reset_logging() {
				/*if(_Debug){
					System.out.println("LMSFrame.jsp: In reset_logging");
				}*/
				mlog = "API and Datamodel Calls";
				top.frames['log'].document.getElementById('log_span').innerHTML = mlog;

				SRTE_CLIENT.resetLoggingVariable();
			}

			/****************************************************************************
			 **
			 ** Function: doChoiceEvent( navEvent )
			 ** Input:   none
			 ** Output:  none
			 **
			 ** Description:  This function is called when Terminate has been called by 
			 ** the SCO after a choice navEvent has been set.
			 **                
			 **
			 ***************************************************************************/
			function doChoiceEvent(choiceEvent) {
				window.top.frames['Content'].location.href = 'http://'
						+ window.document.location.host
						+ '/adl/runtime/pleaseWait.jsp?scoID=' + choiceEvent;

			}

			/****************************************************************************
			 **
			 ** Function: doJumpEvent( navEvent )
			 ** Input:   id of the activity to jump to
			 ** Output:  none
			 **
			 ** Description:  This function is called when Terminate has been called by 
			 ** the SCO after a jump navEvent has been set.
			 **                
			 **
			 ***************************************************************************/
			function doJumpEvent(choiceEvent) {
				window.top.frames['Content'].location.href = 'http://'
						+ window.document.location.host
						+ '/adl/runtime/pleaseWait.jsp?scoID=' + choiceEvent
						+ '&jump=true';

			}

			/****************************************************************************
			 **
			 ** Function: invokeSuspendAll()
			 ** Input:   none
			 ** Output:  none
			 **
			 ** Description:  This function is called when the learner presses the RTE
			 **				 provided Suspend button.
			 **                
			 **
			 ***************************************************************************/
			function invokeSuspendAll() {

				SRTE_CLIENT.setWasLmsSuspendAllPushed(true);
				doNavEvent('suspendAll');
			}

			/****************************************************************************
			 **
			 ** Function: invokeQuit()
			 ** Input:   none
			 ** Output:  none
			 **
			 ** Description:  This function is called when the learner presses the RTE
			 **				 provided Quit button.
			 **                
			 **
			 ***************************************************************************/
			function invokeQuit() {

				SRTE_CLIENT.setWasQuitButtonPushed(true);
				doNavEvent('exitAll');
			}

			/****************************************************************************
			 **
			 ** Function: doNavEvent( navEvent)
			 ** Input:   none
			 ** Output:  none
			 **
			 ** Description:  This function is called when an LMSFinish has been called by 
			 ** the SCO after a navEvent has been set.
			 **                
			 **
			 ***************************************************************************/
			function doNavEvent(navEvent) {
				// Disable the button controls
				document.forms['buttonform'].next.disabled = true;
				document.forms['buttonform'].previous.disabled = true;
				document.forms['buttonform'].quit.disabled = true;
				document.forms['buttonform'].suspend.disabled = true;

				// This is the launch line for the next SCO...
				// The Sequencing Engine determines which to launch and
				// serves it up into the LMS's content frame or child window - depending
				//on the method that was used to launch the content in the first place.
				var scoWinType = typeof (window.parent.frames['Content'].scoWindow);
				if (navEvent == "continue") {
					navEvent = "next";
				}
				if (navEvent == "previous") {
					navEvent = "prev";
				}

				//var theURL = "sequencingEngine.jsp?button=" + navEvent;
				var theURL = null;
				//var urlPrepend;

				DetectBrowser();

				//if ( !IE )
				//{
				//window.parent.frames['Content'].location.reload();

				//var temp = window.parent.frames['Content'].location.pathname;

				// Firefox set directory to CourseImports, must be escaped to runtime	  
				/*if ( temp.indexOf("CourseImports") != -1 )
				{
					  alert(temp);
					  var temp_array = temp.split("/");
					  alert(temp_array.length);
					  
					  var firstOne = 1;
					  
					  var i = 0;
					  while ( i < (temp_array.length - 3) )
					  {
					     if ( firstOne == 1 )
					     {
					        urlPrepend = "../";
					        firstOne = 0;
					     }
					     else
					     {
					        urlPrepend += "../";
					     }
					     i += 1;
					  }
					  
				   theURL = urlPrepend + "runtime/sequencingEngine.jsp?button=" + navEvent;
				   alert(theURL);
				}
				else
				{
				   theURL = "sequencingEngine.jsp?button=" + navEvent;
				}*/
				// }
				//else
				// {
				theURL = 'http://' + window.document.location.host
						+ '/adl/runtime/sequencingEngine.jsp?button='
						+ navEvent;
				// }

				if (scoWinType != "undefined" && scoWinType != "unknown") {
					if (window.parent.frames['Content'].scoWindow != null) {
						// there is a child content window so display the sco there.
						window.parent.frames['Content'].scoWindow.document.location.href = theURL;
					} else {
						window.parent.frames['Content'].document.location.href = theURL;

					}
				} else {
					window.parent.frames['Content'].document.location.href = theURL;
				}
				if (document.layers != null) {
					swapLayers();
				} else if (document.all != null) {
					// window.top.frames[0].document.forms[0].next.disabled = true;
					// window.top.frames[0].document.forms[0].previous.disabled = true;
				} else {
					//Neither IE or Netscape is being used
					//alert("your browser may not be supported");
				}
			}

			/****************************************************************************
			 **
			 ** Function: previousSCO()
			 ** Input:   none
			 ** Output:  none
			 **
			 ** Description:  This function is called when the user clicks the "previous"
			 **               button.  The Sequencing Engine is called, and all relevant
			 **               controls are affected. 
			 **
			 ***************************************************************************/
			function previousSCO() {

				// This function is called when the "Previous" button is clicked.
				// The LMSLesson servlet figures out which SCO to launch and
				// serves it up into the LMS's content frame or child window - depending
				//on the method that was used to launch the content in the first place.

				// Disable the button controls
				document.forms['buttonform'].next.disabled = true;
				document.forms['buttonform'].previous.disabled = true;
				document.forms['buttonform'].quit.disabled = true;
				document.forms['buttonform'].suspend.disabled = true;

				var scoWinType = typeof (window.parent.frames['Content'].scoWindow);
				var theURL = 'http://' + window.document.location.host
						+ '/adl/runtime/pleaseWait.jsp?button=prev';

				SRTE_CLIENT.setWasPreviousButtonPushed(true);

				if (scoWinType != "undefined" && scoWinType != "unknown") {
					if (window.parent.frames['Content'].scoWindow != null) {
						// there is a child content window so display the sco there.
						window.parent.frames['Content'].scoWindow.document.location.href = theURL;
					} else {
						window.parent.frames['Content'].document.location.href = theURL;

					}
				} else {
					window.parent.frames['Content'].document.location.href = theURL;

					//  scoWindow is undefined which means that the content frame
					//  does not contain the lesson menu at this time.
				}
				if (document.layers != null) {
					swapLayers();
				} else if (document.all != null) {
					// window.document.forms[0].next.disabled = true;
					// window.document.forms[0].previous.disabled = true;
				} else {
					//Neither IE nor Netscape is being used
					//alert("your browser may not be supported");
				}

			}

			/****************************************************************************
			 **
			 ** Function: closeSCOContent()
			 ** Input:   none
			 ** Output:  none
			 **
			 ** Description:  This function exits out of the current lesson and presents
			 **               the RTE menu. 
			 **
			 ***************************************************************************/
			function closeSCOContent() {
				var scoWinType = typeof (window.parent.frames['Content'].window);

				ctrl = window.document.forms['buttonform'].control.value;

				if (ctrl == "auto") {

					window.top.frames['Content'].location.href = "/adl/LMSCourseAdmin?type="
							+ ServletRequestTypes.GO_HOME + "&userID=" + userid;
					window.top.contentWindow.close();
				} else {

					if (scoWinType != "undefined" && scoWinType != "unknown") {
						if (window.parent.frames['Content'].scoWindow != null) {
							// there is a child content window so close it.
							window.parent.frames['Content'].scoWindow.close();
							window.parent.frames['Content'].scoWindow = null;
						}
						window.parent.frames['Content'].document.location.href = "/adl/LMSCourseAdmin?type="
								+ ServletRequestTypes.GO_HOME
								+ "&userID="
								+ userid;
					} else {
						//  scoWindow is undefined which means that the content frame
						//  does not contain the lesson menu so do nothing...
					}
				}
			}

			/****************************************************************************
			 **
			 ** Function: swapLayers()
			 ** Input:   none
			 ** Output:  none
			 **
			 ** Description:  This function is used to swap the login and logout buttons
			 **
			 ***************************************************************************/
			function swapLayers() {
				if (document.loginLayer.visibility == "hide") {
					document.logoutLayer.visibility = "hide";
					document.loginLayer.visibility = "show";
				} else {
					document.loginLayer.visibility = "hide";
					document.logoutLayer.visibility = "show";
				}
			}

			/****************************************************************************
			 **
			 ** Function: init()
			 ** Input:   none
			 ** Output:  none
			 **
			 ** Description:  This function sets the API variable and hides the
			 **               the navigation buttons
			 **
			 ***************************************************************************/
			function init() {
				DetectBrowser();

				window.top.frames['LMSFrame'].document.forms['buttonform'].next.style.visibility = "hidden";
				window.top.frames['LMSFrame'].document.forms['buttonform'].previous.style.visibility = "hidden";
			}

			/****************************************************************************
			 **
			 ** Function: doConfirms()
			 ** Input:   none
			 ** Output:  none
			 **
			 ** Description:  This function prompts the user that they may lose
			 **               data if they exit the course.  If exit is confirmed,
			 **               the sequencing engine is called with "ExitAll".
			 **
			 ***************************************************************************/
			function doConfirm() {
				if (confirm("If you quit now the course information may not be saved.  Do you wish to quit?")) {
					// Disable the button controls
					document.forms['buttonform'].next.disabled = true;
					document.forms['buttonform'].previous.disabled = true;
					document.forms['buttonform'].quit.disabled = true;
					document.forms['buttonform'].suspend.disabled = true;

					var scoWinType = typeof (window.parent.frames['Content'].scoWindow);
					var theURL = "sequencingEngine.jsp?button=exitAll";

					if (scoWinType != "undefined" && scoWinType != "unknown") {
						if (window.parent.frames['Content'].scoWindow != null) {
							// there is a child content window so display the sco there.
							window.parent.frames['Content'].scoWindow.document.location.href = theURL;
						} else {
							window.parent.frames['Content'].document.location.href = theURL;

						}
					} else {
						window.parent.frames['Content'].document.location.href = theURL;

					}
					if (document.layers != null) {
						swapLayers();
					} else if (document.all != null) {
						// window.top.frames[0].document.forms[0].next.disabled = true;
						// window.top.frames[0].document.forms[0].previous.disabled = true;
					} else {
						//Neither IE nor Netscape is being used
						//alert("your browser may not be supported");
					}

				} else {
				}
				
			}
		//]]-->
		</script>
	</head>

	<body onload="init();" id="topNav">
	<div class="container">
		<form name="buttonform" action="">

			<div id="applet">
				
			<!-- ClientRTS -->
				
			

			</div>

			<table width="1200">
				<tr>
					<td width="40" colspan="2">
						<img border="0" align="left" src="ADL_Logo_2013_small.png" width="80px" alt="ADL Logo" />
					</td>
					<td width="670" colspan="4" align="center" valign="middle">
						<h1 class="scorm">ADL E-Learning Library 
						</h1>
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td colspan="7" valign="bottom" class="topNavLinks" align="right">
		               &nbsp; 
		            </td>                  
				</tr>
				<tr>
					<td align="center">
						<input class="btn btn-default" type="button" value="Start" id="login" name="login"
							onclick="return login_onclick();" />
					</td>
					<td align="left">
						<input class="btn btn-default" type="button" value="Suspend" id="suspend" name="suspend"
							style="visibility: hidden" onclick="return invokeSuspendAll();" />
					</td>
					<td align="center">
						<input class="btn btn-default" type="button" align="right" id ="quit" value="    Quit    "
							name="quit" onclick="return invokeQuit();"
							style="visibility: hidden" />
					</td>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td align="center">

						<input class="btn btn-default" type="button" align="right" value="&lt;- Previous"
							id="previous" name="previous" onclick="return previousSCO();"
							style="visibility: hidden" />
					</td>
					<td align="center">
						<input class="btn btn-default" type="button" align="right" value="Continue -&gt;"
							id="next" name="next" onclick="return nextSCO();"
							style="visibility: hidden" />
					</td>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>

				</tr>
			</table>

			<div>
				<input type="hidden" name="control" value="" />
				<input type="hidden" name="isNextAvailable" value="" />
				<input type="hidden" name="isPrevAvailable" value="" />
				<input type="hidden" name="isTOCAvailable" value="" />
				<input type="hidden" name="isSuspendAvailable" value="" />
			</div>

		</form>
</div><!--  container  -->
	</body>
</html>
