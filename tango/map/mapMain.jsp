<%@ include file="/common/declare.jsp" %>
<%@ include file="/common/mapHead.jsp" %>

<body onload="LoadFuncs()">
<div id="wrapper">
  <div id="container">
    <div class="title">
      <img src="<%=request.getContextPath()%>/images/lillac_strip2.jpg" alt="Eugene Tango" />
    </div>
    <div id="menu">
      <%@ include file="/menu/menu.jsp" %>
    </div>
    <div class="clearer"></div>
    <div class="main" id="two-columns">
     	<div class="all">
		    <div class="left">
			    <div class="content">
           <%@ include file="/map/mapBody.jsp" %>
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
		  