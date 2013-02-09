<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="db.AccountsDb" %>
<%
AccountsDb adb = (AccountsDb)request.getAttribute("AccountsDb");
%>


<h1>Edit Account</h1>

  <table border="1" cellpadding="4" cellspacing="0">
    <tr>
      <td><b>ID</b></td>
      <td><b>Description</b></td>
    </tr>
    <form action="admin.do">
    <input type="hidden" name="mode" value="updateAccount">
    <input type="hidden" name="acctId" value="<%=adb.getAcctId()%>">
    <tr>
      <td>
        <input type="submit" value="Update">
      </td>
      <td>
        <input type="text" name="description" size="45" maxlength="45" value="<%=adb.getDescription()%>">
      </td>
    </tr>     
    </form>
  </table>
