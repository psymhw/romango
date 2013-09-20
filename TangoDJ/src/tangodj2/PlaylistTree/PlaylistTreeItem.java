package tangodj2.PlaylistTree;

import java.sql.SQLException;

import tangodj2.Db;


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



  public void moveTandaUp(int index)
  {
	TandaTreeItem tti =  (TandaTreeItem)getChildren().get(index);
	getChildren().remove(index);
	getChildren().add(index-1, tti);
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

  public void deleteTanda(int index)
  {
	TandaTreeItem tti =  (TandaTreeItem)getChildren().get(index);
	getChildren().remove(index);
	try 
    {
	  Db.deleteTanda(tti);
	  Db.updateTandaPositions(this);
	} catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
  }
}
