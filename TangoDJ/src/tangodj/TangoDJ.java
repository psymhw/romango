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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
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
 
    
    
       
    public static void main(String[] args) 
    {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) 
   {
    	loadFonts();
    	parser = new iTunesParser(data);
		parser.parseFile();
		
    	getPlaylistData();
    	
    	progress.setMaxWidth(300);
        Scene scene = new Scene(new Group());
        stage.setTitle("Tango DJ");
        stage.setWidth(950);
        stage.setHeight(600);
 
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
				 System.out.println("Mouse Event: "+index);
				// setPlaylistTracks(index);
				 getTrackRows(index);
			}
        };
        
        GenericCellFactory cellFactory = new GenericCellFactory(click);
      
        nameCol.setCellFactory(cellFactory);
        
        playlistTable.setItems(playlistData);
        playlistTable.getColumns().addAll(idCol, nameCol);
	}
   
    /*
	private void setupTrackTable() 
	{
		trackTable.setEditable(true);
		
		TaskCellFactory cellFactory = new TaskCellFactory();
		
		TableColumn nameCol = new TableColumn("Track Name");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Track, String>("name"));
        nameCol.setCellFactory(cellFactory);
        
        TableColumn artistCol = new TableColumn("Artist");
        artistCol.setMinWidth(200);
        artistCol.setCellValueFactory(
                new PropertyValueFactory<Track, String>("artist"));
		
        TableColumn groupCol = new TableColumn("Group");
        groupCol.setMinWidth(100);
        groupCol.setCellValueFactory(
                new PropertyValueFactory<Track, String>("grouping"));
 
        TableColumn timeCol = new TableColumn("Length");
        timeCol.setMinWidth(100);
        timeCol.setCellValueFactory(
                new PropertyValueFactory<Track, Integer>("time"));
        
      
       
        trackTable.setItems(trackData);
        trackTable.getColumns().addAll(nameCol, artistCol, groupCol, timeCol);
        
        
        EventHandler <MouseEvent>click = new EventHandler<MouseEvent>() {
            
			@Override
			public void handle(MouseEvent ev) 
			{
				playListTrackIndex = ((TableCell)ev.getSource()).getIndex();
				
				 
				  
				 
				 if (ev.getClickCount()==1) {
			          processFirstClick();
			        }
			        if (ev.getClickCount()==2) {
			          processSecondClick();
			        }
			        
			       
			     
			   
				 
				 
			}
        };
        
       // GenericCellFactory cellFactory = new GenericCellFactory(click);
      
      //  nameCol.setCellFactory(cellFactory);
	}
    */
	
	/*
	 private void processFirstClick() {
		    new Thread(new Runnable() {
		      @Override
		      public void run() {
		        try {
		          Thread.sleep(DOUBLE_CLICK_WAIT_TIME);
		          if (! isBoxDoubleClicked()) {
		            Platform.runLater(new Runnable() {
		              @Override
		              public void run() {
						 
		                  System.out.println("Single click");
		                }
		            });
		          }
		        } catch (InterruptedException exc) {
		          // Should not be possible
		          throw new Error(exc);
		        }
		      }
		    }).start();
		  }
		  
		  private void processSecondClick() {
		    setBoxDoubleClicked(true);
		   
		    playList();
		
		    new Thread(new Runnable() {
		      @Override
		      public void run() {
		        try {
		          Thread.sleep(DOUBLE_CLICK_WAIT_TIME);
		          setBoxDoubleClicked(false);
		        } catch (InterruptedException exc) {
		          // should not happen
		          throw new Error(exc);
		        }
		      }
		    }).start();
		  }
		  public synchronized void setBoxDoubleClicked(boolean doubleClicked) {
			    boxDoubleClicked = doubleClicked ;
			  }
			  
			  public synchronized boolean isBoxDoubleClicked() {
			    return boxDoubleClicked ;
			  }
	/*
	void playSong(String path, boolean autoPlay)
	{
		try {
			 path = URLDecoder.decode(path,"UTF-8");
			 } catch (Exception e) { e.printStackTrace(); }
			 //System.out.println("Track: "+path);
		    
		     
		     int size = vbox.getChildren().size();
		     if (size>2) 
		     {
		       mc.stop();
		       vbox.getChildren().remove(2);
		     }
		     mc = new MediaControl(path, autoPlay);
		     vbox.getChildren().add(mc);
	}
	*/
		
	private void playList()
	{
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
                  nextPlayer.play();
                  
            }
          });
        }
        
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
        vbox.getChildren().add(currentlyPlaying);
        
        
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
	           mediaView.getMediaPlayer().play();
	           
             }
          });
        }
        
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
		 currentArtist.setText(trackRows.get(playListTrackIndex-1).artist.getText());
		
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
	  ScrollPane scrollPane = new ScrollPane();
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
	  
	  GridPane trackGrid = new GridPane();
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
	   Iterator<TrackRow> it = trackRows.iterator();
	   while(it.hasNext())
	   {
		  tr=it.next(); 
		  trackGrid.add(tr.nowPlaying, 0, row);
		  trackGrid.add(tr.index, 1, row);
		  trackGrid.add(tr.grouping, 2, row);
		  trackGrid.add(tr.artist, 3, row);
		  trackGrid.add(tr.name, 4, row);
			  
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
	   System.out.println("pIndex:  "+TrackRow.getPindex());
	   playListTrackIndex=TrackRow.getPindex();
	   playList();
	   
	}

	
} 