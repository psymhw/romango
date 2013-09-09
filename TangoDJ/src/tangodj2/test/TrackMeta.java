package tangodj2.test;

public class TrackMeta 
{
  public String title="";
  public String artist="";
  public  String album="";
  public String comment="";
  public String genre="";
  public int duration;
  public String pathHash="";
  public String path;
  
  public TrackMeta(String title, 
		           String artist, 
		           String album, 
		           String comment, 
		           String genre, 
		           String pathHash, 
		           String path)
                   {
	                 this.title=title;
	                 this.artist=artist;
	                 this.album=album;
	                 this.comment=comment;
	                 this.genre=genre;
	                 this.pathHash=pathHash;
	                 this.path=path;
                   }
}
