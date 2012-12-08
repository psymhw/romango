package tangodj;

import java.util.concurrent.TimeUnit;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Track 
{
	private final SimpleStringProperty name;
    private final SimpleStringProperty artist;
    private final SimpleStringProperty path;
    private final SimpleStringProperty grouping;
    private final SimpleIntegerProperty time;
   
    
	public Track(String name, String artist, String path, String grouping, int time)
	{
      this.name=new SimpleStringProperty(name);	
      this.artist=new SimpleStringProperty(artist);	
      this.path=new SimpleStringProperty(path);	
      this.grouping=new SimpleStringProperty(grouping);	
      this.time=new SimpleIntegerProperty(time);	
	}
	
	public String getName() {
        return name.get();
    }

    public void setName(String fName) {
        name.set(fName);
    }
    
    public String getArtist() {
        return artist.get();
    }

    public void setArtist(String fName) {
        artist.set(fName);
    }
    
    public String getPath() {
        return path.get();
    }

    public void setPath(String fName) {
        path.set(fName);
    }
    
    public String getTime() {
    	return String.format("%d:%d", 
    		    TimeUnit.MILLISECONDS.toMinutes(time.get()),
    		    TimeUnit.MILLISECONDS.toSeconds(time.get()) - 
    		    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time.get()))
    		);
        //return time.get();
    }

    public void setTime(int fName) {
        time.set(fName);
    }

	public String getGrouping() {
		return grouping.get();
	}
	
	public void setGrouping(String gName) {
        grouping.set(gName);
    }
    
	

}
