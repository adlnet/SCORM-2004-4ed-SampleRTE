<%@page import = "org.adl.samplerte.server.ServletRequestTypes, org.adl.samplerte.server.CourseData, org.adl.samplerte.server.ItemData, java.util.List" %>
<%
   CourseData coursedata = (CourseData)request.getAttribute("coursedata");
   String courseid = request.getParameter("courseID");
   String coursetitle = request.getParameter("courseTitle");
   List<ItemData> items = null;
   if (coursedata != null) {
      courseid = coursedata.mCourseID;
      coursetitle = coursedata.mCourseTitle;
      items = coursedata.items;
   }
%>
<!doctype html>
<html lang="en">
<head>
   <title>display course</title>
   <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
   <script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
   <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>
<body style="margin-top: 4.5em;">
      <jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
         <jsp:param value="none" name="helpURL"/>
      </jsp:include>
      <div class="page-header">
         <h1>Course <%= coursetitle %> <small><%= courseid %></small></h1>
      </div>
   <div class="container">
      <div class="col-md-12" style="margin-bottom:1em; padding-bottom:1em;">
         <div class="row">
            <div class="col-md-4"><strong>completion: </strong><%= coursedata.mCompleted %></div>
            <div class="col-md-4"><strong>success: </strong><%= coursedata.mSatisfied %></div>
         </div>
         <div class="row">
            <div class="col-md-4"><strong>prog measure: </strong><%= coursedata.mProgMeasure %></div>
            <div class="col-md-4"><strong>measure: </strong><%= coursedata.mMeasure %></div>
         </div>
      </div>
      <% for (ItemData item : items)
      {
      %>
         <div class="col-md-12" style="margin-bottom:1em; padding-bottom:1em; border-bottom: 1px solid gray;">
            <div class="row">
               <div class="col-md-12"><h3><%= item.itemTitle %> <small><%= item.itemID %></small></h3></div>
            </div>
               <% if (item.itemLaunch != null && ! "".equals(item.itemLaunch)) {%>
            <div class="row">
               <div class="col-md-12"><a href="<%= item.itemLaunch %>"><%= item.itemLaunch %></a></div>
            </div>
               <% } %>
            <div class="row">
               <div class="col-md-4"><strong>completion:</strong> <%= item.completion %></div>
               <div class="col-md-4"><strong>success:</strong> <%= item.success%></div>
            </div>
            <div class="row">
               <div class="col-md-4"><strong>scaled:</strong> <%= item.scaled%></div>
               <div class="col-md-4"><strong>raw:</strong> <%= item.raw%></div>
            </div>
            <div class="row">
               <div class="col-md-4"><strong>min:</strong> <%= item.min%></div>
               <div class="col-md-4"><strong>max:</strong> <%= item.max %></div>
            </div>
            <div class="row">
               <div class="col-md-4"><strong>response:</strong> <%= item.response %></div>
               <div class="col-md-4"><strong>duration:</strong> <%= item.duration %></div>
            </div>
         </div>
      <%
      }
      %>
   </div>
</body>
</html>