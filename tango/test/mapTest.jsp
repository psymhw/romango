<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
 
 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAA0fFzHCc5eBuha_6_PObRJxSja03KnP5cnnwmJm_NSN00_AmNUhTC3B-srFl7rEYHNiyFhT2VHE2nLg"
      type="text/javascript"></script>
    <script type="text/javascript">

    //<![CDATA[
    var map = null;
    var geocoder = null;
		
    function load() 
		{
      if (GBrowserIsCompatible()) {
        map = new GMap2(document.getElementById("map"));
				map.addControl(new GSmallMapControl());
        map.addControl(new GMapTypeControl());

       
          map.setCenter(new GLatLng(44.046573,-123.092794), 12);
          map.setUIToDefault();
				geocoder = new GClientGeocoder();

				showAddress("615 W. 31st Ave, Eugene, Oregon 97405", "Pams House", "a link");
				
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
       // map.setCenter(point, 13);
        var marker = new GMarker(point);
        map.addOverlay(marker);
				GEvent.addListener(marker, "click", function() 
				{
          marker.openInfoWindowHtml(address+"<br>"+info+"<br>"+"<a href=\""+ref+"\">Link</a>");
        });
				 
				
       // marker.openInfoWindowHtml(address);
      }
    }
  );
}

    //]]>
    </script>
</head>
<body  onload="load()" onunload="GUnload()">

<h1>hello</h1>

<div id="map" style="width: 600px; height: 400px"></div>
</body>
</html>