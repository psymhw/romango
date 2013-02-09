<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="tracks.TrackData" %>
<%@ page import="trans.Lookups" %>
<%
TrackData trackData = (TrackData)request.getAttribute("TrackData");
ArrayList transList = trackData.getTransList();
pageContext.setAttribute("transList", transList);
DecimalFormat df = new DecimalFormat("#,###.00");
String set;

ArrayList archiveSets = (ArrayList)request.getSession().getServletContext().getAttribute("ArchiveSets");
String archiveSet = (String)request.getSession().getAttribute("ArchiveSet");
if (archiveSet==null)  archiveSet="Current";
if (archiveSets==null) out.println("trackShowBody - archiveSets is null<br>");
else
{
  Iterator it = archiveSets.iterator();
  while(it.hasNext())
  {
    set=(String)it.next();
    if (archiveSet.equals(set))
      out.println(set+" ");
    else
      out.println("<a href='tracks.do?mode=showTrack&trackId="+trackData.getTrackId()
          +"&archiveSet="+set+"'>"+set+"</a> ");
    //out.println(">>>"+it.next()+"<br>");
  } 
  
  
   
}
%>
<br>
<b><%=trackData.getTrackName()%></b>
<%
if (!"Current".equals(archiveSet)) out.println("<font size=+2> ... Archive Set: "+archiveSet+"</font>");
%>

<br><br>


          
       <a href="transEdit.do?mode=newTrans&trackId=<%=trackData.getTrackId()%>">
         insert</a>  | 
       <a href="tracks.do?mode=summary&trackId=<%=trackData.getTrackId()%>">
         summary</a>
       <br><br>
     

  <table border="1" cellpadding="4" cellspacing="0">
    <tr>
      <td><b>ID</b></td>
      <td><b>Date</b></td>
      <td><b>Description</b></td>
      <td><b>Track</b></td>
      <td><b>Account</b></td>
      <td><b>Credit</b></td>
      <td><b>Debit</b></td>
      <td><b>Customer</b></td>
      <td><b>Order</b></td>
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
           <a href="tracks.do?mode=showTrackAccount&trackId=<bean:write name="trans" property="trackId"/>&acctId=<bean:write name="trans" property="acctId"/>">
           <bean:write name="trans" property="account"/></a>
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
           <bean:write name="trans" property="cust_id"/>
         </td>
         <td align="left">
           <bean:write name="trans" property="order_id"/>
         </td>
         <td align="left">
           <bean:write name="trans" property="comment"/>
         </td>
      </tr> 
     </logic:iterate> 
     <tr>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
     <td align="right"><b>Totals:</b> </td>
     <td align="right"><b><%=df.format(trackData.getCreditTotal())%></b> </td>
     <td align="right"><b><%=df.format(trackData.getDebitTotal())%></b> </td>
     <td align="right"><b><%=df.format(trackData.getNet())%></b> </td>
     </tr>
     
  </table>    