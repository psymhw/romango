package model;

import org.apache.struts.upload.FormFile;

public class ImageUploadForm 
{
  private static final long serialVersionUID = 1613957718159818702L;
  private FormFile fileForm;
  private String description;
  private long fileSize;
  
  private int id;
 
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
 
  public long getFileSize()
  {
    return fileSize;
  }
  public void setFileSize(long fileSize)
  {
    this.fileSize = fileSize;
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
 
}
