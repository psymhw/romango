package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
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
  int handicap=0;
  int loginAttempts=0;
  String message;
  boolean currentMessage=false;
  
  public String getMessage() {
	  if (currentMessage)
	  {
	    message=message.replaceAll("_", " ");
	    message=message.substring(0,message.length()-1);
		  return message;
	  }
	  else return "";
}

public void setMessage(String message) {
	this.message = message;
}

public int getHandicap() {
	return handicap;
}

public void setHandicap(int handicap) {
	this.handicap = handicap;
}

Move lastSgfMove;
  boolean loggedIn=false;
  
  public Move getLastSgfMove() {
	return lastSgfMove;
}

public void setLastSgfMove(Move lastSgfMove) {
	this.lastSgfMove = lastSgfMove;
}

public int getLastSgfMoveNumber() {
	return lastSgfMoveNumber;
}

public void setLastSgfMoveNumber(int lastSgfMoveNumber) {
	this.lastSgfMoveNumber = lastSgfMoveNumber;
}

int lastSgfMoveNumber=0;
	
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
   
    
   // cookieJar.add(dragonURI, cookie_handle);
  //  cookieJar.add(dragonURI, cookie_sessioncode);
  //  loggedIn=true;
    
   /* 
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
   
   public long checkForMove()
   {
	 String surl = "http://www.dragongoserver.net/quick_status.php?&quick_mode=1&user=" + userId;
	 
	 String secondLine="";    
	 String gameStr="";
	 StringBuffer rawMessage = new StringBuffer();
	 rawMessage.append("checkForMove: "); 
	 boolean emptyList = false;
	 long gameLong=0;
	 feedback=new StringBuffer();
	 
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
		 //System.out.println("line: " + line);
	      rawMessage.append(line+"\n");
	      count++;
	      if (count==2) secondLine=line;
	   }
	 } catch (Exception e)  {  feedback.append(e.getMessage()); return 0; }
	 
	 if (secondLine.contains("empty list")) emptyList=true;
	 
	 
	 if (emptyList)
	 {
	    feedback.append("no moves waiting"); 
	 }
	 else 
	 {	 
	   try
	   {
	     gameStr=secondLine.substring(4,11);
	   } catch ( Exception e) {feedback.append("error parsing gameStr: "); feedback.append(rawMessage); }
	   
	   if (gameStr.length()>0)
	   {
	     try { gameLong = Long.parseLong(gameStr.trim()); } catch (Exception e) {}
	     if (gameLong>0) feedback.append("Game Found: #"+gameStr.trim());
	     else feedback.append(rawMessage);
	   }
	 }
	 
	 
	 return gameLong;
   }
   
   public ArrayList <Move> getSgf(String currentGameNo)
   {
	 ArrayList <Move>sgfMoves = new ArrayList<>();
	 sgfFileString= new StringBuffer(); 
	 String surl = "http://www.dragongoserver.net/sgf.php?gid="+currentGameNo+"&owned_comments=1";
	 ArrayList <String>moveLine= new ArrayList<>();
	 Move move=null;
	 lastSgfMoveNumber=0;
	 
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
	     // System.out.println("line: " + line);
	   }
	 } catch (Throwable t)  {  t.printStackTrace(); }
	 
	 Iterator it = moveLine.iterator();
     String sgfPosition="";
     String line="";
	  
     while(it.hasNext())  // get handicap count
     {
       line=(String)it.next();
       System.out.println(line);
       if (line.startsWith("HA"))
       {
	     handicap=line.charAt(3)-48;  
         continue;
       }
		      
       if (line.startsWith("AB"))  // get handicap moves
       {
	     if (handicap>0)
	     {
	       int xPos=3;
	       int yPos=5;
	       for(int i=0; i<handicap; i++)
	       {
	         sgfPosition=  line.substring(xPos,yPos);
	         move=new Move(sgfPosition,GoClient.BLACK);
	         sgfMoves.add(move);
	         lastSgfMoveNumber++;
	         xPos+=4;
	        yPos+=4;
	       }
	     }
	     continue;
       }
	 
       if (line.startsWith(";MN"))  // get first white move
       {
	     sgfPosition = line.substring(8,10);
	     move=new Move(sgfPosition,GoClient.WHITE);
	     sgfMoves.add(move);
	     lastSgfMoveNumber++;
	     continue;
       }
       if (line.startsWith(";B"))  // black move
       {
	     sgfPosition=line.substring(3,5);
	     move=new Move(sgfPosition,GoClient.BLACK);
	     sgfMoves.add(move);
	     lastSgfMoveNumber++;
	     currentMessage=false;
	     continue;
       }
      
       if (line.startsWith(";W"))  // white move
       {
	     sgfPosition=line.substring(3,5);
	     move=new Move(sgfPosition,GoClient.WHITE);
	     sgfMoves.add(move);
	     lastSgfMoveNumber++;
	     currentMessage=false;
	     continue;
       }
       if (line.startsWith("C["))  // white move
       {
    	  message=line.substring(line.indexOf(':')+1); 
    	  currentMessage=true;
       }
     }
	   
     lastSgfMove = move;
     return sgfMoves;
   }
   
   public boolean makeMove(String gameNo, String lastMove, String thisMove, int color, String message)
   {
	 StringBuffer rawMessage = new StringBuffer();
	 String secondLine="";
     String colorString;
     boolean loginError=false;
     boolean success=false;
     
     if (!loggedIn) login(); else feedback = new StringBuffer();
    // feedback.append("Dragon move\n");
     
     if (color==GoClient.BLACK) colorString="B"; else colorString="W";
	 String surl = "http://www.dragongoserver.net/quick_play.php?gid="+gameNo+"&handle="+userId
			 +"&sgf_prev="+lastMove
			 +"&sgf_move="+thisMove
			 +"&color="+colorString;
	 
	 if (message!=null)
	 {
		 if (message.length()>0)
		 {
		   message=message.replaceAll(" ", "_");	 
		   surl=surl+"&message="+message;
		 }
	 }
	 try
	 {
	   URL url;
	   url = new URL(surl);
	           
	   URLConnection con = url.openConnection();
	   con = url.openConnection();
	   con.setDoOutput(true);  // triggers POST method
	   InputStream is = con.getInputStream();
	   InputStreamReader isr = new InputStreamReader(is);
	   BufferedReader br = new BufferedReader(isr);
	   String line = null;
	   
	  // boolean success=false;
	          
	   int count=0;
	   System.out.println("move cmd: " + surl);
	   while ( (line = br.readLine()) != null)
	   {
	      System.out.println("move line: " + line);
	      if (line.contains("#Error: not_logged_in")) loginError=true;
	      rawMessage.append(line);
	      count++;
	      if (count==2) secondLine=line;
	   }
	 } catch (Throwable t)  {  t.printStackTrace(); }
	 
	 if (secondLine.contains("Ok")) { success=true; feedback.append("move: ok\n"); }
	  else { feedback.append(rawMessage); feedback.append("\n"); }
	 
	 if (loginError)
	 {
		if (loginAttempts<2)
		{	
		  login();
		  loginAttempts++;
		  success=makeMove(gameNo, lastMove,thisMove,color, message);
		}
	 }
	 
	 return success;
   }
   
   
   public void login()
   {
	 loggedIn=true;  
	 String secondLine="";
	 StringBuffer rawMessage = new StringBuffer();
	 rawMessage.append("login(): ");
	 feedback = new StringBuffer();
	 
	// feedback.append("Dragon Login\n");
	 
	 String surl = "http://www.dragongoserver.net/login.php?quick_mode=1&userid=" + userId + "&passwd=" + password;
	 try
	 {
	   URL url;
	   url = new URL(surl);
	           
	   URLConnection con = url.openConnection();
	   Object obj = con.getContent();
	   con = url.openConnection();
	   
	   obj = con.getContent();
       cookieJar = manager.getCookieStore();
       /*
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
       InputStream is = con.getInputStream();
       InputStreamReader isr = new InputStreamReader(is);
       BufferedReader br = new BufferedReader(isr);
       String line = null;
       int count=0;
       while ( (line = br.readLine()) != null)
       {
         System.out.println("login line: " + line);
         rawMessage.append(line);
         count++;
         if (count==2) secondLine=line;
       }
	 } catch (Throwable t)  {  t.printStackTrace(); } 
       
	  if (secondLine.contains("Ok")) 
	  {	  
		  System.out.println("login appending OK");
		  feedback.append("login: ok\n");
	  }
	  else { feedback.append(rawMessage); feedback.append("\n"); }
   }
   
   public String getSgfFile()
   {
	  return sgfFileString.toString(); 
   }
   
   public String getFeedback()
   {
	  return feedback.toString(); 
   }
   
   public void testNotes()
   {
		 String surl = "http://www.dragongoserver.net/do_quicl.php?obj=game&cmd=get_notes&gid=736429";
		 try
		 {
		   URL url;
		   url = new URL(surl);
		           
		   URLConnection con = url.openConnection();
		   Object obj = con.getContent();
		   con = url.openConnection();
		   
		   obj = con.getContent();
	       cookieJar = manager.getCookieStore();
	      
	       InputStream is = con.getInputStream();
	       InputStreamReader isr = new InputStreamReader(is);
	       BufferedReader br = new BufferedReader(isr);
	       String line = null;
	     
	       while ( (line = br.readLine()) != null)
	       {
	         System.out.println("test line: " + line);
	       }
		 } catch (Throwable t)  {  t.printStackTrace(); } 
	       
		 
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
