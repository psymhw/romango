package db;

import java.sql.Date;

public class MileageDb
{
  private long memberId;
  private long id;
  private Date mileage_date;
  private String description;
  private long track;
  private long start_mile;
  private long end_mile;
  private int miles;
  public String getDescription()
  {
    return description;
  }
  public void setDescription(String description)
  {
    this.description = description;
  }
  public long getEnd_mile()
  {
    return end_mile;
  }
  public void setEnd_mile(long end_mile)
  {
    this.end_mile = end_mile;
  }
  public long getId()
  {
    return id;
  }
  public void setId(long id)
  {
    this.id = id;
  }
  
  public Date getMileage_date()
  {
    return mileage_date;
  }
  public void setMileage_date(Date mileage_date)
  {
    this.mileage_date = mileage_date;
  }
  public int getMiles()
  {
    return miles;
  }
  public void setMiles(int miles)
  {
    this.miles = miles;
  }
  public long getStart_mile()
  {
    return start_mile;
  }
  public void setStart_mile(long start_mile)
  {
    this.start_mile = start_mile;
  }
  public long getTrack()
  {
    return track;
  }
  public void setTrack(long track)
  {
    this.track = track;
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
