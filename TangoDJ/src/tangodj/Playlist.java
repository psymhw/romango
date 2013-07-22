package tangodj;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.LinearGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

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
  public int tandaFirstTrackMark=-1;
  public int tandaLastTrackMark=-1;
  private Rectangle trackHighlightBox;
  public Rectangle tandaHighlightBox;
  
  public Playlist()
  {
	setupTrackHightlightBox();
	setupTandaHighlightBox();
	
  }
	
  public void addTrackRow(TrackRow trackRow)
  {
	trackRows.add(trackRow);
	trackNumber++; 
	
	/*
    if (newTanda)
    {
      Tanda t = new Tanda(trackRow.getArtist(), trackRow.getGrouping()); 
	  tandas.add(t);
	  newTanda=false;
	  tandaTrackNumber=0;
	}
	Tanda tanda = tandas.get(tandas.size()-1);
	trackRow.setTandaInfo(tandas.size()-1, tandaTrackNumber, trackNumber);
	// System.out.println("tandatracknumber: "+tandaTrackNumber);
	tandaTrackNumber++;
	trackNumber++;
	tanda.addTrackRow(trackRow);
	   
	if ("cortina".equalsIgnoreCase(trackRow.getGrouping()))
	{
	  newTanda=true;
	  tandaTrackNumber=0;
	}
	*/
  }
	
  public void addTanda(String orchestra, String style)
  {
	TrackRow trackRow=null;
	int tandaTrack=0;
	Tanda tanda = new Tanda(orchestra, style); 
	for(int i=tandaFirstTrackMark; i<(tandaLastTrackMark+1); i++)
	{
	  trackRow = trackRows.get(i);
	  trackRow.setTandaInfo(tandas.size(), tandaTrack, i);
	  if (tandaTrack==0)
	  {
	    trackRow.setArtist(orchestra);
	    trackRow.setGrouping(style);
	    //trackRow.setGroupingInMp3(style);
	    trackRow.setGroupingLabel(style);
	    trackRow.setBackgroundColor(style);
	  }
	  else if (tandaTrack==tandaLastTrackMark-tandaFirstTrackMark)
	  {
		trackRow.setGroupingLabel("CORTINA"); 
		//trackRow.setGroupingInMp3(style);
		trackRow.setBackgroundColor("CORTINA");
	  }
	  else
	  {
		trackRow.setArtist(""); 
		//trackRow.setGroupingInMp3(style);
		trackRow.setBackgroundColor(style);
	  }
	  tanda.addTrackRow(trackRow);
	  tandaTrack++;
	}
	
	double pos=0;
    double rowHeight=(scrollPaneContentsHeight)/getNumberOfTracks();
	pos=tandaFirstTrackMark*(rowHeight);
	tanda.setPosition(pos);

	displayGroup.getChildren().add(tanda.getTandaHighlightBox());
	// test: displayGroup.getChildren().remove(tanda.getTandaHighlightBox());
	
	tandas.add(tanda);
	
	//calcPositions();
	
	numberOfTandas=tandas.size();
	
	tandaFirstTrackMark=-1;
	tandaLastTrackMark=-1;
	
	trackHighlightBox.setVisible(false);
	//tanda.highlight(true);
  }
  
  public void highlightFirstTrack()
  {
	  double pos=0;
	  double rowHeight=(scrollPaneContentsHeight)/getNumberOfTracks();
	  pos=tandaFirstTrackMark*(rowHeight); 
	  trackHighlightBox.setY(pos);
	//  System.out.println("first track pos: "+pos);
	  tandaHighlightBox.setVisible(false);
	  trackHighlightBox.setVisible(true);
  }
  
  public void highlightTandaBlock()
  {
	  double pos=0;
	  double rowHeight=(scrollPaneContentsHeight)/getNumberOfTracks();
	  pos=tandaFirstTrackMark*(rowHeight); 
	  tandaHighlightBox.setY(pos);
	  
	  tandaHighlightBox.setHeight((tandaLastTrackMark-tandaFirstTrackMark+1)*22.188);
	 // System.out.println("tanda highlight pos: "+pos);
	  trackHighlightBox.setVisible(false);
	  tandaHighlightBox.setVisible(true);
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
	Tanda tanda;
	tandaPositions = new double[tandas.size()];
	int i=0;
	double pos=0;
    double rowHeight=(scrollPaneContentsHeight)/getNumberOfTracks();
    
    
	Iterator<Tanda> it = tandas.iterator();
	while(it.hasNext())
	{
	  tanda=it.next();
	  
	  pos=trackNumber*(rowHeight);
	  tanda.setPosition(pos);
	  tandaPositions[i]=pos;
	  trackNumber+=tanda.getTrackRows().size();
	  
	  System.out.println("Tanda: "+tanda.artist.lastName+" pos: "+pos);
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
	//setTrackRows();
	displayGroup.getChildren().add(getTrackGrid());
	displayGroup.getChildren().add(trackHighlightBox);
	displayGroup.getChildren().add(tandaHighlightBox);
	 
	/*
 	Tanda tanda;
	  
	Iterator<Tanda> it = tandas.iterator();
	while(it.hasNext())
	{
	  tanda = it.next();
	  displayGroup.getChildren().add(tanda.getTandaHighlightBox());
	}
	
	numberOfTandas=tandas.size();
	*/
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
	
	/*
	private GridPane getTrackGrid()
	{
	  Tanda tanda;
	  ArrayList<TrackRow> trs;
	  TrackRow tr;
	  int row=0;
	  int numberOfTracksInPlaylist=0;
	  
	  
	  GridPane trackGrid = new GridPane();
		
	  trackGrid.setPadding(new Insets(0, 0, 0, 10));
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
	*/
	
	private GridPane getTrackGrid()
	{
		TrackRow tr;
		
		 int row=0;
		  int numberOfTracksInPlaylist=0;
		  
		  
		  GridPane trackGrid = new GridPane();
			
		  trackGrid.setPadding(new Insets(0, 0, 0, 10));
		  trackGrid.setVgap(0);
		  trackGrid.setHgap(0);
		
		Iterator<TrackRow> itx = trackRows.iterator();
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
	
	public String getPlayingTitle()
	{
	  return trackRows.get(playingTrack).getTrackTitle(); 
	}
	
	public String getPlayingArtist()
	{
	  return trackRows.get(playingTrack).getArtist();
	}
	
	public String getPlayingGrouping()
	{
	  return trackRows.get(playingTrack).getGrouping();
	}
	
	public String getTandaInfo()
	{
	  TrackRow tr=trackRows.get(playingTrack);
	  String tandaInfo=	tr.getGrouping()+" "+(tr.getTandaTrackIndex()+1)+" of "+getTanda(tr.getTandaNumber()).getTracksInTanda();
	  return tandaInfo;
	}
	
	public int getTandaTrackIndex()
	{
		TrackRow tr=trackRows.get(playingTrack);
		return tr.getTandaTrackIndex();
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
	
	public int getNumberOfTracksInPlayingTanda()
	{
		TrackRow tr = getTrack(playingTrack);
		return getTanda(tr.getTandaNumber()).getTracksInTanda();
	}
	
	public String getNextTandaInfo()
	{
		TrackRow tr = getTrack(playingTrack);
		int nextTandaIndex=tr.getTandaNumber()+1;
		//System.out.println("Next Tanda: "+nextTandaIndex);
		if (nextTandaIndex<tandas.size())
		{	
		  Tanda t = tandas.get(nextTandaIndex);
		  return "Next Tanda: "+t.artist.lastName+" - "+t.group;
		}
		else return "The End";
		
	}
	
	public int getNumberOfTandas()
	{
		return numberOfTandas;
	}


	public void setScrollPaneContentsHeight(double scrollPaneContentsHeight) {
		this.scrollPaneContentsHeight = scrollPaneContentsHeight;
		//calcPositions();
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

	
	private void setupTrackHightlightBox()
	{
	  trackHighlightBox = new Rectangle(560, 22.188);
      trackHighlightBox.setX(15);
      trackHighlightBox.setY(0);
      trackHighlightBox.setFill(linearGradient_REFLECT);
      trackHighlightBox.setOpacity(.3);
      trackHighlightBox.setStroke(Color.BLACK);
      trackHighlightBox.setStrokeWidth(3);
      trackHighlightBox.setStrokeType(StrokeType.INSIDE);
      trackHighlightBox.setVisible(false);
      trackHighlightBox.setArcHeight(10);
      trackHighlightBox.setArcWidth(10);
	}
	
	 LinearGradient linearGradient_REFLECT
	  = LinearGradientBuilder.create()
	  .startX(275)
	  .startY(50)
	  .endX(250)
	  .endY(100)
	  .proportional(false)
	  .cycleMethod(CycleMethod.REFLECT)
	  .stops(
	      new Stop(0.1f, Color.rgb(255, 0, 255, 0.9)),
	      new Stop(1.0f, Color.rgb(0, 255, 0, 1.0)))
	  .build();
	  
	 private void setupTandaHighlightBox() 
	 {
	   tandaHighlightBox = new Rectangle(560, 22.188);
	   tandaHighlightBox.setX(15);
	   tandaHighlightBox.setY(0);
	   tandaHighlightBox.setFill(linearGradient_REFLECT);
	   tandaHighlightBox.setOpacity(.3);
	   tandaHighlightBox.setStroke(Color.BLACK);
	   tandaHighlightBox.setStrokeWidth(3);
	   tandaHighlightBox.setStrokeType(StrokeType.INSIDE);
	   tandaHighlightBox.setVisible(false);
	   tandaHighlightBox.setArcHeight(10);
	   tandaHighlightBox.setArcWidth(10);
	 }
	   
	   
	   
 }
