<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.Collection" %>
<%
Collection trackList = (Collection)request.getAttribute("TrackList");
pageContext.setAttribute("trackList", trackList);
Collection accountList = (Collection)request.getAttribute("AccountList");
pageContext.setAttribute("accountList", accountList);
%>
<br>
<font size="+1"><b>Administration</b></font><br>
<br>
<b>Tracks</b> <br><br>
  <table border="1" cellpadding="4" cellspacing="0">
    <tr>
      <td><b>ID</b></td>
      <td><b>Description</b></td>
    </tr>
    <form action="admin.do">
    <input type="hidden" name="mode" value="insertTrack">
    <tr>
      <td>
        <input type="submit" value="Insert">
      </td>
      <td>
        <input type="text" name="description" size="45" maxlength="45">
      </td>
    <tr>      
    </form>
    
     <logic:iterate id="track" name="trackList">
       <tr>
         <td>
           <a href="admin.do?mode=editTrack&trackId=<bean:write name="track" property="trackId"/>">
           <bean:write name="track" property="trackId"/></a>
         </td>
          <td>
           <bean:write name="track" property="description"/>
         </td>
       </tr> 
     </logic:iterate> 
  </table>
  
  <br>
<b>Accounts</b><br><br>
  <table border="1" cellpadding="4" cellspacing="0">
    <tr>
      <td><b>ID</b></td>
      <td><b>Description</b></td>
    </tr>
    <form action="admin.do">
    <input type="hidden" name="mode" value="insertAccount">
    <tr>
      <td>
        <input type="submit" value="Insert">
      </td>
      <td>
        <input type="text" name="description" size="45" maxlength="45">
      </td>
    <tr>      
    </form>
    
     <logic:iterate id="account" name="accountList">
       <tr>
         <td>
           <a href="admin.do?mode=editAccount&acctId=<bean:write name="account" property="acctId"/>">
           <bean:write name="account" property="acctId"/></a>
         </td>
          <td>
           <bean:write name="account" property="description"/>
         </td>
       </tr> 
     </logic:iterate> 
  </table>