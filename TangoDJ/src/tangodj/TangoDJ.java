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
    Group tandaDrag = new Group();
    Text dragText = new Text("HELLO");
    private int dragStartIndex=0;
    private int dragFinishIndex=0;
    
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
    	dragText.setFill(Color.RED);
    	dragText.setOpacity(.5);
    	dragText.setFont(new Font("Cambria", 18));
    	
    	loadFonts();
    	parser = new iTunesParser(data);
		parser.parseFile();
		
    	getAllPlaylistData();
    	
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
        hbox.getChildren().add(getTrackScrollPane());
       
        
     
        
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
          if (playlist.isDone()) return;
                       
          playlist.incrementPlaying();
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
          playlist.setPlayingTrackToSelected();
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
		  stopTrack();
	      stop.setDisable(true);
	      play.setDisable(true);
	      skip.setDisable(true);
	      info.setDisable(true);
		  createPlaylist(index);
		  populateTrackGrid();
		  playlist.resetSelectedIndicator();
		  play.setDisable(false);
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
	  final MediaPlayer player = createMediaPlayer(playlist.getPlayingTrackPath(), false);
	  player.setOnEndOfMedia(new Runnable() 
	  {
        public void run() 
        {
          if (playlist.isDone()) return;
          if (playlist.playingIsSelected()) playlist.incrementSelected();
          playlist.incrementPlaying();
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
	
    
    
   void getAllPlaylistData()
   {
	   for(int i=0; i<data.playlists.length; i++)
		{	
		  pd = 	data.playlists[i];
	      playlistData.add(new PlaylistTitle(i+1, pd.name));
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
	
	
	
	private ScrollPane getTrackScrollPane()
	{
	  scrollPane = new ScrollPane();
	  EventHandler <MouseEvent>mouseDownHandler = new EventHandler<MouseEvent>() 
	  {
        public void handle(MouseEvent event)  
        { 
          int trackIndex=0;
          trackIndex=(int)Math.round(((event.getY()+(scrollPane.getVvalue()*(trackGrid.getHeight()-scrollPane.getHeight())))  /22.188));
          //System.out.println("down trackIndex: "+trackIndex);
          dragStartIndex=trackIndex;
          dragText.setText(playlist.getDragText(trackIndex-1));
          dragText.setX(event.getX());
          dragText.setY(event.getY());
          trackGroup.getChildren().add(dragText);
        }};
          
        EventHandler <MouseEvent>mouseReleasedHandler = new EventHandler<MouseEvent>() 
        {
          public void handle(MouseEvent event)  
          { 
            int trackIndex=0;
        	//trackIndex=((event.getY()+(scrollPane.getVvalue()*(trackGrid.getHeight()-scrollPane.getHeight())))  /22.188);
            trackIndex=(int)Math.round(((event.getY()+(scrollPane.getVvalue()*(trackGrid.getHeight()-scrollPane.getHeight())))  /22.188));
            dragFinishIndex=trackIndex;
            trackGroup.getChildren().remove(1);
            if (dragStartIndex!=dragFinishIndex) 
            {
            	playlist.reorder(dragStartIndex, dragFinishIndex);
            	populateTrackGrid();
            	//printTandas();
            }
        	//System.out.println("up trackIndex: "+Math.round(trackIndex));
        	
          }
        };
        
       
        trackGroup.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	//System.out.println("Mouse Moved");
                dragText.setX(event.getX());
                dragText.setY(event.getY());
            }
        });
   
	    scrollPane.setOnMousePressed(mouseDownHandler);
	    scrollPane.setOnMouseReleased(mouseReleasedHandler);
	    scrollPane.getHvalue();
	    scrollPane.setPrefWidth(600);
	    scrollPane.setFitToHeight(true);
	    scrollPane.setContent(trackGroup);
	 
	    return scrollPane;
	}
	
	
	
	
	private void createPlaylist(int index)
	{
	  
	  
	  PlaylistData pd = data.playlists[index];
	  ItunesTrackData td = null;
	  String path=null;
	  playlist = new Playlist();
	  	  	   
	  // populate Tandas and TrackRows
	  for(int trackNumber=0; trackNumber<pd.tracks.length; trackNumber++)
	  {
	    td=data.tracks[pd.tracks[trackNumber]];
		path=td.path.substring(16);
		try 
		{ path = URLDecoder.decode(path,"UTF-8"); } catch (Exception e) { e.printStackTrace(); }
		File temp = new File(path);
		path=temp.toURI().toString();
		playlist.addTrackRow( new TrackRow(td.name, td.artist, path, td.grouping, td.time, trackNumber));
	  }
	  playlist.setTrackRows();
	  //printTandas();
	  
	  
	}
	
	
	
	private void ShowIndex()
	{
		playlist.setNotSelectedIndicator();
	  playlist.selectedTrack=TrackRow.getPindex();
	  playlist.setSelectedIndicator();
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
	  }
	  
	  trackGrid = new GridPane();
		
	  trackGrid.setPadding(new Insets(10, 10, 10, 10));
	  trackGrid.setVgap(0);
	  trackGrid.setHgap(0);

	  Iterator<Tanda> it = playlist.getTandas().iterator();
	  while(it.hasNext())
	  {
		tanda = it.next();
		trs=tanda.getTrackRows();
		Iterator<TrackRow> itx = trs.iterator();
		while(itx.hasNext())
		{
		  tr = itx.next();
		  trackGrid.add(tr.indicator, 0, row);
		  trackGrid.add(tr.index, 1, row);
		  trackGrid.add(tr.grouping, 2, row);
		  trackGrid.add(tr.artist, 3, row);
		  trackGrid.add(tr.name, 4, row);
		  numberOfTracksInPlaylist++;  
		  row++;
		}
	  }  
	  
	  EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() 
	  {
	    public void handle(MouseEvent event)  { ShowIndex(); }
	  };
				
	  trackGrid.setOnMouseClicked(bHandler);
	  trackGroup.getChildren().add(trackGrid);
	
	}
	
	private void printTandas()
	{
	  Tanda tanda;
	  ArrayList<TrackRow> trs;
	  TrackRow tr;
	  int row=1;
	  numberOfTracksInPlaylist=0;
	  int tandaNumber=1;
	  

	  Iterator<Tanda> it = playlist.getTandas().iterator();
	  while(it.hasNext())
	  {
		tanda = it.next();
		row=1;
		System.out.println(tandaNumber+" "+tanda.artist.lastName+" "+tanda.group);
		trs=tanda.getTrackRows();
		Iterator<TrackRow> itx = trs.iterator();
		while(itx.hasNext())
		{
		  tr = itx.next();
		  System.out.println(row+" of "+tanda.tracksInTanda()+"   "+tr.title);
		  row++;
		}
		tandaNumber++;
	  }  
	  

	}

	
} 