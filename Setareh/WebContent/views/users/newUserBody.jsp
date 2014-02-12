<%@ taglib prefix="s" uri="/struts-tags" %>

<h2>New User</h2>

<s:form> 
  <s:hidden name="user.id" />
  <s:hidden name="mode" value="insert" />
  <s:textfield label="Name" name="userName" />
  <s:textfield label="Password" name="userPassword"/>
  <s:submit value="Create" />
</s:form>