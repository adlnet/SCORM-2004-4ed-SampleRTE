/*******************************************************************************
**
** FileName: APIWrapper.js
**
*******************************************************************************/

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

/*******************************************************************************
** This file is part of the ADL Sample API Implementation intended to provide
** an elementary example of the concepts presented in the ADL Sharable
** Content Object Reference Model (SCORM).
**
** The file is used by the run time environment to maintain internal communication
** within the frames of the environment.
**
*******************************************************************************/
 
var _Debug = false;  // set this to false to turn debugging off
                     // and get rid of those annoying alert boxes.


// local variable definitions
var apiHandle = null;
var API = null;
var findAPITries = 0;


/*******************************************************************************
**
** Function: initAPI()
** Inputs:  none
** Return:  false if the API handle cannot be located
**
** Description: gets the API handle and sets up necessary api variable values
**
**
*******************************************************************************/
function initAPI()
{
   var api = top.SRTE_CLIENT; //getAPIHandle();
   
   if (api == null)
   {
      alert("Unable to locate the RTE's API Implementation");
      return "false";
   }
   else
   {
	   if(_Debug){
	   		//alert("API is apparently not null");
	   }
	   
	   
	   if(_Debug && !IE){
	   		java.lang.System.out.println("api object: " + api);
	   }
	   
	   
       api.setActivityID( document.forms[0].activityID.value );
       api.setCourseID( document.forms[0].courseID.value );
       api.setStateID( document.forms[0].stateID.value );
       api.setUserID( document.forms[0].userID.value );
       api.setUserName( document.forms[0].userName.value );
       api.setNumAttempts( document.forms[0].numAttempts.value );
       api.clearState();
    }
}


/******************************************************************************
**
** Function getAPIHandle()
** Inputs:  None
** Return:  value contained by APIHandle
**
** Description:
** Returns the handle to API object if it was previously set,
** otherwise it returns null
**
*******************************************************************************/
function getAPIHandle()
{
   if (apiHandle == null)
   {
      apiHandle = getAPI();
   }
   return apiHandle;
}


/*******************************************************************************
**
** Function findAPI(win)
** Inputs:  win - a Window Object
** Return:  If an API object is found, it's returned, otherwise null is returned
**
** Description:
** This function looks for an object named API in parent and opener windows
**
*******************************************************************************/
function findAPI(win)
{
   var browser = navigator.userAgent.toLowerCase();
   
   while ((win.API_1484_11 == null) && (win.parent != null) && (win.parent != win))
   {
   
      findAPITries++;
      // Note: 500 is a number based on the IEEE API Standards.
      if ( findAPITries > 500 )
      {
         alert("Error finding API -- too deeply nested.");
         return null;
      }

      win = win.parent;

   }
   
  return win.API_1484_11; 

  
// 	if (Netscape) 
//    {
//    	if(_Debug && !IE){
//			java.lang.System.out.println("Debug: Found Netscape, not supported - returning null...");
//		}
//        //alert("Netscape Browser is not supported.");
//		
//    	//return win.frames['LMSFrame'].document.getElementById("API_1484_11");
//        return null;
//    }
//   	
//    else if (Firefox){
//	    if(_Debug){
//			java.lang.System.out.println("APIWrapper.js ->findAPI: Found Mozilla Firefox, returning getElementById:NNAPIAdapter");
//		}
//		
//		
//		var retVal;
//		try{
//		
//		//retVal = win.frames['LMSFrame'].document.getElementById("API_1484_11");
//		retVal = win.frames['LMSFrame'].document.getElementById("NNAPIAdapter");
//		//retVal = win.frames['LMSFrame'].document.API_1484_11;
//		}
//		catch( e ){
//			alert( "An exception has occurred:" + e );
//		}
//		return retVal;
//	}   	
//    else 
//    {
//   		if(_Debug && !IE){
//       		java.lang.System.out.println("Debug: Returning win.API_1484_11");
//       	}
//       return win.API_1484_11;       
//   }
}



/*******************************************************************************
**
** Function getAPI()
** Inputs:  none
** Return:  If an API object is found, it's returned, otherwise null is returned
**
** Description:
** This function looks for an object named API, first in the current window's
** frame hierarchy and then, if necessary, in the current window's opener window
** hierarchy (if there is an opener window).
**
*******************************************************************************/
function getAPI()
{
   var theAPI = findAPI(window);
   if ((theAPI == null) && (window.opener != null) && (typeof(window.opener) != "undefined"))
   {
	   	if(_Debug){
	   		alert("asking to find the API of the windows opener");
	   	}      
	    theAPI = findAPI(window.opener);
   }
   if (theAPI == null)
   {
      alert("RTE - Can not locate API adapter");
   }
   return theAPI
}