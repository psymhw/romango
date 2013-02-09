<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<%@ page import="address.AddressForm" %>

<%
AddressForm af = (AddressForm)request.getAttribute("AddressForm");
if (af==null)
{
  out.println("AddressForm is null");
  return;
}
pageContext.setAttribute("af", af);
%>
<html:form action="/addressEdit.do">
<html:hidden name="af" property="mode" />
<html:hidden name="af" property="id" />


<table border="1">
  <tr>
    <th align="right">ID: </th>
    <td><bean:write name="af" property="id"/></td>
  </tr>
  <tr>
    <th align="right">Categories: </th>                     
    <td>
       <html:checkbox name="af" property="personal" value="true"/> personal<br>
       <html:checkbox name="af" property="business" value="true"/> business<br>
       <html:checkbox name="af" property="provider" value="true"/> provider<br>
       <html:checkbox name="af" property="customer" value="true"/> customer<br>
       <html:checkbox name="af" property="family" value="true"/> family<br>
       <html:checkbox name="af" property="feet" value="true"/> feet<br>
    </td>
  </tr>
  <tr>
    <th align="right">Company: </th>
    <td><html:text name="af" property="company" size="45" maxlength="45"/></td>
  </tr>
  <tr>
    <th align="right">First Name: </th>              
    <td><html:text name="af" property="firstname" size="45" maxlength="45"/></td>
  </tr>
  <tr>
    <th align="right">Last Name: </th>
    <td><html:text name="af" property="lastname" size="45" maxlength="45"/></td>
  </tr>
  <tr>
    <th align="right">Street1: </th>
    <td><html:text name="af" property="street1" size="45" maxlength="45"/></td>
  </tr>
  <tr>
    <th align="right">Street2: </th>
    <td><html:text name="af" property="street2" size="45" maxlength="45"/></td>
  </tr>
  <tr>
    <th align="right">City, State Zip: </th>
    <td><html:text name="af" property="csz" size="45" maxlength="55"/></td>
  </tr>
  <tr>
    <th align="right">E-Mail: </th>
    <td><html:text name="af" property="email" size="45" maxlength="45"/></td>
  </tr>
  <tr>
    <th align="right">Home Phone: </th>
    <td><html:text name="af" property="home_phone" size="45" maxlength="15"/></td>
  </tr>
  <tr>
    <th align="right">Work Phone: </th>
    <td><html:text name="af" property="work_phone" size="45" maxlength="15"/></td>
  </tr>
  <tr>
    <th align="right">Cell Phone: </th>
    <td><html:text name="af" property="cell_phone" size="45" maxlength="15"/></td>
  </tr>
   <tr>
    <th align="right">Web Site: </th>
    <td><html:text name="af" property="website" size="45" maxlength="200"/></td>
  </tr>
  <tr>
    <th align="right">Notes: </th>
    <td><html:textarea name="af" property="notes" cols="34" rows="6"/></td>
  </tr>
  <tr>
    
    <td colspan="2"><html:submit /></td>
  </tr>
</table>
</html:form>