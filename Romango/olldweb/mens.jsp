<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1"/>
<jsp:include page="metatags.html" />
<link rel="stylesheet" type="text/css" href="style.css" media="screen"/>

<title>Romango Handmade Shoes</title>
<script type="text/javascript" src="p7pm/p7popmenu.js"></script>
<style type="text/css" media="screen">
<!--
@import url("p7pm/p7pmh0.css");
-->
</style>


<script type="text/javascript">

//If using image buttons as controls, Set image buttons' image preload here true
//(use false for no preloading and for when using no image buttons as controls):
var preload_ctrl_images=true;

//And configure the image buttons' images here:
var previmg='images/left.gif';
var stopimg='images/stop.gif';
var playimg='images/play.gif';
var nextimg='images/right.gif';

var slides=[]; //FIRST SLIDESHOW
//configure the below images and descriptions to your own. 
slides[0] = ["gallery/men/blue_black_derby.jpg", "Blue Black Derby"];
slides[1] = ["gallery/men/boots.jpg", "Black Boots"];
slides[2] = ["gallery/men/fiddle_1.jpg", "French calf - full brogue Oxfords"];
slides[3] = ["gallery/men/brogue3.jpg", "Semi-brogues"];
slides[4] = ["gallery/men/yellow-grade.gif", "Whole Cut Oxfords"];
slides[5] = ["gallery/men/sm_IMG_4331.jpg", "Double sole w Gossier stitch"];
slides[6] = ["gallery/men/ollie_oxfords.jpg", "French Calf Oxfords with fiddleback"];
//above slide show uses only the defaults

//Notes:
//slides#.target will set a target for a slide group, will be overridden by slides#[#][3], if present
//slides#.specs will set new window specifications for a slide group, will be overridden by slides#[#][4], if present
//slides#.fadecolor will set fading images background color, defaults to white
//slides#.no_controls will set a slide show with no controls
//slides#.random will set a random slide sequence on each page load
//slides#.delay=3000 will set miliseconds delay between slides for a given show, may also be set in the call as the last parameter
//slides#.jumpto=1 will display added controls to jump to a particular image by its number
//slides#.no_added_linebreaks=1; use for no added line breaks in formatting of texts and controls

//use below to create a customized onclick event for linked images in a given show:
//slides#.onclick="window.open(this.href,this.target,'top=0, left=0, width='+screen.availWidth+', height='+screen.availHeight);return false;"

</script>

<script src="swissarmy.js" type="text/javascript">

/***********************************************
* Swiss Army Image slide show script  - © John Davenport Scheuer: http://home.comcast.net/~jscheuer1/
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for full original source code
***********************************************/

</script>
</head>

<body onload="P7_initPM(1,0,1,-20,10)">

<div id="wrapper">
<div id="container">

<div class="title">
<jsp:include page="title.html" />
</div>


<div id="menu">
<jsp:include page="menu.html" />
</div>

		<div class="clearer"></div>

<div class="main">

	<div class="content">
     <h1>Men's</h1>
        <p>
        <script type="text/javascript">
        //Notes on Parameters: The only required parameter is the slides_array_name.  If Width is used, so must Height.
        //Interval is optional too.  It is always last, either fourth after Width and Height or second after Slides_array_name.
        //Usage: new inter_slide(Slides_array_name, Width, Height, Interval)
        new inter_slide(slides, 400, 400)
        </script>
        </p>

</div>
</p>    
      </div>


	<div class="footer">
		<jsp:include page="footer.html" />
	</div>


</div>

</div>
</div>

</body>
</html>