package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet 
{
	public void service(HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException
{
  System.out.println("Test Servlet");
  ServletOutputStream out = response.getOutputStream();
  out.println("<html>");
  out.println("<head>");
  out.println("<title>Test Servlet</title>");
  out.println("<link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\" />");
  out.println("</head>");
  out.println("<body>");
  out.println("<h1>Hello World - Test Servlet</h1>");
  out.println("</body></html>");
		
}

}
