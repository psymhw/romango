package tangodj2;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AllTracksTab 
{
  Tab tab;
  SharedValues sharedValues = new SharedValues();
  AllTracksTable allTracksTable;
  
  Playlist playlist;
  
  //TrackLoader2 trackLoader = new TrackLoader2();
  private Button newTandaButton;
	 	
  public AllTracksTab(Playlist playlist)
  {
    this.playlist=playlist;
	  tab = new Tab();
	  tab.setText("All Tracks");
	  allTracksTable = new AllTracksTable(playlist);
	  
	 final Label label = new Label("All Tracks");
	 label.setFont(new Font("Arial", 20));
	      
	 final VBox vbox = new VBox();
	 vbox.setPadding(new Insets(10, 10, 10, 10));
	 vbox.setSpacing(20);
	 vbox.setStyle("-fx-background-color: DAE6F3; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work
	 //PlayerControls playerControls_1 = new PlayerControls(SharedValues.selectedAllTracksPathHash);
	 
	// HBox belowAllTracksTableBox = new HBox();
	// belowAllTracksTableBox.getChildren().add(addButton);
	 //belowAllTracksTableBox.getChildren().add(playerControls_1.get());
	 vbox.getChildren().addAll(getSearchAndFilterBar(), allTracksTable.getTable());
	      
	 HBox hbox =  new HBox();
	 hbox.setPadding(new Insets(10, 10, 10, 10));
	 hbox.setSpacing(20);
	 hbox.getChildren().add(vbox);
	 hbox.setStyle("-fx-background-color: CC99CC; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work
	 
	// setupTreeView();
	
    // VBox playlistBox = new VBox();
   //  playlistBox.setPadding(new Insets(10, 10, 10, 10));
   //  playlistBox.setSpacing(20);
     setupTandaButton();
     
     
     
     
    // playlistBox.getChildren().add(getTestButton());
   //  HBox belowPlaylistTreeBox = new HBox();
   //  belowPlaylistTreeBox.getChildren().add(newTandaButton);
   //  playlistBox.getChildren().add(playlist.getTreeView());
   //  playlistBox.getChildren().add(belowPlaylistTreeBox);
     hbox.getChildren().add(playlist.getTreeView());
     hbox.setHgrow(playlist.getTreeView(), Priority.ALWAYS);
    // setupListeners();
     
   //  final VBox mainBox = new VBox();
   //  mainBox.getChildren().add(hbox);
   //  mainBox.setPadding(new Insets(10, 10, 10, 10));
   //  mainBox.setSpacing(20);
   //  mainBox.setStyle("-fx-background-color: #4169e1; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work
     
    
   //  mainBox.getChildren().add(playerControls_1.get());
     tab.setContent(hbox);
	
     
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
	final Label label = new Label("All Tracks");
	label.setFont(new Font("Arial", 20));
	final RadioButton rb1 = new RadioButton("Tango");
	final RadioButton rb2 = new RadioButton("Cortina\\Cleanup");
    final ToggleGroup styleGroup = new ToggleGroup();
    
    rb1.setToggleGroup(styleGroup);
    rb2.setToggleGroup(styleGroup);
    rb1.setFont(new Font("Arial", 16));
    rb2.setFont(new Font("Arial", 16));
    
    rb1.setSelected(true);
    
	final Label spacer = new Label("       ");
	spacer.setFont(new Font("Arial", 16));
	final Label spacer2 = new Label("       ");
	spacer2.setFont(new Font("Arial", 16));
	final Label spacer3 = new Label("  ");
	spacer3.setFont(new Font("Arial", 16));
	
	TextField searchBox = new TextField();
	
	Button searchButton = new Button("Filter");	
	HBox hbox = new HBox();
	hbox.setAlignment(Pos.BASELINE_CENTER);
	
	hbox.getChildren().addAll(label,spacer,rb1,spacer3,rb2,spacer2,searchBox, searchButton);

	return hbox;
  }
   
   private void setupTandaButton() 
   {
     newTandaButton = new Button("New Tanda");
     final ComboBox comboBox = new ComboBox();
     //Separator separator = new Separator();
     
     final TextBuilder seperatorBuilder = TextBuilder.create()
    		    .fill(Color.BLACK)
    		    .font(Font.font("Serif", 18));
     
     final Text alist = seperatorBuilder.text("A List").build();
     Text blist =  seperatorBuilder.text("B List").build();
     Text clist =  seperatorBuilder.text("C List").build();
    
     comboBox.getItems().add(alist);
     comboBox.getItems().addAll(SharedValues.artistsA);
     comboBox.getItems().add(blist);
     comboBox.getItems().addAll(SharedValues.artistsB);
     comboBox.getItems().add(clist);
     comboBox.getItems().addAll(SharedValues.artistsC);
    
     final RadioButton rb1 = new RadioButton("Tango");
     final RadioButton rb2 = new RadioButton("Vals");
     final RadioButton rb3 = new RadioButton("Milonga");
     final RadioButton rb4 = new RadioButton("Alternative");
     final RadioButton rb5 = new RadioButton("Mixed");
     final RadioButton rb6 = new RadioButton("Cleanup");
     
     rb1.setId(""+SharedValues.TANGO);
     rb2.setId(""+SharedValues.VALS);
     rb3.setId(""+SharedValues.MILONGA);
     rb4.setId(""+SharedValues.ALTERNATIVE);
     rb5.setId(""+SharedValues.MIXED);
     rb6.setId(""+SharedValues.CLEANUP);
     
     final ToggleGroup styleGroup = new ToggleGroup();
     
     rb1.setToggleGroup(styleGroup);
     rb2.setToggleGroup(styleGroup);
     rb3.setToggleGroup(styleGroup);
     rb4.setToggleGroup(styleGroup);
     rb5.setToggleGroup(styleGroup);
     rb6.setToggleGroup(styleGroup);
        
	 rb1.setSelected(true);
	 
	 
	 
	 final VBox styleBox = new VBox();
	 styleBox.getChildren().addAll(rb1,rb2,rb3,rb4,rb5,rb6);
		
     
     final TextField tandaName = new TextField();
     
     EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() 
     {
       public void handle(MouseEvent event) 
       {
         final Stage myDialog = new Stage();
         myDialog.initModality(Modality.APPLICATION_MODAL);
         Button okButton = new Button("SAVE");
         okButton.setOnAction(new EventHandler<ActionEvent>()
         {
           public void handle(ActionEvent arg0) 
           {
        	 String artist = comboBox.getSelectionModel().getSelectedItem().toString();
        	 int styleId = 0;
        	 String selectedStr=styleGroup.getSelectedToggle().toString();
        	 int i = selectedStr.indexOf("id=");
        	 String numStr = selectedStr.substring(i+3, i+4);
        	 try 
        	 {
        	   styleId= Integer.parseInt(numStr);
        	 } catch (Exception e) {}
        	 playlist.addTanda(artist, styleId);
             myDialog.close();
           }
         });
       
         Text tandaLabel = new Text("Orchestra: ");
         tandaLabel.setFont(Font.font("Serif", 20));
         
         
         GridPane gridPane = new GridPane();
         gridPane.setPadding(new Insets(10, 10, 10, 10));
         gridPane.setVgap(2);
         gridPane.setHgap(5);
         gridPane.add(tandaLabel, 0, 0);
         gridPane.add(comboBox, 1, 0);
         //gridPane.add(new Text("Style"), 0, 1);
         gridPane.add(styleBox, 0, 1);
         gridPane.add(okButton, 1, 2);
         
         Scene myDialogScene = new Scene(gridPane, 300, 200);
         myDialog.setScene(myDialogScene);
         myDialog.show();
       }
     };
     
     newTandaButton.setOnMouseClicked(bHandler);
   }
   
   public Tab getTab()
   {
	 return tab;
   }
	
   
	  
	}
