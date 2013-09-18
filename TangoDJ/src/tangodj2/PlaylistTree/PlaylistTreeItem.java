package tangodj2.PlaylistTree;


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


}
