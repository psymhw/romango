package tangodj2;

import tangodj2.PlaylistTree.BaseTreeItem;
import tangodj2.PlaylistTree.CortinaTreeItem;
import tangodj2.PlaylistTree.TrackTreeItem;

public class PlaylistTrack
{
   public String title;
   public String tandaName;
   public String nextTandaName;
   public String artist;
   public String album;
   public String path;
   
   public int tandaNumber;
   public int trackInTanda=0;
   public int numberOfTracksInTanda;
   public BaseTreeItem baseTreeItem;
   //CortinaTreeItem cortinaTreeItem;
   public String trackHash;
   
   public boolean cortina=false;
   
   public int fadein;
   public int fadeout;
   public int delay;
   public int original_duration;
   public int startValue;
   public int stopValue;
   public boolean playing=false;
   
 
}
