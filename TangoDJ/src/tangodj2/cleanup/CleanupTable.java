package tangodj2.cleanup;

import java.util.ArrayList;

import javafx.application.Platform;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import tangodj2.Db;
import tangodj2.Playlist;
import tangodj2.SharedValues;
import tangodj2.PlaylistTree.TandaTreeItem;
import tangodj2.tango.TangoTrack;

public class CleanupTable extends TableView<CleanupTrack>
{
  private SimpleStringProperty action = new SimpleStringProperty("nada");
  private static TableView<CleanupTrack> cleanupTable;
  private int tableIndex=-1;
  
  public final static ObservableList<CleanupTrack> cleanupTracksData = FXCollections.observableArrayList();
	 
  public CleanupTable()
  {
	  this.cleanupTable=this;
	  setupTable();
	  reloadData();
  }
  
  public static void reloadData()
  {
    cleanupTable.getSortOrder().clear();
    Db.loadCleanupTracks(null);
  }
  
  public static void reloadData(final String search)
  {
    Platform.runLater(new Runnable() 
    {
      public void run() 
      {
    Db.loadCleanupTracks(search);
    ArrayList<TableColumn<CleanupTrack, ?>> sortOrder = new ArrayList<>(cleanupTable.getSortOrder());
    cleanupTable.getSortOrder().clear();
    cleanupTable.getSortOrder().addAll(sortOrder);
      }});
  }
  
  
  /*
   * Platform.runLater(new Runnable() 
    {
      public void run() 
      {
        Db.loadTangoTracks(search);
       ArrayList<TableColumn<TangoTrack, ?>> sortOrder = new ArrayList<>(tangoTable.getSortOrder());
       tangoTable.getSortOrder().clear();
       tangoTable.getSortOrder().addAll(sortOrder);
      }
    });
  }
  
  public static void reloadData()
  {
    tangoTable.getSortOrder().clear();
    reloadData(null);
    
  }
   */
	 
  private void setupTable() 
  {
    setupColumns();
    this.setItems(cleanupTracksData);
  //  table.setPrefWidth(tableWidth);
  //  table.setMinWidth(tableWidth);
  //  table.setMaxWidth(tableWidth);
    this.setEditable(false);
   
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
          this.getStyleClass().add("cleanupTableText");
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
          tableIndex=cleanupTable.getSelectionModel().getSelectedIndex();
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
  
		  
	   private void setupColumns()
	   {
		   TableColumn titleCol = new TableColumn("Title");
		   titleCol.setMinWidth(100);
		   titleCol.setPrefWidth(150);
		   titleCol.setCellValueFactory(new PropertyValueFactory<CleanupTrack, String>("title"));
		   titleCol.setCellFactory(new MyCellFactory());
		       
		  TableColumn artistCol = new TableColumn("Artist");
		  artistCol.setMinWidth(50);
		  artistCol.setPrefWidth(100);
		  artistCol.setCellValueFactory(new PropertyValueFactory<CleanupTrack, String>("artist"));
		  artistCol.setCellFactory(new MyCellFactory());
		      
		  TableColumn albumCol = new TableColumn("Album");
		  albumCol.setMinWidth(50);
		  albumCol.setPrefWidth(150);
		  albumCol.setCellValueFactory(new PropertyValueFactory<CleanupTrack, String>("album"));
		  albumCol.setCellFactory(new MyCellFactory());
		      
		  TableColumn genreCol = new TableColumn("Genre");
		  genreCol.setMinWidth(30);
		  genreCol.setPrefWidth(50);
		  genreCol.setCellValueFactory(new PropertyValueFactory<CleanupTrack, String>("genre"));
		  genreCol.setCellFactory(new MyCellFactory());
		      
		  TableColumn commentCol = new TableColumn("Comment");
		  commentCol.setMinWidth(50);
		  commentCol.setPrefWidth(100);
		  commentCol.setCellValueFactory(new PropertyValueFactory<CleanupTrack, String>("comment"));
		  commentCol.setCellFactory(new MyCellFactory());
		 		      
		  TableColumn durationCol = new TableColumn("Length");
		  durationCol.setMinWidth(50);
		  durationCol.setPrefWidth(50);
		  durationCol.setCellValueFactory(new PropertyValueFactory<CleanupTrack, String>("duration"));
		  durationCol.setCellFactory(new MyCellFactory());
		      
		  TableColumn yearCol = new TableColumn("Year");
		  yearCol.setMinWidth(50);
		  yearCol.setPrefWidth(50);
		  yearCol.setCellValueFactory(new PropertyValueFactory<CleanupTrack, String>("track_year"));
		  yearCol.setCellFactory(new MyCellFactory());
		     
		  this.getColumns().addAll(titleCol, durationCol, yearCol, artistCol, albumCol, genreCol, commentCol);
		      
	  }
		

	  public SimpleStringProperty getAction()
    {
      return action;
    }


    public int getTableIndex()
    {
      return tableIndex;
    }


}
