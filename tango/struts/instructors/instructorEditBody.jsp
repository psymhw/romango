<%@ page language="java" %>
<%@ page import="db.MemberDb" %>
<%@ page import="model.Settings" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.apache.struts.util.LabelValueBean" %>

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

<h1>Instructor Edit</h1>
<%
  MemberDb mdb = (MemberDb)request.getAttribute("MemberDb");
  if (mdb==null) { out.println("memberEdit.jsp: mdb is null"); return; } 
  
  pageContext.setAttribute("mdb", mdb);
  Settings settings = new Settings();
  ArrayList areas = settings.areas;
  LabelValueBean lvb=null;
  System.out.println("memberEditBody.jsp");
%>

<form action="<%=request.getContextPath()%>/instructors.do" method="post" >


<input type="hidden" name="mode" value="<%=mdb.getMode()%>" />
<input type="hidden" name="id" value="<%=mdb.getId()%>" />

<br><br>
<table border="1">

<tr>
  <td align="right"><b>First Name: </b></td>
  <td><input type="text" name="first_name" value="<%=mdb.getFirst_name()%>" /></td>
</tr>
<tr>
  <td align="right"><b>Last Name: </b></td>
  <td><input type="text" name="last_name" value="<%=mdb.getLast_name()%>" /></td>
</tr>
<tr>
  <td align="right"><b>e-mail: </b></td>
  <td><input type="text" name="email" value="<%=mdb.getEmail()%>" /></td>
</tr>

<tr>
  <td align="right"><b>Password: </b></td>
  <td><input type="password" name="password" value="<%=mdb.getPassword()%>" /></td>
</tr>
<tr>
  <td align="right"><b>Photo Caption: </b></td>
  <td><input type="text" name="caption" value="<%=mdb.getCaption()%>" /></td>
</tr>

<tr>
  <td align="right"><b>Description: </b></td>
  <td><textarea  name="description" COLS=70 ROWS=15><%=mdb.getDescription()%></textarea></td>
</tr>

</table>
<input type="submit" value="Update" />
</form> 
 <%
  if (mdb.isPhotoPresent())
  {  
  %>
  <a href="<%=request.getContextPath()%>/photo/fullPhoto.jsp?mode=member&id=<%=mdb.getId()%>&caption=<%=mdb.getCaption()%>">
 <img src="<%=request.getContextPath()%>/imageReader?mode=member&id=<%=mdb.getId()%>" alt="member Photo" width="200"/><br>
 </a>
 <b><%=mdb.getCaption() %></b><br><br>
 <%} else { %>
 No Photo
 <%} %>
<hr><br>
 
    <h1>Add or replace photo</h1>

    <form action="<%=request.getContextPath()%>/imageUpload" method="post" enctype="multipart/form-data">
    <input type="hidden" name="mode" value="instructor" />
    <input type="hidden" name="id" value="<%=mdb.getId()%>" />
      <p><br/>
      <table>
      <tr>
        <td align="right"><b>Choose a photo: </b></td>
       <td><input name="myFile" type="file"/>  (JPEG only)</td>
       </tr>
       <tr>
        <td align="right"><b>Photo Caption: </b></td>
       <td><input name="caption" type="text"  value="<%=mdb.getCaption()%>"/></td>
       </tr>
       </table>
      </p>

      <p>
        <input type="submit" value="upload photo"/>
       
      </p>
    </form>
  