package tangodj2;

import tangodj2.PlaylistTree.TandaTreeItem;
import tangodj2.cleanup.CleanupTable;
import tangodj2.cleanup.CleanupTrack;
import tangodj2.cortina.CortinaTable;
import tangodj2.cortina.CortinaTrack;
import tangodj2.tango.TangoTable;
import tangodj2.tango.TangoTrack;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class PlaylistBuilderTab extends Tab
{
  //Tab tab;
  //SharedValues sharedValues = new SharedValues();
  //AllTracksTable allTracksTable;
  TangoTable tangoTable;
  CleanupTable cleanupTable;
  CortinaTable cortinaTable;
  Playlist playlist;
  final VBox vbox = new VBox();
  int savedType=0;
  Player player;
  TrackLoader2 trackLoader = new TrackLoader2();
  HBox hbox =  new HBox();
  
  public PlaylistBuilderTab(Playlist playlist, Player player)
  {
    this.playlist=playlist;
    this.player=player;
    //this.cleanupTable=cleanupTable;
    
    tangoTable = new TangoTable();
    cleanupTable = new CleanupTable();
    cortinaTable = new CortinaTable();
    
    trackLoader.setTangoTable(tangoTable);
    trackLoader.setCleanupTable(cleanupTable);
    	  
	  this.setText("Edit Playlist");
	  
	  vbox.setPadding(new Insets(10, 10, 10, 10));
	  vbox.setSpacing(20);
	  vbox.setStyle("-fx-background-color: DAE6F3; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work
	 
	  vbox.getChildren().add(getSearchAndFilterBar());
	  vbox.getChildren().add(tangoTable);
	 
	  
	  hbox.setPadding(new Insets(10, 10, 10, 10));
	  hbox.setSpacing(20);
	  hbox.getChildren().add(vbox);
	  hbox.setStyle("-fx-background-color: CC99CC; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work

  
    hbox.getChildren().add(this.playlist.getTreeView());
   // hbox.setHgrow(this.playlist.getTreeView(), Priority.ALWAYS);
    hbox.setHgrow(this.tangoTable, Priority.ALWAYS);
    setupListeners() ;
    this.setContent(hbox);
  }
  
  public void loadTangoDirectory(String path)
  {
    try
    {
      trackLoader.process(path, false, true);
      tangoTable.reloadData();
    } catch (Exception ex) {ex.printStackTrace();}
  }
  
  public void loadCleanupDirectory(String path)
  {
    try
    {
      trackLoader.process(path, false, false);
      cleanupTable.reloadData();
    } catch (Exception ex) {ex.printStackTrace();}
  }
  public void loadTangoFile(String path)
  {
    try
    {
      trackLoader.process(path, true, true);
      tangoTable.reloadData();
    } catch (Exception ex) {ex.printStackTrace();}
  }
  
  public void loadCleanupFile(String path)
  {
    try
    {
      trackLoader.process(path, true, false);
      cleanupTable.reloadData();
    } catch (Exception ex) {ex.printStackTrace();}
  }
  
  
  
  private Button getTestButton()
  {
    Button testButton = new Button("Test"); 
     
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() 
    {
      public void handle(MouseEvent event) 
      {
        playlist.printTracks();
      }
    };
    testButton.setOnMouseClicked(bHandler);
    return testButton;
  }
  
  private HBox getSearchAndFilterBar()
  {
	  final Label label = new Label("Tracks");
	  label.setFont(new Font("Arial", 20));
	  final RadioButton rb1 = new RadioButton("Tango");
	  final RadioButton rb2 = new RadioButton("Cortina");
	  final RadioButton rb3 = new RadioButton("Cleanup");
	  
	  rb1.setId("tango");
	  rb2.setId("cortina");
	  rb3.setId("cleanup");
	  
    final ToggleGroup trackTypeGroup = new ToggleGroup();
    
    rb1.setToggleGroup(trackTypeGroup);
    rb2.setToggleGroup(trackTypeGroup);
    rb3.setToggleGroup(trackTypeGroup);
    rb1.setFont(new Font("Arial", 14));
    rb2.setFont(new Font("Arial", 14));
    rb3.setFont(new Font("Arial", 14));
    
    rb1.setSelected(true);
    
	  final Label spacer = new Label("   ");
	  spacer.setFont(new Font("Arial", 16));
	  final Label spacer2 = new Label("       ");
	  spacer2.setFont(new Font("Arial", 16));
	  final Label spacer3 = new Label("  ");
	  spacer3.setFont(new Font("Arial", 16));
	  final Label spacer4 = new Label("  ");
    spacer4.setFont(new Font("Arial", 16));
	
	  TextField searchBox = new TextField();
	
	  Button searchButton = new Button("Filter");	
	  HBox hbox = new HBox();
	  hbox.setAlignment(Pos.BASELINE_CENTER);
	
	  hbox.getChildren().addAll(label,spacer,rb1,spacer3,rb2,spacer4,rb3,spacer2,searchBox, searchButton);

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
            vbox.getChildren().add(tangoTable);
            savedType=0;
          }
          else if (selectedStr.contains("cleanup")) 
          {
            vbox.getChildren().remove(tangoTable);
            vbox.getChildren().remove(cortinaTable);
            vbox.getChildren().add(cleanupTable);
            savedType=1;
          }
          else if (selectedStr.contains("cortina")) 
          {
            vbox.getChildren().remove(tangoTable);
            vbox.getChildren().remove(cleanupTable);
            vbox.getChildren().add(cortinaTable);
            savedType=2;
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
        
        System.out.println("PlaylistBuilderTab - tangoTable Action: "
            + action+" row: "
                  +row+" "+tangoTrack.getTitle());
        
        
        if ("addToTanda".equals(action))
        {
          if (playlist.getTandaCount()>0)
          {  
            TandaTreeItem tandaTreeItem = playlist.getSelectedTanda();
            tandaTreeItem.addTrack(tangoTrack.getPathHash());
            playlist.generateFlatList();
          }
        }
        
        if ("edit".equals(action))
        {
          new MP3EditorDialog(tangoTrack,  tangoTable);
        }
        
        tangoTable.getAction().set("nada");
        }
      }
    };   
    tangoTable.getAction().addListener(tangoTableListener);
    
    // CLEANUP TABLE LISTENER
    ChangeListener cleanupTableListener = new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
        if (!"nada".equals(newValue))
        {
          int row = cleanupTable.getTableIndex();
          String action=cleanupTable.getAction().get();
          CleanupTrack cleanupTrack = cleanupTable.getItems().get(row);
        
        System.out.println("tangoTable Action: "
            + action+" row: "
                  +row+" "+cleanupTrack.getTitle());
        
        
        if ("addToTanda".equals(action))
        {
          if (playlist.getTandaCount()>0)
          {  
            TandaTreeItem tandaTreeItem = playlist.getSelectedTanda();
            
            tandaTreeItem.addTrack(cleanupTrack.getPathHash());
            playlist.generateFlatList();
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
        player.setFeaturesMode(Player.PLAYLIST);
      }
    };   
    playlist.playlistFocus.addListener(playlistFocusListener);
    
  }


public TangoTable getTangoTable() 
{
	return tangoTable;
}

public void changePlaylist(int playlistId)
{
	  try {
	  playlist = new Playlist(playlistId);
	  hbox.getChildren().remove(1);
	  hbox.getChildren().add(playlist.getTreeView());
	  } catch (Exception e) {e.printStackTrace();};
}
  
}
