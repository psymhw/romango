<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insert definition="eventListDef" flush="true" >
<tiles:put name="left_body" value="/struts/events/eventEditBody.jsp" />
<tiles:put name="right_body" value="/struts/events/eventEditHelpBody.jsp" />
</tiles:insert>