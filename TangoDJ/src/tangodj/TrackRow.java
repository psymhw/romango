package tangodj;

import java.util.concurrent.TimeUnit;

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
	
	
	
	public int getTandaNumber() {
		return tandaNumber;
	}

	public void setTandaNumber(int tandaNumber) {
		this.tandaNumber = tandaNumber;
	}

	public TrackRow clone(TrackRow tr)
	{
	   return new TrackRow(tr.getTrackTitle(), tr.getArtist(), tr.getPath(), tr.getGrouping(), tr.getTime(), tr.getTrackNumber());
	}
	
	

	public TrackRow(String trackTitle, String artist, String path, String grouping, int time, int trackNumber) 
	{
		
	  this.trackNumber=trackNumber;
	  this.trackTitle = trackTitle;
	  this.artist=artist;
	  this.grouping=grouping;
	  this.path=path;
	  this.time=time;
	  
		greenLightImage = new Image(TangoDJ.class.getResourceAsStream("/resources/images/green_light.png"));
		noLightImage = new Image(TangoDJ.class.getResourceAsStream("/resources/images/no_light.png"));
		greyLightImage = new Image(TangoDJ.class.getResourceAsStream("/resources/images/gray_light.png"));
		selectedArrowImage = new Image(TangoDJ.class.getResourceAsStream("/resources/images/selected_arrow.png"));
		
		String cssBkgColor = "tangoBkg";
		
		indicator.getChildren().add(selectedImage);
		indicator.getChildren().add(playingImage);
		
		
		//EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	    //      public void handle(MouseEvent event)  { setIndex(); }};
	          
			          
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
				//System.out.println("padding");
			  groupingLabel.setText("");
			}
		//	groupingLabel.setOnMouseClicked(bHandler);
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
		
		//this.artist=textLabel(iartist, 200, cssBkgColor);
		
		
		timeLabel=textLabel(getTimeStr(time), 100, cssBkgColor);
		
		this.trackNumberLabel=textLabel(""+(trackNumber+1)+") ", 50, cssBkgColor);
		trackNumberLabel.setAlignment(Pos.CENTER_RIGHT);
		
		//this.trackNumberLabel.setOnMouseClicked(bHandler);
		//this.trackTitleLabel.setOnMouseClicked(bHandler);
		//this.artistLabel.setOnMouseClicked(bHandler);
		
		//timeLabel.setOnMouseClicked(bHandler);
		//playingImage.setOnMouseClicked(bHandler);
	//	selectedImage.setOnMouseClicked(bHandler);
		playingImage.setImage(noLightImage);
		
	}
	

	private void setIndex()
	{
		//System.out.println("TrackRow: "+index);
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

	public void setGrouping(String grouping) {
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

	public int getTandaTrackIndex() {
		return tandaTrackIndex;
	}
}

