<%@ page language="java" %>
<%@ page import="db.EventDb" %>
<%@ page import="model.Settings" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.lang.Integer" %>
<%@ page import="org.apache.struts.util.LabelValueBean" %>

<%
EventDb edb= (EventDb)request.getAttribute("EventDb");
Boolean editDates = (Boolean)request.getAttribute("EditDates");

if (editDates==null) editDates = new Boolean(true);
//System.out.println("eventsEditBody.jsp - editDates: "+editDates.booleanValue());
if (edb==null) { out.println("eventsEditBody.jsp: EventDb is null"); return; }
String submitText="Update Event";
%>
<script type="text/javascript">
function adjustBoxes()
{
	if(document.eventForm.all_x.checked == true)
	{
		document.eventForm.first.checked=false;
		document.eventForm.second.checked=false;
		document.eventForm.third.checked=false;
		document.eventForm.fourth.checked=false;
		document.eventForm.fifth.checked=false;
	}
		  
	
}
function adjustBoxes2()
{
	if(document.eventForm.first.checked == true) { document.eventForm.all_x.checked=false; }
	if(document.eventForm.second.checked == true) { document.eventForm.all_x.checked=false; }
	if(document.eventForm.third.checked == true) { document.eventForm.all_x.checked=false; }
	if(document.eventForm.fourth.checked == true) { document.eventForm.all_x.checked=false; }
	if(document.eventForm.fifth.checked == true) { document.eventForm.all_x.checked=false; }
}

function detailSwitch()
{
	if (document.eventForm.day_of_week.value=="na")
	{	
		document.eventForm.all_x.checked=false;
	  document.eventForm.all_x.disabled=true;

	  document.eventForm.first.checked=false;
	  document.eventForm.first.disabled=true;

	  document.eventForm.second.checked=false;
	  document.eventForm.second.disabled=true;

	  document.eventForm.third.checked=false;
	  document.eventForm.third.disabled=true;

	  document.eventForm.fourth.checked=false;
	  document.eventForm.fourth.disabled=true;

	  document.eventForm.fifth.checked=false;
	  document.eventForm.fifth.disabled=true;
	}
	else 
	{
		document.eventForm.all_x.checked=true;
		document.eventForm.all_x.disabled=false;

		document.eventForm.first.checked=false;
		  document.eventForm.first.disabled=false;

		  document.eventForm.second.checked=false;
		  document.eventForm.second.disabled=false;

		  document.eventForm.third.checked=false;
		  document.eventForm.third.disabled=false;

		  document.eventForm.fourth.checked=false;
		  document.eventForm.fourth.disabled=false;

		  document.eventForm.fifth.checked=false;
		  document.eventForm.fifth.disabled=false;
	}
}
</script>
<% 
if ("insert".equals(edb.getMode())) 
{
	%>
	<script type="text/javascript">
	detailSwitch();
	</script>
	<% 
	submitText="Add Event";
	out.println("<h2>New Event</h2>"); 
}
else out.println("<h2>Edit Event</h2>");
Settings settings = new Settings();
ArrayList areas = settings.areas;
ArrayList eventTypes = settings.eventTypes;
LabelValueBean lvb=null;
Iterator it = null;
%>
 <%@ include file="/script/datePicker.js" %>
 
 </script>
<!-- TinyMCE -->
<script type="text/javascript" src="<%=request.getContextPath()%>/jscripts/tiny_mce/tiny_mce.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/jscripts/tiny_mce/basic_config.js"></script>
<script type="text/javascript">
	tinyMCE.init({
		mode : "textareas",
			theme : "advanced"
	});
</script>
<!-- /TinyMCE -->


<br><br>
<form name="eventForm" action="<%=request.getContextPath()%>/events.do" method="post">
<input type="hidden" name="mode" value="<%=edb.getMode()%>"/>
<input type="hidden" name="member_id" value="<%=edb.getMember_id()%>"/>
<input type="hidden" name="id" value="<%=edb.getId()%>"/>
<input type="hidden" name="active" value="<%=edb.getActive()%>"/>
<input type="hidden" name="type_id" value="<%=edb.getType_id()%>"/>
<input type="hidden" name="location_id" value="<%=edb.getLocation_id()%>"/>

<table>
<tr>
  <td align="right"><b>Event Type: </b></td>
  <td><%=settings.eventTypesHash.get(""+edb.getType_id())%> -
  <%=settings.areasHash.get(""+edb.getLocation_id())%>
  </td>
</tr>

<tr  bgcolor="#FFEFD5">
  <td align="right" valign="top"><b>Day: </b></td>
  
  <%
  String all_checked="", first_checked="", second_checked="", third_checked="", fourth_checked="", fifth_checked="";
  if (edb.getAll_x()==1) all_checked="checked";
  if (edb.getFirst()==1) first_checked="checked";
  if (edb.getSecond()==1) second_checked="checked";
  if (edb.getThird()==1) third_checked="checked";
  if (edb.getFourth()==1) fourth_checked="checked";
  if (edb.getFifth()==1) fifth_checked="checked";
  
  String naSel="", monSel="", tueSel="", wedSel="", thuSel="", friSel="", satSel="", sunSel="";
  String disabled="";
	if ("na".equals(edb.getDay_of_week()))  { naSel="selected"; disabled="disabled"; }
	if ("mon".equals(edb.getDay_of_week())) monSel="selected";
	if ("tue".equals(edb.getDay_of_week())) tueSel="selected";
	if ("wed".equals(edb.getDay_of_week())) wedSel="selected";
	if ("thu".equals(edb.getDay_of_week())) thuSel="selected";
	if ("fri".equals(edb.getDay_of_week())) friSel="selected";
	if ("sat".equals(edb.getDay_of_week())) satSel="selected";
	if ("sun".equals(edb.getDay_of_week())) sunSel="selected";
	
	String drop_in_checked="";
	if (1==edb.getDrop_in()) drop_in_checked="checked";
	
  %>
  
  <td><select name="day_of_week" value="<%=edb.getDay_of_week()%>" onChange="detailSwitch()">
	      <option <%=naSel%> value="na">not applicable</option>
	      <option <%=monSel%> value="mon">Mondays</option>
	      <option <%=tueSel%> value="tue">Tuesdays</option>
	      <option <%=wedSel%> value="wed">Wednesdays</option>
	      <option <%=thuSel%> value="thu">Thursdays</option>
	      <option <%=friSel%> value="fri">Fridays</option>
	      <option <%=satSel%> value="sat">Saturdays</option>
	      <option <%=sunSel%> value="sun">Sundays</option>
      </select>
  </td></tr>
  <tr bgcolor="#FFEFD5">
  <td align="right"><b>week in month: </b></td>
  <td>
    &nbsp;&nbsp;ALL <input type="checkbox" name="all_x" value="1" <%=all_checked%> <%=disabled%> onClick="adjustBoxes()"/>&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp; 1st <input type="checkbox" name="first" value="1" <%=first_checked%> <%=disabled%> onClick="adjustBoxes2()"/>&nbsp;
    2nd <input type="checkbox" name="second" value="1" <%=second_checked%> <%=disabled%> onClick="adjustBoxes2()"/>&nbsp;
    3rd <input type="checkbox" name="third" value="1" <%=third_checked%> <%=disabled%> onClick="adjustBoxes2()"/>&nbsp;
    4th <input type="checkbox" name="fourth" value="1" <%=fourth_checked%> <%=disabled%> onClick="adjustBoxes2()"/>&nbsp;
    5th <input type="checkbox" name="fifth" value="1" <%=fifth_checked%> <%=disabled%> onClick="adjustBoxes2()"/>&nbsp;
    </td>
    </tr>
  
  
  
</tr>
<% 
if (editDates.booleanValue()==true)
{
%>
<tr>
  <td align="right" valign="top"><b>Start Date: </b></td>
  <td><input type="text" name="start_date" size="10" value="<%=edb.getStart_date_formatted() %>"  />
  <input type=button value="select" onclick="displayDatePicker('start_date');">
  </td>
</tr>

<tr>
  <td align="right" valign="top"><b>Expiration Date: </b></td>
  <td><input type="text" name="expire_date" size="10" value="<%=edb.getExpire_date_formatted() %>" />
  <input type=button value="select" onclick="displayDatePicker('expire_date');">
  </td>
</tr>

<%}%>

<% if (edb.getType_id()==Settings.CLASS) { %>
<tr>
  <td align="right" valign="top"><b>Drop In?: </b></td>
  <td><input type="checkbox" name="drop_in" value="1" <%=drop_in_checked%> "/>
  </td>
</tr>
<%}%>
<tr>
  <td align="right" valign="top"><b>Event Title: </b></td>
  <td><input type="text" name="title" size="60" value="<%=edb.getTitle() %>"/></td>
</tr>
<tr>
  <td align="right" valign="top"><b>Short Title: </b></td>
  <td><input type="text" name="short_title" size="45" value="<%=edb.getShort_title() %>" /> (calender view)</td>
</tr>
<tr>
  <td align="right" valign="top"><br><b>Description: </b></td>
  <td><br><textarea name="description" COLS=46 ROWS=15><%=edb.getDescription()%></textarea></td>
</tr>

<tr>
<td align="right" valign="top"><b>Venue Name: </b></td>
<td><input type="text" name="venue_name" size="60" value="<%=edb.getVenue_name() %>"/></td>
</tr>
<tr>
<td align="right" valign="top"><b>Venue Street Addr: </b></td>
<td><input type="text" name="venue_addr" size="60" value="<%=edb.getVenue_addr() %>"/></td>
</tr>
<tr>
<td align="right" valign="top"><b>City, State Zip: </b></td>
<td><input type="text" name="venue_csz" size="60" value="<%=edb.getVenue_csz() %>"/></td>
</tr>

 
</table>

<input type="submit" value="<%=submitText %>" />
</form>


