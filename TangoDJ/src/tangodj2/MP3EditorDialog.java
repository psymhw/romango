package tangodj2;

import java.io.File;
import java.nio.file.Path;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3v1;
import org.farng.mp3.id3.AbstractID3v2;

import tangodj2.tango.TangoTable;
import tangodj2.tango.TangoTrack;

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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MP3EditorDialog extends Stage
{
  TrackDb trackDb;
  TextField title   = new TextField("");
  TextField artist  = new TextField("");
  TextField leader  = new TextField("");
  TextField album   = new TextField("");
 // TextField track_no= new TextField("");
  RestrictiveTextField track_no   =   new RestrictiveTextField();
  //TextField year    = new TextField("");
  RestrictiveTextField year   =   new RestrictiveTextField();
  TextField genre   = new TextField("");
  RestrictiveTextField bpm   =   new RestrictiveTextField();
  TextField singer  = new TextField("");
  TextArea comment = new TextArea("");
 // TextField rating  = new TextField("");
  TangoTrack ttrack;
  TangoTable ttable;
  
  Label pathLabel = new Label();
 // int idx=0;
  final ComboBox styleComboBox = new ComboBox();
 // String style = "Tango";
  
  public MP3EditorDialog(TangoTrack tangoTrack,  TangoTable tangoTable)
  {
    this.ttrack=tangoTrack;
    this.ttable=tangoTable;
   // this.idx=index;
    
	  final int col[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    final int row[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    this.initModality(Modality.APPLICATION_MODAL); 
    
    track_no.setPrefWidth(50);
    track_no.setMaxWidth(50);
    
    bpm.setMaxLength(3);
    bpm.setRestrict("[0-9]");
    bpm.setMaxWidth(35);
    
    year.setMaxLength(4);
    year.setRestrict("[0-9]");
    year.setMaxWidth(45);
    
    track_no.setMaxLength(3);
    track_no.setRestrict("[0-9]");
    track_no.setMaxWidth(35);
    
    comment.setPrefRowCount(2);
    
    
    
    trackDb=Db.getTrackInfo(tangoTrack.getPathHash());
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
    
    
    
    // TODO implement style, singer, adjectives, rating (tango/vals etc)
    
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
    Button okButton = new Button("OK");
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
    //gridPane.add(okButton, col[0], row[2]);
    
    okButton.setOnAction(new EventHandler<ActionEvent>()
    {
      public void handle(ActionEvent arg0) 
      {
       
        updateTrackDb();
        updateTangoTableView();
        Db.updateTrack(trackDb);
        Platform.runLater(new Runnable() 
        {
          public void run() 
          {
            updateMP3tag();
          }});
        close();	
      }});
    double width=300;
    double height=400;
    Scene myDialogScene = new Scene(gridPane, width, height);
    setScene(myDialogScene);
    show();
  } 
  
  private void updateTangoTableView()
  {
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
    
    // this forces the table to update the row
    
    int numberOfColumns=ttable.getColumns().size();
    for(int i=0; i<numberOfColumns; i++)
    {
      if (ttable.getColumns().get(i).isVisible())
      {
        ttable.getColumns().get(i).setVisible(false);
        ttable.getColumns().get(i).setVisible(true);
      }
    }
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
