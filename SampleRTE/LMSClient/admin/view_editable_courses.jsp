<%@page import = "org.adl.samplerte.server.ServletRequestTypes, org.adl.samplerte.server.CourseData, org.adl.samplerte.server.ItemData, java.util.List, java.util.ArrayList" %>
<%
   String is_admin = (String)session.getAttribute( "AdminCheck" );
   String id = session.getId();
   // Condition statement to determine if the Session Variable has been
   // properly set.
   if ( (! (is_admin == null)) && ( is_admin.equals("true")) )
   {
      List<CourseData> courses = (List<CourseData>)request.getAttribute("courses");
      courses = (courses == null) ? new ArrayList<CourseData>() : courses;
      //String courseid = request.getParameter("courseID");
      //String coursetitle = request.getParameter("courseTitle");
      //List<ItemData> items = null;
      //if (coursedata != null) {
      //   courseid = coursedata.mCourseID;
      //   coursetitle = coursedata.mCourseTitle;
      //   items = coursedata.items;
      //}
%>

<!doctype html>
<html lang="en">
   <head>
      <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
      <script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
      <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
   </head>
   <body style="margin-top: 4.5em;">
      <jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
       <jsp:param value="none" name="helpURL"/>
      </jsp:include>
      <h2>Courses available to edit</h2>
      <div class="container">
         <% if (courses.size() > 0) { %>
         <table id="ctable" class="table table-hover">
            <thead><tr><th>Course Name</th><th>Course URI</th></tr></thead>
            <tbody>
	         <% for (CourseData cd : courses) { %>
					<tr>
						<td><%= cd.mCourseTitle %></td>
						<td><%= cd.mCourseID %></td>
					</tr>
	         <% } %>
	         </tbody>
         </table>  
         <% } %>
      </div>
      <div class="col-md-2" style="display:none;">
         <form method="post" action="/adl/LMSCourseAdmin" id="editCourse" name="editCourse" accept-charset="utf-8">
             <input type="hidden" name="type" value="<%= ServletRequestTypes.UPDATE_EXT_COURSE %>" />
             <input type="hidden" name="courseID" id="courseID" />
            <input type="hidden" name="courseTitle" id="courseTitle" />
         </form>
         
      </div>
      
      <script>
         
         $('#ctable > tbody > tr').click(function(e, f, g) {
             $('#courseTitle').val(e.currentTarget.children[0].innerText);
             $('#courseID').val(e.currentTarget.children[1].innerText);
             $('#editCourse').submit();
         });
      </script>
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