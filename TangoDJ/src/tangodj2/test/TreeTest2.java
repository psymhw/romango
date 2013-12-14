package tangodj2.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TreeTest2 extends Application
{

  /**
   * @param args
   */
  public static void main(String[] args) 
  {
    launch(args);

  }

  @Override
  public void start(Stage stage) throws Exception
  {
    VBox vbox = new VBox();
    vbox.getChildren().add(new Label("Hello Tree World"));
    Scene scene = new Scene(vbox, 300, 300);
    stage.setScene(scene);
    
 // create the tree model
    CheckBoxTreeItem<String> jonathanGiles = new CheckBoxTreeItem<String>("Jonathan");
    CheckBoxTreeItem<String> juliaGiles = new CheckBoxTreeItem<String>("Julia");
    CheckBoxTreeItem<String> mattGiles = new CheckBoxTreeItem<String>("Matt");
    CheckBoxTreeItem<String> sueGiles = new CheckBoxTreeItem<String>("Sue");
    CheckBoxTreeItem<String> ianGiles = new CheckBoxTreeItem<String>("Ian");
    
    
    CheckBoxTreeItem<String> gilesFamily = new CheckBoxTreeItem<String>("Giles Family");
    gilesFamily.setExpanded(true);
    gilesFamily.getChildren().addAll(jonathanGiles, juliaGiles, mattGiles, sueGiles, ianGiles);
    
    // create the treeView
    final TreeView<String> treeView = new TreeView<String>();
    treeView.setRoot(gilesFamily);
          
    // set the cell factory
    treeView.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
    
    vbox.getChildren().add(treeView);
    
    stage.show();
    
  }

}
