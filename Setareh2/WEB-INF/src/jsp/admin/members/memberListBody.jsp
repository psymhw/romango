<h2>Member List</h2><br>
<%@ page language="java" %>
<%@ page import="db.MemberDb" %>
<%@ page import="model.Settings" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%
ArrayList members = (ArrayList)request.getAttribute("MemberList");
if (members==null) { out.println("Members is null"); return; }
Iterator it = members.iterator();
MemberDb mdb=null;
%>
<a href="<%=request.getContextPath()%>/admin/members.do?mode=newMember">new</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

<br><br>
<table border="0">
<tr>
<td><b>ID</b></td>
<td><b>User</b></td>
<td><b>Full name</b></td>
</tr>
<%
//System.out.println("MemberListBody.jsp");
while(it.hasNext())
{
	mdb=(MemberDb)it.next();
	%>
	<tr>
	<td><%out.println("<a href=\""+request.getContextPath()+"/admin/members.do?mode=show&id="+mdb.getId()+"\">"+mdb.getId()+"</a>");%></td>
	<td><%=mdb.getUsername() %></td>
	<td><%= mdb.getFirst_name()+" "+mdb.getLast_name()%></td>
	<td><%=mdb.getPassword() %></td>
	<td><%=mdb.getEmail() %></td>
	<%if (mdb.getActive()==1)
	{%>
		
	
	<td>ACTIVE</td>
	<% } else { %>

	<td><font color='red'>INACTIVE</font></td>
	<%} %>
	
</tr>
<%
}%>
</table>
<%
//out.println("hellos");
//ArrayList membersx = (ArrayList)request.getAttribute("MemberList");
Iterator itx = members.iterator();

while(itx.hasNext())
{
	mdb=(MemberDb)itx.next();
	out.println(mdb.getEmail()+",");
}
%>