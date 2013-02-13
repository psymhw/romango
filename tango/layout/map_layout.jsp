<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ page language="java" %>
<%@ page import="db.EventDb" %>
<%
EventDb edb= (EventDb)request.getAttribute("EventDb");
if (edb==null) { out.println("mapHead.jsp: EventDb is null"); return; }
%>
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
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAA0fFzHCc5eBuha_6_PObRJxSja03KnP5cnnwmJm_NSN00_AmNUhTC3B-srFl7rEYHNiyFhT2VHE2nLg"
 
      type="text/javascript"></script>
    <script type="text/javascript">

    //<![CDATA[
    var map = null;
    var geocoder = null;
		
    function mapLoad() 
		{
      if (GBrowserIsCompatible()) {
        map = new GMap2(document.getElementById("map"));
				map.addControl(new GSmallMapControl());
        map.addControl(new GMapTypeControl());

       
        //  map.setCenter(new GLatLng(44.046573,-123.092794), 12);
          map.setUIToDefault();
				geocoder = new GClientGeocoder();

				showAddress("<%=edb.getVenue_addr()%> <%=edb.getVenue_csz()%>", "<%=edb.getVenue_name()%>", "a link");
				
				}
				
    }

		function createMarker(point, number) 
		{
      var marker = new GMarker(point);
      GEvent.addListener(marker, "click", function() {
      marker.openInfoWindowHtml("Marker #<b>" + number + "</b>");});
      return marker;
    }
		
		function showAddress(address, info, ref) 
		{
      geocoder.getLatLng(
      address,
      function(point) {
      if (!point) {
        alert(address + " not found");
      } else {
        map.setCenter(point, 14);
        var marker = new GMarker(point);
        map.addOverlay(marker);
				GEvent.addListener(marker, "click", function() 
				{
          marker.openInfoWindowHtml("<b>"+info+"</b><br>"+address);
        });
				 
				
       // marker.openInfoWindowHtml(address);
      }
    }
  );
}

    //]]>
    </script>

<script language="JavaScript">
function LoadFuncs() {
	P7_initPM(1,0,1,-20,10);
	mapLoad();
}
</script>
</head>

<body onload="LoadFuncs()">
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
     	<div class="all">
		    <div class="left">
			    <div class="content">
			      
            <tiles:insert attribute='body' />
          </div>
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
		  