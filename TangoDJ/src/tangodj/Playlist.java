package tangodj;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class Playlist 
{
	private ArrayList<Tanda> tandas = new ArrayList<Tanda>();
	private ArrayList<TrackRow> trackRows = new ArrayList<TrackRow>();
	public int playingTrack=-1;
	public int selectedTrack=0;
	private int tandaTrackNumber=0;
	private int trackNumber=0;
	boolean newTanda = true;
	double currentPosition=0;
	double[] tandaPositions;
	double height=0;
	int highlightedTanda=0;
	
	ScrollPane scrollPane;
	 Group displayGroup = new Group();
	 boolean highlightActive=false;
	 double scrollPaneContentsHeight=0;
	
	
	
	public void addTrackRow(TrackRow trackRow)
	{
	   
	   if (newTanda)
	   {
		 Tanda t = new Tanda(trackRow.getArtist(), trackRow.getGrouping()); 
		 //t.setPosition(trackNumber*22.188);
		 tandas.add(t);
		 newTanda=false;
	   }
	   Tanda tanda = tandas.get(tandas.size()-1);
	   trackRow.setTandaInfo(tandas.size()-1, tandaTrackNumber, trackNumber);
	   tandaTrackNumber++;
	   trackNumber++;
	   tanda.addTrackRow(trackRow);
	  // trackRows.add(trackRow);
	   
	   if ("cortina".equalsIgnoreCase(trackRow.getGrouping()))
	   {
		 newTanda=true;
		 tandaTrackNumber=0;
	   }
	}
	
	
	public void incrementSelected()
    {
      setNotSelectedIndicator();
      selectedTrack++;
      setSelectedIndicator();
    }
	
	 public void incrementPlaying()
	 {
	   setNotPlayingIndicator();
	      
	   if (playingIsSelected()) 
	   {
	     incrementSelected();
	     playingTrack++;
	   }
	   else 
	   {
	     playingTrack=selectedTrack;
	   }
     }
	    
	 public String getDragText(int index)
	 {
		TrackRow trackRow = trackRows.get(index);
		int tandaNumber = trackRow.getTandaNumber();
		System.out.println("Track title: "+trackRow.getTrackTitle()+" "+trackRow.getTandaNumber());
		Tanda tanda = tandas.get(tandaNumber);
		return tanda.artist.lastName+" - "+tanda.group;
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
			 // System.out.println("playlist, adding trackrow "+trackNumber);
			  tandaTrackNumber++;
			  trackNumber++;
			}
			
			tandaNumber++;
		  }  
	 }
	 
	 public void reorder(KeyCode direction)
	 {
		
		 //System.out.println("start tanda: "+startTandaIndex+", finish tanda: "+finishTandaIndex); 
		 int move=0;
		 if (direction==KeyCode.UP) move=-1;
		 else move=1;
		 
		 Tanda holdTanda = tandas.get(highlightedTanda);
		 tandas.remove(highlightedTanda);
		 tandas.add(highlightedTanda+move, holdTanda);
		 
	     finalize();
	     
	     highlightedTanda+=move;
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
	  
	  
	 // setScrollPane();
	  
	 // displayGroup.getChildren().add(scrollPane);
	 // scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
	 // calcTandaPositions();
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
	  }
	}
	

	
	/*
	private void setScrollPane()
	{
		scrollPane = new ScrollPane();
	  // MOUSE DOWN
	    scrollPane.setOnMousePressed(mouseDownHandler);
	   // scrollPane.setOnMouseReleased(mouseReleasedHandler);
	  ///scrollPane.setonm
	    scrollPane.getHvalue();
	    scrollPane.setPrefWidth(600);
	    scrollPane.setFitToHeight(true);
	    scrollPane.setOnKeyReleased(new EventHandler<KeyEvent>() {
	    public void handle(KeyEvent ke) 
	    {
	      if (ke.getCode()==KeyCode.ESCAPE) System.out.println(" esc Pressed");
	    }});
	}
	*/
	
	public ArrayList<Tanda> getTandas()
	{
	  return tandas;	
	}
	
	public ArrayList<TrackRow> getTrackRows()
	{
	   return trackRows;
	}
	
	public void setPlayingIndicator()
	{
	  trackRows.get(playingTrack).setNowPlayingIndicatorBall();
	}
	
	public void setNotPlayingIndicator()
	{
	  trackRows.get(playingTrack).setNotPlayingIndicatorBall();
	}
	
	public void setSelectedIndicator()
	{
		trackRows.get(selectedTrack).setSelectedIndicatorBall();
	}
	
	public void setNotSelectedIndicator()
	{
		trackRows.get(selectedTrack).setNotSelectedIndicatorBall();
	}
	public void resetSelectedIndicator()
	{
		trackRows.get(0).setNotSelectedIndicatorBall();
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
	
	public void setPlayingTrackToSelected()
	{
		playingTrack=selectedTrack;
	}
	
	public boolean playingIsSelected()
	{
		if (playingTrack==selectedTrack) return true;
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

 }
