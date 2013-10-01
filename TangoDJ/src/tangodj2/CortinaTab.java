package tangodj2;

import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class CortinaTab extends Tab
{
 
  AllTracksTable allTracksTable;
  GridPane gridPane = new GridPane();
  
  public CortinaTab(AllTracksTable allTracksTable)
  {
    this.setText("Cortinas");
    this.allTracksTable=allTracksTable;
    
    gridPane.setGridLinesVisible(true);
    
      
    gridPane.setPadding(new Insets(10, 10, 10, 10));
   
    gridPane.setStyle("-fx-background-color: DAE6F3; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work
   
    gridPane.add(new Text("Source Tracks"), 0, 0);
    //gridPane.add(allTracksTable.getTable(), 1, 0);
    
    
   // hbox.getChildren().add(playlist.getTreeView());
   // hbox.setHgrow(playlist.getTreeView(), Priority.ALWAYS);
   
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
}
