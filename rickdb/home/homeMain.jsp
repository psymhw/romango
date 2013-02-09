<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert definition="home_def" flush="true" >
<tiles:put name="body"   value="/home/homeBody.jsp" />
<tiles:put name="images"   value="/image/imageBody.jsp" />
</tiles:insert>
