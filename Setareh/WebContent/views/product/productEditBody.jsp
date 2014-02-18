<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="mode.equals('edit')">
   <h2>Product Edit</h2>
</s:if>
<s:else>
  <h2> New Product</h2>
</s:else>

<div class="mainTableStyle">

<s:form action="update" method="post" namespace="/product">

<%@include file="formFields.jsp" %>

   <s:submit    value="UPDATE"/>
</s:form>

 </div>