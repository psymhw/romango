package tangodj2.allPlaylistsTree;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;

public class AllPlaylistsBaseItem extends TreeItem<String>
{
  private String treeType="";
  public static Image folderImage;
  public static Image playlistImage;
  private int id;
  private int level;
  private int position;
  private int parentId;
  private String location="";
  
  
  public AllPlaylistsBaseItem(String value)
  {
	super(value);
	if (folderImage==null) folderImage = new Image(getClass().getResourceAsStream("/resources/images/folder.png"));
	if (playlistImage==null) playlistImage = new Image(getClass().getResourceAsStream("/resources/images/gray_light.png"));
  }

public String getTreeType() {
	return treeType;
}

public void setTreeType(String treeType) {
	this.treeType = treeType;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public int getLevel() {
	return level;
}

public void setLevel(int level) {
	this.level = level;
}

public int getPosition() {
	return position;
}

public void setPosition(int position) {
	this.position = position;
}

public int getParentId() {
	return parentId;
}

public void setParentId(int parentId) {
	this.parentId = parentId;
}

public String getLocation()
{
  return location;
}

public void setLocation(String location)
{
  this.location = location;
}


}
