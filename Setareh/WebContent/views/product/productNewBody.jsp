<%@ taglib prefix="s" uri="/struts-tags" %>

<h2>New Product</h2>

<div class="mainTableStyle">

<s:form action="insert" method="post" namespace="/product">
<s:textfield  name="product.item" label="Item Key"   maxlength="10" />
<%@include file="formFields.jsp" %>
<s:submit    value="ADD PRODUCT"/>
</s:form>

 </div>