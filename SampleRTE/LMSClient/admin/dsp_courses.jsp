<%@ page contentType="text/html;charset=utf-8" %>

<%
   String is_admin = (String)session.getAttribute( "AdminCheck" );

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
   ** Filename:  dsp_courses.jsp
   **
   ** File Description: This file displays a list of courses.
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


<html>
<%       

   request.setCharacterEncoding("utf-8");  
   String setProcess = (String)request.getAttribute("setProcess");
   int valueInt = 0;
   String formBody = "";
   String submitValue = "";
   String submissionType = "";
   String coursesListMsg = "";
   String helpScreen = "";

   Vector coursesList = new Vector();
   coursesList = (Vector)request.getAttribute("courses");


   if (  coursesList != null && coursesList.size() == 0 ) 
   {
        formBody = "<p class='font_header'><b>" +
            "No courses have been imported into the Sample RTE." +
            "</b></p>";
   }
   else
   {
  
   if (coursesList != null)
   {   

	  if ( setProcess.equals("manage") )
      {         
	     coursesListMsg = "Manage Course List";
         submissionType = "manage";
         valueInt = ServletRequestTypes.GET_SCOS;
		 String color = "#FFFFFF";
         for ( int i=0; i< coursesList.size(); i++)
         {
         	CourseData courseItem = (CourseData)coursesList.elementAt(i);
         	if ( i == 0 )
         	{
         	   formBody += "<tr bgcolor='" + color + "'><td><input type='radio' id='courseID' name='courseID'" + 
         	   " value='" + courseItem.mCourseID + "' checked>" + courseItem.mCourseTitle + 
         	   " - " + courseItem.mImportDateTime + "</input></td></tr>";
         	}
         	else
         	{
         	   formBody += "<tr bgcolor='" + color + "'><td><input type='radio' id='courseID' name='courseID'" + 
         	   " value='" + courseItem.mCourseID + "'>" + courseItem.mCourseTitle + " - " + 
               courseItem.mImportDateTime + "</input></td></tr>";
         	}
         	if(color.equals("#FFFFFF"))
	        {
	             color = "#CDCDCD";
	        }
	        else
	        {
	             color = "#FFFFFF";
	        }
         }
         
         formBody += "</select></td></tr>";
         submitValue = "";
         helpScreen = "/adl/help/manageCoursesHelp.htm";
      
        
	    } 
	    else 
	    {
	       coursesListMsg = "Delete Course List";
           submissionType = "delete";
	       String color = "#FFFFFF";
	       valueInt = ServletRequestTypes.DELETE_COURSE;
	       for ( int i=0; i< coursesList.size(); i++)
	       {
	          CourseData courseItem = (CourseData)coursesList.elementAt(i);
	
	          formBody = formBody + "<tr bgcolor='" + color + 
                  "'><td width='10%'>" + 
                  "<input type='checkbox' name='chkCourse' id='" + 
                  courseItem.mCourseID + "' value='" + courseItem.mCourseID + 
                  "'/></td><td><label for='" + courseItem.mCourseID + "'><p>" +
                  courseItem.mCourseTitle + "<br>Imported on: " + 
	              courseItem.mImportDateTime + "</p></label></td></tr>";
	          
	          if(color.equals("#FFFFFF"))
	          {
	             color = "#CDCDCD";
	          }
	          else
	          {
	             color = "#FFFFFF";
	          }
	       }                                        
           submitValue="return checkValues()";
           helpScreen = "/adl/help/deleteCoursesHelp.htm";                         
       }
      
      // Inserting the beginning of the table in front of what was 
      // compiled by the function so far
      formBody = "<table width='458'><tr><td colspan='2'><hr></td>" + 
       "</tr><tr><td bgcolor='#5E60BD' colspan='2' class='white_text'><b>" +
        "Please select courses: </b></td></tr></table>" + 
         "<table width='458'><tr bgcolor='white'><td align='left'" +
         " colspan='3'></td></tr>" + formBody;
     
     
        formBody += "<tr><td colspan='3'><input type='hidden' name='type'" +  
            "value=" + valueInt + " /><hr></td></tr><tr>" + 
            "<td colspan='3' align='center'>" +
             "<input type='submit' name='submit' value='Submit' /></td>" +
             "</tr></table>";
        
    }
   
   }
%>


<SCRIPT language="javascript">
var submitType = "";
/****************************************************************************
** isArray(obj)
** Returns true if the object is an array, else false
***************************************************************************/
function isArray(obj){return(typeof(obj.length)=="undefined")?false:true;}


/****************************************************************************
** getInputValue(input_object[,delimiter])
**   Get the value of any form input field
**   Multiple-select fields are returned as comma-separated values, or
**   delmited by the optional second argument
**  (Doesn't support input types: button,file,reset,submit)
***************************************************************************/
function getInputValue(obj,delimiter) {
    
	var use_default=(arguments.length>2)?arguments[2]:false;
	if (isArray(obj) && (typeof(obj.type)=="undefined")) {
		var values=new Array();
        ;
		for(var i=0;i<obj.length;i++){
			var v=getSingleInputValue(obj[i],use_default,delimiter);
			if(v!=null){values[values.length]=v;}
			}
		return commifyArray(values,delimiter);
		}
	return getSingleInputValue(obj,use_default,delimiter);
	}

/****************************************************************************
** getSingleInputValue(input_object,use_default,delimiter)
**   Utility function used by others
***************************************************************************/
function getSingleInputValue(obj,use_default,delimiter) {
	switch(obj.type){
		case 'radio': case 'checkbox': return(((use_default)?obj.defaultChecked:obj.checked)?obj.value:null);
		case 'text': case 'hidden': case 'textarea': return(use_default)?obj.defaultValue:obj.value;
		case 'password': return((use_default)?null:obj.value);
		case 'select-one':
			if (obj.options==null) { return null; }
			if(use_default){
				var o=obj.options;
				for(var i=0;i<o.length;i++){if(o[i].defaultSelected){return o[i].value;}}
				return o[0].value;
				}
			if (obj.selectedIndex<0){return null;}
			return(obj.options.length>0)?obj.options[obj.selectedIndex].value:null;
		case 'select-multiple': 
			if (obj.options==null) { return null; }
			var values=new Array();
			for(var i=0;i<obj.options.length;i++) {
				if((use_default&&obj.options[i].defaultSelected)||(!use_default&&obj.options[i].selected)) {
					values[values.length]=obj.options[i].value;
					}
				}
			return (values.length==0)?null:commifyArray(values,delimiter);
		}
	alert("FATAL ERROR: Field type "+obj.type+" is not supported for this function");
	return null;
}

/****************************************************************************
** commifyArray(array[,delimiter])
**  Take an array of values and turn it into a comma-separated string
**  Pass an optional second argument to specify a delimiter other than
**   comma.
***************************************************************************/
function commifyArray(obj,delimiter){
	if (typeof(delimiter)=="undefined" || delimiter==null) {
		delimiter = ",";
		}
	var s="";
	if(obj==null||obj.length<=0){return s;}
	for(var i=0;i<obj.length;i++){
		s=s+((s=="")?"":delimiter)+obj[i].toString();
		}
	return s;
}

/****************************************************************************
** Function:  checkValues()
** Input:   none
** Output:  boolean
**
** Description:  This function ensures that a course is selected
**               before submitting.
**
***************************************************************************/
function checkValues()
{              
    submitType = "<%=submissionType%>";
    if ( submitType == "delete" ) 
    {
        var inputValue = "";
        inputValue = getInputValue(scoAdmin.chkCourse);
        
        if (inputValue==""  || inputValue==null)
        {
           alert('Please Select a Course.');
           return false;
        }
    }
return true;
}

/****************************************************************************
**
** Function:  newWindow()
** Input:   pageName = Name of the window
** Output:  none
**
** Description:  This method opens a window named <pageName>
**
***************************************************************************/
function newWindow(pageName)
{
   window.open(pageName, 'Help',"toolbar=no,location=no,directories=no," +
               "status=no,menubar=no,scrollbars=yes,resizable=yes," +
               "width=500,height=500");
}
</SCRIPT>

<head>
   <title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1
   - Courses</title>
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

   <link href="includes/sampleRTE_style.css" rel="stylesheet" type="text/css">

</head>

   <body bgcolor="#FFFFFF" style="margin-top: 4.5em;">

   <jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
       <jsp:param value="<%= helpScreen %>" name="helpURL"/>
   </jsp:include>

   <p class="font_header">
   <b>
      <%= coursesListMsg %>
   </b>
   </p>
     <form method='post' action='/adl/LMSCourseAdmin'name='scoAdmin'
         onSubmit="return checkValues()"  >
         
       	<%= formBody %> 
         
   </form>
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
