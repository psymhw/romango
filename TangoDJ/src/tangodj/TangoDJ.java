package tangodj;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.EqualizerBand;
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
//	private static String dbURL = "jdbc:derby:tangoDj;create=true;user=rick;password=smegma";
 //   private static String tableName = "restaurants";
    // jdbc Connection
//    private static Connection conn = null;
//    private static Statement stmt = null;
    
    PlaylistData pd=null;
	//TrackData td = null;
	String path=null;
	String destFileName;
	DecimalFormat nf = new DecimalFormat("00");
	
	String playlist="";
	iTunesData data = new iTunesData();
	iTunesParser parser;
	
	VBox vbox;
	MediaControl mc;
	 private static final int DOUBLE_CLICK_WAIT_TIME = 400 ; // milliseconds
	 private boolean boxDoubleClicked ;
	
	
    
    private TableView<Playlist> allPlaylistsTable = new TableView<Playlist>();
    private TableView<Track> trackTable = new TableView<Track>();
    
    private  ObservableList<Playlist> playlistData= FXCollections.observableArrayList(); 
   // private  ObservableList<Track> trackData= FXCollections.observableArrayList(); 
    
    private ArrayList<TrackRow> trackRows = new ArrayList<TrackRow>();
   
    
    final ProgressBar progress = new ProgressBar();
    private ChangeListener<Duration> progressChangeListener;
    final Label currentlyPlaying = new Label();
    private Text currentArtist = new Text();
    final Label currentTrackName = new Label();
    final Label nextArtist = new Label();
    final Label nextTrackName = new Label();
    private boolean listPlaying=false;
    
    private MediaView mediaView=null;
    //private List<MediaPlayer> players;
    private InfoWindow infoWindow;
    
    Group trackGroup = new Group();
    
    Rectangle redBox = new Rectangle(100, 100);
    Equalizer eq;
    long lastPlaylistRequestTime=System.currentTimeMillis();
    ScrollPane scrollPane;
    GridPane trackGrid;
    int numberOfTracksInPlaylist=0;  // total number in playlist
    int nowPlayingIndex=0;
    int selectedIndex=0;
    
    final Button skip = new Button("Skip");
    final Button play = new Button("Play");
    final Button stop = new Button("Stop");
    final Button info = new Button("Info Window");
    
    /*
    private static final double START_FREQ = 250.0;
    private static final int BAND_COUNT = 7;
    
    private SpectrumBar[] spectrumBars;
    private SpectrumListener spectrumListener;
    */
       
    public static void main(String[] args) 
    {
        launch(args);
    }
 
    @Override
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
        stage.setHeight(810);
 
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
        
       // mediaView = new MediaView(createMediaPlayer(TangoDJ.class.getResource("/resources/sounds/Who1.mp3").toExternalForm(), false));
        HBox hb = HBoxBuilder.create().spacing(10).alignment(Pos.CENTER).children(info, skip, play, stop, progress).build();
        vbox.getChildren().add(hb);
  
        
        stage.show();
    }

	private void setupButtonActions() 
	{
		skip.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) 
            {
              stopTrack();
              if ((nowPlayingIndex+1)==numberOfTracksInPlaylist) return;
              if (selectedIndex==nowPlayingIndex) incrementSelected();
             
              incrementNowPlaying();

              playTrack();
            }
          });
        
        // info window
        info.setOnAction(new EventHandler<ActionEvent>() {
          @Override public void handle(ActionEvent actionEvent) {
            showInfoWindow();
          }
        });
        

        // allow the user to play or pause a track.
        play.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent actionEvent) 
         {
        	nowPlayingIndex=selectedIndex;
        	play.setDisable(true);
        	playTrack();
        	
         }
        });

        stop.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) 
            {
            	play.setDisable(false);
              stopTrack();
             // vbox.getChildren().remove(2);
            }

			
			
          });
	}



	private void setupAllPlaylistsTable() 
	{
		allPlaylistsTable.setEditable(true);
 
        TableColumn idCol = new TableColumn("ID");
        idCol.setMinWidth(100);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Playlist, Integer>("index"));
        
        TableColumn nameCol = new TableColumn("Playlist Name");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Playlist, String>("name"));
       
        EventHandler <MouseEvent>click = new EventHandler<MouseEvent>() {
         
			@Override
			public void handle(MouseEvent ev) {
				
				// TODO Auto-generated method stub
				int index = ((TableCell)ev.getSource()).getIndex();
				// System.out.println("Mouse Event: "+index);
				// setPlaylistTracks(index);
				 getTrackRows(index);
			}
        };
        
        GenericCellFactory cellFactory = new GenericCellFactory(click);
      
        nameCol.setCellFactory(cellFactory);
        
        allPlaylistsTable.setItems(playlistData);
        allPlaylistsTable.getColumns().addAll(idCol, nameCol);
	}
   
    
	
	private void playList2()
	{
		if (deBounce()) return;
		cancelListPlay();
		listPlaying=true;
		
        
        //nowPlayingIndex=0;
        //numberOfTracks=players.size();

        playTrack();

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
	   
	   final MediaPlayer player = createMediaPlayer(trackRows.get(nowPlayingIndex).path, false);
	   player.setOnEndOfMedia(new Runnable() {
           public void run() {
                              // nowPlayingIndex++;
        	                   if ((nowPlayingIndex+1)==numberOfTracksInPlaylist) return;
        	                   if (selectedIndex==nowPlayingIndex) incrementSelected();
        	                  
                               incrementNowPlaying();
                               playTrack();
                             }
       });    
	   
	   mediaView = new MediaView(player);
	   clearNowPlayingIndicatorBall();
       trackRows.get(nowPlayingIndex).setNowPlayingIndicatorBall();
       if (eq!=null) 
       {
    	 vbox.getChildren().remove(3);
       }
       
       setInfoWindow();
       
       eq = new Equalizer(player);
       vbox.getChildren().add(eq.getGridPane());
       progress.setProgress(0);
       progressChangeListener = new ChangeListener<Duration>() {
          public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
           progress.setProgress(1.0 * player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis());
         }
       };
       player.currentTimeProperty().addListener(progressChangeListener);
     
       listPlaying=true;
       mediaView.getMediaPlayer().play();
	}
	
    private void stopTrack() 
    {
       if (!listPlaying) return;	
	   final MediaPlayer curPlayer = mediaView.getMediaPlayer();
	   curPlayer.currentTimeProperty().removeListener(progressChangeListener);
	   curPlayer.stop();
	   trackRows.get(nowPlayingIndex).setNotPlayingIndicatorBall();
	   selectedIndex=0;
	   vbox.getChildren().remove(3);
	   eq=null;
	   listPlaying=false;
    }   
	
    private void incrementSelected()
    {
      trackRows.get(selectedIndex).setNotSelectedIndicatorBall();
      selectedIndex++;
      trackRows.get(selectedIndex).setSelectedIndicatorBall();
    }
    
    private void incrementNowPlaying()
    {
      trackRows.get(nowPlayingIndex).setNotPlayingIndicatorBall();
      nowPlayingIndex++;
      trackRows.get(nowPlayingIndex).setNowPlayingIndicatorBall();
    }
    
	private void clearNowPlayingIndicatorBall()
	{
	  for(int i=0; i<trackRows.size(); i++)
	  {
		  trackRows.get(i).setNotPlayingIndicatorBall();
	  }
	}
	
	private void clearSelectedIndicatorBall()
	{
	  for(int i=0; i<trackRows.size(); i++)
	  {
		  trackRows.get(i).setNotSelectedIndicatorBall();
	  }
	}
	
    private MediaPlayer createMediaPlayer(String path, boolean autoPlay)
    {
    	//File temp = new File(path);
	  //  Media  media = new Media(temp.toURI().toString());
    	final String thisPath=path;
    	Media  media = new Media(path);
	      final MediaPlayer mp = new MediaPlayer(media);
	      mp.setOnError(new Runnable() {
	          public void run() {
	            System.out.println("Media error occurred: " + mp.getError());
	            System.out.println("path: " + thisPath);
	          }
	        });
	      mp.setAutoPlay(autoPlay);
	      return mp;
    }
	
    /*
	void setPlaylistTracks(int index)
	{
	   PlaylistData pd = data.playlists[index];
	   ItunesTrackData td = null;
	   String path;
	   String lastGroup="";
	   int tandas=0;
	   
	   trackData= FXCollections.observableArrayList(); 
	   for(int j=0; j<pd.tracks.length; j++)
		{
		  td=data.tracks[pd.tracks[j]];
		  path=td.path.substring(16);
		  try {
   			 path = URLDecoder.decode(path,"UTF-8");
   			 } catch (Exception e) { e.printStackTrace(); }
		  File temp = new File(path);
		  path=temp.toURI().toString();
		  //System.out.println("plalist track: "+path);
		  if (td.grouping!=null)
		  {
		    if (td.grouping.toLowerCase().equals("cortina"))
		    {
		      tandas++;
		    }
		  }
		  trackData.add(new Track(td.name, td.artist, path, td.grouping, td.time));
		}
	   trackTable.setItems(trackData);
	   
	   // in case last track in playlist is not a cotrina;
	   if (td.grouping!=null)
	  {
	     if (!td.grouping.toLowerCase().equals("cortina")) tandas++;
	}
	   System.out.println("Tandas: "+tandas);
	}
	*/
    
    
   void getPlaylistData()
   {
	   for(int i=0; i<data.playlists.length; i++)
		{	
		  pd = 	data.playlists[i];
	      playlistData.add(new Playlist(i+1, pd.name));
		}
   }
   
   /** sets the currently playing label to the label of the new media player and updates the progress monitor. 
   private void setCurrentlyPlaying(final MediaPlayer newPlayer) 
   {
     progress.setProgress(0);
     progressChangeListener = new ChangeListener<Duration>() {
       @Override public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
         progress.setProgress(1.0 * newPlayer.getCurrentTime().toMillis() / newPlayer.getTotalDuration().toMillis());
       }
     };
     newPlayer.currentTimeProperty().addListener(progressChangeListener);

     String source = newPlayer.getMedia().getSource();
     source = source.substring(0, source.length() - ".mp3".length());
     source = source.substring(source.lastIndexOf("/") + 1).replaceAll("%20", " ");
     currentlyPlaying.setText("Now Playing: " + source);
     setInfoWindow();
  }
   */
  
    
    private void cancelListPlay()
    {
    	if (listPlaying)
    	{
    	   final MediaPlayer curPlayer = mediaView.getMediaPlayer();
           curPlayer.currentTimeProperty().removeListener(progressChangeListener);
           curPlayer.stop();
           //players.clear();
           vbox.getChildren().remove(3);
          // vbox.getChildren().remove(2);
           listPlaying=false;
    	}
    }
    
    
    
   
   
    
 	
	
	 private void setInfoWindow()
	   {
		 String curArtist = trackRows.get(nowPlayingIndex).getArtistName();
		 currentArtist.setText(curArtist);
		
		 currentTrackName.setText(trackRows.get(nowPlayingIndex).name.getText());
		 
	    if (infoWindow!=null) infoWindow.update(currentArtist.getText(), currentTrackName.getText());
	   }
	
	private void showInfoWindow()
    {
       infoWindow = new InfoWindow(currentArtist.getText(), currentTrackName.getText());
        
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
	  
	  EventHandler <MouseEvent>mouseDownHandler = new EventHandler<MouseEvent>() {
          public void handle(MouseEvent event)  
          { 
        	 // System.out.println("mouse down: "+event.getY()+" - "+scrollPane.getVvalue());
        	 // System.out.println("scrollPane Height: "+scrollPane.getHeight());
        	//  System.out.println("trackGrid Height: "+trackGrid.getHeight());
        	//  System.out.println("number of rows: "+numberOfTracksInPlaylist);
        	  double trackIndex=0;
        	  trackIndex=((event.getY()+(scrollPane.getVvalue()*(trackGrid.getHeight()-scrollPane.getHeight())))  /22.188);
        	  System.out.println("down trackIndex: "+Math.round(trackIndex));
        	   
          }};
          
      EventHandler <MouseEvent>mouseReleasedHandler = new EventHandler<MouseEvent>() {
          public void handle(MouseEvent event)  
          { 
        	 // System.out.println("mouse up idx: "+event.getY()+" - "+scrollPane.getVvalue());   
        	  double trackIndex=0;
        	  trackIndex=((event.getY()+(scrollPane.getVvalue()*(trackGrid.getHeight()-scrollPane.getHeight())))  /22.188);
        	  System.out.println("up trackIndex: "+Math.round(trackIndex));
        	  
          }};
  
	  scrollPane.setOnMousePressed(mouseDownHandler);
	  scrollPane.setOnMouseReleased(mouseReleasedHandler);
	  scrollPane.getHvalue();
	  
	  scrollPane.setPrefWidth(600);
	  scrollPane.setFitToHeight(true);
	 
	  scrollPane.setContent(trackGroup);
	 
	  return scrollPane;
	}
	
	private void getTrackRows(int index)
	{
	   stopTrack();
	  
	  // System.out.println("TrackGroup size: "+trackGroup.getChildren().size());
	  if (trackGroup.getChildren().size()>0) 
	  {	  
		  trackGroup.getChildren().remove(0);
		 trackRows.clear();
	  }
	  
	 // trackData= FXCollections.observableArrayList(); 
	  
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
		  try {
  			 path = URLDecoder.decode(path,"UTF-8");
  			 } catch (Exception e) { e.printStackTrace(); }
		  File temp = new File(path);
		  path=temp.toURI().toString();
		  //System.out.println("plalist track: "+path);
		 
		 
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
	   
	   EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event)  { ShowIndex();  }};
		
	  trackGrid.setOnMouseClicked(bHandler);
	   
	 // tr=trackRows.get(3);
	 //  tr.artist.setStyle("-fx-border: 2px solid; -fx-border-color: gray; -fx-background-color: red;");
	    
	  trackGroup.getChildren().add(trackGrid);
	  /*
		 players = new ArrayList<MediaPlayer>();
			
	        for(int i=0; i<trackRows.size(); i++)
	        {
		      players.add(createMediaPlayer(trackRows.get(i).path, false));
		    }
	*/
	        trackRows.get(0).setSelectedIndicatorBall();
	        nowPlayingIndex=0;
	}
	
	private void ShowIndex()
	{
	   selectedIndex=TrackRow.getPindex();
	   //System.out.println("pIndex:  "+nowPlayingIndex);
	  
	   clearSelectedIndicatorBall();
	   trackRows.get(selectedIndex).setSelectedIndicatorBall();
	   
	   //playList2();
	   
	}

	
} 