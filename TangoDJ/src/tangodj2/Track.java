package tangodj2;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Track
{
	private final SimpleStringProperty title;
    private final SimpleStringProperty artist;
    private final SimpleStringProperty album;
    private final SimpleStringProperty genre;
    private final SimpleStringProperty comment;
    private final SimpleStringProperty pathHash;
    private final SimpleStringProperty path;
    private final SimpleIntegerProperty duration;
    
    public Track(String titleStr, String artistStr, String albumStr, String genreStr, String commentStr, String pathHashStr, String path, int duration)
    {
      this.title = new SimpleStringProperty(titleStr);
      this.artist = new SimpleStringProperty(artistStr);
      this.album = new SimpleStringProperty(albumStr);
      this.genre = new SimpleStringProperty(genreStr);
      this.comment = new SimpleStringProperty(commentStr);
      this.pathHash = new SimpleStringProperty(pathHashStr);
      this.path = new SimpleStringProperty(path);
      this.duration = new SimpleIntegerProperty(duration);
    }
    
    public String getPathHash() {
    	return pathHash.get(); }
    
    public void setPathHash(String pathHashStr) {
    	pathHash.set(pathHashStr); }
    
    public String getComment() {
    	return comment.get(); }
    
    public void setComment(String commentStr) {
    	comment.set(commentStr); }
    
    public String getGenre() {
    	return genre.get(); }
    
    public void setGenre(String genreStr) {
    	genre.set(genreStr); }
    
    public String getTitle() {
    	return title.get(); }
    
    public void setTitle(String titleStr) {
    	title.set(titleStr); }
    
    public String getArtist() {
    	return artist.get(); }
    
    public void setArtist(String artistStr) {
    	artist.set(artistStr); }
    
    public String getAlbum() {
    	return album.get(); }
    
    public void setAlbum(String albumStr) {
    	album.set(albumStr); }
    
    public String getDuration()
    {
    	return formatIntoMMSS(duration.get());
    }
    
    static String formatIntoMMSS(double secsIn)
	{

	int hours = (int)secsIn / 3600,
	remainder = (int)secsIn % 3600,
	minutes = remainder / 60,
	seconds = remainder % 60;

	return ( (minutes < 10 ? "0" : "") + minutes
	+ ":" + (seconds< 10 ? "0" : "") + seconds );

	}

}