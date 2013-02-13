<%@ page language="java" %>
<%@ page import="db.ArticleDb" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%
ArticleDb adb = (ArticleDb)request.getAttribute("ArticleDb");
//System.out.println("musicEditBody.jsp - summary: "+adb.getSummary());
pageContext.setAttribute("adb", adb);
%>
</script>
<!-- TinyMCE -->
<script type="text/javascript" src="<%=request.getContextPath()%>/jscripts/tiny_mce/tiny_mce.js"></script>
<script type="text/javascript">
	tinyMCE.init({
		mode : "textareas",
			theme : "advanced",
			plugins : "safari,spellchecker,style,advhr,advimage,advlink,emotions,iespell,insertdatetime,preview,media,visualchars,imagemanager",
			theme_advanced_buttons1 : "bold,italic,underline,|,formatselect,fontselect,fontsizeselect",
			theme_advanced_buttons2 : "bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,image,code,|,preview,|,forecolor",
			theme_advanced_buttons3 : "hr,removeformat,|,emotions,iespell,media"
	});
</script>
<!-- /TinyMCE -->
<%@ include file="/script/datePicker.js" %>
<h1>Articles Edit</h1>
<br>
<html:form action="/music">
<html:hidden name="adb" property="mode"/>
<html:hidden name="adb" property="id"/>
<table>
<tr> 
  <td align=right><b>Active:</b> </td>
  <td><html:checkbox name="adb" property="activeBool"/></td>
  </tr>
<tr>
  <td align="right" valign="top"><b>Article Date: </b></td>
  <td><html:text name="adb" property="strDate" size="10"  />
  <html:button property="strDate" value="select" onclick="displayDatePicker('strDate');"/>
  </td>
</tr>

<tr>
    <td align="right"><b>Category: </b></td>
    <td><html:text name="adb"  property="category" size="45" maxlength="45"/></td>
  </tr>
  
  <tr>
    <td align="right"><b>Author: </b></td>
    <td><html:text name="adb"  property="author" size="40" maxlength="100"/></td>
  </tr>
  
  <tr>
    <td align="right"><b>Title: </b></td>
    <td><html:text name="adb"  property="title" size="70" maxlength="100"/></td>
  </tr>
  
  <tr>
    <td align="right" valign="top"><b>Summary: </b></td>
    <td><html:textarea  name="adb" property="summary" rows="5" cols="70"/></td>
  </tr>
  
   <tr>
    <td align="right" valign="top"><b>Article: </b></td>
    <td><html:textarea  name="adb"  property="body" rows="20" cols="70"/></td>
  </tr>
   <tr>
    <td align="right" valign="top"><b>Admin Notes: </b></td>
    <td><html:text  name="adb"  property="admin_notes" size="70" maxlength="200"/></td>
  </tr>
  
</table>



<html:submit/>
</html:form>