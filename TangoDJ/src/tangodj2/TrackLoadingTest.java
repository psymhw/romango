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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3v1;
import org.farng.mp3.id3.AbstractID3v2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


public class TrackLoadingTest extends Application
{
  //String inPath="C:\\music\\tango\\Biagi, Rodolfo\\Campo Afuera";
  //String inPath="C:\\music\\tango\\Biagi, Rodolfo";
    
  //String inPath="C:\\music\\tango\\Di Sarli, Carlos";
  // String inPath="C:\\music\\tango";
  String inPath="C:\\tango";
  static int counter=0;
  static int counter2=0;
  ArrayList<File> fileList;
  ArrayList<ProblemFile> problemFileList;
  private static List<TrackDb> trackInfo = new ArrayList<TrackDb>();
 // public final static ObservableList<TrackDb> trackInfo = FXCollections.observableArrayList();
  
  private static List<TrackDb> finalTrackInfo = new ArrayList<TrackDb>();
  private static Hasher hasher = new Hasher();
  int errors=0;
  BufferedWriter out;
  private int trackCount=0;
  private int trackSize=0;
  static Stage primaryStage;
  final Label dirLabel = new Label("C:\\tango");
  final Label playersLabel = new Label("0");
  ArrayList<String> al;
  static int players=0;
  int trackInfoSizeInt=0;
  Label trackInfoSize = new Label("0");
  int finalTrackInfoSizeInt=0;
  Label finalTrackInfoSize = new Label("0");
  
  public TrackLoadingTest()
  {
    
  }
  
  public void start(Stage stage) throws Exception
  {
    this.primaryStage=stage;
    Group root = new Group();
    
    final int col[] = {0,1,2,3,4,5,6,7,8,9,10};
    final int row[] = {0,1,2,3,4,5,6,7,8,9,10};
    GridPane gridPane = new GridPane();
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    
    
    
    
    final Button button = new Button("Get Paths & Farng tags");
    button.setOnAction(new EventHandler<ActionEvent>() 
    {
       public void handle(ActionEvent e) 
       {
         try 
         {
           counter=0;
           File outFile = new File("out.txt");
           if (outFile.exists()) outFile.delete();
           FileWriter fstream = new FileWriter("out.txt");
           out = new BufferedWriter(fstream);
           fileList = new ArrayList<File>();
           problemFileList = new ArrayList<ProblemFile>();
           FileVisitor<Path> fileProcessor = new ProcessFile();
           Files.walkFileTree(Paths.get(dirLabel.getText()), fileProcessor);
           getFarngMP3Tags();
           out.close();
           System.out.println("Problems list: "+problemFileList.size());
           System.out.println("OK list: "+fileList.size());
           } catch (Exception ex) { ex.printStackTrace(); }
       }
     });
     
     final Button button2 = new Button("List Problems");
     button2.setOnAction(new EventHandler<ActionEvent>() 
     {
       public void handle(ActionEvent e) 
       {
         try 
         {
           ProblemFile problemFile;
           Iterator<ProblemFile> it = problemFileList.iterator();
           while(it.hasNext())
           {
             problemFile = it.next();
             System.out.println(problemFile.message);
           }
           } catch (Exception ex) { ex.printStackTrace(); }
       }
     });
     
     final Button button3 = new Button("Get Durations");
     button3.setOnAction(new EventHandler<ActionEvent>() 
     {
       public void handle(ActionEvent e) 
       {
          getDurationsForNonProblems();
       }
     });
     
     final Button button4 = new Button("List Durations");
     button4.setOnAction(new EventHandler<ActionEvent>() 
     {
       public void handle(ActionEvent e) 
       {
          listTracksWithDurations();
       }
     });
     
     /*
     final Button button5 = new Button("List Problems Data");
     button5.setOnAction(new EventHandler<ActionEvent>() 
     {
       public void handle(ActionEvent e) 
       {
         getDurationsForProblems();
       }
     });
     */
    
     final Button setDirectoryButton = new Button("Set Directory");
     setDirectoryButton.setOnAction(new EventHandler<ActionEvent>() 
     {
       public void handle(ActionEvent e) 
       {
         DirectoryChooser directoryChooser = new DirectoryChooser();
         directoryChooser.setInitialDirectory(new File("C:\\tango"));  // temporary 
         File selectedDirectory = directoryChooser.showDialog(primaryStage);
                   
         if(selectedDirectory == null) { System.out.println("No Directory selected"); } 
         else
         {
           dirLabel.setText(selectedDirectory.toPath().toString());
         }
       }
     });
    
   // HBox hbox = new HBox();
  //  hbox.setPadding(new Insets(10, 10, 10, 10));
   // hbox.setSpacing(20);
   // hbox.getChildren().addAll(setDirectoryButton, dirLabel, button, button2, button3, button4, playersLabel);
  //  root.getChildren().add(hbox);
     //gridPane.setStyle("-fx-background-color: DAE6F3; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work
   //  gridPane.gridLinesVisibleProperty().set(true);
     gridPane.setHgap(10);
     gridPane.setVgap(10);
    gridPane.add(setDirectoryButton, col[0], row[0]); 
    gridPane.add(dirLabel,           col[1], row[0]); 
    gridPane.add(button,             col[0], row[1]); 
    gridPane.add(button2,            col[0], row[2]);
    gridPane.add(button3,            col[0], row[3]);
    gridPane.add(button4,            col[0], row[4]);
    gridPane.add(new Label("Players: "),    col[0], row[5]);
    gridPane.add(playersLabel,              col[1], row[5]);
    gridPane.add(new Label("TrackInfo Size: "), col[0],row[6]);
    gridPane.add(trackInfoSize,                 col[1], row[6]);
    gridPane.add(new Label("Final TrackInfo Size: "), col[0],row[7]);
    gridPane.add(finalTrackInfoSize,                 col[1], row[7]);
    
    root.getChildren().add(gridPane);
    Scene scene = new Scene(root, 950, 550, Color.WHITE);
    stage.setScene(scene);
    stage.show();
    
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
        System.out.println(counter+") "+pathStr2);
        fileList.add(file);
      }
    }
   
    else System.out.println(counter+") Not an MP3 file: "+pathStr);
    counter++;
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
   
   public static void main(String[] args) 
   {
     launch(args);
   }

  
  private void listTracksWithDurations()
  {
    Iterator<TrackDb> it = finalTrackInfo.iterator();
    TrackDb trackDb;
    int count=0;
    File file;
    while(it.hasNext())
    {
      trackDb = it.next();
      System.out.println(count+") Duration List: "+trackDb.duration+", "+trackDb.title);
      count++;
    }
    
  }
  
  private void getDurationsForNonProblems()
  {
    Iterator<TrackDb> it = trackInfo.iterator();
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
         final TrackDb trackDb = trackInfo.get(trackCount);
         getTags(trackDb, trackCount);
         trackCount++;
       }});
            
      timeline.getKeyFrames().add(keyFrame);
      timeline.playFromStart();
  }
  
  private void getDurationsForProblems()
  {
    Iterator<ProblemFile> it = problemFileList.iterator();
    Media media;
    final int count=0;
    trackCount=0;
    
    int trackSize=problemFileList.size();
    
    Timeline timeline = new Timeline();
    timeline.setCycleCount(trackSize);
    KeyFrame keyFrame= new KeyFrame(Duration.seconds(.3), new EventHandler() 
    {
       public void handle(Event event) 
       {
         final ProblemFile problemFile = problemFileList.get(trackCount);
         getProblemTags(problemFile, trackCount);
         trackCount++;
       }});
            
      timeline.getKeyFrames().add(keyFrame);
      timeline.playFromStart();
  }
  
  private void getTags(final TrackDb trackDb, final int count)
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
        System.out.println("MEDIA ERROR: "+media.getError());
      }});
    } catch (Exception e) {  System.out.println("Problem with media, "+trackDb.path);  return; }
    
    if (media==null) { System.out.println("Media is null, "+trackDb.path);  return; }
    
   // javafx.scene.media.Track t = (media.getTracks().get(0));
   
    
    final MediaPlayer mp = new MediaPlayer(media);
    players++;
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
        trackDb.duration=(int)mp.getTotalDuration().toMillis();
        if ("NO TITLE".equals(trackDb.title))
        {
          trackDb.artist=(String)media.getMetadata().get("artist");
          trackDb.album=(String)media.getMetadata().get("album");
          trackDb.title=(String)media.getMetadata().get("title");
          trackDb.comment=(String)media.getMetadata().get("comment-0");
          trackDb.genre=(String)media.getMetadata().get("genre");
          trackDb.track_year = (String)media.getMetadata().get("year");
        }
        
        System.out.println(count+") "+trackDb.duration+", "+trackDb.title);
        finalTrackInfo.add(trackDb);
        finalTrackInfoSizeInt++;
        finalTrackInfoSize.setText(""+finalTrackInfoSizeInt);
        mp.dispose();
        players--;
        playersLabel.setText(""+players);
      }
    });
    
  }
  
  private void getProblemTags(final ProblemFile problemFile, final int count)
  {
    File file;
    final Media media;
    file = problemFile.file;
    String path = file.toPath().toString();
    final String pathStr2=path.toString().trim().toLowerCase();
    try 
    {
      media = new Media(file.toURI().toString());
    } catch (Exception e) {  System.out.println("Problem with media, "+path);  return; }
    
    if (media==null) { System.out.println("Media is null, "+path);  return; }
    
    final MediaPlayer mp = new MediaPlayer(media);
    
    if (mp==null) { System.out.println("MediaPlayer is null, "+path); return; }
    
    mp.setOnReady(new Runnable() 
    {
      public void run() 
      {
         String artist=(String)media.getMetadata().get("artist");
         String title=(String)media.getMetadata().get("title");
         String comment=(String)media.getMetadata().get("comment-0");
         String genre=(String)media.getMetadata().get("genre");
         String pathHash= hasher.getMd5Hash(pathStr2.getBytes());
         String track_year = (String)media.getMetadata().get("year");
        
      /*
        return new TrackDb(cleanString(media.getDbdata()), 
            cleanString(artist),
            cleanString(album),
            cleanString(comment), 
            cleanString(genre), 
            pathHash, 
            cleanString(pathStr2), 
            cleanString(track_year));
        
        
        trackDb.duration=(int)mp.getTotalDuration().toMillis();
        System.out.println(count+") "+trackDb.duration+", "+trackDb.title);
        */
        mp.dispose();
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
    TrackDb trackDb;
    while(it.hasNext())
    {
      file = it.next();
      trackDb=getSingleMP3tag(file.toPath());
      
      if (trackDb!=null) 
      {  
        trackInfo.add(trackDb);
        trackInfoSizeInt++;
        trackInfoSize.setText(""+trackInfoSizeInt);
        System.out.println(counter2+") "+trackDb.artist+", "+trackDb.title);
      }
    }
    System.out.println("Success: "+counter2+" Errors: "+errors);
  }
  
  private TrackDb getSingleMP3tag(Path path) throws Exception
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
      System.out.println(counter+" Could not create MP3File class: "+pathStr+"\n"); 
      out.write("Could not create MP3File class: "+pathStr);
      out.newLine();
      counter2++; 
      problemFileList.add(new ProblemFile(file, message));
      return null;  
    }
    
    try { tag = mp3.getID3v2Tag();  } catch (Exception e2) 
    {  errors++; 
      System.out.println(counter+" Could not get ID3v2 tag: "+pathStr+"\n"); 
      out.write("Could not get ID3v2 tag: "+pathStr);
      out.newLine();
      counter2++; 
      message="Could not get ID3v2 tag: "+pathStr;
      problemFileList.add(new ProblemFile(file, message));
      return null;  
    }
     
    if (tag==null)
    {
      errors++; System.out.println(counter+" tag is null: "+pathStr+"\n"); 
      out.write("tag is null: "+pathStr);
      out.newLine();
      counter2++; 
      message="tag is null: "+pathStr;
      problemFileList.add(new ProblemFile(file, message));
      return null;  
    }
      
    System.out.println("bitrate:" +mp3.getBitRate());
    System.out.println("frequency:" +mp3.getFrequency());
   
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
    
    //tag.setSongTitle("Test");
    //mp3.setID3v2Tag(tag);
    // mp3.save();
     
    /*
    if ("NO TITLE".equals(title)) 
    { 
      message="NO TITLE, "+pathStr;
      problemFileList.add(new ProblemFile(file, message));
      out.write("NO TITLE: "+pathStr);
      out.newLine();
      return null; 
    }
    
    if ("NO ARTIST".equals(artist)) 
    { 
      message="NO ARTIST, "+pathStr;
      problemFileList.add(new ProblemFile(file, message)); 
      out.write("NO ARTIST: "+pathStr);
      out.newLine();
      return null; 
    }
    */
    pathStr2=path.toString();
              
    pathHash = hasher.getMd5Hash(pathStr2.getBytes());
    counter2++;
    tag=null;
    return new TrackDb(cleanString(title), 
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
}
