package tangodj2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class AllPlaylists 
{
	private TableView<Playlist> allPlaylistsTable = new TableView<Playlist>();
	private final static ObservableList<Playlist> allPlaylistsData = FXCollections.observableArrayList();

}
