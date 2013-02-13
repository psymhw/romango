<%@ page language="java" %>
<%@ page import="model.Settings" %>
<%@ page import="db.LinksDb" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.struts.util.LabelValueBean" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>



<%
  LinksDb ldb = (LinksDb)request.getAttribute("LinksDb");
  pageContext.setAttribute("ldb", ldb);
  if (ldb==null) { out.println("linksEdit.jsp: ldb is null"); return; } 
  ArrayList <LabelValueBean>categories = (ArrayList)request.getAttribute("LinkCats");
  pageContext.setAttribute("cats", categories);
%>
  <h1>Links <%=ldb.getMode() %></h1>
  
  <html:form action="/linksEdit" method="post" >


<html:hidden name="ldb" property="mode" />
<html:hidden name="ldb" property="id" />
<html:hidden name="ldb" property="member_id"  />


<br><br>
<table border="1">

<tr>
  <td align="right"><b>Title: </b></td>
  <td><html:text name="ldb" property="title" size="100" /></td>
</tr>
<tr>
  <td align="right"><b>Sequence: </b></td>
  <td><html:text name="ldb" property="seq"  size="3" /></td>
</tr>
<tr>
 <td><b>Category: </b></td><td><html:select name="ldb" property="category" >
        <html:options collection="cats" property="value" labelProperty="label"/></html:select></td>
</tr>
<tr>
  <td align="right"><b>URL: </b></td>
  <td><html:text name="ldb" property="url"   size="100" /></td>
</tr>

<tr>
  <td align="right"><b>Description: </b></td>
  <td><html:textarea  name="ldb" property="description" cols="70" rows="5" /></td>
</tr>

</table>
<input type="submit" />
</html:form>