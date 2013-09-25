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
  
  public static Image green_light;
  public static Image gray_light;
  public static Image selected_light;
  private String album;
  private String path;
  public final static int PLAYING = 1;
  public final static int SELECTED = 2;
  public final static int NONE = 3;
  private int status=NONE;
  
  public TrackTreeItem(String trackHash, int position)
  {
  	super();
  	this.trackHash=trackHash;
  	this.setTreeType("track");
  	//if (greenCheckBallImage==null) greenCheckBallImage = new Image(getClass().getResourceAsStream("/resources/images/green_check_ball.png"));
    //if (dimBallImage==null) dimBallImage = new Image(getClass().getResourceAsStream("/resources/images/dim_ball.png"));

    if (green_light==null) green_light = new Image(getClass().getResourceAsStream("/resources/images/green_light.png"));
    if (gray_light==null) gray_light = new Image(getClass().getResourceAsStream("/resources/images/gray_light.png"));
    if (selected_light==null) selected_light = new Image(getClass().getResourceAsStream("/resources/images/selected_arrow.png"));
    
  	setGraphic(new ImageView(gray_light));
  	TrackMeta trackMeta=Db.getTrackInfo(trackHash);
  	album = trackMeta.album;
  	path = trackMeta.path;
  	this.setValue(trackMeta.title+" "+formatDuration(trackMeta.duration));
  }
    
  
  public void setPlayingImage(boolean set)
  {
     if (set)
     {
       setGraphic(new ImageView(green_light));
       status=PLAYING;
     }
     else 
     {  
       setGraphic(new ImageView(gray_light));
       status=NONE;       
     }
     // have to set and reset the value here or the image doesn't change
     String tValue = getValue();
     setValue(tValue+" ");
     setValue(tValue);
  }
  
  public int getStatus()
  {
    return status;
  }
  
  public void setNextPlayImage(boolean set)
  {
     if (set) 
     {  
         setGraphic(new ImageView(selected_light));
         status=SELECTED;
     }
     else 
     {  
       setGraphic(new ImageView(gray_light));
       status=NONE;
     }
     // have to set and reset the value here or the image doesn't change
     String tValue = getValue();
     setValue(tValue+" ");
     setValue(tValue);
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
