<%@ page language="java" %>
<%@ page import="model.Settings" %>
<%@ page import="db.MediaDb" %>



<%
  MediaDb mdb = (MediaDb)request.getAttribute("MediaDb");
  if (mdb==null) { out.println("mediaEdit.jsp: mdb is null"); return; } 
%>
  <h1>Media <%=mdb.getMode() %></h1>
  
  <form action="<%=request.getContextPath()%>/mediaEdit.do" method="post" >


<input type="hidden" name="mode" value="<%=mdb.getMode()%>" />
<input type="hidden" name="id" value="<%=mdb.getId()%>" />
<input type="hidden" name="member_id" value="<%=mdb.getMember_id()%>" />


<br><br>
<table border="1">

<tr>
  <td align="right"><b>Title: </b></td>
  <td><input type="text" name="title" value="<%=mdb.getTitle()%>" size="100" /></td>
</tr>
<tr>
  <td align="right"><b>Category: </b></td>
  <td><input type="text" name="category" value="<%=mdb.getCategory()%>"  size="100"/></td>
</tr>
<tr>
  <td align="right"><b>Description: </b></td>
  <td><textarea  name="description" COLS=70 ROWS=5><%=mdb.getDescription()%></textarea></td>
</tr>
<tr>
  <td align="right"><b>Embed: </b></td>
  <td><textarea  name="embed" COLS=70 ROWS=15><%=mdb.getEmbed()%></textarea></td>
</tr>
</table>
<input type="submit" />
</form>