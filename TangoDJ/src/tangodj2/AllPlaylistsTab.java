package tangodj2;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBuilder;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class AllPlaylistsTab 
{
  Tab tab;
  private TableView<Playlist> allPlaylistsTable = new TableView<Playlist>();
  private final static ObservableList<Playlist> allPlaylistsData = FXCollections.observableArrayList();

	
  public AllPlaylistsTab()
  {
	tab = new Tab();
	tab.setText("Playlists");
	
	TableColumn nameCol = TableColumnBuilder.create()
            .text("Name")
            .minWidth(100)
            .prefWidth(150)
            .build();

     nameCol.setCellValueFactory(new PropertyValueFactory<Track, String>("name"));
    
     
     TableColumn idCol = TableColumnBuilder.create()
             .text("ID")
             .minWidth(20)
             .maxWidth(20)
             .prefWidth(20)
             .build();

      idCol.setCellValueFactory(new PropertyValueFactory<Track, String>("id"));
     
    
      TableColumn locationCol = TableColumnBuilder.create()
              .text("Location")
              .minWidth(100)
              .prefWidth(150)
              .build();

       locationCol.setCellValueFactory(new PropertyValueFactory<Track, String>("location"));
       
       
     //allPlaylistsTable.setMaxWidth(340);
     allPlaylistsTable.setEditable(false);
     allPlaylistsTable.getColumns().addAll(idCol, nameCol, locationCol);
     
     allPlaylistsTable.setItems(allPlaylistsData);  
	
	setData();
	
	final ContextMenu contextMenu = new ContextMenu();
	

	MenuItem item1 = new MenuItem("About");
	item1.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent e) {
	        System.out.println("About");
	    }
	});
	MenuItem item2 = new MenuItem("Preferences");
	item2.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent e) {
	        System.out.println("Preferences");
	    }
	});
	contextMenu.getItems().addAll(item1, item2);
	allPlaylistsTable.setContextMenu(contextMenu);
	 
	 
    
	
	
	final Label label = new Label("Playlists");
    label.setFont(new Font("Arial", 20));
    
    final VBox vbox = new VBox(5);
    
    vbox.setPadding(new Insets(10, 10, 10, 10));
    vbox.getChildren().addAll(label, allPlaylistsTable, getEntryBox());
    vbox.setMaxWidth(350);
    tab.setContent(vbox);
  }
  
  private void setData() 
  {
	String name="";
	String location = "";
	Date incept;
	int id;
	  try
 	    {
 			Connection connection = DriverManager.getConnection("jdbc:derby:tango_db;create=false");
 			Statement statement = connection.createStatement();
 			ResultSet resultSet = statement.executeQuery("select * from playlists");
 			while(resultSet.next())
 			{
 			  name=resultSet.getString("name");
 			  location = resultSet.getString("location");
 			  incept = resultSet.getDate("incept");
 			  id = resultSet.getInt("id");
 			 allPlaylistsData.add(new Playlist(name, location, incept, id));
 			  
 			//  System.out.println("added: "+name);
 			}
 			if (statement!=null) statement.close();
 			if (connection!=null) connection.close();
 	    } catch (Exception e) { e.printStackTrace();}
 	  }

	


public Tab getTab()
  {
    return tab;
  }

  private VBox getEntryBox()
  {
	VBox vbox = new VBox(5);
	vbox.setPadding(new Insets(10, 10, 10, 10));
	vbox.setSpacing(5);
	//vbox.setMaxWidth(350);
	
	vbox.setStyle("-fx-border-style: solid;"
            + "-fx-border-width: 1;"
            + "-fx-border-color: black");
	
	final Label panelLabel    = new Label("New Playlist");
	final Label nameLabel     = new Label(" Name: ");
	final Label locationLabel = new Label(" Location: ");
	
    panelLabel.setFont(   new Font("Arial", 20));
    nameLabel.setFont(    new Font("Arial", 18));
    locationLabel.setFont(new Font("Arial", 18));
    
    final TextField nameField = new TextField();
    final TextField locationField = new TextField();
    
    GridPane gridPane = new GridPane();
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    gridPane.setVgap(2);
    gridPane.setHgap(15);
    
    int row=0;
    gridPane.add(nameLabel, 0, row);
    gridPane.add(nameField, 1, row++);
    
    gridPane.add(locationLabel, 0, row);
    gridPane.add(locationField, 1, row++);
    
    GridPane.setHalignment(nameLabel, HPos.RIGHT);
    GridPane.setHalignment(locationLabel, HPos.RIGHT);

    final Button addButton = new Button("New Playlist");
    addButton.setOnAction(new EventHandler<ActionEvent>() 
    {
      public void handle(ActionEvent e) 
      {
    	addPlaylist(nameField.getText(), locationField.getText());
    	nameField.clear();
    	locationField.clear();
      }
    });
    
    vbox.getChildren().addAll(panelLabel, gridPane, addButton);
   
	return vbox;
  }
  
  private void addPlaylist(String name, String location)
  {
	 if (name==null) return;
	 if (name.trim().length()==0) return;
	 Connection connection=null;
	 
	 try
	 {
	   Class.forName(SharedValues.DRIVER);
	   connection = DriverManager.getConnection(SharedValues.JDBC_URL);
	   String sql="insert into playlists (name, location) values ('"+name+"', '"+location+"')";
	   connection.createStatement().execute(sql);
	   allPlaylistsData.clear();
	   setData();
	 } 
	 catch (Exception e) { e.printStackTrace(); }
	 finally 
	 { 
	   try 
	   {
		 connection.close(); 
	   }
	   catch (Exception ex) { ex.printStackTrace(); }
	 }
   }
	 
}

