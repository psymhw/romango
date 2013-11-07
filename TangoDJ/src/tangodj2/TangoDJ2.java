package tangodj2;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/*
 * TODO export playlist?
 * MP3 Tagtools
 * CDEX
 * Get rating from iTunes XML file?
 * Create a tangoGenre MP3tag and populate when making tandas?
 * MP3 tag editor
 * Event Tab: fade/next track
 * table search or restriction or fulltext
 * Spare DB fields for updates
 * try mediaPlayer.setOnReady instead of Timeline for loading MP3 tag info
 * Add treeitems below song title to show artist, time, album etc
 * Show total time for each tanda. Maybe even real end time from system clock
 * Add pre-made cortinas directly to cortinas table
 * When making cotinas, there should be a length counter
 *   from the time the set start position button is pressed
 * Tooltips on TreeItems (treeCell?) to show time, album, etc
 * Need a file for Orchestra, Singer, Band Leader (principles) 
 *    then a pointer from the tracks file
 * Spell check band leaders
 * Create playlists from tandas.
 * Change cleanup to non-tango
 * Handle bad directory address in preferences
 * Need an info line to show loading progress and error messages
 * Song adjectives: instramental, duet, female singer,peppy,perky
 * modern, dreamy, dramatic, slow, mediun, alternative, better, best
 * lots of feelings, heavy vocals, fast, old scratchy, sweet
 * rythmic, romantic, uplifting, others better, late night, 
 * mysterious, candombe, mild, special, trumpet, muted trumpet, clarinet
 * traspie, fun, high energy, paso double, horns, Big orchestra,
 * weak, piano, mellow, long intro, oboe, whistles, driving beat,
 * violins, crooner, happy, better, glosa (talking at beginning), best
 * bandonian, favored by other DJs, too old, wordy, sax, guitar
 * worst, challenging, complex rhythm, great vocals, interesting
 * breaks, fast, changes, lyrical, strings, wanders, drama, noises,
 * whimsical, minor, low energy, delightful, poor quality, lovley
 * flat, shrill, lush, confusing beat, busy, novelty, sad, angry
 * medium, inconsistent rhythm, anguish, laughing, sounds
 * melancholy, funky, organ, funny voices, live
 * Select tango table columns to show
 * Save column widths too?
 * Oh Jeeze, what about column position?
 * Add BPM to tracks?
 * Add lyrics?
 * Guess STYLE from title, genre and comment
 * Principle as band leader last name only. Artist has full artist name
 * Need track number in database in case I want to identify songs by album and track #
 */
public class TangoDJ2 extends Application 
{
  static Stage primaryStage;
  static PlaylistBuilderTab playlistBuilderTab;
  static PlaylistChoiceTab playlistChoiceTab;
  static CortinaTab cortinaTab;
  static EventTab eventTab;
  public static Preferences prefs = new Preferences();
  
  MenuBar menuBar;
  Playlist playlist;
   
  Rectangle r = new Rectangle(10,10,10,10);
  Player player;
  public static Label feedback = new Label("FEEDBACK ");
 
  
	
  public static void main(String[] args) 
  {
    launch(args);
  }
	
  public void start(Stage stage) 
  {
	primaryStage=stage;
	loadFonts();
	feedback.setPrefWidth(800);
	
	stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
    {
    public void handle(WindowEvent e)
    {
      savePreferences();
      System.out.println("Exit");
    }
   });
	
	
	  VBox root = new VBox();
    Scene scene = new Scene(root, 1200, 700, Color.WHITE);
    r.setFill(Color.RED);
   
    final URL stylesheet = getClass().getResource("style.css");
    scene.getStylesheets().add(stylesheet.toString());

    CreateDatabase cb = new CreateDatabase();
    try {if (!cb.exists()) cb.create(); } catch (Exception e) { e.printStackTrace(); }
    loadPreferences();
    
    TabPane tabPane = new TabPane();
    BorderPane mainPane = new BorderPane();
    setupMenuBar();
      
    try { playlist = new Playlist(prefs.currentPlaylist);} 
    catch (SQLException se) { System.out.println("PROGRAM ALREADY RUNNING"); System.exit(0); } 
    catch (ClassNotFoundException e) { e.printStackTrace(); }
        
    Tab equalizerTab = new Tab();
  //  equalizerTab.setStyle("-fx-background-color: #bfc2c7;");
    equalizerTab.setText("Equalizer");
    
    player = new Player(equalizerTab);
    player.setPlaylist(playlist);
    eventTab = new EventTab(player);
   
    playlistBuilderTab = new PlaylistBuilderTab(playlist,  player);
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
          player.setFeaturesMode(Player.CORTINA_CREATE);
        }
        if (mostRecentlySelectedTab.equals(playlistBuilderTab))
        {
          player.setFeaturesMode(Player.PLAYLIST_CREATE);
          player.setPlaylist(playlist);
        }
        if (mostRecentlySelectedTab.equals(eventTab))
        {
          player.setFeaturesMode(Player.PLAYLIST_CREATE);
          player.setPlaylist(eventTab.playlist);
        }
      }
    });
      
    tabPane.getTabs().add(equalizerTab);
    tabPane.getTabs().add(cortinaTab);
    tabPane.getTabs().add(eventTab);
    
    mainPane.setTop(tabPane);
    mainPane.setCenter(player.get());
    mainPane.setBottom(feedback);
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
   
    MenuItem menuAddTangoDir = new MenuItem("Add Tango Folder");
    MenuItem menuAddTangoFile = new MenuItem("Add Tango Track");
    MenuItem menuAddCleanupDir = new MenuItem("Add Cortina\\Cleanup Folder");
    MenuItem menuAddCleanupFile = new MenuItem("Add Cortina\\Cleanup Track");
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
          playlistBuilderTab.loadTangoDirectory(selectedDirectory.toPath().toString());
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
    	  playlistBuilderTab.loadCleanupDirectory(selectedDirectory.toPath().toString());
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
          playlistBuilderTab.loadTangoFile(selectedFile.toPath().toString());
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
          playlistBuilderTab.loadCleanupFile(selectedFile.toPath().toString());
        }
      }
    });   
    	    
    preferences.setOnAction(new EventHandler<ActionEvent>() 
    { public void handle(ActionEvent t) { new PreferencesDialog(); }});
    
    about.setOnAction(new EventHandler<ActionEvent>() 
    { public void handle(ActionEvent t) { new AboutDialog(); }});
    
     manual.setOnAction(new EventHandler<ActionEvent>() 
     { public void handle(ActionEvent t) { new ManualDialog(); }});
    
    menuFile.getItems().addAll(menuAddTangoDir, menuAddTangoFile,menuAddCleanupDir, menuAddCleanupFile);
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

  private void loadPreferences()
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
