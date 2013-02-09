package order;

import java.sql.Date;

import org.apache.struts.action.ActionForm;

public class OrderForm extends ActionForm
{
  /**
   *
   */
  private static final long serialVersionUID = 6350583453467130795L;
  String mode;
  long id;
  long cust_id;
  Date order_date;
  Date est_delivery;
  String notes;
  boolean complete;
  double price;
  String status;

  public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
String customer_name;

 public String getCustomer_name()
  {
    return customer_name;
  }
  public void setCustomer_name(String customer_name)
  {
    this.customer_name = customer_name;
  }
public long getCust_id()
 {
   return cust_id;
 }
 public void setCust_id(long cust_id)
 {
   this.cust_id = cust_id;
 }
 public Date getEst_delivery()
 {
   return est_delivery;
 }
 public void setEst_delivery(Date est_delivery)
 {
   this.est_delivery = est_delivery;
 }
 public long getId()
 {
   return id;
 }
 public void setId(long id)
 {
   this.id = id;
 }
 public String getNotes()
 {
   return notes;
 }
 public void setNotes(String notes)
 {
   this.notes = notes;
 }
 public Date getOrder_date()
 {
   return order_date;
 }
 public void setOrder_date(Date order_date)
 {
   this.order_date = order_date;
 }
public String getMode()
{
  return mode;
}
public void setMode(String mode)
{
  this.mode = mode;
}
public boolean getComplete()
{
  return complete;
}
public void setComplete(boolean complete)
{
  this.complete = complete;
}
public double getPrice()
{
  return price;
}
public void setPrice(double price)
{
  this.price = price;
}
} 