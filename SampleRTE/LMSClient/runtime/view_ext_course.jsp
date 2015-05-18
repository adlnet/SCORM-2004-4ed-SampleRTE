<%@page import = "org.adl.samplerte.server.ServletRequestTypes, org.adl.samplerte.server.CourseData, org.adl.samplerte.server.ItemData, java.util.List" %>
<%
   String userID = (String)session.getAttribute( "USERID" );
	String isUserAdmin = (String)session.getAttribute( "RTEADMIN" );
	
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
   <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/highlight.js/8.5/styles/default.min.css">
   <script src="//cdnjs.cloudflare.com/ajax/libs/highlight.js/8.5/highlight.min.js"></script>
   <script>hljs.initHighlightingOnLoad();</script>
</head>
<body style="margin-top: 4.5em;">
      <jsp:include page="../runtime/LMSNavigation.jsp" flush="true">
         <jsp:param value="none" name="helpURL"/>
      </jsp:include>
      <div class="page-header">
         <h1>Course <%= coursetitle %> <small><%= courseid %></small></h1>
         <div class="row">
            <div class="col-md-3">
		         <form class="form-inline" method="post" action="/adl/LMSCourseAdmin" name="updateCourseStatus" id="updateCourseStatus" accept-charset="utf-8">
		           <input type="hidden" name="type" value="<%= ServletRequestTypes.UPDATE_EXT_COURSE_STATUS %>" />
		           <input type="hidden" name="userID" value="<%= userID %>" />
		           <input type="hidden" name="courseID" value="<%= courseid %>" />
		           
		           <button type="submit" name="submit" class="btn btn-primary btn-xs">Update Status</button>
		        <span class="text-primary" id="status_update_text" style="display: none;">updating...</span>
		        </form>
	        </div>
	        <% if ((! (isUserAdmin == null)) && ( isUserAdmin.equals("true"))) { %>
	        <div class="col-md-2">
		        <form method="post" action="/adl/LMSCourseAdmin" name="editCourse" accept-charset="utf-8">
		            <input type="hidden" name="type" value="<%= ServletRequestTypes.UPDATE_EXT_COURSE %>" />
		            <input type="hidden" name="courseID" id="courseID" value="<%= courseid %>" />
		           <input type="hidden" name="courseTitle" value="<%= coursetitle %>" />
		           
		           <button type="submit" name="submit" class="btn btn-default btn-xs">Edit Course</button>
		        </form>
	        </div>
	        <% } %>
        </div>
      </div>
   <div class="container">
      <div class="col-md-12" style="margin-bottom:1em; padding-bottom:1em;">
         <div class="row">
            <div class="col-md-4"><strong>completion: </strong><span id="course_completion"><%= coursedata.mCompleted %></span></div>
            <div class="col-md-4"><strong>success: </strong><span id="course_success"><%= coursedata.mSatisfied %></span></div>
         </div>
         <div class="row">
            <div class="col-md-4"><strong>prog measure: </strong><span id="course_progmeasure"><%= coursedata.mProgMeasure %></span></div>
            <div class="col-md-4"><strong>measure: </strong><span id="course_measure"><%= coursedata.mMeasure %></span></div>
         </div>
      </div>
      <% for (ItemData item : items)
      {
      %>
         <div class="col-md-12" style="margin-bottom:1em; padding-bottom:1em; border-bottom: 1px solid gray;">
            <div class="row">
               <div class="col-md-12"><h3 id="title_<%= item.activityID %>"><%= item.itemTitle %> <small id="itemid_<%= item.activityID %>"><%= item.itemID %></small></h3></div>
            </div>
               <% if (item.itemLaunch != null && ! "".equals(item.itemLaunch)) {%>
            <div class="row">
               <div class="col-md-12"><a target="_blank" href="<%= item.itemLaunch %>" id="launch_<%= item.activityID %>"><%= item.itemLaunch %></a></div>
            </div>
               <% } %>
            <div class="row">
               <div class="col-md-4"><strong>completion:</strong> <span id="completion_<%= item.activityID %>"><%= item.completion %></span></div>
               <div class="col-md-4"><strong>success:</strong> <span id="success_<%= item.activityID %>"><%= item.success%></span></div>
            </div>
            <div class="row">
               <div class="col-md-4"><strong>scaled:</strong> <span id="scaled_<%= item.activityID %>"><%= item.scaled%></span></div>
               <div class="col-md-4"><strong>raw:</strong> <span id="raw_<%= item.activityID %>"><%= item.raw%></span></div>
            </div>
            <div class="row">
               <div class="col-md-4"><strong>min:</strong> <span id="min_<%= item.activityID %>"><%= item.min%></span></div>
               <div class="col-md-4"><strong>max:</strong> <span id="max_<%= item.activityID %>"><%= item.max %></span></div>
            </div>
            <div class="row">
               <div class="col-md-4"><strong>response:</strong> <span id="response_<%= item.activityID %>"><%= item.response %></span></div>
               <div class="col-md-4"><strong>duration:</strong> <span id="duration_<%= item.activityID %>"><%= item.duration %></span></div>
            </div>
            <div class="row">
               <div class="col-md-12"><strong><a href="#" class="stmt_link">statement id:</a></strong> <span id="refStmtID_<%= item.activityID %>"><%= item.refStmtID %></span></div>
            </div>
            <div class="row" style="display:none">
               <div class="col-md-12">
               <pre><code class="js" id="stmt_<%= item.activityID  %>"><%= item.statement %></code></pre>
               </div>
            </div>
         </div>
      <%
      }
      %>
   </div>
   <script>
      $('code.js').each(function () {
    	  $(this).text(JSON.stringify(JSON.parse($(this).text()), null, 4));
      });
      $('a.stmt_link').on('click', function(e) {
    	  e.preventDefault();
    	  var block = $(this).parent().parent().parent().next('.row');
    	  if (block && block.length > 0)
    		  hljs.highlightBlock(block[0]);
    	  block.toggle(); 
      });
      $("#updateCourseStatus").submit(function(e) {
    	   $('#status_update_text').show();
    	   e.preventDefault();
	      $.post("/adl/LMSCourseAdmin", $('#updateCourseStatus').serialize(), function(data, stat, xhr){
	    	  console.log(data);
	    	  updateStatus(data);
	      }).always(function() { $('#status_update_text').hide("slow"); });
	      return false;
      });
      
      function updateStatus(data) {
    	   var coursedata = data.coursedata;
    	   // do course update
    	   $('#course_completion').text(coursedata.mCompleted);
    	   $('#course_success').text(coursedata.mSatisfied);
    	   $('#course_progmeasure').text(coursedata.mProgMeasure);
    	   $('#course_measure').text(coursedata.mMeasure);
    	   for (var idx in coursedata.items) {
    		   var item = coursedata.items[idx];
    		   $('#completion_'+item.activityID).text(item.completion);
    		   $('#success_'+item.activityID).text(item.success);
    		   $('#scaled_'+item.activityID).text(item.scaled);
    		   $('#raw_'+item.activityID).text(item.raw);
    		   $('#min_'+item.activityID).text(item.min);
    		   $('#max_'+item.activityID).text(item.max);
    		   $('#response_'+item.activityID).text(item.response);
    		   $('#duration_'+item.activityID).text(item.duration);
    		   $('#refStmtID_'+item.activityID).text(item.refStmtID);
    		   if (item.statement)
    			   $('#stmt_'+item.activityID).text(JSON.stringify(JSON.parse(item.statement), null, 4));
    	   }
      }
   </script>
   
</body>
</html>