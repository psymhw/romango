<%@ taglib prefix="s" uri="/struts-tags" %>

<h2>Edit User</h2>

<a href='UserAction.action?mode=delete&id=<s:property value="id" />'>Delete User</a><br/><br/>

<s:form> 
  <s:hidden name="user.id" />
  <s:hidden name="mode" value="update" />
  <s:textfield label="Name" name="user.name" />
  <s:textfield label="Password" name="user.password"/>
  <s:submit value="Update" />
</s:form>