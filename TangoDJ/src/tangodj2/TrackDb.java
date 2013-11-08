package tangodj2;

public class TrackDb 
{
  public String title="";
  public String artist="";
  public String album="";
  public String comment="";
  public String genre="";
  public int    duration=0;
  public String pathHash="";
  public String path="";
  public String track_year="";
  public int    cleanup=0;
  public String singer="";
  public String rating="";
  public String bpm="";
  public String leader="";
  public int    track_no=0;
  public String style="";
  public String adjectives="";
  public int    delay=0;
  
  public boolean metaComplete=false;
  
  
  public TrackDb(String title, 
		               String artist, 
		               String album, 
		               String comment, 
		               String genre, 
		               String pathHash, 
		               String path,
		               String track_year)
                   {
	                   this.title=title;
	                   this.artist=artist;
	                   this.album=album;
	                   this.comment=comment;
	                   this.genre=genre;
	                   this.pathHash=pathHash;
	                   this.path=path;
	                   this.track_year=track_year;
                   }
}
