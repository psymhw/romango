<%
String mode= (String)request.getParameter("mode");
String id = (String)request.getParameter("id");
String caption = (String)request.getParameter("caption");

%>

<img src="<%=request.getContextPath()%>/imageReader?mode=<%=mode%>&id=<%=id%>" alt="member Photo" /><br>

 <b><%=caption %></b>