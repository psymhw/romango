package tangodj2.tanda;

import tangodj2.Db;
import tangodj2.TandaDb;
import javafx.beans.property.SimpleStringProperty;

public class TandaTrack 
{
  private final SimpleStringProperty artist;
  private final SimpleStringProperty style;
  private final SimpleStringProperty playlistName;
  private final SimpleStringProperty comment;
  private final SimpleStringProperty track_0_title;
  private final SimpleStringProperty track_1_title;
  private final SimpleStringProperty track_2_title;
  private final SimpleStringProperty track_3_title;
  private final SimpleStringProperty cortina_title;
  
  public TandaTrack(TandaDb tandaDb)
  {
	this.artist = new SimpleStringProperty(tandaDb.getArtist());
	this.style = new SimpleStringProperty(tandaDb.getStyle());
	this.comment = new SimpleStringProperty(tandaDb.getComment());
	this.playlistName = new SimpleStringProperty(Db.getPlaylistName(tandaDb.getPlaylistId()));
	this.track_0_title = new SimpleStringProperty(Db.getTrackTitle(tandaDb.getTrackHash_0()));
	this.track_1_title = new SimpleStringProperty(Db.getTrackTitle(tandaDb.getTrackHash_1()));
	this.track_2_title = new SimpleStringProperty(Db.getTrackTitle(tandaDb.getTrackHash_2()));
	this.track_3_title = new SimpleStringProperty(Db.getTrackTitle(tandaDb.getTrackHash_3()));
	this.cortina_title = new SimpleStringProperty(Db.getCortinaTitle(tandaDb.getCortinaId()));
  }

public String getArtist() {
	return artist.get();
}

public String getStyle() {
	return style.get();
}

public String getPlaylistName() {
	return playlistName.get();
}

public String getComment() {
	return comment.get();
}

public String getTrack_0_title() {
	return track_0_title.get();
}

public String getTrack_1_title() {
	return track_1_title.get();
}

public String getTrack_2_title() {
	return track_2_title.get();
}

public String getTrack_3_title() {
	return track_3_title.get();
}

public String getCortina_title() {
	return cortina_title.get();
}
}
