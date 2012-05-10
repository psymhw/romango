package main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

public class DragonAccess 
{

   public DragonAccess() 
   {
	   
   }
   
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
   

}
