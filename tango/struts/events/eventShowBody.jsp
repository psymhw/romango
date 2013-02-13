<%@ page language="java" %>
<%@ page import="db.EventDb" %>
<%@ page import="model.Settings" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.apache.struts.util.LabelValueBean" %>

<%
EventDb edb= (EventDb)request.getAttribute("EventDb");

//System.out.println("eventShowBody.jsp");
if (edb==null) { out.println("eventsShowBody.jsp: EventDb is null"); return; }
Settings settings = new Settings();

Boolean eventEditAllowed = (Boolean)request.getAttribute("EventEditAllowed");
if (eventEditAllowed==null) eventEditAllowed= new Boolean(false);
Boolean editDates = (Boolean)request.getAttribute("EditDates");

if (editDates==null) editDates = new Boolean(true);
%>
<h2>Event</h2>
<br>
<table>
<tr>
  <td align="right"><b>Status: </b></td>
  <td>
  <%=edb.getStatus()%></td>
</tr>
 <%  if (eventEditAllowed.booleanValue())
		{ %>
<tr>
  <td align="right" valign="top"><b>Activation: </b></td>
  <td>
   
	<% 
		if ("not visible".equals(edb.getStatus())) 
		  {%>
		  <form action="<%=request.getContextPath()%>/events.do" method="post">
			<input type="hidden" name="mode" value="activate"/>
			<input type="hidden" name="id" value="<%=edb.getId()%>"/>
			<input type="submit" value="Make Visible"/>
          </form>
			
	  <% 
		}
		if ("visible".equals(edb.getStatus())) 
		  {%>
		   <form action="<%=request.getContextPath()%>/events.do" method="post">
			<input type="hidden" name="mode" value="deactivate"/>
			<input type="hidden" name="id" value="<%=edb.getId()%>"/>
			<input type="submit" value="Make Invisible"/>
          </form>
	           <%
		} 
	  %>
  </td>
</tr>
	<%	 }%>
<tr><td colspan="2"><hr></td></tr>
<tr>
<td align="right"><b>Event Title: </b></td>
<td><%=edb.getTitle()%> </td>
</tr>

<tr>
<td align="right"><b>Short Title: </b></td>
<td><%=edb.getShort_title()%> </td>
</tr>

<tr>
<td align="right"><b>Owner: </b></td>
<td><%=edb.getOwner()%> </td>
</tr>
<tr>
  <td align="right"><b>Event Type: </b></td>
  <td><%=settings.eventTypesHash.get(""+edb.getType_id())%></td>
</tr>
<tr>
  <td align="right"><b>Event Region: </b></td>
  <td><%=edb.getLocation()%></td>
</tr>
<tr>
  <td align="right" valign="top"><b>Day of the Week: </b></td>
  <td>
  <%
  if (edb.getAll_x()==1) out.println("All ");
  if (edb.getFirst()==1) out.println("1st ");
  if (edb.getSecond()==1) out.println("2nd ");
  if (edb.getThird()==1) out.println("3rd ");
  if (edb.getFourth()==1) out.println("4th ");
  if (edb.getFifth()==1) out.println("5th ");
  %>
  <%=edb.getFullDay_of_week()%>
  </td>
</tr>
<% if (editDates.booleanValue()) { %>
<tr>
  <td align="right" valign="top"><b>Start Date: </b></td>
  <td><%=edb.getStart_date_formatted()%></td>
</tr>
<tr>
  <td align="right" valign="top"><b>Expiration Date: </b></td>
  <td><%=edb.getExpire_date_formatted()%></td>
</tr>
<%}%>
<% if (edb.getType_id()==Settings.CLASS) { 
String dropIn="No"; 
if (edb.getDrop_in()==1) dropIn="Yes";
%>
<tr>
  <td align="right"><b>Drop In: </b></td>
  <td><%=dropIn%></td>
</tr>
<%}%>
<tr>
  <td align="right"><b>Venue Name: </b></td>
  <td><%=edb.getVenue_name()%></td>
</tr>
<tr>
  <td align="right"><b>Venue Stree Addr: </b></td>
  <td><%=edb.getVenue_addr()%></td>
</tr>
<tr>
  <td align="right"><b>Venue City, State Zip: </b></td>
  <td><%=edb.getVenue_csz()%></td>
</tr>

</table>
<% if (eventEditAllowed.booleanValue())
 { %>
 <a href="<%=request.getContextPath()%>/events.do?mode=edit&id=<%=edb.getId()%>">edit this event</a>
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=request.getContextPath()%>/events.do?mode=delete&id=<%=edb.getId()%>">delete this event (permanent!)</a>
<%}%>
  <br><br>
  <table width="100%" border="1">
  <tr><td valign="top"  width="210px">
  <%
  if (edb.isPhotoPresent())
  {  
  %>
  <a href="<%=request.getContextPath()%>/photo/fullPhoto.jsp?mode=event&id=<%=edb.getId()%>&caption=<%=edb.getCaption()%>">
 <img src="<%=request.getContextPath()%>/imageReader?mode=event&id=<%=edb.getId()%>" alt="Event Photo" width="200"/><br>
 </a>
 <br>
 <%} else { %>
 No Photo
 <%} %>
 </td>
 <td valign="top">
 
 
 <table border="0" width="100%">
           <tr><td>
          <b><%=edb.getTitle()%></b>
          <br>(by <%=edb.getOwner_username()%>)
          </td></tr>
          <tr><td>
          <br><%=edb.getDescription()%>
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
 </table>
 <%
 if (eventEditAllowed.booleanValue())
 { %>
 <br>
 
    <h1>Add or replace photo</h1>

    <form action="<%=request.getContextPath()%>/imageUpload" method="post" enctype="multipart/form-data">
    <input type="hidden" name="mode" value="event" />
    <input type="hidden" name="id" value="<%=edb.getId()%>" />
      <p><br/>
      <table>
      <tr>
        <td align="right"><b>Choose a photo: </b></td>
       <td><input name="myFile" type="file"/>  (JPEG only)</td>
       </tr>
       
       </table>
      </p>

      <p>
        <input type="submit" value="upload photo"/>
       
      </p>
    </form>
<%}%>  