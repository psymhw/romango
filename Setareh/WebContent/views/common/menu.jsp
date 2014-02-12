<%@taglib uri="/struts-tags" prefix="s"%>

<a href="<%=request.getContextPath()%>/login/logOut"/>Logout</a><br>
<a href="<%=request.getContextPath()%>/home/home.action"/>Home</a><br>
<a href="<%=request.getContextPath()%>/users/UserAction.action"/>Users</a><br>
<a href="<s:url action="friendsTest"/>" >Friends</a><br>
<a href="<s:url action="officeTest"/>" >The Office</a><br>
