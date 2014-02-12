<%@ taglib prefix="s" uri="/struts-tags" %>

<h1>Authorized Users</h1>
<table id="mytable">

	<th scope="col" abbr="Id" class="nobg">Id</th>
  <th scope="col" abbr="Name">Name</th>	
   <th scope="col" abbr="Password">Password</th>			

<!--  
<tr>
  <td><b>ID</b></td>
  <td><b>Name</b></td>
  <td><b>Password</b></td>
</tr>
-->

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

