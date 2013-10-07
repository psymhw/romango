package tangodj2;

import tangodj2.cleanup.CleanupTable;
import tangodj2.cleanup.CleanupTrack;
import tangodj2.cortina.CortinaTable;
import tangodj2.cortina.CortinaTrack;

import tangodj2.tango.TangoTable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class CortinaTab extends Tab
{
 
  CleanupTable cleanupTable;
  CortinaTable cortinaTable;
  GridPane gridPane = new GridPane();
  final Player player;
  
  public CortinaTab(CleanupTable cleanupTable, CortinaTable cortinaTable, Player player)
  {
    this.setText("Cortinas");
    this.cleanupTable=cleanupTable;
    this.cortinaTable=cortinaTable;
    this.player=player;
    
    gridPane.setGridLinesVisible(true);
    
      
    gridPane.setPadding(new Insets(10, 10, 10, 10));
   
    gridPane.setStyle("-fx-background-color: DAE6F3; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work
    gridPane.add(cleanupTable, 0, 1);
    gridPane.add(new Text("Source Tracks"), 0, 0);
    
    gridPane.add(cortinaTable, 1, 1);
    //gridPane.add(allTracksTable.getTable(), 1, 0);
    
    
   // hbox.getChildren().add(playlist.getTreeView());
   // hbox.setHgrow(playlist.getTreeView(), Priority.ALWAYS);
   // setupListeners();
    this.setContent(gridPane);
    setupListener();
 
  }
  
  
  private void setupListener()
  {
 // MOUSE TABLE ROW SELECTION
    cleanupTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        CleanupTrack selectedTrack = (CleanupTrack)newValue;
        if (selectedTrack!=null)
        {
          player.setTrack(selectedTrack.getPathHash(), Player.CORTINA_CREATE_CLEANUP_TABLE);
        }
      }
    });
    
 // MOUSE TABLE ROW SELECTION
    cortinaTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        CortinaTrack cortinaTrack = (CortinaTrack)newValue;
        if (cortinaTrack!=null)
        {
          player.setTrack(cortinaTrack.getPathHash(), Player.CORTINA_CREATE_CORTINA_TABLE, cortinaTrack);
        }
      }
    });
  }
  
}
  
  /*  
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
  */

