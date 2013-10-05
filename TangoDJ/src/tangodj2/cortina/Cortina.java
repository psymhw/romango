package tangodj2.cortina;

import javafx.beans.property.SimpleStringProperty;

public class Cortina
{
  public int id;
  public int start;
  public int stop;
  public int fadein;
  public int fadeout;
  public int track_count;
  public int delay;
  public String comment;
  public String title;
  public String hash;
  
  private final SimpleStringProperty tableTitle = new SimpleStringProperty();;
  
  public Cortina()
  {
    
  }

  public String getTableTitle()
  {
    return tableTitle.get();
  }
  
  public void setTableTitle(String title)
  {
    tableTitle.set(title);
  }
  
}
