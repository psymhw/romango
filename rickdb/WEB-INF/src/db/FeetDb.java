package db;

public class FeetDb
{
  private long memberId;
  private int id;
  private int rt_length;
  private int rt_bb_width;
  private int rt_b2b;
  private int rt_bb_girth;
  private int rt_waist_girth;
  private int rt_instep_girth;
  private int rt_heel_width;
  private int rt_short_heel;
  private int rt_ankle_height;
  private int stylus_allowance;
  private String cust_dir;
  private String shoe_size_1;
  private String shoe_size_2;
  private String notes;
  private int cust_id;
  private java.sql.Timestamp measure_date;
  private String units;
  private int lf_length;
  private int lf_bb_width;
  private int lf_b2b;
  private int lf_bb_girth;
  private int lf_waist_girth;
  private int lf_instep_girth;
  private int lf_heel_width;
  private int lf_short_heel;
  private int lf_ankle_height;   
  




public int getId(){
   return this.id;
}

public void setId( int param ){
   this.id= param;
}
public int getRt_length(){
   return this.rt_length;
}

public void setRt_length( int param ){
   this.rt_length= param;
}
public int getRt_bb_width(){
   return this.rt_bb_width;
}

public void setRt_bb_width( int param ){
   this.rt_bb_width= param;
}
public int getRt_b2b(){
   return this.rt_b2b;
}

public void setRt_b2b( int param ){
   this.rt_b2b= param;
}
public int getRt_bb_girth(){
   return this.rt_bb_girth;
}

public void setRt_bb_girth( int param ){
   this.rt_bb_girth= param;
}
public int getRt_waist_girth(){
   return this.rt_waist_girth;
}

public void setRt_waist_girth( int param ){
   this.rt_waist_girth= param;
}
public int getRt_instep_girth(){
   return this.rt_instep_girth;
}

public void setRt_instep_girth( int param ){
   this.rt_instep_girth= param;
}
public int getRt_heel_width(){
   return this.rt_heel_width;
}

public void setRt_heel_width( int param ){
   this.rt_heel_width= param;
}
public int getRt_short_heel(){
   return this.rt_short_heel;
}

public void setRt_short_heel( int param ){
   this.rt_short_heel= param;
}
public int getRt_ankle_height(){
   return this.rt_ankle_height;
}

public void setRt_ankle_height( int param ){
   this.rt_ankle_height= param;
}
public int getStylus_allowance(){
   return this.stylus_allowance;
}

public void setStylus_allowance( int param ){
   this.stylus_allowance= param;
}
public String getCust_dir(){
   return this.cust_dir;
}

public void setCust_dir( String param ){
   this.cust_dir= param;
}
public String getShoe_size_1(){
   return this.shoe_size_1;
}

public void setShoe_size_1( String param ){
   this.shoe_size_1= param;
}
public String getShoe_size_2(){
   return this.shoe_size_2;
}

public void setShoe_size_2( String param ){
   this.shoe_size_2= param;
}
public String getNotes(){
   return this.notes;
}

public void setNotes( String param ){
   this.notes= param;
}
public int getCust_id(){
   return this.cust_id;
}

public void setCust_id( int param ){
   this.cust_id= param;
}
public java.sql.Timestamp getMeasure_date(){
   return this.measure_date;
}

public void setMeasure_date( java.sql.Timestamp param ){
   this.measure_date= param;
}
public String getUnits(){
   return this.units;
}

public void setUnits( String param ){
   this.units= param;
}
public int getLf_length(){
   return this.lf_length;
}

public void setLf_length( int param ){
   this.lf_length= param;
}
public int getLf_bb_width(){
   return this.lf_bb_width;
}

public void setLf_bb_width( int param ){
   this.lf_bb_width= param;
}
public int getLf_b2b(){
   return this.lf_b2b;
}

public void setLf_b2b( int param ){
   this.lf_b2b= param;
}
public int getLf_bb_girth(){
   return this.lf_bb_girth;
}

public void setLf_bb_girth( int param ){
   this.lf_bb_girth= param;
}
public int getLf_waist_girth(){
   return this.lf_waist_girth;
}

public void setLf_waist_girth( int param ){
   this.lf_waist_girth= param;
}
public int getLf_instep_girth(){
   return this.lf_instep_girth;
}

public void setLf_instep_girth( int param ){
   this.lf_instep_girth= param;
}
public int getLf_heel_width(){
   return this.lf_heel_width;
}

public void setLf_heel_width( int param ){
   this.lf_heel_width= param;
}
public int getLf_short_heel(){
   return this.lf_short_heel;
}

public void setLf_short_heel( int param ){
   this.lf_short_heel= param;
}
public int getLf_ankle_height(){
   return this.lf_ankle_height;
}

public void setLf_ankle_height( int param ){
   this.lf_ankle_height= param;
}

public String toString(){
       return  " [id] " + id + " [rt_length] " + rt_length + " [rt_bb_width] " + rt_bb_width + " [rt_b2b] " + rt_b2b + " [rt_bb_girth] " + rt_bb_girth + " [rt_waist_girth] " + rt_waist_girth + " [rt_instep_girth] " + rt_instep_girth + " [rt_heel_width] " + rt_heel_width + " [rt_short_heel] " + rt_short_heel + " [rt_ankle_height] " + rt_ankle_height + " [stylus_allowance] " + stylus_allowance + " [cust_dir] " + cust_dir + " [shoe_size_1] " + shoe_size_1 + " [shoe_size_2] " + shoe_size_2 + " [notes] " + notes + " [cust_id] " + cust_id + " [measure_date] " + measure_date + " [units] " + units + " [lf_length] " + lf_length + " [lf_bb_width] " + lf_bb_width + " [lf_b2b] " + lf_b2b + " [lf_bb_girth] " + lf_bb_girth + " [lf_waist_girth] " + lf_waist_girth + " [lf_instep_girth] " + lf_instep_girth + " [lf_heel_width] " + lf_heel_width + " [lf_short_heel] " + lf_short_heel + " [lf_ankle_height] " + lf_ankle_height;
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
