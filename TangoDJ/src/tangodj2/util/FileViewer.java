package tangodj2.util;

import java.util.List;

import tangodj2.EventTab;
import tangodj2.Preferences;
import tangodj2.TrackDb;
import tangodj2.infoWindow.InfoWindow2;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FileViewer  extends Stage
{
  TextArea textArea = new TextArea();
  TextArea resultArea = new TextArea();
  Button startButton = new Button("Start");
  FileDbCompare fileDbCompare;
  boolean tangoSelected=true;
  final RadioButton rb1 = new RadioButton("tango");
  final RadioButton rb2 = new RadioButton("non-tango");
  final ToggleGroup toggleGroup = new ToggleGroup();
  private int lastListSize=0;
  
  public FileViewer(Preferences prefs)
  {
	fileDbCompare = new FileDbCompare(prefs, textArea, resultArea);  
	
	textArea.setMinHeight(275);
	textArea.setEditable(false);
	resultArea.setMinHeight(275);
	resultArea.setEditable(false);
	
	
	rb1.setToggleGroup(toggleGroup);
	rb1.setId("tango");
	rb2.setId("non-tango");
	rb2.setToggleGroup(toggleGroup);
	rb1.setSelected(true);
	rb1.setPrefWidth(75);
		
	toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov,
		        Toggle old_toggle, Toggle new_toggle) {
		            if (toggleGroup.getSelectedToggle() != null) 
		            {
		              String selectedStr=toggleGroup.getSelectedToggle().toString();
		              if (selectedStr.contains("tango")) fileDbCompare.selectRoot(true);
		              if (selectedStr.contains("non-tango"))  fileDbCompare.selectRoot(false);
		            }                
		        }
		});
		
	HBox radioBox = new HBox();
	radioBox.getChildren().add(rb1);
	radioBox.getChildren().add(rb2);
	
	  
	 
	startButton.setOnAction(new EventHandler<ActionEvent>() 
	{
	  public void handle(ActionEvent actionEvent) 
	  {
		timerStart();
		fileDbCompare.start();   	  
	  }
	});  
	  
	Pane pane = new Pane();
	VBox vbox = new VBox();
	Text feedbackLabel = new Text("All Files");
	feedbackLabel.setFont(Font.font("Serif", 16));
	Text resultsLabel = new Text("Files not found in database");
	resultsLabel.setFont(Font.font("Serif", 16));
	
	
	vbox.setPadding(new Insets(5,5,5,5));
	vbox.getChildren().add(radioBox);
	vbox.getChildren().add(startButton);
	vbox.getChildren().add(feedbackLabel);
	vbox.getChildren().add(textArea);
	vbox.getChildren().add(resultsLabel);
	vbox.getChildren().add(resultArea);
	
	VBox.setMargin(radioBox, new Insets(5,5,5,5));
	VBox.setMargin(startButton, new Insets(5,5,5,5));
	VBox.setMargin(feedbackLabel, new Insets(5,5,5,5));
	VBox.setMargin(textArea, new Insets(5,5,5,5));
	VBox.setMargin(resultsLabel, new Insets(5,5,5,5));
	VBox.setMargin(resultArea, new Insets(5,5,5,5));
	
	
	pane.getChildren().add(vbox);
	
	Scene myDialogScene = new Scene(pane, 600, 700);
    setScene(myDialogScene);
    show();
  }
  
  private void timerStart()
  {
	Timeline timeline = new Timeline();
	timeline.setCycleCount(Timeline.INDEFINITE); 
	KeyFrame keyFrame= new KeyFrame(Duration.seconds(.1), new EventHandler() 
	{
	  public void handle(Event event) 
	 {
		if (fileDbCompare.running)
		{
	      List<String> tList = fileDbCompare.getTrackList(); 
	      int listSize=tList.size();
	      for(int i=lastListSize; i<listSize; i++)
	      {
	        textArea.appendText(tList.get(i));
	      }
		}
	 }});
     timeline.getKeyFrames().add(keyFrame);
     timeline.playFromStart();

  }
  
}
