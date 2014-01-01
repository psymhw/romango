<%@ page import="db.MemberDb" %>
<%
             MemberDb userData = (MemberDb)session.getAttribute("UserData");
             String user = (String)session.getAttribute("user");
%>
<ul id="p7PMnav">
  <li><a href="<%=request.getContextPath()%>/inventory.do">Inventory</a></li>
  
  <li><a href="<%=request.getContextPath()%>/waddup.do">QC</a></li>
  <li><a href="<%=request.getContextPath()%>/djs.do">MSDS</a></li>
	
	<li><a href="<%=request.getContextPath()%>/music.do">CofA</a></li>
	
<!--	<li><a href="<%=request.getContextPath()%>/gallery/linksMain.jsp">Links</a></li> -->
	
	<li><a href="<%=request.getContextPath()%>/market.do">Labels</a></li> 
	<li><a href="<%=request.getContextPath()%>/media.do">POs</a></li> 
	<li><a href="<%=request.getContextPath()%>/links.do">Invoices</a></li> 
	
 
  <li><a href="<%=request.getContextPath()%>/struts/members/aboutMain.jsp">Members</a>
	      <ul>
	        <li><a href="<%=request.getContextPath()%>/struts/members/aboutMain.jsp">About Membership</a></li>
					<% 
             if (userData==null)
             { %>
           <li><a href="<%=request.getContextPath()%>/struts/login/loginMain.jsp">Sign in</a></li>
              <%
              }
              else
              {
              %>
              <li><a href="<%=request.getContextPath()%>/login.do">Sign out <%=userData.getUsername() %></a></li>
              <li><a href="<%=request.getContextPath()%>/events.do?mode=myevents">My Events </a></li>
			 <%
              if (userData.getAdmin()==1)
              { %>
            	  <li><a href="<%=request.getContextPath()%>/struts/admin/adminMain.jsp">Admin</a></li>
              <% }
              } %>
               
				    </ul>
				    
	</li>
	
  
 
  <!--[if lte IE 6]><style>#p7PMnav a{height:1em;}#p7PMnav li{height:1em;}#p7PMnav ul li{float:left;clear:both;width:100%}</style><![endif]-->
  <!--[if IE 6]><style>#p7PMnav ul li{clear:none;}</style><![endif]-->
  <!--[if IE 7]><style>#p7PMnav a{zoom:100%;}#p7PMnav ul li{float:left;clear:both;width:100%;}</style><![endif]-->
</ul>