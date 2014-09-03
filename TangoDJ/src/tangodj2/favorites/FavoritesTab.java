package tangodj2.favorites;

import java.util.Iterator;

import tangodj2.Db;
import tangodj2.Player;
import tangodj2.TangoDJ2;
import tangodj2.TrackDb;
import tangodj2.cleanup.CleanupTrack;
import tangodj2.tango.TangoTrack;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FavoritesTab extends Tab
{
  private final Player player;
  final FavoritesTable favoritesTable2 = new FavoritesTable(FavoritesTable.FAVORITES_TAB_STYLE);
  private FavoritesTab favoritesTab;
  
  public FavoritesTab(final Player player)
  {
    this.setText("Favorites");
    this.player=player;
    favoritesTab=this;
    
    ListView<String> list = new ListView<String>();
    ObservableList<String> items =FXCollections.observableArrayList ();
    list.setItems(items);
    list.setPrefWidth(150);
    //list.setMaxWidth(200);
    list.setMinWidth(150);
    list.setPrefHeight(300);
    
    
    
    /*
    final ComboBox listsComboBox = new ComboBox();
    listsComboBox.getSelectionModel().selectedItemProperty().addListener
    (
      new ChangeListener<String>() 
      {
        public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) 
        { 
          System.out.println("favorites list changed");
        } 
      });

   // listsComboBox.setValue(trackDb.style);
    */
    
    
    SplitPane sp = new SplitPane();
    sp.setStyle("-fx-background-color: plum;");
    
    list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    
    ChangeListener cl = new ChangeListener<String>() 
    {
      public void changed(ObservableValue<? extends String> observable,
      String oldValue, String newValue) 
      {
        //System.out.println("New Value: "+newValue); 
        int list_id = Db.getListHeaderId(newValue);
        favoritesTable2.reloadData(list_id);
      }
    };
    
    list.getSelectionModel().selectedItemProperty().addListener(cl);
    
    favoritesTable2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() 
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
    
    
    sp.getItems().addAll(list, favoritesTable2);
    sp.setDividerPositions(0.2f);
    
    setContent(sp);
    
    ListHeaderDb lhdb;
    Iterator<ListHeaderDb> it = TangoDJ2.favoritesList.iterator();
    while(it.hasNext())
    {
      lhdb=it.next();
   //   listsComboBox.getItems().add(lhdb.getName());
      items.add(lhdb.getName());
     // System.out.println(lhdb.getName());
    }
    
    ChangeListener playingListener = new ChangeListener() 
    {
      public void changed(ObservableValue observable, Object oldValue, Object newValue) 
      {
      	if (player.getActiveTab()!=Player.FAVORITES_TAB)  favoritesTab.setDisable(Player.playing.get());
      }
    };   
    Player.playing.addListener(playingListener);
    
  }
  
  public void updateTableRow(TrackDb trackDb)
  {
    favoritesTable2.updateRow(trackDb);
  }
  
  
}
