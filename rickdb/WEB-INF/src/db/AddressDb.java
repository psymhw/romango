package db;

public class AddressDb
{
  private long memberId;
  private long id;
  private String lastname;
  private String firstname;
  private String street1;
  private String street2;
  private String csz;
  private String type;
  private String home_phone;
  private String work_phone;
  private String cell_phone;
  private String notes;
  private String email; 
  private String company;
  private int personal;
  private int business;
  private int provider;
  private int customer;
  private int family;
  private int feet;
  private java.sql.Timestamp last_touch;
  private String website;


public String getWebsite()
  {
    return website;
  }

  public void setWebsite(String website)
  {
    this.website = website;
  }

public java.sql.Timestamp getLast_touch()
  {
    return last_touch;
  }

  public void setLast_touch(java.sql.Timestamp last_touch)
  {
    this.last_touch = last_touch;
  }

public long getId(){
   return this.id;
}

public void setId( long param ){
   this.id= param;
}
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
public String getStreet1(){
   return this.street1;
}

public void setStreet1( String param ){
   this.street1= param;
}
public String getStreet2(){
   return this.street2;
}

public void setStreet2( String param ){
   this.street2= param;
}
public String getCsz(){
   return this.csz;
}

public void setCsz( String param ){
   this.csz= param;
}
public String getType(){
   return this.type;
}

public void setType( String param ){
   this.type= param;
}
public String getHome_phone(){
   return this.home_phone;
}

public void setHome_phone( String param ){
   this.home_phone= param;
}
public String getWork_phone(){
   return this.work_phone;
}

public void setWork_phone( String param ){
   this.work_phone= param;
}
public String getCell_phone(){
   return this.cell_phone;
}

public void setCell_phone( String param ){
   this.cell_phone= param;
}
public String getNotes(){
   return this.notes;
}

public void setNotes( String param ){
   this.notes= param;
}
public String getEmail(){
   return this.email;
}

public void setEmail( String param ){
   this.email= param;
}

public String toString(){
       return  " [id] " + id + " [lastname] " + lastname + " [firstname] " + firstname + " [street1] " + street1 + " [street2] " + street2 + " [csz] " + csz + " [type] " + type + " [home_phone] " + home_phone + " [work_phone] " + work_phone + " [cell_phone] " + cell_phone + " [notes] " + notes + " [email] " + email;
}

public String getCompany()
{
  return company;
}

public void setCompany(String company)
{
  this.company = company;
}

public int getBusiness()
{
  return business;
}

public void setBusiness(int business)
{
  this.business = business;
}

public int getCustomer()
{
  return customer;
}

public void setCustomer(int customer)
{
  this.customer = customer;
}

public int getFamily()
{
  return family;
}

public void setFamily(int family)
{
  this.family = family;
}

public int getFeet()
{
  return feet;
}

public void setFeet(int feet)
{
  this.feet = feet;
}

public int getPersonal()
{
  return personal;
}

public void setPersonal(int personal)
{
  this.personal = personal;
}

public int getProvider()
{
  return provider;
}

public void setProvider(int provider)
{
  this.provider = provider;
}

public long getMemberId()
{
  return memberId;
}

public void setMemberId(long memberId)
{
  this.memberId = memberId;
}



}
