<%@ page language="java" %>
<%@ page import="db.MemberDb" %>
<%@ page import="model.Settings" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>

<h1>Eugene Tango Instructors</h1>
<br>
<table border="1" width="850px">
			
<%
MemberDb ud = (MemberDb)session.getAttribute("UserData");
if (ud==null) ud= new MemberDb();
int uid=0;
if (ud!=null)	uid=ud.getId();
ArrayList instructors = (ArrayList)request.getAttribute("Instructors");
if (instructors==null) { out.println("no instructors found"); return; }
Iterator it = instructors.iterator();
MemberDb mdb=null;
while(it.hasNext())
{
  mdb = (MemberDb)it.next();%>
  <tr> 
  <td width="206px" align="center" valign="top"><a href="<%=request.getContextPath()%>/photo/fullPhoto.jsp?mode=member&id=<%=mdb.getId()%>&caption=<%=mdb.getCaption()%>">
  <img src="<%=request.getContextPath()%>/imageReader?mode=member&id=<%=mdb.getId()%>" alt="Instructor Photo" width="200"/>
  </a><br>
  <center><b><%=mdb.getCaption()%></b></center>
  <% 
 
  if (mdb.getId()==uid||ud.getAdmin()==1) {%>
	  <a href="<%=request.getContextPath()%>/instructors.do?mode=edit&id=<%=mdb.getId()%>">edit</a>
	  <%}%>
  </td>
  <td valign="top"><center><h3><%=mdb.getFirst_name() %> <%=mdb.getLast_name() %></h3></center>
  <%=mdb.getDescription() %></td>
  </tr>
  <%} %>
  </table>
			