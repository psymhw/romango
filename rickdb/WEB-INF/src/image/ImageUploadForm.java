package image;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class ImageUploadForm extends ActionForm
{
  private static final long serialVersionUID = 1613957718159818702L;
  private FormFile fileForm;
  private String description;
  private String notes;
  private long cust_id;
  private long feet_id;
  private long fileSize;
  private long note_id;
  private String mode;
  private String type;
  
  public String getType()
  {
    return type;
  }
  public void setType(String type)
  {
    this.type = type;
  }
  public String getMode()
  {
    return mode;
  }
  public void setMode(String mode)
  {
    this.mode = mode;
  }
  public long getNote_id()
  {
    return note_id;
  }
  public void setNote_id(long note_id)
  {
    this.note_id = note_id;
  }
  public long getFileSize()
  {
    return fileSize;
  }
  public void setFileSize(long fileSize)
  {
    this.fileSize = fileSize;
  }
  public long getCust_id()
  {
    return cust_id;
  }
  public void setCust_id(long cust_id)
  {
    this.cust_id = cust_id;
  }
  public long getFeet_id()
  {
    return feet_id;
  }
  public void setFeet_id(long feet_id)
  {
    this.feet_id = feet_id;
  }
  public String getDescription()
  {
    return description;
  }
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  public FormFile getFileForm()
  {
    return fileForm;
  }
  public void setFileForm(FormFile fileForm)
  {
    this.fileForm = fileForm;
  }
  public String getNotes()
  {
    return notes;
  }
  public void setNotes(String notes)
  {
    this.notes = notes;
  }
}
