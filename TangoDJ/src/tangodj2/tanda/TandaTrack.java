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
  private long dbId;
  private TandaDb tandaDb;
  
  
public TandaTrack(TandaDb tandaDb)
  {
	artist = new SimpleStringProperty(tandaDb.getArtist());
	style = new SimpleStringProperty(tandaDb.getStyle());
	comment = new SimpleStringProperty(tandaDb.getComment());
	playlistName = new SimpleStringProperty(Db.getPlaylistName(tandaDb.getPlaylistId()));
	track_0_title = new SimpleStringProperty(Db.getTrackTitle(tandaDb.getTrackHash_0()));
	track_1_title = new SimpleStringProperty(Db.getTrackTitle(tandaDb.getTrackHash_1()));
	track_2_title = new SimpleStringProperty(Db.getTrackTitle(tandaDb.getTrackHash_2()));
	track_3_title = new SimpleStringProperty(Db.getTrackTitle(tandaDb.getTrackHash_3()));
	cortina_title = new SimpleStringProperty(Db.getCortinaTitle(tandaDb.getCortinaId()));
	dbId=tandaDb.getId();
	this.tandaDb=tandaDb;
  }

public TandaDb getTandaDb() {
	return tandaDb;
}

public void update(TandaDb tandaDb)
{
  artist.set(tandaDb.getArtist());
  style.set(tandaDb.getStyle());
  comment.set(tandaDb.getComment());
  playlistName.set(Db.getPlaylistName(tandaDb.getPlaylistId()));
  track_0_title.set(Db.getTrackTitle(tandaDb.getTrackHash_0()));
  track_1_title.set(Db.getTrackTitle(tandaDb.getTrackHash_1()));
  track_2_title.set(Db.getTrackTitle(tandaDb.getTrackHash_2()));
  track_3_title.set(Db.getTrackTitle(tandaDb.getTrackHash_3()));
  cortina_title.set(Db.getCortinaTitle(tandaDb.getCortinaId()));
}

public long getDbId() {
	return dbId;
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
