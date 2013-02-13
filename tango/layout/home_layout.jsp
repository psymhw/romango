<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1"/>
<link rel="stylesheet" type="text/css" href="style/style.css" media="screen"/>

<title>Eugene Argentine Tango</title>


<script type="text/javascript" src="p7pm/p7popmenu.js"></script>

<style TYPE="text/css" MEDIA="screen">
<!--
@import url("p7pm/p7pmh0.css");
-->
</style>


</head>

<body onload="P7_initPM(1,0,1,-20,10)">

<div id="wrapper">
<div id="container">

<div class="title">
<img src="images/lillac_strip2.jpg" alt="Eugene Tango" />


</div>


<div id="menu">
<tiles:insert attribute="menu" />
</div>
		
        <div class="clearer"></div>

<div class="main" id="two-columns">

	<div class="col2">

		<div class="left">

			<div class="content">
			
<tiles:insert attribute="body" />
		
		  </div>
				<div class="clearer"></div>
</div>

		<div class="right">
			
			<div class="content">
          <tiles:insert attribute="dj" />
			</div>

		</div>

		<div class="clearer"></div>
<br /><br />
	</div>



	<div class="footer">
		footer here
	</div>

</div>

</div>
</div>

</body>
</html>