<%@ page language="java" %>
<%
String errorMessage  = (String)request.getAttribute("ErrorMessage");
if (errorMessage==null) errorMessage = "No error message found";
%>
 <h3>Event Access Error</h3>
<br>
<br>
<font color="red">
<%=errorMessage%>
</font>