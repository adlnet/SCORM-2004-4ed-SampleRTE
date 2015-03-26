function srte_xhr (url) {
   this.url = url;
}

srte_xhr.prototype.getXHR = function () {
    if (typeof XMLHttpRequest !== 'undefined') {
        return new XMLHttpRequest();  
    }
    var versions = [
        "MSXML2.XmlHttp.5.0",   
        "MSXML2.XmlHttp.4.0",  
        "MSXML2.XmlHttp.3.0",   
        "MSXML2.XmlHttp.2.0",  
        "Microsoft.XmlHttp"
    ];

    var xhr;
    for(var i = 0; i < versions.length; i++) {  
        try {  
            xhr = new ActiveXObject(versions[i]);  
            break;  
        } catch (e) {
        }  
    }
    return xhr;
};

srte_xhr.prototype.send = function(data, xhr) {
    var x = xhr || this.getXHR();
    x.open('POST', this.url, false);
    x.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    x.send(data);
    try {
       return JSON.parse(x.responseText);
    }
    catch (e){}
    return x.responseText;
};


var SRTEErrorManager = (function () {
   var currentErrorCode = "0";
   var errors = {
         "0" : {
            code : "0",
            description : "No Error",
            diagnostic : "No Error"
         },
         "101" : {
            code : "101",
            description : "General Exception",
            diagnostic : "General Exception"
         },
         "102" : {
            code : "102",
            description : "General Initialization Error",
            diagnostic : "General Initialization Error"
         },
         "103" : {
            code : "103",
            description : "Already Initialized",
            diagnostic : "Already Initialized"
         },
         "104" : {
            code : "104",
            description : "Content Instance Terminated",
            diagnostic : "Content Instance Terminated"
         },
         "111" : {
            code : "111",
            description : "General Termination Failure",
            diagnostic : "General Termination Failure"
         },
         "112" : {
            code : "112",
            description : "Termination Before Initialization",
            diagnostic : "Termination Before Initialization"
         },
         "113" : {
            code : "113",
            description : "Termination After Termination",
            diagnostic : "Termination After Termination"
         },
         "122" : {
            code : "122",
            description : "Retrieve Data Before Initialization",
            diagnostic : "Retrieve Data Before Initialization"
         },
         "123" : {
            code : "123",
            description : "Retrieve Data After Termination",
            diagnostic : "Retrieve Data After Termination"
         },
         "132" : {
            code : "132",
            description : "Store Data Before Initialization",
            diagnostic : "Store Data Before Initialization"
         },
         "133" : {
            code : "133",
            description : "Store Data After Termination",
            diagnostic : "Store Data After Termination"
         },
         "142" : {
            code : "142",
            description : "Commit Before Initialization",
            diagnostic : "Commit Before Initialization"
         },
         "143" : {
            code : "143",
            description : "Commit After Termination",
            diagnostic : "Commit After Termination"
         },
         "201" : {
            code : "201",
            description : "General Argument Error",
            diagnostic : "General Argument Error"
         },
         "301" : {
            code : "301",
            description : "General Get Failure",
            diagnostic : "General Get Failure"
         },
         "351" : {
            code : "351",
            description : "General Set Failure",
            diagnostic : "General Set Failure"
         },
         "391" : {
            code : "391",
            description : "General Commit Failure",
            diagnostic : "General Commit Failure"
         },
         "401" : {
            code : "401",
            description : "Undefined Data Model Element",
            diagnostic : "Undefined Data Model Element"
         },
         "402" : {
            code : "402",
            description : "Unimplemented Data Model Element",
            diagnostic : "Unimplemented Data Model Element"
         },
         "403" : {
            code : "403",
            description : "Data Model Element Value Not Initialized",
            diagnostic : "Data Model Element Value Not Initialized"
         },
         "404" : {
            code : "404",
            description : "Data Model Element Is Read Only",
            diagnostic : "Data Model Element Is Read Only"
         },
         "405" : {
            code : "405",
            description : "Data Model Element Is Write Only",
            diagnostic : "Data Model Element Is Write Only"
         },
         "406" : {
            code : "406",
            description : "Data Model Element Type Mismatch",
            diagnostic : "Data Model Element Type Mismatch"
         },
         "407" : {
            code : "407",
            description : "Data Model Element Value Out Of Range",
            diagnostic : "Data Model Element Value Out Of Range"
         },
         "408" : {
            code : "408",
            description : "Data Model Dependency Not Established",
            diagnostic : "Data Model Dependency Not Established"
         },
         "1000" : {
            code : "301",
            description : "General Get Failure",
            diagnostic : "Does Not Have Children"
         },
         "1001" : {
            code : "301",
            description : "General Get Failure",
            diagnostic : "Does Not Have Count"
         },
         "1002" : {
            code : "301",
            description : "General Get Failure",
            diagnostic : "Does Not Have Version"
         },
         "1003" : {
            code : "351",
            description : "General Get Failure",
            diagnostic : "Set Out Of Order"
         },
         "1004" : {
            code : "301",
            description : "General Get Failure",
            diagnostic : "Out Of Range"
         },
         "1005" : {
            code : "351",
            description : "General Get Failure",
            diagnostic : "Element Not Specified"
         },
         "1006" : {
            code : "351",
            description : "General Set Failure",
            diagnostic : "Not Unique"
         },
         "1007" : {
            code : "351",
            description : "General Set Failure",
            diagnostic : "Max Exceeded"
         },
         "1008" : {
            code : "301",
            description : "General Get Failure",
            diagnostic : "Invalid Argument"
         },
         "1009" : {
            code : "351",
            description : "General Set Failure",
            diagnostic : "Overwrite ID"
         },
         "2000" : {
            code : "404",
            description : "Data Model Element Is Read Only",
            diagnostic : "Set Keyword"
         },
         "9000" : {
            code : "401",
            description : "Undefined Data Model Element",
            diagnostic : "Invalid Request"
         },
   };
   
   return {
      setCurrentErrorCode : function (code) {
         currentErrorCode = code;
      },
      getCurrentErrorCode : function() {
         return (errors[currentErrorCode]) 
            ? errors[currentErrorCode].code : "0";
      },
      getErrorDescription : function (code) {
         code = code || currentErrorCode;
         return (errors[code]) ? errors[code].description : "";
      },
      getErrorDiagnostic : function (code) {
         code = code || currentErrorCode;
         return (errors[code]) ? errors[code].diagnostic : "";
      },
      clearCurrentErrorCode : function () {
         currentErrorCode = "0";
      }
   };
   
}());


function DM(errmgr) {
   this.calls = [];
   this.elements = {};
   this.errmgr = errmgr;
   this.validrequests = {};
}
DM.prototype = {
   fromJSON : function (dmjs) {
      if (dmjs.elems)
    	  this.elements = dmjs.elems;
      if (dmjs.validrequests)
    	  this.validrequests = dmjs.validrequests;
   },
   calllist : function () {
      return this.calls;
   },
   clearcalllist: function() {
	   this.calls = [];
   },
   getValue : function (element) {
      var ret = "";
	  try {
         ret = this.elements[element];
         if ( ret === undefined ) {
        	 this.errmgr.setCurrentErrorCode(403);
        	 ret = "";
         }
         this.errmgr.clearCurrentErrorCode();
      }
      catch (e) { 
    	  this.errmgr.setCurrentErrorCode(301);
      }
      return ret;
   },
   setValue : function (element, value) {
      this.errmgr.clearCurrentErrorCode();
      this.calls.push([element, value]);
      this.elements[element] = value;
   }
};

   
var SRTE_CLIENT = (function () {
   var _logging_on = false,
       _initializedState = false,
       _terminatedState = false,
       _terminateCalled = false,
       _lmsSuspendAllPushed = false,
       _quitButtonPushed = false,
       _previousButtonPushed = false,
       _nextButtonPushed = false,
       _TOCPushed = false,
       _userNavRequest = "_none_",
       _servletURL = window.location.protocol + "//" + window.location.host + "/adl/lmscmi",
       _activityID = "",
       _stateID = "",
       _userID = "",
       _userName = "",
       _courseID = "",
       _numAttempts = 0,
       _errorManager = SRTEErrorManager,
       _comm = new srte_xhr(_servletURL);
   
   function isInitialized() { return _initializedState; }
   
   function clearState() {
      _initializedState = false;
      _terminatedState = false;
      _terminateCalled = false;
      _errorManager.clearCurrentErrorCode();
   }
   
   function suspendButtonPushed() {
      if ( isInitialized() ) {
         _lmsSuspendAllPushed = true;
         _userNavRequest = "suspendAll";
         API_1484_11.Commit("");
      }
   }

   function quitButtonPushed() {
      if ( isInitialized() ) {
         _quitButtonPushed = true
         _userNavRequest = "_none_";
         API_1484_11.Commit("");
      }
   }

   function previousButtonPushed() {
      if ( isInitialized() ) {
         _previousButtonPushed = true;
         _userNavRequest = "previous";
         // set adl.nav.request to "_none_" ??
      }
   }
   
   function nextButtonPushed() {
      if ( isInitialized() ) {
         _nextButtonPushed = true;
         _userNavRequest = "continue";
         // set adl.nav.request to "_none_" ??
      }
   }
   
   function TOCPushed(scoID) {
      if ( isInitialized() ) {
         _TOCPushed = true;
         _userNavRequest = "{target=" + scoID + "}choice";
         // set adl.nav.request to "_none_" ??
      }
   }
   
   function resetLoggingVariable() {
      _logging_on = !_logging_on;
   }
   
   function log (message) {
      if (_logging_on && typeof display_log === "function") {
    	  top.frames['LMSFrame'].display_log(message);
      }
   }
   
   function send(data) {
      return _comm.send((typeof data === "string") ? 
                     data : JSON.stringify(data));
   }
   
   // RETURN
   return {
      send : send,
      isInitialized : isInitialized,
      clearState : clearState,
      getWasLmsSuspendAllPushed : function () { return _lmsSuspendAllPushed; },
      setWasLmsSuspendAllPushed : function (val) { _lmsSuspendAllPushed = val; },
      getWasQuitButtonPushed : function () { return _quitButtonPushed; },
      setWasQuitButtonPushed : function (val) { _quitButtonPushed = val; },
      getWasPreviousButtonPushed : function () { return _previousButtonPushed; },
      setWasPreviousButtonPushed : function (val) { _previousButtonPushed = val; },
      getWasNextButtonPushed : function () { return _nextButtonPushed; },
      setWasNextButtonPushed : function (val) { _nextButtonPushed = val; },
      getWasTOCPushed : function () { return _TOCPushed; },
      setWasTOCPushed : function (val) { _TOCPushed = val; },
      setTOCPushed : function (val) { 
    	  _userNavRequest = val;
    	  _TOCPushed = true; 
      },
      getUserNavRequest : function () { return _userNavRequest; },
      setUserNavRequest : function (val) { _userNavRequest = val; },
      suspendButtonPushed : suspendButtonPushed,
      quitButtonPushed : quitButtonPushed,
      nextButtonPushed : nextButtonPushed,
      previousButtonPushed : previousButtonPushed,
      resetLoggingVariable : resetLoggingVariable,
      log : log,
      getActivityID : function () { return _activityID; },
      setActivityID : function (id) { _activityID = id; },
      getCourseID : function () { return _courseID; },
      setCourseID : function (id) { _courseID = id; },
      getStateID : function () { return _stateID; },
      setStateID : function (id) { _stateID = id },
      getUserID : function () { return _userID },
      setUserID : function (id) { _userID = id },
      getUserName : function () { return _userName; },
      setUserName : function (name) { _userName = name; },
      getNumAttempts : function () { return _numAttempts; },
      setNumAttempts : function (num) { _numAttempts = num; },
      getServletURL : function () { return _servletURL; },
      setServletURL : function (url) { _servletURL = url; },
      getTerminatedState : function () { return _terminatedState; },
      setTerminatedState : function (state) { _terminatedState = state; },
      getTerminateCalled : function () { return _terminateCalled; },
      setTerminateCalled : function (called) { _terminateCalled = called; },
      getInitializedState : isInitialized,
      setInitializedState : function (init) { _initializedState = init; },
      errorManager : _errorManager,
   };
}());


var API_1484_11 = (function (srte) {
   var dm = new DM(srte.errorManager);
   // SCORM API
   function Initialize (param) {
      srte.log("Called Initialize");
      var result = "false";
      
      if (srte.terminatedState) {
         srte.errorManager.setCurrentErrorCode("104");
         srte.log("Initialize Returned Error Code 104");
         return result;
      }
      
      srte.setTerminatedState(false);
      srte.setTerminateCalled(false);
      
      if (param !== "") srte.errorManager.setCurrentErrorCode("201");
      else if ( srte.isInitialized() ) srte.errorManager.setCurrentErrorCode("103");
      else {
         srte.setWasLmsSuspendAllPushed(false);
         srte.setWasQuitButtonPushed(false);
         srte.setWasPreviousButtonPushed(false);
         srte.setWasNextButtonPushed(false);
         srte.setWasTOCPushed(false);
         srte.setUserNavRequest("_none_");
         
         // build request (ClientRTS:450)
         var reqdata = {
            "mActivityID" : srte.getActivityID(),
            "mStateID" : srte.getStateID(),
            "mStudentID" : srte.getUserID(),
            "mUserName" : srte.getUserName(),
            "mCourseID" : srte.getCourseID(),
            "mRequestType" : 1,
            "mNumAttempt" : srte.getNumAttempts()
         };
         // post to lms : srte.getServletURL()
         var resp = srte.send(reqdata);
         // get datamodel back : reponse.mActivityData
         // set valid requests : mValidRequests
         dm.fromJSON(resp)
         srte.setInitializedState(true);
         srte.errorManager.clearCurrentErrorCode();
         result = "true";
      }
      srte.log("Initialize Returned Error Code " + srte.errorManager.getCurrentErrorCode())
      
      return result;
   }
   
   function Commit (param) {
      var result = false;
      
      if (srte.getTerminatedState() ) {
    	 srte.errorManager.setCurrentErrorCode(143);
    	 srte.log("Commit Returned Error Code " + srte.errorManager.getcurrentErrorcode());
         return result;
      }
      
      top.frames['LMSFrame'].setUIState(false);
      
      if ( srte.getTerminatedState() ) {
         srte.errorManager.setCurrentErrorCode(143);
         srte.log("Commit Returned Error Code " + srte.errorManager.getCurrentErrorCode());
         return result;
      }
      if ( !srte.isInitialized() ) {
         srte.errorManager.setCurrentErrorCode(142);
         srte.log("Commit Returned Error Code " + srte.errorManager.getCurrentErrorCode());
         return result;
      }
      if (param !== "") {
         srte.errorManager.setCurrentErrorCode("201");
         srte.log("Commit Returned Error Code " + srte.errorManager.getCurrentErrorCode());
         return result;
      }
      
      // request type = type_set (4)
      var reqdata = {
         "mActivityData" : dm.calllist(),
         "mIsFinished" : srte.getTerminateCalled(),
         "mRequestType" : 4,
         "mCourseID" : srte.getCourseID(),
         "mStudentID" : srte.getUserID(),
         "mUserName" : srte.getUserName(),
         "mStateID" : srte.getStateID(),
         "mActivityID" : srte.getActivityID(),
         "mNumAttempt" : srte.getNumAttempts(),
         "mQuitPushed" : srte.getWasQuitButtonPushed(),
         "mSuspendPushed" : srte.getWasLmsSuspendAllPushed()
      };
      dm.clearcalllist();
      // ClientRTS:1293
      var resp = srte.send(reqdata);
      
      if ( resp.mError !== "OK") {
    	  srte.errorManager.setCurrentErrorCode(101);
      }
      else { 
    	  srte.errorManager.clearCurrentErrorCode();
    	  result = "true";
    	  dm.fromJSON(resp);
      }
      
      top.frames['LMSFrame'].setUIState(true);
      top.frames['LMSFrame'].refreshMenu();
      
      srte.log("Commit Returned Error Code " + srte.errorManager.getCurrentErrorCode());
      
      return result;
   }
   
   function Terminate (param) {
      var result = "false";
      srte.setTerminateCalled(true);
      srte.log("Called Terminate ");
      
      if ( srte.getTerminatedState() ) {
         srte.errorManager.setCurrentErrorCode(113);
         srte.log("Terminate Returned Error Code " + srte.errorManager.getCurrentErrorCode());
         return result;
      }
      if ( !srte.isInitialized() ) {
         srte.errorManager.setCurrentErrorCode(112);
         srte.log("Terminate Returned Error Code " + srte.errorManager.getCurrentErrorCode());
         return result;
      }
      if (param !== "") {
         srte.errorManager.setCurrentErrorCode("201");
         srte.log("Terminate Returned Error Code " + srte.errorManager.getCurrentErrorCode());
         return result;
      }

      // ClientRTS:600
      if (srte.getWasLmsSuspendAllPushed() || srte.getWasQuitButtonPushed() ||
          srte.getWasPreviousButtonPushed() || srte.getWasNextButtonPushed() ||
          srte.getWasTOCPushed()) {
         // set adl.nav.request to srte.getUserNavRequest()
    	  dm.setValue("adl.nav.request", srte.getUserNavRequest());
      }
      
      var event = dm.getValue("adl.nav.request");
      
      // check if suspended, if so make sure cmi.exit is "suspend"
      if (event === "suspendAll" || srte.getWasLmsSuspendAllPushed()) {
            var curexitval = dm.getValue("cmi.exit");
            if  (curexitval && curexitval !== "logout"){
            	dm.setValue("cmi.exit", "suspend");
            }    
      }
      
      // don't commit data on abandon: clientrts:624
      if (event !== "abandon" && event !== "abandonAll") {
    	  result = Commit("");
      }
      else {
    	  result = "true";
      }
      
      srte.setTerminatedState(true);
      
      // ClientRTS:636
      if ( result !== "true" ) {
         srte.errorManager.setCurrentErrorCode("391");
         srte.log("Commit failed causing Terminate to fail.");
         return result;
      }
      // else on ClientRTS:645
      srte.setInitializedState(false);
      // get value of exit...
      var exitvalue = dm.getValue("cmi.exit") || "";
      var tempevent = "_none_";
      var isChoice = false;
      var isJump = false;
      
      // figure out event
      tempevent = (exitvalue === "time-out" || exitvalue === "logout") ? "exitAll" : exitvalue;
      if ( event.indexOf("}jump") > -1 ) {
    	  try {
    		  tempevent = /{target=(.*)}jump/.exec(foo)[1];
    	  }
    	  catch (e) { tempevent = "_none_"; }
    	  isJump = true;
      } 
      else if ( event.indexOf("}choice") ) {
    	  try {
    		  tempevent = /{target=(.*)}choice/.exec(foo)[1];
	      }
		  catch (e) { tempevent = "_none_"; }
    	  isChoice = true;
      }
      
      // now handle the event ClientRTS:734
      if (!(srte.getWasLmsSuspendAllPushed() || srte.getWasPreviousButtonPushed() || 
    		  srte.getWasNextButtonPushed() || srte.getWasTOCPushed()) && 
    		  tempevent !== "_none_") {
    	  if ( isChoice ) top.frames['LMSFrame'].doChoiceEvent(tempevent);
    	  else if ( isJump ) top.frames['LMSFrame'].doJumpEvent(tempevent);
    	  else top.frames['LMSFrame'].doNavEvent(tempevent);
      }
      
      
      srte.log("Terminate Returned Error Code " + srte.errorManager.getCurrentErrorCode());
      return result;
   }
   
   function SetValue (dmelement, value) {
      srte.log("Called SetValue(" + dmelement + ", " + value +") ");
      var val = "false";
      
      if ( srte.getTerminatedState() ) {
          srte.errorManager.setCurrentErrorCode(133);
          srte.log("SetValue Returned Error Code " + srte.errorManager.getCurrentErrorCode());
          return result;
       }
       if ( !srte.isInitialized() ) {
          srte.errorManager.setCurrentErrorCode(132);
          srte.log("SetValue Returned Error Code " + srte.errorManager.getCurrentErrorCode());
          return result;
       }
       
       dm.setValue(dmelement, value);
       val = "true";
      
      srte.log("SetValue Returned " + val);
      return val;
   }
	   
   
   function GetValue (element) {
      srte.log("Called GetValue(" + element + ") ");
      var val = "";
      if ( srte.getTerminatedState() ) {
         srte.errorManager.setCurrentErrorCode(123);
         srte.log("GetValue Returned Error Code " + srte.errorManager.getCurrentErrorCode());
         return val;
      }
      if ( !srte.isInitialized() ) {
         srte.errorManager.setCurrentErrorCode(122);
         srte.log("GetValue Returned Error Code " + srte.errorManager.getCurrentErrorCode());
         return val;
      }
      
      srte.errorManager.clearCurrentErrorCode();
      
      // is a status?.. look for threshold and measure
      if (element === "cmi.completion_status") {
    	  var compthresh = dm.getValue("cmi.completion_threshold");
    	  if ( srte.errorManager.getCurrentErrorCode() == 0 ) {
    		  var progmeas = dm.getValue("cmi.progress_measure");
    		  if ( srte.errorManager.getCurrentErrorCode() == 0 ) {
    			  val = ( progmeas >= compthresh ) ? "completed" : "incomplete";
    		  }
    		  else {
    			  val = "unknown";
    		  }
    		  srte.errorManager.clearCurrentErrorCode();
		  }
      }
      else if (element === "cmi.success_status") {
    	  var sps = dm.getValue("cmi.scaled_passing_score");
    	  if ( srte.errorManager.getCurrentErrorCode() == 0 ) {
    		  var ss = dm.getValue("cmi.score.scaled");
    		  if ( srte.errorManager.getCurrentErrorCode() == 0 ) {
    			  val = ( ss >= sps ) ? "passed" : "failed";
    		  }
    		  else {
    			  val = "unknown";
    		  }
    		  srte.errorManager.clearCurrentErrorCode();
		  }
      }
      else {
    	  val = dm.getValue(element);
      }
      
      srte.log("GetValue Returned Error Code " + srte.errorManager.getCurrentErrorCode());
      return val;
   }
   
   function GetLastError () {
      srte.log("Called GetLastError() ");
      var val = srte.errorManager.getCurrentErrorCode();
      srte.log("GetLastError Returned " + val);
      return val;
   }
   
   function GetErrorString (errcode) {
      srte.log("Called GetErrorString(" + errcode + ") ");
      var val = srte.errorManager.getErrorDescription(errcode);
      srte.log("GetErrorString Returned " + val);
      return val;
   }
   
   function GetDiagnostic (errcode) {
      srte.log("Called GetDiagnostic(" + errcode + ") ");
      var val = srte.errorManager.getErrorDiagnostic(errcode);
      srte.log("GetDiagnostic Returned " + val);
      return val;
   }

   // RETURN
   return {
      // SCORM
      version : "1.0",
      Initialize : Initialize,
      Commit : Commit,
      Terminate : Terminate,
      SetValue : SetValue,
      GetValue : GetValue,
      GetLastError : GetLastError,
      GetErrorString : GetErrorString,
      GetDiagnostic : GetDiagnostic,
   };
}(SRTE_CLIENT));
