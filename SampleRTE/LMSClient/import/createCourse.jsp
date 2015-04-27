<%@page import = "org.adl.samplerte.server.ServletRequestTypes" %>
<%
   String is_admin = (String)session.getAttribute( "AdminCheck" );
   String id = session.getId();
   // Condition statement to determine if the Session Variable has been
   // properly set.
   if ( (! (is_admin == null)) && ( is_admin.equals("true")) )
   {
%>

<!doctype html>
<html>
   <head>
	   <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
	   <script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
	   <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
   </head>
   <body style="margin-top: 4.5em;">
		<jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
		 <jsp:param value="none" name="helpURL"/>
		</jsp:include>
      <div class="container">
         <h2>Create an external course</h2>
         
         <form method="post" action="/adl/LMSCourseAdmin" name="newCourse" accept-charset="utf-8">
            <input type="hidden" name="type" value="<%= ServletRequestTypes.CREATE_NEW_COURSE %>" />
            <div class="form-group">
	            <label for="courseID">Course URI</label>
	            <input type="text" class="form-control" name="courseID" id="courseID" placeholder="http://adlnet.gov/course/making-tea" />
	         </div>
	         <div class="form-group">
               <label for="courseTitle">Course Title</label>
               <input type="text" class="form-control" name="courseTitle" id="courseTitle" placeholder="Making Tea" />
            </div>
            
            <button type="submit" name="submit" class="btn btn-primary">Save</button>
         </form>
         
         
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