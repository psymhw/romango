package tangodj.tangodj2;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class TangoDJ2 extends Application 
{
  private TableView<Track> table = new TableView<Track>();
  private final static ObservableList<Track> data = FXCollections.observableArrayList();
  Stage primaryStage;
  Tanda tanda;
	
  public static void main(String[] args) 
  {
    try { new CreateDatabase(); } catch (Exception e) { e.printStackTrace(); }
    setData();
    launch(args);
  }
	
  public void start(Stage stage) 
  {
	  primaryStage=stage;
	  Group root = new Group();
      Scene scene = new Scene(root, 950, 550, Color.WHITE);
    
      TabPane tabPane = new TabPane();
      BorderPane mainPane = new BorderPane();
      
      setupAllTracksTable();
      
     
      Tab tabA = new Tab();
      tabA.setText("All Tracks");
      tabPane.getTabs().add(tabA);
    
      Tab tabB = new Tab();
      tabB.setText("Tab B");
      tabPane.getTabs().add(tabB);
    
      Tab tabC = new Tab();
      tabC.setText("Tab C");
      tabPane.getTabs().add(tabC);
    
      mainPane.setCenter(tabPane);
      //mainPane.getChildren().add(tabPane);
    
      mainPane.prefHeightProperty().bind(scene.heightProperty());
      mainPane.prefWidthProperty().bind(scene.widthProperty());
    
      root.getChildren().add(mainPane);
      
      final Button addButton = new Button("Add");
      addButton.setOnAction(new EventHandler<ActionEvent>() 
      {
        public void handle(ActionEvent e) 
        {
          DirectoryChooser directoryChooser = new DirectoryChooser();
          directoryChooser.setInitialDirectory(new File("C:\\music\\tango"));  // temporary 
          File selectedDirectory = 
          directoryChooser.showDialog(primaryStage);
              
          if(selectedDirectory == null)
          {
             System.out.println("No Directory selected");
          } 
          else
          {
             try
             {
            	
              	TrackLoader trackLoader = new TrackLoader(selectedDirectory.toPath().toString());
              	data.clear(); 
              	setData();
              } catch (Exception ex) {ex.printStackTrace();}
          }
        }
      });
     
      

      final Label label = new Label("All Tracks");
      label.setFont(new Font("Arial", 20));
      
      final VBox vbox = new VBox();
      vbox.setPadding(new Insets(10, 0, 0, 10));
      vbox.getChildren().addAll(label, table, addButton);
      
      HBox hbox =  new HBox();
      hbox.setPadding(new Insets(10, 0, 0, 10));
      ;
      
      hbox.getChildren().add(vbox);
      
      tanda = new Tanda("Canaro", "Vals");
      hbox.getChildren().add(tanda.getTanda());
      
      //((Group) scene.getRoot()).getChildren().addAll(vbox);
      tabA.setContent(hbox);
            
      primaryStage.setScene(scene);
      primaryStage.show();
  }

private void setupAllTracksTable() 
{
	double tableWidth=setupAllTracksColumns()+43;
    
    table.setItems(data);
    table.setPrefWidth(tableWidth);
    table.setMinWidth(tableWidth);
    table.setMaxWidth(tableWidth);
    table.setEditable(false);
    
    table.setOnKeyReleased(keyEvent);
    
    
    table.getSelectionModel().selectedItemProperty()
                 .addListener(new ChangeListener() {
              	public void changed(ObservableValue observable, Object oldValue, Object newValue) {
              		Track selectedTrack = (Track)newValue;
              		System.out.println("selected: "+selectedTrack.getTitle());
              		tanda.addTrack(new TandaTrack(selectedTrack.getTitle()));
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

     
     // table.getColumns().addAll(titleCol, artistCol, albumCol, genreCol, commentCol);
      table.getColumns().add(titleCol);
      table.getColumns().add(artistCol);
      table.getColumns().add(albumCol);
      table.getColumns().add(genreCol);
      table.getColumns().add(commentCol);
      
      EventHandler <MouseEvent>click = new EventHandler<MouseEvent>() 
      {
        public void handle(MouseEvent ev) 
        {
    		if (MouseEvent.MOUSE_CLICKED != null) System.out.println("mouse clicked");  
    		if (MouseEvent.MOUSE_ENTERED != null) System.out.println("drag entered");   
        }
      };
    		      
      GenericCellFactory cellFactory = new GenericCellFactory(click);
      titleCol.setCellFactory(cellFactory);
      
   
      //table.setPrefWidth(titleCol.getWidth()+artistCol.getWidth()+albumCol.getWidth()+genreCol.getWidth()+commentCol.getWidth());
     
      //table.setMinWidth(700);
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
 	    if (ke.getCode()==KeyCode.ENTER) 
 	    {
 	    	System.out.println("ENTER Pressed");    	
 	    }
 	  }
 	};
 	
     
  
  private static void setData()
  {
    String title;
    String artist;
    String album;
    String genre;
    String comment;
    
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
		  data.add(new Track(title, artist, album, genre, comment));
		  
		  //System.out.println("added: "+title);
		}
		if (statement!=null) statement.close();
		if (connection!=null) connection.close();
    } catch (Exception e) { e.printStackTrace();}
  }
  
  

}
