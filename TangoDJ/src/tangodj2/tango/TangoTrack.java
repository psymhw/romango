package tangodj2.tango;

import java.text.DecimalFormat;

import tangodj2.TrackDb;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TangoTrack
{
	private final SimpleStringProperty title;
  private final SimpleStringProperty artist;
  private final SimpleStringProperty leader;
  private final SimpleStringProperty album;
  private final SimpleStringProperty genre;
  private final SimpleStringProperty comment;
  private final SimpleStringProperty track_year;
  private final SimpleStringProperty pathHash;
  private final SimpleStringProperty path;
  private final SimpleStringProperty singer;
  private final SimpleStringProperty style;
  private final SimpleStringProperty rating;
  private final SimpleIntegerProperty duration;
  private final SimpleIntegerProperty track_no;
  private final SimpleStringProperty bpm;
  
  private int cortina=0;
    
  public TangoTrack(TrackDb trackDb)
  {
    this.title = new SimpleStringProperty(trackDb.title);
    this.artist = new SimpleStringProperty(trackDb.artist);
    this.leader = new SimpleStringProperty(trackDb.leader);
    this.album = new SimpleStringProperty(trackDb.album);
    this.genre = new SimpleStringProperty(trackDb.genre);
    this.comment = new SimpleStringProperty(trackDb.comment);
    this.track_year = new SimpleStringProperty(trackDb.track_year);
    this.pathHash = new SimpleStringProperty(trackDb.pathHash);
    this.path = new SimpleStringProperty(trackDb.path);
    this.singer = new SimpleStringProperty(trackDb.singer);
    this.style = new SimpleStringProperty(trackDb.style);
    this.rating = new SimpleStringProperty(trackDb.rating);
    this.bpm = new SimpleStringProperty(trackDb.bpm);
    this.duration = new SimpleIntegerProperty(trackDb.duration);
    this.track_no = new SimpleIntegerProperty(trackDb.track_no);
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
    
    public String getLeader() {
      return leader.get(); }
    
    public void setLeader(String leaderStr) {
      leader.set(leaderStr); }
    
    
    public String getBpm() {
      return bpm.get(); }
    
    public void setBpm(String bpmStr) {
      bpm.set(bpmStr); }
    
    public String getRating() {
      return rating.get(); }
    
    public void setRating(String ratingStr) {
      rating.set(ratingStr); }
    
    
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



  public int getTrack_no()
  {
    return track_no.get();
  }

  public void setTrack_no(int track_no_int)
  {
    track_no.set(track_no_int);
  }
  
}