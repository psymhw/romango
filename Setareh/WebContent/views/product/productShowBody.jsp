<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="ProductListTable" >
<table>
  
  <tr>
    <td><b>Item: </b></td> <td><s:property value="product.item" /></td>
  </tr>
  <tr>
    <td><b>Stock: </b></td> <td> <s:property value="product.qty" /> x <s:property value="product.unit_size" /></td>
  </tr>
  
  <tr>
    <td><b>Price: </b></td> <td> $<s:property value="product.unit_price" /> / <s:property value="product.unit_size" /></td>
  </tr>
  <tr>
    <td><b>Name: </b></td><td><s:property value="product.product_name" /></td>  
  </tr>
  
   <tr>
    <td><b>Formula/Mwt: </b></td> <td> <s:property value="product.mol_formula" />  <s:property value="product.mwt" /></td>
  </tr>
  
  <tr>
    <td><b>Storage: </b></td><td><s:property value="product.storage_prod" /></td>  
  </tr>
  <tr>
    <td><b>Appearance: </b></td><td><s:property value="product.appearance" /></td>  
  </tr>
  <tr>
    <td><b>Cost to Make: </b></td><td><s:property value="product.cost_to_make" /></td>  
  </tr>
  <tr>
    <td><b>Inventory Cost: </b></td><td><s:property value="product.cost_invt" /></td>  
  </tr>
  <tr>
    <td><b>PDT: </b></td><td><s:property value="product.pdt_line" /></td>  
  </tr>
   <tr>
    <td><b>Analytical: </b></td><td><s:property value="product.analytical" /></td>  
  </tr>
   <tr>
    <td><b>Applications: </b></td><td><s:property value="product.application" /></td>  
  </tr>
   <tr>
    <td><b>References: </b></td><td><s:property value="product.refs" /></td>  
  </tr>
    </table>
    </div>