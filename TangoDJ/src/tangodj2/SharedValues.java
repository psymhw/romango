package tangodj2;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SharedValues 
{
  public static SimpleStringProperty title = new SimpleStringProperty();
  public static SimpleStringProperty pathHash = new SimpleStringProperty();
  public static SimpleIntegerProperty playlistTrackAdd = new SimpleIntegerProperty(0);
  public static String DRIVER ="org.apache.derby.jdbc.EmbeddedDriver";
  public static String JDBC_URL ="jdbc:derby:tango_db;create=false";
  public static int currentPlaylist = 1;
}


