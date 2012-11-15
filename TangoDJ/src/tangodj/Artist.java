package tangodj;

import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Artist 
{
  public String firstName;
  public String lastName;
  FontMeta fontMeta;
  int lastNameSize;
  Font lastNameFont;
  Font firstNameFont;
  
  public Artist(String firstName, String lastName, FontMeta fontMeta, int lastNameSize)
  {
	  this.firstName=firstName;
	  this.lastName=lastName;
	  this.fontMeta=fontMeta;
	  this.lastNameSize=lastNameSize;
	  lastNameFont =  Font.font(fontMeta.name, fontMeta.style, lastNameSize);
	  firstNameFont =  Font.font(fontMeta.name, fontMeta.style, 70);
  }
  
  public Text getLastNameText()
  {
	return getDistantLight(lastName, lastNameFont);
  }
  
  public Text getFirstNameText()
  {
	return getDistantLight(firstName, firstNameFont);
  }
  
  public static Text getDistantLight(String inStr, Font f)
	{
	  Light.Distant light = new Light.Distant();
	  light.setAzimuth(-135.0f);
	  light.setElevation(30.0f);

	  Lighting l = new Lighting();
	  l.setLight(light);
	  l.setSurfaceScale(5.0f);

	  final Text t = new Text();
	  t.setText(inStr);
	  t.setFill(Color.RED);
	  t.setFont(f);
	      

	  t.setEffect(l);
	
	  return t;
	}
}
