package tangodj2;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
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
	
  public static void main(String[] args) 
  {
    launch(args);
  }
	
  public void start(Stage stage) 
  {
	  primaryStage=stage;
	  Group root = new Group();
      Scene scene = new Scene(root, 950, 550, Color.WHITE);
    
      TabPane tabPane = new TabPane();
      BorderPane mainPane = new BorderPane();
          
      CreateDatabase cb = new CreateDatabase();
    
      try {if (!cb.exists()) cb.create(); } catch (Exception e) { e.printStackTrace(); }
      
      
      
      setupListeners();
     
      allTracksTab = new AllTracksTab();
      tabPane.getTabs().add(allTracksTab.getTab());
      
      allPlaylistsTab = new AllPlaylistsTab();
      tabPane.getTabs().add(allPlaylistsTab.getTab());
    
          
      Tab tabC = new Tab();
      tabC.setText("Tab C");
      tabPane.getTabs().add(tabC);
    
      mainPane.setCenter(tabPane);
    
      mainPane.prefHeightProperty().bind(scene.heightProperty());
      mainPane.prefWidthProperty().bind(scene.widthProperty());
    
      root.getChildren().add(mainPane);
      primaryStage.setScene(scene);
      primaryStage.show();
  }

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
}
