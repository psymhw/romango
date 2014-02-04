package tangodj2.favorites;

import java.util.Iterator;

import tangodj2.Db;
import tangodj2.TangoDJ2;
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

  public FavoritesTab(final FavoritesTable favoritesTable)
  {
    this.setText("Favorites");
    
    ListView<String> list = new ListView<String>();
    ObservableList<String> items =FXCollections.observableArrayList ();
    list.setItems(items);
    list.setPrefWidth(200);
    //list.setMaxWidth(200);
    list.setMinWidth(200);
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
        System.out.println("New Value: "+newValue); 
        int list_id = Db.getListHeaderId(newValue);
        favoritesTable.reloadData(list_id);
      }
    };
    
    list.getSelectionModel().selectedItemProperty().addListener(cl);
    
    
    
    
    
    sp.getItems().addAll(list, favoritesTable);
    sp.setDividerPositions(0.5f);
    
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
    
  }
  
}
