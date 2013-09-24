package tangodj2;

import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/* TODO 
 * 
 * iTunes playlist import?
 * No duplicate hash codes in all tracks
 * Db close on window close
 * 
*/ 

public class TangoDJ2 extends Application 
{
  static Stage primaryStage;
  SharedValues sharedValues = new SharedValues();
  static AllTracksTab allTracksTab;
  static AllPlaylistsTab allPlaylistsTab;
  MenuBar menuBar;
  TrackLoader2 trackLoader = new TrackLoader2();
  public static Tab tabC;
 
	
  public static void main(String[] args) 
  {
    launch(args);
  }
	
  public void start(Stage stage) 
  {
	  primaryStage=stage;
	 // Group root = new Group();
	  VBox root = new VBox();
      Scene scene = new Scene(root, 950, 550, Color.WHITE);
      final URL stylesheet = getClass().getResource("style.css");
      scene.getStylesheets().add(stylesheet.toString());
      TabPane tabPane = new TabPane();
      BorderPane mainPane = new BorderPane();
          
      CreateDatabase cb = new CreateDatabase();
    
      try {if (!cb.exists()) cb.create(); } catch (Exception e) { e.printStackTrace(); }
      setupMenuBar();
      
      
     // setupListeners();
     
      allTracksTab = new AllTracksTab();
      tabPane.getTabs().add(allTracksTab.getTab());
      
      allPlaylistsTab = new AllPlaylistsTab();
      tabPane.getTabs().add(allPlaylistsTab.getTab());
    
          
      tabC = new Tab();
      tabC.setText("Equalizer");
      tabPane.getTabs().add(tabC);
    
      mainPane.setCenter(tabPane);
    
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
             trackLoader.process(selectedDirectory.toPath().toString());
           } catch (Exception ex) {ex.printStackTrace();}
         }
    }
  });   
    menuFile.getItems().addAll(menuAddTangoDir, menuAddTangoFile);
  }

  /*
  private void setupListeners() 
  {
    ChangeListener cl2 = new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        System.out.println("selected Path Hash: "+sharedValues.pathHash.get());
      }
    };   
    sharedValues.pathHash.addListener(cl2);
  }
  */
}
