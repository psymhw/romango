package tangodj2.allPlaylistsTree;

import javafx.scene.image.ImageView;

public class AllPlaylistsFolderItem extends AllPlaylistsBaseItem
{

  public AllPlaylistsFolderItem(String folderName)
  {
	super(folderName);
	this.setTreeType("folder");  
	setGraphic(new ImageView(folderImage));
  }
}
