package tangodj2.allPlaylistsTree;

import javafx.scene.image.ImageView;

public class AllPlaylistsPlaylistItem extends AllPlaylistsBaseItem
{
  public AllPlaylistsPlaylistItem(String playlistName)
  {
	super(playlistName);
	setTreeType("playlist");
	setGraphic(new ImageView(playlistImage));
  }
}
