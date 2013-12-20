package tangodj2;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tangodj2.cleanup.CleanupTable;
import tangodj2.cortina.CortinaTable;
import tangodj2.infoWindow.InfoWindow2;
import tangodj2.tango.TangoTable;
/*
 * TODO export playlist?
 * MP3 Tagtools
 * CDEX
 * Get rating from iTunes XML file?
 * Create a tangoGenre MP3tag and populate when making tandas?
 * after each file load message at bottom done: added: ### duplicates: ###
 
 * Spare DB fields for updates
 * Show total time for each tanda. Maybe even real end time from system clock
 * When making cotinas, there should be a length counter
 *   from the time the set start position button is pressed
 * Tooltips on TreeItems (treeCell?) to show time, album, etc
 * Need a file for Orchestra, Singer, Band Leader (principles) 
 *    then a pointer from the tracks file
 * Spell check band leaders
 * Create playlists from tandas.
 * Handle bad directory address in preferences
 * Feedback col could say playing from playlist or playing from library
 * Automatiacally play next in library. Also allow skip fwd and back from library
 * Link to track folder from MP3 Editor
 * Feedback could show # of track stats on startup
 * Pop up a window with load errors?
 * Allow interactively new named playlist(text field). Would allow non tango playlists.
 * Remember equalizer values - for next tune and when program closed
 
 * Index on tracks to make them load faster
 * put distribution version in state file
 * Lists tab where you can create various lists of tracks. Then be able to add them from the PlalistBuilder tab
 * App icon
 * * sunset screen for cleanup?
 */
public class TangoDJ2 extends Application 
{
  public final static String version = "1.5";
  static Stage primaryStage;
  static PlaylistBuilderTab playlistBuilderTab;
  static PlaylistChoiceTab playlistChoiceTab;
  static CortinaTab cortinaTab;
  static EventTab eventTab;
  public static Preferences prefs = new Preferences();
  static VBox playerPane;
  int sceneHeight=600;
  int sceneWidth=1150;
  
  MenuBar menuBar;
  Playlist playlist;
   
  Rectangle r = new Rectangle(10,10,10,10);
  Player player;
  public static Label feedback = new Label("");
  TangoTable tangoTable;
  CleanupTable cleanupTable;
  CortinaTable cortinaTable;
  TrackLoader3 trackLoader = new TrackLoader3();
  private Button infoWindowButton = new Button("Info Window");
  final URL stylesheet = getClass().getResource("style.css");
	
  public static void main(String[] args) 
  {
    launch(args);
  }
	
  public void start(Stage stage) 
  {
    boolean deploy = false;
    
    if (deploy) // Redirect system.out to tdj_error.txt file
    {
      try
      { 
        File f = new File("tdj_error.txt");
        if (f.exists()) f.delete();
        PrintStream ps = new PrintStream(
          new BufferedOutputStream(new FileOutputStream(
          new File("tdj_error.txt"))), true);
          System.setOut(ps);         
          System.setErr(ps);    
      } catch (Exception e) { e.printStackTrace(); }
    }
    
	primaryStage=stage;
	loadFonts();
	//feedback.setPrefWidth(800);
	infoWindowButton.setDisable(true);
	
	stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
    {
    public void handle(WindowEvent e)
    {
      savePreferences();
      try { Db.databaseDisconnect(); } catch (SQLException e1) { e1.printStackTrace();}
      if (player.infoWindow!=null) player.infoWindow.close();
      System.out.println("Exit");
    }
   });
	
	
	  VBox root = new VBox();
    Scene scene = new Scene(root, sceneWidth, sceneHeight, Color.WHITE);
    r.setFill(Color.RED);
   
    //final URL stylesheet = getClass().getResource("style.css");
    scene.getStylesheets().add(stylesheet.toString());

    CreateDatabase cb = new CreateDatabase();
    try {if (!cb.exists()) cb.create(); } catch (Exception e) { e.printStackTrace(); }
   
    try
    {
      Db.databaseConnect(false);
    } catch (ClassNotFoundException e1)
    {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (SQLException e1)
    {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    
    try { loadPreferences(); } 
    catch (Exception se) { System.out.println("PROGRAM ALREADY RUNNING"); System.exit(0); } 
    
    tangoTable = new TangoTable();
    cleanupTable = new CleanupTable();
    cortinaTable = new CortinaTable();
    trackLoader.setTangoTable(tangoTable);
    trackLoader.setCleanupTable(cleanupTable);
    
    TabPane tabPane = new TabPane();
    
    setupMenuBar();
      
    playlist = new Playlist(prefs.currentPlaylist); 
   // System.out.println("TangoDJ2 - new Playlist for playlist builder tab");
    
   // Tab equalizerTab = new Tab();
  //  equalizerTab.setStyle("-fx-background-color: #bfc2c7;");
  //  equalizerTab.setText("Equalizer");
    
    eventTab = new EventTab();
    player = new Player(eventTab);
    player.setPlaylist(playlist);
    eventTab.setPlayer(player);
    
   
    playlistBuilderTab = new PlaylistBuilderTab(playlist,  player, tangoTable, cleanupTable, cortinaTable);
    tabPane.getTabs().add(playlistBuilderTab);
    
    playlistChoiceTab = new PlaylistChoiceTab(this, player, playlistBuilderTab, eventTab);
    tabPane.getTabs().add(playlistChoiceTab);
    
    cortinaTab = new CortinaTab(player);
      
    // TAB SELECTION LISTENER
    tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>()
    {
      public void changed(ObservableValue<? extends Tab> arg0, Tab arg1, Tab mostRecentlySelectedTab)
      {
        if (mostRecentlySelectedTab.equals(cortinaTab))       
        {  
          player.setActiveTab(Player.CORTINA_CREATE_TAB);
        }
        if (mostRecentlySelectedTab.equals(playlistBuilderTab)) 
        { 
          playlistBuilderTab.reloadPlaylist();   // this has to happen first
          player.setActiveTab(Player.PLAYLIST_BUILDER_TAB); // before this
        }
        if (mostRecentlySelectedTab.equals(eventTab))           
        { 
          eventTab.reloadPlaylist();  // this has to happen first
          infoWindowButton.setDisable(false);
          player.setActiveTab(Player.EVENT_TAB);  // before this
        }
        else { infoWindowButton.setDisable(true); }
      }
    });
      
    //tabPane.getTabs().add(equalizerTab);
    tabPane.getTabs().add(cortinaTab);
    tabPane.getTabs().add(eventTab);
   
    VBox mainPane = new VBox();
    //mainPane.setStyle("-fx-background-color: DAE6F3; -fx-border-color: RED; -fx-border-style: SOLID; -fx-border-width: 1px;");     
    VBox.setVgrow(tabPane, Priority.ALWAYS);
    mainPane.getChildren().add(tabPane);
      
    feedback.setPrefWidth(sceneWidth-120);
    //feedback.setPrefHeight(30);
    //feedback.setMaxHeight(30);
   // feedback.setMinHeight(30);
    feedback.setStyle("-fx-background-color: #bfc2c7;");
   // VBox.setVgrow(feedback, Priority.NEVER);
    HBox feedbackBox = new HBox();
    feedbackBox.setPadding(new Insets(3, 0, 7, 20));  // top, right, bottom, left
    feedbackBox.setStyle("-fx-background-color: #bfc2c7;");
    feedbackBox.getChildren().add(feedback);
    feedbackBox.getChildren().add(infoWindowButton);
    VBox playerBox = player.get();
    mainPane.setVgrow(playerBox, Priority.NEVER);
    
    mainPane.getChildren().add(playerBox);
    mainPane.getChildren().add(feedbackBox);
    mainPane.minHeightProperty().bind(scene.heightProperty().subtract(20));
    mainPane.minWidthProperty().bind(scene.widthProperty());
    
    root.getChildren().addAll(menuBar, mainPane);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  // INFO WINDOW BUTTON ========================================================
  private void setupMenuBar()
  {
    infoWindowButton.setOnAction(new EventHandler<ActionEvent>() 
    {
      public void handle(ActionEvent actionEvent) 
      {
        if (player.infoWindow==null)  player.infoWindow=new InfoWindow2(EventTab.playlist, stylesheet);  
      }
   });
    
 // MENU BAR ========================================================
    menuBar = new MenuBar();
    Menu menuFile = new Menu("File");
   
    MenuItem menuAddTangoDir = new MenuItem("Add Tango Folder");
    MenuItem menuAddTangoFile = new MenuItem("Add Tango Track");
    MenuItem menuAddCleanupDir = new MenuItem("Add Non-Tango Folder");
    MenuItem menuAddCleanupFile = new MenuItem("Add Non-Tango Track");
    MenuItem menuAddCortinaFile = new MenuItem("Add Prepared Cortina Track");
    MenuItem menuAddCortinaDir = new MenuItem("Add Prepared Cortina Folder");
    MenuItem preferences = new MenuItem("Preferences");
    MenuItem about = new MenuItem("About");
    MenuItem manual = new MenuItem("TangoDJ Manual");
    
    Menu menuEdit = new Menu("Edit");
    Menu menuView = new Menu("View");
    Menu menuHelp = new Menu("Help");
    menuBar.getMenus().addAll(menuFile, menuEdit, menuView, menuHelp);
    
    menuAddTangoDir.setOnAction(new EventHandler<ActionEvent>() 
    {
      public void handle(ActionEvent t) 
      {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(prefs.tangoFolder));  // temporary 
        File selectedDirectory = 
        directoryChooser.showDialog(primaryStage);
               
        if(selectedDirectory == null) { System.out.println("No Directory selected"); } 
        else
        {
          prefs.tangoFolder=selectedDirectory.getParent().toString();  
          try
          {
            trackLoader.process(selectedDirectory, SharedValues.DIRECTORY, SharedValues.TANGO);
            //tangoTable.reloadData();
          } catch (Exception ex) {ex.printStackTrace();}
        }
      }
    });   
    menuAddCleanupDir.setOnAction(new EventHandler<ActionEvent>() 
    {
      public void handle(ActionEvent t) 
      {
    	  DirectoryChooser directoryChooser = new DirectoryChooser();
    	  directoryChooser.setInitialDirectory(new File(prefs.cleanupFolder));  // temporary 
    	  File selectedDirectory = directoryChooser.showDialog(primaryStage);
    	  if (selectedDirectory == null) { System.out.println("No Directory selected"); } 
    	  else
    	  {
    	    try
          {
            trackLoader.process(selectedDirectory, SharedValues.DIRECTORY, SharedValues.CLEANUP);
          } catch (Exception e) { e.printStackTrace();   }
    	  }
      }
    });   
    
    menuAddCortinaDir.setOnAction(new EventHandler<ActionEvent>() 
        {
          public void handle(ActionEvent t) 
          {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(prefs.cleanupFolder));  // temporary 
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory == null) { System.out.println("No Directory selected"); } 
            else
            {
              try
              {
                trackLoader.process(selectedDirectory, SharedValues.DIRECTORY, SharedValues.CORTINA);
              } catch (Exception e) { e.printStackTrace();   }
            }
          }
        });   
              
    	    
    menuAddTangoFile.setOnAction(new EventHandler<ActionEvent>() 
    {
      public void handle(ActionEvent t) 
      {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(prefs.tangoFolder));  // temporary 
        File selectedFile = 
        fileChooser.showOpenDialog(primaryStage);
                  
        if(selectedFile == null) { System.out.println("No File selected"); } 
        else
        {
          try
          {
            trackLoader.process(selectedFile, SharedValues.FILE, SharedValues.TANGO);
            //tangoTable.reloadData();
          } catch (Exception ex) {ex.printStackTrace();}
        }
      }
    });   
    
    menuAddCleanupFile.setOnAction(new EventHandler<ActionEvent>() 
    {
      public void handle(ActionEvent t) 
      {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(prefs.cleanupFolder));  // temporary 
        File selectedFile = 
        fileChooser.showOpenDialog(primaryStage);
              
        if(selectedFile == null) { System.out.println("No File selected"); } 
        else
        {
          prefs.cleanupFolder=selectedFile.getParent();  
          try
          {
            trackLoader.process(selectedFile, SharedValues.FILE, SharedValues.CLEANUP);
           //cleanupTable.reloadData();
          } catch (Exception ex) {ex.printStackTrace();}
        }
      }
    });   
    
     menuAddCortinaFile.setOnAction(new EventHandler<ActionEvent>() 
     {
       public void handle(ActionEvent t) 
       {
         FileChooser fileChooser = new FileChooser();
         fileChooser.setInitialDirectory(new File(prefs.cleanupFolder));  // temporary 
         File selectedFile = 
         fileChooser.showOpenDialog(primaryStage);
                      
         if(selectedFile == null) { System.out.println("No File selected"); } 
         else
         {
           try
           {
             prefs.cleanupFolder=selectedFile.getParent();  
             trackLoader.process(selectedFile, SharedValues.FILE, SharedValues.CORTINA);
             //cortinaTable.reloadData();
           } catch (Exception ex) {ex.printStackTrace();}
         }
       }
     });   
     
    	    
    preferences.setOnAction(new EventHandler<ActionEvent>() 
    { public void handle(ActionEvent t) { new PreferencesDialog(); }});
    
    about.setOnAction(new EventHandler<ActionEvent>() 
    { public void handle(ActionEvent t) { new AboutDialog(); }});
    
     manual.setOnAction(new EventHandler<ActionEvent>() 
     { public void handle(ActionEvent t) { new ManualDialog(); }});
    
    menuFile.getItems().addAll(menuAddTangoDir, menuAddTangoFile,menuAddCleanupDir, menuAddCleanupFile,menuAddCortinaDir,menuAddCortinaFile);
    menuEdit.getItems().add(preferences);
    menuHelp.getItems().addAll(about, manual);
  }
  
  private void loadFonts()
  {
	Font.loadFont(TangoDJ2.class.getResource("/resources/fonts/Carousel.ttf").toExternalForm(), 10  );
	Font.loadFont(TangoDJ2.class.getResource("/resources/fonts/Anagram.ttf").toExternalForm(), 10  );
	Font.loadFont(TangoDJ2.class.getResource("/resources/fonts/Carrington.ttf").toExternalForm(), 10  );
	Font.loadFont(TangoDJ2.class.getResource("/resources/fonts/DEFTONE.ttf").toExternalForm(), 10  );
	Font.loadFont(TangoDJ2.class.getResource("/resources/fonts/EastMarket.ttf").toExternalForm(), 10  );
	Font.loadFont(TangoDJ2.class.getResource("/resources/fonts/england.ttf").toExternalForm(), 10  );
	Font.loadFont(TangoDJ2.class.getResource("/resources/fonts/FFF_Tusj.ttf").toExternalForm(), 10  );
  }

  private void loadPreferences() throws Exception
  {
	 prefs=Db.getPreferences();
	 //System.out.println("TangoDJ2 current playlist: "+prefs.currentPlaylist);
  }
  
  public void changePlaylist(int playlistId)
  {
	try 
	{
	  playlist = new Playlist(playlistId);
	} catch (Exception e) {e.printStackTrace();};
  }
  
  public void savePreferences()
  {
      int numberOfCols= playlistBuilderTab.getTangoTable().getColumns().size();
      TableColumn t;
      String columnName;
      double width;
      boolean visible;
      for(int i=0; i<numberOfCols; i++)
      {
        t = playlistBuilderTab.getTangoTable().getColumns().get(i);
        columnName=t.getText();
        width=t.getWidth();
        visible=t.isVisible();
        Db.updateTangoTableColumnVisible(columnName+"Visibility", visible);
        Db.updateTangoTableColumnWidth(columnName+"Width", width);
       // System.out.println(i+") "+columnName+", "+width+", "+visible);
      }
  }
  
  
}
