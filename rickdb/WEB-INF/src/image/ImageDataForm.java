package image;

import org.apache.struts.action.ActionForm;

public class ImageDataForm extends ActionForm
{
  /**
   * 
   */
  private static final long serialVersionUID = -4017507976163717937L;
  private long id;
  private long cust_id;
  private String description;
  private String path;
  private String filename;
  private long feet_id;
  private String notes;   
  private long file_size;
  private String mode;
  private long note_id;


public long getNote_id()
  {
    return note_id;
  }

  public void setNote_id(long note_id)
  {
    this.note_id = note_id;
  }

public long getId(){
   return this.id;
}

public void setId( long param ){
   this.id= param;
}
public long getCust_id(){
   return this.cust_id;
}

public void setCust_id( long param ){
   this.cust_id= param;
}
public String getDescription(){
   return this.description;
}

public void setDescription( String param ){
   this.description= param;
}
public String getPath(){
   return this.path;
}

public void setPath( String param ){
   this.path= param;
}
public String getFilename(){
   return this.filename;
}

public void setFilename( String param ){
   this.filename= param;
}
public long getFeet_id(){
   return this.feet_id;
}

public void setFeet_id( long param ){
   this.feet_id= param;
}
public String getNotes(){
   return this.notes;
}

public void setNotes( String param ){
   this.notes= param;
}

public String toString(){
       return  " [id] " + id + " [cust_id] " + cust_id + " [description] " + description + " [path] " + path + " [filename] " + filename + " [feet_id] " + feet_id + " [notes] " + notes;
}

public long getFile_size()
{
  return file_size;
}

public void setFile_size(long file_size)
{
  this.file_size = file_size;
}

public String getMode()
{
  return mode;
}

public void setMode(String mode)
{
  this.mode = mode;
}

}
