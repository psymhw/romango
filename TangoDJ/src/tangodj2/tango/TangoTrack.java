package tangodj2.tango;

import java.text.DecimalFormat;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TangoTrack
{
	private final SimpleStringProperty title;
  private final SimpleStringProperty artist;
  private final SimpleStringProperty album;
  private final SimpleStringProperty genre;
  private final SimpleStringProperty comment;
  private final SimpleStringProperty track_year;
  private final SimpleStringProperty pathHash;
  private final SimpleStringProperty path;
  private final SimpleStringProperty singer;
  private final SimpleStringProperty style;
  private final SimpleIntegerProperty duration;
  
  private int cortina=0;
    
    
	public TangoTrack(String titleStr, String artistStr, String albumStr, String genreStr, String commentStr, String pathHashStr, String path, int duration, int cortina, String track_yearStr, String singer, String style)
  {
    this.title = new SimpleStringProperty(titleStr);
    this.artist = new SimpleStringProperty(artistStr);
    this.album = new SimpleStringProperty(albumStr);
    this.genre = new SimpleStringProperty(genreStr);
    this.comment = new SimpleStringProperty(commentStr);
    this.track_year = new SimpleStringProperty(track_yearStr);
    this.pathHash = new SimpleStringProperty(pathHashStr);
    this.path = new SimpleStringProperty(path);
    this.singer = new SimpleStringProperty(singer);
    this.style = new SimpleStringProperty(style);
    this.duration = new SimpleIntegerProperty(duration);
    this.cortina=cortina;
  }
    
    public String getPathHash() {
    	return pathHash.get(); }
    
    public void setPathHash(String pathHashStr) {
    	pathHash.set(pathHashStr); }
    
    public String getComment() {
    	return comment.get(); }
    
    public void setComment(String commentStr) {
    	comment.set(commentStr); }
    
    public String getTrack_year() {
    	return track_year.get(); }
    
    public void setTrack_year(String yearStr) {
    	track_year.set(yearStr); }
    
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
    
    public int getCortina() {
		return cortina;
	}

	public void setCortina(int cortina) {
		this.cortina = cortina;
	}

    
  static String formatIntoMMSS(double secsIn)
	{
    secsIn=secsIn/1000;
	  int hours = (int)secsIn / 3600,
	  remainder = (int)secsIn % 3600,
	  minutes = remainder / 60,
	  seconds = remainder % 60;
	  DecimalFormat sec = new DecimalFormat( "00" );
	DecimalFormat min = new DecimalFormat( "##" );
	//return ( (minutes < 10 ? "0" : "") + minutes
	//+ ":" + (seconds< 10 ? "0" : "") + seconds );
	
	return min.format(minutes)+":"+sec.format(seconds);

	}

  public String getSinger()
  {
    return singer.get();
  }
  
  public void setSinger(String singer)
  {
    this.singer.set(singer);
  }
  
  public String getStyle()
  {
    return style.get();
  }
  
  public void setStyle(String style)
  {
    this.style.set(style);
  }

}