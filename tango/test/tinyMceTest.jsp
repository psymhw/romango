<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"  dir="ltr">

<head>
<script>
<jsp:include page="/p7pm/p7popmenu.js" flush="true"/>
</script>
<!-- TinyMCE -->
<script type="text/javascript" src="<%=request.getContextPath()%>/jscripts/tiny_mce/tiny_mce.js"></script>
<script type="text/javascript">
	tinyMCE.init({
		mode : "textareas",
			theme : "simple"
	});
</script>
<!-- /TinyMCE -->
<title>TinyMCE TEST</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>

</head>
<body>


<form action="test" method="post" >



<br><br>
<table border="1">

<tr>
  <td align="right"><b>First Name: </b></td>
  <td><input type="text" name="first_name"  /></td>
</tr>
<tr>
  <td align="right"><b>Last Name: </b></td>
  <td><input type="text" name="last_name"  /></td>
</tr>
<tr>
  <td align="right"><b>e-mail: </b></td>
  <td><input type="text" name="email"  /></td>
</tr>

<tr>
  <td align="right"><b>Password: </b></td>
  <td><input type="password" name="password"  /></td>
</tr>
<tr>
  <td align="right"><b>Photo Caption: </b></td>
  <td><input type="text" name="caption" /></td>
</tr>

<tr>
  <td align="right"><b>Description: </b></td>
  <td><textarea id="elm1" name="elm1"  name="description" COLS=70 ROWS=15>Hello World</textarea></td>
</tr>

</table>
<input type="submit" value="Update" />
</form> 

</body>
</html>