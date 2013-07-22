package tangodj;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.FrameBodyTIT1;
import org.farng.mp3.id3.ID3v2_3Frame;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.LinearGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

public class TrackRow 
{
	private String artist;
	private String path; 
	private String grouping;
	private String trackTitle;
	private int trackNumber=0;
	private int time;
	
	private Label trackTitleLabel; 
	private Label artistLabel; 
	private Label groupingLabel; 
	private Label  timeLabel;
	private Label trackNumberLabel;
	
	
	private static int pIndex=0;
	private ImageView playingImage = new ImageView();
	private ImageView selectedImage = new ImageView();
	private Image greenLightImage;
	private Image noLightImage;
	private Image greyLightImage;
	private Image selectedArrowImage;
	private int fontSize=18;
	private static String lastGrouping="";
	private static String lastArtist="";
	private boolean groupingVisible=true;
	private boolean playing=false;
	private boolean selected=false;
	private StackPane indicator = new StackPane();
	private int tandaNumber=0;
	private int tandaTrackIndex=0;
	String cssBkgColor = "tangoBkg";
	private MP3File mp3;
	
	public int getTandaNumber() {
		return tandaNumber;
	}

	public void setTandaNumber(int tandaNumber) {
		this.tandaNumber = tandaNumber;
	}

	public TrackRow clone(TrackRow tr)
	{
	   return new TrackRow(tr.getMp3(), tr.getTrackTitle(), tr.getArtist(), tr.getPath(), tr.getGrouping(), tr.getTime(), tr.getTrackNumber());
	}
	
	

	public TrackRow(MP3File mp3, String trackTitle, String artist, String path, String grouping, int time, int trackNumber) 
	{
		
	  this.trackNumber=trackNumber;
	  this.trackTitle = trackTitle;
	  this.artist=artist;
	  this.grouping=grouping;
	  this.path=path;
	  this.time=time;
	  this.mp3=mp3;
	  
	  
	  
	  
	  greenLightImage = new Image(TangoDJ.class.getResourceAsStream("/resources/images/green_light.png"));
	  noLightImage = new Image(TangoDJ.class.getResourceAsStream("/resources/images/no_light.png"));
	  greyLightImage = new Image(TangoDJ.class.getResourceAsStream("/resources/images/gray_light.png"));
	  selectedArrowImage = new Image(TangoDJ.class.getResourceAsStream("/resources/images/selected_arrow.png"));
		
	  
	  indicator.getChildren().add(selectedImage);
	  indicator.getChildren().add(playingImage);
			          
	  if (grouping!=null)
	  {
		if (grouping.toLowerCase().equals("vals"))    cssBkgColor = "valsBkg";
		else if (grouping.toLowerCase().equals("milonga")) cssBkgColor = "milongaBkg";
		else if (grouping.toLowerCase().equals("cortina")) cssBkgColor = "cortinaBkg";
		else if (grouping.toLowerCase().equals("padding")) cssBkgColor = "paddingBkg";
		else if (grouping.toLowerCase().equals("alternative")) cssBkgColor = "alternativeBkg";
		else cssBkgColor = "tangoBkg";
		  
		  
		if (!lastGrouping.equals(grouping))
		{
		  groupingLabel=textLabel(grouping, 100, cssBkgColor);
		  lastGrouping=grouping;
		}
		else groupingLabel=textLabel("", 100, cssBkgColor);
			
		if (grouping.toLowerCase().equals("padding")) 
		{	
		  groupingLabel.setText("");
		}
	  }
	  else 
	  {	
		groupingLabel=textLabel("", 100, cssBkgColor);
		grouping=lastGrouping;
	  }	
		
	  if (grouping==null) grouping="General";
		
	  this.trackTitleLabel=textLabel(trackTitle, 200, cssBkgColor);
		
	  // holds the name even though it might be surpressed in the table view.
	  if (!lastArtist.equals(artist))
	  {
		this.artistLabel=textLabel(artist, 200, cssBkgColor);
		lastArtist=artist;
	  }
	  else this.artistLabel=textLabel("", 200, cssBkgColor);
		
	  timeLabel=textLabel(getTimeStr(time), 100, cssBkgColor);
	  this.trackNumberLabel=textLabel(""+(trackNumber+1)+") ", 50, cssBkgColor);
	  trackNumberLabel.setAlignment(Pos.CENTER_RIGHT);
	  playingImage.setImage(noLightImage);
	}
	

	private void setIndex()
	{
	  pIndex=trackNumber;
	}
	
	public static int getPindex()
	{
	  return pIndex;	
	}
	
	private Label textLabel(String text, int width, String cssBkgColor)
	{
	   Label label = new Label(" "+text);
	   label.setPrefWidth(width);
	   label.getStyleClass().add(cssBkgColor);
	   label.setFont(new Font("Cambria", fontSize));
	  // label.setStyle("tangoBkg");
	   
	   return label;
	}
	
	public String getTimeStr(int time) {
    	return String.format("%d:%d", 
    		    TimeUnit.MILLISECONDS.toMinutes(time),
    		    TimeUnit.MILLISECONDS.toSeconds(time) - 
    		    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
    		);
        //return time.get();
    }

	public String getArtist() {
		return artist;
	}
	
	public void setTandaInfo(int tandaNumber, int tandaTrackIndex, int trackNumber)
	{
		this.tandaNumber=tandaNumber;
		this.tandaTrackIndex=tandaTrackIndex;
		this.trackNumber=trackNumber;
		this.trackNumberLabel.setText(""+(trackNumber+1)+") ");
		trackNumberLabel.setAlignment(Pos.CENTER_RIGHT);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTrackTitle() {
		return trackTitle;
	}

	public void setTrackTitle(String trackTitle) {
		this.trackTitle = trackTitle;
	}

	public StackPane getIndicator() {
		return indicator;
	}

	public void setIndicator(StackPane indicator) {
		this.indicator = indicator;
	}

	public Label getTrackTitleLabel() {
		return trackTitleLabel;
	}

	public void setTrackTitleLabel(Label trackTitleLabel) {
		this.trackTitleLabel = trackTitleLabel;
	}

	public Label getGroupingLabel() {
		return groupingLabel;
	}

	public void setGroupingLabel(Label groupingLabel) {
		this.groupingLabel = groupingLabel;
	}
	
	public void setGroupingLabel(String grouping) {
		this.groupingLabel.setText(grouping);
	}

	public Label getTrackNumberLabel() {
		return trackNumberLabel;
	}

	public void setTrackNumberLabel(Label trackNumberLabel) {
		this.trackNumberLabel = trackNumberLabel;
	}

	public Label getArtistLabel() {
		return artistLabel;
	}

	public void setArtistLabel(Label artistLabel) {
		this.artistLabel = artistLabel;
	}

	public String getGrouping() {
		return grouping;
	}

	public void setGrouping(String grouping) 
	{
		this.grouping = grouping;
	}

	public int getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) 
	{
		this.playing = playing;
		if (playing) playingImage.setImage(greenLightImage);
		else
		playingImage.setImage(noLightImage);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) 
	{
		this.selected = selected;
		if (selected) selectedImage.setImage(selectedArrowImage);
		else
		selectedImage.setImage(noLightImage);
	}
	
	public void setArtist(String artist)
	{
	  this.artist=artist;
	  artistLabel.setText(artist);
	}
	
	public void setBackgroundColor(String grouping)
	{
		if (grouping.toLowerCase().equals("vals"))    cssBkgColor = "valsBkg";
		else if (grouping.toLowerCase().equals("milonga")) cssBkgColor = "milongaBkg";
		else if (grouping.toLowerCase().equals("cortina")) cssBkgColor = "cortinaBkg";
		else if (grouping.toLowerCase().equals("padding")) cssBkgColor = "paddingBkg";
		else if (grouping.toLowerCase().equals("alternative")) cssBkgColor = "alternativeBkg";
		else cssBkgColor = "tangoBkg";
		
		artistLabel.getStyleClass().add(cssBkgColor);
		groupingLabel.getStyleClass().add(cssBkgColor);
		trackTitleLabel.getStyleClass().add(cssBkgColor);
		trackNumberLabel.getStyleClass().add(cssBkgColor);
	}
	

	public int getTandaTrackIndex() {
		return tandaTrackIndex;
	}

	public MP3File getMp3() {
		return mp3;
	}

	public void setMp3(MP3File mp3) {
		this.mp3 = mp3;
	}
	
	boolean debug=true;
	public String getGroupingFromMp3()
	{
	  ID3v2_3Frame ivf=null;
	  FrameBodyTIT1 grouping;
	  
	  try 
	  {
		ivf = (ID3v2_3Frame)mp3.getID3v2Tag().getFrame("TIT1");
		grouping = (FrameBodyTIT1)ivf.getBody();
		if (debug) System.out.println("Got grouping for "+mp3.getID3v2Tag().getSongTitle());
	    return grouping.getText();
	  } catch (Exception e)  
	  { 	
		  if (debug) System.out.println("grouping not found for "+mp3.getID3v2Tag().getSongTitle());
		  return "no grouping";	
		  }
	}
	
	public void setGroupingInMp3(String style)
	{
	  String currentGrouping = getGroupingFromMp3();
	  FrameBodyTIT1 grouping;
	  
	  if ("no grouping".equals(currentGrouping))
	  {
		try 
		{
	      FrameBodyTIT1 newTiT1body = new FrameBodyTIT1();
	      newTiT1body.setText(style);
	      newTiT1body.setTextEncoding((byte)0);
	      ID3v2_3Frame newFrame = new ID3v2_3Frame(newTiT1body);
	      mp3.getID3v2Tag().setFrame(newFrame);
		  mp3.save();
		  if (debug) System.out.println("added new grouping for "+mp3.getID3v2Tag().getSongTitle());
		} catch (Exception e) { e.printStackTrace(); }
	  }
	  else
	  {
		try 
		{
		  ID3v2_3Frame ivf = (ID3v2_3Frame)mp3.getID3v2Tag().getFrame("TIT1");
		  grouping = (FrameBodyTIT1)ivf.getBody();
		  grouping.setText("Tango");
		  mp3.getID3v2Tag().getFrame("TIT1").setBody(grouping);
		  mp3.save();
		  if (debug) System.out.println("updated grouping for "+mp3.getID3v2Tag().getSongTitle());
		} catch (Exception e) { e.printStackTrace(); }
	  }

	}
}

