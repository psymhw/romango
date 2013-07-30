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
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBuilder;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
             .minWidth(100)
             .prefWidth(150)
             .build();

      idCol.setCellValueFactory(new PropertyValueFactory<Track, String>("id"));
     
    
      TableColumn locationCol = TableColumnBuilder.create()
              .text("Location")
              .minWidth(100)
              .prefWidth(150)
              .build();

       locationCol.setCellValueFactory(new PropertyValueFactory<Track, String>("location"));
       
       

     allPlaylistsTable.getColumns().addAll(idCol, nameCol, locationCol);
     
     allPlaylistsTable.setItems(allPlaylistsData);  
	
	setData();
	
	
	final Button addButton = new Button("New Playlist");
    addButton.setOnAction(new EventHandler<ActionEvent>() 
    {
      public void handle(ActionEvent e) 
      {
    	  System.out.println("New Playlist");
    	  addPlaylist();
      }
    });
	final Label label = new Label("Playlists");
    label.setFont(new Font("Arial", 20));
    
    final VBox vbox = new VBox();
    vbox.setPadding(new Insets(10, 0, 0, 10));
    vbox.getChildren().addAll(label, allPlaylistsTable, getEntryBox(), addButton);
    
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
 			  
 			  System.out.println("added: "+name);
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
	VBox vbox = new VBox();
	final Label label = new Label("New Playlist");
    label.setFont(new Font("Arial", 20));
    vbox.getChildren().add(label);
    
    HBox hb1 = new HBox();
    final Label label2 = new Label(" Name: ");
    label2.setFont(new Font("Arial", 18));
    hb1.getChildren().add(label2);
    
    vbox.getChildren().add(hb1);
 
    
   
	return vbox;
  }
  
  private void addPlaylist()
  {
	 String DRIVER ="org.apache.derby.jdbc.EmbeddedDriver";
	 String JDBC_URL ="jdbc:derby:tango_db;create=false";
	 Connection connection=null;
	 
	 try
	 {
	   Class.forName(DRIVER);
	   connection = DriverManager.getConnection(JDBC_URL);
	   String sql="insert into playlists (name, location) values ('Test Playlist', 'Saturday Milonga')";
	   connection.createStatement().execute(sql);
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

