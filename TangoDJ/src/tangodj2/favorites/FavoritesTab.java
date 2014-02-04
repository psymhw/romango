package tangodj2.favorites;

import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class FavoritesTab extends Tab
{

  public FavoritesTab()
  {
    this.setText("Favorites");
    
    HBox hbox = new HBox();
    hbox.getChildren().add(new Text("TOP"));
    hbox.getChildren().add(new Text("BOTTOM"));
    
    setContent(hbox);
  }
  
}
