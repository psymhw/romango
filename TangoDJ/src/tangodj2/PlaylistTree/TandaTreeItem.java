package tangodj2.PlaylistTree;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.TreeItem;

import tangodj2.Db;
import tangodj2.SharedValues;

public class TandaTreeItem extends BaseTreeItem
{
   private String artist;
   private int styleId;
   private String style;
   private int dbId=0;
   
   private ArrayList<String> trackHashCodes = new ArrayList<String>();
   //private int numberOfTracks=0;
   
   private String cortinaHashCode;
   private int cortinaStart=0;
   private int cortinaStop;
   private String treeType="";
      
   /*
   public TandaTreeItem(String artist, int styleId, int position, boolean insert)
   {
	 super();
	 this.setTreeType("tanda");
	 this.artist=artist;
	 this.styleId=styleId;
	 this.style=SharedValues.styles.get(styleId) ;
	 this.setPosition(position);
	 this.setValue(artist + " - "+style);
	 
	 if (insert)
	 {
	   try 
	   {
	     dbId=Db.insertTanda(artist, styleId, position);
	   } catch (ClassNotFoundException | SQLException e) {	e.printStackTrace(); }
	 }
   }
   */
   
   public TandaTreeItem(String artist, int styleId)
   {
	 super();
	 
	 this.setTreeType("tanda");
	 this.artist=artist;
	 this.styleId=styleId;
	 this.style=SharedValues.styles.get(styleId) ;
	 this.setValue(artist + " - "+style);
   }
   
   
  public String getArtist() {
	return artist;
  }
  
  public void addTrack(String trackHash)
  {
	if (trackHash==null) return;  
	trackHashCodes.add(trackHash);
	TrackTreeItem tti = new TrackTreeItem(trackHash, trackHashCodes.size());
	getChildren().add(tti);
	setExpanded(true);
	try 
	{
	  Db.updateTandaTracks(this);
	} catch (ClassNotFoundException | SQLException e) {	e.printStackTrace();}
	//numberOfTracks++;
  }
  
  public void loadTrack(String trackHash)
  {
	if (trackHash==null) return;  
	trackHashCodes.add(trackHash);
	TrackTreeItem tti = new TrackTreeItem(trackHash, trackHashCodes.size());
	getChildren().add(tti);
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


   
}
