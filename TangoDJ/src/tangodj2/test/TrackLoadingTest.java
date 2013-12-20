package tangodj2.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class TrackLoadingTest extends Application
{
  String inPath="C:\\music\\tango\\Biagi, Rodolfo";
  static int counter=0;
  ArrayList<File> fileList;
  
  public TrackLoadingTest()
  {
    
  }

  void processDirectory()
  {
    FileVisitor<Path> fileProcessor = new ProcessFile();
  }
  
  public static FileVisitResult processSingleFile(Path path)
  {
    String pathStr = path.toString().trim().toLowerCase();
    String pathStr2="";
    
    if (pathStr.endsWith(".mp3")) 
    {
      File file = path.toFile();
      pathStr2=path.toString();
      System.out.println(counter+") "+pathStr2);
    }
    else System.out.println(counter+") Not an MP3 file: "+pathStr);
    counter++;
    return FileVisitResult.CONTINUE;
  }
  
 private static final class ProcessFile extends SimpleFileVisitor<Path> 
 {
   public FileVisitResult visitFile(Path path, BasicFileAttributes aAttrs) throws IOException 
   {
     return processSingleFile(path);
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

  @Override
  public void start(Stage stage) throws Exception
  {
    Group root = new Group();
    
    final Button addButton = new Button("Check");
     addButton.setOnAction(new EventHandler<ActionEvent>() 
     {
       public void handle(ActionEvent e) 
       {
         try 
         {
           counter=0;
           fileList = new ArrayList<File>();
           FileVisitor<Path> fileProcessor = new ProcessFile();
           Files.walkFileTree(Paths.get(inPath), fileProcessor);
           } catch (Exception ex) { ex.printStackTrace(); }
       }
     });
    
    root.getChildren().add(addButton);
    Scene scene = new Scene(root, 950, 550, Color.WHITE);
    stage.setScene(scene);
    stage.show();
    
  }
}
