<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ page import="db.MemberDb" %>
<%@ page import="links.LinksCategoryForm" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="db.LinksDb" %>
<%@ page import="org.apache.struts.util.LabelValueBean" %>
<%
MemberDb userData = (MemberDb)session.getAttribute("UserData");
String user = (String)session.getAttribute("user");
boolean admin=false;

if (userData!=null)
	if (userData.getAdmin()==1) admin=true;
	if (admin)	out.println("<a href=\""+request.getContextPath()+"/linksEdit.do?mode=new\">new</a><br><br>");

Collection links = (Collection)request.getAttribute("Links");
if (links==null)
{
  out.println("linksListBody - links is null"); return;
}

LinksCategoryForm cf = (LinksCategoryForm)request.getAttribute("LinksCategoryForm");
pageContext.setAttribute("cf", cf);


ArrayList <LabelValueBean>categories = (ArrayList)request.getAttribute("LinkCats");
		
pageContext.setAttribute("cats", categories);
String lastCategory="";
%>
<h1>Links</h1><br>

<html:form action="/links">
<b>Category: </b><html:select name="cf" property="selectedValue" onchange="submit()">
        <html:options collection="cats" property="value" labelProperty="label"/>
</html:select>
</html:form>

<% 
if (links.size()==0)
{
  out.println("no links found"); return;
}
Iterator it = links.iterator();
LinksDb ldb=null;
while(it.hasNext())
{
  ldb=(LinksDb)it.next();
  if (!lastCategory.equals(ldb.getCategory()))
  {
	  lastCategory=ldb.getCategory();
	  out.println("<h2>"+lastCategory+"</h2><br>");
  }
  out.println("<a href=\""+ldb.getUrl()+"\">"+ldb.getTitle()+"</a>");
  if (admin)	
  {
	  out.println("&nbsp;&nbsp;&nbsp;&nbsp;<a href=\""+request.getContextPath()+"/linksEdit.do?mode=edit&id="+ldb.getId()+"\">edit</a>");
    out.println("(seq: "+ldb.getSeq()+")");
  }
  out.println("<br>");
  out.println(ldb.getDescription());
  out.println("<br>");
  out.println("<br>");
}

%>
	
