MODE_1.JSP

<%

String someObject = (String)request.getAttribute("SomeObject");

if (someObject==null)
	out.println("some Object is null");
	else
  out.println(someObject);
%>