<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="db.AddressDb" %>
<%@ page import="address.AddressListSelectForm" %>
<br>
<a href="addressEdit.do?mode=new">new address</a>

<%
AddressListSelectForm alsf = (AddressListSelectForm)request.getAttribute("AddressListSelectForm");
pageContext.setAttribute("alsf", alsf);
String highlight = "red";
String color = highlight;
%>

<br><br/>

<html:form action="/addressList">
<b>Sort: </b>
<html:radio  property="sort" value="last" onclick="submit();"/>Last
<html:radio  property="sort" value="first" onclick="submit();"/>First
<html:radio  property="sort" value="company" onclick="submit();"/>Company
<br><br>
<table>
<tr>
<td>
<html:submit property="letter" value="Clear"/>
</td>
<% if ("A".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="A"/>
</td>
<% if ("B".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="B"/>
</td>
<% if ("C".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="C"/>
</td>
<% if ("D".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="D"/>
</td>
<% if ("E".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="E"/>
</td>
<% if ("F".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="F"/>
</td>
<% if ("G".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="G"/>
</td>
<% if ("H".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="H"/>
</td>
<% if ("I".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="I"/>
</td>
<% if ("J".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="J"/>
</td>
<% if ("K".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="K"/>
</td>
<% if ("L".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="L"/>
</td>
<% if ("M".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="M"/>
</td>
<% if ("N".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="N"/>
</td>
<% if ("O".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="O"/>
</td>
<% if ("P".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="P"/>
</td>
<% if ("Q".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="Q"/>
</td>
<% if ("R".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="R"/>
</td>
<% if ("S".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="S"/>
</td>
<% if ("T".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="T"/>
</td>
<% if ("U".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="U"/>
</td>
<% if ("V".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="V"/>
</td>
<% if ("W".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="W"/>
</td>
<% if ("X".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="X"/>
</td>
<% if ("Y".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="Y"/>
</td>
<% if ("Z".equals(alsf.getLetter())) color=highlight; else color="white";  %>
<td bgcolor="<%=color %>">
<html:submit property="letter" value="Z"/>
</td>
</tr>
</table>
<br>
<html:radio  property="category" value="all" onclick="submit();"/>All
<html:radio  property="category" value="personal" onclick="submit();"/>Personal
<html:radio  property="category" value="business" onclick="submit();"/>Business
<html:radio  property="category" value="provider" onclick="submit();"/>Provider
<html:radio  property="category" value="customer" onclick="submit();"/>Customer
<html:radio  property="category" value="family" onclick="submit();"/>Family

</html:form>
<br><br>
<%
Collection addressList = (Collection)request.getAttribute("AddressList");
Iterator it = addressList.iterator();
AddressDb adb;

%>
<table border="1" bgcolor='#F5F5DC'>
<tr>
<th>ID</th>

<%
if ("last".equals(alsf.getSort()))
{
  out.println("<th nowrap>Last Name</th>");
  out.println("<th nowrap>First Name</th>");
  out.println("<th nowrap>Company</th>");
}
if ("first".equals(alsf.getSort()))
{
  out.println("<th nowrap>First Name</th>");
  out.println("<th nowrap>Last Name</th>");
  out.println("<th nowrap>Company</th>");
}
if ("company".equals(alsf.getSort()))
{
  out.println("<th nowrap>Company</th>");
  out.println("<th nowrap>First Name</th>");
  out.println("<th nowrap>Last Name</th>");
 }
%>
<th>E-Mail</th>
<th>Phone</th>
</tr>

<%


while(it.hasNext())
{
  adb=(AddressDb)it.next();
  if ("last".equals(alsf.getSort()))
  {
    if (adb.getLastname()==null) continue;
    if (adb.getLastname().trim().length()==0) continue;
  }
  if ("first".equals(alsf.getSort()))
  {
    if (adb.getFirstname()==null) continue;
    if (adb.getFirstname().trim().length()==0) continue;
  }
  if ("company".equals(alsf.getSort()))
  {
    if (adb.getCompany()==null) continue;
    if (adb.getCompany().trim().length()==0) continue;
  }

  pageContext.setAttribute("adb", adb);

  %>

<tr>
<td><a href="addressEdit.do?mode=show&id=<bean:write name="adb" property="id"/>">
<bean:write name="adb" property="id"/></a></td>
<% if ("last".equals(alsf.getSort()))
    { %>
<td><bean:write name="adb" property="lastname"/></td>
<td><bean:write name="adb" property="firstname"/></td>
<td><bean:write name="adb" property="company"/></td>
<% }
        if ("first".equals(alsf.getSort()))
    { %>
<td><bean:write name="adb" property="firstname"/></td>
<td><bean:write name="adb" property="lastname"/></td>
<td><bean:write name="adb" property="company"/></td>
<% }
     if ("company".equals(alsf.getSort()))
    { %>
<td><bean:write name="adb" property="company"/></td>
<td><bean:write name="adb" property="firstname"/></td>
<td><bean:write name="adb" property="lastname"/></td>
<% } %>

<td><a href='mailto:<bean:write name="adb" property="email"/>'><bean:write name="adb" property="email"/></a></td>
<td>
<logic:notEmpty name="adb" property="home_phone">
        home: <bean:write name="adb" property="home_phone"/>
</logic:notEmpty>
<logic:notEmpty name="adb" property="work_phone">
        work: <bean:write name="adb" property="work_phone"/>
</logic:notEmpty>
<logic:notEmpty name="adb" property="cell_phone">
        cell: <bean:write name="adb" property="cell_phone"/>
</logic:notEmpty>
</td>
</tr>
<%
}
%>
</table> 