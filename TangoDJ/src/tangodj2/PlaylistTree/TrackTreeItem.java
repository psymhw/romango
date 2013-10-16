package tangodj2.PlaylistTree;

import java.sql.SQLException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import tangodj2.Db;
import tangodj2.SharedValues;
import tangodj2.TrackMeta;

public class TrackTreeItem extends BaseTreeItem
{
  private String trackHash;
  private int type = 0;
  private int tandaDbId=-1;
  public static Image greenCheckBallImage;
  public static Image dimBallImage;
  

  private String album;
  private String path;
 
  
  public TrackTreeItem(String trackHash, int position)
  {
  	super();
  	this.trackHash=trackHash;
  	this.setTreeType("tango");
  	//if (greenCheckBallImage==null) greenCheckBallImage = new Image(getClass().getResourceAsStream("/resources/images/green_check_ball.png"));
    //if (dimBallImage==null) dimBallImage = new Image(getClass().getResourceAsStream("/resources/images/dim_ball.png"));
   
  	setGraphic(new ImageView(gray_light));
  	TrackMeta trackMeta=Db.getTrackInfo(trackHash);
  	if (trackMeta.cleanup==1) setTreeType("cleanup");
  	album = trackMeta.album;
  	path = trackMeta.path;
  	this.setValue(trackMeta.title+" "+formatDuration(trackMeta.duration));
  }
    
  
 
  
  
  
  public String getTandaAndTrackPosition(TrackTreeItem tti)
  {
  	TandaTreeItem tandaTreeItem = (TandaTreeItem)getParent();
  	PlaylistTreeItem playlistTreeItem = (PlaylistTreeItem)tandaTreeItem.getParent();
  	int tandaIndex = playlistTreeItem.getTandaPosition(tandaTreeItem);
  	int trackIndex = tandaTreeItem.getChildren().indexOf(tti);
  	return tandaIndex+","+trackIndex;
  }
  
  public int getTrackPosition(TrackTreeItem tti)
  {
	  TandaTreeItem tandaTreeItem = (TandaTreeItem)getParent();
	  int trackIndex = tandaTreeItem.getChildren().indexOf(tti);
	  return trackIndex;
  }

  public String getTrackHash() {
	return trackHash;
  }

  public void setTrackHash(String trackHash) {
	this.trackHash = trackHash;
  }

  public int getType() {
	return type;
  }

  public void setType(int type) {
	this.type = type;
  }

    
  private String formatDuration(int sec) {
    
    int seconds = (int)( sec  % 60);
    int minutes = (int) (sec /  60);
    return String.format("%02d:%02d", minutes, seconds);
  }

public int getTandaDbId() {
	return tandaDbId;
}

public void setTandaDbId(int tandaDbId) {
	this.tandaDbId = tandaDbId;
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



}
