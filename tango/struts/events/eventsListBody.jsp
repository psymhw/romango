<%@ page language="java" %>

<%@ page import="db.EventDb" %>
<%@ page import="model.Settings" %>
<%@ page import="events.EventSections" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.apache.struts.util.LabelValueBean" %>

<!--  
<table width="100%" border="0" cellspacing="0" bgcolor="#FAF0E6" 	>
<tr>
  <td width="40%">
    <table width="100%" border="0" cellspacing="2" >
       <tr>
         <td valign="center"><img src="images/calo_cover.jpg" alt="Tango Market" /></td>
         <td valign = "top" align ="left">Don't forget to check out the great music in the<br><a href="/market.do">Tango Market</a></td>
       </tr>
    </table>  
  <td>
    <table width="100%" border="0" cellspacing="2" >
       <tr>
         <td align="right"><img src="images/shoe_1.jpg" alt="Romango Shoes" /></td>
       </tr>
       <tr>  
         <td align="right">Custom fit Handmade shoes from <a href="http://www.romangoshoes.com">Romango Shoes</a></td>
       </tr>
    </table>  
</tr>
</table>
-->
<!--  
<table border="0" width = "100%"><tr>
	  <td><h1>Events</h1></td>
	  <td align="right"><img src="<%=request.getContextPath()%>/images/new.jpg" width="75px"/></td>
	  <td><a href="<%=request.getContextPath()%>/events.do?mode=calendar"><b>CALENDAR VIEW</b></a></td>
	  </tr>
	  </table>
	  -->
<table width="100%" border="0" cellspacing="0" >

  <td><a href="#Special Events">Special Events</a></td>
  <td><a href="#Regular Milongas">Regular Milongas</a></td>
  <td><a href="#Regular Practicas">Regular Practicas</a></td>
  <td><a href="#Classes">Classes</a></td>

<td><a href="<%=request.getContextPath()%>/events.do?mode=calendar">Calendar</a></td>
</tr>
<tr><td colspan='5' height='30px' valign='bottom'><a href='https://www.facebook.com/groups/184260995654/'>Eugene Tango on <img src="<%=request.getContextPath()%>/images/facebook_logo.jpg"  height='20' /></a></td></tr>
<tr >
</table>
<table width="100%" border="0" cellspacing="0" >

<%
String titleColor="#FFCCFF";
EventSections eventSections = (EventSections)request.getAttribute("EventSections");
Settings settings = new Settings();
ArrayList eventTypes = settings.getEventTypes();
Iterator it = eventTypes.iterator();
int count=0;

String sectionTitle=null;
ArrayList section;
Iterator itx=null;
EventDb edb=null;
String pluralSectionTitle="";
LabelValueBean eventBean = null;

while(it.hasNext())
{
   eventBean=(LabelValueBean)it.next();
   count=Integer.parseInt(eventBean.getValue());
   sectionTitle=eventBean.getLabel();
  //sectionTitle=(String)it.next();
  if (!sectionTitle.endsWith("s")) pluralSectionTitle=sectionTitle+"s"; else pluralSectionTitle=sectionTitle;
  if (count>0) 
  {
    %>
    <tr><td>&nbsp;</td><td></td></tr>
     <tr bgcolor="<%=titleColor%>"><td><hr><font size="+1"><a name="<%=pluralSectionTitle%>"><%=pluralSectionTitle%></a></font></td>
     <td align="right" bgcolor="<%=titleColor%>" valign="top"><hr>
      <a href="<%=request.getContextPath()%>/events.do?mode=new&location_id=1&type_id=<%=count%>">add new <%=sectionTitle%></a></right</td></tr>
   <%
    section=eventSections.getSection(count);
    if (section.size()==0)
    out.println("<tr><td colspan=\"2\" align=\"center\">No "+pluralSectionTitle+" found</td></tr>");	
    itx=section.iterator();
    while(itx.hasNext())
    {
      edb=(EventDb)itx.next();
      %>
        <tr>
<%
  if (edb.isPhotoPresent())
  {  
  %>
  <td width="210px" valign="top"><br>
  <a href="<%=request.getContextPath()%>/photo/fullPhoto.jsp?mode=event&id=<%=edb.getId()%>&caption=<%=edb.getCaption()%>">
 <img border="0" src="<%=request.getContextPath()%>/imageReader?mode=event&id=<%=edb.getId()%>" alt="Event Photo" width="200"/><br>
 </a>

 </td>
 <td valign="top">
 <%} else { %>
 <td width="210px" valign="top" >
 </td>
 <td valign="top">
 <%} %>
           <table border="0" width="100%">
           <tr><td>
          <a href="<%=request.getContextPath()%>/events.do?mode=show&id=<%=edb.getId()%>"><b><%=edb.getTitle()%></b></a>
          <br>(by <%=edb.getOwner_username()%>)
          </td></tr>
          <tr><td align="left">
          <table border="0" width="90%"><tr><td><%=edb.getDescription()%></tr></td></table>
          </td></tr><tr>
          <td valign="bottom"><br><hr>
		          <table border="0" width = "100%">
		          <tr><td valign="top">
		          <%
		          if (edb.getVenue_name()!=null) if (!"".equals(edb.getVenue_name())) out.println(edb.getVenue_name());
		        	if (edb.getVenue_addr()!=null) if (!"".equals(edb.getVenue_addr())) out.println("<br>"+edb.getVenue_addr());
		          if (edb.getVenue_csz()!=null) if (!"".equals(edb.getVenue_csz())) out.println("<br>"+edb.getVenue_csz());
		          %>
		          </td>
		          <td align="right"><img src="<%=request.getContextPath()%>/images/map_ico.jpg" />&nbsp;<a href="<%=request.getContextPath()%>/events.do?mode=map&id=<%=edb.getId()%>" >Map</a>
		          </td></tr></table>
		       </td></tr></table>
          
          </td>
      </tr>
      <%
    }
  }
  count++;
}



%> 
</table>