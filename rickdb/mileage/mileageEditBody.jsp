<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="mileage.MileageForm" %>
<%@ page import="trans.Lookups" %>

<%
Lookups lookups = new Lookups();
pageContext.setAttribute("tracks", lookups.tracks);

MileageForm mf = (MileageForm)request.getAttribute("MileageForm");
if (mf==null)
{
  out.println("MileageForm is null");
  return;
}
pageContext.setAttribute("mf", mf);
%>
<html:form action="/mileage.do">
<html:hidden name="mf" property="mode" />
<table>
    <tr>
      <td align="right"><b>Date: </b></td>
      <td><html:text name="mf" property="mileage_date" size="10" maxlength="10"/></td>
    </tr>
 
    <tr>
      <td align="right"><b>Description: </b></td>
      <td><html:text name="mf" property="description" size="45" maxlength="45"/></td>
    </tr>
    <tr>
      <td align="right"><b>Track: </b> </td>
      <td><html:select name="mf" property="track">
         <html:options collection="tracks" property="value" labelProperty="label"/> 
         </html:select>
       </td>
    </tr>
    <tr>
      <td align="right"><b>Start Odometer: </b></td>
      <td><html:text name="mf" property="start_mile" size="7" maxlength="7"/></td>
    </tr>

    <tr>
      <td align="right"><b>Miles: </b></td>
      <td><html:text name="mf" property="miles" size="5" maxlength="5"/></td>
    </tr>
    <tr>
      <td align="right"><b>End Odometer: </b></td>
      <td><html:text name="mf" property="end_mile" size="7" maxlength="7"/></td>
    </tr>
    <tr>
      <td colspan="2"><html:submit /></td>
    </tr>
</table>
</html:form>
<br>
<br>
<%
if ("update".equals(mf.getMode()))
{
  out.println("<a href='mileage.do?mode=delete&id="+mf.getId()+"'>delete</a>");
}

%>
