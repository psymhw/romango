package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class DragonAccess 
{
  CookieManager manager; 
  CookieStore cookieJar;
  String userId;
  String password;
  StringBuffer sgfFileString = new StringBuffer();
  StringBuffer feedback = new StringBuffer();
  String playerWhite="";
  String playerBlack="";
  String loginNameBlack;
  String loginNameWhite;
  boolean localFile=false;
  String lastMoveColor="b";
  boolean excessive_usage=false;
  ArrayList<String> comments = new ArrayList<>();
  int lastSgfMoveNumber=0;
  int handicap=0;
  int loginAttempts=0;
  String message;
  boolean currentMessage=false;
  Date lastMoveCheck;


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

public long checkForMove2()
{
	 //String surl = "http://www.dragongoserver.net/quick_status.php?&quick_mode=1&user=" + userId;
	 String surl = "http://www.dragongoserver.net/quick_do.php?obj=game&cmd=list&view=status&lstyle=table&uid="+userId;

	 String secondLine="";    
	 String gameStr="";
	 StringBuffer rawMessage = new StringBuffer();
	 rawMessage.append("checkForMove: "); 
	 boolean emptyList = false;
	 long gameLong=0;
	 feedback=new StringBuffer();
	 System.out.println(surl);
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
		  System.out.println("check for move 2: " + line);
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

   
   public long checkForMove()
   {
	 return checkForMove("");
   }

   public long checkForMove(String timeStr)
   {
	 String surl = "http://www.dragongoserver.net/quick_status.php?version=2&quick_mode=1&user=" + userId;
	 SimpleDateFormat df = new SimpleDateFormat("h:mm:ss MM-dd-yy");
	 
	 StringTokenizer st;
	 String token;
     
	 
	 String secondLine="";    
	 String gameStr="";
	 StringBuffer rawMessage = new StringBuffer();
	 
	 if (lastMoveCheck!=null)
	 {
	   Date now = new Date();
	   if (now.getTime()<lastMoveCheck.getTime()+10000)
	   {
		   System.out.println("DragonAccess: move frequency violaion");
		   return 0;
	   }
	 }
	 
	 lastMoveCheck= new Date();
	 
	 if (!loggedIn) 
	 {	 
		 //System.out.println("DragonAccess:checkForMove("+timeStr+") not logged in");
		 return 0;
	 }
	 rawMessage.append("checkForMove: "); 
	 boolean emptyList = false;
	 long gameLong=0;
	 feedback=new StringBuffer();
	   int lineCount=0;
	   int tokenCount=0;
	   int commentCount=0;
	   int dataCount=0;
	   String dataLine="";
	   excessive_usage=false;

	 
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
		   rawMessage.append(line+"\n");
		   //System.out.println(line);
		   if (line.contains("excessive_usage")) excessive_usage=true;
		   
		   if (line.startsWith("#")) { commentCount++; lineCount++; continue; };
		   if (line.startsWith("B")) { lineCount++; continue; };
		   if (line.startsWith("M")) { lineCount++; continue; };
		   
		   dataLine=line;
		   dataCount++;
		  
		   st = new StringTokenizer(line, ",");
		   
		   tokenCount=0;
		   while (st.hasMoreTokens()) 
		   {
			 token=st.nextToken();
			// System.out.println("token: "+token);
		     if (tokenCount==1) gameStr=token;
		     tokenCount++;
		   }
	      lineCount++;
	   }
	 } catch (Exception e)  {  feedback.append(e.getMessage()); return 0; }
	 System.out.println(timeStr+" move check: "+df.format(new Date())
			 +" lines: "+lineCount
			 +" comments: "+commentCount
			 +" data: "+dataCount
			 +" excessive usage: "+excessive_usage
			 );
	 if (dataCount>0) System.out.println(dataLine);

	   if (gameStr.length()>0)
	   {
	     try { gameLong = Long.parseLong(gameStr.trim()); } catch (Exception e) {}
	     if (gameLong>0) feedback.append("Move Found, game #"+gameStr.trim());
	     else feedback.append(rawMessage);
	   }
	   else
	   {
		    feedback.append("no moves waiting"); 
	   }
	
	 
	 //System.out.println();
	 return gameLong;
   }
   
   
   
   public boolean getSgf(String currentGameNo)
   {
	 sgfMoves = new ArrayList<>();
	 sgfFileString= new StringBuffer(); 
	 String surl = "http://www.dragongoserver.net/sgf.php?gid="+currentGameNo+"&owned_comments=1";
	 ArrayList <String>moveLine= new ArrayList<>();
	 Move move=null;
	 lastSgfMoveNumber=0;
	 comments=new ArrayList();
	// String lastMoveColor="B";
	 
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
		   if ("C[]".equals(line)) continue;
		   if (")".equals(line)) continue;
		   sgfFileString.append(line+"\n");
	      //System.out.println("line: " + line);
	   }
	 } catch (Throwable t)  {  t.printStackTrace(); }
	 
	 Iterator it = moveLine.iterator();
     String sgfPosition="";
     String line="";
     File resourceFile=null;
	  File directory = new File (".");
	  
	  try{
			resourceFile= new File(directory.getCanonicalPath()+"\\"+currentGameNo+".sgf");
		    FileWriter fstream=null;
		
			fstream = new FileWriter(resourceFile);
		    BufferedWriter out = new BufferedWriter(fstream);
		    out.write(sgfFileString.toString());
		    out.close();
	 } catch (IOException e) { e.printStackTrace();	}
	  
        while(it.hasNext())  // get handicap count
        {
          line=(String)it.next();
          parseLine(line);
        }
	  
         
     if (sgfMoves.size()==0) return false;
     return true;
   }
   
   public void parseLine(String line)
   {
     Move move=null;
	 //lastSgfMoveNumber=0;
	 String sgfPosition="";
	 
	 
	 if (line.startsWith("HA"))
	 {
	   handicap=line.charAt(3)-48;  
	   return;
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
	   return;
	 }
		 
	 if (line.startsWith("PW["))  // get white player
	 {
	   int i = line.indexOf('(');
	   if (i>0) playerWhite = line.substring(3,i);
	   
	   int j=line.indexOf(')');
	   loginNameWhite=line.substring(i+1,j);
	   return;
	 }
	 if (line.startsWith("PB["))  // get black player
	 {
	   int i = line.indexOf('(');
	   if (i>0) playerBlack = line.substring(3,i);
	   int j=line.indexOf(')');
	   loginNameBlack=line.substring(i+1,j);
	   return;
	 }
	 if (line.startsWith(";MN"))  // get first white move
	 {
	   sgfPosition = line.substring(8,10);
	   move=new Move(sgfPosition,GoClient.WHITE);
	   sgfMoves.add(move);
	   lastSgfMoveNumber++;
	   return;
	 }
	 if (line.startsWith(";B"))  // black move
	 {
       sgfPosition=line.substring(3,5);
	   move=new Move(sgfPosition,GoClient.BLACK);
	   sgfMoves.add(move);
	   lastSgfMove = move;
	   lastSgfMoveNumber++;
	   currentMessage=false;
	   lastMoveColor="b";
	   return;
	 }
	      
	 if (line.startsWith(";W"))  // white move
	 {
	   sgfPosition=line.substring(3,5);
	   move=new Move(sgfPosition,GoClient.WHITE);
	   sgfMoves.add(move);
	   lastSgfMove = move;
	   lastSgfMoveNumber++;
	   currentMessage=false;
	   lastMoveColor="w";
	   return;
	 }
	 if (line.startsWith("C["))  // comment
	 {
	   if (line.startsWith("C[]")) return;
	   message=line.substring(line.indexOf(':')+1); 
	   message=message.replaceAll("_", " ");
	   message=message.substring(0,message.length()-1);
	   comments.add("* "+lastMoveColor+" "+lastSgfMoveNumber +"\t"+message );
	   currentMessage=true;
	 }
	       
	}
   
   public boolean getLocalSgfFile(String currentGameNo) 
   {
     File resourceFile=null;
     File directory = new File (".");
     boolean returnVal=false;
   //  System.out.println("trying local file");
     int count=0;
     lastSgfMoveNumber=0;
     sgfMoves = new ArrayList<>();
     try 
     {
   	   resourceFile= new File(directory.getCanonicalPath()+"\\"+currentGameNo+".sgf");
     } catch (IOException e) { e.printStackTrace(); }

     if (resourceFile!=null)
     {
   	   if (resourceFile.exists())
       {
   	     try 
   	     {
   	       InputStream in = new FileInputStream(resourceFile);
   	       InputStreamReader isr = new InputStreamReader(in);
   	       BufferedReader br = new BufferedReader(isr);
   	       String line;
   	       
   	       while ((line = br.readLine()) != null) 
   	      { 
   	        parseLine(line);
   	        count++;
   	       // System.out.println(line);
   	      }
   	    } catch (IOException io) { System.out.println("Ooops");  }
      }
    }
     
    if (count>0) 
    {
      feedback.append("SGF file loaded from local copy.\n"); 
      localFile=true;
      //System.out.println("local file loaded");
      return true; 
    }
    else return false;
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
	 //  System.out.println("move cmd: " + surl);
	   while ( (line = br.readLine()) != null)
	   {
	    //  System.out.println("move line: " + line);
	      if (line.contains("#Error: not_logged_in")) loginError=true;
	      rawMessage.append(line);
	      count++;
	      if (count==2) secondLine=line;
	   }
	 } catch (Throwable t)  {  t.printStackTrace(); }
	 
	 if (secondLine.contains("Ok")) { success=true; feedback.append("move: ok"); }
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
   
   
   public boolean login()
   {
	 loggedIn=false;  
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
         //System.out.println("login line: " + line);
         rawMessage.append(line);
         count++;
         if (count==2) secondLine=line;
       }
	 } catch (Throwable t)  { return false; } 
       
	  if (secondLine.contains("Ok")) 
	  {	  
		  feedback.append("login: ok\n");
	  }
	  else { feedback.append(rawMessage); feedback.append("\n"); }
	  
	  loggedIn=true;
	  return true;
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
   public boolean isLocalFile() {
		return localFile;
	}


	public void setLocalFile(boolean localFile) {
		this.localFile = localFile;
	}


	  
	  
	  public ArrayList<Move> getSgfMoves() {
		return sgfMoves;
	}

	ArrayList <Move>sgfMoves = new ArrayList<>();
	  
	  public String getPlayerBlack() {
			return playerBlack;
		}


		public void setPlayerBlack(String playerBlack) {
			this.playerBlack = playerBlack;
		}
	  
	  public boolean isExcessive_usage() {
			return excessive_usage;
		}


		public void setExcessive_usage(boolean excessive_usage) {
			this.excessive_usage = excessive_usage;
		}


	public String getMessage() 
	  {
		  if (currentMessage) return message;
		 
		  else return "";
	    }

	  
	  public String getplayerWhite() {
			return playerWhite;
		}

		public void setplayerWhite(String playerWhite) {
			this.playerWhite = playerWhite;
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
	  
	  public boolean isLoggedIn() {
		return loggedIn;
	}


	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}


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

	public ArrayList<String> getComments() {
		return comments;
	}


	public void setComments(ArrayList comments) {
		this.comments = comments;
	}



	  public String getLoginNameBlack() {
		return loginNameBlack;
	}


	public void setLoginNameBlack(String loginNameBlack) {
		this.loginNameBlack = loginNameBlack;
	}


	public String getLoginNameWhite() {
		return loginNameWhite;
	}


	public void setLoginNameWhite(String loginNameWhite) {
		this.loginNameWhite = loginNameWhite;
	}
	
	public String getSgfFile()
	   {
		  return sgfFileString.toString(); 
	   }
	   
	   public String getFeedback()
	   {
		  return feedback.toString(); 
	   }



}
