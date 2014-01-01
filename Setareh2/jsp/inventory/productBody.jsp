<%@ page language="java" %>
<%@ page import="db.InventoryDb" %>
<%@ page import="db.MemberDb" %>
<%@ page import="model.Settings" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="inventory.FileInfo" %>

<link href="table_style.css" rel="stylesheet" type="text/css" />
<link href="treeview.css" rel="stylesheet" type="text/css" />
<h1>Setareh Biotech Product</h1>
<br>

<table border="1"><tr><td valign="top">

<table id="mytable" cellspacing="0" summary="Setareh Product">
<caption>Table 1: Setareh Product </caption>
<%
MemberDb ud = (MemberDb)session.getAttribute("UserData");
if (ud==null) ud= new MemberDb();
int uid=0;
if (ud!=null)	uid=ud.getId();
InventoryDb idb=(InventoryDb)request.getAttribute("Product"); 
ArrayList<FileInfo> fileDetails = (ArrayList<FileInfo>)request.getAttribute("FileDetails"); 
Iterator<FileInfo> it = fileDetails.iterator();
%>
 <tr> 
   <th scope="row" class="spec">Item#</th>
   <td valign="top"><%=idb.getItem_no()%></td>
 </tr>
 <tr>
   <th scope="row" class="spec">Name</th>
   <td valign="top"><%=idb.getProduct_name()%></td>
 </tr>
 <tr>
   <th scope="row" class="spec">CAS#</th>
   <td valign="top"><%=idb.cas_no%></td>
 </tr>
 <tr>
   <th scope="row" class="spec">MDL#</th>
   <td valign="top"><%=idb.mdl_no%></td>
 </tr>
 <tr>
   <th scope="row" class="spec">Molecular Wt</th>
   <td valign="top"><%=idb.mol_wt%></td>
 </tr>
 <tr>
   <th scope="row" class="spec">Formula</th>
   <td valign="top"><%=idb.formatFormula()%></td>
 </tr>
 
 <tr>
   <th scope="row" class="spec">Stock</th>
   <td valign="top"><%=idb.stock()%></td>
 </tr>
 <tr>
   <th scope="row" class="spec">Unit Price</th>
   <td valign="top">$ <%=idb.unit_price%></td>
 </tr>
 <tr>
   <th scope="row" class="spec">Storage</th>
   <td valign="top"><%=idb.storage%></td>
 </tr>
 <tr>
   <th scope="row" class="spec">Appearance</th>
   <td valign="top"><%=idb.appearance%></td>
 </tr>
 <tr>
   <th scope="row" class="spec">Cost To Make</th>
   <td valign="top"><%=idb.cost_to_make%></td>
 </tr>
 <tr>
   <th scope="row" class="spec">Invt Cost</th>
   <td valign="top"><%=idb.invt_cost%></td>
 </tr>
 <tr>
   <th scope="row" class="spec">PDT</th>
   <td valign="top"><%=idb.pdt_line%></td>
 </tr>
 <tr>
   <th scope="row" class="spec">Analytical</th>
   <td valign="top"><%=idb.analytical%></td>
 </tr>
 <tr>
   <th scope="row" class="spec">Application</th>
   <td valign="top"><%=idb.application%></td>
 </tr>
 <tr>
   <th scope="row" class="spec">References</th>
   <td valign="top"><%=idb.refs%></td>
 </tr>
 
  </table>
  </td>
  
  
  <td valign="top">
  
  <table>
  <tr><td>
  <form name="myWebForm" action="nothing" method="post">
   <input type="button" value="QC"/></td></tr>
   <tr><td>
    <tr><td> <input type="button" value="MSDS"/></td></tr>
  <tr><td> <input type="button" value="Sales" /></td></tr>
  <tr><td> <input type="button" value="Vendors"/></td></tr>
</form>
  </td>
  </tr>
  <tr>
  <td>
  Documents<br><br>
  
  <div class="css-treeview">
  

        <li><input type="checkbox" id="item-0" /><label for="item-0">QC</label>
           <ul>
               <li><a href="./">Lot st 14047-1 QC ABS test run</a></li>
               <li><a href="./">Lot st 14047-1 QC Final..</a></li>
               <li><a href="./">Lot st 14047-1 QC Pdt ...</a></li>
               <li><a href="./">Lot st 14047-1 QC Pdt Testing</a></li>
               <li><a href="./">Template QC Fluoresce...</a></li>
           </ul>
</li>

<li><input type="checkbox" id="item-1"  /><label for="item-1">C of A</label>
<ul>
               <li><a href="./">Lot st 14047-1 QC ABS test run</a></li>
               <li><a href="./">Lot st 14047-1 QC Final ..</a></li>
               <li><a href="./">Lot st 14047-1 QC Pdt ...</a></li>
               <li><a href="./">Lot st 14047-1 QC Pdt ..</a></li>
               <li><a href="./">Template QC Fluorescence test</a></li>
           </ul>
</li>

<li><input type="checkbox" id="item-2"  /><label for="item-2">MSDS</label>
<ul>
               <li><a href="./">Lot st 14047-1 QC ABS test run</a></li>
               <li><a href="./">Lot st 14047-1 QC Final Lot analysis</a></li>
               <li><a href="./">Lot st 14047-1 QC Pdt approval_reject</a></li>
               <li><a href="./">Lot st 14047-1 QC Pdt Testing</a></li>
               <li><a href="./">Template QC Fluorescence test</a></li>
           </ul>
</li>

<li><input type="checkbox" id="item-3"  /><label for="item-3">Labels</label>
<ul>
               <li><a href="./">Label lot ST 14047-1</a></li>
               <li><a href="./">Label lot ST 14047-2</a></li>
               <li><a href="./">Label lot ST 14047-3</a></li>
           </ul>
</li>
<li><input type="checkbox" id="item-4"  /><label for="item-4">Packing Slips</label>
<ul>
               <li><a href="./">Lot st 14047-1 QC ABS test run</a></li>
               <li><a href="./">Lot st 14047-1 QC Final Lot analysis</a></li>
               <li><a href="./">Lot st 14047-1 QC Pdt approval_reject</a></li>
               <li><a href="./">Lot st 14047-1 QC Pdt Testing</a></li>
               <li><a href="./">Template QC Fluorescence test</a></li>
           </ul>
</li>
<li><input type="checkbox" id="item-5"  /><label for="item-5">POs</label>
<ul>
               <li><a href="./">Lot st 14047-1 QC ABS test run</a></li>
               <li><a href="./">Lot st 14047-1 QC Final Lot analysis</a></li>
               <li><a href="./">Lot st 14047-1 QC Pdt approval_reject</a></li>
               <li><a href="./">Lot st 14047-1 QC Pdt Testing</a></li>
               <li><a href="./">Template QC Fluorescence test</a></li>
           </ul>
</li>
<li><input type="checkbox" id="item-6"  /><label for="item-6">Invoices</label>
<ul>
               <li><a href="./">Lot st 14047-1 QC ABS test run</a></li>
               <li><a href="./">Lot st 14047-1 QC Final Lot analysis</a></li>
               <li><a href="./">Lot st 14047-1 QC Pdt approval_reject</a></li>
               <li><a href="./">Lot st 14047-1 QC Pdt Testing</a></li>
               <li><a href="./">Template QC Fluorescence test</a></li>
           </ul>
</li>
        
</li>
 
    </ul>
    
    
</div>
</td>
</tr>
</table>
  </td></tr></table>
  
			