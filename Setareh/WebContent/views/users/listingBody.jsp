<%@ taglib prefix="s" uri="/struts-tags" %>

<h1>Authorized Users</h1>

<div class="listTableStyle" >
<table>

 
<tr>
  <td><b>ID</b></td>
  <td><b>Name</b></td>
  <td><b>Password</b></td>
</tr>


<a href='UserAction.action?mode=new'>New User</a><br/><br/>

<s:iterator value="users">
  <tr>
    <td>
      <a href='UserAction.action?mode=edit&id=<s:property value="id" />'><s:property value="id" /></a>
    </td>
    <td>
      <s:property value="name" />
    </td>
    <td>
      <s:property value="password" />
    </td>
  </tr>
  
</s:iterator>
</table>
</div>

