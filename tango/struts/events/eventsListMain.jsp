<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insert definition="eventListDef" flush="true" >
<tiles:put name="left_body" value="/struts/events/eventsListBody.jsp" />
<tiles:put name="right_body" value="/struts/music/musicBody.jsp" />
</tiles:insert>