package tangodj;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.net.URI;

import test.Test;

import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import data.ItunesTrackData;
import data.PlaylistData;
import data.iTunesData;
import data.iTunesParser;
 
public class TangoDJ extends Application 
{
	private static String dbURL = "jdbc:derby:tangoDj;create=true;user=rick;password=smegma";
    private static String tableName = "restaurants";
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;
    
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
    private  ObservableList<Track> trackData= FXCollections.observableArrayList(); 
   
    
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
    
    
       
    public static void main(String[] args) 
    {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) 
    {
    	
    	parser = new iTunesParser(data);
		parser.parseFile();
		
    	getPlaylistData();
    	
    	progress.setMaxWidth(300);
        Scene scene = new Scene(new Group());
        stage.setTitle("Tango DJ");
        stage.setWidth(850);
        stage.setHeight(600);
 
        final Label label = new Label("Tango DJ");
        label.setFont(new Font("Arial", 20));
         setupPlaylistTable();
        setupTrackTable();
 
        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        final HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.setPadding(new Insets(10, 0, 0, 10));
        
        hbox.getChildren().add(playlistTable);
        hbox.getChildren().add(trackTable);
        
       // File temp = new File("C:/t2/Track02.mp3");
        
      //  Media media = new Media(temp.toURI().toString());
        
       // temp = new File("C:/t2/Track03.mp3");
      //  media = new Media(temp.toURI().toString());
        
      //  mp = new MediaPlayer(media);
       // mp.setAutoPlay(true);
        
     //   mc = new MediaControl(mp);
     //  ;
        
        vbox.getChildren().addAll(label, hbox);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
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
				 setPlaylistTracks(index);
			}
        };
        
        GenericCellFactory cellFactory = new GenericCellFactory(click);
      
        nameCol.setCellFactory(cellFactory);
        
        playlistTable.setItems(playlistData);
        playlistTable.getColumns().addAll(idCol, nameCol);
	}
    
	private void setupTrackTable() 
	{
		trackTable.setEditable(true);
		
		TableColumn nameCol = new TableColumn("Track Name");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Track, String>("name"));
 
        
        TableColumn artistCol = new TableColumn("Artist");
        artistCol.setMinWidth(200);
        artistCol.setCellValueFactory(
                new PropertyValueFactory<Track, String>("artist"));
		
 
        TableColumn timeCol = new TableColumn("Length");
        timeCol.setMinWidth(100);
        timeCol.setCellValueFactory(
                new PropertyValueFactory<Track, Integer>("time"));
       
        trackTable.setItems(trackData);
        trackTable.getColumns().addAll(nameCol, artistCol, timeCol);
        
        
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
			        
			       
			     
			   /*     
				 if (doubleClick==2)
				 {
					path=trackData.get(index).getPath().substring(16);
					playSong(path, true); 
					System.out.println("DD");
				}
				 else if (doubleClick==1)
				 {
					 path=trackData.get(index).getPath().substring(16);
					 playSong(path, false);
					 System.out.println("SS");
				 }
				 */
				
				 
				 
			}
        };
        
        GenericCellFactory cellFactory = new GenericCellFactory(click);
      
        nameCol.setCellFactory(cellFactory);
	}
    
	
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
        
        for(int i=playListTrackIndex; i<trackData.size(); i++)
        {
	      players.add(createMediaPlayer(trackData.get(i).getPath(), false));
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
        
        
        System.out.println("double click");
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
	           playListTrackIndex++;
	           setInfoWindow();
	           mediaView.getMediaPlayer().play();
	           
             }
          });
        }
        
        playListTrackIndex++;
        setInfoWindow();
        mediaView.getMediaPlayer().play();
        setCurrentlyPlaying(mediaView.getMediaPlayer());

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
			  
	void setPlaylistTracks(int index)
	{
	   PlaylistData pd = data.playlists[index];
	   ItunesTrackData td = null;
	   String path;
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
		  trackData.add(new Track(td.name, td.artist, path, td.time));
		}
	   trackTable.setItems(trackData);
	}
	
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
		 currentArtist.setText(trackData.get(playListTrackIndex-1).getArtist());
		
		 currentTrackName.setText(trackData.get(playListTrackIndex-1).getName());
		 
		 if (infoWindow!=null) infoWindow.update(currentArtist.getText(), currentTrackName.getText());
	   }
	
	private void showInfoWindow()
    {
       infoWindow = new InfoWindow(currentArtist.getText(), currentTrackName.getText());
        
    }

} 