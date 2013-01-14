package tangodj;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PlaylistTitle 
{
  private final SimpleStringProperty name;
  private final SimpleIntegerProperty index;

    public PlaylistTitle(int index, String name) 
    {
        this.name = new SimpleStringProperty(name);
        this.index = new SimpleIntegerProperty(index);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String fName) {
        name.set(fName);
    }

	public int getIndex() {
		return index.get();
	}

	public void setIndex(int indexX)
	{
		index.set(indexX);
	}
    
    
}