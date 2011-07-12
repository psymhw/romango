<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
       <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Language" content="en-us" />

<title><tiles:insert attribute='title' /></title>
 <link rel="stylesheet" href="style.css" type="text/css" />
</head>
<body>
       
<table width="100%" cellspacing="0" cellpadding="0" border="1">
  <tr valign="top">
	 <td colspan="2" bgcolor="#CC99CC" align="center">
	   <tiles:insert attribute='header' />
	 </td>
  </tr>
  <tr valign="top">
	 <td colspan="2">
	   <tiles:insert attribute='menu' />
	 </td>
  </tr>
  <tr valign="top">
	 <td colspan="2">
	   <tiles:insert attribute='body' />
	 </td>
  </tr>
  
   <tr valign="top">
	 <td colspan="2">
	   <tiles:insert attribute='footer' />
	 </td>
  </tr>
</table>
</body>
</html>
		 
	 