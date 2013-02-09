package tracks;

import java.util.ArrayList;
import java.sql.Date;
import trans.TransData;

public class TrackData
{
 // private ArrayList<TransData> transList = new ArrayList<TransData>();
  private ArrayList transList = new ArrayList();
  
  double debitTotal=0;
  double creditTotal=0;
  double net=0;
  String trackName;
  String trackId;
  Date max_date, min_date;

  public ArrayList<TransData> getTransList()
  {
    return transList;
  }

  public void setTransList(ArrayList<TransData> transList)
  {
    this.transList = transList;
  }

  public double getCreditTotal()
  {
    return creditTotal;
  }

  public void setCreditTotal(double creditTotal)
  {
    this.creditTotal = creditTotal;
  }

  public double getDebitTotal()
  {
    return debitTotal;
  }

  public void setDebitTotal(double debitTotal)
  {
    this.debitTotal = debitTotal;
  }
  
  public void addToDebitTotal(double debit)
  {
    this.debitTotal = this.debitTotal+debit;
    this.net=this.net+debit;
  }

  public void addToCreditTotal(double credit)
  {
    this.creditTotal=this.creditTotal+credit;
    this.net=this.net-credit;
  }
  
  public double getNet()
  {
    return net;
  }

  public void setNet(double net)
  {
    this.net = net;
  }

  public String getTrackName()
  {
    return trackName;
  }

  public void setTrackName(String trackName)
  {
    this.trackName = trackName;
  }

  public String getTrackId()
  {
    return trackId;
  }

  public void setTrackId(String trackId)
  {
    this.trackId = trackId;
  }

  public Date getMax_date()
  {
    return max_date;
  }

  public void setMax_date(Date max_date)
  {
    this.max_date = max_date;
  }

  public Date getMin_date()
  {
    return min_date;
  }

  public void setMin_date(Date min_date)
  {
    this.min_date = min_date;
  }

  
}
