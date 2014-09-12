package tangodj2.PlaylistTree;

import java.sql.SQLException;

import tangodj2.Db;
import tangodj2.SharedValues;
import tangodj2.TandaDb;


public class PlaylistTreeItem  extends BaseTreeItem
{
  private int id=0;
  private String title;
  
  
  public PlaylistTreeItem(int id, String title)
  {
	super(title);  
	this.setTreeType("playlist");
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



  public int getTandaPosition(TandaTreeItem tti)
  {
	return getChildren().indexOf(tti);
  }


  public int addTanda(String artist, int styleId, String comment)
  {
    TandaTreeItem tandaTreeItem = new TandaTreeItem(artist, styleId, comment);
    tandaTreeItem.setExpanded(false);
    int tandaDbId =0;
	  getChildren().add(tandaTreeItem); 
	  try 
	  {  tandaDbId = Db.insertTanda(artist, styleId, getTandaPosition(tandaTreeItem), comment);
	    tandaTreeItem.setDbId(tandaDbId);
	  } catch (Exception e) {	e.printStackTrace(); }  
	  return tandaDbId;
  }
  
  public int addTandaCopy(long tandaId)
  {
    TandaDb tandaDb = Db.getTanda(tandaId);
    TandaTreeItem tandaTreeItem = new TandaTreeItem(tandaDb);	
    		
    tandaTreeItem.setExpanded(false);
    int tandaDbId =0;
	  getChildren().add(tandaTreeItem); 
	  try 
	  {  tandaDbId = Db.insertTanda(tandaDb.getArtist(), tandaDb.getStyleId(), getTandaPosition(tandaTreeItem), tandaDb.getComment());
	    tandaTreeItem.setDbId(tandaDbId);
	  } catch (Exception e) {	e.printStackTrace(); }  
	  return tandaDbId;
  }
  
  public void updateTanda(long tandaId, String artist, int styleId,  String comment, TandaTreeItem tandaTreeItem)
  {
	//System.out.println("PlaylistTreeItem - updateTanda: tandaId="+tandaId+", artist="+artist+", styleId="+styleId+", comment="+comment);
		  
    tandaTreeItem.setArtist(artist);
    tandaTreeItem.setStyleId(styleId);
    tandaTreeItem.setStyle(SharedValues.styles.get(styleId));
    String value = artist+" - "+SharedValues.styles.get(styleId);
    if (comment!=null) if (comment.length()>0) value+=" ("+comment+")";
    tandaTreeItem.setValue(value);
	  try 
	  {  Db.updateTanda(tandaId, artist, styleId, comment);
	  } catch (Exception e) {	e.printStackTrace(); }  
  }



    
  public int getTandaCount()
  {
	return getChildren().size();
  }

   public TandaTreeItem getTanda(int i)
   {
	 return (TandaTreeItem)getChildren().get(i);  
   }
   
   public TandaTreeItem getFirstTanda()
   {
     if (getChildren().size()==0) return null;
     return (TandaTreeItem)getChildren().get(0);  
   }

  public void moveTandaUp(TandaTreeItem tti)
  {
    int index = tti.getPlaylistIndex();
    getChildren().remove(index);
    getChildren().add(index-1, tti);
   
    try { Db.updateTandaPositions(this); } catch (Exception e) { e.printStackTrace(); }
  }

  public void moveTandaDown(TandaTreeItem tti)
  {
    int index = tti.getPlaylistIndex();
    getChildren().remove(index);
    if (getTandaCount()<index) 
      getChildren().add( tti);
    else
      getChildren().add(index+1, tti);
    
    
    try 
    { Db.updateTandaPositions(this); } catch (Exception e) { e.printStackTrace(); }
  }

  
  public void deleteTanda(TandaTreeItem tti)
  {
    int index = tti.getPlaylistIndex();
	  getChildren().remove(index);
	  
	  try 
    { Db.deleteTanda(tti); Db.updateTandaPositions(this);	} catch (Exception e) { e.printStackTrace(); }
  }
}
