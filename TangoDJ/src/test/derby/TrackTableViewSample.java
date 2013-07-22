package test.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
 
public class TrackTableViewSample extends Application 
{
    private TableView<Track> table = new TableView<Track>();
    private final static ObservableList<Track> data =
            FXCollections.observableArrayList();
    final HBox hb = new HBox();
 
    public static void main(String[] args) 
    {
    	setData();
        launch(args);
    }
 
    private static void setData()
    {
     // data.add(new Track("Jacob", "Smith"))	;
     // data.add(new Track("Isabella", "Johnson"));
    //  data.add(new Track("Ethan", "Williams"));
    //  data.add(new Track("Emma", "Jones"));
    //  data.add(new Track("Michael", "Brown"));
      
      String title;
      String artist;
      try
      {
		Connection connection = DriverManager.getConnection(TrackLoader.JDBC_URL2);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from tracks");
		while(resultSet.next())
		{
			title=resultSet.getString("title");
			artist = resultSet.getString("artist");
			 data.add(new Track(title, artist));
		}
		if (statement!=null) statement.close();
		if (connection!=null) connection.close();
      } catch (Exception e) { e.printStackTrace();}
    }
    
    @Override
    public void start(Stage stage) 
    {
        Scene scene = new Scene(new Group());
        stage.setTitle("All Tracks");
        stage.setWidth(450);
        stage.setHeight(550);
 
        final Label label = new Label("All Tracks");
        label.setFont(new Font("Arial", 20));
 
        table.setEditable(true);
 
        TableColumn titleCol = new TableColumn("Title");
        titleCol.setMinWidth(100);
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
        artistCol.setMinWidth(100);
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
 
     
 
        table.setItems(data);
        table.getColumns().addAll(titleCol, artistCol);
 
        final TextField addTitle = new TextField();
        addTitle.setPromptText("Title");
        addTitle.setMaxWidth(titleCol.getPrefWidth());
        
        final TextField addArtist = new TextField();
        addArtist.setMaxWidth(artistCol.getPrefWidth());
        addArtist.setPromptText("Artist");
                     
 
        final Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                data.add(new Track(
                        addTitle.getText(),
                        addArtist.getText()
                        ));
                addTitle.clear();
                addArtist.clear();
            }
        });
 
        hb.getChildren().addAll(addTitle, addArtist,  addButton);
        hb.setSpacing(3);
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        stage.setScene(scene);
        stage.show();
    }
 
    
    
    public static class Track
    {
		private final SimpleStringProperty title;
        private final SimpleStringProperty artist;
        
        private Track(String titleStr, String artistStr)
        {
          this.title = new SimpleStringProperty(titleStr);
          this.artist = new SimpleStringProperty(artistStr);
        }
        
        public String getTitle() {
        	return title.get(); }
        
        public void setTitle(String titleStr) {
        	title.set(titleStr); }
        
        public String getArtist() {
        	return artist.get(); }
        
        public void setArtist(String artistStr) {
        	artist.set(artistStr); }
        
 
    }
}