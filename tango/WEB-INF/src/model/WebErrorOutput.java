package model;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class WebErrorOutput 
{
   StackTraceElement[] errors = null;
   StringBuffer sb = new StringBuffer();
   public WebErrorOutput(Exception e)
   {
	   
	   errors = e.getStackTrace(); 
	   try {
		   	if (errors.length>0)
		       {
		    	  sb.append(e.getMessage().toString()+"<br>");
		         for(int i=0; i<errors.length; i++) { 	sb.append(errors[i].toString()+"<br>");  }
		       }
		       else sb.append("Errors length is 0");
		       } catch (Exception ex) { ex.printStackTrace(); }
	   
   }
   
   public String getErrors()
   {
	  return sb.toString();
   }
   public WebErrorOutput(Exception e, ServletOutputStream out)
   {
	   e.printStackTrace();
	   errors = e.getStackTrace(); 
       try {
   	   if (errors.length>0)
       {
    	  //out.println(e.getMessage().toString()+"<br>");
         for(int i=0; i<errors.length; i++) { 	out.println(errors[i].toString()+"<br>");  }
       }
       else out.println("Errors length is 0");
       } catch (Exception ex) { ex.printStackTrace(); }
   }
   
   public WebErrorOutput(Exception e, HttpServletResponse response)
   {
	   try {
	   response.setContentType("text/html");
	      ServletOutputStream out = response.getOutputStream();

	      response.setContentType("text/html");
	   e.printStackTrace();
	   errors = e.getStackTrace(); 
      
   	   if (errors.length>0)
       {
    	  //out.println(e.getMessage().toString()+"<br>");
         for(int i=0; i<errors.length; i++) { 	out.println(errors[i].toString()+"<br>");  }
       }
       else out.println("Errors length is 0");
       } catch (Exception ex) { ex.printStackTrace(); }
   }
}
