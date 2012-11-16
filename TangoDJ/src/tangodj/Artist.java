package tangodj;

import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Artist 
{
  public String firstName;
  public String lastName;
  FontMeta fontMeta;
  int lastNameSize;
  Font lastNameFont;
  Font firstNameFont;
	static FontMeta carousel = new FontMeta("Carousel", FontWeight.BOLD);
	static FontMeta anagram = new FontMeta("Anagram", FontWeight.NORMAL);
	static FontMeta carrington = new FontMeta("Carrington", FontWeight.BOLD);
	static FontMeta deftone = new FontMeta("Deftone Stylus", FontWeight.BOLD);
	static FontMeta eastMarket = new FontMeta("EastMarket", FontWeight.BOLD);
	static FontMeta englandHand = new FontMeta("England Hand DB", FontWeight.BOLD);
	static FontMeta tusj = new FontMeta("FFF Tusj", FontWeight.BOLD);
	
  
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
  
  public static Artist getArtist(String inStr)
	{
	  if (inStr.toLowerCase().contains("cortina"))     return new Artist("", "CORTINA", carousel, 250);
	  if (inStr.toLowerCase().contains("biagi"))       return new Artist("Rudolfo", "Biagi", carousel, 300);
	  if (inStr.toLowerCase().contains("calo"))        return new Artist("Miguel", "Calo",anagram, 300);
	  if (inStr.toLowerCase().contains("canaro"))      return new Artist("Francisco", "Canaro", carrington, 300);
	  if (inStr.toLowerCase().contains("rodriguez"))   return new Artist("Enrique", "Rodriguez", deftone, 300);
	  if (inStr.toLowerCase().contains("d'arienzo"))   return new Artist("Juan", "D'Arienzo", eastMarket, 250);
	  if (inStr.toLowerCase().contains("angelise")) return new Artist("Alfredo", "De Angelise", englandHand, 250);
	  if (inStr.toLowerCase().contains("di sarli"))    return new Artist("Carlos", "Di Sarli", tusj, 300);
	  
	  
	  return new Artist("", inStr, carousel, 200);
	}
}
