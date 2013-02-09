<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>


<tiles:insert definition="image_support_def" flush="true" >
<tiles:put name="body"   value="/notes/noteEditBody.jsp" />
<tiles:put name="images"   value="/image/imageBody.jsp" />
<tiles:put name="feet"   value="/dummy/dummy.jsp" />
</tiles:insert>
