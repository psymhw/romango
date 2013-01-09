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
import test.Test;
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
	 int playListTrackIndex=0;
	
    
    private TableView<Playlist> playlistTable = new TableView<Playlist>();
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
    private List<MediaPlayer> players;
    private InfoWindow infoWindow;
    
    Group trackGroup = new Group();
    
    Rectangle redBox = new Rectangle(100, 100);
    Equalizer eq;
    long lastPlaylistRequestTime=System.currentTimeMillis();
    ScrollPane scrollPane;
    GridPane trackGrid;
    int numberOfTracksInPlaylist=0;
    
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
         setupPlaylistTable();
      //  setupTrackTable();
 
        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        final HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.setPadding(new Insets(10, 0, 0, 10));
        
        hbox.getChildren().add(playlistTable);
       // hbox.getChildren().add(trackTable);
        hbox.getChildren().add(getTrackGrid());
       
        
     
        
        vbox.getChildren().addAll(label, hbox);
        ((Group) scene.getRoot()).getChildren().add(vbox);
        
        final URL stylesheet = getClass().getResource("style.css");
        scene.getStylesheets().add(stylesheet.toString());
        stage.setScene(scene);
        stage.show();
    }

    
	private void setupPlaylistTable() 
	{
		playlistTable.setEditable(true);
 
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
        
        playlistTable.setItems(playlistData);
        playlistTable.getColumns().addAll(idCol, nameCol);
	}
   
    
	private void playList()
	{
		// debounce
		long currentTime=System.currentTimeMillis();
		if (currentTime < (lastPlaylistRequestTime+500)) 
		{	
			System.out.println("debounced");
		  return;
		}
		lastPlaylistRequestTime=currentTime;
		
		cancelListPlay();
		listPlaying=true;
        
		players = new ArrayList<MediaPlayer>();
		//System.out.println("playListTrackIndex: "+playListTrackIndex);
		
        for(int i=playListTrackIndex; i<trackRows.size(); i++)
        {
	      players.add(createMediaPlayer(trackRows.get(i).path, false));
	      //System.out.println("path: "+trackData.get(i).getPath());
        }
        
        mediaView = new MediaView(players.get(0));
        
        final Button skip = new Button("Skip");
        final Button play = new Button("Pause");
        final Button cancel = new Button("Cancel");
        final Button info = new Button("Info Window");

        /*
        // play each audio file in turn.
        for (int i = 0; i < players.size(); i++) {
          final MediaPlayer player = players.get(i);
          final MediaPlayer nextPlayer = players.get((i + 1) % players.size());
          
          
          player.setOnEndOfMedia(new Runnable() {
            @Override public void run() {
               
                player.currentTimeProperty().removeListener(progressChangeListener);
                mediaView.setMediaPlayer(nextPlayer);
                System.out.println("next song");
                clearNowPlaying();
                trackRows.get(playListTrackIndex).setNowPlaying();
                playListTrackIndex++;
                
                setInfoWindow();
                vbox.getChildren().remove(3);
                eq = new Equalizer(nextPlayer);
                vbox.getChildren().add(eq.getGridPane());
                  nextPlayer.play();
                  
            }
          });
        }
        */
        
        // allow the user to skip a track.
        skip.setOnAction(new EventHandler<ActionEvent>() {
          @Override public void handle(ActionEvent actionEvent) {
            final MediaPlayer curPlayer = mediaView.getMediaPlayer();
            MediaPlayer nextPlayer = players.get((players.indexOf(curPlayer) + 1) % players.size());
            mediaView.setMediaPlayer(nextPlayer);
            curPlayer.currentTimeProperty().removeListener(progressChangeListener);
            curPlayer.stop();
            clearNowPlaying();
            trackRows.get(playListTrackIndex).setNowPlaying();
            playListTrackIndex++;
            
            setInfoWindow();
           vbox.getChildren().remove(3);
           eq = new Equalizer(nextPlayer);
           vbox.getChildren().add(eq.getGridPane());
           
        
            nextPlayer.play();
            
          }
        });
        
     // cancel playlist.
        cancel.setOnAction(new EventHandler<ActionEvent>() {
          @Override public void handle(ActionEvent actionEvent) {
            final MediaPlayer curPlayer = mediaView.getMediaPlayer();
            curPlayer.currentTimeProperty().removeListener(progressChangeListener);
            curPlayer.stop();
            players.clear();
            vbox.getChildren().remove(3);
            vbox.getChildren().remove(2);
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
          @Override public void handle(ActionEvent actionEvent) {
            if ("Pause".equals(play.getText())) {
              mediaView.getMediaPlayer().pause();
              play.setText("Play");
            } else {
              mediaView.getMediaPlayer().play();
              play.setText("Pause");
            }
          }
        });


        
        // display the name of the currently playing track.
        mediaView.mediaPlayerProperty().addListener(new ChangeListener<MediaPlayer>() {
          @Override public void changed(ObservableValue<? extends MediaPlayer> observableValue, MediaPlayer oldPlayer, MediaPlayer newPlayer) {
            setCurrentlyPlaying(newPlayer);
          }
        });
        
        HBox hb = HBoxBuilder.create().spacing(10).alignment(Pos.CENTER).children(info, skip, play, progress, mediaView).build();
        vbox.getChildren().add(hb);
        //vbox.getChildren().add(currentlyPlaying);
        
        
        /*
        GridPane gp = new GridPane();
        createEQBands(gp,players.get(0) );
        createSpectrumBars(gp, players.get(0));
        spectrumListener = new SpectrumListener(START_FREQ, players.get(0), spectrumBars);
        players.get(0).setAudioSpectrumListener(spectrumListener);
        vbox.getChildren().add(gp);
        */
        
        eq = new Equalizer(players.get(0));
        vbox.getChildren().add(eq.getGridPane());
        //System.out.println("vbox size: "+vbox.getChildren().size());
        
       // System.out.println("players: "+players.size());
     // play each audio file in turn.
        for (int i = 0; i < players.size(); i++) 
        {
          final MediaPlayer player = players.get(i);
          
          final MediaPlayer nextPlayer = players.get((i + 1) % players.size());
          player.setOnEndOfMedia(new Runnable() {
            @Override public void run() {
//          
	           System.out.println("next player");
	           mediaView.setMediaPlayer(nextPlayer);
	           clearNowPlaying();
	           trackRows.get(playListTrackIndex).setNowPlaying();
	           playListTrackIndex++;
	           setInfoWindow();
	           vbox.getChildren().remove(3);
	           eq = new Equalizer(nextPlayer);
	           vbox.getChildren().add(eq.getGridPane());
	           mediaView.getMediaPlayer().play();
	           
             }
          });
        }
        //?
        clearNowPlaying();
        trackRows.get(playListTrackIndex).setNowPlaying();
        playListTrackIndex++;
        
        setInfoWindow();
        mediaView.getMediaPlayer().play();
        setCurrentlyPlaying(mediaView.getMediaPlayer());

	}
	
	private void clearNowPlaying()
	{
	  for(int i=0; i<trackRows.size(); i++)
	  {
		  trackRows.get(i).setNotPlaying();
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
   
   /** sets the currently playing label to the label of the new media player and updates the progress monitor. */
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
   
  
    
    private void cancelListPlay()
    {
    	if (listPlaying)
    	{
    	   final MediaPlayer curPlayer = mediaView.getMediaPlayer();
           curPlayer.currentTimeProperty().removeListener(progressChangeListener);
           curPlayer.stop();
           players.clear();
           vbox.getChildren().remove(3);
           vbox.getChildren().remove(2);
           listPlaying=false;
    	}
    }
    
    
    
   
   
    
 	
	
	 private void setInfoWindow()
	   {
		 String curArtist = trackRows.get(playListTrackIndex-1).getArtistName();
		 currentArtist.setText(curArtist);
		
		 currentTrackName.setText(trackRows.get(playListTrackIndex-1).name.getText());
		 
	    if (infoWindow!=null) infoWindow.update(currentArtist.getText(), currentTrackName.getText());
	   }
	
	private void showInfoWindow()
    {
       infoWindow = new InfoWindow(currentArtist.getText(), currentTrackName.getText());
        
    }
	
	private void loadFonts()
	  {
		
		Font.loadFont(Test.class.getResource("/resources/fonts/Carousel.ttf").toExternalForm(), 10  );
		Font.loadFont(Test.class.getResource("/resources/fonts/Anagram.ttf").toExternalForm(), 10  );
		Font.loadFont(Test.class.getResource("/resources/fonts/Carrington.ttf").toExternalForm(), 10  );
		Font.loadFont(Test.class.getResource("/resources/fonts/DEFTONE.ttf").toExternalForm(), 10  );
		Font.loadFont(Test.class.getResource("/resources/fonts/EastMarket.ttf").toExternalForm(), 10  );
		Font.loadFont(Test.class.getResource("/resources/fonts/england.ttf").toExternalForm(), 10  );
		Font.loadFont(Test.class.getResource("/resources/fonts/FFF_Tusj.ttf").toExternalForm(), 10  );
		
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
	//	 trackData.add(new Track(td.name, td.artist, path, td.grouping, td.time));

		 // GridPane.setHalignment(artist, HPos.LEFT);
		//  gridPane.add(textLabel(td.name, 200, 1), 0, j);
		//  gridPane.add(textLabel(td.artist, 200, 1), 1, j);
		//  gridPane.add(textLabel(td.grouping, 100, 1), 2, j);
		  
		  //trackData.add(new Track(td.name, td.artist, path, td.grouping, td.time));
		}
	  
	   TrackRow tr = null;
	   int row=0;
	   numberOfTracksInPlaylist=0;
	   Iterator<TrackRow> it = trackRows.iterator();
	   while(it.hasNext())
	   {
		  tr=it.next(); 
		  trackGrid.add(tr.nowPlaying, 0, row);
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
	}
	
	private void ShowIndex()
	{
	  // System.out.println("pIndex:  "+TrackRow.getPindex());
	   playListTrackIndex=TrackRow.getPindex();
	   playList();
	   
	}

	/*
	private void createEQBands(GridPane gp, MediaPlayer mp) {
	    final ObservableList<EqualizerBand> bands =
	            mp.getAudioEqualizer().getBands();

	    bands.clear();
	    
	    double min = EqualizerBand.MIN_GAIN;
	    double max = EqualizerBand.MAX_GAIN;
	    double mid = (max - min) / 2;
	    double freq = START_FREQ;

	    // Create the equalizer bands with the gains preset to
	    // a nice cosine wave pattern.
	    for (int j = 0; j < BAND_COUNT; j++) {
	      // Use j and BAND_COUNT to calculate a value between 0 and 2*pi
	      double theta = (double)j / (double)(BAND_COUNT-1) * (2*Math.PI);
	      
	      // The cos function calculates a scale value between 0 and 0.4
	      double scale = 0.4 * (1 + Math.cos(theta));
	      
	      // Set the gain to be a value between the midpoint and 0.9*max.
	      double gain = min + mid + (mid * scale);
	      
	      bands.add(new EqualizerBand(freq, freq/2, gain));
	      freq *= 2;
	    }
	    
	    for (int i = 0; i < bands.size(); ++i) {
	      EqualizerBand eb = bands.get(i);
	      Slider s = createEQSlider(eb, min, max);

	      final Label l = new Label(formatFrequency(eb.getCenterFrequency()));
	      l.getStyleClass().addAll("mediaText", "eqLabel");

	      GridPane.setHalignment(l, HPos.CENTER);
	      GridPane.setHalignment(s, HPos.CENTER);
	      GridPane.setHgrow(s, Priority.ALWAYS);

	      gp.add(l, i, 1);
	      gp.add(s, i, 2);
	    }
	  }
	
	private Slider createEQSlider(EqualizerBand eb, double min, double max) {
	    final Slider s = new Slider(min, max, eb.getGain());
	    s.getStyleClass().add("eqSlider");
	    s.setOrientation(Orientation.VERTICAL);
	    s.valueProperty().bindBidirectional(eb.gainProperty());
	    s.setPrefWidth(44);
	    return s;
	  }

	private void createSpectrumBars(GridPane gp, MediaPlayer mp) {
	  spectrumBars = new SpectrumBar[BAND_COUNT];

	  for (int i = 0; i < spectrumBars.length; i++) {
	    spectrumBars[i] = new SpectrumBar(100, 20);
	    spectrumBars[i].setMaxWidth(44);
	    GridPane.setHalignment(spectrumBars[i], HPos.CENTER);
	    gp.add(spectrumBars[i], i, 0);
	  }
	}

	  private String formatFrequency(double centerFrequency) {
	    if (centerFrequency < 1000) {
	      return String.format("%.0f Hz", centerFrequency);
	    } else {
	      return String.format("%.1f kHz", centerFrequency / 1000);
	    }
	  }
	*/
} 