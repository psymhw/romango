<%@ page language="java" %>
<%
String currentMenuItem = (String)request.getAttribute("CurrentMenuItem");
if (currentMenuItem==null) currentMenuItem="none";
%>

<h1>RickDb</h1>
&nbsp;&nbsp;

<% if (!"home".equals(currentMenuItem))
{ %>
 <a href="home.do">home</a> 
<%
}
else
{
%>
 home
<% } %>

| 

<% if (!"address".equals(currentMenuItem))
{ %>
 <a href="addressList.do">address book</a> 
<%
}
else
{
%>
 address book
<% } %>

<% if (!"transaction master".equals(currentMenuItem))
{ %>
| <a href="transHome.do">transaction master</a> 
<%
}
else
{
%>
| transaction master
<% } %>

<% if (!"tracks".equals(currentMenuItem))
{ %>
| <a href="tracks.do">tracks</a> 
<%
}
else
{
%>
| tracks
<% } %>

<% if (!"feet".equals(currentMenuItem))
{ %>
| <a href="feet.do">feet</a> 
<%
}
else
{
%>
| feet
<% } %>


<% if (!"admin".equals(currentMenuItem))
{ %>
| <a href="admin.do?mode=show">admin</a>
<%
}
else
{
%>
| admin
<% } %> | budget 

<% if (!"mileage".equals(currentMenuItem))
{ %>
| <a href="mileage.do?mode=list">mileage</a>
<%
}
else
{
%>
| mileage
<% } %>

<% if (!"notes".equals(currentMenuItem))
{ %>
| <a href="notes.do?mode=list">notes</a>
<%
}
else
{
%>
| notes
<% } %>

<% if (!"bookmarks".equals(currentMenuItem))
{ %>
| <a href="notes.do?mode=list&filter=bookmarks">bookmarks</a>
<%
}
else
{
%>
| bookmarks
<% } %>

<% if (!"orders".equals(currentMenuItem))
{ %>
| <a href="order.do">orders</a>
<%
}
else
{
%>
| orders
<% } %>

<% if (!"logout".equals(currentMenuItem))
{ %>
| <a href="logout.do">logout</a>
<%
}
else
{
%>
| logout
<% } %>


<br>