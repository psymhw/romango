<%@ taglib prefix="s" uri="/struts-tags" %>

<h2>Inventory</h2>

PRODUCT

<table id="mytable">

	<th scope="col" abbr="Id" class="nobg">Id</th>
  <th scope="col" abbr="Name">Name</th>	
  
  <tr>
    <td>
      <s:property value="product.item" />
    </td>
    <td>
      <s:property value="product.product_name" />
    </td>
    </tr>
    </table>