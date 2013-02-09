package tracks;

public class AccountSummary
{
  private long acct_id;
  private String acct_desc;
  private double debit;
  private double credit;
  private String period;
  private String type;
  public String getAcct_desc()
  {
    return acct_desc;
  }
  public void setAcct_desc(String acct_desc)
  {
    this.acct_desc = acct_desc;
  }
  public long getAcct_id()
  {
    return acct_id;
  }
  public void setAcct_id(long acct_id)
  {
    this.acct_id = acct_id;
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
  public String getPeriod()
  {
    return period;
  }
  public void setPeriod(String period)
  {
    this.period = period;
  }
  public String getType()
  {
    return type;
  }
  public void setType(String type)
  {
    this.type = type;
  }
}
