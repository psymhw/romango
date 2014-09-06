package tangodj2;

public class TandaDb 
{
  private long id;
  private long playlistId;
  private int position;
  private String artist;
  private int styleId;
  private String comment;
  private String style;
  
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public long getPlaylistId() {
	return playlistId;
}
public void setPlaylistId(long playlistId) {
	this.playlistId = playlistId;
}
public int getPosition() {
	return position;
}
public void setPosition(int position) {
	this.position = position;
}
public String getArtist() {
	return artist;
}
public void setArtist(String artist) {
	this.artist = artist;
}
public int getStyleId() {
	return styleId;
}
public void setStyleId(int styleId) {
	this.styleId = styleId;
}
public String getComment() {
	return comment;
}
public void setComment(String comment) {
	this.comment = comment;
}
public String getStyle() {
	return style;
}
public void setStyle(String style) {
	this.style = style;
}

}
