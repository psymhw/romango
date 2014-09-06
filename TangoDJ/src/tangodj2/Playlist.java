package tangodj2;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
//import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import tangodj2.PlaylistTree.BaseTreeItem;
import tangodj2.PlaylistTree.CortinaTreeItem;
import tangodj2.PlaylistTree.PlaylistTreeItem;
import tangodj2.PlaylistTree.TandaTreeItem;
import tangodj2.PlaylistTree.TrackTreeItem;
import tangodj2.cortina.CortinaTrack;
import tangodj2.dialogs.TandaInfoDialog;
import tangodj2.infoWindow.InfoWindow2;

public class Playlist 
{
  ChangeListener<TreeItem<String>> treeViewChangeListener;
  private PlaylistTreeItem playlistTreeItem;
  private BaseTreeItem selectedBaseTreeItem;
  private TandaTreeItem selectedTandaTreeItem=null;
  private TrackTreeItem selectedTrackTreeItem=null;
  private CortinaTreeItem selectedCortinaTreeItem=null;
  private TreeView<String> treeView;
  private PlaylistTrack previouslyPlayingTrack=null;
  private PlaylistTrack previouslySelectedTrack=null;
  private Playlist playlist;
  private int selectedFlatPlayableIndex=-1;
  
  private int nextTrack=0; 
  private int playingTrack=0;
 // private int selectedPlaylistTrack=0;
  private ArrayList<PlaylistTrack> flatPlaylistTracks =  new ArrayList<PlaylistTrack>();
 // private ArrayList<TandaInfo> tandas =  new ArrayList<TandaInfo>();
  private int selectedTandaIndex=-1;
  private int numberOfTandas=-1;
  double totalPlaylistTime=0;
  public static SimpleIntegerProperty playlistFocus = new SimpleIntegerProperty(0);
   
  public Playlist(int playlistId) 
  {
	  this.playlist=this;
	  setupTreeView(playlistId);	
	  setNextTrackToPlay();  
  }
	
  public void printTracks()
  {
	int i=0;
	while( true)
	{
	  BaseTreeItem ti = (BaseTreeItem)treeView.getTreeItem(i);
	  if (ti==null) break;
	  System.out.println(i+") "+ti.getTreeType());
	  i++;
	}
  }
    	
  public void stopPlaying()
  {
    int playlistSize=flatPlaylistTracks.size();
    if (playlistSize==0) return;
	  flatPlaylistTracks.get(playingTrack).baseTreeItem.setPlayingImage(false);
	  flatPlaylistTracks.get(playingTrack).baseTreeItem.setNextPlayImage(true);
	  
	 // if (playingTrack<(playlistSize-1))
	  //{
	 //   flatPlaylistTracks.get(playingTrack+1).baseTreeItem.setNextPlayImage(true);
	// }
  }
  
  public void skip()
  {
    int playlistSize=flatPlaylistTracks.size();
    if (playlistSize==0) return;
    flatPlaylistTracks.get(playingTrack).baseTreeItem.setPlayingImage(false);
    
   // if (playingTrack<(playlistSize-1))
    //{
   //   flatPlaylistTracks.get(playingTrack+1).baseTreeItem.setNextPlayImage(true);
  // }
  }
	
  public PlaylistTrack getTrack(int trackNo)
  {
	  if (trackNo>=flatPlaylistTracks.size()) return null;
	  return flatPlaylistTracks.get(trackNo);
  }
	
  public void setPrevious()
  {
	  nextTrack-=1;
	  if (nextTrack<0) nextTrack=0;
  }
	
	
  public void setNextTrackToPlay()
  {
	  if (selectedFlatPlayableIndex==-1) return;
	  if (flatPlaylistTracks==null) return;
	  if (flatPlaylistTracks.size()==0) return;
	  PlaylistTrack playlistTrack = flatPlaylistTracks.get(selectedFlatPlayableIndex);
	
	  if (playlistTrack.baseTreeItem.getStatus()!=TrackTreeItem.PLAYING)
    {  
      playlistTrack.baseTreeItem.setNextPlayImage(true);
      nextTrack=selectedFlatPlayableIndex;
      if (previouslySelectedTrack!=null) 
      {
        if (!previouslySelectedTrack.playing)
        previouslySelectedTrack.baseTreeItem.setNextPlayImage(false);
      }
      previouslySelectedTrack=playlistTrack;
    }
	  //System.out.println("Playlist next track: "+nextTrack);
  }
  
  public void disableTrack(boolean set)
  {
    TrackTreeItem trackTreeItem = (TrackTreeItem)selectedBaseTreeItem;
    //System.out.println("Playlist disable track: "+set);
  
    if (trackTreeItem.getStatus()!=BaseTreeItem.PLAYING)
    {  
      Db.disableTrack(set, trackTreeItem.getTandaDbId(), trackTreeItem.getTrackInTree());
      trackTreeItem.setDisableImage(set);
    }
  }
  
  public void delayTrack(boolean set)
  {
    TrackTreeItem trackTreeItem = (TrackTreeItem)selectedBaseTreeItem;
    //System.out.println("Playlist delay track: "+set);
  
    if (trackTreeItem.getStatus()!=BaseTreeItem.PLAYING)
    {  
      Db.setTrackDelay(set, trackTreeItem.getDbId());
    }
  }
  
  public void disableTanda(boolean set)
  {
    TandaTreeItem tandaTreeItem = getSelectedTanda();
    tandaTreeItem.setDisableImage(set);
    BaseTreeItem baseTreeItem=null;
    
    //System.out.println("disable tanda: "+tandaTreeItem.getDbId());
    Db.disableTanda(set, tandaTreeItem.getDbId());
    
    // disable the tracks but not in the DB file
    for(int i=0; i<tandaTreeItem.getChildren().size(); i++)
    {
      baseTreeItem=(BaseTreeItem)tandaTreeItem.getChildren().get(i);
      baseTreeItem.setDisableImage(set);
    }
    
  }
	
  /*
  public void generateFlatList()
  {
    int i=0;
    flatPlaylistTracks.clear();
    int tandaCounter=-1;
    int tandaTrackCounter=0;
    int numberOfTracksInTanda=0;
    String tandaName=null;
    PlaylistTrack playlistTrack;
    numberOfTandas=0;
    int playableIndex=0;
    
    TandaInfo tandaInfo=null;
    double startTimeInTanda=0;
    totalPlaylistTime=0;
    String style="";
    
    
    
    while( true)
    {
      BaseTreeItem ti = (BaseTreeItem)treeView.getTreeItem(i);
      if (ti==null) break;
      if ("playlist".equals(ti.getTreeType()))
      {
        PlaylistTreeItem playlistTreeItem = (PlaylistTreeItem)ti;
        playlistTreeItem.setPlayableIndex(0);
        numberOfTandas=playlistTreeItem.getChildren().size();
      }
      else if ("tanda".equals(ti.getTreeType()))
      {
        startTimeInTanda=0;
        tandaInfo = new TandaInfo();
        tandaTrackCounter=0;
        tandaCounter++;
        TandaTreeItem tandaTreeItem = (TandaTreeItem)ti;
        tandaName = tandaTreeItem.getArtistAndStyle();
        style= tandaTreeItem.getStyle();
        numberOfTracksInTanda=tandaTreeItem.getChildren().size()-1; // minus 1 for cortina
        tandaTreeItem.setPlayableIndex(playableIndex);
        
        tandaInfo.numberOfTracksInTanda=0;
        tandaInfo.tandaNumber=tandaCounter;
        tandaInfo.tandaName=tandaName;
        tandaInfo.numberOfTracksInTanda=numberOfTracksInTanda;
        if ((tandaCounter+1)<numberOfTandas)
        {
          tandaInfo.nextTandaName=((TandaTreeItem)playlistTreeItem.getChildren().get(tandaCounter+1)).getArtistAndStyle();
        }
      }
      else if ("tango".equals(ti.getTreeType())||"cleanup".equals(ti.getTreeType()))
      {
        TrackTreeItem trackTreeItem = (TrackTreeItem)ti;
        trackTreeItem.setPlayableIndex(playableIndex);
        playableIndex++;
        playlistTrack = new PlaylistTrack();
        playlistTrack.title=trackTreeItem.getValue();
        playlistTrack.album=trackTreeItem.getAlbum();
        playlistTrack.artist=trackTreeItem.getArtist();
        playlistTrack.singer=trackTreeItem.getSinger();
        playlistTrack.comment=trackTreeItem.getComment();
        playlistTrack.path=trackTreeItem.getPath();
        playlistTrack.style=style;
        tandaTrackCounter++;
        playlistTrack.trackInTanda=tandaTrackCounter;
        playlistTrack.cortina=false;
        playlistTrack.baseTreeItem=trackTreeItem;
        playlistTrack.trackHash=trackTreeItem.getTrackHash();
        playlistTrack.duration=trackTreeItem.getDuration();
        playlistTrack.startTimeInPlaylist=totalPlaylistTime;
        totalPlaylistTime+=playlistTrack.duration;
        playlistTrack.startTimeInTanda=startTimeInTanda;
        startTimeInTanda+=playlistTrack.duration;
        tandaInfo.tandaDuration+=playlistTrack.duration;
        playlistTrack.tandaInfo=tandaInfo;
        flatPlaylistTracks.add(playlistTrack);
      }
      else if ("cortina".equals(ti.getTreeType()))
      {
        CortinaTreeItem cortinaTreeItem = (CortinaTreeItem)ti;
        cortinaTreeItem.setPlayableIndex(playableIndex);
        playableIndex++;
        playlistTrack = new PlaylistTrack();
        playlistTrack.title=cortinaTreeItem.getValue();
        playlistTrack.album = cortinaTreeItem.getAlbum();
        playlistTrack.artist = cortinaTreeItem.getArtist();
        playlistTrack.singer="";
        playlistTrack.comment="";
        playlistTrack.style="Cortina";
        playlistTrack.path=cortinaTreeItem.getPath();
        playlistTrack.cortina=true;
        playlistTrack.baseTreeItem=cortinaTreeItem;
        playlistTrack.trackHash=cortinaTreeItem.getPathHash();
        
        playlistTrack.premade=cortinaTreeItem.getPremade();
        playlistTrack.startValue =cortinaTreeItem.getStart();
        playlistTrack.stopValue  =cortinaTreeItem.getStop();
        playlistTrack.fadein     =cortinaTreeItem.getFadein();
        playlistTrack.fadeout    =cortinaTreeItem.getFadeout();
        playlistTrack.delay      =cortinaTreeItem.getDelay();
        playlistTrack.original_duration  =cortinaTreeItem.getOriginal_duration();
        playlistTrack.duration=cortinaTreeItem.getDuration();
        
        playlistTrack.tandaInfo=tandaInfo;
        playlistTrack.startTimeInPlaylist=totalPlaylistTime;
        totalPlaylistTime+=playlistTrack.duration;
        tandaInfo.tandaDuration+=playlistTrack.duration;
        playlistTrack.startTimeInTanda=startTimeInTanda;
        startTimeInTanda+=playlistTrack.duration;
        flatPlaylistTracks.add(playlistTrack);
        // didn't set or increment tandaTrackCounter or set tandaCounter
      }
      i++;
    }
    
    BaseTreeItem  ti = (BaseTreeItem)treeView.getTreeItem(i-1);
    if ("tanda".equals(ti.getTreeType()))
    {
      // if the last item in the tree is a tanda, there is no next track
      ti.setPlayableIndex(999);
    }
    else
    {
     // tandas.add(tandaInfo);
    }
  //  totalPlaylistTimeProperty.set(totalPlaylistTime);
    // System.out.println("Playlist total duration: "+formatIntoMMSS(totalPlaylistTime));
   //  printFlatList();
   // printTandaInfoList();
  //  collapseTandas();
  }
   */
  
  public void generateFlatList()
  {
    int trackAddCounter=0;
    flatPlaylistTracks.clear();
    int tandaCounter=-1;
    int tandaTrackCounter=0;
    int numberOfTracksInTanda=0;
    String tandaName=null;
    PlaylistTrack playlistTrack;
    numberOfTandas=0;
    int playableIndex=0;
    
    TandaInfo tandaInfo=null;
    double startTimeInTanda=0;
    totalPlaylistTime=0;
    String style="";
    
    PlaylistTreeItem playlistTreeItem = (PlaylistTreeItem)treeView.getTreeItem(0);
    playlistTreeItem.setPlayableIndex(0);
    numberOfTandas=playlistTreeItem.getChildren().size();
    
    // LOOP THROUGH TANDAS
    for(int j=0; j<playlistTreeItem.getChildren().size(); j++) 
    {
      TandaTreeItem tandaTreeItem = (TandaTreeItem)playlistTreeItem.getChildren().get(j);
      tandaTreeItem.setPlaylistIndex(j);  // set even if tanda is disabled
      if (tandaTreeItem.isDisabled()) continue;
      startTimeInTanda=0;
      tandaInfo = new TandaInfo();
      tandaTrackCounter=0;
      tandaCounter++;
      tandaName = tandaTreeItem.getArtistAndStyle();
      style= tandaTreeItem.getStyle();
      numberOfTracksInTanda=tandaTreeItem.getChildren().size()-1; // minus 1 for cortina
      tandaTreeItem.setPlayableIndex(playableIndex);
      
      tandaInfo.tandaTreeItem=tandaTreeItem;
      tandaInfo.numberOfTracksInTanda=0;
      tandaInfo.tandaNumber=tandaCounter;
      tandaInfo.tandaName=tandaName;
      tandaInfo.numberOfTracksInTanda=numberOfTracksInTanda;
      tandaInfo.dbId=j;
      int checkIndex=tandaCounter+1;
      while (checkIndex<numberOfTandas)
      {
        TandaTreeItem tti = (TandaTreeItem)playlistTreeItem.getChildren().get(checkIndex);
        if (tti.isDisabled()) 
        { 
          checkIndex++; 
        }
        else 
        { 
          tandaInfo.nextTandaName=tti.getArtistAndStyle(); 
          break; 
        }
      }
      
      if (tandaTreeItem.getChildren().size()==0) tandaTreeItem.setPlayableIndex(999); // no tracks in tanda
     // System.out.println("Playlist - generateFlatTracks - tanda: "+tandaInfo.tandaNumber+", "+tandaInfo.tandaName);
     
      // LOOP THROUGH TRACKS
      for(int k=0; k<tandaTreeItem.getChildren().size(); k++)
      {
        BaseTreeItem baseTreeItem = (BaseTreeItem)tandaTreeItem.getChildren().get(k);
        baseTreeItem.setTrackInTree(k);
        
        if (baseTreeItem.isDisabled()) continue;
        
        
        if (baseTreeItem.isPlaying()) playingTrack = trackAddCounter;
        
        if ("tango".equals(baseTreeItem.getTreeType())||"cleanup".equals(baseTreeItem.getTreeType()))
        {
          TrackTreeItem trackTreeItem = (TrackTreeItem)baseTreeItem;
          
          trackTreeItem.setPlayableIndex(playableIndex);
          playableIndex++;
          playlistTrack = new PlaylistTrack();
          playlistTrack.title=trackTreeItem.getValue();
          playlistTrack.album=trackTreeItem.getAlbum();
          playlistTrack.artist=trackTreeItem.getArtist();
          playlistTrack.singer=trackTreeItem.getSinger();
          playlistTrack.comment=trackTreeItem.getComment();
          playlistTrack.path=trackTreeItem.getPath();
          playlistTrack.style=style;
          playlistTrack.delay=trackTreeItem.isDelay();
          tandaTrackCounter++;
          playlistTrack.trackInTanda=tandaTrackCounter;
          playlistTrack.cortina=false;
          playlistTrack.baseTreeItem=trackTreeItem;
          playlistTrack.trackHash=trackTreeItem.getTrackHash();
          playlistTrack.duration=trackTreeItem.getDuration();
          playlistTrack.startTimeInPlaylist=totalPlaylistTime;
          totalPlaylistTime+=playlistTrack.duration;
          playlistTrack.startTimeInTanda=startTimeInTanda;
          startTimeInTanda+=playlistTrack.duration;
          tandaInfo.tandaDuration+=playlistTrack.duration;
          playlistTrack.tandaInfo=tandaInfo;
          trackAddCounter++;
          flatPlaylistTracks.add(playlistTrack);
        }
        else if ("cortina".equals(baseTreeItem.getTreeType()))
        {
          CortinaTreeItem cortinaTreeItem = (CortinaTreeItem)baseTreeItem;
          cortinaTreeItem.setPlayableIndex(playableIndex);
          playableIndex++;
          playlistTrack = new PlaylistTrack();
          playlistTrack.title=cortinaTreeItem.getValue();
          playlistTrack.album = cortinaTreeItem.getAlbum();
          playlistTrack.artist = cortinaTreeItem.getArtist();
          playlistTrack.singer="";
          playlistTrack.comment="";
          playlistTrack.style="Cortina";
          playlistTrack.path=cortinaTreeItem.getPath();
          playlistTrack.cortina=true;
          playlistTrack.baseTreeItem=cortinaTreeItem;
          playlistTrack.trackHash=cortinaTreeItem.getPathHash();
          
          playlistTrack.premade=cortinaTreeItem.getPremade();
          playlistTrack.startValue =cortinaTreeItem.getStart();
          playlistTrack.stopValue  =cortinaTreeItem.getStop();
          playlistTrack.fadein     =cortinaTreeItem.getFadein();
          playlistTrack.fadeout    =cortinaTreeItem.getFadeout();
          // playlistTrack.delay      =cortinaTreeItem.getDelay();
          playlistTrack.original_duration  =cortinaTreeItem.getOriginal_duration();
          playlistTrack.duration=cortinaTreeItem.getDuration();
          
          playlistTrack.tandaInfo=tandaInfo;
          playlistTrack.startTimeInPlaylist=totalPlaylistTime;
          totalPlaylistTime+=playlistTrack.duration;
          tandaInfo.tandaDuration+=playlistTrack.duration;
          playlistTrack.startTimeInTanda=startTimeInTanda;
          startTimeInTanda+=playlistTrack.duration;
          trackAddCounter++;
          flatPlaylistTracks.add(playlistTrack);
          // didn't set or increment tandaTrackCounter or set tandaCounter
        }
        
        //System.out.println(k+", "+baseTreeItem.getValue());
      }
    }
    
    
    // System.out.println("Playlist total duration: "+formatIntoMMSS(totalPlaylistTime));
   //  printFlatList();
   // printTandaInfoList();
  //  collapseTandas();
  }
  
  private void collapseTandas()
  {
    int i=0;
 
    while( true)
    {
      BaseTreeItem ti = (BaseTreeItem)treeView.getTreeItem(i);
      if (ti==null) break;
      if ("tanda".equals(ti.getTreeType()))
      {
        TandaTreeItem tandaTreeItem = (TandaTreeItem)ti;
        tandaTreeItem.setExpanded(false);
      }
      i++;
    }
  }
 
	
  public void printFlatList()
  {
	int i=0;
	System.out.println("print flat playlist");
	System.out.println("playingTrack: "+playingTrack);
	System.out.println("nextTrack: "+nextTrack);
	PlaylistTrack playlistTrack;
    Iterator<PlaylistTrack> it = flatPlaylistTracks.iterator();
    while(it.hasNext())
    {
      playlistTrack = it.next();
      System.out.println(i+") "
      +formatIntoMMSS(playlistTrack.tandaInfo.tandaDuration)+" "
      +playlistTrack.tandaInfo.tandaName+" "
      +playlistTrack.trackInTanda
      +" of "+playlistTrack.tandaInfo.numberOfTracksInTanda+", "
      +formatIntoMMSS(playlistTrack.duration)+", "
      +playlistTrack.title+", "
      +playlistTrack.album+", "
      +playlistTrack.path);
      i++;
    }
  }
  
  public int getFlatPlaylistSize()
  {
    return flatPlaylistTracks.size();
  }
  
  /*
  public void printTandaInfoList()
  {
  int i=0;
  System.out.println("print tanda info list");
  TandaInfo tandaInfo;
    Iterator<TandaInfo> it = tandas.iterator();
    while(it.hasNext())
    {
      tandaInfo = it.next();
      System.out.println(i+") "
      +tandaInfo.tandaNumber+" "
      +tandaInfo.tandaName+" "
      +tandaInfo.numberOfTracksInTanda+", "
      +formatIntoMMSS(tandaInfo.tandaDuration)+", "
      +tandaInfo.nextTandaName);
      i++;
    }
  }
  */
	
  static String formatIntoMMSS(double millisIn)
  {
    millisIn=millisIn/1000;
    int hours = (int)millisIn / 3600,
    remainder = (int)millisIn % 3600,
    minutes = remainder / 60,
    seconds = remainder % 60;
    DecimalFormat sec = new DecimalFormat( "00" );
    DecimalFormat min = new DecimalFormat( "##" );
    DecimalFormat hr = new DecimalFormat( "##" );
  //return ( (minutes < 10 ? "0" : "") + minutes
  //+ ":" + (seconds< 10 ? "0" : "") + seconds );
  
  return hr.format(hours)+":"+min.format(minutes)+":"+sec.format(seconds);

  }
  
  public TreeView<String> getTreeView() { return treeView; }
	
  public void addTanda(String artist, int styleId, String comment)
  {
	  playlistTreeItem.addTanda(artist, styleId, comment);
	  generateFlatList();
  }
  
  public void updateTanda(long tandaId, String artist, int styleId, String comment)
  {
	  playlistTreeItem.updateTanda(tandaId, artist, styleId, comment);
	  generateFlatList();
  }
	
  public int getTandaCount() { return playlistTreeItem.getTandaCount();	}
	
  public TandaTreeItem getSelectedTanda()
  {
	  if (selectedTandaIndex==-1) selectedTandaIndex=0;
	  return getTanda(selectedTandaIndex);
  }
	
  public TandaTreeItem  getTanda(int index)
  {
    TandaTreeItem tandaTreeItem = (TandaTreeItem)playlistTreeItem.getChildren().get(index);
	  //System.out.println("Playlist tanda: "+tandaTreeItem.getArtist());
	  return tandaTreeItem;
	}
  
  
	
  private void setupTreeView(int playlistId) 
	{
	  playlistTreeItem =  Db.getPlaylist(playlistId);
	  treeView = new TreeView<String>(playlistTreeItem);
	  treeView.setMinWidth(300);
	  treeView.setPrefWidth(300);
	  treeView.setMaxWidth(400);
	  
	  treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	 
	  //treeView.getStyleClass().add("playlistTree");
	 // treeView.getStyleClass().add("tree_stylesheet");
	  
	  treeView.getSelectionModel().select(playlistTreeItem);
		treeView.setShowRoot(true);
		ArrayList<TandaTreeItem> tandaTreeItems=null;
		try 
		{
		  tandaTreeItems = Db.getTandaTreeItems(playlistId);
		} catch (ClassNotFoundException | SQLException e) { e.printStackTrace();}
			
		 Iterator<TandaTreeItem> it = tandaTreeItems.iterator();
			
		 if (tandaTreeItems.size()>0)
		 {
		   playlistTreeItem.getChildren().addAll(tandaTreeItems);
		 }
			 
		// treeView.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
		 treeView.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>()
		 {
		   @Override
		   public TreeCell<String> call(TreeView<String> p) 
		   {
			   return new MyCellFactory();
		   }
		  });
			
		 /* 
		  * Detect tree item selected
		  */
		// ChangeListener<TreeItem<String>> treeViewChangeListener = new ChangeListener<TreeItem<String>>() 
		 treeViewChangeListener = new ChangeListener<TreeItem<String>>() 
		 {
		   public void changed(ObservableValue<? extends TreeItem<String>> observableValue, TreeItem<String> oldItem, TreeItem<String> newItem) 
		   {
			   if (newItem!=null)
			   {
			     BaseTreeItem bti = (BaseTreeItem)newItem;
			     setSelectedTreeItems(bti);
			 }
		 }};
							 
		 //treeView.focusedProperty().addListener(treeViewChangeListener);
		// treeView.
		// treeView.setOnMouseClicked(treeViewChangeListener);
		treeView.getSelectionModel().selectedItemProperty().addListener(treeViewChangeListener);
		/*
		treeView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		  
      @Override
      public void changed(ObservableValue<? extends Number> arg0,
              Number arg1, Number arg2) {
          System.out.println("Playlist index change listener");
      }

  });
 */
		//treeView.getSelectionModel().
		// treeView.getFocusModel().focusedItemProperty().addListener(treeViewChangeListener);
		 
		 generateFlatList();
	 }

  private void setSelectedTreeItems(String contextId)
  {
    int tandaIndex=-1;
    int trackIndex=-1;
   
    String[] tokens = contextId.split(",");
    tandaIndex = Integer.parseInt(tokens[0]);
    trackIndex = Integer.parseInt(tokens[1]);
    
    if (tandaIndex==-1) // playlist
    {
      setSelectedTreeItems((BaseTreeItem)playlistTreeItem);
      return;
    }
    else if (trackIndex==-1) // tanda
    {
      setSelectedTreeItems((BaseTreeItem)playlistTreeItem.getChildren().get(tandaIndex));
      return;
    }
    else
    {
      TandaTreeItem tandaTreeItem = (TandaTreeItem)playlistTreeItem.getChildren().get(tandaIndex);
      BaseTreeItem baseTreeItem = (BaseTreeItem)tandaTreeItem.getChildren().get(trackIndex);
      setSelectedTreeItems(baseTreeItem);
    }
    
    
  }
 
   private void setSelectedTreeItems(BaseTreeItem bti)
   {
     selectedBaseTreeItem=bti;
     selectedFlatPlayableIndex=-1;
     selectedTandaIndex=-1;
     selectedTandaTreeItem=null;
     selectedTrackTreeItem=null;
     selectedCortinaTreeItem=null;
     
     String treeType = bti.getTreeType();
     switch(treeType)
     {
       case "playlist":
            selectedTandaTreeItem = playlistTreeItem.getFirstTanda();
            if (selectedTandaTreeItem!=null) selectedTrackTreeItem = selectedTandaTreeItem.getFirstTrack();
            if (selectedTrackTreeItem!=null) selectedFlatPlayableIndex = selectedTrackTreeItem.getPlayableIndex();
            break;
       case "tanda":
            selectedTandaTreeItem = (TandaTreeItem)bti;
            
            selectedTrackTreeItem = selectedTandaTreeItem.getFirstTrack();
            if (selectedTrackTreeItem!=null) selectedFlatPlayableIndex = selectedTrackTreeItem.getPlayableIndex();
            break;
       case "tango":
           selectedTrackTreeItem = (TrackTreeItem)bti;
           selectedTandaTreeItem = selectedTrackTreeItem.getTanda();
           selectedFlatPlayableIndex = selectedTrackTreeItem.getPlayableIndex();
           break;
       case "cleanup":
           selectedTrackTreeItem = (TrackTreeItem)bti;
           selectedTandaTreeItem = selectedTrackTreeItem.getTanda();
           selectedFlatPlayableIndex = selectedTrackTreeItem.getPlayableIndex();
           break;
       case "cortina":
           selectedCortinaTreeItem = (CortinaTreeItem)bti;
           selectedTandaTreeItem = selectedCortinaTreeItem.getTanda();
           selectedFlatPlayableIndex = selectedCortinaTreeItem.getPlayableIndex();
           break;
       default:
         System.out.println("Playlist - unsupported tree type: "+treeType);
     }
     
     if (selectedTandaTreeItem!=null) selectedTandaIndex = selectedTandaTreeItem.getPlaylistIndex();
     
     playlistFocus.set(playlistFocus.get()+1);
     treeView.getSelectionModel().select(bti);
     
     boolean debug=false;
     if (debug)
     {
       System.out.println("Playlist - treeType: "+treeType);
       System.out.println("Tanda index: "+selectedTandaIndex);
       System.out.println("Flat track index: "+selectedFlatPlayableIndex);
       System.out.println("playlist: "+playlistTreeItem);
       System.out.println("tanda: "+selectedTandaTreeItem);
       System.out.println("tanda index: "+selectedTandaIndex);
       System.out.println("track: "+selectedTrackTreeItem);
       System.out.println("cortina: "+selectedCortinaTreeItem);
     }       
   }
  
   private int getTandaNumber(int selectedPlaylistTrack)
   {
     if (selectedPlaylistTrack==999) return numberOfTandas-1;
     if (selectedPlaylistTrack==-1) return 0;
     if (flatPlaylistTracks==null) return 0;
     if (flatPlaylistTracks.size()==0) return 0;
     PlaylistTrack playlistTrack = flatPlaylistTracks.get(selectedPlaylistTrack);
     return playlistTrack.tandaInfo.tandaNumber;
   }
     
     
			
	   private final class MyCellFactory extends TreeCell<String> 
	   {
	     private ContextMenu tandaContextMenu =  new ContextMenu();
	     private ContextMenu trackContextMenu =  new ContextMenu();
	     private ContextMenu playlistContextMenu =  new ContextMenu();
	     private ContextMenu cortinaContextMenu =  new ContextMenu();

	     public MyCellFactory() 
	     {
	       setupTandaContextMenu(tandaContextMenu);
	       setupTrackContextMenu(trackContextMenu);
         setupPlaylistContextMenu(playlistContextMenu);
	       setupCortinaContextMenu(cortinaContextMenu);
	     }
	       
	     @Override
	     public void updateItem(String item, boolean empty) 
	     {
	       super.updateItem(item, empty);

	       if (empty) 
	       {
	         setText(null);
	         setGraphic(null);
	       } 
	       else 
	       {
	         setText(getString());
	         BaseTreeItem bti = (BaseTreeItem)getTreeItem();
	         if (isSelected()) 
	         {
	           TreeItem treeItem = (TreeItem)bti;
	          // System.out.println("selected tree cell: "+treeItem.getValue());
	         }
	         if ("playlist".equals(bti.getTreeType())) 
	         {  
	           playlistContextMenu.setId("-1,-1");
	           setContextMenu(playlistContextMenu);
	           
	         }
	         else if ("tanda".equals(bti.getTreeType())) 
	         {	   
	           if (playlistTreeItem.getTandaPosition((TandaTreeItem)bti)==0) tandaContextMenu.getItems().get(0).setDisable(true);  // disable move up
	           if (playlistTreeItem.getTandaPosition((TandaTreeItem)bti)==playlistTreeItem.getTandaCount()-1) tandaContextMenu.getItems().get(1).setDisable(true); // disable move down
		       
	           
	           tandaContextMenu.setId(((TandaTreeItem)bti).getTandaAndTrackPosition());
		       
		            // set disable checkmark in context menu
		           CheckMenuItem cmi = (CheckMenuItem)tandaContextMenu.getItems().get(3);
		           cmi.setSelected(bti.isDisabled());
		           
		           setContextMenu(tandaContextMenu);
	         }
	         else if ("tango".equals(bti.getTreeType())|| ("cleanup".equals(bti.getTreeType() )))
	         {
	           TrackTreeItem trackTreeItem = (TrackTreeItem)bti;
	           //this.getStyleClass().add("cortinaPlaylistText");
	         
	           int trackCount=((TandaTreeItem)trackTreeItem.getParent()).getTrackCount();
	           int trackPosition=trackTreeItem.getTrackPosition(trackTreeItem);
	           
	           if (trackPosition==0) trackContextMenu.getItems().get(0).setDisable(true);  // disable move up
	           if (trackPosition==trackCount-1) trackContextMenu.getItems().get(1).setDisable(true); // disable move down
	          
	           //Kludge alert
	           trackContextMenu.setId(trackTreeItem.getTandaAndTrackPosition());
	           
	           CheckMenuItem skipTrackMenuItem = (CheckMenuItem)trackContextMenu.getItems().get(4);
	           MenuItem playNextMenuItem = (MenuItem)trackContextMenu.getItems().get(3);
	           CheckMenuItem delayMenuItem = (CheckMenuItem)trackContextMenu.getItems().get(5);
	           
	           skipTrackMenuItem.setSelected(bti.isDisabled());
	           delayMenuItem.setSelected(trackTreeItem.isDelay());
	           playNextMenuItem.setDisable(bti.isDisabled()); // can't play next if disabled
             
	           
	           setContextMenu(trackContextMenu);
	         }
	         else if ("cortina".equals(bti.getTreeType()))
           {
             CortinaTreeItem cortinaTreeItem = (CortinaTreeItem)bti;
             cortinaContextMenu.setId(cortinaTreeItem.getTandaAndTrackPosition());
             setContextMenu(cortinaContextMenu);
           }
	         /*
	         else if ("cleanup".equals(bti.getTreeType()))
           {
             TrackTreeItem cleanupTreeItem = (TrackTreeItem)bti;
             CheckMenuItem cmi = (CheckMenuItem)trackContextMenu.getItems().get(4);
             cmi.setSelected(bti.isDisabled());
             trackContextMenu.setId(cleanupTreeItem.getTandaAndTrackPosition());
             setContextMenu(trackContextMenu);
           }
           */
	         setGraphic(getTreeItem().getGraphic());
	       }
	     }
	      
	     private String getString() 
	     {
	       return getItem() == null ? "" : getItem().toString();
	     }
	   }
	   
	   private void setupTandaContextMenu(final ContextMenu tandaContextMenu)
	   {
	     MenuItem moveUp = new MenuItem("Move Tanda Up"); 
	     MenuItem moveDown = new MenuItem("Move Tanda Down");
	     MenuItem edit = new MenuItem("Edit Tanda Title" );
	     MenuItem delete = new MenuItem("Delete Tanda" );
	     final CheckMenuItem disableItem = new CheckMenuItem("Skip Tanda"); 
	     
	     // don't change the positions of menu items. The cell factory gets them by index
	     tandaContextMenu.getItems().addAll(moveUp, moveDown,  delete, disableItem, edit);
	     
	     
	     tandaContextMenu.setOnShowing(new EventHandler() 
       {
         public void handle(Event t) 
         {
           //System.out.println("Playlist - Tanda Context Menu: "+tandaContextMenu.getId());
           setSelectedTreeItems(tandaContextMenu.getId());
         }
       });
       
	     disableItem.setOnAction(new EventHandler() 
       {
         public void handle(Event t) 
         {
          // System.out.println("Playlist - disable menu: "+disableItem.isSelected());
           disableTanda(disableItem.isSelected()); generateFlatList();}
       });
	     
	     moveUp.setOnAction(new EventHandler() 
	     {
	       public void handle(Event t) 
	       { 
	         playlistTreeItem.moveTandaUp(selectedTandaTreeItem);
	         generateFlatList(); 
	       }
	     });
	     
	     edit.setOnAction(new EventHandler() 
	     {
	       public void handle(Event t) 
	       { 
	    	// System.out.println("selectedTandaTreeItem: "+selectedTandaTreeItem.getDbId()); 
	         new TandaInfoDialog(playlist, selectedTandaTreeItem.getDbId(), selectedTandaIndex);
	         generateFlatList(); 
	       }
	     });
	     
	     moveDown.setOnAction(new EventHandler() 
	     {
	       public void handle(Event t) { playlistTreeItem.moveTandaDown(selectedTandaTreeItem); generateFlatList(); }
	     });
	     
	     delete.setOnAction(new EventHandler() 
	     {
	       public void handle(Event t) { playlistTreeItem.deleteTanda(selectedTandaTreeItem); generateFlatList();  }
	     });
	   } 
	   
	   private void setupPlaylistContextMenu(final ContextMenu playlistContextMenu)
     {
       MenuItem newTanda = new MenuItem("New Tanda"); 
     
       playlistContextMenu.getItems().add(newTanda);
       
       playlistContextMenu.setOnShowing(new EventHandler() 
       {
         public void handle(Event t) 
         {
          // System.out.println("Playlist - Playlist Context Menu: "+playlistContextMenu.getId());
           setSelectedTreeItems(playlistContextMenu.getId());
         }
       });
       newTanda.setOnAction(new EventHandler() 
       {
         public void handle(Event t) 
         {
          // newTandaDialog();   
           new TandaInfoDialog(playlist);	 
         }
       });
      
     } 
     
	   private void setupCortinaContextMenu(final ContextMenu cortinaContextMenu)
	     {
	       MenuItem removeCortina = new MenuItem("Remove Cortina");
	       MenuItem playNext = new MenuItem("Play Next");
	     
	       cortinaContextMenu.getItems().addAll(playNext, removeCortina);
	       
	       cortinaContextMenu.setOnShowing(new EventHandler() 
	       {
	         public void handle(Event t) 
	         {
	           //System.out.println("Playlist - Tanda Context Menu: "+cortinaContextMenu.getId());
	           setSelectedTreeItems(cortinaContextMenu.getId());
	         }
	       });
	       
	       removeCortina.setOnAction(new EventHandler() 
	       {
	         public void handle(Event t) 
	         {
	        	 playlistTreeItem.getTanda(selectedTandaIndex).removeCortina();
	  	       generateFlatList();
	         }
	       });
	       playNext.setOnAction(new EventHandler() 
	       {
	         public void handle(Event t) 
	         {
	           setNextTrackToPlay();
	         }
	     });
	      
	     } 
	       
	   private void setupTrackContextMenu(final ContextMenu trackContextMenu)
	   {
	     
	     MenuItem moveUp = new MenuItem("Move Track Up"); 
	     MenuItem moveDown = new MenuItem("Move Track Down");
	     MenuItem delete = new MenuItem("Remove Track");
	     MenuItem playNext = new MenuItem("Play Next");
	     final CheckMenuItem disableItem = new CheckMenuItem("Skip Track"); 
	     final CheckMenuItem delayItem = new CheckMenuItem("Add 3 sec silence"); 
	     
	     trackContextMenu.getItems().addAll(moveUp, moveDown, delete, playNext, disableItem, delayItem);
	     trackContextMenu.setConsumeAutoHidingEvents(true);
	     
	     
	   
	     trackContextMenu.setOnShowing(new EventHandler() 
       {
         public void handle(Event t) 
         {
           //System.out.println("Playlist - Track Context Menu: "+trackContextMenu.getId());
           setSelectedTreeItems(trackContextMenu.getId());
         }
       });
	     
	     playNext.setOnAction(new EventHandler() 
       {
         public void handle(Event t) { setNextTrackToPlay(); }
       });
	     
	     delayItem.setOnAction(new EventHandler() 
       {
         public void handle(Event t) 
         { 
           delayTrack(delayItem.isSelected());
           generateFlatList();
         }
       });
       
	     
	     disableItem.setOnAction(new EventHandler() 
       {
         public void handle(Event t) 
         { 
           disableTrack(disableItem.isSelected()); 
           generateFlatList();
         }
       });
	   

	     moveUp.setOnAction(new EventHandler() 
	     {
	       public void handle(Event t) 
	       { 
	        /*
	         PlaylistTrack playlistTrack = getSelectedPlaylistTrack();
	         TandaTreeItem tandaTreeItem = playlistTrack.tandaInfo.tandaTreeItem;
	         tandaTreeItem.moveTrackUp(playlistTrack.trackInTanda-1); 
	        */
	         if (selectedTandaTreeItem==null) return;
	         if (selectedTrackTreeItem==null) return;
	         selectedTandaTreeItem.moveTrackUp(selectedTrackTreeItem);
	         generateFlatList();
	       }
	         
	     });
	   
	     moveDown.setOnAction(new EventHandler() 
	     {
	       public void handle(Event t) 
	       { 
	         /*
	         PlaylistTrack playlistTrack = getSelectedPlaylistTrack();
           TandaTreeItem tandaTreeItem = playlistTrack.tandaInfo.tandaTreeItem;
           tandaTreeItem.moveTrackDown(playlistTrack.trackInTanda-1); 
           */
           selectedTandaTreeItem.moveTrackDown(selectedTrackTreeItem);
           generateFlatList();
	       }
	     });
	   
	     delete.setOnAction(new EventHandler() 
	     {
	       public void handle(Event t) 
	       { 
	         /*
	         PlaylistTrack playlistTrack = getSelectedPlaylistTrack();
           TandaTreeItem tandaTreeItem = playlistTrack.tandaInfo.tandaTreeItem;
           tandaTreeItem.deleteTrack(playlistTrack.trackInTanda-1); 
           */
	         selectedTandaTreeItem.deleteTrack(selectedTrackTreeItem);
           generateFlatList(); }
	     });
	   } 

	   private void newTandaDialog() 
	   {
	     final ComboBox comboBox = new ComboBox();
	     
	     
	     final Text alist = new Text("A List");
	     Text blist =   new Text("B List");
	     Text clist =   new Text("C List");
	     
	     alist.setFill(Color.BLACK);
	     alist.setFont(Font.font("Serif", 18));
	     blist.setFill(Color.BLACK);
	     blist.setFont(Font.font("Serif", 18));
	     clist.setFill(Color.BLACK);
	     clist.setFont(Font.font("Serif", 18));
	     
	     final TextField artistOverride = new TextField("");
	    
	     comboBox.getItems().add(alist);
	     comboBox.getItems().addAll(SharedValues.artistsA);
	     comboBox.getItems().add(blist);
	     comboBox.getItems().addAll(SharedValues.artistsB);
	     comboBox.getItems().add(clist);
	     comboBox.getItems().addAll(SharedValues.artistsC);
	     
	   
	     comboBox.setOnAction(new EventHandler<ActionEvent>() 
	     {
	       public void handle(ActionEvent actionEvent) 
	       {
	         //System.out.println("Combobox action");
	         ArtistX ax = (ArtistX)comboBox.getValue();
	          artistOverride.setText(ax.getLeader()); 
	       }
	    });
	    
	    
	     final RadioButton rb1 = new RadioButton("Tango");
	     final RadioButton rb2 = new RadioButton("Vals");
	     final RadioButton rb3 = new RadioButton("Milonga");
	     final RadioButton rb4 = new RadioButton("Alternative");
	     final RadioButton rb5 = new RadioButton("Mixed");
	     final RadioButton rb6 = new RadioButton("Cleanup");
	     
	     rb1.setId(""+SharedValues.TANGO);
	     rb2.setId(""+SharedValues.VALS);
	     rb3.setId(""+SharedValues.MILONGA);
	     rb4.setId(""+SharedValues.ALTERNATIVE);
	     rb5.setId(""+SharedValues.MIXED);
	     rb6.setId(""+SharedValues.CLEANUP);
	     
	     final ToggleGroup styleGroup = new ToggleGroup();
	     
	     rb1.setToggleGroup(styleGroup);
	     rb2.setToggleGroup(styleGroup);
	     rb3.setToggleGroup(styleGroup);
	     rb4.setToggleGroup(styleGroup);
	     rb5.setToggleGroup(styleGroup);
	     rb6.setToggleGroup(styleGroup);
	        
	     rb1.setSelected(true);

	     final VBox styleBox = new VBox();
	     styleBox.getChildren().addAll(rb1,rb2,rb3,rb4,rb5,rb6);
	     final TextField comment = new TextField();
	     
         final Stage myDialog = new Stage();
         myDialog.initModality(Modality.APPLICATION_MODAL);
         
         Button okButton = new Button("SAVE");
         okButton.setOnAction(new EventHandler<ActionEvent>()
         {
           public void handle(ActionEvent arg0) 
           {
	           //String artist = comboBox.getSelectionModel().getSelectedItem().toString();
	           int styleId = 0;
	           String selectedStr=styleGroup.getSelectedToggle().toString();
	           int i = selectedStr.indexOf("id=");
	           String numStr = selectedStr.substring(i+3, i+4);
	           try 
	           {
	             styleId= Integer.parseInt(numStr);
	           } catch (Exception e) {}
	           addTanda(artistOverride.getText(), styleId, comment.getText());
	             myDialog.close();
           }});
       
         Text tandaLabel = new Text("Orchestra: ");
         tandaLabel.setFont(Font.font("Serif", 20));
         
         
         GridPane gridPane = new GridPane();
         //gridPane.setGridLinesVisible(true);
         gridPane.setPadding(new Insets(10, 10, 10, 10));
         gridPane.setVgap(2);
         gridPane.setHgap(5);
         gridPane.add(tandaLabel, 0, 0);
         gridPane.add(comboBox, 1, 0);
         gridPane.add(artistOverride, 1, 1);
         //gridPane.add(new Text("Style"), 0, 1);
         gridPane.add(styleBox, 0, 1);
         gridPane.add(new Label("Comment: "), 0, 3);
         gridPane.add(comment, 1, 3);
         gridPane.add(okButton, 1, 4);
         
         Scene myDialogScene = new Scene(gridPane, 300, 200);
         myDialog.setScene(myDialogScene);
         myDialog.show();
	   }

    public int getNextTrack()
    {
      return nextTrack;
    }

    public void setNextTrack(int nextTrack)
    {
      this.nextTrack = nextTrack;
    }

    public int getPlayingTrack()
    {
      return playingTrack;
    }
    
    public PlaylistTrack getPlayingPlaylistTrack()
    {
      if ((playingTrack<0)||(flatPlaylistTracks==null)||(flatPlaylistTracks.size()==0))
      {
        System.out.println("Playlist-getPlayingPlaylistTrack() unexpected null");
        System.out.println("Playlist-playingTrack: "+playingTrack);
        if (flatPlaylistTracks==null) System.out.println("Playlist-flatPlaylistTracks is null ");
        else System.out.println("Playlist-flatPlaylistTracks size: "+flatPlaylistTracks.size());
        return null;
      }
    
      return flatPlaylistTracks.get(playingTrack);
    }
    
    public PlaylistTrack getFirstPlaylistTrack()
    {
      if (flatPlaylistTracks==null) return null;
      if (flatPlaylistTracks.size()<1) return null;
      return flatPlaylistTracks.get(0);
    }

    public PlaylistTrack getSelectedPlaylistTrack()
    {
      if (selectedFlatPlayableIndex==-1) return null;
      return flatPlaylistTracks.get(selectedFlatPlayableIndex);
    }
    
    public void setPlayingTrack(int playingTrack)
    {
      this.playingTrack = playingTrack;
      PlaylistTrack playlistTrack= getTrack(playingTrack);
      playlistTrack.baseTreeItem.setPlayingImage(true);
      playlistTrack.playing=true;
     // timeLeftProperty.set(getTimeLeft());
    }
   
    /*
   private double getTimeLeft()
   {
     
     double remainingPlaylistTime=0;
     for(int i=playingTrack; i< flatPlaylistTracks.size(); i++)
     {
       remainingPlaylistTime+=flatPlaylistTracks.get(i).duration;
     }
     
     System.out.println("Playlist - remaining playlist time: "+playingTrack+", "+remainingPlaylistTime);
    return remainingPlaylistTime;
   }
*/
   
   
   public String getPlayingArtist()
   {
	 return flatPlaylistTracks.get(playingTrack).artist; 
   }
   
   
   public String getPlayingTitle()
   {
	   return flatPlaylistTracks.get(playingTrack).title; 
   }
   
   public double getStartTimeInPlaylist()
   {
     return flatPlaylistTracks.get(playingTrack).startTimeInTanda; 
   }
   
  
   
   public String getPlayingTandaProgress()
   {
     PlaylistTrack playlistTrack = flatPlaylistTracks.get(playingTrack);
     String progress=playlistTrack.trackInTanda+" of "+playlistTrack.tandaInfo.numberOfTracksInTanda;
     //System.out.println("Playlist - progress: "+progress);
     return  progress;
   }
   
   
   
   public String getNextTandaInfo()
   {
	   return flatPlaylistTracks.get(playingTrack).tandaInfo.nextTandaName; 
   }
	 
   public boolean isCortina()
   {
	 return  flatPlaylistTracks.get(playingTrack).cortina; 
   }

  public double getTotalPlaylistTime()
  {
    return totalPlaylistTime;
  }
}
