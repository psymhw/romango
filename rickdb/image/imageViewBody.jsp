<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="image.ImageDataForm" %>

<%
ImageDataForm idf = (ImageDataForm)request.getAttribute("ImageDataForm");
pageContext.setAttribute("idf", idf);

  out.println("<img src=\"DbImageReader?id="+idf.getId()
      +"\" alt=\"Image\"  /><br>");
%>
<html:form action="imageEdit.do">
<html:hidden name="idf" property="mode"/>
<html:hidden name="idf" property="cust_id"/>
<html:hidden name="idf" property="note_id"/>
<html:hidden name="idf" property="id"/>
<table>
<tr>
<td align="right"><b>Description: </b></td>
<td>
<html:text name="idf" property="description"/></td>
</tr>
<tr>
<td align="right"><b>Notes: </b></td>
<td>
<html:textarea name="idf" property="notes"/>
</td>
</tr>
<tr>
<td>
<html:submit/>
</td>
</tr>
</table>
</html:form>
<a href='imageEdit.do?mode=delete&id=<bean:write name="idf" property="id"/>&cust_id=<bean:write name="idf" property="cust_id"/>'>delete</a>

