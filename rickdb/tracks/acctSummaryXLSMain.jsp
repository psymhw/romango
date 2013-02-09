<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="tracks.TrackData" %>
<%
TrackData trackData = (TrackData)request.getAttribute("TrackData");
ArrayList transList = trackData.getTransList();
pageContext.setAttribute("transList", transList);
DecimalFormat df = new DecimalFormat("#,###.00");
response.setContentType("application/vnd.ms-excel");
response.setHeader("Content-disposition","inline; filename="+trackData.getTrackName().replace(" ","_")+"_Summary.html");
%>
<br>
<b><%=trackData.getTrackName()%> Account Summary <%=trackData.getMin_date()%> to  <%=trackData.getMax_date()%></b><br><br>

          

  <table border="1" cellpadding="4" cellspacing="0">
    <tr>
       <td><b>Account</b></td>
      <td><b>Credit</b></td>
      <td><b>Debit</b></td>
     </tr>
     <logic:iterate id="trans" name="transList">
       <tr>
         
         <td align="left">
          
           <bean:write name="trans" property="acct_desc"/>
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
         
      </tr> 
     </logic:iterate> 
     <tr>
     
     <td align="right"><b>Totals:</b> </td>
     <td align="right"><b><%=df.format(trackData.getCreditTotal())%></b> </td>
     <td align="right"><b><%=df.format(trackData.getDebitTotal())%></b> </td>
     <td align="right"><b><%=df.format(trackData.getNet())%></b> </td>
     </tr>
     
  </table>    