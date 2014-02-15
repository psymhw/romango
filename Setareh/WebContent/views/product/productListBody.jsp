<%@ taglib prefix="s" uri="/struts-tags" %>

<h1>Matching Products</h1>

<div class="ProductListTable" >
<table>

  <th scope="col" abbr="Id" >Id</th>
  <th scope="col" abbr="Name">Name</th>	
<s:iterator value="productList">
  <tr>
    <td>
      <a href='product.action?mode=show&searchStr=<s:property value="item" />'><s:property value="item" /></a>
    </td>
    <td>
      <s:property value="product_name" />
    </td>
  </tr>
  
</s:iterator>
</table>
</div>

