<%@ taglib prefix="s" uri="/struts-tags" %>

<h2>Inventory</h2>

<s:form> 
<s:hidden name="mode" value="show" />
   <s:textfield label="Product Number" name="item" />  <s:submit value="Find" />
</s:form>