<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">


<%@ page contentType="text/html;charset=utf-8" %>

<%@page import = "java.sql.*, java.util.*,org.adl.sequencer.*,java.io.*, 
               java.lang.Boolean.*, org.adl.samplerte.util.*" %>
			   
<%
   /***************************************************************************
   **
   ** Filename:  code.jsp
   **
   ** File Description:   This file implements a menu built from the SCOs in 
   ** the current course.  It includes code from mtmcode.js which contains 
   ** the copyright information for this menu implemention.  
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
   ** Module/Package Name: Sample RTE
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
   /* This menu uses  
   ** Morten's JavaScript Tree Menu 
   ** version 2.3.0, dated 2001-04-30
   ** http://www.treemenu.com/
   **
   ** Copyright (c) 2001, Morten Wang & contributors
   ** All rights reserved.
   ***************************************************************************/
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<title>Menu</title>

<script type="text/javascript" src="mtmcode.js"></script>

<script type="text/javascript">

 
/******************************************************************************
* User-configurable options.                                                  *
******************************************************************************/

// Menu table width, either a pixel-value (number) or a percentage value.
var MTMTableWidth = "100%";

// Name of the frame where the menu is to appear.
var MTMenuFrame = "menu";

// variable for determining whether a sub-menu always gets a plus-sign
// regardless of whether it holds another sub-menu or not
var MTMSubsGetPlus = "Never";

// variable that defines whether the menu emulates the behaviour of
// Windows Explorer
var MTMEmulateWE = true;

// Directory of menu images/icons
var MTMenuImageDirectory = "menu-images/";

// Variables for controlling colors in the menu document.
// Regular BODY atttributes as in HTML documents.
var MTMBGColor = "white";
var MTMBackground = "";
var MTMTextColor = "black";

// color for all menu items
var MTMLinkColor = "black";

// Hover color, when the mouse is over a menu link
var MTMAhoverColor = "red";

// Foreground color for the tracking & clicked submenu item
var MTMTrackColor ="yellow";
var MTMSubExpandColor = "black";
var MTMSubClosedColor = "black";

// All options regarding the root text and it's icon
var MTMRootIcon = "adl_tm_small.jpg";
var MTMenuText = "";
var MTMRootColor = "black";
var MTMRootFont = "Tahoma";
var MTMRootCSSize = "10pt";
var MTMRootFontSize = "-0";

// Font for menu items.
var MTMenuFont = "Tahoma";
var MTMenuCSSize = "10pt";
var MTMenuFontSize = "-0";

// Variables for style sheet usage
// 'true' means use a linked style sheet.
var MTMLinkedSS = false;
var MTMSSHREF = "style/menu.css";

// Additional style sheet properties if you're not using a linked style sheet.
// See the documentation for details on IDs, classes & elements used 
// in the menu. Empty string if not used.
var MTMExtraCSS = "";

// Header & footer, these are plain HTML.
// Leave them to be "" if you're not using them

var MTMHeader = "";
var MTMFooter = "";

// Whether you want an open sub-menu to close automagically
// when another sub-menu is opened.  'true' means auto-close
var MTMSubsAutoClose = false;

// This variable controls how long it will take for the menu
// to appear if the tracking code in the content frame has
// failed to display the menu. Number is in tenths of a second
// (1/10) so 10 means "wait 1 second".
var MTMTimeOut = 10;

// Cookie usage.  First is use cookie (yes/no, true/false).
// Second is cookie name to use.
// Third is how many days we want the cookie to be stored.
var MTMUseCookies = false;
var MTMCookieName = "MTMCookie";
var MTMCookieDays = 3;

// Tool tips.  A true/false-value defining whether the support
// for tool tips should exist or not.
var MTMUseToolTips = true;

/******************************************************************************
* User-configurable list of icons.                                            *
******************************************************************************/

var MTMIconList = null;
MTMIconList = new IconList();
MTMIconList.addIcon(new MTMIcon("menu_link_external.gif", "http://", "pre"));
MTMIconList.addIcon(new MTMIcon("menu_link_pdf.gif", ".pdf", "post"));

var menu = new MTMenu();

<%
   //  Get the information needed to build the menu
   Vector title_vector = new Vector();
   Vector id_vector = new Vector();
   Vector level_vector = new Vector();
   Vector parent_vector = new Vector();
   Vector item_number_vector = new Vector();
   Vector enabled_vector = new Vector();
   Vector current_vector = new Vector();
   Vector selectable_vector = new Vector();
   Vector visible_vector = new Vector();
   ADLTOC toc_item = new ADLTOC();

   int length;
   int current_int_level;
   int current_index;
   int z;
   int previous_level;
   int parent_index;
   String menu_name = new String();
   int new_level;

   boolean hasAMenu = false;
   Boolean bool_obj = new Boolean(true);
   Boolean sel_obj = new Boolean(true);
   Boolean curr_obj = new Boolean(true);


   // Get the session information
   session.getServletContext();
   String courseID = (String)session.getAttribute("COURSEID");
   String userID = (String)session.getAttribute("USERID");
   String control = (String)session.getAttribute("TOC");
   String activityID = (String)session.getAttribute("ACTIVITYID");
   ADLSequencer sequencer = new ADLSequencer();
   ADLValidRequests ValidRequests = new ADLValidRequests();
   Vector TOCList = new Vector();
   SeqActivityTree activityTree = new SeqActivityTree();

   // check  to see if a menu should be displayed
   if ( (!(control == null)) && (!(control.equals(""))) && (!(courseID == null))
        && (!( courseID.equals("")) ) && (control.equals("true")) )
   {
      hasAMenu = true;
      FileInputStream in = 
         new FileInputStream(System.getProperty("user.home") + File.separator + "SCORM4EDSampleRTE111Files"+ File.separator + userID +
         File.separator + courseID + File.separator + "serialize.obj");
      ObjectInputStream ois = new ObjectInputStream(in);
      activityTree = (SeqActivityTree)ois.readObject();
      ois.close(); 
      in.close();
    
      sequencer.setActivityTree(activityTree);
     
     
      sequencer.getValidRequests(ValidRequests);
     
      TOCList = ValidRequests.mTOC;
   } 

   try
   {  // get the menu items from the list which came from the sequencer
      // items are in reverse order
      if ( hasAMenu )
      {
               
         for ( int j = TOCList.size()-1; j >= 0; j-- )
         {
            if ( TOCList.elementAt(j) != null )
            {   
	       toc_item =  (ADLTOC)TOCList.elementAt(j);

               if  ( (toc_item.mTitle != null) && (toc_item.mDepth > -1) && 
                    (toc_item.mID != null) )
               {
                  String tempTitle = toc_item.mTitle;
                  tempTitle = tempTitle.replaceAll("\\r\\n", "");  
                  tempTitle = tempTitle.replaceAll("\\n", "");
                  tempTitle = tempTitle.replaceAll("\\r", "");
                  tempTitle = tempTitle.replaceAll("\r\n", "");  
                  tempTitle = tempTitle.replaceAll("\n", "");
                  tempTitle = tempTitle.replaceAll("\r", "");
                  
                  title_vector.addElement(tempTitle);
                 
                  int l_obj = toc_item.mDepth;
                  level_vector.addElement(new Integer(l_obj));
                  
                  String tempID = toc_item.mID;
                  tempID = tempID.replaceAll("\\r\\n", "");  
                  tempID = tempID.replaceAll("\\n", "");
                  tempID = tempID.replaceAll("\\r", "");
                  tempID = tempID.replaceAll("\r\n", "");  
                  tempID = tempID.replaceAll("\n", "");
                  tempID = tempID.replaceAll("\r", "");
                  
                  id_vector.addElement(tempID);               
                  enabled_vector.
                     addElement(bool_obj.valueOf(toc_item.mIsEnabled));
                                    current_vector.
                     addElement(bool_obj.valueOf(toc_item.mIsCurrent));
                  
                  selectable_vector.
                     addElement(bool_obj.valueOf(toc_item.mIsSelectable));
                  visible_vector.                        
                     addElement(bool_obj.valueOf(toc_item.mIsVisible));
                  
               }
            }
         }
      }                   
   } // end try
   catch(Exception e)
   {
      System.out.println("problem in TOC  "); 
      e.printStackTrace();   
   }
%>  
    
    
<% // begin menu construction
   if  ( hasAMenu )
   {   
           
      int i = 0;
%>

var MTMenuText = " ";
      
<% 
      if ( title_vector.size() != 0 )
      {
         String previous_parent = "menu";
         previous_level = Integer.parseInt(level_vector.elementAt(i).toString());
         
         parent_index = 0;
         parent_vector.addElement("menu");
         length = title_vector.size();
         item_number_vector.addElement("0");
         current_index = 0; 
         // first item is menu root
         bool_obj = Boolean.valueOf(enabled_vector.elementAt(i).toString());
         sel_obj = Boolean.valueOf(selectable_vector.elementAt(i).toString());
         curr_obj = Boolean.valueOf(current_vector.elementAt(i).toString());
         // menu items are in vectors
         // information includes title of item. if it is enabled, and its 
         // nesting level
 
    if ( ( curr_obj.booleanValue() ) && ( bool_obj.booleanValue() ) )
    {  
%>

<%= parent_vector.elementAt(parent_index).toString()%>.MTMAddItem(new MTMenuItem("<%= title_vector.elementAt(i).toString()%>", 
              "javascript:top.frames['code'].launchItem('<%=id_vector.elementAt(i).toString()%>')", "code", "curr"));

<%  
    }

    else if ( (! sel_obj.booleanValue() ) && ( bool_obj.booleanValue() ) )
    {  
%>
//function MTMenuItem(text, url, target, tooltip, icon)
<%= parent_vector.elementAt(parent_index).toString()%>.MTMAddItem(
 new MTMenuItem("<%= title_vector.elementAt(i).toString()%>", "", "code", "nonsel"));
<%  
    } 


    else if ( bool_obj.booleanValue() )
         {  

%>
       
<%= parent_vector.elementAt(parent_index).toString() %>.MTMAddItem(
    new MTMenuItem("<%= title_vector.elementAt(i).toString()%>", 
              "javascript:top.frames['code'].launchItem('<%=id_vector.elementAt(i).toString()%>')",
              "code"));
<%  
         }
         else          
         {  
%>
       
<%= parent_vector.elementAt(parent_index).toString()%>.MTMAddItem(
      new MTMenuItem("<%= title_vector.elementAt(i).toString()%>", "", "code"));
<%  
         }
        
         i++;
         while ( i < length )
         { // if nesting level of current item is same as that of previous item
             

            if ( Integer.parseInt(level_vector.elementAt(i).toString()) == previous_level )
            {      
	           bool_obj = Boolean.valueOf(enabled_vector.elementAt(i).
                                          toString());
               sel_obj = Boolean.valueOf(selectable_vector.elementAt(i).toString());
               curr_obj = Boolean.valueOf(current_vector.elementAt(i).toString());
		if ( ( curr_obj.booleanValue() ) && ( bool_obj.booleanValue() ) )

               {  
%>

<%= parent_vector.elementAt(parent_index).toString()%>.MTMAddItem(
   new MTMenuItem("<%= title_vector.elementAt(i).toString()%>", 
              "javascript:top.frames['code'].launchItem('<%=id_vector.elementAt(i).toString()%>')",
              "code", "curr"));

<%  
               }
               else if ( (! sel_obj.booleanValue() ) && ( bool_obj.booleanValue() ) )

               {  
%>

<%= parent_vector.elementAt(parent_index).toString()%>.MTMAddItem(
 new MTMenuItem("<%= title_vector.elementAt(i).toString()%>", "", "code", "nonsel"));
<%  
               }
                

               else if ( bool_obj.booleanValue() )
               { 
%>
   
<%= parent_vector.elementAt(parent_index).toString()%>.MTMAddItem(
   new MTMenuItem("<%= title_vector.elementAt(i).toString()%>", 
              "javascript:top.frames['code'].launchItem('<%=id_vector.elementAt(i).toString()%>')",
              "code"));
              
<%  
               }
               else
               {
%>
    
<%= parent_vector.elementAt(parent_index).toString() %>.MTMAddItem(
   new MTMenuItem("<%= title_vector.elementAt(i).toString() %>", "", "code"));
             
<%  
               }
                  
               // increment item_number_vector at current_index so know which 
               // item are at
               Integer inc = new Integer(item_number_vector.elementAt(
                                     current_index).toString());
               new_level = inc.intValue();
               new_level++;
               item_number_vector.setElementAt(inc.toString(new_level),
                                           current_index);
               i++;
            }// end if
            // if level is greater, get new menu name, add name to 
            // parent_vector and use as current menu name
            else if ( previous_level < Integer.parseInt(level_vector.elementAt(i).toString()) )
            {   

               menu_name = "sub"+parent_index;
               Integer tempInt = new Integer(
	              item_number_vector.elementAt(current_index).toString()); 
               int item_number = tempInt.intValue();
               
%>

var <%= menu_name%> = new MTMenu();
               
<%= parent_vector.elementAt(parent_index).toString()%>.items[<%= item_number%>].
   MTMakeSubmenu(<%= menu_name%>);
               
<% 
               parent_vector.addElement(menu_name);
               parent_index++;
               item_number_vector.addElement("0");
               current_index++;
               bool_obj = Boolean.valueOf(enabled_vector.elementAt(i).
                                          toString());
               sel_obj = Boolean.valueOf(selectable_vector.elementAt(i).toString());
               curr_obj = Boolean.valueOf(current_vector.elementAt(i).toString());
		   if ( ( curr_obj.booleanValue() ) && ( bool_obj.booleanValue() ) )

               {  
               %>

<%= parent_vector.elementAt(parent_index).toString()%>.MTMAddItem(
   new MTMenuItem("<%= title_vector.elementAt(i).toString()%>", 
              "javascript:top.frames['code'].launchItem('<%=id_vector.elementAt(i).toString()%>')",
              "code", "curr"));

<%  
               }
               else if ( (! sel_obj.booleanValue() ) && ( bool_obj.booleanValue() ) )

               {  
      %>

      <%= parent_vector.elementAt(parent_index).toString()%>.MTMAddItem(
            new MTMenuItem("<%= title_vector.elementAt(i).toString()%>", "", "code", "nonsel"));
      <%  
               } 
               else if ( bool_obj.booleanValue() )
               { 
%>

<%=menu_name%>.MTMAddItem(new MTMenuItem(
   "<%=title_vector.elementAt(i).toString()%>",  
   "javascript:top.frames['code'].launchItem('<%=id_vector.elementAt(i).toString()%>')", "code"));

<%  
               }


               else
               {  
%>

<%= menu_name%>.MTMAddItem(new MTMenuItem(
   "<%= title_vector.elementAt(i).toString()%>", "", "code")); 
 
<%  
               }
               previous_level = Integer.parseInt(level_vector.elementAt(i++).toString());
            } //end else if
            else
            //if level is less
            {  
               current_int_level = previous_level - Integer.parseInt(level_vector.elementAt(i).toString()); 
               for (z = 0; z<current_int_level; z++)
               {   
                  parent_vector.removeElementAt(parent_index--);
                  item_number_vector.removeElementAt(current_index--);
               }// end for    
               
               bool_obj = Boolean.valueOf(enabled_vector.elementAt(i).
                                          toString());
               sel_obj = Boolean.valueOf(selectable_vector.elementAt(i).toString());
               curr_obj = Boolean.valueOf(current_vector.elementAt(i).toString());
               
		   if ( ( curr_obj.booleanValue() ) && ( bool_obj.booleanValue() ) )

               {  
%>

<%= parent_vector.elementAt(parent_index).toString()%>.MTMAddItem(
   new MTMenuItem("<%= title_vector.elementAt(i).toString()%>", 
              "javascript:top.frames['code'].launchItem('<%=id_vector.elementAt(i).toString()%>')",
              "code", "curr"));

<%  
               }
               else if ( (! sel_obj.booleanValue() ) && ( bool_obj.booleanValue() ) )

               { 
 
      %>

      <%= parent_vector.elementAt(parent_index).toString()%>.MTMAddItem(
            new MTMenuItem("<%= title_vector.elementAt(i).toString()%>", "", "code", "nonsel"));
      <%  
               } 
               else if ( bool_obj.booleanValue() )
               {   
%>

<%=parent_vector.elementAt(parent_index).toString()%>.MTMAddItem(
   new MTMenuItem(
   "<%=title_vector.elementAt(i).toString()%>",  
   "javascript:top.frames['code'].launchItem('<%=id_vector.elementAt(i).toString()%>')", "code"));

<%  
               }


               else
               {  
%>

<%= parent_vector.elementAt(parent_index).toString()%>.MTMAddItem(
   new MTMenuItem(
   "<%= title_vector.elementAt(i).toString()%>", "", "code")); 
 
<%  
               }  
               // increment item_number_vector at current_index so know which 
               // item are at
               Integer inc = new Integer(item_number_vector.
                                         elementAt(current_index).toString());
		
               new_level = inc.intValue();

               new_level++;
               item_number_vector.setElementAt(inc.toString(new_level), 
                                               current_index);
               previous_level = Integer.parseInt(level_vector.elementAt(i++).toString());
               

                
            }// end else
         } //end while
      }//end if not zero length
   } // end menu creation
%>

</script>
   
</head> 

<body onload="MTMStartMenu()" id="codeBody">   
  <!-- bgcolor="#FFFFFF" text="#black" link="yellow" 
   vlink="lime" alink="red" -->
</body>

</html>