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
  private static int currentDirTotal=0;
  private static int currentDirNotFound=0;
  private static List<DirectoryInfo> dirList = new ArrayList<DirectoryInfo>();
  private static List<String> trackList = new ArrayList<String>();
  public boolean running=false;
  
  public static List<DirectoryInfo> getDirList() 
  {
	return dirList;
  }
  
  public static List<String> getTrackList() 
  {
	return trackList;
  }

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
	trackList = new ArrayList<String>();
	running=true;
	
	FileVisitor<Path> fileProcessor = new ProcessFile();
	try 
	{
	  Files.walkFileTree(Paths.get(root), fileProcessor);
    } catch (IOException e) { e.printStackTrace();	}
	
	// catches the last one
	dirList.add(new DirectoryInfo(currentDirectory, currentDirTotal, currentDirNotFound));
	  
	  //running=false;
	resultArea.appendText("\nTotal Count: " +counter+"\n");
	resultArea.appendText("Not Found Count: " +notFoundCount+"\n\n");
	 
	textArea.setText("Done");
	for (DirectoryInfo di : dirList)
	{
	  if (di.notFound>0)
	  {	  
	    resultArea.appendText("Directory"+di.path+"\n");
	    resultArea.appendText(" Files: "+di.total+" ");
	    resultArea.appendText(" Not Found in Db: "+di.notFound+"\n");
	  }
	}
	
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
	      trackList.add(aFile.toString());
	      counter++;
	      currentDirTotal++;
	      if (!Db.trackExists(pathHash)) 
	      {
	    	notFoundCount++;  
	    	currentDirNotFound++;
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
    	dirList.add(new DirectoryInfo(currentDirectory, currentDirTotal, currentDirNotFound));
    	currentDirNotFound=0;
    	currentDirTotal=0;
    	currentDirectory=aDir.toString();
      }
      else currentDirectory=aDir.toString();
    	  
    	  
	  return FileVisitResult.CONTINUE;
	}
  }
}
