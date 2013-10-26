package tangodj2.PlaylistTree;

public class DetailTreeItem extends BaseTreeItem
{
  public DetailTreeItem(String label, String value)
  {
    this.setTreeType("detail");
    this.setValue(label+": "+value);
  }
  
  
}
