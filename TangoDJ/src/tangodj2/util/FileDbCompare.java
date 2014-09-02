package tangodj2.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TextArea;
import tangodj2.Db;
import tangodj2.Hasher;
import tangodj2.Preferences;

public class FileDbCompare 
{
  String tangoDir;
  String otherDir;
  static int counter=0;
  private static Hasher hasher = new Hasher();
  private static StringBuffer sb = new StringBuffer();
  private static TextArea textArea;
  private static TextArea resultArea;
  boolean tangoSelected=true;
  private static int notFoundCount=0;
  private static String currentDirectory=null;
  private static List<DirectoryInfo> dirList = new ArrayList<DirectoryInfo>();
  
  
  public FileDbCompare(Preferences preferences, TextArea textArea, TextArea resultArea)
  {
     tangoDir=preferences.tangoFolder;
     otherDir=preferences.cleanupFolder;
     this.textArea=textArea;
     this.resultArea=resultArea;
  }
  
  public void selectRoot(boolean tangoSelected)
  {
	 this.tangoSelected=tangoSelected;
  }
  
  public void start()
  {
	String root;
	if (tangoSelected) root=tangoDir;
	else root = otherDir;
	
	textArea.setText("");
	resultArea.setText("");
	notFoundCount=0;
	counter=0;
	currentDirectory=null;
	
	FileVisitor<Path> fileProcessor = new ProcessFile();
	try 
	{
	  Files.walkFileTree(Paths.get(root), fileProcessor);
    } catch (IOException e) { e.printStackTrace();	}
	
	// catches the last one
	DirectoryInfo directoryInfo = new DirectoryInfo();
	  directoryInfo.path=currentDirectory;
	  directoryInfo.totalFileCount=counter;
	  directoryInfo.notFoundCount=notFoundCount;
	  dirList.add(directoryInfo);
	
	for (DirectoryInfo di : dirList)
	{
	  textArea.appendText(directoryInfo.path+"\n");
	  textArea.appendText("Total Files: "+di.totalFileCount+"\n");
	  textArea.appendText("Not Found in Db: "+di.notFoundCount+"\n");
	}
	
	textArea.appendText("\nTotal Count: " +counter+"\n");
	resultArea.appendText("\nNot Found Count: " +notFoundCount+"\n");
  }
  
  private static final class ProcessFile extends SimpleFileVisitor<Path> 
  {
	public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs) throws IOException 
	{
	  String pathStr = aFile.toString().trim().toLowerCase();	
	  if (pathStr.endsWith(".mp3")) 
	  {
	    if  (!pathStr.contains("._"))
	    {
	      String pathStr2=aFile.toString();
	      String pathHash = hasher.getMd5Hash(pathStr2.getBytes());
	      //sb.append(aFile+"\n");
	      counter++;
	      if (!Db.trackExists(pathHash)) 
	      {
	    	notFoundCount++;  
	      }
	    }
	  }
	  
	  
	  counter++;
	  return FileVisitResult.CONTINUE;
	}
	    
	public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException 
	{
      if (currentDirectory!=null)
      {
    	  DirectoryInfo directoryInfo = new DirectoryInfo();
    	  directoryInfo.path=currentDirectory;
    	  directoryInfo.totalFileCount=counter;
    	  directoryInfo.notFoundCount=notFoundCount;
    	  dirList.add(directoryInfo);
    	  notFoundCount=0;
    	  counter=0;
    	  currentDirectory=aDir.toString();
      }
      else currentDirectory=aDir.toString();
    	  
    	  
	  return FileVisitResult.CONTINUE;
	}
  }
}
