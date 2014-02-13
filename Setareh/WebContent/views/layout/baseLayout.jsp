<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<html style="height: 100%;">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="table_style.css" rel="stylesheet" type="text/css" />
        <link href="style.css" rel="stylesheet" type="text/css" />
            <title><tiles:insertAttribute name="title" ignore="true" /></title>
    </head>
    <body style="height: 100%;">
        <table  id="mytable" border="1" cellpadding="2" cellspacing="2" align="center" height="100%" width="100%">
            <tr>
                <td height="98" colspan="2" bgcolor="black">
                    <tiles:insertAttribute name="header" />
                </td>
            </tr>
            <tr>
                <td width="150" >
                    <tiles:insertAttribute name="menu" />
                </td>
                <td valign="top">
                    <tiles:insertAttribute name="body" />
                </td>
            </tr>
            <tr>
                <td height="30" colspan="2">
                    <tiles:insertAttribute name="footer" />
                </td>
            </tr>
        </table>
    </body>
</html>
