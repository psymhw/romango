package tangodj2.test;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

public class SharedValues 
{
  public static SimpleStringProperty title = new SimpleStringProperty();
  public static SimpleStringProperty pathHash = new SimpleStringProperty();
  public static SimpleIntegerProperty playlistTrackAdd = new SimpleIntegerProperty(0);
  public static  String DRIVER ="org.apache.derby.jdbc.EmbeddedDriver";
  public static  String JDBC_URL ="jdbc:derby:tango_db;create=false";
  public static SimpleIntegerProperty loadMonitor = new SimpleIntegerProperty(0);
  
  
}


