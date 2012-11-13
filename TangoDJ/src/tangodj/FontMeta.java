package tangodj;

import javafx.scene.text.FontWeight;

public class FontMeta 
{
  public String name;
  public FontWeight style;
  public int ratio;  // 0 - 10   10 for fonts that take more room per character, so need font size adjusted down.
  
  public FontMeta(String name, FontWeight style)
  {
	this.name=name;
	this.style=style;
  }
}
