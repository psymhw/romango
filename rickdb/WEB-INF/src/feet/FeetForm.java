package feet;

import org.apache.struts.action.ActionForm;

public class FeetForm extends ActionForm
{
  private static final long serialVersionUID = 7708164923477978156L;
  private String mode="new";
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

  String customer_name;



public int getId(){
   return this.id;
}

public void setId( int param ){
   this.id= param;
}


public String getCust_dir()
{
  return cust_dir;
}

public void setCust_dir(String cust_dir)
{
  this.cust_dir = cust_dir;
}

public int getCust_id()
{
  return cust_id;
}

public void setCust_id(int cust_id)
{
  this.cust_id = cust_id;
}

public int getLf_ankle_height()
{
  return lf_ankle_height;
}

public void setLf_ankle_height(int lf_ankle_height)
{
  this.lf_ankle_height = lf_ankle_height;
}

public int getLf_b2b()
{
  return lf_b2b;
}

public void setLf_b2b(int lf_b2b)
{
  this.lf_b2b = lf_b2b;
}

public int getLf_bb_girth()
{
  return lf_bb_girth;
}

public void setLf_bb_girth(int lf_bb_girth)
{
  this.lf_bb_girth = lf_bb_girth;
}

public int getLf_bb_width()
{
  return lf_bb_width;
}

public void setLf_bb_width(int lf_bb_width)
{
  this.lf_bb_width = lf_bb_width;
}

public int getLf_heel_width()
{
  return lf_heel_width;
}

public void setLf_heel_width(int lf_heel_width)
{
  this.lf_heel_width = lf_heel_width;
}

public int getLf_instep_girth()
{
  return lf_instep_girth;
}

public void setLf_instep_girth(int lf_instep_girth)
{
  this.lf_instep_girth = lf_instep_girth;
}

public int getLf_length()
{
  return lf_length;
}

public void setLf_length(int lf_length)
{
  this.lf_length = lf_length;
}

public int getLf_short_heel()
{
  return lf_short_heel;
}

public void setLf_short_heel(int lf_short_heel)
{
  this.lf_short_heel = lf_short_heel;
}

public int getLf_waist_girth()
{
  return lf_waist_girth;
}

public void setLf_waist_girth(int lf_waist_girth)
{
  this.lf_waist_girth = lf_waist_girth;
}

public java.sql.Timestamp getMeasure_date()
{
  return measure_date;
}

public void setMeasure_date(java.sql.Timestamp measure_date)
{
  this.measure_date = measure_date;
}

public String getNotes()
{
  return notes;
}

public void setNotes(String notes)
{
  this.notes = notes;
}

public int getRt_ankle_height()
{
  return rt_ankle_height;
}

public void setRt_ankle_height(int rt_ankle_height)
{
  this.rt_ankle_height = rt_ankle_height;
}

public int getRt_b2b()
{
  return rt_b2b;
}

public void setRt_b2b(int rt_b2b)
{
  this.rt_b2b = rt_b2b;
}

public int getRt_bb_girth()
{
  return rt_bb_girth;
}

public void setRt_bb_girth(int rt_bb_girth)
{
  this.rt_bb_girth = rt_bb_girth;
}

public int getRt_bb_width()
{
  return rt_bb_width;
}

public void setRt_bb_width(int rt_bb_width)
{
  this.rt_bb_width = rt_bb_width;
}

public int getRt_heel_width()
{
  return rt_heel_width;
}

public void setRt_heel_width(int rt_heel_width)
{
  this.rt_heel_width = rt_heel_width;
}

public int getRt_instep_girth()
{
  return rt_instep_girth;
}

public void setRt_instep_girth(int rt_instep_girth)
{
  this.rt_instep_girth = rt_instep_girth;
}

public int getRt_length()
{
  return rt_length;
}

public void setRt_length(int rt_length)
{
  this.rt_length = rt_length;
}

public int getRt_short_heel()
{
  return rt_short_heel;
}

public void setRt_short_heel(int rt_short_heel)
{
  this.rt_short_heel = rt_short_heel;
}

public int getRt_waist_girth()
{
  return rt_waist_girth;
}

public void setRt_waist_girth(int rt_waist_girth)
{
  this.rt_waist_girth = rt_waist_girth;
}

public String getShoe_size_1()
{
  return shoe_size_1;
}

public void setShoe_size_1(String shoe_size_1)
{
  this.shoe_size_1 = shoe_size_1;
}

public String getShoe_size_2()
{
  return shoe_size_2;
}

public void setShoe_size_2(String shoe_size_2)
{
  this.shoe_size_2 = shoe_size_2;
}

public int getStylus_allowance()
{
  return stylus_allowance;
}

public void setStylus_allowance(int stylus_allowance)
{
  this.stylus_allowance = stylus_allowance;
}

public String getUnits()
{
  return units;
}

public void setUnits(String units)
{
  this.units = units;
}

public String getMode()
{
  return mode;
}

public void setMode(String mode)
{
  this.mode = mode;
}

public String getCustomer_name()
{
  return customer_name;
}

public void setCustomer_name(String customer_name)
{
  this.customer_name = customer_name;
}
}
