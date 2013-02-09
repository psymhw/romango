<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList" %>
<%
ArrayList transList = (ArrayList)request.getAttribute("TransList");
pageContext.setAttribute("transList", transList);
%>
<br>
<b>Transaction Master</b><br><br>

      
       <a href="transEdit.do?mode=newTrans">
         insert
       </a>  <br><br>

  <table border="1" cellpadding="4" cellspacing="0" bgcolor='#FFF8DC'>
    <tr>
      <td><b><a href="transHome.do?sortBy=transId">ID</a></b></td>
      <td><b><a href="transHome.do?sortBy=date">Date</a></b></td>
      <td><b>Description</b></td>
      <td><b>Track</b></td>
      <td><b>Account</b></td>
      <td><b>Credit</b></td>
      <td><b>Debit</b></td>
      <td><b>Comment</b></td>
    </tr>
     <logic:iterate id="trans" name="transList">
       <tr>
         <td>
           <a href="transEdit.do?mode=edit&transId=<bean:write name="trans" property="transId"/>">
           <bean:write name="trans" property="transId"/></a>
         </td>
         <td>
           <bean:write name="trans" property="transDate" format="d-MMM-yyyy" locale="true"/>
         </td>
         <td>
           <bean:write name="trans" property="description"/>
         </td>
         <td align="left">
           <bean:write name="trans" property="track"/>
         </td><td align="left">
           <bean:write name="trans" property="account"/>
         </td>
         <td align="right">
           <logic:equal name="trans" property="credit" value="0">
              &nbsp;
           </logic:equal>
           <logic:notEqual name="trans" property="credit" value="0">&nbsp;
             <bean:write name="trans" property="credit" format="####.00"/>
           </logic:notEqual>
         </td>
         <td align="right">
           <logic:equal name="trans" property="debit" value="0">
              &nbsp;
           </logic:equal>
           <logic:notEqual name="trans" property="debit" value="0">&nbsp;
              <bean:write name="trans" property="debit" format="####.00"/>
           </logic:notEqual>
         </td>
         <td align="left">
           <bean:write name="trans" property="comment"/>
         </td>
      </tr> 
     </logic:iterate> 
  </table>    