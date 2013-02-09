<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="trans.Lookups" %>
<%@ page import="org.apache.struts.util.LabelValueBean" %>
<%
Lookups lookups = new Lookups();

%>
<h1>Tracks</h1>

<%
ArrayList tracks = lookups.tracks;
Iterator it = tracks.iterator();
LabelValueBean track;
while(it.hasNext())
{
   track = (LabelValueBean)it.next();
   out.println("<a href=\"tracks.do?mode=showTrack&trackId="+track.getValue()+"\">");
   out.println(track.getLabel()+"</a><br>");
}
%>