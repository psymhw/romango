package tangodj2.tango;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import tangodj2.Db;
import tangodj2.Playlist;
import tangodj2.SharedValues;
import tangodj2.PlaylistTree.TandaTreeItem;

public class TangoTable extends TableView<TangoTrack>
{
  //private TableView<TangoTrack> table = new TableView<TangoTrack>();
  private Playlist playlist;
  
  public final static ObservableList<TangoTrack> tangoTracksData = FXCollections.observableArrayList();
	 
  public TangoTable(Playlist playlist)
  {
	  this.playlist=playlist;
	 
	 
	  Db.loadTangoTracks(tangoTracksData);
	  setupTable();
	  System.out.println("TangoTable - tracks loaded: "+tangoTracksData.size());
	  /*
	  if (SharedValues.allTracksData.size()>0)
	  {
	    Track firstTrack = SharedValues.allTracksData.get(0);
	    if (type==TANGO)
	    SharedValues.selectedTangoPathHash.set(firstTrack.getPathHash());
	    if (type==CORTINA)
	    SharedValues.selectedCleanupPathHash.set(firstTrack.getPathHash());

	  }
	  */
  }
	 
  
  
  public void reloadData()
  {
    Db.loadTangoTracks(tangoTracksData);
  }
	 
  /*
  public TableView<TangoTrack> getTable() 
  { 
    return this; 
  }
	*/
  private void setupTable() 
  {
    setupColumns();
    this.setItems(tangoTracksData);
  //  this.setPrefWidth(thisWidth);
  //  this.setMinWidth(thisWidth);
  //  this.setMaxWidth(thisWidth);
    this.setEditable(false);
	    
    this.setOnKeyReleased(keyEvent);
	    
   
  }
		
		  
	   private void setupColumns()
	   {
		   TableColumn titleCol = new TableColumn("Title");
		   titleCol.setMinWidth(100);
		   titleCol.setPrefWidth(150);
		   titleCol.setCellValueFactory(new PropertyValueFactory<TangoTrack, String>("title"));
		   titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
		   
		       
		  TableColumn artistCol = new TableColumn("Artist");
		  artistCol.setMinWidth(50);
		  artistCol.setPrefWidth(100);
		  artistCol.setCellValueFactory(new PropertyValueFactory<TangoTrack, String>("artist"));
		  artistCol.setCellFactory(TextFieldTableCell.forTableColumn());
		 
		      
		  TableColumn albumCol = new TableColumn("Album");
		  albumCol.setMinWidth(50);
		  albumCol.setPrefWidth(150);
		  albumCol.setCellValueFactory(new PropertyValueFactory<TangoTrack, String>("album"));
		  albumCol.setCellFactory(TextFieldTableCell.forTableColumn());
		   
		      
		  TableColumn genreCol = new TableColumn("Genre");
		  genreCol.setMinWidth(30);
		  genreCol.setPrefWidth(50);
		  genreCol.setCellValueFactory(new PropertyValueFactory<TangoTrack, String>("genre"));
		  genreCol.setCellFactory(TextFieldTableCell.forTableColumn());
		  
		      
		  TableColumn commentCol = new TableColumn("Comment");
		  commentCol.setMinWidth(50);
		  commentCol.setPrefWidth(100);
		  commentCol.setCellValueFactory(new PropertyValueFactory<TangoTrack, String>("comment"));
		  commentCol.setCellFactory(TextFieldTableCell.forTableColumn());
		 		      
		  TableColumn durationCol = new TableColumn("Length");
		  durationCol.setMinWidth(50);
		  durationCol.setPrefWidth(50);
		  durationCol.setCellValueFactory(new PropertyValueFactory<TangoTrack, String>("duration"));
		  durationCol.setCellFactory(TextFieldTableCell.forTableColumn());
		      
		  TableColumn yearCol = new TableColumn("Year");
		  yearCol.setMinWidth(50);
		  yearCol.setPrefWidth(50);
		  yearCol.setCellValueFactory(new PropertyValueFactory<TangoTrack, String>("track_year"));
		  yearCol.setCellFactory(TextFieldTableCell.forTableColumn());
		     
		  this.getColumns().addAll(titleCol, durationCol, yearCol, artistCol, albumCol, genreCol, commentCol);
		      
	  }
		
	  EventHandler<KeyEvent>keyEvent = new EventHandler<KeyEvent>() 
	  {
	    public void handle(KeyEvent ke) 
      {		    
	      if (ke.getCode()==KeyCode.ESCAPE) { System.out.println("Esc Pressed"); }
		 	  if (ke.getCode()==KeyCode.UP)     { System.out.println("UP Pressed");  }
			  if (ke.getCode()==KeyCode.DOWN)   { System.out.println("DOWN Pressed");}
			  if (ke.getCode()==KeyCode.RIGHT)  
			  {   	
			 	  if (playlist.getTandaCount()>0)
			  	{	 
			 	 	  if (SharedValues.selectedTanda==-1)	SharedValues.selectedTanda=0;
			 	 	  TandaTreeItem tandaTreeItem = playlist.getTanda(SharedValues.selectedTanda);
			 	 	  tandaTreeItem.addTrack(SharedValues.selectedTangoPathHash.get());
			 	 	}
			 	}
			}
	  };

    


}
