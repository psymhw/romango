<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="db.ImageRefDb" %>

<%
Collection imageList = (Collection)request.getAttribute("ImageList");
ImageRefDb irdb=null;
boolean odd = true;
if (imageList==null)
{
out.println("no images");
return;
}
if (imageList.size()==0)
{
out.println("no images");
return;
}

out.println("<table border='1'>");
 

Iterator it = imageList.iterator();
String imageLayout=(String)request.getAttribute("ImageLayout");

if (imageLayout==null) imageLayout="sideBar";
int max_columns=2;

if ("home".equals(imageLayout)) max_columns=4;

int column_counter=0;
while(it.hasNext())
{
  if (column_counter==0) out.println("<tr>");
  
  //if (odd) { out.println("<tr><td valign='top'>"); }
  //else { out.println("<td valign='top'>"); }
  
  irdb = (ImageRefDb)it.next();
  if (irdb.getDescription()==null) irdb.setDescription("");
  if (irdb.getNotes()==null) irdb.setNotes("");
 
  if ("sideBar".equals(imageLayout)) 
  out.println("<td valign='top' width='205'><a href='imageView.do?id="+irdb.getId()+"'><img src=\"DbImageReader?id="+irdb.getId()
      +"\" alt=\"Image\"  width=\"200\"/></a><br>"
      +"<b>"+irdb.getDescription()+"</b> "+irdb.getNotes()
      +"</td>");
  if ("home".equals(imageLayout)) 
  {
    long cust_id=irdb.getCust_id();
    long note_id=irdb.getNote_id();
    
    if (cust_id>0)
    out.println("<td valign='top'  width='205'><a href='addressEdit.do?mode=show&id="+cust_id+"'><img src=\"DbImageReader?id="+irdb.getId()
        +"\" alt=\"Image\"  width=\"200\"/></a><br>"
        +"<b>"+irdb.getDescription()+"</b> "+irdb.getNotes()
        +"</td>");
    else if (note_id>0)
      out.println("<td valign='top'  width='205'><a href='notes.do?mode=edit&id="+note_id+"'><img src=\"DbImageReader?id="+irdb.getId()
          +"\" alt=\"Image\"  width=\"200\"/></a><br>"
          +"<b>"+irdb.getDescription()+"</b> "+irdb.getNotes()
          +"</td>");
    else
      out.println("<td valign='top' width='205'><img src=\"DbImageReader?id="+irdb.getId()
          +"\" alt=\"Image\"  width=\"200\"/><br>"
          +"<b>"+irdb.getDescription()+"</b> "+irdb.getNotes()
          +"</td>");

   
  }
  //if (odd) { out.println("</td>"); }
  //else { out.println("</td></tr>"); }
   column_counter++;
   if (column_counter==max_columns) 
   {
     out.println("</tr>");
     column_counter=0;
   }
  
  //odd=!odd;
}

if (column_counter<max_columns)  out.println("</tr>");
out.println("</table>");
%>

