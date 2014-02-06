package tangodj2.favorites;


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
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import tangodj2.Db;
import tangodj2.TrackDb;
import tangodj2.tango.TangoTrack;

public class FavoritesTable extends TableView<FavoritesTrack>
{
  private SimpleStringProperty action = new SimpleStringProperty("nada");
  private static TableView<FavoritesTrack> favoritesTable;
  private int tableIndex=-1;
  
  public static final int FAVORITES_TAB_STYLE = 0;
  public static final int PLAYLIST_BUILDER_TAB_STYLE = 1;
  
  private int style;
  
  private final ObservableList<FavoritesTrack> favoritesTracksData = FXCollections.observableArrayList();
	 
  public FavoritesTable(int style)
  {
	  this.favoritesTable=this;
	  this.style=style;
	  
	  favoritesTable.selectionModelProperty().get().selectedIndexProperty().addListener(new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        tableIndex=(int)newValue;
       // System.out.println("FavoritesTable-new Value: "+tableIndex);
      }
    });
	  setupTable();
	  reloadData();
  }
  
  public  void reloadData()
  {
    favoritesTable.getSortOrder().clear();
    favoritesTracksData.clear();
    favoritesTracksData.addAll(Db.loadFavoritesTracks(1));
  }
  
  public  void reloadData(final int list_id)
  {
    Platform.runLater(new Runnable() 
    {
      public void run() 
      {
        favoritesTracksData.clear();
        favoritesTracksData.addAll(Db.loadFavoritesTracks(list_id));
        ArrayList<TableColumn<FavoritesTrack, ?>> sortOrder = new ArrayList<>(favoritesTable.getSortOrder());
        favoritesTable.getSortOrder().clear();
        favoritesTable.getSortOrder().addAll(sortOrder);
      }
    });
  }
  
	 
  private void setupTable() 
  {
    setupColumns();
    this.setItems(favoritesTracksData);
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
          this.getStyleClass().add("favoritesTableText");
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
      final ContextMenu contextMenu = new ContextMenu();
      
      contextMenu.setOnShowing(new EventHandler() 
      {
        public void handle(Event e) 
        {
          //tableIndex=favoritesTable.getSelectionModel().getSelectedIndex();
          System.out.println("FavoritesTable: index: "+tableIndex);
        }
      });
      
      
      if (style==this.PLAYLIST_BUILDER_TAB_STYLE) contextMenu.getItems().addAll(addToTanda, edit, play, delete);
      if (style==this.FAVORITES_TAB_STYLE) contextMenu.getItems().addAll(edit, play, delete);
      addToTanda.setOnAction(new EventHandler() 
        { public void handle(Event t) { action.set("addToTanda"); }});
      edit.setOnAction(new EventHandler() 
        { public void handle(Event t) { action.set("edit"); } });
      play.setOnAction(new EventHandler() 
        { public void handle(Event t) { action.set("play"); }});
      delete.setOnAction(new EventHandler() 
        { public void handle(Event t) { action.set("delete"); }});
      return contextMenu;
    } 
  
		  
	   private void setupColumns()
	   {
		   TableColumn titleCol = new TableColumn("Title");
		   titleCol.setMinWidth(100);
		   titleCol.setPrefWidth(150);
		   titleCol.setCellValueFactory(new PropertyValueFactory<FavoritesTrack, String>("title"));
		   titleCol.setCellFactory(new MyCellFactory());
		   
		// STYLE COLUMN
	     TableColumn styleCol = new TableColumn("Style");
	     styleCol.setMinWidth(60);
	     styleCol.setCellValueFactory(new PropertyValueFactory<FavoritesTrack, String>("style"));
	     styleCol.setCellFactory(new MyCellFactory());
		       
		  TableColumn artistCol = new TableColumn("Artist");
		  artistCol.setMinWidth(50);
		  artistCol.setPrefWidth(100);
		  artistCol.setCellValueFactory(new PropertyValueFactory<FavoritesTrack, String>("artist"));
		  artistCol.setCellFactory(new MyCellFactory());
		      
		  TableColumn albumCol = new TableColumn("Album");
		  albumCol.setMinWidth(50);
		  albumCol.setPrefWidth(150);
		  albumCol.setCellValueFactory(new PropertyValueFactory<FavoritesTrack, String>("album"));
		  albumCol.setCellFactory(new MyCellFactory());
		  
		  // RATING COLUMN
	    TableColumn ratingCol = new TableColumn("Rating");
	    ratingCol.setMinWidth(60);
	    ratingCol.setCellValueFactory(new PropertyValueFactory<FavoritesTrack, String>("rating"));
	    ratingCol.setCellFactory(new MyCellFactory());
		      
		  TableColumn genreCol = new TableColumn("Genre");
		  genreCol.setMinWidth(30);
		  genreCol.setPrefWidth(50);
		  genreCol.setCellValueFactory(new PropertyValueFactory<FavoritesTrack, String>("genre"));
		  genreCol.setCellFactory(new MyCellFactory());
		      
		  TableColumn commentCol = new TableColumn("Comment");
		  commentCol.setMinWidth(50);
		  commentCol.setPrefWidth(100);
		  commentCol.setCellValueFactory(new PropertyValueFactory<FavoritesTrack, String>("comment"));
		  commentCol.setCellFactory(new MyCellFactory());
		 		      
		  TableColumn durationCol = new TableColumn("Length");
		  durationCol.setMinWidth(50);
		  durationCol.setPrefWidth(50);
		  durationCol.setCellValueFactory(new PropertyValueFactory<FavoritesTrack, String>("duration"));
		  durationCol.setCellFactory(new MyCellFactory());
		      
		  TableColumn yearCol = new TableColumn("Year");
		  yearCol.setMinWidth(50);
		  yearCol.setPrefWidth(50);
		  yearCol.setCellValueFactory(new PropertyValueFactory<FavoritesTrack, String>("track_year"));
		  yearCol.setCellFactory(new MyCellFactory());
		     
		  this.getColumns().addAll(titleCol, styleCol, ratingCol, durationCol, yearCol, artistCol, albumCol, genreCol, commentCol);
		      
	  }
		

	  public SimpleStringProperty getAction()
    {
      return action;
    }


    public int getTableIndex()
    {
      return tableIndex;
    }
    
    public void updateRow(TrackDb trackDb)
    {
      FavoritesTrack ftrack;
      for(int i=0; i<favoritesTracksData.size(); i++)
      {
        if (trackDb.pathHash.equals(favoritesTracksData.get(i).getPathHash()))
        {
          ftrack=favoritesTracksData.get(i);
          ftrack.setTitle(trackDb.title);
          ftrack.setArtist(trackDb.artist);
          ftrack.setAlbum(trackDb.album);
          ftrack.setTrack_year(trackDb.track_year);
          ftrack.setGenre(trackDb.genre);
          ftrack.setComment(trackDb.comment);
          ftrack.setStyle(trackDb.style);
          ftrack.setSinger(trackDb.singer);
          ftrack.setBpm(trackDb.bpm);
          ftrack.setLeader(trackDb.leader);
          ftrack.setRating(trackDb.rating);
          
          // this forces the table to update the row
          int numberOfColumns=getColumns().size();
          for(int j=0; j<numberOfColumns; j++)
          {
            if (getColumns().get(j).isVisible())
            {
              getColumns().get(j).setVisible(false);
              getColumns().get(j).setVisible(true);
            }
          }
          
          //System.out.println("FavoritesTable: row found");
          return;
        }
      }
    }


}

