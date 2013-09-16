package tangodj2.PlaylistTree;

import java.util.ArrayList;

import tangodj2.Db;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Text;

public class PlaylistTree 
{
  ArrayList<TreeItem<Text>> tandas;
  TreeView<Text> treeView;
  PlaylistTreeItem playlistTreeItem;
  
  public PlaylistTree (int id)
  {
	playlistTreeItem =  Db.getPlaylist(id);
	treeView = new TreeView<Text>(playlistTreeItem);
	treeView.setShowRoot(true);
  }

  public TreeView<Text> getTreeView() 
  {
	return treeView;
  }

  
}
