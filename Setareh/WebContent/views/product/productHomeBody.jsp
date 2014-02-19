<%@ taglib prefix="s" uri="/struts-tags" %>

<h2>Product</h2>

<s:form> 
<s:hidden name="mode" value="show" />
   <s:textfield label="Product" name="searchStr" />  <s:submit value="Find" />
</s:form>

<s:form action="new" method="get" namespace="/product">
        <s:submit value="Add Product"/>
</s:form>