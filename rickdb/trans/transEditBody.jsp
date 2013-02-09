<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="trans.Lookups" %>
<%@ page import="trans.TransForm" %>
<%
Lookups lookups = new Lookups();
pageContext.setAttribute("tracks", lookups.tracks);
pageContext.setAttribute("accounts", lookups.accounts);
TransForm tf = (TransForm)request.getAttribute("TransForm");
pageContext.setAttribute("tf", tf);
%>

<html:form action="/transEdit.do" focus="debit">
<html:hidden name="tf" property="mode" />
<html:hidden name="tf" property="transId" />
<logic:equal name="tf" property="mode" value="update">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

<a href="transEdit.do?mode=delete&transId=<bean:write name="tf" property="transId"/>">
         delete
</a>
</logic:equal>


<table cellspacing="0" cellpadding="2" border="0" style="margin-left:-2px"><tr>
<tr>
<td><b>Mode: </b> </td>
<td><bean:write name="tf" property="mode" /></td>
</tr>
<tr>
<td><b>Date: </b> </td>
<td><html:text  name="tf" property="transDate" size="11" maxlength="11"/> (<i>yyyy-mm-dd</i>)
</td>
</tr>
<tr>
<td><b>Debit: </b> </td>
<td><html:text  name="tf" property="debit" size="11" maxlength="11" />
</td>
<td><b>Credit: </b> </td>
<td><html:text  name="tf" property="credit" size="11" maxlength="11"/>
</td>
</tr>
<tr>
<td><b>Description: </b> </td>
<td colspan="3"><html:text  name="tf" property="description" size="100" maxlength="100"/>
</td>
</tr>
<tr>
<td><b>Track: </b> </td>
<td><html:select name="tf" property="trackId">
<html:options collection="tracks" property="value" labelProperty="label"/>
</html:select></td>
</tr>
<tr>
<td><b>Account: </b> </td>
<td><html:select name="tf" property="acctId">
<html:options collection="accounts" property="value" labelProperty="label"/>
</html:select></td>
</tr>
<tr>
<td><b>Customer: </b> </td>
<td colspan="3"><html:text  name="tf" property="cust_id" size="5" maxlength="5"/>
</td>
</tr>
<tr>
<td><b>Order: </b> </td>
<td colspan="3"><html:text  name="tf" property="order_id" size="5" maxlength="5"/>
</td>
</tr>
<tr>
<td><b>Comment: </b> </td>
<td colspan="3"><html:text  name="tf" property="comment" size="100" maxlength="200"/>
</td>
</tr>

<tr>
<td>&nbsp;</td>
<td><div style="padding-top:18px"><html:submit value=" Submit "/> <input type="button" onclick="document.location.href ='/';" value="Cancel" /></div></td>
</tr>
</table>

</html:form> 

