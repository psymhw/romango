package tangodj2.cortina;

import tangodj2.Db;
import tangodj2.SharedValues;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class CortinaTable extends TableView<CortinaTrack>
{
  public final static ObservableList<CortinaTrack> cortinaTracksData = FXCollections.observableArrayList();
  private SimpleStringProperty action = new SimpleStringProperty("nada");
  private int tableIndex=-1;
  private CortinaTable cortinaTable=this;
  
  public CortinaTable()
  {
    Db.loadCortinaTracks(cortinaTracksData);
   
	  setupTable();
  }
	 
  public static void reloadData()
  {
    cortinaTracksData.clear();
    Db.loadCortinaTracks(cortinaTracksData);
  }
  
  public static void addTrack(int id)
  {
    cortinaTracksData.add(Db.getCortinaTrack(id));
  }
  
  private final class  MyCellFactory implements Callback<TableColumn,TableCell> 
  {
    private ContextMenu contextMenu;
    public MyCellFactory() 
    {
      contextMenu = setupContextMenu();
    }

    public TableCell call(TableColumn p) 
    {
      TableCell cell = new TableCell() {
              
      protected void updateItem(Object item, boolean empty) 
      {
        super.updateItem(item, empty);
        if(item != null) 
        { 
         // this.setStyle("-fx-background-color:rgba(219, 42, 199,.41);");
          this.getStyleClass().add("cortinaTableText");
          // see http://rgba.it/
         // this.setTextFill(Color.web("#0076a3"));
          
          setText(item.toString()); 
        }
      }};
          
      cell.setContextMenu(contextMenu);
   
      return cell;
    }
  }
    
    private ContextMenu setupContextMenu()
    {
      MenuItem addToTanda = new MenuItem("Add To Tanda"); 
      MenuItem edit = new MenuItem("Edit");
      MenuItem play = new MenuItem("Play" );
      MenuItem delete = new MenuItem("Delete" );
      final ContextMenu tandaContextMenu = new ContextMenu();
      
      tandaContextMenu.setOnShowing(new EventHandler() 
      {
        public void handle(Event e) 
        {
          tableIndex=cortinaTable.getSelectionModel().getSelectedIndex();
        }
      });
      
      tandaContextMenu.getItems().addAll(addToTanda, edit, play);
      addToTanda.setOnAction(new EventHandler() 
        { public void handle(Event t) { action.set("addToTanda"); }});
      edit.setOnAction(new EventHandler() 
        { public void handle(Event t) { action.set("edit"); } });
      play.setOnAction(new EventHandler() 
        { public void handle(Event t) { action.set("play"); }});
      delete.setOnAction(new EventHandler() 
        { public void handle(Event t) { action.set("delete"); }});
      return tandaContextMenu;
    } 
  
  
  private void setupTable() 
  {
		    
    this.setItems(cortinaTracksData);
    this.setEditable(false);
    
    MyCellFactory myCellFactory = new MyCellFactory();
	    
   // table.setOnKeyReleased(keyEvent);
    
    TableColumn titleCol = new TableColumn("Title");
    titleCol.setMinWidth(100);
    titleCol.setPrefWidth(150);
    titleCol.setCellValueFactory(new PropertyValueFactory<Cortina, String>("title"));
    titleCol.setCellFactory(myCellFactory);
    titleCol.setStyle("-fx-background-color:rgba(219, 42, 199,.41);");
    
    TableColumn lengthCol = new TableColumn("Length");
    lengthCol.setMinWidth(50);
    lengthCol.setPrefWidth(50);
    lengthCol.setCellValueFactory(new PropertyValueFactory<Cortina, String>("length"));
    lengthCol.setCellFactory(myCellFactory);
    lengthCol.setStyle("-fx-background-color:rgba(219, 42, 199,.41);");
    
    TableColumn artistCol = new TableColumn("Artist");
    artistCol.setMinWidth(50);
    artistCol.setPrefWidth(100);
    artistCol.setCellValueFactory(new PropertyValueFactory<Cortina, String>("artist"));
    artistCol.setCellFactory(new MyCellFactory());
    artistCol.setStyle("-fx-background-color:rgba(219, 42, 199,.41);");
    
    TableColumn albumCol = new TableColumn("Album");
    albumCol.setMinWidth(50);
    albumCol.setPrefWidth(100);
    albumCol.setCellValueFactory(new PropertyValueFactory<Cortina, String>("album"));
    albumCol.setCellFactory(new MyCellFactory());
    albumCol.setStyle("-fx-background-color:rgba(219, 42, 199,.41);");
    
    
    TableColumn startCol = new TableColumn("Start");
    startCol.setMinWidth(40);
    startCol.setPrefWidth(40);
    startCol.setCellValueFactory(new PropertyValueFactory<Cortina, String>("start"));
    startCol.setCellFactory(myCellFactory);
    startCol.setStyle("-fx-background-color:rgba(219, 42, 199,.41);");
    this.getColumns().addAll(titleCol, lengthCol, artistCol, albumCol, startCol);
  }

  public int getTableIndex()
  {
    return tableIndex;
  }

  public SimpleStringProperty getAction()
  {
    return action;
  }
}
