package tangodj;

import java.util.concurrent.TimeUnit;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class TrackRow 
{
	
	Label name; 
	Label artist; 
	String path; 
	Label grouping; 
	Label  time;
	Label index;
	int idx=0;
	
	static int pIndex=0;
	ImageView nowPlaying = new ImageView();
	Image greenLightImage;
	Image noLightImage;
	int fontSize=18;
	static String lastGrouping="";
	static String lastArtist="";
	boolean groupingVisible=true;
	
	public TrackRow(String iname, String iartist, String ipath, String igrouping, int  itime, int iindex)
	{
		greenLightImage = new Image(TangoDJ.class.getResourceAsStream("/resources/images/green_light.png"));
		noLightImage = new Image(TangoDJ.class.getResourceAsStream("/resources/images/no_light.png"));
		String cssBkgColor = "tangoBkg";
		
		EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event)  { setIndex();  }};
		
		if (igrouping!=null)
		{
		  if (igrouping.toLowerCase().equals("vals")) cssBkgColor = "valsBkg";
		  else if (igrouping.toLowerCase().equals("milonga")) cssBkgColor = "milongaBkg";
		  else if (igrouping.toLowerCase().equals("cortina")) cssBkgColor = "cortinaBkg";
		  else if (igrouping.toLowerCase().equals("padding")) cssBkgColor = "paddingBkg";
		  
		  if (!lastGrouping.equals(igrouping))
			{
			  this.grouping=textLabel(igrouping, 100, cssBkgColor);
			  lastGrouping=igrouping;
			}
			else this.grouping=textLabel("", 100, cssBkgColor);
			
			if (igrouping.toLowerCase().equals("padding")) 
			{	
				//System.out.println("padding");
			  this.grouping.setText("");
			}
			this.grouping.setOnMouseClicked(bHandler);
		}
		else this.grouping=textLabel("", 100, cssBkgColor);
			
		
		this.name=textLabel(iname, 200, cssBkgColor);
		
		if (!lastArtist.equals(iartist))
		{
		  this.artist=textLabel(iartist, 200, cssBkgColor);
		  lastArtist=iartist;
		}
		else this.artist=textLabel("", 200, cssBkgColor);
		
		//this.artist=textLabel(iartist, 200, cssBkgColor);
		this.path=ipath;
		
		
		
		
		
		this.time=textLabel(getTime(itime), 100, cssBkgColor);
		idx=iindex;
		this.index=textLabel(""+(iindex+1)+") ", 50, cssBkgColor);
		index.setAlignment(Pos.CENTER_RIGHT);
		
		this.name.setOnMouseClicked(bHandler);
		this.artist.setOnMouseClicked(bHandler);
		
		this.time.setOnMouseClicked(bHandler);
		nowPlaying.setOnMouseClicked(bHandler);
		nowPlaying.setImage(noLightImage);
		
	}
	
	public void setNowPlaying()
	{
		nowPlaying.setImage(greenLightImage);
	}
	
	public void setNotPlaying()
	{
		nowPlaying.setImage(noLightImage);
	}
		
	private void setIndex()
	{
		//System.out.println("TrackRow: "+index);
		pIndex=idx;
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
	
	public String getTime(int time) {
    	return String.format("%d:%d", 
    		    TimeUnit.MILLISECONDS.toMinutes(time),
    		    TimeUnit.MILLISECONDS.toSeconds(time) - 
    		    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
    		);
        //return time.get();
    }
	
}

