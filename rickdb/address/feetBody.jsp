<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<%@ page import="address.AddressForm" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="db.FeetDb" %>

<%
AddressForm af = (AddressForm)request.getAttribute("AddressForm");


pageContext.setAttribute("af", af);

%>

<%
if (af.isFeet())
{
  out.println("<h2>Feet Measurements</h2>");
  Collection feetMeasurements=(Collection)request.getAttribute("FeetMeasurements");
  
  FeetDb fdb;
  if (feetMeasurements!=null)
  {  
    Iterator it = feetMeasurements.iterator();
    if (feetMeasurements.size()>0)
    {
      %>
      <br>
      <table border="1">
     
      <% 
      while (it.hasNext())
      {
        
        fdb = (FeetDb)it.next();
        pageContext.setAttribute("fdb", fdb);
        %>
         <tr>
         <td nowrap valign="top"><b>Shoe size: </b></td>
         <td nowrap valign="top"><bean:write name="fdb" property="shoe_size_1" /></td>
         <td nowrap valign="top"><b>Shoe size: </b></td>
         <td nowrap valign="top"><bean:write name="fdb" property="shoe_size_2" /></td>
         <td nowrap valign="top"><b>Notes: </b></td>
         
        <td colspan="9"><bean:write name="fdb" property="notes" /></td>
      </tr>
      <tr>
         <th>&nbsp;</th>
         <th colspan="7">Left Foot</th>
         <th colspan="7">Right Foot</th>
        </tr>
      <tr>
        <th valign="bottom">Feet<br>ID</th>
        <th valign="bottom">Length</th>
        <th valign="bottom">BB Width</th>
         <th valign="bottom">Heel Width</th>
        <th valign="bottom">BB Girth</th>
        <th valign="bottom">Waist<br>Girth</th>
        <th valign="bottom">Instep<br>Girth</th>
        <th valign="bottom">Short<br>Heel</th>
        <th valign="bottom">Length</th>
        <th valign="bottom">BB Width</th>
         <th valign="bottom">Heel Width</th>
        <th valign="bottom">BB Girth</th>
        <th valign="bottom">Waist<br>Girth</th>
        <th valign="bottom">Instep<br>Girth</th>
        <th valign="bottom">Short<br>Heel</th>
       
        
      </tr>  
        <tr>
          <td valign="top">
            <a href="feetEdit.do?mode=edit&feet_id=<bean:write name="fdb" property="id"/>">
            <bean:write name="fdb" property="id" /></a>
          </td>
          <td valign="top"><bean:write name="fdb" property="lf_length" /></td>
          <td valign="top"><bean:write name="fdb" property="lf_bb_width" /></td>
          <td valign="top"><bean:write name="fdb" property="lf_heel_width"/></td>
          <td valign="top"><bean:write name="fdb" property="lf_bb_girth" /></td>
          <td valign="top"><bean:write name="fdb" property="lf_waist_girth" /></td>
          <td valign="top"><bean:write name="fdb" property="lf_instep_girth" /></td>
          <td valign="top"><bean:write name="fdb" property="lf_short_heel" /></td>
          <td valign="top"><bean:write name="fdb" property="rt_length" /></td>
          <td valign="top"><bean:write name="fdb" property="rt_bb_width" /></td>
          <td valign="top"><bean:write name="fdb" property="rt_heel_width"/></td>
          <td valign="top"><bean:write name="fdb" property="rt_bb_girth" /></td>
          <td valign="top"><bean:write name="fdb" property="rt_waist_girth" /></td>
          <td valign="top"><bean:write name="fdb" property="rt_instep_girth" /></td>
          <td valign="top"><bean:write name="fdb" property="rt_short_heel" /></td>
          
        </tr> 
        <%
      }
      %>
      </table>
      <%
    }
  }
}

%>
