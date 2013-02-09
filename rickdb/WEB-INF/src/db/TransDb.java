package db;

import java.sql.Date;

public class TransDb
{
  private long memberId;
  private long transId;
  private long trackId;
  private long acctId;
  private double debit;
  private double credit;
  private Date transDate;
  private String description;
  private String comment;
  private int archived;
  private String archive_set;
  long cust_id;
  long order_id;
  
  public long getCust_id() {
	return cust_id;
}
public void setCust_id(long custId) {
	cust_id = custId;
}
public long getOrder_id() {
	return order_id;
}
public void setOrder_id(long orderId) {
	order_id = orderId;
}
public String getArchive_set()
  {
    return archive_set;
  }
  public void setArchive_set(String archive_set)
  {
    this.archive_set = archive_set;
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
  @Override
public String toString() {
	return "TransDb [acctId=" + acctId + ", archive_set=" + archive_set
			+ ", archived=" + archived + ", comment=" + comment + ", credit="
			+ credit + ", cust_id=" + cust_id + ", debit=" + debit
			+ ", description=" + description + ", memberId=" + memberId
			+ ", order_id=" + order_id + ", trackId=" + trackId
			+ ", transDate=" + transDate + ", transId=" + transId + "]";
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
 
  public long getMemberId()
  {
    return memberId;
  }

  public void setMemberId(long memberId)
  {
    this.memberId = memberId;
  }
  public int getArchived()
  {
    return archived;
  }
  public void setArchived(int archived)
  {
    this.archived = archived;
  }
  
  

}
