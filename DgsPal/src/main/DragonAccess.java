package main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DragonAccess 
{
  CookieManager manager; 
  CookieStore cookieJar;
  String userId;
  String password;
  StringBuffer sgfFileString = new StringBuffer();
  StringBuffer feedback = new StringBuffer();
	
  public DragonAccess(String userId, String password) 
  {
	manager = new CookieManager();
    manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    CookieHandler.setDefault(manager);
    cookieJar = manager.getCookieStore();
    this.userId=userId;
    this.password=password;
    
    HttpCookie cookie_handle = new HttpCookie("cookie_handle", userId);
    cookie_handle.setDomain("www.dragongoserver.net");
    cookie_handle.setPath("/");
    cookie_handle.setSecure(false);
    
    HttpCookie cookie_sessioncode = new HttpCookie("cookie_sessioncode", "9009AB8583A63AC6A66A2C6914A98E7BFA2601C93");
    cookie_sessioncode.setDomain("www.dragongoserver.net");
    cookie_sessioncode.setPath("/");
    cookie_sessioncode.setSecure(false);
    
   
    
    URI dragonURI=null;
    
   
    try {
		dragonURI = new URI("www.dragongoserver.net");
	} catch (URISyntaxException e) {e.printStackTrace();}
   
   /* 
    cookieJar.add(dragonURI, cookie_handle);
    cookieJar.add(dragonURI, cookie_sessioncode);
    
    List cookies = cookieJar.getCookies();
    System.out.println("cookies: "+cookies.size());
    Iterator it = cookies.iterator();
    HttpCookie cookie;
    while(it.hasNext()) 
    {
      cookie = (HttpCookie)it.next();
      System.out.println("Cookie: "+ cookie.getName()
     		 +" value: "+cookie.getValue()
     		 +" secure: "+cookie.getSecure()
     		 +" domain: "+cookie.getDomain()
     		 +" path: "+cookie.getPath()
     		 );
    }
    */
  }
   
   public String getStatus()
   {
	 String surl = "http://www.dragongoserver.net/quick_status.php?&quick_mode=1&user=" + userId;
	 String returnVal="error";
	  String gameLine="";    
	   String gameStr="";
	 try
	 {
	   URL url;
	   url = new URL(surl);
	           
	   URLConnection con = url.openConnection();
	   con = url.openConnection();
	   InputStream is = con.getInputStream();
	   InputStreamReader isr = new InputStreamReader(is);
	   BufferedReader br = new BufferedReader(isr);
	   String line = null;
	 
	   
	   int count=0;
	   while ( (line = br.readLine()) != null)
	   {
	      System.out.println("line: " + line);
	     
	      count++;
	      if (count==2) gameLine=line;
	      
	   }
	 } catch (Throwable t)  {  t.printStackTrace(); }
	 
	 gameStr=gameLine.substring(4,11);
	
	 long gameLong=0;
	 try {
	  gameLong = Long.parseLong(gameStr.trim());
	 } catch (Exception e) {}
	 
	 if (gameLong>0) returnVal=gameStr.trim();
	 
	 feedback=new StringBuffer();
	 feedback.append("DGS Status: "+returnVal+"\n");
	 return returnVal;
   }
   
   public ArrayList <String> getSgf(String currentGameNo)
   {
	 sgfFileString= new StringBuffer(); 
	 String surl = "http://www.dragongoserver.net/sgf.php?gid="+currentGameNo+"&quick_mode=1";
	 ArrayList <String>moveLine= new ArrayList<>();;
	 try
	 {
	   URL url;
	   url = new URL(surl);
	           
	   URLConnection con = url.openConnection();
	   con = url.openConnection();
	   InputStream is = con.getInputStream();
	   InputStreamReader isr = new InputStreamReader(is);
	   BufferedReader br = new BufferedReader(isr);
	   String line = null;
	          
	   while ( (line = br.readLine()) != null)
	   {
		   moveLine.add(line);
		   sgfFileString.append(line+"\n");
	      System.out.println("line: " + line);
	   }
	 } catch (Throwable t)  {  t.printStackTrace(); }
	 
	 return moveLine;
   }
   
   public void makeMove(String lastMove, String thisMove, int color)
   {
     String colorString;
     
     if (color==GoClient.BLACK) colorString="B"; else colorString="W";
	 String surl = "http://www.dragongoserver.net/quick_play.php?gid=735095&handle="+userId
			 +"&sgf_prev="+lastMove
			 +"&sgf_move="+thisMove
			 +"&color="+colorString;
	 try
	 {
	   URL url;
	   url = new URL(surl);
	           
	   URLConnection con = url.openConnection();
	   con = url.openConnection();
	   InputStream is = con.getInputStream();
	   InputStreamReader isr = new InputStreamReader(is);
	   BufferedReader br = new BufferedReader(isr);
	   String line = null;
	          
	   while ( (line = br.readLine()) != null)
	   {
	      System.out.println("line: " + line);
	   }
	 } catch (Throwable t)  {  t.printStackTrace(); }
   }
   
   
   public void login()
   {
	 String surl = "http://www.dragongoserver.net/login.php?quick_mode=1&userid=" + userId + "&passwd=" + password;
	 try
	 {
	   URL url;
	   url = new URL(surl);
	           
	   URLConnection con = url.openConnection();
	   Object obj = con.getContent();
	   con = url.openConnection();
	   obj = con.getContent();
       CookieStore cookieJar = manager.getCookieStore();
       List cookies = cookieJar.getCookies();
       System.out.println("cookies: "+cookies.size());
       Iterator it = cookies.iterator();
       HttpCookie cookie;
       while(it.hasNext()) 
       {
         cookie = (HttpCookie)it.next();
         System.out.println("Cookie: "+ cookie.getName()
        		 +" value: "+cookie.getValue()
        		 +" secure: "+cookie.getSecure()
        		 +" domain: "+cookie.getDomain()
        		 +" path: "+cookie.getPath()
        		 );
       }
      
       InputStream is = con.getInputStream();
       InputStreamReader isr = new InputStreamReader(is);
       BufferedReader br = new BufferedReader(isr);
       String line = null;
       while ( (line = br.readLine()) != null)
       {
           System.out.println("line: " + line);
       }
	 } catch (Throwable t)  {  t.printStackTrace(); } 
       
   }
   
   public String getSgfFile()
   {
	  return sgfFileString.toString(); 
   }
   
   public String getFeedback()
   {
	  return feedback.toString(); 
   }
   
   /*
   public void Stuff()
   {
	   // get an HTTP connection to POST to
   	String username="romango", password="smegma";
   	//String loginUrl = "http://www.dragongoserver.net/login.php";
   	//String redirectUrl = "http://www.dragongoserver.net/wap/status.php";
   //	String getSgf = "http://www.dragongoserver.net/sgf.php?gid=732698&quick_mode=1&userid=" + username + "&passwd=" + password;
   	String getSgf = "http://www.dragongoserver.net/sgf.php?gid=732698&quick_mode=1";
       
   	String getStatus = "http://www.dragongoserver.net/quick_status.php?&quick_mode=1&user=" + username + "&passwd=" + password;
   	String makePlay="http://www.dragongoserver.net/quick_play.php?gid=732698&handle=romango&sgf_prev=en&sgf_move=ef&color=B";
       String login="http://www.dragongoserver.net/login.php?quick_mode=1&userid=" + username + "&passwd=" + password;
   	try
       {
           // LOGIN
           String surl = login;
           URL url = new URL(surl);
           
           CookieManager manager = new CookieManager();
           manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
           CookieHandler.setDefault(manager);
         
           URLConnection con = url.openConnection();
           Object obj = con.getContent();
          
           con= url.openConnection();
           obj = con.getContent();
           CookieStore cookieJar = manager.getCookieStore();
           List cookies = cookieJar.getCookies();
           System.out.println("cookies: "+cookies.size());
           Iterator it = cookies.iterator();
           HttpCookie cookie;
           while(it.hasNext()) 
           {
             cookie = (HttpCookie)it.next();
             System.out.println("Cookie: "+ cookie.getName()+" value: "+cookie.getValue());
           }
          
           InputStream is = con.getInputStream();
           InputStreamReader isr = new InputStreamReader(is);
           BufferedReader br = new BufferedReader(isr);
           String line = null;
           while ( (line = br.readLine()) != null)
           {
               System.out.println("line: " + line);
           }
           
           
           // MAKE PLAY
           surl = makePlay;
           url = new URL(surl);
           
           con = url.openConnection();
           is = con.getInputStream();
           isr = new InputStreamReader(is);
           br = new BufferedReader(isr);
           
           line = null;
           while ( (line = br.readLine()) != null)
           {
               System.out.println("line: " + line);
           }
          
       } catch (Throwable t)  {  t.printStackTrace(); }
   }
   */

}
