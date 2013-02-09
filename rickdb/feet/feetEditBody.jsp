<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="feet.FeetForm" %>

<%
FeetForm ff = (FeetForm)request.getAttribute("FeetForm");
if (ff==null) { out.println("FeetForm is null");  return; }
pageContext.setAttribute("ff", ff);
%>
<h2>Foot Measurements for <bean:write name="ff" property="customer_name"/></h2>

<html:form action="/feetEdit.do">
<html:hidden name="ff" property="mode" />
<html:hidden name="ff" property="id" />
<html:hidden name="ff" property="cust_id" />
<table border="1">

  <tr>
    <th align="right">Measure Date: </th>
    <td colspan="2"><html:text name="ff" property="measure_date"/></td>
  </tr>
  <tr>
    <th align="right">Shoe Size 1: </th>
    <td colspan="2"><html:text name="ff" property="shoe_size_1"/></td>
  </tr>
   <tr>
    <th align="right">Shoe Size 2: </th>
    <td colspan="2"><html:text name="ff" property="shoe_size_2"/></td>
  </tr>
  
  <tr>
    <th align="right">Notes: </th>
    <td colspan="2"><html:textarea name="ff" property="notes" cols="34" rows="6"/></td>
  </tr>
  
 <tr>
    <th>&nbsp;</th>
    <th align="center" >Left Foot </th>
    <th align="center" >Right Foot </th>
</tr>
  
  <tr>
    <th align="right">length: </th>
    <td><html:text name="ff" property="lf_length" size="6" maxlength="6"/><bean:write name="ff" property="units" /></td>
    <td><html:text name="ff" property="rt_length" size="6" maxlength="6"/><bean:write name="ff" property="units" /></td>
  </tr>
  <tr>
    <th align="right">ball width: </th>
    <td><html:text name="ff" property="lf_bb_width" size="6" maxlength="6"/><bean:write name="ff" property="units" /></td>
    <td><html:text name="ff" property="rt_bb_width" size="6" maxlength="6"/><bean:write name="ff" property="units" /></td>
  </tr>
  <tr>
    <th align="right">heel width: </th>
    <td><html:text name="ff" property="lf_heel_width" size="6" maxlength="6"/><bean:write name="ff" property="units" /></td>
    <td><html:text name="ff" property="rt_heel_width" size="6" maxlength="6"/><bean:write name="ff" property="units" /></td>
  </tr>
  <tr>
    <th align="right">ball girth: </th>
    <td><html:text name="ff" property="lf_bb_girth" size="6" maxlength="6"/><bean:write name="ff" property="units" /></td>
    <td><html:text name="ff" property="rt_bb_girth" size="6" maxlength="6"/><bean:write name="ff" property="units" /></td>
  </tr>
  <tr>
    <th align="right">waist girth: </th>
    <td><html:text name="ff" property="lf_waist_girth" size="6" maxlength="6"/><bean:write name="ff" property="units" /></td>
     <td><html:text name="ff" property="rt_waist_girth" size="6" maxlength="6"/><bean:write name="ff" property="units" /></td>
  </tr>
  <tr>
    <th align="right">instep girth: </th>
    <td><html:text name="ff" property="lf_instep_girth" size="6" maxlength="6"/><bean:write name="ff" property="units" /></td>
    <td><html:text name="ff" property="rt_instep_girth" size="6" maxlength="6"/><bean:write name="ff" property="units" /></td>
  </tr>
  <tr>
    <th align="right">short heel: </th>
    <td><html:text name="ff" property="lf_short_heel" size="6" maxlength="6"/><bean:write name="ff" property="units" /></td>
    <td><html:text name="ff" property="rt_short_heel" size="6" maxlength="6"/><bean:write name="ff" property="units" /></td>
  </tr>
    
    <td colspan="3"><html:submit /></td>
  </tr>
  </table>
  </html:form>