package tangodj2.PlaylistTree;

import tangodj2.SharedValues;

public class TrackTreeItem extends BaseTreeItem
{
  private String trackHash;
  private int type = 0;
  private int tandaId=-1;

  public TrackTreeItem()
  {
	super();
	this.setTreeType("track");
	this.setValue(SharedValues.selectedPathHash.get());
  }

  public String getTrackHash() {
	return trackHash;
  }

  public void setTrackHash(String trackHash) {
	this.trackHash = trackHash;
  }

  public int getType() {
	return type;
  }

  public void setType(int type) {
	this.type = type;
  }

public int getTandaId() {
	return tandaId;
}

public void setTandaId(int tandaId) {
	this.tandaId = tandaId;
}
}
