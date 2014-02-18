<%@ taglib prefix="s" uri="/struts-tags" %>
 
 <s:hidden    name="product.id"/>
  <s:hidden    name="product.item"/>
  <s:hidden    name="mode"/>
  <s:label     name="product.item"         label="Item Key"/>
  <s:textarea  name="product.product_name" label="Name"             maxlength="700" rows="4" cols="90" wrap="true"/>
  <s:textfield name="product.unit_size"    label="Unit Size"        maxlength="45" size="10"/>
  <s:textfield name="product.qty"          label="Stock"            maxlength="20" size="10"/>
  <s:textfield name="product.unit_price"   label="Unit Price $"     maxlength="20" size="10"/>
  <s:textfield name="product.storage_prod" label="Storage"          maxlength="45" size="10"/>
  <s:textfield name="product.cas"          label="CAS#"             maxlength="45" size="10"/>
  <s:textfield name="product.appearance"   label="Appearance"       maxlength="45" size="45"/>
  <s:textfield name="product.cost_to_make" label="Cost To Make $"   maxlength="20" size="10"/>
  <s:textfield name="product.cost_invt"    label="Inventory Cost $" maxlength="20" size="10"/>
  <s:textfield name="product.pdt_line"     label="PDT"              maxlength="200" size="45"/>
  <s:textfield name="product.analytical"   label="Anayltical"       maxlength="200" size="100"/>
  <s:textarea  name="product.application"  label="Application"      maxlength="400" rows="4" cols="90" wrap="true"/>
  <s:textarea  name="product.refs"         label="References"       maxlength="1050" rows="4" cols="90" wrap="true"/>
