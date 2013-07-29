package tangodj2;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;


public class AllTracksTab 
{
  Tab tab;
  Tanda tanda;
  AllTracks allTracks;
  SharedValues sharedValues = new SharedValues();
	 	
	public AllTracksTab()
	{
		  tab = new Tab();
	      tab.setText("All Tracks");
	      allTracks = new AllTracks();
	      
	      final Button addButton = new Button("Add");
	      addButton.setOnAction(new EventHandler<ActionEvent>() 
	      {
	        public void handle(ActionEvent e) 
	        {
	          DirectoryChooser directoryChooser = new DirectoryChooser();
	          directoryChooser.setInitialDirectory(new File("C:\\music\\tango"));  // temporary 
	          File selectedDirectory = 
	          directoryChooser.showDialog(TangoDJ2.primaryStage);
	              
	          if(selectedDirectory == null)
	          {
	             System.out.println("No Directory selected");
	          } 
	          else
	          {
	             try
	             {
	            	
	              	TrackLoader trackLoader = new TrackLoader(selectedDirectory.toPath().toString());
	              	allTracks.clearTable(); 
	              	allTracks.setData();
	              } catch (Exception ex) {ex.printStackTrace();}
	          }
	        }
	      });
	     
	      

	      final Label label = new Label("All Tracks");
	      label.setFont(new Font("Arial", 20));
	      
	      final VBox vbox = new VBox();
	      vbox.setPadding(new Insets(10, 0, 0, 10));
	      vbox.getChildren().addAll(label, allTracks.getTable(), addButton);
	      
	      HBox hbox =  new HBox();
	      hbox.setPadding(new Insets(10, 0, 0, 10));
	      
	      
	      hbox.getChildren().add(vbox);
	      
	      tanda = new Tanda("Canaro", "Vals");
	      hbox.getChildren().add(tanda.getTanda());
	      
	      //((Group) scene.getRoot()).getChildren().addAll(vbox);
	      setupListeners();
	      tab.setContent(hbox);
	}
	
	public Tab getTab()
	{
		return tab;
	}
	
	private void setupListeners() 
	{
	  ChangeListener cl = new ChangeListener() 
	  {
	      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
	      {
	        tanda.addTrack(new TandaTrack(sharedValues.title.get()));
	      }
	    };   
	      sharedValues.playlistTrackAdd.addListener(cl);
	}
}
