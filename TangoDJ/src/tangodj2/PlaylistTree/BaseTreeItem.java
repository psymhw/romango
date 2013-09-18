package tangodj2.PlaylistTree;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

public class BaseTreeItem extends TreeItem<String>
{
	private String treeType="";
	private int position=0;
	
	public BaseTreeItem()
	{
	  super();
	}
	
   public BaseTreeItem(String str)
   {
	 super(str);
   }
   
   public BaseTreeItem(String str, Node node)
   {
	 super(str, node);
   }

public String getTreeType() {
	return treeType;
}

public void setTreeType(String treeType) {
	this.treeType = treeType;
}

public int getPosition() {
	return position;
}

public void setPosition(int position) {
	this.position = position;
}
}
