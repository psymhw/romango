<%
session.removeAttribute("user");
session.removeAttribute("UserData");
%>
<h1>Sign In</h1><br>
<form action="<%=request.getContextPath()%>/login.do" method="post" >
<table border="0">
<tr>
<td><b>User Name</b></td>
<td><input type="text" name="username" /> </td>
</tr>
<tr>
<td><b>Password</b></td>
<td><input type="password" name="password" /> </td>
</tr>
</table>
<input type="submit" value="Sign In"/>
</form>