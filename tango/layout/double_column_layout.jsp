<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"  dir="ltr">
<script>
<jsp:include page="/p7pm/p7popmenu.js" flush="true"/>
</script>

<head>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1"/>

<style type="text/css" media="screen">
<jsp:include page="/style.css" flush="true"/>

</style>
<title><tiles:insert attribute='title' /></title>

<style type="text/css" media="screen">
<%@ include file="/p7pm/p7pmh0.css" %>
</style>

</head>

<body onload="P7_initPM(1,0,1,-20,10)">

<div id="wrapper">
  <div id="container">
    <div class="title">
      <img src="<%=request.getContextPath()%>/images/lillac_strip2_tg.jpg" alt="Eugene Tango" />
    </div>
    <div id="menu">
      <tiles:insert attribute='menu' />
    </div>
    <div class="clearer"></div>
    <div class="main" id="two-columns">
	    <div class="col2">
		    <div class="all">
			    <div class="content">
            <tiles:insert attribute='left_body' />
		      </div>
				  <div class="clearer"></div>
        </div>
		   
		    <div class="clearer"></div>
          <br /><br />
	    </div>
	    <div class="footer">
	    </div>
    </div>
  </div>
</div>

</body>
</html>