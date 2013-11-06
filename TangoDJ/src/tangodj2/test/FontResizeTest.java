package tangodj2.test;

import java.io.File;
import java.util.ArrayList;

import javafx.application.Application;
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

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3v1;
import org.farng.mp3.id3.AbstractID3v2;

import tangodj.FontMeta;
import tangodj2.Player;




public class FontResizeTest extends Application
{
  FontMeta tusj = new FontMeta("FFF Tusj", FontWeight.BOLD);
  Font titleFont = Font.font(tusj.name, tusj.style, 200);
  Label testLabel = new Label("Test Text");
  double origStageHeight;
  double origStageWidth;
  
  public static void main(String[] args) {  launch(args);}
  
  public void start(Stage stage) 
  {
    testLabel.setFont(titleFont);
    origStageHeight=stage.getHeight();
    origStageWidth=stage.getWidth();
    
    
    
	  stage.heightProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue o, Object oldVal, Object newVal) 
      {
        Double rodp = (Double)newVal;
        System.out.println("scale x: "+ (rodp.doubleValue()/550));
        testLabel.setScaleX((rodp.doubleValue()/550));
      }
    });
	  stage.widthProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue o, Object oldVal, Object newVal) 
      {
        Double rodp = (Double)newVal;
        System.out.println("scale y: "+ (rodp.doubleValue()/950));
        testLabel.setScaleX((rodp.doubleValue()/950));
      }
    });
	  
	  Group rootGroup = new Group();
	  VBox vbox = new VBox();
	  
	  
	  
	  
	  vbox.getChildren().add(testLabel);
	  rootGroup.getChildren().add(vbox);
	  Scene scene = new Scene(rootGroup, 950, 550, Color.WHITE);
	  stage.setScene(scene);
	  stage.show();
	
  
}
  
  
}