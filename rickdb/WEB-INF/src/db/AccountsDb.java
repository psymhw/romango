package db;

public class AccountsDb
{
  private long memberId;
  private long acctId;
  private String description;
  public long getAcctId()
  {
    return acctId;
  }
  public void setAcctId(long acctId)
  {
    this.acctId = acctId;
  }
  public String getDescription()
  {
    return description;
  }
  public void setDescription(String description)
  {
    this.description = description;
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
