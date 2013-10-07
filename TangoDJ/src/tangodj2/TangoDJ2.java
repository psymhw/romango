package tangodj2;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;

import tangodj2.cleanup.CleanupTable;
import tangodj2.cortina.CortinaTable;
import tangodj2.tango.TangoTable;
import javafx.beans.value.ChangeListener;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
/*
 * TODO export playlist?
 */
public class TangoDJ2 extends Application 
{
  static Stage primaryStage;
  SharedValues sharedValues = new SharedValues();
  static PlaylistBuilderTab playlistBuilderTab;
  static PlaylistChoiceTab playlistChoiceTab;
  MenuBar menuBar;
  TrackLoader2 trackLoader = new TrackLoader2();
  //public static Tab equalizerTab;
  Playlist playlist;
 // AllTracksTable allTracksTable;
  TangoTable tangoTable;
  CleanupTable cleanupTable;
  CortinaTable cortinaTable;
  static CortinaTab cortinaTab;
  Rectangle r = new Rectangle(10,10,10,10);
  Player player;
 
	
  public static void main(String[] args) 
  {
    launch(args);
  }
	
  public void start(Stage stage) 
  {
	  primaryStage=stage;
	  VBox root = new VBox();
    Scene scene = new Scene(root, 950, 550, Color.WHITE);
    r.setFill(Color.RED);
   
    final URL stylesheet = getClass().getResource("style.css");
    scene.getStylesheets().add(stylesheet.toString());
    
    TabPane tabPane = new TabPane();
    BorderPane mainPane = new BorderPane();
          
    CreateDatabase cb = new CreateDatabase();
    
    try {if (!cb.exists()) cb.create(); } catch (Exception e) { e.printStackTrace(); }
    
    setupMenuBar();
      
    try { playlist = new Playlist();} 
    catch (SQLException se) { System.out.println("PROGRAMALREADY RUNNING"); } 
    catch (ClassNotFoundException e) { e.printStackTrace(); }
      
    //allTracksTable = new AllTracksTable(playlist);
    tangoTable = new TangoTable();
    cleanupTable = new CleanupTable(playlist);
    cortinaTable = new CortinaTable();
    
    trackLoader.setTangoTable(tangoTable);
    trackLoader.setCleanupTable(cleanupTable);
     
    Tab equalizerTab = new Tab();
    equalizerTab.setStyle("-fx-background-color: #bfc2c7;");
    equalizerTab.setText("Equalizer");
    
    player = new Player(playlist, equalizerTab);
   
    playlistBuilderTab = new PlaylistBuilderTab(playlist, tangoTable, cleanupTable, player);
    tabPane.getTabs().add(playlistBuilderTab);
  //  playlistBuilderTab.addAllTracksTable();  
    
    playlistChoiceTab = new PlaylistChoiceTab();
    tabPane.getTabs().add(playlistChoiceTab);
    
    cortinaTab = new CortinaTab(new CleanupTable(playlist), cortinaTable, player);
      
    // TAB SELECTION LISTENER
    tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>()
    {
      public void changed(ObservableValue<? extends Tab> arg0, Tab arg1, Tab mostRecentlySelectedTab)
      {
        if (mostRecentlySelectedTab.equals(cortinaTab))
        {
         
          player.setMode(Player.CORTINA_CREATE);
        }
        if (mostRecentlySelectedTab.equals(playlistBuilderTab))
        {
        
          player.setMode(Player.PLAYLIST_CREATE);
        }
      }
    });
      
    tabPane.getTabs().add(equalizerTab);
    tabPane.getTabs().add(cortinaTab);
    
    mainPane.setCenter(tabPane);
      
   
    mainPane.setBottom(player.get());
     
    mainPane.prefHeightProperty().bind(scene.heightProperty());
    mainPane.prefWidthProperty().bind(scene.widthProperty());
    
    root.getChildren().addAll(menuBar, mainPane);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void setupMenuBar()
  {
    menuBar = new MenuBar();
    Menu menuFile = new Menu("File");
   
    MenuItem menuAddTangoDir = new MenuItem("Add Tango Directory");
    MenuItem menuAddTangoFile = new MenuItem("Add Tango Track");
    MenuItem menuAddCortinaFile = new MenuItem("Add Cortina\\Cleanup Track");
   
    Menu menuEdit = new Menu("Edit");
    Menu menuView = new Menu("View");
    menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
    
    menuAddTangoDir.setOnAction(new EventHandler<ActionEvent>() 
    {
      public void handle(ActionEvent t) 
      {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("C:\\music\\tango"));  // temporary 
        File selectedDirectory = 
        directoryChooser.showDialog(primaryStage);
                  
        if(selectedDirectory == null) { System.out.println("No Directory selected"); } 
        else
        {
          try
          {
            trackLoader.process(selectedDirectory.toPath().toString(), false, true);
          } catch (Exception ex) {ex.printStackTrace();}
        }
      }
    });   
    
    menuAddTangoFile.setOnAction(new EventHandler<ActionEvent>() 
    {
      public void handle(ActionEvent t) 
      {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\music\\tango"));  // temporary 
        File selectedFile = 
        fileChooser.showOpenDialog(primaryStage);
                  
        if(selectedFile == null) { System.out.println("No File selected"); } 
        else
        {
          try
          {
            trackLoader.process(selectedFile.toPath().toString(), true, true);
          } catch (Exception ex) {ex.printStackTrace();}
        }
      }
    });   
    
    menuAddCortinaFile.setOnAction(new EventHandler<ActionEvent>() 
    {
      public void handle(ActionEvent t) 
      {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\music\\tango"));  // temporary 
        File selectedFile = 
        fileChooser.showOpenDialog(primaryStage);
                  
        if(selectedFile == null) { System.out.println("No File selected"); } 
        else
        {
          try
          {
            trackLoader.process(selectedFile.toPath().toString(), true, false);
          } catch (Exception ex) {ex.printStackTrace();}
        }
      }
    });   
    	    
    menuFile.getItems().addAll(menuAddTangoDir, menuAddTangoFile,menuAddCortinaFile);
  }

 
  
 
}
