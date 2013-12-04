package tangodj2.test;

import java.io.File;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3v1;
import org.farng.mp3.id3.AbstractID3v2;

import tangodj.FontMeta;
import tangodj2.Player;




public class FontResizeTest extends Application
{
  FontMeta tusj = new FontMeta("FFF Tusj", FontWeight.BOLD);
  double fontSize=20;
  Font titleFont = Font.font(tusj.name, tusj.style, fontSize);
  Label testLabel = new Label("Test Text Blah Blah");
  double origStageHeight;
  double origStageWidth;
  Button testButton = new Button("Test");
  int counter=0;
  Stage mainStage;
  final int LARGER = 0;
  final int SMALLER = 1;
  
  public static void main(String[] args) {  launch(args);}
  
  public void start(Stage stage) 
  {
    mainStage=stage;
    testLabel.setFont(titleFont);
    
    
    
    testButton.setOnAction(new EventHandler<ActionEvent>() 
        {
      public void handle(ActionEvent e) 
      {
        System.out.println("width: "+testLabel.getWidth());
      }
    });
    
	  stage.widthProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue o, Object oldVal, Object newVal) 
      {
         int direction=-1;
         if ( ((Number)newVal).doubleValue()> ((Number)oldVal).doubleValue() )
         {
	          direction = LARGER;
         } else direction=SMALLER;
         
         changeLabelWidth(((Number)newVal).doubleValue(), direction);
      }
    });
    
     
	  
	 
	  Group rootGroup = new Group();
	  VBox vbox = new VBox();
	  
	  
	  vbox.getChildren().add(testLabel);
	  vbox.getChildren().add(testButton);
	  rootGroup.getChildren().add(vbox);
	  Scene scene = new Scene(rootGroup, 950, 550, Color.WHITE);
	  stage.setScene(scene);
	  stage.show();
	  origStageHeight=stage.getHeight();
    origStageWidth=stage.getWidth();
	//  System.out.println("starting width: "+testLabel.getWidth());
	//  System.out.println("starting stage width: "+origStageWidth);
    
    changeLabelWidth(+origStageWidth, LARGER);
	}
  
  private void changeLabelWidth(final double target, final int direction)
  {
    counter=0;
   // System.out.println("CHANGE CALLED");
    fontSize = testLabel.getFont().getSize();
    
    final Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    
    KeyFrame keyFrame  =  new KeyFrame(Duration.seconds(.01),  new EventHandler() 
    {
      public void handle(Event event) 
      {
        double width=testLabel.getWidth();
        if (direction==LARGER)
        {  
          if (width>=(target-60)) { timeline.stop(); return; }
          if (counter>=300) { timeline.stop(); return; }
          fontSize+=1;
          //System.out.println("label is smaller");
        }
        if (direction==SMALLER)
        {
          if (width<=(target-60)) { timeline.stop(); return; }
          if (counter>=300) { timeline.stop(); return; }
          fontSize-=1;
         // System.out.println("label is larger");
        }
        
        Font titleFont = Font.font(tusj.name, tusj.style, fontSize);
        testLabel.setFont(titleFont);
        
      //  System.out.println(counter+") width: "+width+" target: "+(target-60)); 
        counter++;
       
      }
    });
    
    timeline.getKeyFrames().add(keyFrame);
    timeline.play();
  }
  
}