package tangodj2.test;

import java.io.File;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3v1;
import org.farng.mp3.id3.AbstractID3v2;




public class TreeTest extends Application
{
  static Stage primaryStage;
  File file;
  TreeItem<Text> canaro;
  TreeItem<Text> newitem;
  ArrayList<TreeItem<Text>> tandas;
  TreeView treeView;
  
  public static void main(String[] args) {  launch(args);}
  
  public void start(Stage stage) 
  {
	primaryStage=stage;
	Group rootGroup = new Group();
	VBox vbox = new VBox();
	rootGroup.getChildren().add(vbox);
	Scene scene = new Scene(rootGroup, 950, 550, Color.WHITE);
	primaryStage.setScene(scene);
	
	Button btn = new Button();
	btn.setText("Add Item");
	btn.setOnAction(new EventHandler<ActionEvent>() 
	{
      public void handle(ActionEvent event) 
      {
    	  newitem = new TreeItem<Text>(new Text("New Item"));
    	  tandas.get(0).getChildren().add(newitem);
      }
    });
	      
	Button btn2 = new Button();
	btn2.setText("Remove Item");
	btn2.setOnAction(new EventHandler<ActionEvent>() 
	{
      public void handle(ActionEvent event) 
      {
    	  tandas.get(0).getChildren().remove(newitem);
      }
    });
	      
	TreeItem<Text> playlist = new TreeItem<Text>(new Text("Test Playlist"));
	
	playlist.setExpanded(true);
	
	tandas = new ArrayList<TreeItem<Text>>();
	tandas.add(new TreeItem<Text>(new Text("Canaro - VALS")));
	tandas.add(new TreeItem<Text>(new Text("Di Sarli - Tango")));
	tandas.add(new TreeItem<Text>(new Text("Biagi - Milonga")));
	
	Text track1_text = new Text("Track 1");
	track1_text.setFont(Font.font("Serif", 20));
	TrackTreeItem track1 = new TrackTreeItem(track1_text);
	track1.setTrackHash("1234567");
	tandas.get(0).getChildren().add(track1);
	tandas.get(0).getChildren().add(new TreeItem<Text>(new Text("Track 2")));
	tandas.get(0).getChildren().add(new TreeItem<Text>(new Text("Track 3")));
	playlist.getChildren().addAll(tandas);
	
	
	/*
	canaro = new TreeItem<Text>(new Text("Canaro - VALS"));
	canaro.getChildren().add(track1);
	canaro.getChildren().add(new TreeItem<Text>(new Text("Track 2")));
	playlist.getChildren().addAll(canaro, 
	          new TreeItem<Text>(new Text("Di Sarli - Tango")),
	          new TreeItem<Text>(new Text("Biagi - Milonga"))
	      );
	//TreeViewWithItems treeView = new TreeViewWithItems(treeRoot);
	 */
	 
	treeView = new TreeView(playlist);
	treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	

	//treeView.setShowRoot(false);
	
	vbox.getChildren().add(btn);
	vbox.getChildren().add(btn2);
	vbox.getChildren().add(treeView);
	
	primaryStage.show();
	
	ChangeListener<TreeItem<Text>> cl = new ChangeListener<TreeItem<Text>>() 
	{
		public void changed(ObservableValue<? extends TreeItem<Text>> observableValue, TreeItem<Text> oldItem, TreeItem<Text> newItem) 
		{
		   if (newItem!=null)
			System.out.println("Selected: " + ((TrackTreeItem)newItem).getTrackHash());
		}
		
		public void handle(ActionEvent event) 
		{
		  TreeItem selectedItem = getSelectedItem();

		  if (selectedItem == null) 
		  {
		    System.out.println("Error: You have to select a item in the tree.");
		            return;
		  }
	    }
		
		private TreeItem getSelectedItem() 
		  {
		   return (TreeItem) treeView.getSelectionModel().getSelectedItem();
		   }
	};
			 
	treeView.getSelectionModel().selectedItemProperty().addListener(cl);
	
  
}
}