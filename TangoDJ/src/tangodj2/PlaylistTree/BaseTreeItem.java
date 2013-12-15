package tangodj2.PlaylistTree;

import javafx.scene.Node;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BaseTreeItem extends TreeItem<String>
{
	private String treeType="";
  public static Image green_light;
  public static Image gray_light;
  public static Image selected_light;
  public static Image flagsImage;
  public static Image disable;
  public int playableIndex=-1;
  private int trackInTree;
  
  public final static int PLAYING = 1;
  public final static int SELECTED = 2;
  public final static int NONE = 3;
  public final static int DISABLED = 4;
  
  public int status=NONE;
	
	 //private boolean selected=false;
	//private int position=0;
	
	public BaseTreeItem()
	{
	  super();
	  
    if (green_light==null) green_light = new Image(getClass().getResourceAsStream("/resources/images/green_light.png"));
    if (gray_light==null) gray_light = new Image(getClass().getResourceAsStream("/resources/images/gray_light.png"));
    if (selected_light==null) selected_light = new Image(getClass().getResourceAsStream("/resources/images/selected_arrow.png"));
    if (disable==null) disable = new Image(getClass().getResourceAsStream("/resources/images/disable.png"));
    if (flagsImage==null) flagsImage = new Image(getClass().getResourceAsStream("/resources/images/small_flags.png"));
    
    if ("tanda".equals(treeType)) setGraphic(new ImageView(flagsImage)); else setGraphic(new ImageView(gray_light));
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

public int getPlayableIndex()
{
  return playableIndex;
}

public void setPlayableIndex(int playableIndex)
{
  this.playableIndex = playableIndex;
}

public void setPlayingImage(boolean set)
{
   //System.out.println("BaseTreeItem - Set Playing Image: "+set);
   if (set)
   {
     setGraphic(new ImageView(green_light));
     status=PLAYING;
   }
   else 
   {  
     setGraphic(new ImageView(gray_light));
     status=NONE;       
   }
   // have to set and reset the value here or the image doesn't change
   String tValue = getValue();
   setValue(tValue+" ");
   setValue(tValue);
}



public void setNextPlayImage(boolean set)
{
  //System.out.println("BaseTreeItem - Set Next Play Image: "+set);
   if (set) 
   {  
       setGraphic(new ImageView(selected_light));
       status=SELECTED;
   }
   else 
   {  
     setGraphic(new ImageView(gray_light));
     status=NONE;
   }
   // have to set and reset the value here or the image doesn't change
   String tValue = getValue();
   setValue(tValue+" ");
   setValue(tValue);
}

public void setDisableImage(boolean set)
{
  //System.out.println("BaseTreeItem - Set Next Disable Image: "+set);
   if (set) 
   {  
       setGraphic(new ImageView(disable));
       status=DISABLED;
   }
   else 
   {  
     setGraphic(new ImageView(gray_light));
     status=NONE;
   }
   // have to set and reset the value here or the image doesn't change
   String tValue = getValue();
   setValue(tValue+" ");
   setValue(tValue);
}

public boolean isDisabled()
{
  if (status==DISABLED) return true;
  else return false;
}

public boolean isPlaying()
{
  if (status==PLAYING) return true;
  else return false;
}

public void setDisabled(int set)
{
  if (set==1) setDisableImage(true);
  else setDisableImage(false);
}

public int getStatus()
{
  return status;
}

public int getTrackInTree()
{
  return trackInTree;
}

public void setTrackInTree(int trackInTree)
{
  this.trackInTree = trackInTree;
}


/*
public boolean isSelected()
{
  return selected;
}


public void setSelected(boolean selected)
{
  this.selected = selected;
}
*/
/*
public int getPosition() {
	return position;
}

public void setPosition(int position) {
	this.position = position;
}
*/
}
