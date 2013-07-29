package tangodj2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBuilder;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class AllPlaylists 
{
	private TableView<Playlist> allPlaylistsTable = new TableView<Playlist>();
	private final static ObservableList<Playlist> allPlaylistsData = FXCollections.observableArrayList();

	public AllPlaylists()
	{
				
		TableColumn nameCol = TableColumnBuilder.create()
				                                .text("Name")
				                                .minWidth(100)
				                                .prefWidth(150)
				                                .build();
		
		nameCol.setCellValueFactory(new PropertyValueFactory<Track, String>("title"));
		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		nameCol.setOnEditCommit(
		          new EventHandler<CellEditEvent<Track, String>>() {
		              @Override
		              public void handle(CellEditEvent<Track, String> t) {
		                  ((Track) t.getTableView().getItems().get(
		                          t.getTablePosition().getRow())
		                          ).setTitle(t.getNewValue());
		              }
		          }
		      );
				
		allPlaylistsTable.getColumns().add(nameCol);
		allPlaylistsTable.setItems(allPlaylistsData);

	}
}
