package tangodj2.PlaylistTree;

import tangodj2.cortina.CortinaTrack;

public class CortinaTreeItem extends BaseTreeItem
{
  CortinaTrack cortinaTrack;
  public CortinaTreeItem(CortinaTrack cortinaTrack)
  {
	super();
	this.setTreeType("cortina");
	this.cortinaTrack=cortinaTrack;
	
	setValue(cortinaTrack.getTitle());
  }

}
