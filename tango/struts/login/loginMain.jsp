<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insert definition="eventListDef" flush="true" >
<tiles:put name="left_body" value="/struts/login/loginBody.jsp" />
<tiles:put name="right_body" value="/struts/login/loginNotes.jsp" />
</tiles:insert>