package tangodj2;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SharedValues 
{
  public final static ObservableList<Track> allTracksData = FXCollections.observableArrayList();
  public static SimpleStringProperty title = new SimpleStringProperty();
  public static SimpleStringProperty selectedAllTracksPathHash = new SimpleStringProperty();
  public static SimpleIntegerProperty playlistFocus = new SimpleIntegerProperty(0);
 // public static Playlist playlist;
  public static String DRIVER ="org.apache.derby.jdbc.EmbeddedDriver";
  public static String JDBC_URL ="jdbc:derby:tango_db;create=false";
  public static int currentPlaylist = 1;
  public static int selectedTanda = -1;
  public static int selectedPlaylistTrack = -1;
  public static int allTracksType = 0;
  
  
  public final static int TANGO = 0;
  public final static int VALS = 1;
  public final static int MILONGA  = 2;
  public final static int ALTERNATIVE = 3;
  public final static int MIXED = 4;
  public final static int CLEANUP = 5;
  public final static int CORTINA = 6;
  
  public final static ArrayList<String> styles = new ArrayList<String>(Arrays.asList(
		  "Tango",
		  "Vals",
		  "Milonga",
		  "Alternative",
		  "Mixed",
		  "Cleanup"
		  ));
  
  public final static ArrayList<Artist> artistsA = new ArrayList<Artist>(Arrays.asList(
		  new Artist("Rudolfo",   "Biagi", 1),
		  new Artist("Francisco", "Canaro", 2),
		  new Artist("Angel",     "D'Agostino",3),
		  new Artist("Juan",      "D'Arienzo",4),
		  new Artist("Carlos",    "Di Sarli",5),
		  new Artist("Edgardo",   "Donato",6),
		  new Artist("Osvaldo",   "Pugliese",7),
		  new Artist("Ricardo",   "Tanturi",8),
		  new Artist("Anibal",     "Troilo",9)
		  ));
		
  public final static ArrayList<Artist> artistsB = new ArrayList<Artist>(Arrays.asList(
		  new Artist("Miguel",    "Calo",10),
		  new Artist("Lucio",     "Demare",11),
		  new Artist("Osvalos",   "Fresedo",12),
		  new Artist("Pedro",     "Laurenz",13),
		  new Artist("Francisco", "Lomuto",14),
		  new Artist("",          "Orquesta Tipica Victor",15),
		//  new Artist("",          "Quinteto Pirincho",16),
		  new Artist("Enrique",   "Rodriguez",17)
		  ));
		 
  public final static ArrayList<Artist> artistsC = new ArrayList<Artist>(Arrays.asList(
		  new Artist("Alfredo",   "De Angelise",18),
		  new Artist("Julio",     "De Caro",19),
		  new Artist("Roberto",   "Firpo",20),
		  new Artist("Ciriaco",   "Ortiz",21),
		  new Artist("Astor",     "Piazzolla",22),
		  new Artist("",           "Other",23)
		  ));
		 
		  
  
  
}


