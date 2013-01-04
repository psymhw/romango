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
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class DragonAccess 
{
  boolean testMode=false;

  CookieManager manager; 
  CookieStore cookieJar;
  String userId;
  String password;
 // StringBuffer sgfFileString = new StringBuffer();
  StringBuffer feedback = new StringBuffer();
  String playerWhite="";
  String playerBlack="";
  String loginNameBlack;
  String loginNameWhite;
  boolean localFile=false;
  String lastMoveColor="b";
  boolean excessive_usage=false;
  ArrayList<String> comments = new ArrayList<>();
  ArrayList<String> sgfFileLine = null; 
  int lastSgfMoveNumber=0;
  int handicap=0;
  int loginAttempts=0;
  String message="";
  boolean currentMessage=false;
  Date lastMoveCheck;
  long currentGame=0;
  String liveServer="www.dragongoserver.net";
  String testServer="dragongoserver.sourceforge.net";
  String server=liveServer;
  

public long getCurrentGame() {
	return currentGame;
}

public boolean isTestMode() {
	return testMode;
}

public void setTestMode(boolean testMode) {
	this.testMode = testMode;
}

public DragonAccess(String userId, String password) 
  {
	if (testMode) server=testServer;
	manager = new CookieManager();
    manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    CookieHandler.setDefault(manager);
    cookieJar = manager.getCookieStore();
    this.userId=userId;
    this.password=password;
    
    
    HttpCookie cookie_handle = new HttpCookie("cookie_handle", userId);
    cookie_handle.setDomain(server);
    cookie_handle.setPath("/");
    cookie_handle.setSecure(false);
    
    HttpCookie cookie_sessioncode = new HttpCookie("cookie_sessioncode", "9009AB8583A63AC6A66A2C6914A98E7BFA2601C93");
    cookie_sessioncode.setDomain(server);               
    cookie_sessioncode.setPath("/");
    cookie_sessioncode.setSecure(false);
    
    URI dragonURI=null;
    
   
    try {
		dragonURI = new URI(server);
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


   
   public int checkForMove()
   {
	 return checkForMove("");
   }

   public int checkForMove(String timeStr)
   {
	 String surl = "http://"+server+"/quick_status.php?version=2&quick_mode=1&user=" + userId;
	 SimpleDateFormat df = new SimpleDateFormat("h:mm:ss MM-dd-yy");
	 
	 StringTokenizer st;
	 String token;
	 
	 String secondLine="";    
	 String gameStr="";
	 StringBuffer rawMessage = new StringBuffer();
	 
	 if (!loggedIn) return GoClient.NOT_LOGGED_IN;
	 
	 if (lastMoveCheck!=null)
	 {
	   Date now = new Date();
	   
	   long elapsedTime=now.getTime()-lastMoveCheck.getTime();
	   
	   if (elapsedTime<60000)  
	   {
		  System.out.println("elapsed since last refresh: "+elapsedTime);
		  return GoClient.MIN_RETRY_TIME;
	   }
	 }
	 lastMoveCheck= new Date();
	 
	 
	 
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
		  // System.out.println("check for move line: "+line);
		   if (line.contains("excessive_usage")) excessive_usage=true;
		   
		   if (line.startsWith("#")) { commentCount++; lineCount++; continue; };
		   if (line.startsWith("B")) { lineCount++; continue; };
		   if (line.startsWith("M")) 
		   { 
			   lineCount++; 
			   
			   if (line.contains("Game result")) 
			   {	   
				   System.out.println("Game Over");
				   return GoClient.GAME_OVER;
			   }
			   continue; 
		   };
		   
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
	 } catch (Exception e)  {  feedback.append(e.getMessage()); return GoClient.EXCEPTION; }
	
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
		  feedback.append("no moves waiting\n"); 
	   }
	   if (dataCount>0) feedback.append(dataLine+"\n");
	 
	 if (gameLong==0) 
		 return GoClient.NO_MOVE_WAITING;
	 else
	 {
		 currentGame=gameLong;
	 return GoClient.GAME_FOUND;
	 }
   }
   
   
   
   public boolean getSgf(String currentGameNo)
   {
	 sgfMoves = new ArrayList<>();
//	 sgfFileString= new StringBuffer(); 
	 String surl = "http://"+server+"/sgf.php?gid="+currentGameNo+"&owned_comments=1";
	 sgfFileLine= new ArrayList<>();
	 Move move=null;
	 lastSgfMoveNumber=0;
	 comments=new ArrayList();
	 int count=0;
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
		   if (count==0) if (!line.startsWith("(")) return false;  // if the first line is not "(" it'ds not a valid SGF
		   sgfFileLine.add(line);
		   if ("C[]".equals(line)) continue;
		   if (")".equals(line)) continue; 
		//   sgfFileString.append(line+"\n");
		   count++;
	      //System.out.println("line: " + line);
	   }
	 } catch (Throwable t)  {  t.printStackTrace(); return false; }
	 
	 Iterator it = sgfFileLine.iterator();
     String sgfPosition="";
     String line="";
     File gameFile=null;
	  File directory = new File (".");
	  
	  try{
			gameFile= new File(directory.getCanonicalPath()+"\\"+currentGameNo+".sgf");
		    FileWriter fstream=null;
		
			fstream = new FileWriter(gameFile);
		    BufferedWriter out = new BufferedWriter(fstream);
		    
		    while(it.hasNext())
		    {
		      out.write((String)it.next()+"\n");
		    }
		    out.close();
	 } catch (IOException e) { e.printStackTrace();	}
	  
	  it  = sgfFileLine.iterator();
        while(it.hasNext())  
        {
          line=(String)it.next();
          parseLine(line);
        }
	  
         
     if (sgfMoves.size()==0) return false;
     return true;
   }
   
   public ArrayList<String> getSgfFileLine() {
	return sgfFileLine;
}
   
   public long getRunningGame()
   {
	
	   long gid=0;
	
	 long uid=getUserInfo();
	 String surl = "http://"+server+"/show_games.php?uid="+uid;
	 
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
	   int pos=0;   
	   String strId="";
	   String partLine="";
	   
	   
	   while ( (line = br.readLine()) != null)
	   {
	      pos=line.indexOf("?gid=");
	      if (pos>1)
	      {
	    	partLine=line.substring(pos+5);  
	    	pos=partLine.indexOf("\"");
	    	strId=partLine.substring(0,pos);
	    	try { gid=Long.parseLong(strId); } catch (Exception e) {}
	    	break;
	      }
	      
	   }
	 } catch (Throwable t)  {  t.printStackTrace(); return 0; }
	// System.out.println("gid = "+gid);
	 
	  
	 return gid;
   }
   
   
   public long getUserInfo()
   {
	
	 String surl = "http://"+server+"/quick_do.php?obj=user&cmd=info&user="+userId;
	 long uid=0;
	 
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
	   int pos=0;   
	   String strId="";
	   String partLine="";
	   
	   
	   while ( (line = br.readLine()) != null)
	   {
	      pos=line.indexOf(",\"id\":");
	      if (pos>1)
	      {
	    	partLine=line.substring(pos+6);
	    	pos=partLine.indexOf(',');
	    	strId=partLine.substring(0,pos);
	    	try { uid=Long.parseLong(strId); } catch (Exception e) {}
	      }
	    //  System.out.println("uid = "+uid);
	   }
	 } catch (Throwable t)  {  t.printStackTrace(); return 0; }
	 
	 
	  
	 return uid;
   }
   
   public long getGameInfo(String gid)
   {
	
	 String surl = "http://"+server+"/quick_do.php?obj=game&cmd=info&lstyle=json&gid="+gid;
	 long uid=0;
	 System.out.println("call: "+surl);
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
	   int pos=0;   
	   String strId="";
	   String partLine="";
	   
	   
	   while ( (line = br.readLine()) != null)
	   {
		   System.out.println("Game Info line: "+line);
	      
	      }
	   
	 } catch (Throwable t)  {  t.printStackTrace(); return 0; }
	 
	 
	  
	 return uid;
   }
   
  
public void parseLine(String line)
   {
     Move move=null;
	 String sgfPosition="";
	 
	 try {
	 if (line.startsWith("GC["))
	 {
	   String strGameNo = line.substring(line.indexOf(':')+1).trim(); 
	   try
	   {
		   currentGame=Long.parseLong(strGameNo);
	   } catch(Exception e) { System.out.println("can't parse "+strGameNo);}
	   return;
	 }
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
		   lastSgfMoveNumber++;
		   move.setMoveNumber(lastSgfMoveNumber);
		   sgfMoves.add(move);
		   lastSgfMove = move;
		  
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
	   lastSgfMoveNumber++;
	   move.setMoveNumber(lastSgfMoveNumber);
	   sgfMoves.add(move);
	   lastSgfMove = move;
	   
	   return;
	 }
	 
	 if (line.startsWith(";W[]"))  // white pass
	 {
	   move=new Move(GoClient.WHITE); //PASS
	   sgfMoves.add(move);
	   lastSgfMove = move;
	   lastSgfMoveNumber++;
	   currentMessage=false;
	   lastMoveColor="w";
	   return;
	 }
	 if (line.startsWith(";W[tt]"))  // white pass
	 {
	   move=new Move(GoClient.WHITE); //PASS
	   sgfMoves.add(move);
	   lastSgfMove = move;
	   lastSgfMoveNumber++;
	   currentMessage=false;
	   lastMoveColor="w";
	   return;
	 }
	 if (line.startsWith(";B[]"))  // black pass
	 {
	   move=new Move(GoClient.BLACK); //PASS
	   sgfMoves.add(move);
	   lastSgfMove = move;
	   lastSgfMoveNumber++;
	   currentMessage=false;
	   lastMoveColor="b";
	   return;
	 }
	 if (line.startsWith("Result: B+Resign"))  // white resign
	 {
	   move=new Move(GoClient.WHITE); // resign
	   move.setResign(true);
	   sgfMoves.add(move);
	   lastSgfMove = move;
	   lastSgfMoveNumber++;
	   currentMessage=false;
	   lastMoveColor="w";
	   return;
	 }
	 if (line.startsWith("Result: W+Resign"))  // black resign
	 {
	   move=new Move(GoClient.BLACK); // resign
	   move.setResign(true);
	   sgfMoves.add(move);
	   lastSgfMove = move;
	   lastSgfMoveNumber++;
	   currentMessage=false;
	   lastMoveColor="b";
	   return;
	 }
	 if (line.startsWith(";B[tt]"))  // black pass
	 {
	   move=new Move(GoClient.BLACK); //PASS
	   sgfMoves.add(move);
	   lastSgfMove = move;
	   lastSgfMoveNumber++;
	   currentMessage=false;
	   lastMoveColor="b";
	   return;
	 }
	 
	 
	 if (line.startsWith(";B"))  // black move
	 {
       sgfPosition=line.substring(3,5);
	   move=new Move(sgfPosition,GoClient.BLACK);
	   lastSgfMoveNumber++;
	   move.setMoveNumber(lastSgfMoveNumber);
	   sgfMoves.add(move);
	   lastSgfMove = move;
	   
	   currentMessage=false;
	   lastMoveColor="b";
	   return;
	 }
	      
	 if (line.startsWith(";W"))  // white move
	 {
	   sgfPosition=line.substring(3,5);
	   move=new Move(sgfPosition,GoClient.WHITE);
	   lastSgfMoveNumber++;
	   move.setMoveNumber(lastSgfMoveNumber); 
	   sgfMoves.add(move);
	   lastSgfMove = move;
	   
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
	 catch (Exception e) 
	 { 
	   feedback.append("\nerror parsing sgf line: "+line+"\n");
	   return;
	 }
	       
	}
   
   public boolean getLocalSgfFile(String currentGameNo) 
   {
     File gameFile=null;
     File directory = new File (".");
     int count=0;
     //System.out.println("get local file - gameNo");
     try 
     {
   	   gameFile= new File(directory.getCanonicalPath()+"\\"+currentGameNo+".sgf");
     } catch (IOException e) { e.printStackTrace(); }

     if (gameFile!=null)
     {
   	   if (gameFile.exists())
       {
   		count=getLocalSgfFile(gameFile);
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
   
   
   public int getLocalSgfFile(File gameFile) 
   {
     boolean returnVal=false;
     int count=0;
     lastSgfMoveNumber=0;
     sgfMoves = new ArrayList<>();
     comments=new ArrayList();
     
   	     try 
   	     {
   	       InputStream in = new FileInputStream(gameFile);
   	       InputStreamReader isr = new InputStreamReader(in);
   	       BufferedReader br = new BufferedReader(isr);
   	       String line;
   	       
   	       while ((line = br.readLine()) != null) 
   	      { 
   	        parseLine(line);
   	        if (count==0) if (!line.startsWith("(")) return 0;  // if the first line is not "(" it'ds not a valid SGF
   	        count++;
   	       // System.out.println(line);
   	      }
   	       
   	       br.close();
   	       isr.close();
   	       in.close();
   	    } catch (IOException io) { System.out.println("Ooops");  }
    
    return count;
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
	 String surl = "http://"+server+"/quick_play.php?gid="+gameNo+"&handle="+userId
			 +"&sgf_prev="+lastMove
			 +"&sgf_move="+thisMove
			 +"&color="+colorString;
	 
	 if (message!=null)
	 {
		 if (message.length()>0)
		 {
		  // message=message.replaceAll(" ", "_");
		   String text="";
		   try
		   {
		   text = URLEncoder.encode(message,"UTF-8");
		   } catch (Exception e) { e.printStackTrace();}
		   surl=surl+"&message="+text;
		 }
	 }
	 try
	 {
		 System.out.println("move cmd: " + surl);
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
   
   public boolean makeMove2(String gameNo, int moveNo, String thisMove, String message)
   {
	 StringBuffer rawMessage = new StringBuffer();
	 String secondLine="";
     String colorString;
     boolean loginError=false;
     boolean success=false;
     
     if (!loggedIn) login(); else feedback = new StringBuffer();
    // feedback.append("Dragon move\n");
     
    String surl = "http://"+server+"/quick_do.php?obj=game&cmd=move&gid="+gameNo+"&move_id="+moveNo
			 +"&move="+thisMove;
	 
	 if (message!=null)
	 {
		 if (message.length()>0)
		 {
		  // message=message.replaceAll(" ", "_");
		   String text="";
		   try
		   {
		   text = URLEncoder.encode(message,"UTF-8");
		   } catch (Exception e) { e.printStackTrace();}
		   surl=surl+"&msg="+text;
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
	  //System.out.println("move2 cmd: " + surl);
	   while ( (line = br.readLine()) != null)
	   {
	     //System.out.println("move2 line: " + line);
	      if (line.contains("#Error: not_logged_in")) loginError=true;
	      rawMessage.append(line);
	      count++;
	      secondLine=line;
	   }
	 } catch (Throwable t)  {  t.printStackTrace(); }
	 
	 if (secondLine.contains(",\"error\":\"\",")) { success=true; feedback.append("move: ok"); }
	  else { feedback.append(rawMessage); feedback.append("\n"); }
	 
	 if (loginError)
	 {
		if (loginAttempts<2)
		{	
		  login();
		  loginAttempts++;
		  success=makeMove2(gameNo, moveNo ,thisMove, message);
		}
	 }
	 
	 
	 return success;
   }
   
   public boolean resign(String gameNo, int moveNo, String thisMove, String message)
   {
	 StringBuffer rawMessage = new StringBuffer();
	 String secondLine="";
     String colorString;
     boolean loginError=false;
     boolean success=false;
     
     if (!loggedIn) login(); else feedback = new StringBuffer();
    // feedback.append("Dragon move\n");
     
    String surl = "http://"+server+"/quick_do.php?obj=game&cmd=resign&gid="+gameNo+"&move_id="+moveNo;
	 
	 if (message!=null)
	 {
		 if (message.length()>0)
		 {
		  // message=message.replaceAll(" ", "_");
		   String text="";
		   try
		   {
		   text = URLEncoder.encode(message,"UTF-8");
		   } catch (Exception e) { e.printStackTrace();}
		   surl=surl+"&msg="+text;
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
	  //System.out.println("move2 cmd: " + surl);
	   while ( (line = br.readLine()) != null)
	   {
	     //System.out.println("move2 line: " + line);
	      if (line.contains("#Error: not_logged_in")) loginError=true;
	      rawMessage.append(line);
	      count++;
	      secondLine=line;
	   }
	 } catch (Throwable t)  {  t.printStackTrace(); }
	 
	 if (secondLine.contains(",\"error\":\"\",")) { success=true; feedback.append("move: ok"); }
	  else { feedback.append(rawMessage); feedback.append("\n"); }
	 
	 if (loginError)
	 {
		if (loginAttempts<2)
		{	
		  login();
		  loginAttempts++;
		  success=makeMove2(gameNo, moveNo ,thisMove, message);
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
	 
	 String surl = "http://"+server+"/login.php?quick_mode=1&userid=" + userId + "&passwd=" + password;
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
        // System.out.println("login line: " + line);
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
   
   
   
   public void testNotes(String currentGameNo)
   {
		 String surl = "http://"+server+"/quick_do.php?obj=game&cmd=list&view=status&uid="+userId;
		 		//"&gid="+currentGameNo;
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

	  
	  public String getPlayerWhite() {
			return playerWhite;
		}

		public void setPlayerWhite(String playerWhite) {
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
	
	
	   public String getFeedback()
	   {
		  return feedback.toString(); 
	   }



}
