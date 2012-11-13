package tangodj;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Track 
{
	private final SimpleStringProperty name;
    private final SimpleStringProperty artist;
    private final SimpleStringProperty path;
    private final SimpleIntegerProperty time;
    
	public Track(String name, String artist, String path, int time)
	{
      this.name=new SimpleStringProperty(name);	
      this.artist=new SimpleStringProperty(artist);	
      this.path=new SimpleStringProperty(path);	
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
    
    public int getTime() {
        return time.get();
    }

    public void setTime(int fName) {
        time.set(fName);
    }
	

}
