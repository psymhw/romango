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
  private ArrayList<TreeItem<String>> tandas;
  private TreeView<String> treeView;
  private PlaylistTreeItem playlistTreeItem;
  private static int tandaCounter=0;
  
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
	TandaTreeItem tti=null;
	while(it.hasNext())
	{
	  tti = it.next();
	  System.out.println(tti.toString());
	}
	
	if (tandaTreeItems.size()>0)
	{
	  playlistTreeItem.getChildren().addAll(tandaTreeItems);
	}
  }

  public TreeView<String> getTreeView() 
  {
	return treeView;
  }
  
  public void addTanda(TandaTreeItem tandaTreeItem)
  {
	 playlistTreeItem.getChildren().add(tandaTreeItem); 
  }

  public void addTrack()
  {
	TrackTreeItem trackTreeItem = new TrackTreeItem();
	TandaTreeItem tandaTreeItem = (TandaTreeItem)playlistTreeItem.getChildren().get(SharedValues.selectedTanda);
	trackTreeItem.setPosition(tandaTreeItem.getPosition());
	tandaTreeItem.incrementTrackCount();
	tandaTreeItem.getChildren().add(trackTreeItem);
	tandaTreeItem.setExpanded(true);
  }
  
  public static int getTandaCounter() 
  { 
	return tandaCounter++;
  }

public void setTandaCounter(int tandaCounter) {
	this.tandaCounter = tandaCounter;
}

  
}
