package tangodj;

import tangodj.FontMeta;
import javafx.geometry.Bounds;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;



public class SongInfo 
{
  String originalTitle;
  String cleanTitle;
  String style;
  FontMeta carousel = new FontMeta("Carousel", FontWeight.BOLD);
  Font titleFont = Font.font(carousel.name, carousel.style, 70);
  
  public SongInfo(String originalTitle)
  {
	this.originalTitle=originalTitle;
	
	if (originalTitle.startsWith("CORTINA - ")) { cleanTitle=originalTitle.substring(10); return; } 
	if (originalTitle.startsWith("CORTINA-")) { cleanTitle=originalTitle.substring(9); return; } 
	
	
	int dashPosition = originalTitle.indexOf('-');
	
	if (dashPosition==-1) { cleanTitle=originalTitle; return; } 
	
	cleanTitle=originalTitle.substring(0, dashPosition);
	style = originalTitle.substring(dashPosition);
	style=style.toLowerCase();
	if (style.contains("milonga")) 
	{
      style="Milonga";
	}
	else if (style.contains("vals")) 
	{
	  style= "Vals";
	}
	else 
	{
		style="Tango";
	}
  }
  
  public Text getTitleText()
  {
	Text trialText = Artist.getDistantLight(cleanTitle, titleFont);
	Bounds bounds=trialText.getBoundsInLocal();
	
	if (bounds.getWidth()>1150)
	{
		titleFont = Font.font(carousel.name, carousel.style, 60);
		trialText = Artist.getDistantLight(cleanTitle, titleFont);
	}
	
	return trialText;
  }
  
  
  
}
