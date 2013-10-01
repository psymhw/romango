package tangodj2;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class CortinaTab extends Tab
{
 
  AllTracksTable allTracksTable;
  GridPane gridPane = new GridPane();
  Player player;
  
  public CortinaTab(AllTracksTable allTracksTable, Player player)
  {
    this.setText("Cortinas");
    this.allTracksTable=allTracksTable;
    this.player=player;
    
    gridPane.setGridLinesVisible(true);
    
      
    gridPane.setPadding(new Insets(10, 10, 10, 10));
   
    gridPane.setStyle("-fx-background-color: DAE6F3; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work
   
    gridPane.add(new Text("Source Tracks"), 0, 0);
    //gridPane.add(allTracksTable.getTable(), 1, 0);
    
    
   // hbox.getChildren().add(playlist.getTreeView());
   // hbox.setHgrow(playlist.getTreeView(), Priority.ALWAYS);
    setupListeners();
    this.setContent(gridPane);
  }
  
  public void addAllTracksTable()
  {
    gridPane.add(allTracksTable.getTable(), 0, 1);
  }
  
  public void removeAllTracksTable()
  {
    gridPane.add(new Text("Table Displaced"), 0, 1);
  }
  
  private void setupListeners() 
  {
    ChangeListener tangoHashListener = new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
      //  System.out.println("selected Tango Hash: "+sharedValues.selectedTangoPathHash.get());
        player.updateUIValues(SharedValues.selectedCleanupPathHash.get());
      }
    };   
    SharedValues.selectedCleanupPathHash.addListener(tangoHashListener);
  }
  
}
