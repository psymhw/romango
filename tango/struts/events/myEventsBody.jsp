<%@ page language="java" %>
<%@ page import="db.EventDb" %>
<%@ page import="model.Settings" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<h1>My Events</h1><br>
<%
  ArrayList myEvents = (ArrayList)request.getAttribute("MyEvents");
  if (myEvents.size()==0) 
  {
    out.println("You don't appear to be the owner of any events"); return;
  }
%>
<table border="0">
	<tr>
	<td><b>Owner</b></td>
	<td><b>Type</b></td>
        <td><b>Event</b></td>
		<td><b>Status</b></td>
    </tr>
		
<%
  EventDb edb=null;
  Iterator it = myEvents.iterator();
  while(it.hasNext())
  {
	edb=(EventDb)it.next();
	%>
    <tr>
    <td><%=edb.getOwner_username() %></td>
    <td><%=Settings.eventTypesHash.get(""+edb.getType_id())%></td>
        <td><a href="<%=request.getContextPath()%>/events.do?mode=show&id=<%=edb.getId()%>"><%=edb.getTitle()%></a></td>
		<td><%=edb.getStatus()%></td>
    </tr>
		
	<%
  }
%>
</table>
<br><br>
