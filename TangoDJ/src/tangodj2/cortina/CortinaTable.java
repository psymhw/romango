package tangodj2.cortina;

import tangodj2.Db;
import tangodj2.SharedValues;
import tangodj2.tango.TangoTrack;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class CortinaTable extends TableView<CortinaTrack>
{
  public final static ObservableList<CortinaTrack> cortinaTracksData = FXCollections.observableArrayList();
  
  public CortinaTable()
  {
    Db.loadCortinaTracks(cortinaTracksData);
	  setupTable();
  }
	 
  public static void loadData()
  {
    Db.loadCortinaTracks(cortinaTracksData);
  }
  
  public static void addTrack(int id)
  {
    cortinaTracksData.add(Db.getCortinaTrack(id));
  }
  
	 
  
  private void setupTable() 
  {
		    
    this.setItems(cortinaTracksData);
    this.setEditable(false);
	    
   // table.setOnKeyReleased(keyEvent);
    
    TableColumn titleCol = new TableColumn("Title");
    titleCol.setMinWidth(100);
    titleCol.setPrefWidth(150);
    titleCol.setCellValueFactory(new PropertyValueFactory<Cortina, String>("title"));
    titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
    
    TableColumn lengthCol = new TableColumn("Length");
    lengthCol.setMinWidth(40);
    lengthCol.setPrefWidth(40);
    lengthCol.setCellValueFactory(new PropertyValueFactory<Cortina, String>("length"));
    lengthCol.setCellFactory(TextFieldTableCell.forTableColumn());
    
    TableColumn startCol = new TableColumn("Start");
    startCol.setMinWidth(40);
    startCol.setPrefWidth(40);
    startCol.setCellValueFactory(new PropertyValueFactory<Cortina, String>("start"));
    startCol.setCellFactory(TextFieldTableCell.forTableColumn());
    
    this.getColumns().addAll(titleCol, startCol, lengthCol);
  }
}
