package test;

import java.io.File;

import tangodj2.Equalizer;
import tangodj2.TitleGridPane;
import tangodj2.TitleGridPane2;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
*
* @web http://java-buddy.blogspot.com/
*/
public class TabDemo extends Application {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
      launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
      primaryStage.setTitle("http://java-buddy.blogspot.com/");
      Group root = new Group();
      Scene scene = new Scene(root, 400, 300, Color.WHITE);
    
      TabPane tabPane = new TabPane();
      BorderPane mainPane = new BorderPane();
    
      //Create Tabs
      Tab tabA = new Tab();
      tabA.setText("Tab A");
      tabPane.getTabs().add(tabA);
    
      Tab tabB = new Tab();
      tabB.setText("Tab B");
      tabPane.getTabs().add(tabB);
    
      Tab tabC = new Tab();
      tabC.setText("Tab C");
      tabPane.getTabs().add(tabC);
    
      mainPane.setCenter(tabPane);
    
      mainPane.prefHeightProperty().bind(scene.heightProperty());
      mainPane.prefWidthProperty().bind(scene.widthProperty());
      
      //File file = new File("C:\\music\\tango\\ClaroDeLuna.mp3");
     // VBox vbox = new VBox();
     // vbox.getChildren().add(new Label("Equalizer"));
     
      
      
      
     // final Media media = new Media(file.toURI().toString());
    //  final MediaPlayer mp = new MediaPlayer(media);
    //  mp.play();
      
    //  Equalizer eq = new Equalizer(mp);
     // vbox.getChildren().add(eq.getPane());
      
      
      //vbox.setAlignment(Pos.CENTER);
     // vbox.setStyle("-fx-background-color: BLACK; -fx-border-color: RED; -fx-border-style: SOLID; -fx-border-width: 1px;"); 
      
      TitleGridPane2 titleGridPane2 = new TitleGridPane2("Some Title");
      titleGridPane2.addRow("Some Label",new Label("someValue"));
      titleGridPane2.addRow("Label",new Label("aValue"));
      
      VBox vbox2 = new VBox();
      vbox2.getChildren().add(titleGridPane2);
      
      tabA.setContent(vbox2);
      
      TitleGridPane titleGridPane = new TitleGridPane("Some Title");
      titleGridPane.addRow("someLabel",new Label("someValue"));
      titleGridPane.addRow("label",new Label("aValue"));
      
      tabB.setContent(titleGridPane);
    
      SplitPane sp = new SplitPane();
      final StackPane sp1 = new StackPane();
      sp1.getChildren().add(new Button("Button One"));
      final StackPane sp2 = new StackPane();
      sp2.getChildren().add(new Button("Button Two"));
      final StackPane sp3 = new StackPane();
      sp3.getChildren().add(new Button("Button Three"));
      
      sp.getItems().addAll(sp1, sp2, sp3);
      sp.setDividerPositions(0.3f, 0.6f, 0.9f);
      
      tabC.setContent(sp);
      
      root.getChildren().add(mainPane);
      primaryStage.setScene(scene);
      primaryStage.show();
  }
}
