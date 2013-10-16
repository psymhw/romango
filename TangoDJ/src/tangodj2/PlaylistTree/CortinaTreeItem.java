package tangodj2.PlaylistTree;

import javafx.scene.image.ImageView;
import tangodj2.TrackMeta;
import tangodj2.cortina.CortinaTrack;

public class CortinaTreeItem extends BaseTreeItem
{
  CortinaTrack cortinaTrack;
  private String album;
  private String path;
  public final static int PLAYING = 1;
  public final static int SELECTED = 2;
  public final static int NONE = 3;
  private int status=NONE;
  
  
  public CortinaTreeItem(CortinaTrack cortinaTrack)
  {
	  super();
	  this.setTreeType("cortina");
	  this.cortinaTrack=cortinaTrack;
	  
	  
	  
	  setGraphic(new ImageView(gray_light));
	  setValue(cortinaTrack.getTitle());
  }

  public String getAlbum()
  {
    return album;
  }

  public void setAlbum(String album)
  {
    this.album = album;
  }

  public String getPath()
  {
    return path;
  }

  public void setPath(String path)
  {
    this.path = path;
  }

  public int getStatus()
  {
    return status;
  }

  public void setStatus(int status)
  {
    this.status = status;
  }

  public CortinaTrack getCortinaTrack()
  {
    return cortinaTrack;
  }

  

}
