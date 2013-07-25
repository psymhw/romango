package tangodj.tangodj2;

import javafx.beans.property.SimpleStringProperty;

public class Track
{
	private final SimpleStringProperty title;
    private final SimpleStringProperty artist;
    private final SimpleStringProperty album;
    private final SimpleStringProperty genre;
    private final SimpleStringProperty comment;
    private final SimpleStringProperty pathHash;
    
    public Track(String titleStr, String artistStr, String albumStr, String genreStr, String commentStr, String pathHashStr)
    {
      this.title = new SimpleStringProperty(titleStr);
      this.artist = new SimpleStringProperty(artistStr);
      this.album = new SimpleStringProperty(albumStr);
      this.genre = new SimpleStringProperty(genreStr);
      this.comment = new SimpleStringProperty(commentStr);
      this.pathHash = new SimpleStringProperty(pathHashStr);
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
    

}