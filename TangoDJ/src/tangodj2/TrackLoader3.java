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
import java.sql.SQLIntegrityConstraintViolationException;
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
import tangodj2.cortina.CortinaTable;
import tangodj2.tango.TangoTable;




public class TrackLoader3 
{
//  String inPath="C:\\music\\tango\\Di Sarli, Carlos";
  static int fileCounter=0;
  static int farngCounter=0;
  ArrayList<File> fileList;
 // ArrayList<ProblemFile> problemFileList;
  private static List<TrackDb> trackInfo = new ArrayList<TrackDb>();
  
 // private static List<TrackDb> finalTrackInfo = new ArrayList<TrackDb>();
  private static Hasher hasher = new Hasher();
  int errors=0;
  BufferedWriter out;
  private int trackCount=0;
  private int trackSize=0;
 
  //ArrayList<String> al;
  //static int players=0;
  //int trackInfoSizeInt=0;
 // Label trackInfoSize = new Label("0");
  //int finalTrackInfoSizeInt=0;
  //Label finalTrackInfoSize = new Label("0");
 // public static final String DRIVER2 ="org.apache.derby.jdbc.EmbeddedDriver";
 // public static final String JDBC_URL2 ="jdbc:derby:tango_db;create=false";
 // static Connection connection;
//  private boolean isTango=true;
  private int type=SharedValues.TANGO;;
  
  TangoTable tangoTable;
  CleanupTable cleanupTable;
  public  ArrayList<ArtistX> artistsAll = new ArrayList<ArtistX>();
  
  public TrackLoader3() 
  { 
    artistsAll.addAll(SharedValues.artistsA);
    artistsAll.addAll(SharedValues.artistsB);
    artistsAll.addAll(SharedValues.artistsC);
  }
  
  public void process(String inPath, int scope, int type) throws Exception
  {
    // this.isTango=isTango;
    this.type=type;
      fileCounter=0;
     File outFile = new File("out.txt");
     if (outFile.exists()) outFile.delete();
     FileWriter fstream = new FileWriter("out.txt");
     out = new BufferedWriter(fstream);
     fileList = new ArrayList<File>();
    // problemFileList = new ArrayList<ProblemFile>();
     
     //Db.connect();
     if (scope==SharedValues.FILE)
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
    
   //  System.out.println("Problems list: "+problemFileList.size());
   //  System.out.println("OK list: "+fileList.size());
     getDurations();
     
    // Db.disconnect();
     
  }

  void processDirectory()
  {
    FileVisitor<Path> fileProcessor = new ProcessFile();
  }
  
  public  FileVisitResult processSingleFile(Path path) throws Exception
  {
    String pathStr = path.toString().trim().toLowerCase();
    String pathStr2="";
    String pathHash;
    
    
    if (pathStr.endsWith(".mp3")) 
    {
      if  (!pathStr.contains("._"))
      {
        File file = path.toFile();
        pathStr2=path.toString();
        pathHash = hasher.getMd5Hash(pathStr2.getBytes());
        if (Db.trackExists(pathHash)) 
        {
          System.out.println("ALDEAY LOADED: "+pathStr2);
          return FileVisitResult.CONTINUE;
        }
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
    trackCount=0;
    
    final int trackSize=trackInfo.size();
    
    Timeline timeline = new Timeline();
    timeline.setCycleCount(trackSize+1); // need extra cycle 
    KeyFrame keyFrame= new KeyFrame(Duration.seconds(.3), new EventHandler() 
    {
       public void handle(Event event) 
       {
         if (trackCount<trackSize)
         {  
           final TrackDb trackDb = trackInfo.get(trackCount);
           getMediaPlayerTags(trackDb, trackCount, trackSize);
           
           trackCount++;
         }
       }});
            
      timeline.getKeyFrames().add(keyFrame);
      timeline.playFromStart();
      timeline.setOnFinished(new EventHandler() 
      {
        public void handle(Event arg0)
        {
         // sqlReadyTrackInfo();
         // insertAllRecords();
          if (type==SharedValues.TANGO)  tangoTable.reloadData();
          else if (type==SharedValues.CLEANUP) cleanupTable.reloadData();
          else if (type==SharedValues.CORTINA) CortinaTable.reloadData();
          
          try { out.close(); } catch (IOException e) { e.printStackTrace(); }
        }});
  }
  
  
  private void getMediaPlayerTags(final TrackDb trackDb, final int count, final int trackSize)
  {
    File file;
    final Media media;
    file = new File(trackDb.path);
    try 
    {
      media = new Media(file.toURI().toString());
      media.setOnError(new Runnable() 
      {
        public void run() {
        System.out.println("MEDIA ERROR: "+media.getError()); }});
      } catch (Exception e) {  System.out.println("Problem with media, "+trackDb.path);  return; }
    
    if (media==null) { System.out.println("Media is null, "+trackDb.path);  return; }
    
    final MediaPlayer mp = new MediaPlayer(media);
    //players++;
    mp.setOnError(new Runnable() 
    {
      public void run() {
      System.out.println("MEDIA PLAYER ERROR: "+mp.getError());
    }});
    
    if (mp==null) { System.out.println("MediaPlayer is null, "+trackDb.path); return; }
    mp.setOnReady(new Runnable() 
    {
      public void run() 
      {
       
        //String comment1 = (String)media.getMetadata().get("comment-1");
       // String comment2 = (String)media.getMetadata().get("comment-2");
        
        //if (comment1!=null) { if (!"".equals(comment1)) {  System.out.println("comment 1: "+comment1); }}
        //if (comment2!=null) { if (!"".equals(comment2)) {  System.out.println("comment 2: "+comment2); }}
        
        trackDb.duration=(int)mp.getTotalDuration().toMillis();
        System.out.println("duration: "+trackDb.duration+", "+trackDb.title);
        if ("NO TITLE".equals(trackDb.title))
        {
          trackDb.artist=(String)media.getMetadata().get("artist");
          trackDb.album=(String)media.getMetadata().get("album");
          trackDb.title=(String)media.getMetadata().get("title");
          trackDb.comment=(String)media.getMetadata().get("comment-0");
          trackDb.genre=(String)media.getMetadata().get("genre");
          trackDb.track_year = (String)media.getMetadata().get("year");
          Object obj = media.getMetadata().get("track number");
          if (obj!=null) trackDb.track_no = (int)obj;
        }
        
        if (trackDb.title==null) 
        {
          trackDb.title="STILL NO TITLE";
          System.out.println("Still no title");
          return;
        }
        if (trackDb.artist==null) trackDb.artist="";
        if (trackDb.genre==null) trackDb.genre="";
        
        ArtistX artistX = getArtistX(trackDb.artist);
        if (artistX==null)
        {
          trackDb.leader=trackDb.artist;
        }
        else
        {
          trackDb.leader=artistX.getLeader();
          // trackDb.artist=artistX.getArtist();
        }
        String styleGuess="Tango";
        String lowerTitle;
        String lowerGenre;
        
        lowerTitle=trackDb.title.toLowerCase();
        lowerGenre=trackDb.genre.toLowerCase();
        if (lowerTitle.contains("milonga")) styleGuess="Milonga";
        if (lowerTitle.contains("vals")) styleGuess="Vals";
        if (lowerGenre.contains("milonga")) styleGuess="Milonga";
        if (lowerGenre.contains("vals")) styleGuess="Vals";
        
        trackDb.style=styleGuess;
        
        if (trackDb.comment!=null)
        {
          if (trackDb.comment.startsWith("Amazon.com")) trackDb.comment="";
          if (trackDb.comment.startsWith("iTunPGAP")) trackDb.comment="";
        }
        
        TangoDJ2.feedback.setText("Getting MP3 Tags: "+count+" of "+trackSize+", "+trackDb.title);
        //finalTrackInfo.add(trackDb);
        //finalTrackInfoSizeInt++;
        // finalTrackInfoSize.setText(""+finalTrackInfoSizeInt);
        mp.dispose();
        //players--;
        if (type!=SharedValues.CORTINA)
        {  
          Db.insertTrack(trackDb, type);
        }
        else
        {
          System.out.println("Trackloader3 - insert premade");
          Db.insertPremadeCortina(trackDb);
        }
      }
    });
    
  }
  
  private void getFarngMP3Tags() throws Exception
  {
    errors=0;
    farngCounter=0;
    out.write("GET FARNG MP3 TAGS =====================");
    out.newLine();
    Iterator<File> it = fileList.iterator();
    trackInfo.clear();
    //trackInfoSizeInt=0;
    File file;
    TrackDb trackDb;
    while(it.hasNext())
    {
      file = it.next();
      trackDb=getSingleFarngMP3tag(file.toPath());
      
      if (trackDb!=null) 
      {  
        trackInfo.add(trackDb);
       // trackInfoSizeInt++;
        TangoDJ2.feedback.setText("Getting preliminary MP3 tags: "+farngCounter+") "+trackDb.artist+", "+trackDb.title);
      }
    }
   
  }
  
  private TrackDb getSingleFarngMP3tag(Path path) throws Exception
  {
    String pathStr = path.toString().trim().toLowerCase();
    String pathStr2="";
    
    MP3File mp3 = null;
    AbstractID3v2 tag;

    File file = path.toFile();
    pathStr2=path.toString();
      
    TrackDb trackDb = new TrackDb();
    
    try { mp3= new MP3File(file); } catch (Exception e) 
    { 
      errors++; 
      out.write("Could not create MP3File class: "+pathStr);
      out.newLine();
      farngCounter++; 
      return null;  
    }
    
    try { tag = mp3.getID3v2Tag();  } catch (Exception e2) 
    { 
      errors++; 
      out.write("Could not get ID3v2 tag: "+pathStr);
      out.newLine();
      farngCounter++; 
      return null;  
    }
     
    if (tag==null)
    {
      errors++; 
      out.write("tag is null: "+pathStr);
      out.newLine();
      farngCounter++; 
      return null;  
    }
    
    pathStr2=path.toString();
      
    trackDb.title=cleanString(tag.getSongTitle());
    trackDb.artist=cleanString(tag.getLeadArtist());
    trackDb.album=cleanString(tag.getAlbumTitle());
    trackDb.comment=cleanString(tag.getSongComment());
    trackDb.genre=cleanString(tag.getSongGenre());
    trackDb.track_year=cleanString(tag.getYearReleased());
    
    // Track Number
    String strTrackNo="0";
    int track_no=0;
    strTrackNo=tag.getTrackNumberOnAlbum();
    try { track_no = Integer.parseInt(strTrackNo); } catch (Exception e3) {}
    trackDb.track_no=track_no;
    
    trackDb.pathHash = hasher.getMd5Hash(pathStr2.getBytes());
    trackDb.path = cleanString(pathStr2);
    
    if (trackDb.artist==null) trackDb.artist="NO ARTIST";
    else if (trackDb.artist.trim().length()==0)  trackDb.artist="NO ARTIST";
    if (trackDb.title==null) trackDb.title="NO TITLE";
    else if (trackDb.title.trim().length()==0)  trackDb.title="NO TITLE";
    
    farngCounter++;
    tag=null;
    
    return trackDb;
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
  
  /*
  void insertAllRecords() 
  {
    try
    {
      connection = DriverManager.getConnection(JDBC_URL2);
      TrackDb trackDb;
   
      Iterator<TrackDb> it = trackInfo.iterator();
      while(it.hasNext())
      {
        trackDb=it.next();
        insertRecord(trackDb);
      }
      connection.close(); 
    } catch (Exception e) { e.printStackTrace(); }
  }
  */
  
  private ArtistX getArtistX(String artist)
  {
    Iterator<ArtistX> it = artistsAll.iterator();
    ArtistX artistX;
    while (it.hasNext())
    {
      artistX=it.next();
      if (artist.toLowerCase().contains(artistX.last.toLowerCase())) return artistX;
    }
    return null;
  }

 
 void sqlReadyTrackInfo(TrackDb trackDb)
 {
   
   trackDb.title    = sqlReadyString(trackDb.title, 100);
   trackDb.artist   = sqlReadyString(trackDb.artist, 40);
   trackDb.album    = sqlReadyString(trackDb.album, 100);
   trackDb.comment  = sqlReadyString(trackDb.comment, 100);
   trackDb.path     = sqlReadyString(trackDb.path);
   trackDb.genre     = sqlReadyString(trackDb.genre, 40);
   trackDb.leader    = sqlReadyString(trackDb.leader);
   //trackDb.path = new File(trackDb.path).toURI().toString();
   trackDb.track_year     = sqlReadyString(trackDb.track_year, 4);
   
   if (trackDb.comment.length()>100)  trackDb.comment=trackDb.comment.substring(0, 99);
  
 }
 
 public static String sqlReadyString(String inStr, int maxLength)
 {
   String retStr = sqlReadyString(inStr);
   if (retStr.length()>=maxLength) retStr=retStr.substring(0, maxLength-1);
   return retStr;
 }
 
 public static String sqlReadyString(String inStr)
 {
    if (inStr==null) return "";
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
