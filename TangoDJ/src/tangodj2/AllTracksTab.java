package tangodj2;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;


public class AllTracksTab 
{
  Tab tab;
  Tanda tanda;
  SharedValues sharedValues = new SharedValues();
  private TableView<Track> allTracksTable = new TableView<Track>();
  private final static ObservableList<Track> allTracksData = FXCollections.observableArrayList();

	 	
   public AllTracksTab()
   {
	 tab = new Tab();
	 tab.setText("All Tracks");
	 setupAllTracksTable();
	 setData();
	      
	 final Button addButton = new Button("Add");
	 addButton.setOnAction(new EventHandler<ActionEvent>() 
	 {
	   public void handle(ActionEvent e) 
	   {
	     DirectoryChooser directoryChooser = new DirectoryChooser();
	     directoryChooser.setInitialDirectory(new File("C:\\music\\tango"));  // temporary 
	     File selectedDirectory = 
	     directoryChooser.showDialog(TangoDJ2.primaryStage);
	              
	     if(selectedDirectory == null) { System.out.println("No Directory selected"); } 
	     else
	     {
	       try
	       {
	         TrackLoader trackLoader = new TrackLoader(selectedDirectory.toPath().toString());
	         clearTable(); 
	         setData();
	       } catch (Exception ex) {ex.printStackTrace();}
	     }
	   }
	 });

	 final Label label = new Label("All Tracks");
	 label.setFont(new Font("Arial", 20));
	      
	 final VBox vbox = new VBox();
	 vbox.setPadding(new Insets(10, 0, 0, 10));
	 vbox.getChildren().addAll(label, getTable(), addButton);
	      
	 HBox hbox =  new HBox();
	 hbox.setPadding(new Insets(10, 0, 0, 10));
	 hbox.getChildren().add(vbox);
	      
     tanda = new Tanda("Canaro", "Vals");
     hbox.getChildren().add(tanda.getTanda());
	      
	      //((Group) scene.getRoot()).getChildren().addAll(vbox);
     setupListeners();
     tab.setContent(hbox);
   }
	
   public Tab getTab()
   {
	 return tab;
   }
	
   private void setupListeners() 
   {
	 ChangeListener cl = new ChangeListener() 
	 {
	   public void changed(ObservableValue observable, Object oldValue, Object newValue) 
	   {
	     tanda.addTrack(new TandaTrack(sharedValues.title.get()));
	   }
	 };   
	 sharedValues.playlistTrackAdd.addListener(cl);
   }
	
	
   private void setupAllTracksTable() 
   {
	 double tableWidth=setupAllTracksColumns()+43;
	    
	 allTracksTable.setItems(allTracksData);
	 allTracksTable.setPrefWidth(tableWidth);
	 allTracksTable.setMinWidth(tableWidth);
	 allTracksTable.setMaxWidth(tableWidth);
	 allTracksTable.setEditable(false);
	    
	 allTracksTable.setOnKeyReleased(keyEvent);
	    
	 allTracksTable.getSelectionModel().selectedItemProperty()
	                 .addListener(new ChangeListener() 
	                 {
	              	    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
	              		Track selectedTrack = (Track)newValue;
	              		//System.out.println("selected: "+selectedTrack.getTitle());
	              		//tanda.addTrack(new TandaTrack(selectedTrack.getTitle()));
	              		sharedValues.title.set(selectedTrack.getTitle());
	              		sharedValues.pathHash.set(selectedTrack.getPathHash());
	              	}
	             });
	    
   }
	
	  
   private double setupAllTracksColumns()
   {
	 TableColumn titleCol = new TableColumn("Title");
	      titleCol.setMinWidth(100);
	      titleCol.setPrefWidth(150);
	      titleCol.setCellValueFactory(
	          new PropertyValueFactory<Track, String>("title"));
	      titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
	      titleCol.setOnEditCommit(
	          new EventHandler<CellEditEvent<Track, String>>() {
	              @Override
	              public void handle(CellEditEvent<Track, String> t) {
	                  ((Track) t.getTableView().getItems().get(
	                          t.getTablePosition().getRow())
	                          ).setTitle(t.getNewValue());
	              }
	          }
	      );

	      
	       
	  TableColumn artistCol = new TableColumn("Artist");
	      artistCol.setMinWidth(50);
	      artistCol.setPrefWidth(100);
	      artistCol.setCellValueFactory(
	          new PropertyValueFactory<Track, String>("artist"));
	      artistCol.setCellFactory(TextFieldTableCell.forTableColumn());
	      artistCol.setOnEditCommit(
	          new EventHandler<CellEditEvent<Track, String>>() {
	              @Override
	              public void handle(CellEditEvent<Track, String> t) {
	                  ((Track) t.getTableView().getItems().get(
	                      t.getTablePosition().getRow())
	                      ).setArtist(t.getNewValue());
	              }
	          }
	      );
	      
	  TableColumn albumCol = new TableColumn("Album");
	      albumCol.setMinWidth(50);
	      albumCol.setPrefWidth(150);
	      albumCol.setCellValueFactory(
	          new PropertyValueFactory<Track, String>("album"));
	      albumCol.setCellFactory(TextFieldTableCell.forTableColumn());
	      albumCol.setOnEditCommit(
	          new EventHandler<CellEditEvent<Track, String>>() {
	              @Override
	              public void handle(CellEditEvent<Track, String> t) {
	                  ((Track) t.getTableView().getItems().get(
	                      t.getTablePosition().getRow())
	                      ).setAlbum(t.getNewValue());
	              }
	          }
	      );
	      
	  TableColumn genreCol = new TableColumn("Genre");
	      genreCol.setMinWidth(30);
	      genreCol.setPrefWidth(50);
	      genreCol.setCellValueFactory(
	          new PropertyValueFactory<Track, String>("genre"));
	      genreCol.setCellFactory(TextFieldTableCell.forTableColumn());
	      genreCol.setOnEditCommit(
	          new EventHandler<CellEditEvent<Track, String>>() {
	              @Override
	              public void handle(CellEditEvent<Track, String> t) {
	                  ((Track) t.getTableView().getItems().get(
	                      t.getTablePosition().getRow())
	                      ).setGenre(t.getNewValue());
	              }
	          }
	      );
	      
	  TableColumn commentCol = new TableColumn("Comment");
	      commentCol.setMinWidth(50);
	      commentCol.setPrefWidth(100);
	      commentCol.setCellValueFactory(
	          new PropertyValueFactory<Track, String>("comment"));
	      commentCol.setCellFactory(TextFieldTableCell.forTableColumn());
	      commentCol.setOnEditCommit(
	          new EventHandler<CellEditEvent<Track, String>>() {
	              @Override
	              public void handle(CellEditEvent<Track, String> t) {
	                  ((Track) t.getTableView().getItems().get(
	                      t.getTablePosition().getRow())
	                      ).setComment(t.getNewValue());
	              }
	          }
	      );

	     
	  allTracksTable.getColumns().addAll(titleCol, artistCol, albumCol, genreCol, commentCol);
	      
	  return titleCol.getWidth()+artistCol.getWidth()+albumCol.getWidth()+genreCol.getWidth()+commentCol.getWidth();
  }
	  
	//KEY EVENTS
	 	EventHandler<KeyEvent>keyEvent = new EventHandler<KeyEvent>() 
	     {
	 	  public void handle(KeyEvent ke) 
	 	  {
	 	    if (ke.getCode()==KeyCode.ESCAPE) 
	 	    {
	 	    	System.out.println("Esc Pressed");
	 	    	
	 	    }
	 	    if (ke.getCode()==KeyCode.UP) 
	 	    {
	 	    	System.out.println("UP Pressed");
	 	    }
	 	    if (ke.getCode()==KeyCode.DOWN) 
	 	    {
	 	    	System.out.println("DOWN Pressed");
	 	    }
	 	    if (ke.getCode()==KeyCode.RIGHT) 
	 	    {
	 	    	
	 	      sharedValues.playlistTrackAdd.set(sharedValues.playlistTrackAdd.get()+1);  
	 	     System.out.println("RIGHT Pressed: "+sharedValues.playlistTrackAdd.get());
	 	    }
	 	  }
	 	};
	 	
	 	public TableView<Track> getTable() {
			return allTracksTable;
		}
	 	
	 	public static void setData()
	 	  {
	 	    String title;
	 	    String artist;
	 	    String album;
	 	    String genre;
	 	    String comment;
	 	    String pathHash;
	 	    
	 	    try
	 	    {
	 			Connection connection = DriverManager.getConnection("jdbc:derby:tango_db;create=false");
	 			Statement statement = connection.createStatement();
	 			ResultSet resultSet = statement.executeQuery("select * from tracks");
	 			while(resultSet.next())
	 			{
	 			  title=resultSet.getString("title");
	 			  artist = resultSet.getString("artist");
	 			  album = resultSet.getString("album");
	 			  genre = resultSet.getString("genre");
	 			  comment = resultSet.getString("comment");
	 			  pathHash = resultSet.getString("pathHash");
	 			  allTracksData.add(new Track(title, artist, album, genre, comment, pathHash));
	 			  
	 			  //System.out.println("added: "+title);
	 			}
	 			if (statement!=null) statement.close();
	 			if (connection!=null) connection.close();
	 	    } catch (Exception e) { e.printStackTrace();}
	 	  }
	 	
	 	public void clearTable()
	 	{
	 	  allTracksData.clear();
	 	}
	  
	}