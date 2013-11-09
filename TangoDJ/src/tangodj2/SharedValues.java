package tangodj2;

import java.util.ArrayList;
import java.util.Arrays;

import tangodj2.cortina.Cortina;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SharedValues 
{
  //public final static ObservableList<Track> allTracksData = FXCollections.observableArrayList();
 // public final static ObservableList<Cortina> cortinaTracksData = FXCollections.observableArrayList();
  public static SimpleStringProperty title = new SimpleStringProperty();
  public static SimpleStringProperty selectedTangoPathHash = new SimpleStringProperty();
  public static SimpleStringProperty selectedCleanupPathHash = new SimpleStringProperty();
  //public static SimpleIntegerProperty playlistFocus = new SimpleIntegerProperty(0);
 // public static Playlist playlist;
  public static String DRIVER ="org.apache.derby.jdbc.EmbeddedDriver";
  public static String JDBC_URL ="jdbc:derby:tango_db;create=false";
//  public static int currentPlaylist = 1;
 // public static int selectedTanda = -1;
  //public static int selectedPlaylistTrack = -1;
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
  
  public final static ArrayList<ArtistX> artistsA = new ArrayList<ArtistX>(Arrays.asList(
		  new ArtistX("Rodolfo",   "Biagi", 1),
		  new ArtistX("Francisco", "Canaro", 2),
		  new ArtistX("Angel",     "D'Agostino",3),
		  new ArtistX("Juan",      "D'Arienzo",4),
		  new ArtistX("Carlos",    "Di Sarli",5),
		  new ArtistX("Edgardo",   "Donato",6),
		  new ArtistX("Osvaldo",   "Pugliese",7),
		  new ArtistX("Ricardo",   "Tanturi",8),
		  new ArtistX("Anibal",     "Troilo",9)
		  ));
		
  public final static ArrayList<ArtistX> artistsB = new ArrayList<ArtistX>(Arrays.asList(
		  new ArtistX("Miguel",    "Calo",10),
		  new ArtistX("Lucio",     "Demare",11),
		  new ArtistX("Osvalos",   "Fresedo",12),
		  new ArtistX("Pedro",     "Laurenz",13),
		  new ArtistX("Francisco", "Lomuto",14),
		  new ArtistX("",          "Orquesta Tipica Victor",15),
		//  new ArtistX("",          "Quinteto Pirincho",16),
		  new ArtistX("Enrique",   "Rodriguez",17)
		  ));
		 
  public final static ArrayList<ArtistX> artistsC = new ArrayList<ArtistX>(Arrays.asList(
		  new ArtistX("Alfredo",   "De Angelise",18),
		  new ArtistX("Julio",     "De Caro",19),
		  new ArtistX("Roberto",   "Firpo",20),
		  new ArtistX("Ciriaco",   "Ortiz",21),
		  new ArtistX("Astor",     "Piazzolla",22),
		  new ArtistX("",           "Other",23)
		  ));
		 
		  
  
  
}


