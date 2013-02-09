package feet;

import db.FeetDb;

public class FeetData extends FeetDb
{
  private String lastname;
  private String firstname;


public String getLastname(){
  return this.lastname;
}

public void setLastname( String param ){
  this.lastname= param;
}
public String getFirstname(){
  return this.firstname;
}

public void setFirstname( String param ){
  this.firstname= param;
}

}