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
   ** Filename:  dsp_comments.jsp
   **
   ** File Description: This file allows an administrator to update Comments
   **                   from LMS for selected scos.
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
<script>
/*************************************************************************
* Method: newWindow()
* Input: pageName
* Output: none
*
* Description: Opens the page input by 'pageName' in a new browser window.
*************************************************************************/
function newWindow(pageName)
{
   window.open(pageName, 'Help', "toolbar=no,location=no,directories=no," +
               "status=no,menubar=no,scrollbars=yes,resizable=yes," + 
               "width=500,height=500");
}
</script>
<%
  // Obtain the SCOData Object, then Display the SCO Title
  // include the hidden value of the SCO ID
  SCOData scoInfo = new SCOData();
  scoInfo = (SCOData)request.getAttribute( "sco" );
%>

<html>
<head>
   <title>SCORM 2004 4th Edition Sample Run-Time Environment Version 1.1.1 - 
   Comments From LMS Admin</title>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

   <link href="includes/sampleRTE_style.css" rel="stylesheet" type="text/css">

   <%
      // Split the comments within the Vector to a delimited list
      Vector commentVector = new Vector();
      commentVector = scoInfo.mComment;
      Vector dateTimeVector = new Vector();
      dateTimeVector = scoInfo.mDateTime;
      Vector locationVector = new Vector();
      locationVector = scoInfo.mLocation;

      // Split the comments within the Vector to a delimited list
      String commentString = "";
      String dateString = "";
      String locationString = "";

      // Loop over comment Vector and fill comment string
      for ( int i=0; i< commentVector.size(); i++)
      {
        String scoComment = (String)commentVector.elementAt(i);
        String dateTimeStamp = (String)dateTimeVector.elementAt(i);
        String location = (String)locationVector.elementAt(i);

       // Condition for 1st iteration through
       if ( commentString == "")
       {
          commentString = scoComment;
          dateString = dateTimeStamp;
          locationString = location;
       }
       else
       {
          commentString = commentString + "[.]" + scoComment;
          dateString = dateString + "[.]" + dateTimeStamp;
          locationString = locationString + "[.]" + location;
       }
      }
   %>


   <script language="JavaScript">

   // Global Variables
   var commentList = "";
   var dspComments = "";
   var commentsEntered = "";
   var delimintedList = "";
   var firstCommentEntered = 0;
   var firstLocationEntry = 0;
   var firstListEntry = 0;
   var firstListEntry1 = 0;
   var lineString = "----------------------------------------------------";
   var windowCreated = false;

   /**********************************************************************
   ** Function: populateText()
   **
   ** Description:
   **    Populated the textarea display with the retrieved value.
   **
   **********************************************************************/
   function populateText()
   {
      // Populate the Global comment Variable
      commentList =  "<%=commentString%>";
      dateTimeList = "<%=dateString%>";
      locationList = "<%=locationString%>";

      var displayedComments = "";

      // Check if any current Comments Exist
      if (commentList != "")
      {
        firstCommentEntered = 1;

        // Call Parse Method to remove the deliminters
        displayedComments = parseComment(commentList,dateTimeList,locationList);
      }

      // Asign the text Area value to the parsed variable
      scoAdmin.scoComment.value = displayedComments;
   }

   /**********************************************************************
   ** Function: doAdd()
   **
   ** Description:
   **    Adds additional comments to the list of existing comments.
   **
   ***********************************************************************/
   function doAdd()
   {
      // Append to the comment string after every new comment is added
      commentswin = window.open("admin/commentsWin.htm","CommentsLMS",
                                "HEIGHT=250,WIDTH=600 status=no location=no");
      windowCreated = true;

   }

   /**********************************************************************
   ** Function: doClear()
   **
   ** Description:
   **    Clears all current comments.
   **
   **********************************************************************/
   function doClear()
   {
      // Clear the comment string upon user clicking the Clear Comments Button
      dspComments = "";
      delimitedList = "";
      locationString = "";
      firstCommentEntered = 0;
      firstListEntry1 = 0;
      firstListEntry = 0;
      firstLocationEntry = 0;
      commentList = "";
      commentsEntered = "";

      scoAdmin.scoComment.value = dspComments;
      document.scoAdmin.comments.value = "";
      document.scoAdmin.locations.value = null;

      // Set the update value to false
      document.scoAdmin.update.value = "false";
   }

   /**********************************************************************
   ** Function: refresh()
   **
   ** Description:
   **    Calls upon the addition of a new comment.  Refreshes the internal
   **    list representation and the GUI
   **********************************************************************/
   function refresh(incomingComment,timeStamp,incomingLocation)
   {
      var formatedComment = parseComment(incomingComment,timeStamp,
                                                              incomingLocation);

      if (incomingLocation == "")
      {
         incomingLocation = null;
      }

      locationString = createLocationString(incomingLocation);
      delimitedList = createDelimitedList(incomingComment);

      document.scoAdmin.comments.value = delimitedList;
      document.scoAdmin.locations.value = locationString;
      scoAdmin.scoComment.value = formatedComment;
   }

   /**********************************************************************
   ** Function: parseComment()
   **
   ** Description:
   **  This function is used to parse the deliminter out of the comment
   **  list to better display the comments to the user.
   **********************************************************************/
   function parseComment(commentToParse,dateStampToParse,locationToParse)
   {
      var internalDelimiter = "[.]";
      arrayOfComments = commentToParse.split(internalDelimiter);
      arrayOfDateStamps = dateStampToParse.split(internalDelimiter);
      arrayOfLocations = locationToParse.split(internalDelimiter);

      for (i=0; i< arrayOfComments.length; i++)
      {
         if (arrayOfLocations[i] == "null")
         {
           arrayOfLocations[i] = "";
         }

         if (firstListEntry1 == 0)
         {
            dspComments = arrayOfComments[i] + "\n" +
                              "Location:" + arrayOfLocations[i] + " \n" + "[" +
                                                     arrayOfDateStamps[i] + "]";
            firstListEntry1 = 1;
         }
         else
         {
            dspComments = dspComments + "\n" + lineString + "\n" +
                   arrayOfComments[i] + "\n" + "Location:" + arrayOfLocations[i]
                    + " \n" + "[" + arrayOfDateStamps[i] + "]";
         }
      }
      return dspComments;
   }



   /**********************************************************************
   ** Function: createDelimitedList()
   **
   ** Description:
   **  This function is used to create the delimited list to send on
   **********************************************************************/
   function createDelimitedList(stringToParse)
   {
      var internalDelimiter = "[.]";
      arrayOfStrings = stringToParse.split(internalDelimiter);

      for (i=0; i< arrayOfStrings.length; i++)
      {
         if (firstListEntry == 0)
         {
            internalDelimitedList = arrayOfStrings[i];
            firstListEntry = 1;
         }
         else
         {
            internalDelimitedList = internalDelimitedList + "\n" +
                                    arrayOfStrings[i];
         }
      }
      return internalDelimitedList;
   }


   /**********************************************************************
   ** Function: createLocationString()
   **
   ** Description:
   **  This function is used to create a location string
   **********************************************************************/
   function createLocationString(stringToParse)
   {
      var internalDelimiter = "[.]";

      if (stringToParse == null)
      {
         if (firstLocationEntry == 0)
         {
            internalLocationList = null;
            firstLocationEntry = 1;
         }
         else
         {
            internalLocationList = internalLocationList + "\n" + null;
         }
      }
      else
      {
         arrayOfStrings = stringToParse.split(internalDelimiter);

         for (i=0; i< arrayOfStrings.length; i++)
         {
            if (firstLocationEntry == 0)
            {
               internalLocationList = arrayOfStrings[i];
               firstLocationEntry = 1;
            }
            else
            {
               internalLocationList = internalLocationList + "\n" +
                                    arrayOfStrings[i];
            }
         }
      }

      return internalLocationList;
   }


   /**********************************************************************
   ** Function: closeWin()
   **
   ** Description:
   **    This function closes the comment window 
   **********************************************************************/
   function closeWin()
   {
      if ( windowCreated )
      {
         commentswin.close();
      }
   }

   /*************************************************************************
   * Method: newWindow()
   * Input: pageName
   * Output: none 
   *
   * Description: Opens the page input by 'pageName' in a new browser window.
   *************************************************************************/
   function newWindow(pageName)
   {
      window.open(pageName, 'Help', "toolbar=no,location=no,directories=no," +
                  "status=no,menubar=no,scrollbars=yes,resizable=yes," + 
                  "width=500,height=500");
   }
   </script>


</head>

   <body bgcolor="#FFFFFF" onload="populateText()" onunload="closeWin()" style="margin-top: 4.5em;">

   <jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
       <jsp:param value="/adl/help/manageCoursesHelp.htm" name="helpURL"/>
   </jsp:include>

   <p>
   <a href="javascript:history.go(-1)">Back To SCO View</a>
   </p>

   <p class="font_header">
   <b>
      Add Comments
   </b>
   </p>

   <form method="post" action="/adl/LMSCourseAdmin" name="scoAdmin"
                                      accept-charset="utf-8" id="scoAdmin">
      <input type="hidden" name="type" value="<%= ServletRequestTypes.UPDATE_SCO %>" />
      <input type="hidden" name="comments" value="">
      <input type="hidden" name="update" value="true">
      <input type="hidden" name="locations" value="">
      <input type="hidden" name="scoID" value="<%=scoInfo.mActivityID%>"/>

      <table width="450" border="0">
         <tr>
            <td>
               <hr>
            </td>
         </tr>
         <tr>
            <td bgcolor="#5E60BD" colspan="3" class="white_text"><b>Please
             edit comments as needed:</b></td>
         </tr>
      </table>

      <table width="450" border="0" >

         <tr>
            <td >
              <label for="scoComments">
                 <b>Comments for <%=scoInfo.mItemTitle%> </b>
              </label>
            </td>
         </tr>

         <tr>
            <td><textarea cols="53" rows="15" name="scoComment" readonly>
               </textarea></td>
         </tr>
         <tr>
            <td>
               <hr />
            </td>
         </tr>
         <tr>
            <td colspan="3" align="center">
               <input type="button" name="add" value="Add Comment"
                                               onclick="JavaScript: doAdd()" />
               <input type="button" name="clear" value="Reset Comments"
                                               onclick="JavaScript: doClear()"
                                               />
               <input type="submit" name="submit" value="Submit" /> 
            </td>
         </tr>

      </table>

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