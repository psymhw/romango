<%@ page language="java" %>

<%
String brokerOpens= (String)request.getAttribute("brokerOpens");
String brokerCloses= (String)request.getAttribute("brokerCloses");
%>

<html>
<body>
<h1>Test Connections</h1>
<%
out.println("brokerOpens: "+brokerOpens); 
out.println("<br>brokerCloses: "+brokerCloses); 
%>
 
<br>
<br>
done.
</body>

</html>