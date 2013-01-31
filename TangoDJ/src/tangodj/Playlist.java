package tangodj;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

public class Playlist 
{
  private ArrayList<Tanda> tandas = new ArrayList<Tanda>();
  private ArrayList<TrackRow> trackRows = new ArrayList<TrackRow>();
  public int playingTrack=-1;
  public int selectedTrack=0;
  private int tandaTrackNumber=0;
  private int trackNumber=0;
  private boolean newTanda = true;
  private double[] tandaPositions;
  private double height=0;
  private int highlightedTanda=0;
  private Group displayGroup = new Group();
  private boolean highlightActive=false;
  private double scrollPaneContentsHeight=0;
  private int numberOfTandas=0;
  private boolean playing=false;
	
  public void addTrackRow(TrackRow trackRow)
  {
    if (newTanda)
    {
      Tanda t = new Tanda(trackRow.getArtist(), trackRow.getGrouping()); 
	  tandas.add(t);
	  newTanda=false;
	}
	Tanda tanda = tandas.get(tandas.size()-1);
	trackRow.setTandaInfo(tandas.size()-1, tandaTrackNumber, trackNumber);
	tandaTrackNumber++;
	trackNumber++;
	tanda.addTrackRow(trackRow);
	   
	if ("cortina".equalsIgnoreCase(trackRow.getGrouping()))
	{
	  newTanda=true;
	  tandaTrackNumber=0;
	}
  }
	
	
  public void incrementSelected()
  {
    setSelected(false);
    selectedTrack++;
    setSelected(true);
  }
	
  public void incrementPlaying()
  {
    setPlaying(false);
	      
	if (playingTrack==selectedTrack) 
	{
	  incrementSelected();
	  playingTrack++;
	}
	else 
	{
	  playingTrack=selectedTrack;
	}
  }
  
  public void setTrackRows()
  {
	trackRows.clear();
	Tanda tanda;
	ArrayList<TrackRow> trs;
	TrackRow tr;
		 
	int tandaNumber=0;
	int tandaTrackNumber=0;
	int trackNumber=0;
		  
	Iterator<Tanda> it = tandas.iterator();
	while(it.hasNext())
	{
	  tanda = it.next();
	  trs=tanda.getTrackRows();
	  Iterator<TrackRow> itx = trs.iterator();
	  while(itx.hasNext())
      {
	    tr = itx.next();
		tr.setTandaInfo(tandaNumber, tandaTrackNumber, trackNumber);
		trackRows.add(tr);
		tandaTrackNumber++;
		trackNumber++;
	  }
	  tandaNumber++;
	}  
  }
	 
  public void reorder(KeyCode direction)
  {
    int move=0;
    if ((direction==KeyCode.UP)&&(highlightedTanda==0)) return;
    if ((direction==KeyCode.DOWN)&&(highlightedTanda==(numberOfTandas-1))) return;
    
	if (direction==KeyCode.UP)
	{
	  move=-1;
	  
	}
	else // down arrow
	{
	  move=1;
	  
	}
	Tanda holdTanda = tandas.get(highlightedTanda);
	tandas.remove(highlightedTanda);
	tandas.add(highlightedTanda+move, holdTanda);
	
	finalize();
	highlightedTanda+=move;
	calcPositions();
	if (playing) { playingTrack=findPlayingTrack();  selectTrack(playingTrack); }
  }
	 
  public void calcPositions(double scrollPaneContentsHeight) 
  {
    this.scrollPaneContentsHeight=scrollPaneContentsHeight;
	calcPositions();
  }
  
  public void calcPositions() 
  {
    int trackNumber=0;
	Tanda t;
	tandaPositions = new double[tandas.size()];
	int i=0;
	double pos=0;
    double rowHeight=(scrollPaneContentsHeight)/getNumberOfTracks();
        
	Iterator<Tanda> it = tandas.iterator();
	while(it.hasNext())
	{
	  t=it.next();
	  pos=trackNumber*(rowHeight);
	  t.setPosition(pos);
	  tandaPositions[i]=pos;
	  trackNumber+=t.getTrackRows().size();
	  i++;
	}
  }

  public Tanda getTanda(int trackIndex)
  {
	TrackRow tr = trackRows.get(trackIndex);
	return tandas.get(tr.getTandaNumber());
  }
	
  public void finalize()
  {
	displayGroup= new Group();
	setTrackRows();
	displayGroup.getChildren().add(getTrackGrid());
	  
 	Tanda tanda;
	  
	Iterator<Tanda> it = tandas.iterator();
	while(it.hasNext())
	{
	  tanda = it.next();
	  displayGroup.getChildren().add(tanda.getTandaHighlightBox());
	}
	
	numberOfTandas=tandas.size();
  }

  public double[] getTandaPositions() {
		return tandaPositions;
	}
	
	public int getTandaIndex(int trackRowIndex)
	{
	  
      int ti = trackRows.get(trackRowIndex).getTandaNumber();
      if (ti>0) return ti;
	  return 0;
	}
	
	public Group getDisplay()
	{
	  return displayGroup;
	}
	
	private GridPane getTrackGrid()
	{
	  Tanda tanda;
	  ArrayList<TrackRow> trs;
	  TrackRow tr;
	  int row=0;
	  int numberOfTracksInPlaylist=0;
	  
	  
	  GridPane trackGrid = new GridPane();
		
	  trackGrid.setPadding(new Insets(0, 0, 0, 0));
	  trackGrid.setVgap(0);
	  trackGrid.setHgap(0);

	  Iterator<Tanda> it = tandas.iterator();
	  while(it.hasNext())
	  {
		tanda = it.next();
		trs=tanda.getTrackRows();
		Iterator<TrackRow> itx = trs.iterator();
		while(itx.hasNext())
		{
		  tr = itx.next();
		  trackGrid.add(tr.getIndicator(), 0, row);
		  trackGrid.add(tr.getTrackNumberLabel(), 1, row);
		  trackGrid.add(tr.getGroupingLabel(), 2, row);
		  trackGrid.add(tr.getArtistLabel(), 3, row);
		  trackGrid.add(tr.getTrackTitleLabel(), 4, row);
		  numberOfTracksInPlaylist++;  
		  row++;
		}
	  }  
	  
	 return trackGrid;
	
	}

	
	public double getHeight() {
		return height;
	}
	
	public void highlightTanda(int index)
	{
		highlightTandaOff();
		highlightedTanda=index;
		tandas.get(index).highlight(true);
		highlightActive=true;
	}
	
	public void highlightTanda()
	{
		highlightTandaOff();
		tandas.get(highlightedTanda).highlight(true);
		highlightActive=true;
	}
	
	public void highlightTandaOff()
	{
	  if (highlightActive)
	  {	  
	    tandas.get(highlightedTanda).highlight(false);
	    highlightActive=false;
	    System.out.println("highlighted tanda: "+highlightedTanda);
	  }
	}
	
	public ArrayList<Tanda> getTandas()
	{
	  return tandas;	
	}
	
	public ArrayList<TrackRow> getTrackRows()
	{
	   return trackRows;
	}
	
	public void setPlayingIndicator(boolean playing)
	{
	  trackRows.get(playingTrack).setPlaying(playing);
	}
	
	
	
	public void setSelected(boolean selected)
	{
	  trackRows.get(selectedTrack).setSelected(selected);
	}
	
	public void selectTrack(int trackIndex)
	{
	  trackRows.get(selectedTrack).setSelected(false);	
	  selectedTrack=trackIndex;
	  setSelected(true);
	}
	
	public void setPlayingTrack()
	{
      playingTrack=selectedTrack;
      setPlaying(true);
	}
	
	public void selectFirstTrack()
	{
	  selectedTrack=0;
	  setSelected(true);
	}
	
	
	public String getPlayingTrackPath()
	{
		return trackRows.get(playingTrack).getPath();
	}
	
	public TrackRow getTrack(int index)
	{
	  return trackRows.get(index); 
	}
	
	public TrackRow getPlayingTrack()
	{
	  return trackRows.get(playingTrack); 
	}
	
	public boolean isDone()
	{
		if ((playingTrack+1)>=trackRows.size()) return true;
		return false;
	}
	
	
		
	public void highlightTanda(int index, boolean choice)
	{
	   tandas.get(index).highlight(choice);
	}

	public int getNumberOfTracks()
	{
		return trackNumber;
	}
	
	public int getNumberOfTandas()
	{
		return numberOfTandas;
	}


	public void setScrollPaneContentsHeight(double scrollPaneContentsHeight) {
		this.scrollPaneContentsHeight = scrollPaneContentsHeight;
		calcPositions();
	}


	public boolean isPlaying() {
		return playing;
	}


	public void setPlaying(boolean playing) 
	{
	   this.playing = playing;
	   setPlayingIndicator(playing);
	}
	
	private int findPlayingTrack()
	{
	  Iterator it = trackRows.iterator();
	  TrackRow tr;
	  int i=0;
	  while(it.hasNext())
	  {
		  tr=(TrackRow)it.next();
		  if (tr.isPlaying()) return i;
		  i++;
	  }
	  return -1;
	}

 }
