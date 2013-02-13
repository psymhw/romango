package serv;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.MailerThread;
import model.WebErrorOutput;

public class Test extends HttpServlet 
{
	private static final long serialVersionUID = 6582865859253086030L;

	public void service(HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException
    {
		try {
      MailerThread mailerThread = new MailerThread("rick@cotse.net", "rick@cotse.net", "Test", "Hello World", "rick@cotse.net");
      mailerThread.start();
      response.setContentType("text/html");
      ServletOutputStream out = response.getOutputStream();
      out.println("Mail Sent");
		} catch (Exception e) {new WebErrorOutput(e, response); return;}
      
	} 
}
