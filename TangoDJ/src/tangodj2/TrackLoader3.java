package tangodj2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3v1;
import org.farng.mp3.id3.AbstractID3v2;

import tangodj2.cleanup.CleanupTable;
import tangodj2.tango.TangoTable;




public class TrackLoader3 
{
//  String inPath="C:\\music\\tango\\Di Sarli, Carlos";
  static int fileCounter=0;
  static int counter2=0;
  ArrayList<File> fileList;
  ArrayList<ProblemFile> problemFileList;
  private static List<TrackMeta> trackInfo = new ArrayList<TrackMeta>();
  
  private static List<TrackMeta> finalTrackInfo = new ArrayList<TrackMeta>();
  private static Hasher hasher = new Hasher();
  int errors=0;
  BufferedWriter out;
  private int trackCount=0;
  private int trackSize=0;
 
  ArrayList<String> al;
  static int players=0;
  int trackInfoSizeInt=0;
  Label trackInfoSize = new Label("0");
  int finalTrackInfoSizeInt=0;
  Label finalTrackInfoSize = new Label("0");
  public static final String DRIVER2 ="org.apache.derby.jdbc.EmbeddedDriver";
  public static final String JDBC_URL2 ="jdbc:derby:tango_db;create=false";
  static Connection connection;
  private boolean isTango=true;
  
  TangoTable tangoTable;
  CleanupTable cleanupTable;
  
  public TrackLoader3() { }
  
  public void process(String inPath, boolean singleFile, boolean isTango) throws Exception
  {
     this.isTango=isTango;
      fileCounter=0;
     File outFile = new File("out.txt");
     if (outFile.exists()) outFile.delete();
     FileWriter fstream = new FileWriter("out.txt");
     out = new BufferedWriter(fstream);
     fileList = new ArrayList<File>();
     problemFileList = new ArrayList<ProblemFile>();
     
     if (singleFile)
     {
       File f = new File(inPath);
       processSingleFile(f.toPath());
     }
     else
     {
       FileVisitor<Path> fileProcessor = new ProcessFile();
       Files.walkFileTree(Paths.get(inPath), fileProcessor);
     }   
     
     getFarngMP3Tags();
     out.close();
     System.out.println("Problems list: "+problemFileList.size());
     System.out.println("OK list: "+fileList.size());
     getDurations();
     sqlReadyTrackInfo();
     insertRecords();
     if (isTango) { tangoTable.reloadData(); }
     else cleanupTable.reloadData();
  }

  void processDirectory()
  {
    FileVisitor<Path> fileProcessor = new ProcessFile();
  }
  
  public  FileVisitResult processSingleFile(Path path) throws Exception
  {
    String pathStr = path.toString().trim().toLowerCase();
    String pathStr2="";
    
    
    if (pathStr.endsWith(".mp3")) 
    {
      if  (!pathStr.contains("._"))
      {
        File file = path.toFile();
        pathStr2=path.toString();
        TangoDJ2.feedback.setText("Preparing Files: "+fileCounter+" - "+pathStr2);
        fileList.add(file);
      }
    }
    fileCounter++;
    return FileVisitResult.CONTINUE;
  }
  
 private  final class ProcessFile extends SimpleFileVisitor<Path> 
 {
   public FileVisitResult visitFile(Path path, BasicFileAttributes aAttrs) throws IOException 
   {
     try {
     return processSingleFile(path);
     } catch (Exception e) { e.printStackTrace(); }
     return null;
   }
 }
       
   public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException 
   {
   System.out.println("Processing directory:" + aDir);
   return FileVisitResult.CONTINUE;
   }
  
  
  
  private void getDurations()
  {
    Iterator<TrackMeta> it = trackInfo.iterator();
    Media media;
    final int count=0;
    trackCount=0;
    
    int trackSize=trackInfo.size();
    System.out.println("TrackInfo Size: "+trackSize);
    
    Timeline timeline = new Timeline();
    timeline.setCycleCount(trackSize);
    KeyFrame keyFrame= new KeyFrame(Duration.seconds(.3), new EventHandler() 
    {
       public void handle(Event event) 
       {
         final TrackMeta trackMeta = trackInfo.get(trackCount);
         getTags(trackMeta, trackCount);
         trackCount++;
       }});
            
      timeline.getKeyFrames().add(keyFrame);
      timeline.playFromStart();
  }
  
  
  private void getTags(final TrackMeta trackMeta, final int count)
  {
    File file;
    final Media media;
    file = new File(trackMeta.path);
    try 
    {
      media = new Media(file.toURI().toString());
      media.setOnError(new Runnable() 
      {
        public void run() {
        System.out.println("MEDIA ERROR: "+media.getError());
      }});
    } catch (Exception e) {  System.out.println("Problem with media, "+trackMeta.path);  return; }
    
    if (media==null) { System.out.println("Media is null, "+trackMeta.path);  return; }
    
    final MediaPlayer mp = new MediaPlayer(media);
    players++;
    mp.setOnError(new Runnable() 
    {
      public void run() {
      System.out.println("MEDIA PLAYER ERROR: "+mp.getError());
    }});
    
    if (mp==null) { System.out.println("MediaPlayer is null, "+trackMeta.path); return; }
    mp.setOnReady(new Runnable() 
    {
      public void run() 
      {
        trackMeta.duration=(int)mp.getTotalDuration().toMillis();
        if ("NO TITLE".equals(trackMeta.title))
        {
          trackMeta.artist=(String)media.getMetadata().get("artist");
          trackMeta.album=(String)media.getMetadata().get("album");
          trackMeta.title=(String)media.getMetadata().get("title");
          trackMeta.comment=(String)media.getMetadata().get("comment-0");
          trackMeta.genre=(String)media.getMetadata().get("genre");
          trackMeta.track_year = (String)media.getMetadata().get("year");
        }
        
        TangoDJ2.feedback.setText("Getting MP3 Tags: "+count+") "+trackMeta.duration+", "+trackMeta.title);
        finalTrackInfo.add(trackMeta);
        finalTrackInfoSizeInt++;
        finalTrackInfoSize.setText(""+finalTrackInfoSizeInt);
        mp.dispose();
        players--;
       
      }
    });
    
  }
  
  private void getFarngMP3Tags() throws Exception
  {
    errors=0;
    counter2=0;
    out.write("GET FARNG MP3 TAGS =====================");
    out.newLine();
    Iterator<File> it = fileList.iterator();
    trackInfo.clear();
    trackInfoSizeInt=0;
    trackInfoSize.setText(""+trackInfoSizeInt);
    File file;
    TrackMeta trackMeta;
    while(it.hasNext())
    {
      file = it.next();
      trackMeta=getSingleMP3tag(file.toPath());
      
      if (trackMeta!=null) 
      {  
        trackInfo.add(trackMeta);
        trackInfoSizeInt++;
        trackInfoSize.setText(""+trackInfoSizeInt);
        TangoDJ2.feedback.setText("Getting preliminary MP3 tags: "+counter2+") "+trackMeta.artist+", "+trackMeta.title);
      }
    }
   
  }
  
  private TrackMeta getSingleMP3tag(Path path) throws Exception
  {
    String pathStr = path.toString().trim().toLowerCase();
    String pathStr2="";
    String title="";
    String artist="";
    String album="";
    String comment="";
    String genre="";
    String track_year="";
  
    String pathHash="";
    MP3File mp3 = null;
    AbstractID3v2 tag;
    AbstractID3v1 tag2;

    File file = path.toFile();
    pathStr2=path.toString();
    String message;
      
    try { mp3= new MP3File(file); } catch (Exception e) 
    { 
      errors++; 
      message="Could not create MP3File class: "+pathStr;
      out.write("Could not create MP3File class: "+pathStr);
      out.newLine();
      counter2++; 
      problemFileList.add(new ProblemFile(file, message));
      return null;  
    }
    
    try { tag = mp3.getID3v2Tag();  } catch (Exception e2) 
    {  errors++; 
      out.write("Could not get ID3v2 tag: "+pathStr);
      out.newLine();
      counter2++; 
      message="Could not get ID3v2 tag: "+pathStr;
      problemFileList.add(new ProblemFile(file, message));
      return null;  
    }
     
    if (tag==null)
    {
      errors++; 
      out.write("tag is null: "+pathStr);
      out.newLine();
      counter2++; 
      message="tag is null: "+pathStr;
      problemFileList.add(new ProblemFile(file, message));
      return null;  
    }
      
    title=tag.getSongTitle();
    comment= tag.getSongComment();
    genre=tag.getSongGenre();
    artist=tag.getLeadArtist();
   
    album=tag.getAlbumTitle();
    track_year=tag.getYearReleased();
      //System.out.println(track_year);
    
    if (artist==null) artist="NO ARTIST";
    else if (artist.trim().length()==0)  artist="NO ARTIST";
    if (title==null) title="NO TITLE";
    else if (title.trim().length()==0)  title="NO TITLE";
    
   
    pathStr2=path.toString();
              
    pathHash = hasher.getMd5Hash(pathStr2.getBytes());
    counter2++;
    tag=null;
    return new TrackMeta(cleanString(title), 
        cleanString(artist),
        cleanString(album),
        cleanString(comment), 
        cleanString(genre), 
        pathHash, 
        cleanString(pathStr2), 
        cleanString(track_year));
  }
  
  public static String cleanString(String inStr)
  {
    // String returnStr = inStr.replace("'","''");
    String returnStr = inStr.replace("ÿþ","");
    // returnStr = returnStr.replace("\\","\\\\");
     
     char tChar=0;
     returnStr = removeChar(returnStr, tChar);
     
     return returnStr;
  }
  
  public static String removeChar(String s, char c) 
  {
  String r = "";
  for (int i = 0; i < s.length(); i ++) 
  {
    if (s.charAt(i) != c) r += s.charAt(i);
  }
  return r;
  }
  
  void insertRecords() 
  {
   int cleanup=0;  // TODO set this on load
   if (!isTango) cleanup=1;
 try{
   //Db.connect();
    connection = DriverManager.getConnection(JDBC_URL2);
    TrackMeta trackMeta;
    String styleGuess="Tango";
    String lowerTitle;
    String lowerGenre;
    Iterator<TrackMeta> it = trackInfo.iterator();
    while(it.hasNext())
    {
      styleGuess="Tango";
      trackMeta=it.next();
      lowerTitle=trackMeta.title.toLowerCase();
      lowerGenre=trackMeta.genre.toLowerCase();
      if (lowerTitle.contains("milonga")) styleGuess="Milonga";
      if (lowerTitle.contains("vals")) styleGuess="Vals";
      if (lowerGenre.contains("milonga")) styleGuess="Milonga";
      if (lowerGenre.contains("vals")) styleGuess="Vals";
      
      String sql="insert into tracks(cleanup, path, pathHash, title, artist, album, duration, genre, comment, style, track_year) "
       +"values ("+cleanup+", '"+trackMeta.path
               +"', '"+trackMeta.pathHash
               +"', '"+trackMeta.title
               +"', '"+trackMeta.artist
               +"', '"+trackMeta.album
               +"', "+trackMeta.duration
               +", '"+trackMeta.genre
               +"', '"+trackMeta.comment
               +"', '"+styleGuess
               +"', '"+trackMeta.track_year
               +"')";
   TangoDJ2.feedback.setText("Inserting Records: "+trackMeta.title);
    try {
      // TODO java.sql.SQLIntegrityConstraintViolationException needs to be handled here
    if (isSet(trackMeta.title)) connection.createStatement().execute(sql);
    } catch (Exception ex) { System.out.println("SQL ERROR: "+sql); 
    ex.printStackTrace(); } 
   }
   connection.close(); 
  // Db.disconnect();
 } catch (Exception e) { e.printStackTrace(); }
}
  
  private boolean isSet(String inStr)
  {
     if (inStr==null) return false;
     if (inStr.length()==0) return false;
     return true;
  }
 void sqlReadyTrackInfo()
 {
   TrackMeta trackMeta;
   Iterator<TrackMeta> it = trackInfo.iterator();
   while(it.hasNext())
   {
   trackMeta=it.next();
   trackMeta.title    = sqlReadyString(trackMeta.title);
   trackMeta.artist   = sqlReadyString(trackMeta.artist);
   trackMeta.album    = sqlReadyString(trackMeta.album);
   trackMeta.comment  = sqlReadyString(trackMeta.comment);
   trackMeta.path     = sqlReadyString(trackMeta.path);
   trackMeta.genre     = sqlReadyString(trackMeta.genre);
   //trackMeta.path = new File(trackMeta.path).toURI().toString();
   trackMeta.track_year     = sqlReadyString(trackMeta.track_year);
  }
    
 }
 
 public static String sqlReadyString(String inStr)
 {
    String returnStr = inStr.replace("'","''");
    returnStr = returnStr.replace("ÿþ","");
   // returnStr = returnStr.replace("\\","\\\\");
    
    char tChar=0;
    returnStr = removeChar(returnStr, tChar);
    
    return returnStr;
 }
 
 public void setTangoTable(TangoTable tangoTable)
 {
   this.tangoTable = tangoTable;
 }
 
 public void setCleanupTable(CleanupTable cleanupTable)
 {
   this.cleanupTable = cleanupTable;
 }
}
