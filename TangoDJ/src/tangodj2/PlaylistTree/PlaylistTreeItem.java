package tangodj2.PlaylistTree;

import java.sql.SQLException;

import tangodj2.Db;
import tangodj2.SharedValues;


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


  public void addTanda(String artist, int styleId)
  {
    TandaTreeItem tandaTreeItem = new TandaTreeItem(artist, styleId);
	getChildren().add(tandaTreeItem); 
	try 
	{
	  int tandaDbId = Db.insertTanda(artist, styleId, getTandaPosition(tandaTreeItem));
	  tandaTreeItem.setDbId(tandaDbId);
	} catch (ClassNotFoundException | SQLException e) {	e.printStackTrace(); }  
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

   /*
  public void moveTandaUp(int index)
  {
	//System.out.println(" SharedValues.selectedTanda: "+SharedValues.selectedTanda);
	TandaTreeItem tti =  (TandaTreeItem)getChildren().get(index);
	getChildren().remove(index);
	getChildren().add(index-1, tti);
	System.out.println("number of tandas: "+getChildren().size());
	try 
    {
	  Db.updateTandaPositions(this);
	} catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
  }

  public void moveTandaDown(int index)
  {
	TandaTreeItem tti =  (TandaTreeItem)getChildren().get(index);
	getChildren().remove(index);
	if (getTandaCount()<index) 
		getChildren().add( tti);
	else
	getChildren().add(index+1, tti);
	try 
    {
	  Db.updateTandaPositions(this);
	} catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
  }
*/
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
