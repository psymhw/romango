<%@ page language="java" %>
<%@ page import="db.MemberDb" %>
<%@ page import="model.Settings" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.apache.struts.util.LabelValueBean" %>

<h1> MemberEdit </h1>
<%
  MemberDb mdb = (MemberDb)request.getAttribute("MemberDb");
  if (mdb==null) { out.println("memberEdit.jsp: mdb is null"); return; } 
  
  pageContext.setAttribute("mdb", mdb);
  Settings settings = new Settings();
  ArrayList areas = settings.areas;
  LabelValueBean lvb=null;
%>

<form action="<%=request.getContextPath()%>/admin/members.do" method="post" >


<input type="hidden" name="mode" value="<%=mdb.getMode()%>" />
<input type="hidden" name="id" value="<%=mdb.getId()%>" />
<br><br>
<table border="1">
<tr>
  <td align="right"><b>User Name: </b></td>
  <td><input type="text" name="username" value="<%=mdb.getUsername()%>" /></td>
</tr>
<tr>
  <td align="right"><b>First Name: </b></td>
  <td><input type="text" name="first_name" value="<%=mdb.getFirst_name()%>" /></td>
</tr>
<tr>
  <td align="right"><b>Last Name: </b></td>
  <td><input type="text" name="last_name" value="<%=mdb.getLast_name()%>" /></td>
</tr>
<tr>
  <td align="right"><b>e-mail: </b></td>
  <td><input type="text" name="email" value="<%=mdb.getEmail()%>" /></td>
</tr>
<tr>
  <td align="right"><b>Location: </b></td>
  <td><select name="location_id" value="<%=mdb.getLocation_id()%>">
  <%
    Iterator it = areas.iterator();
   while(it.hasNext())
   {
	   lvb=(LabelValueBean)it.next();
	   %>
	   
	   <option value="<%=lvb.getValue()%>"><%=lvb.getLabel()%></option>
	   <% 
   }
  %>
  </select>
  </td>
</tr>
<tr>
  <td align="right"><b>Password: </b></td>
  <td><input type="text" name="password" value="<%=mdb.getPassword()%>" /></td>
</tr>
<tr>
  <td align="right"><b>Active: </b></td>
  <td><input type="checkbox" name="active" value="1" <%=mdb.getActiveChecked()%> /></td>
</tr>
<tr>
  <td align="right"><b>Admin: </b></td>
  <td><input type="checkbox" name="admin" value="1" <%=mdb.getAdminChecked()%> /></td>
</tr>
<tr>
  <td align="right"><b>Host: </b></td>
  <td><input type="checkbox" name="host" value="1" <%=mdb.getHostChecked()%> /></td>
</tr>
<tr>
  <td align="right"><b>Instructor: </b></td>
  <td><input type="checkbox" name="instructor" value="1" <%=mdb.getInstructorChecked()%> /></td>
</tr>
<tr>
  <td align="right"><b>Description: </b></td>
  <td><textarea name="description" COLS=70 ROWS=15><%=mdb.getDescription()%></textarea></td>
</tr>

</table>
<input type="submit" />
</form> 