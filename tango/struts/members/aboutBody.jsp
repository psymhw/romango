<%@ page language="java" %>
<%@ page import="db.MemberDb" %>
<%
String message  = (String)request.getAttribute("Message");
if (message!=null) out.println("<br>Message: <font color='red' size='+1'>"+message+"</font><br><br>");
MemberDb userData2 = (MemberDb)session.getAttribute("UserData");
%>

<h2>About Membership</h2>
<br>
<% 
             if (userData2==null)
             { %>
           <a href="<%=request.getContextPath()%>/struts/login/loginMain.jsp">Sign in</a><br><br>
              <%
              }
              else
              {
              %>
              <a href="<%=request.getContextPath()%>/login.do">Sign out <%=userData2.getUsername() %></a><br>
              <a href="<%=request.getContextPath()%>/events.do?mode=myevents">My Events </a><br><br>
			 <% } %>
			 
If you use eugenetango.com to see what's happening in the Eugene tango community (and beyond), there is no need to be a member.
There are a few other reasons why you might like to be a member:<br><br>

<b>Tango Instructor:</b> If you are a local tango instructor you may list a profile and photo in our Instructors section. 
Instructors are listed alphabetically by last name. When you are logged in, you can edit your own profile and photo! The
Instructor section is currently reserved for only Eugene area teachers.
<br><br>
<b>Event Host:</b> If you host tango events such as milongas, practicas, classes or special events, as a host member 
you may list your event. Events may have an expiration date so they will automatically disappear from the event list
when no longer needed. As a host, you have complete control over any events you post. You may edit or delete them.
They will also be retained after their expiration date should you wish to reactivate them. Foe example, if you
have an irregular special event and don't need a whole new entry each time you wish to post it.
<br><br>
Event listing is restricted to the Eugene area. Events
will be listed in the order: special events, (regular) milongas, practilongas, practicas and classes. 
<br><br>
In most cases, an instructor would probably also be a host but this is not required. 
<br><br>
<!--
<b>Gallery Poster:</b> If you are a tango photographer, a simple membership will allow you to post tango photos. This
will also be restricted to local Eugene photographers. As a gallery poster, you will have control over your photos and may 
add or remove them at your discretion.
<br><br>
-->
Membership is free. You need only e-mail me at <a href="mailto:tango@rick.cotse.net">tango@rick.cotse.net</a> and
I will set you up. I just need to verify that you are an actual local instructor, host or local photographer. This
is not a high security operation. I just don't want to open the site up to people that will post spam we don't want to see.