package tangodj2;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class TangoDJ2 extends Application 
{
  static Stage primaryStage;
  SharedValues sharedValues = new SharedValues();
  static AllTracksTab allTracksTab;
	
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
          
      try { new CreateDatabase(); } catch (Exception e) { e.printStackTrace(); }
      setupListeners();
     
      allTracksTab = new AllTracksTab();
      tabPane.getTabs().add(allTracksTab.getTab());
    
      Tab tabB = new Tab();
      tabB.setText("Playlists");
      tabPane.getTabs().add(tabB);
    
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
