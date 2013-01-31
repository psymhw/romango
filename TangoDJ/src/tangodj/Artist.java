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
	  int MAX=300;
	  int MEDIUM=250;
	  int SMALL=200;
	  	 	  
	  if (inStr.toLowerCase().contains("cortina"))     return new Artist("",          "CORTINA",               carousel, 250);
	  if (inStr.toLowerCase().contains("biagi"))       return new Artist("Rudolfo",   "Biagi",                 tusj, MAX);
	  if (inStr.toLowerCase().contains("calo"))        return new Artist("Miguel",    "Calo",                  tusj, MAX);
	  if (inStr.toLowerCase().contains("canaro"))      return new Artist("Francisco", "Canaro",                tusj, MAX);
	  if (inStr.toLowerCase().contains("rodriguez"))   return new Artist("Enrique",   "Rodriguez",             tusj, MAX);
	  if (inStr.toLowerCase().contains("d'arienzo"))   return new Artist("Juan",      "D'Arienzo",             tusj, SMALL);
	  if (inStr.toLowerCase().contains("angelise"))    return new Artist("Alfredo",   "De Angelise",           tusj, MEDIUM);
	  if (inStr.toLowerCase().contains("di sarli"))    return new Artist("Carlos",    "Di Sarli",              tusj, MAX);
	  if (inStr.toLowerCase().contains("firpo"))       return new Artist("Roberto",   "Firpo",                 tusj, MAX);
	  if (inStr.toLowerCase().contains("tipica"))      return new Artist("",          "Orquesta Tipica Victor",tusj, SMALL);
	  if (inStr.toLowerCase().contains("ortiz"))       return new Artist("Ciriaco",   "Ortiz",                 tusj, MAX);
	  if (inStr.toLowerCase().contains("caro"))        return new Artist("Julio",     "De Caro",               tusj, MAX);	  
	  if (inStr.toLowerCase().contains("donato"))      return new Artist("Edgardo",   "Donato",                tusj, MAX);
	  if (inStr.toLowerCase().contains("castillo"))    return new Artist("Alberto",   "Castillo",              tusj, MEDIUM);
	  if (inStr.toLowerCase().contains("lomuto"))      return new Artist("Francisco", "Lomuto",                tusj, MEDIUM);
	  if (inStr.toLowerCase().contains("d'agostino"))  return new Artist("Angel",     "D'Agostino",            tusj, SMALL);
	  if (inStr.toLowerCase().contains("tanturi"))     return new Artist("Ricardo",     "Tanturi",            tusj, MEDIUM);
	  if (inStr.toLowerCase().contains("pugliese"))    return new Artist("Osvaldo",     "Pugliese",            tusj, MEDIUM);

	
	  
	  return new Artist("", inStr, deftone, 200);
	}
}
