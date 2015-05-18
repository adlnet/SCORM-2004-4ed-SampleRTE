<%
   String is_admin = (String)session.getAttribute( "AdminCheck" );
   String id = session.getId();
   // Condition statement to determine if the Session Variable has been
   // properly set.
   if ( (! (is_admin == null)) && ( is_admin.equals("true")) )
   {
%>

<%@page import = "java.util.*, org.adl.samplerte.util.*,
                  org.adl.samplerte.server.*" %>

<% 
   /***************************************************************************
   **
   ** Filename:  dsp_importResults.jsp
   **
   ** File Description: This page displays the result of the pacakage import 
   ** process and a link to return to the main menu.
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
<%@page import = "org.adl.validator.util.*, java.util.*,
                  org.adl.samplerte.server.*"%>
<%@include file="importUtil.jsp" %>
<%
      String currMessage = "";      
      
      List results = (List)request.getAttribute("result");
      boolean onlineValidation = Boolean.valueOf(request.getAttribute("onlineValidation").toString()).booleanValue();
      String importAttempted = String.valueOf(request.getAttribute("importAttempted"));
      
      String header = "<table>";  
          				  
      String status = "<tr><td'><b>All packages imported successfully.</b></td></tr>";
      String body = "";      
      String closing = "</table>";
      String bodyString = "";
      boolean overallValidation = true;
      int resultCount = 2;
      
      boolean overallOfflineWithErrors = false;
      
      if ( importAttempted.equals("true") )
      {        
         ResultCollection collection = new ResultCollection();           
         
         resultCount = results.size();
         // Loop through the result collections for each course imported
         Iterator resultIter = results.iterator();
         while ( resultIter.hasNext() )
         {
         	Result res = new Result();             
            boolean courseStatus = true; 
            boolean offineWithErrors = false;
            String packageName = ""; 
            String resultMessages = "";  
            String offlineResultsMessages = "";
         
         	// Get course name
          	packageName = resultIter.next().toString();
          	// Get course validation results          	
         	collection = (ResultCollection)resultIter.next();
          	
          	// Check to see if submanifests or external files were referenced
          	Result subRes = collection.getPackageResult(ValidatorCheckerNames.SUBMANIFEST);
          	Result extRes = collection.getPackageResult(ValidatorCheckerNames.RES_HREF);
          	if ( subRes != null && extRes != null )
          	{
          	    boolean failImport = subRes.isTestStopped() || extRes.isTestStopped();
          	    if ( failImport )
          	    {
          	      courseStatus = false;
          	      overallValidation = false;
          	    }
          	}
         	
         	Iterator collectionIter = collection.getPackageResultsCollection().iterator();
         	while ( collectionIter.hasNext() )
         	{         	   
         	   res = (Result)collectionIter.next();
         	   
	           boolean pass = res.isPackageCheckerPassed();
	            
	           if ( !pass )
	           {
	              if ( !onlineValidation && 
                       (res.getPackageCheckerName().equals(ValidatorCheckerNames.SCHEMA_VAL) || 
                       res.getPackageCheckerName().equals(ValidatorCheckerNames.RES_HREF) ) )
	              {
                     overallOfflineWithErrors = true;
                     offineWithErrors = true;
	              }
	              else
	              {
	                 courseStatus = false;               
                     overallValidation = false; 
	              }
	           }
	            
	           List messages = res.getPackageCheckerMessages();
	           Iterator messageIter = messages.iterator();
	           int messageType = -1;
	           ValidatorMessage currValidatorMsg;
               String tempMessage = "";
	           while ( messageIter.hasNext() )
	           {
	              currValidatorMsg  = (ValidatorMessage)messageIter.next();
	              messageType = currValidatorMsg.getMessageType();
	              currMessage = currValidatorMsg.getMessageText();
	              
	              currMessage = currMessage.replaceAll("<", "&lt;");         	
         		  currMessage = currMessage.replaceAll(">", "&gt;");    
         	
	              if ( messageType == ValidatorMessage.FAILED )
	              {
	                 if ( !onlineValidation && 
	                       (res.getPackageCheckerName().equals(ValidatorCheckerNames.SCHEMA_VAL) || 
	                             res.getPackageCheckerName().equals(ValidatorCheckerNames.RES_HREF) ) )
	                  {
	                    tempMessage = tempMessage + 
      	                 "<tr><td class='red_text'>ERROR: " + currMessage + 
      	                 "</td></tr>";
	                  }
	                  else
	                  {
	                     resultMessages = resultMessages + 
                         "<tr><td class='red_text'>ERROR: " + currMessage + 
                         "</td></tr>";
                      }
	              }
	              if ( messageType == ValidatorMessage.WARNING )
	              {
                     if ( !onlineValidation && 
                           (res.getPackageCheckerName().equals(ValidatorCheckerNames.SCHEMA_VAL) || 
                                 res.getPackageCheckerName().equals(ValidatorCheckerNames.RES_HREF) ) )
                     {
                       tempMessage = tempMessage + 
                        "<tr><td class='orange_text'>WARNING: " + currMessage + 
                        "</td></tr>";
                     }
                     else
                     {
                        resultMessages = resultMessages + 
                        "<tr><td class='orange_text'>WARNING: " + currMessage + 
                        "</td></tr>";
                     }
	              }
	              if ( messageType == ValidatorMessage.OTHER )
	              {
                    if ( !onlineValidation && 
                          (res.getPackageCheckerName().equals(ValidatorCheckerNames.SCHEMA_VAL) || 
                                res.getPackageCheckerName().equals(ValidatorCheckerNames.RES_HREF) ) )
                     {
                       tempMessage = tempMessage + 
                        "<tr><td>" + currMessage + "</td></tr>";
                     }
                     else
                     {
                        resultMessages = resultMessages + 
                        "<tr><td>" + currMessage + "</td></tr>";  
                     }
	                 
	              }
	           }
	           if ( !tempMessage.equals("") && !onlineValidation && 
                    res.getPackageCheckerName().equals(ValidatorCheckerNames.SCHEMA_VAL ) )
               {
	              offlineResultsMessages = offlineResultsMessages +
                    "<tr><td class='orange_text'>The following schema validation error(s) occurred during the package import process.  " +   
                    "These errors may be related to the lack of an internet connection during the import process.</td></tr>" +
                    "<tr><td><hr /></td></tr>" +
                    tempMessage + "<tr><td>&nbsp;</td></tr>";
               }
	           if ( !tempMessage.equals("") && !onlineValidation && 
                    res.getPackageCheckerName().equals(ValidatorCheckerNames.RES_HREF ) )
               {
                  offlineResultsMessages = offlineResultsMessages +
                    "<tr><td class='orange_text'>The following file reference error(s) occurred during the package import process.  " +   
                    "These errors may be related to the lack of an internet connection during the import process.</td></tr>" +
                    "<tr><td><hr /></td></tr>" +
                    tempMessage + "<tr><td>&nbsp;</td></tr>";
               }
         	}
         	
            // Output message for offline import that threw allowable errors
            if ( offineWithErrors && courseStatus )               
            {
               body = body + "<tr><td><br><b>The package <u>" + packageName + 
               "</u> imported.</b></td></tr>";
            }	        
            else if ( courseStatus )	// passed validation
		    {
		       body = body + "<tr><td><br><b>The package <u>" + packageName + 
		       "</u> imported successfully.</b></td></tr>";
		    }
		    else	// failed validation
		    {
		       body = body + "<tr><td><br><b>The package <u>" + packageName + 
		       "</u> did not import successfully.</b></td></tr>";	              
		    }
	        body = body + resultMessages + "<tr><td>&nbsp;</td></tr>" + offlineResultsMessages;	        
         }	
      } 
      else // Import process failed
      {
      	 overallValidation = false;
         String errorMessage = String.valueOf(request.getAttribute("errorMsg"));
         body = body + "<tr><td class='red_text'>" + errorMessage + "</td></tr>";	  
      }
      
      if ( !overallValidation )
      {
         status = "<tr><td class='red_text'><b>One or more packages failed to import</b></td></tr>";	
      }
      else if ( overallValidation && overallOfflineWithErrors )
      {
         status = "<tr><td class='green_text'><b>The Package(s) Imported.</b></td></tr>";
      }
      else
      {
         status = "<tr><td class='green_text'><b>The Package(s) Imported Successfully.</b></td></tr>";	
      }
      
      if ( resultCount > 2 )
      {
      	status = status + "<tr><td><hr></td></tr>";
      }
      
      bodyString = header + status + "<br>" + body + closing;      
%>
<!doctype html>
<html lang="en">
<head>
    <title>Import Course</title>
    <link href="/adl/includes/sampleRTE_style.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<body bgcolor="#FFFFFF" style="margin-top:4.5em;">

<jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
    <jsp:param value="" name="helpURL"/>
</jsp:include>

<%=bodyString%>
</body>
</html>

<%
   }   
   else
   {
      // Redirect the user to the RTE Home page.
      response.sendRedirect( "/adl/LMSCourseAdmin?type=" + ServletRequestTypes.GO_HOME + "&userID="); 
   }   
%>
