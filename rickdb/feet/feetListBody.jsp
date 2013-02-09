<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="feet.FeetData" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>

<% 
FeetData fd = null;

ArrayList feetList = (ArrayList)request.getAttribute("FeetList");
Iterator it = feetList.iterator();
%>
<table border="1">
<tr>
         <th>&nbsp;</th>
         <th colspan="7">Left Foot</th>
         <th colspan="7">Right Foot</th>
        </tr>
      <tr>
        <th valign="bottom">Name</th>
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



<% 
while(it.hasNext())
{
  fd=(FeetData)it.next();
  pageContext.setAttribute("fd", fd);
  %>
  <tr>
    <td><bean:write name="fd" property="firstname"/> <bean:write name="fd" property="lastname"/></td>
    <td valign="top"><bean:write name="fd" property="lf_length" /></td>
          <td valign="top"><bean:write name="fd" property="lf_bb_width" /></td>
          <td valign="top"><bean:write name="fd" property="lf_heel_width"/></td>
          <td valign="top"><bean:write name="fd" property="lf_bb_girth" /></td>
          <td valign="top"><bean:write name="fd" property="lf_waist_girth" /></td>
          <td valign="top"><bean:write name="fd" property="lf_instep_girth" /></td>
          <td valign="top"><bean:write name="fd" property="lf_short_heel" /></td>
          <td valign="top"><bean:write name="fd" property="rt_length" /></td>
          <td valign="top"><bean:write name="fd" property="rt_bb_width" /></td>
          <td valign="top"><bean:write name="fd" property="rt_heel_width"/></td>
          <td valign="top"><bean:write name="fd" property="rt_bb_girth" /></td>
          <td valign="top"><bean:write name="fd" property="rt_waist_girth" /></td>
          <td valign="top"><bean:write name="fd" property="rt_instep_girth" /></td>
          <td valign="top"><bean:write name="fd" property="rt_short_heel" /></td>
            
     
  </tr>
  <%
  
}

%>
</table>
