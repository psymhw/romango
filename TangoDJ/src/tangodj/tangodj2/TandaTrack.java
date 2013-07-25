package tangodj.tangodj2;

import javafx.beans.property.SimpleStringProperty;

public class TandaTrack 
{
  int order=0;
  private final SimpleStringProperty title;  // should be trackHash later
  
  public TandaTrack(String titleStr)
  {
	 this.title = new SimpleStringProperty(titleStr);
	 
  }
  
  public String getTitle() {
  	return title.get(); }
  
  public void setTitle(String titleStr) {
  	title.set(titleStr); }
}
