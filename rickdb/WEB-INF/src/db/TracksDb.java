package db;

public class TracksDb
{
  private long memberId;
  private long trackId;
  private String description;
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
  public void setTrackId(long tracksId)
  {
    this.trackId = tracksId;
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
