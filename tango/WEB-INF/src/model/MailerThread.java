package model;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;


public class MailerThread extends Thread
{
  String hostName, from, to, subject, msg, replyTo;
  
  public MailerThread(String from, String to, String subject, String msg, String replyTo) 
  {
    
    this.from=from;
    this.to=to;
    this.subject=subject;
    this.msg=msg;
    this.replyTo=replyTo;
  }
  
  public void run()
  {
    mailMessage(from, to, subject, msg, replyTo);
  }
  
  private boolean mailMessage(String from, String to, String subject, String msg, String replyTo) 
  {   
   
    SimpleEmail email = new SimpleEmail();
    try
    {
       email.setFrom(from);
       email.setHostName("mail.romangoshoes.com");
       email.addTo(to);
       email.setSubject(subject);
       email.setMsg(msg);
       if (replyTo!=null) email.addReplyTo(replyTo);
      email.send();
    } catch (EmailException e) {  e.printStackTrace(); return false;}
       return true;
  } 
}
