package mileage;

import org.apache.struts.action.ActionForm;

public class MileageSelectForm extends ActionForm
{
 
  private static final long serialVersionUID = 1302475547154131663L;
  
  private String track;
  private String mode="list";

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
