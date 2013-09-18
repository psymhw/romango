package tangodj2.PlaylistTree;

import java.sql.SQLException;

import tangodj2.Db;
import tangodj2.SharedValues;
import javafx.scene.control.TreeItem;

public class TandaTreeItem extends BaseTreeItem
{
   private String artist;
   private int styleId;
   private String style;
   private int dbId=0;
   private int trackCount=0;
      
   
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

public String getArtist() {
	return artist;
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
  String str=artist+ ", position: "+this.getPosition()+", styleId: "+styleId+", dbId: "+dbId;
  return str;
}

public int getTrackCount() {
	return trackCount;
}

public void setTrackCount(int trackCount) {
	this.trackCount = trackCount;
}

public void incrementTrackCount()
{
  trackCount++;	
}



   
}
