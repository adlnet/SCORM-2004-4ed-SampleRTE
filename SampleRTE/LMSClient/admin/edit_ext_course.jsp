<%@page import = "org.adl.samplerte.server.ServletRequestTypes, org.adl.samplerte.server.CourseData, org.adl.samplerte.server.ItemData, java.util.List" %>
<%
   String is_admin = (String)session.getAttribute( "AdminCheck" );
   String id = session.getId();
   // Condition statement to determine if the Session Variable has been
   // properly set.
   if ( (! (is_admin == null)) && ( is_admin.equals("true")) )
   {
      CourseData coursedata = (CourseData)request.getAttribute("coursedata");
      String courseid = request.getParameter("courseID");
      String coursetitle = request.getParameter("courseTitle");
      int active = -1;
      List<ItemData> items = null;
      if (coursedata != null) {
         courseid = coursedata.mCourseID;
         coursetitle = coursedata.mCourseTitle;
         active = coursedata.active;
         items = coursedata.items;
      }
%>

<!doctype html>
<html>
   <head>
      <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
      <script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
      <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
      <script>
         function checkData(form)
         {
        	   if (form.itemID.value === "" && form.itemTitle.value === "")
    		   {
        		   alert("Item Id and item title cannot be blank");
        		   return false;
    		   }
        	   return true;
         }
      </script>
   </head>
   <body style="margin-top: 4.5em;">
      <jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
       <jsp:param value="none" name="helpURL"/>
      </jsp:include>
      <h2>Edit an external course
         <% if (active == 0) {%>
         <small>
         <div class="row">
            <div class="col-md-12">
               <form method="post" action="/adl/LMSCourseAdmin" name="publish" accept-charset="utf-8">
                  <input type="hidden" name="type" value="<%= ServletRequestTypes.PUBLISH_EXT_ITEM %>" />
                  <input type="hidden" name="courseID" id="courseID" value="<%= courseid %>" />
                  <button type="submit" name="submit" class="btn btn-xs btn-primary">Publish</button>
               </form>
            </div>
         </div>
         </small>
         <% } else if (active == 1) { %>
         <small>
         <div class="row">
            <div class="col-md-12">
               <form method="post" action="/adl/LMSCourseAdmin" name="unpublish" accept-charset="utf-8">
                  <input type="hidden" name="type" value="<%= ServletRequestTypes.UNPUBLISH_EXT_ITEM %>" />
                  <input type="hidden" name="courseID" id="courseID" value="<%= courseid %>" />
                  <button type="submit" name="submit" class="btn btn-xs btn-primary">Unpublish</button>
               </form>
            </div>
         </div>
         </small>
         <% } %>
      </h2>
      <h4><%= request.getParameter("courseID") %></h4>
      <div class="container">
         <form method="post" action="/adl/LMSCourseAdmin" name="newCourse" accept-charset="utf-8">
            <input type="hidden" name="type" value="<%= ServletRequestTypes.UPDATE_EXT_COURSE %>" />
            <input type="hidden" name="courseID" id="courseID" value="<%= courseid %>" />
            </div>
            <div class="form-group">
               <label for="courseTitle">Course Title</label>
               <input type="text" class="form-control" name="courseTitle" id="courseTitle" value="<%= coursetitle %>" placeholder="Making Tea" />
            </div>
            
            <button type="submit" name="submit" class="btn btn-default">Save</button>
         </form>
         
         <h3>Add an Item (SCO)</h3>
         <form method="post" action="/adl/LMSCourseAdmin" name="addCourseItem" accept-charset="utf-8" onSubmit="return checkData(this)">
            <input type="hidden" name="type" value="<%= ServletRequestTypes.ADD_EXT_ITEM %>" />
            <input type="hidden" name="courseID" id="courseID" value="<%= courseid %>" />
            <input type="hidden" name="courseTitle" id="courseTitle" value="<%= coursetitle %>" />
            <div class="form-group">
               <label for="itemID">Item URI</label>
               <input type="text" class="form-control" name="itemID" id="itemID" placeholder="http://adlnet.gov/course/making-tea/item/getting-cup" />
            </div>
            <div class="form-group">
               <label for="itemTitle">Item Title</label>
               <input type="text" class="form-control" name="itemTitle" id="itemTitle" placeholder="Getting a Cup" />
            </div>
            <div class="form-group">
               <label for="itemLaunch">Item Launch URL (optional)</label>
               <input type="text" class="form-control" name="itemLaunch" id="itemLaunch" placeholder="http://courses.com/course/making-tea/item/getting-cup" />
            </div>
            
            <button type="submit" name="submit" class="btn btn-primary">Add Item</button>
         </form>
         
         <% if (items != null) { %>
         <h3>Course Items (SCOs)</h3>
         <div class="row" id="course_items">
            <%
            for(ItemData c_item : items) {
            %>
            
                <div class="col-md-12" style="margin-bottom: 1em; padding-bottom: 1em; border-bottom: 1px solid gray">
                
	                <form method="post" action="/adl/LMSCourseAdmin" name="addCourseItem" accept-charset="utf-8">
			            <input type="hidden" name="type" value="<%= ServletRequestTypes.UPDATE_EXT_ITEM %>" />
			            <input type="hidden" name="courseID" id="courseID" value="<%= courseid %>" />
			            <input type="hidden" name="courseTitle" id="courseTitle" value="<%= coursetitle %>" />
			            <input type="hidden" name="activityID" id="activityID" value="<%=c_item.activityID%>" />
			            <div class="form-group">
			               <label for="itemID">Item URI</label>
			               <input type="text" class="form-control" name="itemID" id="itemID" value="<%=c_item.itemID %>" placeholder="http://adlnet.gov/course/making-tea/item/getting-cup" />
			            </div>
			            <div class="form-group">
			               <label for="itemTitle">Item Title</label>
			               <input type="text" class="form-control" name="itemTitle" id="itemTitle" value="<%=c_item.itemTitle %>" placeholder="Getting a Cup" />
			            </div>
			            <div class="form-group">
			               <label for="itemLaunch">Item Launch URL</label>
			               <input type="text" class="form-control" name="itemLaunch" id="itemLaunch" value="<%=c_item.itemLaunch %>" placeholder="http://courses.com/course/making-tea/item/getting-cup" />
			            </div>
			            
			            <button type="submit" name="submit" class="btn btn-default">update</button>
			         </form>
               </div> 
            <%
            }
            %>
         </div>
         <% } %>
         
         
            
      </div>
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