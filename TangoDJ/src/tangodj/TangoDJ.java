package tangodj;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.FrameBodyTIT1;
import org.farng.mp3.id3.ID3v2_3Frame;



import np.com.ngopal.control.AutoFillTextBox;

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
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
	int test=0;
	String style="cancelled";
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
    
    private Playlist playlist = new Playlist();;
   
    
    final ProgressBar progress = new ProgressBar();
    final ProgressBar progress2 = new ProgressBar();
    
   
    private ChangeListener<Duration> progressChangeListener;
    final Label currentlyPlaying = new Label();
    private Text currentArtist = new Text();
    final Label currentTrackName = new Label();
    final Label nextArtist = new Label();
    final Label nextTrackName = new Label();
   // private boolean listPlaying=false;
    
    private MediaView mediaView=null;
    private InfoWindow infoWindow;
    
  //  Group trackGroup = new Group();
    
    Rectangle redBox = new Rectangle(100, 100);
    Equalizer eq;
    long lastPlaylistRequestTime=System.currentTimeMillis();
    ScrollPane scrollPane;
    int numberOfTracksInPlaylist=0;  // total number in playlist
    
    final Button skip = new Button("Skip");
    final Button play = new Button("Play");
    final Button stop = new Button("Stop");
    final Button info = new Button("Info Window");
    final RadioButton rb1 = new RadioButton("Mark");
    final RadioButton rb2 = new RadioButton("Play");
    final ToggleGroup modeGroup = new ToggleGroup();
    
    
    private HBox hbox;
    private Slider volumeSlider;
    private CurrentTimeListener currentTimeListener;
    private Label currentTimeLabel;
    private MediaPlayer player;
    private double volume=.75;
    
    public final static int MARK = 0;
    public final static int PLAY = 1;
    private Stage dialog = null;
    private Stage primaryStage;
    
    int mode=PLAY;
    
    public static void main(String[] args) 
    {
        launch(args);
    }
 
  
   public void start(Stage stage) 
   {
    	primaryStage=stage;
    	loadFonts();
    	parser = new iTunesParser(data);
		parser.parseFile();
		
    	getAllPlaylistData();
    	
    	progress.setMaxWidth(300);
    	progress2.setMaxWidth(300);
    	progress2.setPrefHeight(100);
    	
    	//Font myFont = new Font("Arial", 12);
    	
    	
    	setupModeButtons();
    	
    	setupDialog();
    	 
    	
        Scene scene = new Scene(new Group());
        stage.setTitle("Tango DJ");
        stage.setWidth(950);
        stage.setHeight(590);
 
        final Label label = new Label("Tango DJ");
        label.setFont(new Font("Arial", 20));
         setupAllPlaylistsTable();
      //  setupTrackTable();
         
         volumeSlider = createSlider("volumeSlider");
         volumeSlider.setValue(volume);
         currentTimeLabel = createLabel("00:00", "mediaText");
 
        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        hbox = new HBox();
        hbox.setSpacing(5);
        hbox.setPadding(new Insets(10, 0, 0, 10));
        
        hbox.getChildren().add(allPlaylistsTable);
       // hbox.getChildren().add(trackTable);
       hbox.getChildren().add(getTrackScrollPane());
       
        
     
        
        vbox.getChildren().addAll(label, hbox);
        ((Group) scene.getRoot()).getChildren().add(vbox);
        
        final URL stylesheet = getClass().getResource("style.css");
        scene.getStylesheets().add(stylesheet.toString());
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        
        setupButtonActions();
        progress.setProgress(0);
        progress2.setProgress(0);
       // mediaView = new MediaView(createMediaPlayer(TangoDJ.class.getResource("/resources/sounds/Who1.mp3").toExternalForm(), false));
       Label modeLabel = new Label("MODE: ");
       modeLabel.setTextFill(Color.WHITE);
        
        HBox hb = HBoxBuilder.create().spacing(10).alignment(Pos.CENTER).children(modeLabel, rb1, rb2, new Label("       "), info, skip, play, stop, volumeSlider, progress, currentTimeLabel ).build();
        vbox.getChildren().add(hb);
  
        stop.setDisable(true);
        play.setDisable(true);
        skip.setDisable(true);
        info.setDisable(true);
        stage.show();
        
        
       
    }





private void setupModeButtons() 
{
	rb1.setToggleGroup(modeGroup);
	rb1.setId("mark");
	rb2.setId("play");
	rb2.setToggleGroup(modeGroup);
	rb2.setSelected(true);
	rb1.setTextFill(Color.WHITE);
	rb2.setTextFill(Color.WHITE);
	
	modeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
	    public void changed(ObservableValue<? extends Toggle> ov,
	        Toggle old_toggle, Toggle new_toggle) {
	            if (modeGroup.getSelectedToggle() != null) 
	            {
	             //	System.out.println(modeGroup.getSelectedToggle().getUserData().toString()); 
	             String selectedStr=modeGroup.getSelectedToggle().toString();
	             if (selectedStr.contains("mark")) mode=MARK;
	             if (selectedStr.contains("play")) mode=PLAY;
	             
	             if (mode==MARK)
	             System.out.println("mode: mark");   
	             if (mode==PLAY)
		             System.out.println("mode: play");  
	                
	            }                
	        }
	});
}

	private void setupButtonActions() 
	{
	  // SKIP *******************************************************************
	  skip.setOnAction(new EventHandler<ActionEvent>() 
	  {
        public void handle(ActionEvent actionEvent) 
        {
          stopTrack();
          playlist.highlightTandaOff();
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
        

      // PLAY ******************************************************* 
      play.setOnAction(new EventHandler<ActionEvent>() 
      {
        public void handle(ActionEvent actionEvent) 
        {
          playlist.setPlayingTrack();
          playlist.setPlaying(true);
          play.setDisable(true);
          stop.setDisable(false);
          skip.setDisable(false);
          info.setDisable(false);
          playTrack();
        }
      });

      // STOP **************************************************************
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
	      
	      if (playlist.isPlaying()) return;	
		  int index = ((TableCell)ev.getSource()).getIndex();
		  stopTrack();
	      stop.setDisable(true);
	      play.setDisable(true);
	      skip.setDisable(true);
	      info.setDisable(true);
		  createPlaylist(index);
		  scrollPane.setContent(playlist.getDisplay());
		  
		  playlist.selectFirstTrack();
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
	 // final MediaPlayer player = createMediaPlayer(playlist.getPlayingTrackPath(), false);
	  player = createMediaPlayer(playlist.getPlayingTrackPath(), false);
	  player.setVolume(volumeSlider.getValue());
	  player.setOnEndOfMedia(new Runnable() 
	  {
        public void run() 
        {
          if (playlist.isDone()) return;
          //if (playlist.playingIsSelected()) playlist.incrementSelected();
          playlist.incrementPlaying();
          playTrack();
        }
      });    
	   
	  mediaView = new MediaView(player);
      if (eq!=null) vbox.getChildren().remove(3);
       
      
       
      eq = new Equalizer(player);
      vbox.getChildren().add(eq.getGridPane());
      progress.setProgress(0);
      progress2.setProgress(0);
      volumeSlider.valueProperty().bindBidirectional(player.volumeProperty());
      currentTimeListener = new CurrentTimeListener(player, currentTimeLabel, progress, progress2);
      player.currentTimeProperty().addListener(currentTimeListener);
      
      playlist.setPlaying(true);
      mediaView.getMediaPlayer().play();
      setInfoWindow();
	}
	
    private void stopTrack() 
    {
    	
	   if (!playlist.isPlaying()) return; 
	   final MediaPlayer curPlayer = mediaView.getMediaPlayer();
	   //curPlayer.currentTimeProperty().removeListener(progressChangeListener);
	   curPlayer.currentTimeProperty().removeListener(currentTimeListener);
	   volumeSlider.valueProperty().unbind();
	   curPlayer.stop();
	   playlist.setPlaying(false);
	   //selectedIndex=0;
	   vbox.getChildren().remove(3);
	   eq=null;
	   playlist.setPlaying(false);
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
	   //TrackRow row = 	playlist.getPlayingTrack(); 
	   //String curArtist = row.getArtist();
	  // currentArtist.setText(curArtist);
	  // currentTrackName.setText(row.getTrackTitle());
	    if (infoWindow!=null) infoWindow.update(playlist, progress2);
	  }
	
	private void showInfoWindow()
    {
	//	TrackRow row = 	playlist.getPlayingTrack(); 
       infoWindow = new InfoWindow(playlist, progress2);
        
    }
	

	
	
	
	private ScrollPane getTrackScrollPane()
	{
	  scrollPane = new ScrollPane();
	  scrollPane.setOnMousePressed(mouseHandler);
	  scrollPane.setOnKeyReleased(keyEvent);
	  scrollPane.getHvalue();
	  scrollPane.setPrefWidth(600);
	  scrollPane.setFitToHeight(true);
	  return scrollPane;
	}
	
	// KEY EVENTS
	EventHandler<KeyEvent>keyEvent = new EventHandler<KeyEvent>() 
    {
	  public void handle(KeyEvent ke) 
	  {
	    if (ke.getCode()==KeyCode.ESCAPE) 
	    {
	    	playlist.highlightTandaOff();
	    	
	    }
	    if (ke.getCode()==KeyCode.UP) 
	    {
	    	playlist.reorder(KeyCode.UP);
	    	scrollPane.setContent(playlist.getDisplay());
	    	//playlist.calcPositions();
	    	playlist.highlightTanda();
	    }
	    if (ke.getCode()==KeyCode.DOWN) 
	    {
	    	playlist.reorder(KeyCode.DOWN);
	    	scrollPane.setContent(playlist.getDisplay());
	    	//playlist.calcPositions();
	    	playlist.highlightTanda();
	    }
	    if (ke.getCode()==KeyCode.ENTER) 
	    {
	      if (mode==MARK)
	      {
	    	if (playlist!=null)
	    	{
	    	  if (playlist.tandaHighlightBox.isVisible())
	    	  {
	    		dialog.show();
	    	  }
	    	}
	      }
	    	
	    }
	  }
	};
	
	// MOUSE  
	EventHandler <MouseEvent>mouseHandler = new EventHandler<MouseEvent>() 
	{
      public void handle(MouseEvent event)  
      { 
    	if (playlist==null) return;  
        int trackIndex=0;
        double scrollPaneContentsHeight=scrollPane.getContent().getBoundsInLocal().getHeight();
        double rowHeight=scrollPaneContentsHeight/playlist.getNumberOfTracks();
        double scrollWindow=scrollPaneContentsHeight-scrollPane.getViewportBounds().getHeight();

        playlist.setScrollPaneContentsHeight(scrollPaneContentsHeight); // not sure why this doesn't work when first loading the playlist (above).
        
        trackIndex=(int) Math.round(((event.getY()+(scrollPane.getVvalue()*scrollWindow))/rowHeight)-1);
        if (trackIndex<0) trackIndex=0;
        
        if (event.isSecondaryButtonDown())
        {	  
          int tandaIndex=playlist.getTandaIndex(trackIndex);
          playlist.highlightTanda(tandaIndex);
        }
        
        if (event.isPrimaryButtonDown())
        {	 
          //playlist.selectedTrack=trackIndex;
          if (mode==PLAY) playlist.selectTrack(trackIndex);
          if (mode==MARK)
          {
        	if (event.isShiftDown())
        	{
        	  playlist.tandaLastTrackMark=trackIndex;
        	  if ((playlist.tandaFirstTrackMark!=-1)
        	    &&(playlist.tandaLastTrackMark>playlist.tandaFirstTrackMark)) 
        	  {
        	    style="cancelled";
        	    playlist.highlightTandaBlock();
                //dialog.show();
        	  }
        	}
        	else
        	{
        	  playlist.tandaFirstTrackMark=trackIndex;
        	  playlist.highlightFirstTrack();
        	  playlist.tandaLastTrackMark=-1;
        	}
          }
        }
        
        
      }};
        
	
	private void createPlaylist(int index)
	{
	  PlaylistData pd = data.playlists[index];
	  ItunesTrackData td = null;
	  String path=null;
	  playlist = new Playlist();
	  int counter=0;
	  	  	   
	  System.out.println("Tanda id: "+index);
	  // populate Tandas and TrackRows
	  for(int trackNumber=0; trackNumber<pd.tracks.length; trackNumber++)
	  {
	    td=data.tracks[pd.tracks[trackNumber]];
		path=td.path.substring(16);
		try 
		{ path = URLDecoder.decode(path,"UTF-8"); } catch (Exception e) { e.printStackTrace(); }
		File temp = new File(path);
		
	    MP3File mp3=null;
	   /*
	    try { 
	    	  System.out.println(path);
	    	  mp3 = new MP3File(temp); 
	          System.out.println("identifier: "+mp3.getID3v2Tag().getIdentifier());
	          
	    } catch (Exception e) { e.printStackTrace(); }
		
	    */
	    
		path=temp.toURI().toString();
		playlist.addTrackRow( new TrackRow(mp3, td.name, td.artist, path, td.grouping, td.time, trackNumber));
	  }
	  playlist.finalize();
	  //printTandas();
	}
	
	
	
	//private void ShowIndex()
	//{
	//	playlist.setNotSelectedIndicator();
//	  playlist.selectedTrack=TrackRow.getPindex();
	//  playlist.setSelectedIndicator();
//	}

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
		  System.out.println(row+" of "+tanda.tracksInTanda()+"   "+tr.getTrackTitle());
		  row++;
		}
		tandaNumber++;
	  }  
	  

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
	
	private Slider createSlider(String id) {
	    final Slider slider = new Slider(0.0, 1.0, 0.1);
	    slider.setId(id);
	    slider.setValue(0);
	    return slider;
	  }
	
	private String formatDuration(Duration duration) {
	    double millis = duration.toMillis();
	    int seconds = (int) (millis / 1000) % 60;
	    int minutes = (int) (millis / (1000 * 60));
	    return String.format("%02d:%02d", minutes, seconds);
	  }
	
	private Label createLabel(String text, String styleClass) {
	    final Label label = new Label(text);
	    label.getStyleClass().add(styleClass);
	    return label;
	  }
	
	private void setupDialog() 
	{
	  
	  dialog = new Stage(StageStyle.UTILITY);
	  dialog.initModality(Modality.WINDOW_MODAL);
	  dialog.initOwner(primaryStage);
	  
	  dialog.setX(primaryStage.getX()+600);
	  dialog.setWidth(300);
      dialog.setHeight(150);
      
      Group dialogGroup = new Group();
      HBox mainHBox = new HBox();
      
      ObservableList data = FXCollections.observableArrayList();
      String[] s = new String[]{
    		  
    		  "Biagi",
    		  "Calo",
    		  "Canaro",
    		  "Castillo",
    		  "D'Agostino",
    		  "D'Arienzo",
    		  "De Caro",
    		  "De Angelise", 
    		  "De Caro",
    		  "Di Sarli",
    		  "Donato",
    		  "Firpo", 
    		  "Lomuto",
    		  "Orquesta Tipica Victor",
    		  "Pugliese",
    		  "Quinteto Pirincho",
    		  "Rodriguez",
    		  "Tanturi",
    		  "Troilo",
    		  };

          for(int j=0; j<s.length; j++){
              data.add(s[j]);
          }

          final AutoFillTextBox box = new AutoFillTextBox(data);
      
      HBox hbox_1 = new HBox();
      hbox_1.setSpacing(5);
      hbox_1.setPadding(new Insets(5, 5, 5, 5));
      hbox_1.getChildren().add(new Label("Orchestra: "));
      TextField orchestra = new TextField();
     // orchestra.setScaleX(200);
      hbox_1.getChildren().add(box);
      
      
      final RadioButton rb1 = new RadioButton("Tango");
      final RadioButton rb2 = new RadioButton("Vals");
      final RadioButton rb3 = new RadioButton("Milonga");
      final RadioButton rb4 = new RadioButton("Alternative");
      final RadioButton rb5 = new RadioButton("Cleanup");
      
      final ToggleGroup radioButtonGroup = new ToggleGroup();
      
  	
  	rb1.setId("tango");
  	rb2.setId("vals");
  	rb3.setId("milonga");
  	rb4.setId("alternative");
  	rb5.setId("cleanup");
  	
  	rb1.setToggleGroup(radioButtonGroup);
  	rb2.setToggleGroup(radioButtonGroup);
  	rb3.setToggleGroup(radioButtonGroup);
  	rb4.setToggleGroup(radioButtonGroup);
  	rb5.setToggleGroup(radioButtonGroup);
  	
  	//rb1.setSelected(true);
  	//rb1.setTextFill(Color.WHITE);
  	//rb2.setTextFill(Color.WHITE);
  	
  	radioButtonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
  	    public void changed(ObservableValue<? extends Toggle> ov,
  	        Toggle old_toggle, Toggle new_toggle) {
  	            if (radioButtonGroup.getSelectedToggle() != null) 
  	            {
  	              String selectedStr=radioButtonGroup.getSelectedToggle().toString();
  	              if (selectedStr.contains("tango")) style="tango";
  	              else if (selectedStr.contains("vals")) style="vals";
  	              else if (selectedStr.contains("milonga")) style="milonga";
  	              else if (selectedStr.contains("alternative")) style="alternative";
  	              else if (selectedStr.contains("cleanup")) style="cleanup";
  	            }                
  	        }
  	});
      
  	VBox buttonBox = new VBox();
  	buttonBox.getChildren().add(rb1);
  	buttonBox.getChildren().add(rb2);
  	buttonBox.getChildren().add(rb3);
  	buttonBox.getChildren().add(rb4);
  	buttonBox.getChildren().add(rb5);
  	
  	mainHBox.getChildren().add(hbox_1);
  	mainHBox.getChildren().add(buttonBox);
  	
  	Button button = new Button("Ok");
  	
  	button.setOnAction(new EventHandler<ActionEvent>() 
      {
        public void handle(ActionEvent actionEvent) 
        {
        		
           dialog.close();
           playlist.tandaHighlightBox.setVisible(false);
           playlist.addTanda(box.getText().toString(), style);
           printTandas();
          // System.out.println("Orchestra: "+box.getText().toString());
           //System.out.println("Style: "+style);
        }
      });
  	
  	mainHBox.getChildren().add(button);
  	dialogGroup.getChildren().add(mainHBox);
      
    Scene dialogScene = new Scene(dialogGroup);
    dialogScene.getStylesheets().add("/test/control.css");
      
	dialog.setScene(dialogScene);
	dialog.getScene().getStylesheets().add(getClass().getResource("modal-dialog.css").toExternalForm());
  }
	
} 