package tangodj2;

import java.sql.Date;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Playlist 
{
	private  int id=0;
	private final SimpleStringProperty name;
	private final SimpleStringProperty location;
	private  Date incept=null;
	
	public Playlist(String name, String location, Date incept, int id)
	{
	  this.name=new SimpleStringProperty(name);
	  this.location=new SimpleStringProperty(location);
	  this.id=id;
	  this.incept=incept;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getIncept() {
		return incept;
	}

	public void setIncept(Date incept) {
		this.incept = incept;
	}

	public void setName(String nameStr)
	{
	  name.set(nameStr);
	}
	
	public String getName()
	{
	  return name.get();
	}
	
	public void setLocation(String locationStr)
	{
	  location.set(locationStr);
	}
	
	public String getLocation()
	{
	  return location.get();
	}
	
}
