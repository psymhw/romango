<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="image.ImageUploadForm" %>

<%
ImageUploadForm iuf = (ImageUploadForm)request.getAttribute("ImageUploadForm");

if (iuf==null)
{
  out.println("imageUploadFormBody.jsp - ImageUploadForm is null");
  return;
}
if ("cust".equals(iuf.getType())) out.println("<font size=+1>Add Image to address</font> <br>");
if ("note".equals(iuf.getType())) out.println("<font size=+1>Add Image to note</font> <br>");

pageContext.setAttribute("iuf",iuf);
%>
<html:form action="imageUpload.do" enctype="multipart/form-data" method="post">
<html:hidden name="iuf" property="cust_id"/>
<html:hidden name="iuf" property="note_id"/>
<html:hidden name="iuf" property="type"/>
<p>
Type some text (if you like):<br>
<html:text name="iuf" property="description" size="30"/>
</p>
<p>
Please specify a file, or a set of files:<br>
<html:file name="iuf" property="fileForm" size="40"/>
</p>
<p>
<html:submit/>
</p>
</html:form>