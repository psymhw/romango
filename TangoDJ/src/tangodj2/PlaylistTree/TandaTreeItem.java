package tangodj2.PlaylistTree;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import tangodj2.Db;
import tangodj2.SharedValues;
import tangodj2.cortina.CortinaTrack;

public class TandaTreeItem extends BaseTreeItem
{
   private String artist;
   private int styleId;
   private String style;
   private int dbId=0;
   private int cortinaId=-1;
   
   private ArrayList<String> trackHashCodes = new ArrayList<String>();
   //private int numberOfTracks=0;
   
   private String cortinaHashCode;
   private int cortinaStart=0;
   private int cortinaStop;
   private String treeType="";
   private int playlistIndex=0; // irregardless of disabled or not
  // private static Image flagsImage;
      
   public TandaTreeItem(String artist, int styleId)
   {
  	 super();
  	 this.setTreeType("tanda");
  	 this.artist=artist;
  	 this.styleId=styleId;
  	 //if (flagsImage==null) flagsImage = new Image(getClass().getResourceAsStream("/resources/images/small_flags.png"));
  	// setGraphic(new ImageView(flagsImage));
  	 this.style=SharedValues.styles.get(styleId) ;
  	 this.setValue(artist + " - "+style);
   }
   
   public void moveTrackUp(int index)
   {
 	   TrackTreeItem tti =  (TrackTreeItem)getChildren().get(index);
 	   //System.out.println("TandaTreeItem - moveTrackUp - index: "+index+", "+tti.getValue());
 	   getChildren().remove(index);
 	   getChildren().add(index-1, tti);
 	 
 	   String trackHashCode = trackHashCodes.get(index);
 	   trackHashCodes.remove(index);
 	   trackHashCodes.add(index-1, trackHashCode);
 	 
 	 try 
      {
 	   Db.updateTandaTracks(this);
 	} catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
 	
 	   
   }
   
   public void moveTrackDown(int index)
   {
 	 TrackTreeItem tti =  (TrackTreeItem)getChildren().get(index);
 	String trackHashCode = trackHashCodes.get(index);
 	 
 	getChildren().remove(index);
 	trackHashCodes.remove(index);
 	 
 	if (getTrackCount()<index)
 	{	
	  getChildren().add(tti);
	  trackHashCodes.add(trackHashCode);
 	}
	else
	{
	  trackHashCodes.add(index+1, trackHashCode);
	  getChildren().add(index+1, tti);
	}
 	 try 
      {
 	   Db.updateTandaTracks(this);
 	} catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
   }
   
   public void deleteTrack(int index)
   {
   	 TrackTreeItem tti =  (TrackTreeItem)getChildren().get(index);
   	 getChildren().remove(index);
   	 trackHashCodes.remove(index);
  	 
   	 try 
     {
	   Db.updateTandaTracks(this);
	} catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
   }
   
   public void removeCortina()
   {
   	 int size = getChildren().size();
     getChildren().remove(size-1);
  	 cortinaId=-1;
   	 try 
     {
	   Db.updateTandaTracks(this);
	} catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
   }
   
   
   public int getTrackCount()
   {
 	return getChildren().size();
   }
   
  public String getArtist() {
	return artist;
  }
  
  public String getArtistAndStyle() {
    return artist+" - "+style;
    }
  
  public void addTrack(String trackHash)
  {
    if (trackHash==null) return;  
  	trackHashCodes.add(trackHash);
  	TrackTreeItem tti = new TrackTreeItem(trackHash, trackHashCodes.size());
  	int numberOfTracks=getChildren().size();
  	System.out.println("TandaTreeItem - size: "+numberOfTracks);
  	if (cortinaId!=-1)
  	{
  	  getChildren().add(numberOfTracks-1, tti);
  	}
  	else getChildren().add(tti);
  	setExpanded(true);
  	try 
  	{
  	  Db.updateTandaTracks(this);
  	} catch (ClassNotFoundException | SQLException e) {	e.printStackTrace();}
  	//numberOfTracks++;
    }
    
  public void addCortina(CortinaTrack cortinaTrack)
  {
    if (cortinaTrack==null) return;  
    if (this.cortinaId!=-1) 
    {
      System.out.println("TandaTreeItem - CortinaId not -1");
      return;  // can't overwrite. must delete and re-add
    }
      cortinaId=cortinaTrack.getId();
  	
  	CortinaTreeItem cti = new CortinaTreeItem(cortinaTrack);
  	getChildren().add(cti);
  	setExpanded(true);
  	try 
  	{
  	  Db.updateTandaTracks(this);
  	} catch (ClassNotFoundException | SQLException e) {	e.printStackTrace();}
  	//numberOfTracks++;
    }
    
  
  public void loadTrack(String trackHash, int disabled)
  {
  	if (trackHash==null) return;  
  	trackHashCodes.add(trackHash);
  	TrackTreeItem tti = new TrackTreeItem(trackHash, trackHashCodes.size());
  	tti.setDisabled(disabled);
  	getChildren().add(tti);
  	setExpanded(true);
	//numberOfTracks++;
  }
  
  public void loadCortina(int cortinaId, int disable)
  {
    //System.out.println("TandaTreeItem - loadCortinaTrack: "+cortinaId);
    if (cortinaId==-1) return;  
    CortinaTrack cortinaTrack = Db.getCortinaTrack(cortinaId);
    if (cortinaTrack==null)
    {
      //System.out.println("TandaTreeItem - cortinaTrack is null");
      return;
    }
    CortinaTreeItem cti = new CortinaTreeItem(cortinaTrack);
    cti.setDisabled(disable);
    getChildren().add(cti);
    setExpanded(true);
  //numberOfTracks++;
  }
  
  public int getTrackPosition(BaseTreeItem bti)
  {
	 TrackTreeItem tti= (TrackTreeItem) bti; 
	 return trackHashCodes.indexOf(tti.getTrackHash());
  }


  public String getTrackHash(int i)
  {
	  return trackHashCodes.get(i);
  }
  
public void setArtist(String artist) {
	this.artist = artist;
}

public int getStyleId() {
	return styleId;
}

public void setStyleId(int styleId) {
	this.styleId = styleId;
}

public String getStyle() {
	return style;
}

public void setStyle(String style) {
	this.style = style;
}

public int getDbId() {
	return dbId;
}

public void setDbId(int dbId) {
	this.dbId = dbId;
}

public String toString()
{
  String str=artist+ ", styleId: "+styleId+", dbId: "+dbId;
  return str;
}



public ArrayList<String> getTrackHashCodes() {
	return trackHashCodes;
}



public int getNumberOfTracks() {
	return trackHashCodes.size();
}



public String getCortinaHashCode() {
	return cortinaHashCode;
}

public void setCortinaHashCode(String cortinaHashCode) {
	this.cortinaHashCode = cortinaHashCode;
}

public int getCortinaStart() {
	return cortinaStart;
}

public void setCortinaStart(int cortinaStart) {
	this.cortinaStart = cortinaStart;
}

public int getCortinaStop() {
	return cortinaStop;
}

public void setCortinaStop(int cortinaStop) {
	this.cortinaStop = cortinaStop;
}

public String getTreeType() {
	return treeType;
}

public void setTreeType(String treeType) {
	this.treeType = treeType;
}

public int getCortinaId()
{
  return cortinaId;
}

public void setCortinaId(int cortinaId)
{
  this.cortinaId = cortinaId;
}

// these 2 funcs override the ones in BaseTreeItem
public void setDisabled(int set)
{
  if (set==1) setDisableImage(true);
  else setDisableImage(false);
}

public void setDisableImage(boolean set)
{
  //System.out.println("BaseTreeItem - Set Next Play Image: "+set);
   if (set) 
   {  
       setGraphic(new ImageView(disable));
       status=DISABLED;
   }
   else 
   {  
     setGraphic(new ImageView(flagsImage));
     status=NONE;
   }
   // have to set and reset the value here or the image doesn't change
   String tValue = getValue();
   setValue(tValue+" ");
   setValue(tValue);
}

public int getPlaylistIndex()
{
  return playlistIndex;
}

public void setPlaylistIndex(int playlistIndex)
{
  this.playlistIndex = playlistIndex;
}
   
}
