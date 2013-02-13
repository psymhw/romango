<%@ page language="java" %>
<%@ page import="db.EventDb" %>
<%@ page import="model.Settings" %>
<%@ page import="events.CalendarData" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.lang.Integer" %>
<%
CalendarData calendarData = (CalendarData)request.getAttribute("CalendarData");
%>
<h1>Events Calendar - <font color="blue"><%=calendarData.getStrMonth()%></font></h1><br>
<script src="<%=request.getContextPath()%>/script/calendar.js"></script>


<table width="100%" cellspacing='0' cellpadding='0' >
<tr>
<td ><a href="<%=request.getContextPath()%>/events.do?mode=calendar&month=<%=calendarData.getPrevMonth()%>"><img src="<%=request.getContextPath()%>/images/previous.gif"/></a></td>
<td><img src="<%=request.getContextPath()%>/images/class_icon.gif"/></td><td> = Classes&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
   <td><img src="<%=request.getContextPath()%>/images/practica_icon.gif"/></td><td> = Practicas&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
   <td><img src="<%=request.getContextPath()%>/images/milonga_icon2.gif"/></td><td> = Milongas&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
   <td><img src="<%=request.getContextPath()%>/images/special_icon2.gif"/></td><td> = Special Events&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td align="right"><a href="<%=request.getContextPath()%>/events.do?mode=calendar&month=<%=calendarData.getNextMonth()%>"><img src="<%=request.getContextPath()%>/images/next.gif"/></a></td>
</tr>
</table>
<script language="JavaScript">
<!--
<%
ArrayList events = calendarData.getEvents();
Iterator it = events.iterator();
while(it.hasNext())
{	
  out.println((String)it.next());
}
%>
ColorBackground="ccffff";
SpecialDay=0;
FontSize=3;
StartDate=<%=calendarData.getFirstMonth()%>;
FirstMonth=<%=calendarData.getFirstMonth()%>;
LastMonth=<%=calendarData.getLastMonth()%>;
Calendar( );
//-->
</script>
<table width="100%" cellspacing='0' cellpadding='0' >
<tr>
<td ><a href="<%=request.getContextPath()%>/events.do?mode=calendar&month=<%=calendarData.getPrevMonth()%>"><img src="<%=request.getContextPath()%>/images/previous.gif"/></a></td>
<td><img src="<%=request.getContextPath()%>/images/class_icon.gif"/></td><td> = Classes&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
   <td><img src="<%=request.getContextPath()%>/images/practica_icon.gif"/></td><td> = Practicas&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
   <td><img src="<%=request.getContextPath()%>/images/milonga_icon2.gif"/></td><td> = Milongas&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
   <td><img src="<%=request.getContextPath()%>/images/special_icon2.gif"/></td><td> = Special Events&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td align="right"><a href="<%=request.getContextPath()%>/events.do?mode=calendar&month=<%=calendarData.getNextMonth()%>"><img src="<%=request.getContextPath()%>/images/next.gif"/></a></td>
</tr>
</table>

<%
ArrayList specialEvents = calendarData.getThisMonthSpecial();
if (specialEvents.size()>0)
{	
	int count=0;
  Iterator itx = specialEvents.iterator();
  EventDb edb=null;
  out.println("<h1>Special Events This Month</h1><br>");
  out.println("<table border=\"1\">");
  out.println("<tr>");
  while(itx.hasNext())
  {	
	  edb=(EventDb)itx.next();
	  if (edb.isPhotoPresent())
	  {  
		   if (count==3) { out.println("</tr><tr>"); count=0; }
		   count++;
		   
	  %>
	  <td valign="top">
	  <img src="<%=request.getContextPath()%>/imageReader?mode=event&id=<%=edb.getId()%>" alt="Event Photo" width="200"/><br>
	  <a href="<%=request.getContextPath()%>/events.do?mode=show&id=<%=edb.getId()%>"><b><%=edb.getTitle()%></b></a>
  	</td>
    <%}
  }
  out.println("</tr></table>");
}
%>



