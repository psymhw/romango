<%@ page language="java" %>
<%@ page import="db.MemberDb" %>
<%@ page import="model.Settings" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.apache.struts.util.LabelValueBean" %>
<% 
MemberDb mdb = (MemberDb)request.getAttribute("MemberDb");
  if (mdb==null) { out.println("memberEdit.jsp: mdb is null"); return; } 
  
  pageContext.setAttribute("mdb", mdb);
  Settings settings = new Settings();
  ArrayList areas = settings.areas;
  LabelValueBean lvb=null;
  %>
  <table>
  <tr>
     <td align="right"><b>Member ID: </b></td>
     <td><%=mdb.getId() %></td>
  </tr>
   <tr>
     <td align="right"><b>Username: </b></td>
     <td><%=mdb.getUsername() %></td>
  </tr>
   <tr>
     <td align="right"><b>First Name: </b></td>
     <td><%=mdb.getFirst_name() %></td>
  </tr>
   <tr>
     <td align="right"><b>Last Name: </b></td>
     <td><%=mdb.getLast_name() %></td>
  </tr>
   <tr>
     <td align="right"><b>E-mail: </b></td>
     <td><%=mdb.getEmail() %></td>
  </tr>
  <tr>
     <td align="right"><b>Password: </b></td>
     <td><%=mdb.getPassword() %></td>
  </tr>
  <tr>
     <td align="right"><b>Location: </b></td>
     <td><%=mdb.getLocationText() %></td>
  </tr>
  <tr>
     <td align="right"><b>Active: </b></td>
     <td><%=mdb.getActiveChecked() %></td>
  </tr>
  <tr>
     <td align="right"><b>Admin: </b></td>
     <td><%=mdb.getAdminChecked() %></td>
  </tr>
  <tr>
     <td align="right"><b>Host: </b></td>
     <td><%=mdb.getHostChecked() %></td>
  </tr>
  <tr>
     <td align="right"><b>Instructor: </b></td>
     <td><%=mdb.getInstructorChecked() %></td>
  </tr>
  </table>
  <a href="<%=request.getContextPath()%>/admin/members.do?mode=edit&id=<%=mdb.getId()%>">edit</a>
  <br><br>
  <table width="100%" border="1">
  <tr><td valign="top">
  <%
  if (mdb.isPhotoPresent())
  {  
  %>
  <a href="<%=request.getContextPath()%>/photo/fullPhoto.jsp?mode=member&id=<%=mdb.getId()%>&caption=<%=mdb.getCaption()%>">
 <img src="<%=request.getContextPath()%>/imageReader?mode=member&id=<%=mdb.getId()%>" alt="member Photo" width="200"/><br>
 </a>
 <b><%=mdb.getCaption() %></b><br><br>
 <%} else { %>
 No Photo
 <%} %>
 </td>
 <td valign="top"><%=mdb.getDescription() %></td>
 </tr>
 </table>
 <hr><br>
 
    <h1>Add or replace photo</h1>

    <form action="<%=request.getContextPath()%>/imageUpload" method="post" enctype="multipart/form-data">
    <input type="hidden" name="mode" value="adminMember" />
    <input type="hidden" name="id" value="<%=mdb.getId()%>" />
      <p><br/>
      <table>
      <tr>
        <td align="right"><b>Choose a photo: </b></td>
       <td><input name="myFile" type="file"/>  (JPEG only)</td>
       </tr>
       <tr>
        <td align="right"><b>Photo Caption: </b></td>
       <td><input name="caption" type="text"/></td>
       </tr>
       </table>
      </p>

      <p>
        <input type="submit" value="upload photo"/>
       
      </p>
    </form>
  