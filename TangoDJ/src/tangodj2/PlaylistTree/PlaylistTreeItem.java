package tangodj2.PlaylistTree;

import javafx.scene.control.TreeItem;
import javafx.scene.text.Text;

public class PlaylistTreeItem  extends TreeItem<Text>
{
  private int id=0;
  private String title;
  public PlaylistTreeItem(int id, String title)
  {
	super(new Text(title));
    this.id = id;
    this.title=title;
  }
  
  public int getId() {
	return id;
  }  
  public void setId(int id) {
	this.id = id;
  }
  public String getTitle() {
	return title;
  }
  public void setTitle(String title) {
	this.title = title;
  }
  
  public String toString()
  {
	return "PlaylisTreeItem - title: "+title;  
  }
}
