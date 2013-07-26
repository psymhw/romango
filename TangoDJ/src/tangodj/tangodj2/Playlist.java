package tangodj.tangodj2;

import java.sql.Date;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Playlist 
{
	private final int id=0;
	private final SimpleStringProperty name;
	private final SimpleStringProperty location;
	private final Date date=null;
	
	public Playlist(String name, String location)
	{
	  this.name=new SimpleStringProperty(name);
	  this.location=new SimpleStringProperty(location);
	}
	
}
