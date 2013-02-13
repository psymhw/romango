<%@ page language="java" %>
<%@ page import="model.Settings" %>
<%@ page import="db.MediaDb" %>
</script>



<%
  MediaDb mdb = (MediaDb)request.getAttribute("MediaDb");
  if (mdb==null) { out.println("mediaEdit.jsp: mdb is null"); return; } 


%>
<table>
<tr><td><%=mdb.getEmbed()%></td></tr>
<tr><td><b><%=mdb.getTitle()%></b></td></tr>
<tr><td><b>Category: </b><%=mdb.getCategory()%></td></tr>
<tr><td><%=mdb.getDescription()%></td></tr>
</table>
<a href="<%=request.getContextPath()%>/mediaEdit.do?mode=edit&id=<%=mdb.getId()%>">edit</a>