package notes;

import org.apache.struts.action.ActionForm;

public class NotesSelectForm extends ActionForm
{
  /**
   * 
   */
  private static final long serialVersionUID = -669248678548831639L;
  private String track;
  private String mode="list";
  private String filter;

  public String getFilter()
  {
    return filter;
  }

  public void setFilter(String filter)
  {
    this.filter = filter;
  }

  public String getMode()
  {
    return mode;
  }

  public void setMode(String mode)
  {
    this.mode = mode;
  }

  public String getTrack()
  {
    return track;
  }

  public void setTrack(String track)
  {
    this.track = track;
  }
}
