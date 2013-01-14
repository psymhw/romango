package tangodj;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import data.ItunesTrackData;
import data.PlaylistData;
import data.iTunesData;
import data.iTunesParser;
 
public class TangoDJ extends Application 
{
    PlaylistData pd=null;
	String path=null;
	String destFileName;
	DecimalFormat nf = new DecimalFormat("00");
	
	//String playlist="";
	iTunesData data = new iTunesData();
	iTunesParser parser;
	
	VBox vbox;
	MediaControl mc;
	 private static final int DOUBLE_CLICK_WAIT_TIME = 400 ; // milliseconds
	 private boolean boxDoubleClicked ;
    
    private TableView<PlaylistTitle> allPlaylistsTable = new TableView<PlaylistTitle>();
    private TableView<Track> trackTable = new TableView<Track>();
    private  ObservableList<PlaylistTitle> playlistData= FXCollections.observableArrayList(); 
   // private ArrayList<TrackRow> trackRows = new ArrayList<TrackRow>();
   // private ArrayList<Tanda> tandas;
    
    private Playlist playlist;
   
    
    final ProgressBar progress = new ProgressBar();
    private ChangeListener<Duration> progressChangeListener;
    final Label currentlyPlaying = new Label();
    private Text currentArtist = new Text();
    final Label currentTrackName = new Label();
    final Label nextArtist = new Label();
    final Label nextTrackName = new Label();
    private boolean listPlaying=false;
    
    private MediaView mediaView=null;
    private InfoWindow infoWindow;
    
    Group trackGroup = new Group();
    
    Rectangle redBox = new Rectangle(100, 100);
    Equalizer eq;
    long lastPlaylistRequestTime=System.currentTimeMillis();
    ScrollPane scrollPane;
    GridPane trackGrid;
    int numberOfTracksInPlaylist=0;  // total number in playlist
   // int nowPlayingIndex=0;
   // int selectedIndex=0;
    
    final Button skip = new Button("Skip");
    final Button play = new Button("Play");
    final Button stop = new Button("Stop");
    final Button info = new Button("Info Window");
    
    public static void main(String[] args) 
    {
        launch(args);
    }
 
  
   public void start(Stage stage) 
   {
    	redBox.setFill(Color.RED);
    	loadFonts();
    	parser = new iTunesParser(data);
		parser.parseFile();
		
    	getPlaylistData();
    	
    	progress.setMaxWidth(300);
        Scene scene = new Scene(new Group());
        stage.setTitle("Tango DJ");
        stage.setWidth(950);
        stage.setHeight(590);
 
        final Label label = new Label("Tango DJ");
        label.setFont(new Font("Arial", 20));
         setupAllPlaylistsTable();
      //  setupTrackTable();
 
        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        final HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.setPadding(new Insets(10, 0, 0, 10));
        
        hbox.getChildren().add(allPlaylistsTable);
       // hbox.getChildren().add(trackTable);
        hbox.getChildren().add(getTrackGrid());
       
        
     
        
        vbox.getChildren().addAll(label, hbox);
        ((Group) scene.getRoot()).getChildren().add(vbox);
        
        final URL stylesheet = getClass().getResource("style.css");
        scene.getStylesheets().add(stylesheet.toString());
        stage.setScene(scene);
        
        setupButtonActions();
        progress.setProgress(0);
       // mediaView = new MediaView(createMediaPlayer(TangoDJ.class.getResource("/resources/sounds/Who1.mp3").toExternalForm(), false));
        HBox hb = HBoxBuilder.create().spacing(10).alignment(Pos.CENTER).children(info, skip, play, stop, progress).build();
        vbox.getChildren().add(hb);
  
        stop.setDisable(true);
        play.setDisable(true);
        skip.setDisable(true);
        info.setDisable(true);
        stage.show();
    }

	private void setupButtonActions() 
	{
	  // Skip
	  skip.setOnAction(new EventHandler<ActionEvent>() 
	  {
        public void handle(ActionEvent actionEvent) 
        {
          stopTrack();
          if ((playlist.playingTrack+1)==numberOfTracksInPlaylist) return;
                       
          incrementNowPlaying();
          playTrack();
        }
      });
        
      // info window
      info.setOnAction(new EventHandler<ActionEvent>() 
      {
        public void handle(ActionEvent actionEvent) 
        {
          showInfoWindow();
        }
      });
        

      // Play 
      play.setOnAction(new EventHandler<ActionEvent>() 
      {
        public void handle(ActionEvent actionEvent) 
        {
          playlist.playingTrack=playlist.selectedTrack;
          playlist.setPlayingIndicator();
          play.setDisable(true);
          stop.setDisable(false);
          skip.setDisable(false);
          info.setDisable(false);
          playTrack();
        }
      });

      // Stop
      stop.setOnAction(new EventHandler<ActionEvent>() 
      {
        public void handle(ActionEvent actionEvent) 
        {
          play.setDisable(false);
          stop.setDisable(true);
          skip.setDisable(true);
          info.setDisable(true);

          stopTrack();
        }
      });
	}



	private void setupAllPlaylistsTable() 
	{
	  allPlaylistsTable.setEditable(false);
      TableColumn idCol = new TableColumn("ID");
      idCol.setMinWidth(100);
      idCol.setCellValueFactory(new PropertyValueFactory<PlaylistTitle, Integer>("index"));
        
      TableColumn nameCol = new TableColumn("Playlist Name");
      nameCol.setMinWidth(200);
      nameCol.setCellValueFactory(new PropertyValueFactory<PlaylistTitle, String>("name"));
       
      EventHandler <MouseEvent>click = new EventHandler<MouseEvent>() 
      {
	    public void handle(MouseEvent ev) 
	    {
		  int index = ((TableCell)ev.getSource()).getIndex();
		  getTrackRows(index);
		}
      };
        
      GenericCellFactory cellFactory = new GenericCellFactory(click);
      
      nameCol.setCellFactory(cellFactory);
        
      allPlaylistsTable.setItems(playlistData);
      allPlaylistsTable.getColumns().addAll(idCol, nameCol);
	}
   
	
	private boolean deBounce()
	{
		long currentTime=System.currentTimeMillis();
		if (currentTime < (lastPlaylistRequestTime+500)) 
		{	
			System.out.println("debounced");
		    return true;
		}
		lastPlaylistRequestTime=currentTime;
		return false;
	}
	
	private void playTrack()
	{
	  final MediaPlayer player = createMediaPlayer(playlist.getTrackPath(playlist.playingTrack), false);
	  player.setOnEndOfMedia(new Runnable() 
	  {
        public void run() 
        {
          if (playlist.isDone()) return;
          if (playlist.selectedTrack==playlist.playingTrack) incrementSelected();
          incrementNowPlaying();
          playTrack();
        }
      });    
	   
	  mediaView = new MediaView(player);
      if (eq!=null) vbox.getChildren().remove(3);
       
      setInfoWindow();
       
      eq = new Equalizer(player);
      vbox.getChildren().add(eq.getGridPane());
      progress.setProgress(0);
      progressChangeListener = new ChangeListener<Duration>() 
      {
        public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
        progress.setProgress(1.0 * player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis());
      }};
      player.currentTimeProperty().addListener(progressChangeListener);
     
      listPlaying=true;
      playlist.setPlayingIndicator();
      mediaView.getMediaPlayer().play();
	}
	
    private void stopTrack() 
    {
       if (!listPlaying) return;	
	   final MediaPlayer curPlayer = mediaView.getMediaPlayer();
	   curPlayer.currentTimeProperty().removeListener(progressChangeListener);
	   curPlayer.stop();
	   playlist.setNotPlayingIndicator();
	   //selectedIndex=0;
	   vbox.getChildren().remove(3);
	   eq=null;
	   listPlaying=false;
    }   
	
    private void incrementSelected()
    {
      System.out.println("increment selected");
      playlist.setNotSelectedIndicator(playlist.selectedTrack);
      playlist.selectedTrack++;
      playlist.setSelectedIndicator(playlist.selectedTrack);
    }
    
    private void incrementNowPlaying()
    {
      System.out.println("selected: "+playlist.selectedTrack);
      System.out.println("now Playing: "+playlist.playingTrack);
      
      playlist.setNotPlayingIndicator();
      
      if (playlist.selectedTrack==playlist.playingTrack) 
      {
    	incrementSelected();
    	playlist.playingTrack++;
      }
      else 
      {
    	playlist.playingTrack=playlist.selectedTrack;
      }
     
      
    }
    
    
    
    private MediaPlayer createMediaPlayer(String path, boolean autoPlay)
    {
      final String thisPath=path;
      Media  media = new Media(path);
	  final MediaPlayer mp = new MediaPlayer(media);
	  mp.setOnError(new Runnable() 
	  {
	    public void run() 
	    {
	      System.out.println("Media error occurred: " + mp.getError());
	      System.out.println("path: " + thisPath);
	    }});
	    mp.setAutoPlay(autoPlay);
	    return mp;
    }
	
    
    
   void getPlaylistData()
   {
	   for(int i=0; i<data.playlists.length; i++)
		{	
		  pd = 	data.playlists[i];
	      playlistData.add(new PlaylistTitle(i+1, pd.name));
		}
   }
   
   
    private void cancelListPlay()
    {
    	if (listPlaying)
    	{
    	   final MediaPlayer curPlayer = mediaView.getMediaPlayer();
           curPlayer.currentTimeProperty().removeListener(progressChangeListener);
           curPlayer.stop();
           vbox.getChildren().remove(3);
           listPlaying=false;
    	}
    }
    
	
	 private void setInfoWindow()
	 {
	   TrackRow row = 	playlist.getPlayingTrack(); 
	   String curArtist = row.getArtistName();
	   currentArtist.setText(curArtist);
	   currentTrackName.setText(row.name.getText());
	    if (infoWindow!=null) infoWindow.update(currentArtist.getText(), currentTrackName.getText(), row.groupingName);
	  }
	
	private void showInfoWindow()
    {
		TrackRow row = 	playlist.getPlayingTrack(); 
       infoWindow = new InfoWindow(currentArtist.getText(), currentTrackName.getText(), row.groupingName);
        
    }
	
	private void loadFonts()
	  {
		
		Font.loadFont(TangoDJ.class.getResource("/resources/fonts/Carousel.ttf").toExternalForm(), 10  );
		Font.loadFont(TangoDJ.class.getResource("/resources/fonts/Anagram.ttf").toExternalForm(), 10  );
		Font.loadFont(TangoDJ.class.getResource("/resources/fonts/Carrington.ttf").toExternalForm(), 10  );
		Font.loadFont(TangoDJ.class.getResource("/resources/fonts/DEFTONE.ttf").toExternalForm(), 10  );
		Font.loadFont(TangoDJ.class.getResource("/resources/fonts/EastMarket.ttf").toExternalForm(), 10  );
		Font.loadFont(TangoDJ.class.getResource("/resources/fonts/england.ttf").toExternalForm(), 10  );
		Font.loadFont(TangoDJ.class.getResource("/resources/fonts/FFF_Tusj.ttf").toExternalForm(), 10  );
		
	  }
	
	
	
	private ScrollPane getTrackGrid()
	{
	  scrollPane = new ScrollPane();
	  EventHandler <MouseEvent>mouseDownHandler = new EventHandler<MouseEvent>() 
	  {
        public void handle(MouseEvent event)  
        { 
          double trackIndex=0;
          trackIndex=((event.getY()+(scrollPane.getVvalue()*(trackGrid.getHeight()-scrollPane.getHeight())))  /22.188);
          System.out.println("down trackIndex: "+Math.round(trackIndex));
        }};
          
        EventHandler <MouseEvent>mouseReleasedHandler = new EventHandler<MouseEvent>() 
        {
          public void handle(MouseEvent event)  
          { 
            double trackIndex=0;
        	trackIndex=((event.getY()+(scrollPane.getVvalue()*(trackGrid.getHeight()-scrollPane.getHeight())))  /22.188);
        	System.out.println("up trackIndex: "+Math.round(trackIndex));
          }
        };
  
	    scrollPane.setOnMousePressed(mouseDownHandler);
	    scrollPane.setOnMouseReleased(mouseReleasedHandler);
	    scrollPane.getHvalue();
	    scrollPane.setPrefWidth(600);
	    scrollPane.setFitToHeight(true);
	    scrollPane.setContent(trackGroup);
	 
	    return scrollPane;
	}
	
	/*
	private void getTrackRows(int index)
	{
	  stopTrack();
      stop.setDisable(true);
      play.setDisable(true);
      skip.setDisable(true);
      info.setDisable(true);
	  if (trackGroup.getChildren().size()>0) 
	  {	  
		trackGroup.getChildren().remove(0);
		trackRows.clear();
	  }
	  
	  trackGrid = new GridPane();
	  trackGrid.setPadding(new Insets(10, 10, 10, 10));
	  trackGrid.setVgap(0);
	  trackGrid.setHgap(0);
	  
	  PlaylistData pd = data.playlists[index];
	  ItunesTrackData td = null;
	  String path=null;
	   
	  for(int j=0; j<pd.tracks.length; j++)
	  {
	    td=data.tracks[pd.tracks[j]];
		path=td.path.substring(16);
		try 
		{
  		  path = URLDecoder.decode(path,"UTF-8");
  		} catch (Exception e) { e.printStackTrace(); }
		File temp = new File(path);
		path=temp.toURI().toString();
		 
		trackRows.add(new TrackRow(td.name, td.artist, path, td.grouping, td.time, j));
	  }
	  
	  TrackRow tr = null;
	  int row=0;
	  numberOfTracksInPlaylist=0;
	  Iterator<TrackRow> it = trackRows.iterator();
	  while(it.hasNext())
	  {
	    tr=it.next(); 
		trackGrid.add(tr.indicator, 0, row);
		trackGrid.add(tr.index, 1, row);
		trackGrid.add(tr.grouping, 2, row);
		trackGrid.add(tr.artist, 3, row);
		trackGrid.add(tr.name, 4, row);
		numberOfTracksInPlaylist++;  
		row++;
	   }
	   
	   EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() 
	   {
	     public void handle(MouseEvent event)  { ShowIndex(); }
	   };
		
	   trackGrid.setOnMouseClicked(bHandler);
	   trackGroup.getChildren().add(trackGrid);
       trackRows.get(0).setSelectedIndicatorBall();
	   
       playlist.playingTrack=0;
       selectedIndex=0;
	   play.setDisable(false);
	 }
	*/
	
	
	private void getTrackRows(int index)
	{
	  stopTrack();
      stop.setDisable(true);
      play.setDisable(true);
      skip.setDisable(true);
      info.setDisable(true);
	  
	  PlaylistData pd = data.playlists[index];
	  ItunesTrackData td = null;
	  String path=null;
	  String lastGrouping="";
	  String lastArtist="";
	  Tanda tanda=null;
	//  tandas = new ArrayList<Tanda>();
	  int tandaNumber=0;
	  int tandaTrackNumber=0;
	  
	  playlist = new Playlist();
	  	  	   
	  // populate Tandas and TrackRows
	  for(int j=0; j<pd.tracks.length; j++)
	  {
	    td=data.tracks[pd.tracks[j]];
		path=td.path.substring(16);
		try 
		{
  		  path = URLDecoder.decode(path,"UTF-8");
  		} catch (Exception e) { e.printStackTrace(); }
		File temp = new File(path);
		path=temp.toURI().toString();
		
		if (!lastArtist.equals(td.artist))
		{
		  if (td.grouping!=null)
		  {
		    if (!"cortina".equalsIgnoreCase(td.grouping))
			{
		      if (!"padding".equalsIgnoreCase(td.grouping))
			  {
		    	if (tanda!=null) playlist.addTanda(tanda);
		        tanda = new Tanda(td.artist, td.grouping);
		        tandaTrackNumber=0;
		        tandaNumber++;
		        lastArtist=td.artist;
			  }
			}
		  }
		}
		
		if (tanda==null) tanda = new Tanda("Artist", "Group");
		tanda.addTrackRow( new TrackRow(td.name, td.artist, path, td.grouping, td.time, j, tandaNumber, tandaTrackNumber));
		tandaTrackNumber++;
	  }
	  
	  playlist.addTanda(tanda);
	  populateTrackGrid();
	}
	
	
	
	private void ShowIndex()
	{
		playlist.setNotSelectedIndicator(playlist.selectedTrack);
	  playlist.selectedTrack=TrackRow.getPindex();
	  playlist.setSelectedIndicator(playlist.selectedTrack);
	}
	
	private void populateTrackGrid()
	{
	  Tanda tanda;
	  ArrayList<TrackRow> trs;
	  TrackRow tr;
	  int row=0;
	  numberOfTracksInPlaylist=0;
	  
	  if (trackGroup.getChildren().size()>0) 
	  {	  
		trackGroup.getChildren().remove(0);
		//playlist.getTrackRows().clear();
	  }
	  
	  trackGrid = new GridPane();
	  trackGrid.setPadding(new Insets(10, 10, 10, 10));
	  trackGrid.setVgap(0);
	  trackGrid.setHgap(0);

	  Iterator<Tanda> it = playlist.getTandas().iterator();
	  while(it.hasNext())
	  {
		tanda = it.next();
		
		//System.out.println(tanda.artist.lastName+" "+tanda.group);
		trs=tanda.getTrackRows();
		Iterator<TrackRow> itx = trs.iterator();
		while(itx.hasNext())
		{
		  tr = itx.next();
		  //trackRows.add(tr);
		  trackGrid.add(tr.indicator, 0, row);
		  trackGrid.add(tr.index, 1, row);
		  trackGrid.add(tr.grouping, 2, row);
		  trackGrid.add(tr.artist, 3, row);
		  trackGrid.add(tr.name, 4, row);
		  numberOfTracksInPlaylist++;  
		  row++;

		  //System.out.println("   "+tr.title);
		}
		
	  }  
	  
	  EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() 
	  {
	    public void handle(MouseEvent event)  { ShowIndex(); }
	  };
				
	  trackGrid.setOnMouseClicked(bHandler);
	  trackGroup.getChildren().add(trackGrid);
	  playlist.setSelectedIndicator(0);
			   
	  play.setDisable(false);
	}

	
} 