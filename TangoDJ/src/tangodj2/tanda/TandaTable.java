package tangodj2.tanda;

import java.util.ArrayList;

import tangodj2.Db;
import tangodj2.TandaDb;
import tangodj2.tango.TangoTrack;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.util.Callback;

public class TandaTable  extends TableView<TandaTrack>
{
  private SimpleStringProperty action = new SimpleStringProperty("nada");
  static TableView<TandaTrack> tandaTable;
  private int tableIndex=-1;
  private final static ObservableList<TandaTrack> tandaTracksData = FXCollections.observableArrayList();

  public TandaTable()
  {
	tandaTable=this;
	setupTable();
	reloadData();
  }
  
  public static void reloadData()
  {
    Platform.runLater(new Runnable() 
    {
      public void run() 
      {
        tandaTracksData.clear();
        tandaTracksData.addAll(Db.loadTandaTracks());
        // reestablish the sort order.
       ArrayList<TableColumn<TandaTrack, ?>> sortOrder = new ArrayList<>(tandaTable.getSortOrder());
       tandaTable.getSortOrder().clear();
       tandaTable.getSortOrder().addAll(sortOrder);
      }
    });
  }

  public void update(long tandaDbId)
  {
	int counter=0;  
	for(TandaTrack tandaTrack : tandaTracksData )
	{
	  if (tandaTrack.getDbId()==tandaDbId)
	  {
		TandaDb tandaDb = Db.getTanda(tandaDbId);
		// need to remove and re-add because observable list doesn't fire a change on a cell
		tandaTracksData.remove(counter);
		tandaTracksData.add(counter, new TandaTrack(tandaDb));
		tandaTrack.update(tandaDb);
		break;
	  }
	  counter++;
	}
  }

  private void setupTable() 
  {
	  setupColumns();
	    
	  this.setItems(tandaTracksData);
	  this.setEditable(false);
  }

  private void setupColumns() 
  {
	int titleWidth=135;  
	// ARTIST COLUMN 
	TableColumn artistCol = new TableColumn("Artist");
	artistCol.setMinWidth(50);
	artistCol.setPrefWidth(100);
	artistCol.setCellValueFactory(new PropertyValueFactory<TandaTrack, String>("artist"));
	artistCol.setCellFactory(new MyCellFactory());
	artistCol.setVisible(true);
	
	// STYLE COLUMN
    TableColumn styleCol = new TableColumn("Style");
    styleCol.setMinWidth(30);
    styleCol.setPrefWidth(70);
    styleCol.setCellValueFactory(new PropertyValueFactory<TandaTrack, String>("style"));
    styleCol.setCellFactory(new MyCellFactory());
    styleCol.setVisible(true);
    
 // PLAYLIST NAME
    TableColumn playlistNameCol = new TableColumn("Playlist");
    playlistNameCol.setMinWidth(50);
    playlistNameCol.setPrefWidth(140);
    playlistNameCol.setCellValueFactory(new PropertyValueFactory<TandaTrack, String>("playlistName"));
    playlistNameCol.setCellFactory(new MyCellFactory());
    playlistNameCol.setVisible(true);
    
 //  COMMENT
    TableColumn commentCol = new TableColumn("Comment");
    commentCol.setMinWidth(50);
    commentCol.setPrefWidth(150);
    commentCol.setCellValueFactory(new PropertyValueFactory<TandaTrack, String>("Comment"));
    commentCol.setCellFactory(new MyCellFactory());
    commentCol.setVisible(true);
    
 //  Track0
    TableColumn track0Col = new TableColumn("Track 1");
    track0Col.setMinWidth(50);
    track0Col.setPrefWidth(titleWidth);
    track0Col.setCellValueFactory(new PropertyValueFactory<TandaTrack, String>("track_0_title"));
    track0Col.setCellFactory(new MyCellFactory());
    track0Col.setVisible(true);
    
 //  Track1
    TableColumn track1Col = new TableColumn("Track 2");
    track1Col.setMinWidth(50);
    track1Col.setPrefWidth(titleWidth);
    track1Col.setCellValueFactory(new PropertyValueFactory<TandaTrack, String>("track_1_title"));
    track1Col.setCellFactory(new MyCellFactory());
    track1Col.setVisible(true);
    
 //  Track2
    TableColumn track2Col = new TableColumn("Track 3");
    track2Col.setMinWidth(50);
    track2Col.setPrefWidth(titleWidth);
    track2Col.setCellValueFactory(new PropertyValueFactory<TandaTrack, String>("track_2_title"));
    track2Col.setCellFactory(new MyCellFactory());
    track2Col.setVisible(true);
    
 //  Track3
    TableColumn track3Col = new TableColumn("Track 4");
    track3Col.setMinWidth(50);
    track3Col.setPrefWidth(titleWidth);
    track3Col.setCellValueFactory(new PropertyValueFactory<TandaTrack, String>("track_3_title"));
    track3Col.setCellFactory(new MyCellFactory());
    track3Col.setVisible(true);
    
 //  Cortina title
    TableColumn cortinaTitleCol = new TableColumn("Cortina");
    cortinaTitleCol.setMinWidth(50);
    cortinaTitleCol.setPrefWidth(titleWidth);
    cortinaTitleCol.setCellValueFactory(new PropertyValueFactory<TandaTrack, String>("cortina_title"));
    cortinaTitleCol.setCellFactory(new MyCellFactory());
    cortinaTitleCol.setVisible(true);
    
    this.getColumns().addAll(artistCol, styleCol, playlistNameCol, commentCol, track0Col, track1Col, track2Col, track3Col, cortinaTitleCol);
    this.setTableMenuButtonVisible(true);  
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
        if(item != null) { 
        	 this.getStyleClass().add("tangoTableText");
        	setText(item.toString()); }
      }};
          
      cell.setContextMenu(contextMenu);
     
      return cell;
    }
  }
   
  private ContextMenu setupContextMenu()
  {
    MenuItem copyToPlaylist = new MenuItem("Copy To Playlist"); 
    final ContextMenu contextMenu = new ContextMenu();
    contextMenu.setOnShowing(new EventHandler() 
    {
      public void handle(Event e) 
      {
        tableIndex=tandaTable.getSelectionModel().getSelectedIndex();
      }
    });
    
    contextMenu.getItems().add(copyToPlaylist);
    copyToPlaylist.setOnAction(new EventHandler() 
      { public void handle(Event t) { action.set("copyToPlaylist"); }});
    
    return contextMenu;
  } 
}
