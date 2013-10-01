package tangodj2;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class PlaylistBuilderTab extends Tab
{
  //Tab tab;
  //SharedValues sharedValues = new SharedValues();
  AllTracksTable allTracksTable;
  Playlist playlist;
  final VBox vbox = new VBox();
  int savedType=0;
  Player player;
  
  public PlaylistBuilderTab(Playlist playlist, AllTracksTable allTracksTable, Player player)
  {
    this.playlist=playlist;
    this.player=player;
    this.allTracksTable=allTracksTable;
	  //tab = new Tab();
	  this.setText("All Tracks");
	  
	  vbox.setPadding(new Insets(10, 10, 10, 10));
	  vbox.setSpacing(20);
	  vbox.setStyle("-fx-background-color: DAE6F3; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work
	 
	  vbox.getChildren().add(getSearchAndFilterBar());
	  addAllTracksTable();
	 
	  HBox hbox =  new HBox();
	  hbox.setPadding(new Insets(10, 10, 10, 10));
	  hbox.setSpacing(20);
	  hbox.getChildren().add(vbox);
	  hbox.setStyle("-fx-background-color: CC99CC; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work

  
    hbox.getChildren().add(playlist.getTreeView());
    hbox.setHgrow(playlist.getTreeView(), Priority.ALWAYS);
    setupListeners() ;
    this.setContent(hbox);
  }
  
  public void removeAllTracksTable()
  {
    vbox.getChildren().remove(allTracksTable.getTable());
  }
  
  public void addAllTracksTable()
  {
    allTracksTable.setType(savedType);
    vbox.getChildren().add(allTracksTable.getTable());
  }
  
  public void addRectangle(Rectangle r)
  {
    vbox.getChildren().add(r);
  }
  
  public TableView<Track> getTable()
  {
    return allTracksTable.getTable();
  }
  
  private Button getTestButton()
  {
    Button testButton = new Button("Test"); 
     
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() 
    {
      public void handle(MouseEvent event) 
      {
        playlist.printTracks();
      }
    };
    testButton.setOnMouseClicked(bHandler);
    return testButton;
  }
  
  private HBox getSearchAndFilterBar()
  {
	  final Label label = new Label("Tracks");
	  label.setFont(new Font("Arial", 20));
	  final RadioButton rb1 = new RadioButton("Tango");
	  final RadioButton rb2 = new RadioButton("Cortina");
	  final RadioButton rb3 = new RadioButton("Setup\\Cleanup");
	  
	  rb1.setId("tango");
	  rb2.setId("cortina");
	  rb3.setId("cleanup");
	  
    final ToggleGroup trackTypeGroup = new ToggleGroup();
    
    rb1.setToggleGroup(trackTypeGroup);
    rb2.setToggleGroup(trackTypeGroup);
    rb3.setToggleGroup(trackTypeGroup);
    rb1.setFont(new Font("Arial", 14));
    rb2.setFont(new Font("Arial", 14));
    rb3.setFont(new Font("Arial", 14));
    
    rb1.setSelected(true);
    
	  final Label spacer = new Label("   ");
	  spacer.setFont(new Font("Arial", 16));
	  final Label spacer2 = new Label("       ");
	  spacer2.setFont(new Font("Arial", 16));
	  final Label spacer3 = new Label("  ");
	  spacer3.setFont(new Font("Arial", 16));
	  final Label spacer4 = new Label("  ");
    spacer4.setFont(new Font("Arial", 16));
	
	  TextField searchBox = new TextField();
	
	  Button searchButton = new Button("Filter");	
	  HBox hbox = new HBox();
	  hbox.setAlignment(Pos.BASELINE_CENTER);
	
	  hbox.getChildren().addAll(label,spacer,rb1,spacer3,rb2,spacer4,rb3,spacer2,searchBox, searchButton);

	  trackTypeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
    public void changed(ObservableValue<? extends Toggle> ov,
          Toggle old_toggle, Toggle new_toggle) 
      {
        if (trackTypeGroup.getSelectedToggle() != null) 
        {
          String selectedStr=trackTypeGroup.getSelectedToggle().toString();
          if (selectedStr.contains("tango")) 
          {
            allTracksTable.setType(0);
            savedType=0;
          }
          else if (selectedStr.contains("cleanup")) 
          {
            allTracksTable.setType(1);
            savedType=1;
          }
        }                
      }
    });
	  return hbox;
  }
   
  private void setupListeners() 
  {
    ChangeListener tangoHashListener = new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
      //  System.out.println("selected Tango Hash: "+sharedValues.selectedTangoPathHash.get());
        player.updateUIValues(SharedValues.selectedTangoPathHash.get());
      }
    };   
    SharedValues.selectedTangoPathHash.addListener(tangoHashListener);
  }
  
}
