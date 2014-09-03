package tangodj2.test;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FileSystemViewTest  extends Application  
{
  static TextArea textArea = new TextArea();
  Button startButton = new Button("Start");
  
  public void start(final Stage stage) throws Exception  
	  { 
		Pane pane = new Pane();
		VBox vbox = new VBox();
		TextField text = new TextField();
		textArea.setMinHeight(275);
		textArea.setEditable(false);
		vbox.getChildren().add(startButton);
		vbox.getChildren().add(textArea);
		pane.getChildren().add(vbox);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() 
		{
		  public void handle(ActionEvent actionEvent) 
		  {
		    task.run();	  
		  }
		});  
		
		final Scene scene = new Scene(pane, 600, 700);
		stage.setScene(scene);
	    stage.show();
	    
	   // task.messageProperty().addListener(new ChangeListener<String>() {  
       //     public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {  
        //        textArea.appendText(newValue + "\n");  
        //    }  
       // });  
	   
	    
	  }
	
	static Task<Void> task = new Task<Void>() 
	{
      @Override protected Void call() throws Exception 
      {
    	   final class ProcessFile extends SimpleFileVisitor<Path> {
    	        @Override public FileVisitResult visitFile(
    	          Path aFile, BasicFileAttributes aAttrs
    	        ) throws IOException {
    	        	//updateMessage(aFile.toString());
    	        	Platform.runLater(new Runnable() {
                        @Override public void run() {
                            textArea.appendText(aFile.toString()+"\n");
                        } });
    	          System.out.println("Processing file:" + aFile);
    	          return FileVisitResult.CONTINUE;
    	        }
    	        
    	        @Override  public FileVisitResult preVisitDirectory(
    	          Path aDir, BasicFileAttributes aAttrs
    	        ) throws IOException {
    	          System.out.println("Processing directory:" + aDir);
    	          return FileVisitResult.CONTINUE;
    	        }
    	      }
    	   FileVisitor<Path> fileProcessor = new ProcessFile();
    	    
    	    try 
    		{
    		  Files.walkFileTree(Paths.get("C:\\music"), fileProcessor);
    	    } catch (IOException e) { e.printStackTrace();	}
    	
        return null;	
      }
    };
    
    
    
	public static void main(String[] args) 
	{
		Application.launch(args); 

	}

}
