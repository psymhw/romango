package tangodj2;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3v2;

import tangodj2.favorites.FavoritesTab;
import tangodj2.favorites.FavoritesTable;
import tangodj2.favorites.FavoritesTrack;
import tangodj2.tango.TangoTable;
import tangodj2.tango.TangoTrack;

public class MP3EditorDialog extends Stage
{
  TrackDb trackDb;
  TextField title   = new TextField("");
  TextField artist  = new TextField("");
  TextField leader  = new TextField("");
  TextField album   = new TextField("");
  RestrictiveTextField track_no   =   new RestrictiveTextField();
  RestrictiveTextField year   =   new RestrictiveTextField();
  TextField genre   = new TextField("");
  RestrictiveTextField bpm   =   new RestrictiveTextField();
  TextField singer  = new TextField("");
  TextArea comment = new TextArea("");
  TangoTrack ttrack;
  TangoTable ttable;
  
  FavoritesTrack ftrack;
  FavoritesTable ftable;
  FavoritesTab favoritesTab;
  
  public static final int PLAYLIST_BUILDER_FAVORITES_TABLE = 0;
  public static final int TANGO_TABLE = 1;
  public static final int FAVORITES_TAB_FAVORITES_TABLE = 2;  // TODO not yet implemented
  private int type;
  
  Button okButton;
  
  Label pathLabel = new Label();
  final ComboBox styleComboBox = new ComboBox();
  
  double width=500;
  double height=500;
  
  public MP3EditorDialog(TangoTrack tangoTrack,  TangoTable tangoTable)
  {
    this.ttrack=tangoTrack;
    this.ttable=tangoTable;
    
    type = TANGO_TABLE;
    
   
    
    trackDb=Db.getTrackInfo(tangoTrack.getPathHash());
    GridPane gridPane = getEditor();
    
    
   
    Scene myDialogScene = new Scene(gridPane, width, height);
    setScene(myDialogScene);
    show();
  } 
  
  public MP3EditorDialog(FavoritesTrack favoritesTrack,  FavoritesTable favoritesTable)
  {
	  
    this.ftrack=favoritesTrack;
    this.ftable=favoritesTable;
    this.favoritesTab=favoritesTab;
    
    type = PLAYLIST_BUILDER_FAVORITES_TABLE;
    
    trackDb=Db.getTrackInfo(favoritesTrack.getPathHash());
    GridPane gridPane = getEditor();
    
    Scene myDialogScene = new Scene(gridPane, width, height);
    setScene(myDialogScene);
    show();
  } 
  
  private GridPane getEditor()
  {
    final int col[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    final int row[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    this.initModality(Modality.APPLICATION_MODAL); 
    
    /*
    title.setStyle(""
            + "-fx-font-size: 30px;"
            + "-fx-font-style: italic;"
            + "-fx-font-weight: bold;"
            + "-fx-font-family: fantasy;"
            + "-fx-text-fill: blue;"
            + "-fx-background-color: aqua");
    */
    title.setStyle("-fx-font-size: 20px;");
    artist.setStyle("-fx-font-size: 20px;");
    leader.setStyle("-fx-font-size: 20px;"); 
    album.setStyle("-fx-font-size: 20px;");
    track_no.setStyle("-fx-font-size: 20px;");
    year.setStyle("-fx-font-size: 20px;");
    genre.setStyle("-fx-font-size: 20px;");
    bpm.setStyle("-fx-font-size: 20px;");
    singer.setStyle("-fx-font-size: 20px;");
    comment.setStyle("-fx-font-size: 20px;");
    
    
    
    bpm.setMaxLength(3);
    bpm.setRestrict("[0-9]");
    bpm.setMinWidth(50);
    bpm.setMaxWidth(50);
    
    year.setMaxLength(4);
    year.setRestrict("[0-9]");
    year.setMaxWidth(75);
    year.setMinWidth(75);  
    
    track_no.setMaxLength(3);
    track_no.setRestrict("[0-9]");
    track_no.setMinWidth(50);
    track_no.setPrefWidth(50);
    track_no.setMaxWidth(50);
    
    comment.setPrefRowCount(2);
    
    title.setText(trackDb.title);
    leader.setText(trackDb.leader);
    artist.setText(trackDb.artist);
    album.setText(trackDb.album);
    track_no.setText(""+trackDb.track_no);
    year.setText(trackDb.track_year);
    genre.setText(trackDb.genre);
    comment.setText(trackDb.comment);
    bpm.setText(trackDb.bpm);
    singer.setText(trackDb.singer);
    pathLabel.setText(trackDb.path);
    
    
    final ComboBox styleComboBox = new ComboBox();
    styleComboBox.getItems().addAll(
        "Tango",
        "Vals",
        "Milonga",
        "Chacarera",
        "Candombe",
        "Alternative"
    );   
    
    styleComboBox.getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener<String>() {
             public void changed(ObservableValue<? extends String> ov, 
                 String old_val, String new_val) { trackDb.style=new_val; } });

    styleComboBox.setValue(trackDb.style);
    
    final ComboBox ratingComboBox = new ComboBox();
    ratingComboBox.getItems().addAll(
        "",
        "*",
        "**",
        "***",
        "****",
        "*****"
    );   
    
    ratingComboBox.getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener<String>() {
             public void changed(ObservableValue<? extends String> ov, 
                 String old_val, String new_val) { trackDb.rating=new_val; } });

    ratingComboBox.setValue(trackDb.rating);
    
    System.out.println("MP3Editor: "+trackDb.title);
    okButton = new Button("OK");
    GridPane gridPane = new GridPane();
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    gridPane.setVgap(5);
    gridPane.setHgap(5);
    int labelRow=0;
    gridPane.add(new Label("Title: "),   col[0], row[labelRow++]);
    gridPane.add(new Label("Leader: "),  col[0], row[labelRow++]);
    gridPane.add(new Label("Artist: "),  col[0], row[labelRow++]);
    gridPane.add(new Label("Album: "),   col[0], row[labelRow++]);
    gridPane.add(new Label("Track: "),   col[0], row[labelRow++]);
    gridPane.add(new Label("Year: "),    col[0], row[labelRow++]);
    gridPane.add(new Label("Genre: "),   col[0], row[labelRow++]);
    gridPane.add(new Label("Style: "),   col[0], row[labelRow++]);
    gridPane.add(new Label("BPM: "),     col[0], row[labelRow++]);
    gridPane.add(new Label("Singer: "),  col[0], row[labelRow++]);
    gridPane.add(new Label("Comment: "), col[0], row[labelRow++]);
    gridPane.add(new Label("Rating: "),  col[0], row[labelRow++]);
    gridPane.add(new Label("File: "),  col[0], row[labelRow++]);
    
    int fieldRow=0;
    gridPane.add(title,    col[1], row[fieldRow++]);
    gridPane.add(leader,   col[1], row[fieldRow++]);
    gridPane.add(artist,   col[1], row[fieldRow++]);
    gridPane.add(album,    col[1], row[fieldRow++]);
    gridPane.add(track_no, col[1], row[fieldRow++]);
    gridPane.add(year,     col[1], row[fieldRow++]);
    gridPane.add(genre,    col[1], row[fieldRow++]);
    gridPane.add(styleComboBox,    col[1], row[fieldRow++]);
    gridPane.add(bpm,   col[1], row[fieldRow++]);
    gridPane.add(singer,   col[1], row[fieldRow++]);
    gridPane.add(comment,  col[1], row[fieldRow++]);
    gridPane.add(ratingComboBox,   col[1], row[fieldRow++]);
    gridPane.add(pathLabel,  col[1], row[fieldRow++]);
    gridPane.add(okButton,   col[1], row[fieldRow++]);
    
   // GridPane.setHalignment(handicapLabel, HPos.RIGHT);
    ColumnConstraints col0 = new ColumnConstraints();
    ColumnConstraints col1 = new ColumnConstraints();
    col0.setHalignment(HPos.RIGHT);
    col0.setMinWidth(60);
    col1.setMinWidth(200);
    gridPane.getColumnConstraints().addAll(col0, col1);
    
    okButton.setOnAction(new EventHandler<ActionEvent>()
        {
          public void handle(ActionEvent arg0) 
          {
            updateTrackDb();
            Db.updateTrack(trackDb);
            if (type==TANGO_TABLE) ttable.replaceRow(new TangoTrack(trackDb));
            if (type==PLAYLIST_BUILDER_FAVORITES_TABLE) updateFavoritesTableView();
            
            Platform.runLater(new Runnable() 
            {
              public void run() 
              {
                updateMP3tag();
              }});
            close();  
          }});
    
    return gridPane;
    //gridPane.add(okButton, col[0], row[2]);
  }
  
  private void updateTangoTableView()
  {
	/*  
    ttrack.setTitle(trackDb.title);
    ttrack.setArtist(trackDb.artist);
    ttrack.setAlbum(trackDb.album);
    ttrack.setTrack_year(trackDb.track_year);
    ttrack.setGenre(trackDb.genre);
    ttrack.setComment(trackDb.comment);
    ttrack.setStyle(trackDb.style);
    ttrack.setSinger(trackDb.singer);
    ttrack.setBpm(trackDb.bpm);
    ttrack.setLeader(trackDb.leader);
    ttrack.setRating(trackDb.rating);
    */
    
    // new method
    //trackDb=Db.getTrackInfo(trackDb.pathHash);
    ttable.replaceRow(new TangoTrack(trackDb));
    
 // this forces the table to update the row
    /*
    int numberOfColumns=ttable.getColumns().size();
    for(int i=0; i<numberOfColumns; i++)
    {
      if (ttable.getColumns().get(i).isVisible())
      {
        ttable.getColumns().get(i).setVisible(false);
        ttable.getColumns().get(i).setVisible(true);
      }
    }
    */
  }
  
  private void updateFavoritesTableView()
  {
    ftrack.setTitle(trackDb.title);
    ftrack.setArtist(trackDb.artist);
    ftrack.setAlbum(trackDb.album);
    ftrack.setTrack_year(trackDb.track_year);
    ftrack.setGenre(trackDb.genre);
    ftrack.setComment(trackDb.comment);
    ftrack.setStyle(trackDb.style);
    ftrack.setSinger(trackDb.singer);
    ftrack.setBpm(trackDb.bpm);
    ftrack.setLeader(trackDb.leader);
    ftrack.setRating(trackDb.rating);
    
    // this forces the table to update the row
    int numberOfColumns=ftable.getColumns().size();
    for(int i=0; i<numberOfColumns; i++)
    {
      if (ftable.getColumns().get(i).isVisible())
      {
        ftable.getColumns().get(i).setVisible(false);
        ftable.getColumns().get(i).setVisible(true);
      }
    }
    
    TangoTable.updateRow(trackDb);
    //TangoDJ2.favoritesTab.updateTableRow(trackDb);
    
  }
  
  private void updateTrackDb()
  {
    trackDb.title=notNull(title.getText());
    trackDb.artist=notNull(artist.getText());
    trackDb.album=notNull(album.getText());
    trackDb.track_year=notNull(year.getText());
    trackDb.genre=notNull(genre.getText());
    trackDb.singer=notNull(singer.getText());
    trackDb.comment=notNull(comment.getText());
    trackDb.leader=notNull(leader.getText());
    trackDb.adjectives="";
    // trackDb.rating=rating.getText();
    //trackDb.style=style; set in combobox
    trackDb.track_no=Integer.parseInt(track_no.getText());
    trackDb.bpm=notNull(bpm.getText());
  }
  
  private String notNull(String inStr)
  {
    if (inStr==null) return "";
    else return inStr;
  }
  
  private void updateMP3tag() 
  {
    MP3File mp3 = null;
    AbstractID3v2 tag;
   
    File file = new File(trackDb.path);
   
    String message;
      
    try { mp3= new MP3File(file); } catch (Exception e) 
    { 
      System.out.println(" Could not create MP3File class: "+trackDb.path); 
      return;  
    }
    
    try { tag = mp3.getID3v2Tag();  } catch (Exception e2) 
    {  
      System.out.println(" Could not get ID3v2 tag: "+trackDb.path); 
      return;  
    }
     
    if (tag==null)
    {
      System.out.println(" tag is null: "+trackDb.path+"\n"); 
      return;  
    }
    
    tag.setSongTitle(trackDb.title);
    tag.setLeadArtist(trackDb.artist);
    tag.setAlbumTitle(trackDb.album);
    tag.setYearReleased(trackDb.track_year);
    tag.setSongGenre(trackDb.genre);
    tag.setSongComment(trackDb.comment);
    tag.setTrackNumberOnAlbum(""+trackDb.track_no);
    mp3.setID3v2Tag(tag);
    try
    {
      mp3.save();
    } catch (Exception e) { e.printStackTrace(); } 
  }
  
}
