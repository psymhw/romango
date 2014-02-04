package tangodj2.favorites;

import java.util.Iterator;

import tangodj2.TangoDJ2;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FavoritesTab extends Tab
{

  public FavoritesTab()
  {
    this.setText("Favorites");
    
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
    
    VBox vbox = new VBox();
    vbox.getChildren().add(listsComboBox);
    vbox.getChildren().add(new Text("BOTTOM"));
    
    SplitPane sp = new SplitPane();
    sp.setStyle("-fx-background-color: plum;");
    
    
    sp.getItems().addAll(listsComboBox, new Text("RIGHT"));
    sp.setDividerPositions(0.5f);
    
    setContent(sp);

    
    
    
    ListHeaderDb lhdb;
    Iterator<ListHeaderDb> it = TangoDJ2.favoritesList.iterator();
    while(it.hasNext())
    {
      lhdb=it.next();
      listsComboBox.getItems().add(lhdb.getName());
     // System.out.println(lhdb.getName());
    }
    
  }
  
}
