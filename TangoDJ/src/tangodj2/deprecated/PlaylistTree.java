package tangodj2.PlaylistTree;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import tangodj2.Db;
import tangodj2.SharedValues;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class PlaylistTree 
{
  private TreeView<String> treeView;
  private PlaylistTreeItem playlistTreeItem;
  
  public PlaylistTree (int playlistId)
  {
	playlistTreeItem =  Db.getPlaylist(playlistId);
	
	treeView = new TreeView<String>(playlistTreeItem);
	treeView.getSelectionModel().select(playlistTreeItem);
	treeView.setShowRoot(true);
	ArrayList<TandaTreeItem> tandaTreeItems=null;
	try 
	{
	  tandaTreeItems = Db.getTandaTreeItems(playlistId);
	} catch (ClassNotFoundException | SQLException e) { e.printStackTrace();}
	
	Iterator<TandaTreeItem> it = tandaTreeItems.iterator();
	
	
	if (tandaTreeItems.size()>0)
	{
	  playlistTreeItem.getChildren().addAll(tandaTreeItems);
	}
  }

  public TreeView<String> getTreeView() 
  {
	return treeView;
  }
  
  
  public void addTanda(String artist, int styleId)
  {
	 TandaTreeItem tandaTreeItem = new TandaTreeItem(artist, styleId);
	 playlistTreeItem.getChildren().add(tandaTreeItem); 
	 try 
	   {
	     int tandaDbId = Db.insertTanda(artist, styleId, getTandaPosition(tandaTreeItem));
	     tandaTreeItem.setDbId(tandaDbId);
	   } catch (ClassNotFoundException | SQLException e) {	e.printStackTrace(); }  
  }
  
 
      
  public int getTandaCount()
  {
	return playlistTreeItem.getChildren().size();
  }
  
  public int getTandaPosition(TandaTreeItem tti)
  {
	return playlistTreeItem.getChildren().indexOf(tti);
  }
  
  public void moveTandaUp(int index)
  {
	TandaTreeItem tti =  (TandaTreeItem)playlistTreeItem.getChildren().get(index);
	playlistTreeItem.getChildren().remove(index);
	playlistTreeItem.getChildren().add(index-1, tti);
	try 
    {
	  Db.updateTandaPositions(playlistTreeItem);
	} catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
  }
  
  public void moveTandaDown(int index)
  {
	TandaTreeItem tti =  (TandaTreeItem)playlistTreeItem.getChildren().get(index);
	playlistTreeItem.getChildren().remove(index);
	if (getTandaCount()<index) 
		playlistTreeItem.getChildren().add( tti);
	else
	playlistTreeItem.getChildren().add(index+1, tti);
	try 
    {
	  Db.updateTandaPositions(playlistTreeItem);
	} catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
  }
  
  public void deleteTanda(int index)
  {
	TandaTreeItem tti =  (TandaTreeItem)playlistTreeItem.getChildren().get(index);
	playlistTreeItem.getChildren().remove(index);
	try 
    {
	  Db.deleteTanda(tti);
	  Db.updateTandaPositions(playlistTreeItem);
	} catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
  }

public PlaylistTreeItem getPlaylistTreeItem() {
	return playlistTreeItem;
}

public void setPlaylistTreeItem(PlaylistTreeItem playlistTreeItem) {
	this.playlistTreeItem = playlistTreeItem;
}

  
}
