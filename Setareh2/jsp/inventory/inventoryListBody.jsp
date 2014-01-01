<%@ page language="java" %>
<%@ page import="db.InventoryDb" %>
<%@ page import="db.MemberDb" %>
<%@ page import="model.Settings" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<link href="table_style.css" rel="stylesheet" type="text/css" />
<h1>Setareh Biotech Inventory</h1>
<br>
<table id="mytable" cellspacing="0" summary="Setareh Inventory">
<caption>Table 1: Setareh Inventory </caption>
	<th scope="col" abbr="Item" class="nobg">Item#</th>
  <th scope="col" abbr="Product Name">Product Name</th>		
<%
MemberDb ud = (MemberDb)session.getAttribute("UserData");
if (ud==null) ud= new MemberDb();
int uid=0;
if (ud!=null)	uid=ud.getId();
Collection<InventoryDb> items = (Collection<InventoryDb>)request.getAttribute("InventoryList");
if (items==null) { out.println("no inventory found"); return; }
Iterator<InventoryDb> it = items.iterator();
InventoryDb idb=null;
while(it.hasNext())
{
  idb = it.next();%>
  <tr> 
  
  <td valign="top"><a href="<%=request.getContextPath()%>/inventory.do?mode=showProduct&id=<%=idb.getId()%>"><%=idb.getItem_no()%></a></td>
  <td valign="top"><%=idb.getProduct_name()%></td>
  </tr>
  <%} %>
  </table>
			