<%@ taglib prefix="s" uri="/struts-tags" %>

<h2>Product Edit</h2>

<div class="mainTableStyle">

<s:form action="update" method="post" namespace="/product">
<s:hidden    name="product.id"/>
  <s:hidden    name="product.item"/>
<s:label     name="product.item"         label="Item Key"/>
<%@include file="formFields.jsp" %>
<s:submit    value="UPDATE"/>
</s:form>

 </div>