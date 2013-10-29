package tangodj2;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import tangodj2.allPlaylistsTree.AllPlaylistsBaseItem;
import tangodj2.allPlaylistsTree.AllPlaylistsFolderItem;
import tangodj2.allPlaylistsTree.EditPlaylistDialog;

public class PlaylistChoiceTab extends Tab
{
  private TableView<PlaylistData> allPlaylistsTable = new TableView<PlaylistData>();
  private final static ObservableList<PlaylistData> allPlaylistsData = FXCollections.observableArrayList();
  private TreeView allPlaylistsTree = new TreeView();
  private AllPlaylistsFolderItem root;
  int selectedId=-1;
  AllPlaylistsBaseItem selectedItem;
  public final static int PLAYLIST=0;
  public final static int FOLDER=1;
  TangoDJ2 tangoDJ;
  EventTab eventTab;
  Player player;
  PlaylistBuilderTab playlistBuilderTab;
  
  
  public PlaylistChoiceTab(TangoDJ2 tangoDJ, Player player, PlaylistBuilderTab playlistBuilderTab, EventTab eventTab)
  {
	this.setText("Playlists");
	this.tangoDJ=tangoDJ;
	this.eventTab=eventTab;
	this.player=player;
	this.playlistBuilderTab=playlistBuilderTab;
	/*
	TableColumn nameCol = TableColumnBuilder.create()
            .text("Name")
            .minWidth(100)
            .prefWidth(150)
            .build();

     nameCol.setCellValueFactory(new PropertyValueFactory<Track, String>("name"));
    
     
     TableColumn idCol = TableColumnBuilder.create()
             .text("ID")
             .minWidth(20)
             .maxWidth(20)
             .prefWidth(20)
             .build();

      idCol.setCellValueFactory(new PropertyValueFactory<Track, String>("id"));
     
    
      TableColumn locationCol = TableColumnBuilder.create()
              .text("Location")
              .minWidth(100)
              .prefWidth(150)
              .build();

       locationCol.setCellValueFactory(new PropertyValueFactory<Track, String>("location"));
       
       
     //allPlaylistsTable.setMaxWidth(340);
     allPlaylistsTable.setEditable(false);
     allPlaylistsTable.getColumns().addAll(idCol, nameCol, locationCol);
     
     allPlaylistsTable.setItems(allPlaylistsData);  
	
	setData();
	*/
	final ContextMenu contextMenu = new ContextMenu();
	

	MenuItem item1 = new MenuItem("About");
	item1.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent e) {
	        System.out.println("About");
	    }
	});
	MenuItem item2 = new MenuItem("Preferences");
	item2.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent e) {
	        System.out.println("Preferences");
	    }
	});
	contextMenu.getItems().addAll(item1, item2);
	//allPlaylistsTable.setContextMenu(contextMenu);
	//root.setContextMenu(contextMenu);
	
	 ChangeListener<TreeItem<String>> treeViewChangeListener = new ChangeListener<TreeItem<String>>() 
	 {
	   public void changed(ObservableValue<? extends TreeItem<String>> observableValue, TreeItem<String> oldItem, TreeItem<String> newItem) 
	   {
	     if (newItem!=null)
		 {
	    	 selectedItem = (AllPlaylistsBaseItem)newItem; 
	    	
		    selectedId=selectedItem.getId();
		   // bti.isLeaf();
		 }
	}};
	
	allPlaylistsTree.getSelectionModel().selectedItemProperty().addListener(treeViewChangeListener);

	allPlaylistsTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	root = (AllPlaylistsFolderItem)Db.getAllPlaylists();
	
	//System.out.println("root: "+root.getValue());
	
	allPlaylistsTree.getStyleClass().add("playlistTree");
	root.setExpanded(true);
	allPlaylistsTree.setRoot(root);
	//root.setId(0);
	allPlaylistsTree.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>()
			 {
			   @Override
			   public TreeCell<String> call(TreeView<String> p) 
			   {
				 return new MyCellFactory();
			   }
			  });
	/*
	AllPlaylistsFolderItem someFolder = new AllPlaylistsFolderItem("Some Folder");
	someFolder.setId(17);
	AllPlaylistsPlaylistItem aPlaylist= new AllPlaylistsPlaylistItem("a playlist");
	aPlaylist.setId(22);
	root.getChildren().add(someFolder);
	root.getChildren().add(aPlaylist);
	*/
	final Label label = new Label("Playlists");
    label.setFont(new Font("Arial", 20));
    
    final VBox vbox = new VBox(5);
    
    vbox.setPadding(new Insets(10, 10, 10, 10));
    vbox.getChildren().add(allPlaylistsTree);
    vbox.setMaxWidth(350);
    this.setContent(vbox);
  }
  
  private void setData() 
  {
	String name="";
	String location = "";
	Date incept;
	int id;
	  try
 	    {
 			Connection connection = DriverManager.getConnection("jdbc:derby:tango_db;create=false");
 			Statement statement = connection.createStatement();
 			ResultSet resultSet = statement.executeQuery("select * from playlists");
 			while(resultSet.next())
 			{
 			  name=resultSet.getString("name");
 			  location = resultSet.getString("location");
 			  incept = resultSet.getDate("incept");
 			  id = resultSet.getInt("id");
 			 allPlaylistsData.add(new PlaylistData(name, location, incept, id));
 			  
 			//  System.out.println("added: "+name);
 			}
 			if (statement!=null) statement.close();
 			if (connection!=null) connection.close();
 	    } catch (Exception e) { e.printStackTrace();}
 	  }



  
  
  private void addPlaylist(String name, String location)
  {
	 if (name==null) return;
	 if (name.trim().length()==0) return;
	 Connection connection=null;
	 
	 try
	 {
	   Class.forName(SharedValues.DRIVER);
	   connection = DriverManager.getConnection(SharedValues.JDBC_URL);
	   String sql="insert into playlists (name, location) values ('"+name+"', '"+location+"')";
	   connection.createStatement().execute(sql);
	   allPlaylistsData.clear();
	   setData();
	 } 
	 catch (Exception e) { e.printStackTrace(); }
	 finally 
	 { 
	   try 
	   {
		 connection.close(); 
	   }
	   catch (Exception ex) { ex.printStackTrace(); }
	 }
   }
	 
  private final class MyCellFactory extends TreeCell<String> 
  {
    private ContextMenu folderContextMenu =  new ContextMenu();
    private ContextMenu playlistContextMenu =  new ContextMenu();

    public MyCellFactory() 
    {
      setupFolderContextMenu(folderContextMenu);
      setupPlaylistContextMenu(playlistContextMenu);
    }
      
    @Override
    public void updateItem(String item, boolean empty) 
    {
      super.updateItem(item, empty);

      if (empty) 
      {
        setText(null);
        setGraphic(null);
      } 
      else 
      {
        setText(getString());
        AllPlaylistsBaseItem bti = (AllPlaylistsBaseItem)getTreeItem();
        if (isSelected()) 
        {
          TreeItem treeItem = (TreeItem)bti;
          System.out.println("PlaylistChoiceTab - selected tree cell: "+treeItem.getValue());
        }
        if ("folder".equals(bti.getTreeType())) 
        {  
          setFont(Font.font("Serif", 20));
          setContextMenu(folderContextMenu);
        }
        else if ("playlist".equals(bti.getTreeType())) 
        {	   
         // if (playlistTreeItem.getTandaPosition((TandaTreeItem)bti)==0) tandaContextMenu.getItems().get(0).setDisable(true);  // disable move up
          //if (playlistTreeItem.getTandaPosition((TandaTreeItem)bti)==playlistTreeItem.getTandaCount()-1) tandaContextMenu.getItems().get(1).setDisable(true); // disable move down
          setFont(Font.font("Serif", 16));
          setContextMenu(playlistContextMenu);
         // setGraphic(getTreeItem().getGraphic());
        }
       
        setGraphic(getTreeItem().getGraphic());
      }
    }
     
    private String getString() 
    {
      return getItem() == null ? "" : getItem().toString();
    }
  }
  
  private void setupPlaylistContextMenu(final ContextMenu playlistContextMenu)
  {
    MenuItem edit = new MenuItem("Edit");
    MenuItem select = new MenuItem("SELECT");
    MenuItem delete = new MenuItem("Delete");
    MenuItem moveUp = new MenuItem("Move Up");
    MenuItem moveDown = new MenuItem("Move Down");
    MenuItem move = new MenuItem("Move to Diff Folder");
    playlistContextMenu.getItems().addAll(select, edit, moveUp, moveDown, move, delete);
    select.setOnAction(new EventHandler() 
    {
      public void handle(Event t) 
      {
        System.out.println("Selected: "+selectedId);  
        Db.saveCurrentPlaylist(selectedId);
        TangoDJ2.prefs.currentPlaylist=selectedId;
        tangoDJ.changePlaylist(selectedId);
        eventTab.changePlaylist(selectedId);
        playlistBuilderTab.changePlaylist(selectedId);
        player.changePlaylist(selectedId);
      }
    });
  }
    
    private void setupFolderContextMenu(final ContextMenu playlistContextMenu)
    {
      MenuItem newPlaylist = new MenuItem("New Playlist"); 
      MenuItem newFolder = new MenuItem("New Folder"); 
      MenuItem edit = new MenuItem("Edit");
      MenuItem moveUp = new MenuItem("Move Up");
      MenuItem moveDown = new MenuItem("Move Down");
      MenuItem move = new MenuItem("Move to Diff Folder");
      MenuItem delete = new MenuItem("Delete"); 
      playlistContextMenu.getItems().addAll(newPlaylist, newFolder, edit, moveUp, moveDown, move,delete);
      newPlaylist.setOnAction(new EventHandler() 
      {
        public void handle(Event t) 
        {
        	System.out.println("PlaylistChoiceTab, selectedId: "+selectedId);
          new EditPlaylistDialog("new", PLAYLIST, selectedItem);  
         
        }
      });
      newFolder.setOnAction(new EventHandler() 
      {
        public void handle(Event t) 
        {
        	System.out.println("PlaylistChoiceTab, selectedId: "+selectedId);
          new EditPlaylistDialog("new", FOLDER, selectedItem);  
        }
      });
   
  } 

}

