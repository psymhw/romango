package tangodj2;

import java.net.URL;
import java.util.Iterator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import tangodj2.PlaylistTree.TandaTreeItem;
import tangodj2.cleanup.CleanupTable;
import tangodj2.cleanup.CleanupTrack;
import tangodj2.cortina.CortinaTable;
import tangodj2.cortina.CortinaTrack;
import tangodj2.favorites.FavoritesTab;
import tangodj2.favorites.FavoritesTable;
import tangodj2.favorites.FavoritesTrack;
import tangodj2.favorites.ListHeaderDb;
import tangodj2.tanda.TandaTable;
import tangodj2.tango.TangoTable;
import tangodj2.tango.TangoTrack;

public class PlaylistBuilderTab extends Tab
{
  //Tab tab;
  //SharedValues sharedValues = new SharedValues();
  //AllTracksTable allTracksTable;
  private TangoTable tangoTable;
  private CleanupTable cleanupTable;
  private CortinaTable cortinaTable;
  private FavoritesTable favoritesTable;
  private TandaTable tandaTable;
  public static Playlist playlist;
  private PlaylistBuilderTab playlistBuilderTab;
  final VBox vbox = new VBox();
 // int savedType=0;
  Player player;
  //TrackLoader3 trackLoader = new TrackLoader3();
  HBox rightBox =  new HBox();
  TextField searchField = new TextField();
  public static Label trackCount = new Label("");
  String currentTable = "tango";
  TreeView treeView;
  final URL tree_stylesheet = getClass().getResource("PlaylistTree/tree.css");
  
  
  public PlaylistBuilderTab(Playlist playlist, final Player player)
  {
    this.playlist=playlist;
    this.player=player;
    playlistBuilderTab=this;
    //this.cleanupTable=cleanupTable;
   
    tangoTable = new TangoTable();
    tandaTable = new TandaTable();
    
    
    cleanupTable = new CleanupTable();
    cortinaTable = new CortinaTable();
    favoritesTable = new FavoritesTable(FavoritesTable.PLAYLIST_BUILDER_TAB_STYLE);
    
    favoritesTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        FavoritesTrack favoritesTrack = (FavoritesTrack)newValue;
        if (favoritesTrack!=null)
        {
          player.setPlayMode(Player.PLAYMODE_SINGLE_TRACK);
          player.setCurrentTrackHash(favoritesTrack.getPathHash());
          TrackDb trackDb = Db.getTrackInfo(favoritesTrack.getPathHash());
          player.setCurrentTrackTitle(trackDb.title);
         // System.out.println("FavoriteTab-currentHash: "+favoritesTrack.getPathHash());
        }
      }
    });
    
    
    //trackLoader.setTangoTable(tangoTable);
    //trackLoader.setCleanupTable(cleanupTable);
    	  
	  this.setText("Edit Playlist");
	  
	  vbox.setPadding(new Insets(10, 10, 10, 10));
	  vbox.setSpacing(20);
	  vbox.setStyle("-fx-background-color: DAE6F3; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 1px;"); 
	 
	  vbox.getChildren().add(getSearchAndFilterBar());
	  
	  vbox.getChildren().add(tangoTable);
	  vbox.setVgrow(tangoTable, Priority.ALWAYS);
	  vbox.setVgrow(cleanupTable, Priority.ALWAYS);
	  vbox.setVgrow(cortinaTable, Priority.ALWAYS);
	  
	  rightBox.getStylesheets().add(tree_stylesheet.toString());
	  rightBox.setPadding(new Insets(10, 5, 10, 5));
	  rightBox.setSpacing(20);
	//  hbox.getChildren().add(vbox);
	 // hbox.setStyle("-fx-background-color: CC99CC; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 1px;"); 

    treeView=this.playlist.getTreeView();
    rightBox.getChildren().add(treeView);
   // hbox.setHgrow(this.playlist.getTreeView(), Priority.ALWAYS);
  //  hbox.setHgrow(this.tangoTable, Priority.ALWAYS);
    setupListeners() ;
    
    SplitPane sp = new SplitPane();
    sp.setStyle("-fx-background-color: plum;");
    
    
    sp.getItems().addAll(vbox, rightBox);
    sp.setDividerPositions( 0.75f, .9f);
    sp.setStyle("-fx-background-color: plum;");
    
    //trackCount.setText(""+ tangoTable.getRowCount());
    this.setContent(sp);
  }
  
  private Button getTestButton()
  {
    Button testButton = new Button("Test"); 
     
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() 
    {
      public void handle(MouseEvent event) 
      {
        System.out.println("Flatlist size: "+playlist.getFlatPlaylistSize());
      }
    };
    testButton.setOnMouseClicked(bHandler);
    return testButton;
  }
  
  public void reloadPlaylist()
  {
    rightBox.getChildren().remove(treeView);
    playlist = new Playlist(TangoDJ2.prefs.currentPlaylist);
    playlist.getTreeView().setPrefWidth(500);
    treeView = playlist.getTreeView();
    rightBox.getChildren().add(treeView);
  }
  
  private Button getSearchButton()
  {
    Button button = new Button("Find"); 
     
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() 
    {
      public void handle(MouseEvent event) 
      {
       if ("tango".equals(currentTable))
       {
         TangoTable.reloadData(searchField.getText().replaceAll("'", "''"));
       }
       else
       {
         CleanupTable.reloadData(searchField.getText().replaceAll("'", "''"));
       }
      }
    };
    button.setOnMouseClicked(bHandler);
    return button;
  }
  
  private Button getClearButton()
  {
    Button button = new Button(" clear "); 
     
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() 
    {
      public void handle(MouseEvent event) 
      {
        searchField.setText("");
        if ("tango".equals(currentTable))
        
          TangoTable.reloadData();
        
        else
          CleanupTable.reloadData();
      }
    };
    button.setOnMouseClicked(bHandler);
    return button;
  }
  
  private HBox getSearchAndFilterBar()
  {
	  final Label label = new Label("Tracks");
	  label.setFont(new Font("Arial", 20));
	  final RadioButton rb1 = new RadioButton("Tango");
	  final RadioButton rb2 = new RadioButton("Cortina");
	  final RadioButton rb3 = new RadioButton("Non-Tango");
	  final RadioButton rb4 = new RadioButton("Favorites");
	  final RadioButton rb5 = new RadioButton("Tandas");
	  
	  rb1.setId("tango");
	  rb2.setId("cortina");
	  rb3.setId("cleanup");
	  rb4.setId("favorites");
	  rb5.setId("tandas");
	  
    final ToggleGroup trackTypeGroup = new ToggleGroup();
    
    rb1.setToggleGroup(trackTypeGroup);
    rb2.setToggleGroup(trackTypeGroup);
    rb3.setToggleGroup(trackTypeGroup);
    rb4.setToggleGroup(trackTypeGroup);
    rb5.setToggleGroup(trackTypeGroup);
    rb1.setFont(new Font("Arial", 14));
    rb2.setFont(new Font("Arial", 14));
    rb3.setFont(new Font("Arial", 14));
    rb4.setFont(new Font("Arial", 14));
    rb5.setFont(new Font("Arial", 14));
    
    rb1.setSelected(true);
    
	  final Label spacer = new Label("   ");
	  spacer.setFont(new Font("Arial", 16));
	  final Label spacer2 = new Label("       ");
	  spacer2.setFont(new Font("Arial", 16));
	  final Label spacer3 = new Label("  ");
	  spacer3.setFont(new Font("Arial", 16));
	  final Label spacer4 = new Label("  ");
    spacer4.setFont(new Font("Arial", 16));
    final Label spacer5 = new Label("  ");
    spacer5.setFont(new Font("Arial", 16));
    final Label spacer6 = new Label("  ");
    spacer6.setFont(new Font("Arial", 16));
    final Label spacer7 = new Label("  ");
    spacer7.setFont(new Font("Arial", 16));
	
	  final ComboBox favoritesComboBox = getFavoritesComboBox();
	  final HBox favotitesListBox = new HBox();
	  Text listLabel = new Text("Favorites List: ");
	  listLabel.setFont(new Font("Arial", 16));
	  favotitesListBox.getChildren().addAll(listLabel,favoritesComboBox);
	  
	  HBox hbox = new HBox();
	  hbox.setAlignment(Pos.BASELINE_CENTER);
	
	  hbox.getChildren().addAll(label,
			                    spacer,rb1,
			                    spacer3,rb4,
			                    spacer5,rb2,
			                    spacer4,rb3,
			                    spacer7,rb5,
			                    spacer2, searchField, 
			                    getSearchButton(), 
			                    getClearButton(), 
			                    spacer6,trackCount);

	  trackTypeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
    public void changed(ObservableValue<? extends Toggle> ov,
          Toggle old_toggle, Toggle new_toggle) 
      {
        if (trackTypeGroup.getSelectedToggle() != null) 
        {
          String selectedStr=trackTypeGroup.getSelectedToggle().toString();
          if (selectedStr.contains("tango")) 
          {
            vbox.getChildren().remove(cleanupTable);
            vbox.getChildren().remove(cortinaTable);
            vbox.getChildren().remove(tandaTable); 
            vbox.getChildren().remove(favoritesTable);
            vbox.getChildren().remove(favotitesListBox);
            vbox.getChildren().add(tangoTable);
            currentTable="tango";
           // savedType=0;
          }
          else if (selectedStr.contains("cleanup")) 
          {
            vbox.getChildren().remove(tangoTable);
            vbox.getChildren().remove(cortinaTable);
            vbox.getChildren().remove(tandaTable);
            vbox.getChildren().remove(favoritesTable);
            vbox.getChildren().remove(favotitesListBox);
            vbox.getChildren().add(cleanupTable);
            currentTable="cleanup";
            //savedType=1;
          }
          else if (selectedStr.contains("cortina")) 
          {
            vbox.getChildren().remove(tangoTable);
            vbox.getChildren().remove(cleanupTable);
            vbox.getChildren().remove(tandaTable);
            vbox.getChildren().remove(favoritesTable);
            vbox.getChildren().remove(favotitesListBox);
            vbox.getChildren().add(cortinaTable);
            currentTable="cortina";
           // savedType=2;
          }
          else if (selectedStr.contains("favorites")) 
          {
            vbox.getChildren().remove(tangoTable);
            vbox.getChildren().remove(cleanupTable);
            vbox.getChildren().remove(cortinaTable);
            vbox.getChildren().remove(tandaTable);
            vbox.getChildren().add(favotitesListBox);
            vbox.getChildren().add(favoritesTable);
            currentTable="cortina";
           // savedType=2;
          }
          else if (selectedStr.contains("tandas")) 
          {
            vbox.getChildren().remove(tangoTable);
            vbox.getChildren().remove(cleanupTable);
            vbox.getChildren().remove(cortinaTable);
            vbox.getChildren().remove(favoritesTable);
            vbox.getChildren().remove(favotitesListBox);
            vbox.getChildren().add(tandaTable);
            currentTable="cortina";
           // savedType=2;
          }
        }                
      }
    });
	  return hbox;
  }
   
  private void setupListeners() 
  {
    // MOUSE TANGO TABLE ROW SELECTION
    tangoTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        TangoTrack tangoTrack = (TangoTrack)newValue;
        if (tangoTrack!=null)
        {
          player.setPlayMode(Player.PLAYMODE_SINGLE_TRACK);
          player.setCurrentTrackHash(tangoTrack.getPathHash());
          player.setCurrentTrackTitle(tangoTrack.getTitle());
          //System.out.println("PlaylistBuilder - artist: "+tangoTrack.getArtist());
         // player.setTrack(tangoTrack.getPathHash(), Player.PLAYLIST_BUILD_TANGO_TABLE);
        }
      }
    });
    
    // MOUSE CLEANUP TABLE ROW SELECTION
    cleanupTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        CleanupTrack cleanupTrack = (CleanupTrack)newValue;
        if (cleanupTrack!=null)
        {
          player.setPlayMode(Player.PLAYMODE_SINGLE_TRACK);
          player.setCurrentTrackHash(cleanupTrack.getPathHash());
          player.setCurrentTrackTitle(cleanupTrack.getTitle());
          //player.setTrack(cleanupTrack.getPathHash(), Player.PLAYLIST_BUILD_CLEANUP_TABLE);
        }
      }
    });
    
    // MOUSE CORTINA TABLE ROW SELECTION
    cortinaTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        CortinaTrack cortinaTrack = (CortinaTrack)newValue;
        if (cortinaTrack!=null)
        {
          player.setPlayMode(Player.PLAYMODE_CORTINA_SINGLE_TRACK);
          player.setCurrentCortinaId(cortinaTrack.getId());
          player.setCurrentTrackTitle(cortinaTrack.getTitle());   
        }
      }
    });
    
    // TANGO TABLE LISTENER
    ChangeListener tangoTableListener = new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        if (!"nada".equals(newValue))
        {
          int row = tangoTable.getTableIndex();
          String action=tangoTable.getAction().get();
          TangoTrack tangoTrack = tangoTable.getItems().get(row);
        
         // System.out.println("PlaylistBuilderTab - tangoTable Action: "+ action+" row: "+row+" "+tangoTrack.getTitle());
        
        
        if ("addToTanda".equals(action))
        {
          if (playlist.getTandaCount()>0)
          {  
            TandaTreeItem tandaTreeItem = playlist.getSelectedTanda();
            tandaTreeItem.addTrack(tangoTrack.getPathHash());
            tandaTable.update(tandaTreeItem.getDbId());
            playlist.generateFlatList();
          }
        }
        
        if ("edit".equals(action))
        {
          new MP3EditorDialog(tangoTrack,  tangoTable);
        }
        
        if ("delete".equals(action))
        {
          tangoTable.delete(row, tangoTrack.getPathHash());
        }
        
        tangoTable.getAction().set("nada");
        }
      }
    };   
    tangoTable.getAction().addListener(tangoTableListener);
    
 // FAVORITES TABLE LISTENER
    ChangeListener favoritesTableListener = new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        if (!"nada".equals(newValue))
        {
          int row = favoritesTable.getTableIndex();
          String action=favoritesTable.getAction().get();
          FavoritesTrack favoritesTrack = favoritesTable.getItems().get(row);
        
         System.out.println("PlaylistBuilderTab - favoritesTable Action: "+ action+" row: "+row+" "+favoritesTrack.getTitle());
        
        
        if ("addToTanda".equals(action))
        {
          if (playlist.getTandaCount()>0)
          {  
            TandaTreeItem tandaTreeItem = playlist.getSelectedTanda();
            tandaTreeItem.addTrack(favoritesTrack.getPathHash());
            playlist.generateFlatList();
          }
        }
        
        if ("edit".equals(action))
        {
          new MP3EditorDialog(favoritesTrack,  favoritesTable);
       }
        
      //  if ("delete".equals(action))
       // {
       //   tangoTable.delete(row, tangoTrack.getPathHash());
       // }
        
        favoritesTable.getAction().set("nada");
        }
      }
    };   
    favoritesTable.getAction().addListener(favoritesTableListener);
    
    // CLEANUP TABLE LISTENER
    ChangeListener cleanupTableListener = new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        if (!"nada".equals(newValue))
        {
          int row = cleanupTable.getTableIndex();
          String action=cleanupTable.getAction().get();
          if (row>=0)
          { 
            CleanupTrack cleanupTrack = cleanupTable.getItems().get(row);
            System.out.println("PlaylistBuilderTab, cleanupTable Action: "+ action+" row: "+row+" "+cleanupTrack.getTitle());
        
            if ("addToTanda".equals(action))
            {
              if (playlist.getTandaCount()>0)
              {  
                TandaTreeItem tandaTreeItem = playlist.getSelectedTanda();
                tandaTreeItem.addTrack(cleanupTrack.getPathHash());
                playlist.generateFlatList();
              }
            }
          }
          cleanupTable.getAction().set("nada");
        }
      }
    };   
    cleanupTable.getAction().addListener(cleanupTableListener);
    
    // CORTINA TABLE LISTENER
    ChangeListener cortinaTableListener = new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        if (!"nada".equals(newValue))
        {
          int row = cortinaTable.getTableIndex();
          String action=cortinaTable.getAction().get();
          CortinaTrack cortinaTrack = cortinaTable.getItems().get(row);
        
        System.out.println("PlaylistBuilderTab, cortinaTable Action: "
            + action+" row: "
                  +row+" "+cortinaTrack.getTitle());
        
        
        if ("addToTanda".equals(action))
        {
          if (playlist.getTandaCount()>0)
          {  
            TandaTreeItem tandaTreeItem = playlist.getSelectedTanda();
            tandaTreeItem.addCortina(cortinaTrack);
            playlist.generateFlatList();
          }
        }
        
        cortinaTable.getAction().set("nada");
        }
      }
    };   
    cortinaTable.getAction().addListener(cortinaTableListener);
    
    
    // PLAYLIST FOCUS LISTENER
    ChangeListener playlistFocusListener = new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        player.setPlayMode(Player.PLAYMODE_PLAYLIST);
       // player.setMode(Player.PLAYLIST);
      }
    };   
    playlist.playlistFocus.addListener(playlistFocusListener);
  
    
    // PLAYER PLAYING LISTENER
    ChangeListener playingListener = new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        if (player.getActiveTab()!=Player.PLAYLIST_BUILDER_TAB) playlistBuilderTab.setDisable(Player.playing.get());
      }
    };   
   Player.playing.addListener(playingListener);
    
  }


public TangoTable getTangoTable() 
{
	return tangoTable;
}

public CleanupTable getCleanupTable() 
{
  return cleanupTable;
}

public void changePlaylist(int playlistId)
{
	  try {
	  playlist = new Playlist(playlistId);
	  rightBox.getChildren().remove(1);
	  rightBox.getChildren().add(playlist.getTreeView());
	  } catch (Exception e) {e.printStackTrace();};
}

private final ComboBox getFavoritesComboBox()
{
  final ComboBox comboBox = new ComboBox();
  comboBox.getSelectionModel().selectedItemProperty().addListener
  (
    new ChangeListener<String>() 
    {
      public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) 
      { 
        //System.out.println("favorites list changed");
        int list_id = Db.getListHeaderId(new_val);
        favoritesTable.reloadData(list_id);
      } 
    });
  
  ListHeaderDb lhdb;
  Iterator<ListHeaderDb> it = TangoDJ2.favoritesList.iterator();
  while(it.hasNext())
  {
    lhdb=it.next();
    comboBox.getItems().add(lhdb.getName());
   // System.out.println(lhdb.getName());
  }
  comboBox.getSelectionModel().selectFirst();
  return comboBox;
}
  
}
