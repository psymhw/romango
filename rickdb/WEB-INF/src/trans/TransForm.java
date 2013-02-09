package trans;

import java.sql.Date;

import org.apache.struts.action.ActionForm;

public class TransForm extends ActionForm
{
  private static final long serialVersionUID = -6260346574918652744L;
  long transId;
  long trackId;
  long acctId;
  double debit;
  double credit;
  Date transDate;
  String description;
  String comment;
  String mode;
  long cust_id;
  long order_id;

  public String getMode()
  {
    return mode;
  }
  public void setMode(String mode)
  {
    this.mode = mode;
  }
  public long getAcctId()
  {
    return acctId;
  }
  public void setAcctId(long acctId)
  {
    this.acctId = acctId;
  }
  public String getComment()
  {
    return comment;
  }
  public void setComment(String comment)
  {
    this.comment = comment;
  }
  public double getCredit()
  {
    return credit;
  }
  public void setCredit(double credit)
  {
    this.credit = credit;
  }
  public double getDebit()
  {
    return debit;
  }
  public void setDebit(double debit)
  {
    this.debit = debit;
  }
  public String getDescription()
  {
    return description;
  }
  public void setDescription(String description)
  {
    this.description = description;
  }
  public long getTrackId()
  {
    return trackId;
  }
  public void setTrackId(long trackId)
  {
    this.trackId = trackId;
  }
  public Date getTransDate()
  {
    return transDate;
  }
  public void setTransDate(Date transDate)
  {
    this.transDate = transDate;
  }
  public long getTransId()
  {
    return transId;
  }
  public void setTransId(long transId)
  {
    this.transId = transId;
  }
public long getCust_id() {
    return cust_id;
}
public void setCust_id(long cust_id) {
    this.cust_id = cust_id;
}
public long getOrder_id() {
    return order_id;
}
public void setOrder_id(long order_id) {
    this.order_id = order_id;
}



} 