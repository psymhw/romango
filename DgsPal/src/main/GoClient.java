package main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


public class GoClient extends Application  
{  
  final static int OPEN = 0;
  final static int BLACK = 1;
  final static int WHITE = 2;
   
  final static int START=0;
  final static int NORTH=1;
  final static int SOUTH=2;
  final static int EAST=3;
  final static int WEST=4;
  final static int CHECK_ALL_DIRECTIONS=5;
   
  final static int GROUP_MEMBER=33;
  final static int LIBERTY=43;
  
  final static int STYLE_REGULAR=0;
  final static int STYLE_LAST_MOVE=11;
  final static boolean TRIAL=true;
  final static boolean LIVE=false;
  final static int FROM_SERVER = 0;
  final static int FROM_LOCAL_FILE = 1;
  final static int FROM_DISK = 2;
  
  final static int MIN_RETRY_TIME =-1;
  final static int NOT_LOGGED_IN =-2;
  final static int NO_MOVE_WAITING=-3;
  final static int GAME_FOUND=-4;
  final static int EXCEPTION=-5;
  final static int LOGGED_IN =-6;
  final static int GAME_OVER =-7;
  
  private ImageView quit;
  
  Image board_top_image;
  Image board_left_image;
  Image board_right_image;
  Image board_bottom_image;
   
  Image board_fill_image;
   
  Image board_ul_corner_image;
  Image board_ur_corner_image;
  Image board_ll_corner_image;
  Image board_lr_corner_image;
  
  Image black_cursor;
  Image white_cursor;
   
  /*
  Image grid_ul_corner_image;
  Image grid_ur_corner_image;
  Image grid_ll_corner_image;
  Image grid_lr_corner_image;
   
  Image grid_top_image;
  Image grid_left_image;
  Image grid_right_image;
  Image grid_bottom_image;
  Image grid_hoshi_image;
 
  Image grid_cross_image;
   */
  
  //ArrayList <String>moveLine = new ArrayList<>();
  ArrayList <Move>moves = new ArrayList<>();
  ArrayList <Move>sgfMoves = new ArrayList<>();
  ArrayList <Stone>capturedStonesArray = new ArrayList<>();
  ArrayList <BoardMap>positionHistory = new ArrayList<>();
  ArrayList <SimplePosition>markedStones = new ArrayList<>();
   
  int[][] moveMap = new int[19][19];
  
 // int[][] groupMap = new int[19][19];
  int lastMoveColor=WHITE;
  int thisPlayerColor=BLACK;
  int moveNumber=0;

  AudioClip stoneSound;
  AudioClip errorSound;
  AudioClip cuckooSound;
   
  Group visibleMoves;
  Group boardGroup; 
  
  int handicapInt=0;
  int captured[]= new int[3];
   
  Text handicapVal= new Text("0");
  Text moveNoVal= new Text("0");
  Text capturedBlackVal = new Text("0");
  Text capturedWhiteVal= new Text("0");
  Text localMovesVal= new Text("0");
  Text gameNoVal= new Text("0");
  
  String userId = "noset";
  String password="noset";
  Hyperlink userNameLink;
  TextField userIdField;
  TextField passwordField; 
  Text opponentVal;
  Text gameLabel;
  
  DragonAccess dragonAccess;
  Move lastSgfMove;
  int lastSgfMoveNumber;
  int colorToPlay=WHITE;
  int localMoves=0;
  int handicap=0;
  
  
  String currentGameNo="noset";
  
  Button commitButton;
  Button deleteLastMoveButton;
  Button reviewForward_10_Button;
  Button reviewForwardButton;
  Button reviewBackwardButton;
  Button reviewBackward_10_Button;
  Button passButton;
  Button resignButton;
  Button pieceNumberButton;

  TextArea feedbackArea;
  
  TextArea sendMessageArea;
  TextArea receiveMessageArea;
  
  ImageView turnImageView;
  Image blackStoneImage;
  Image whiteStoneImage;
  Image smallerBlackStoneImage;
  Image smallerWhiteStoneImage;
  
  private Timeline timeline;
  private KeyFrame keyFrame;
  
  
  
 // Refresh refresh;
  int cycleCount=0;
  int totalCycleCount=0;
  StringBuffer refreshString=new StringBuffer();
  int level=0;
  int secondCounter=0;
  int cycles[] = new int[]{                1,           5,       100,         100,        100 };
  long interval[] = new long[]{          70,          130,       250,         490,         970};
  String[] timeStr = new String[]{"1 minute", "2 minutes", "4 minutes", "8 minutes", "16 minutes" };

  SimpleDateFormat df = new SimpleDateFormat("h:mm:ss MM-dd-yy");
  Text timedUpdateText; 
  Text gameStatusText;
  Stage stage;
  boolean localFile=false;
  HBox hGridLabels;
  
  
  String labelsStatus="off";
  Image horizLabelsOff;
  Image vertLabelsOff;
  ImageView horizLabelView;
  ImageView vertLabelView;
   
  Image horizSgfLabelsImage;
  Image vertSgfLabelsImage;
  Image vertRegularLabelsImage;
  Image horizRegularLabelsImage;
  Date lastTimedUpdate=null;
  int reviewPosition=0;  
  long lastMouseClick=0;
  int consecutivePasses=0;
  boolean gameOver=false;
  boolean passPlayed=false;
  boolean resignPlayed=false;
  boolean stoneSoundActive=true;
  String resourcesFileName="resources.properties";
  HBox mainBox;
  VBox rightPane;
  VBox smallRightPane;
  boolean rightPaneOn=true;
  Button minMaxButton;
  Button refreshButton;
  Button labelsButton;
  Button userButton;
  Button fileButton;
  
  HBox buttonBox;
  HBox buttonBox2;
  HBox identBox;
  
  Group infoGroup;
  Group feedbackLabelGroup;
  Group messageLabelGroup;
  int windowHeight = 730;
  int windowWidth =1012;
  
  int minStageHeight=745;
  int minStageWidth=761;
  int maxStageHeight=725;
  int maxStageWidth=1016;
  
		  
  double screenHeight=0;
  HBox minSizeBottomBox;
  Text messageLabel = new Text("");
  Text minFeedbackText = new Text();
  boolean maximized=true;
  ScrollPane scrollPane = new ScrollPane();
  VBox mainVBox = new VBox();
  Rectangle turnColorBar = new Rectangle(40, 375, Color.BLACK);
  private static int OFF = 0;
  private static int ON = 1;
  private static int SUBSET = 2;
  
  int pieceNumbering=OFF;
  Group grid = new Group();
  
 
  
  public void start(final Stage stage) throws Exception  
  {  
	this.stage=stage;  
    setQuit();
    importImages();
    getResources();
    
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
   // System.out.println("screen: "+primaryScreenBounds.getHeight()+" x "+primaryScreenBounds.getWidth());
    
    
    stoneSound = Applet.newAudioClip(GoClient.class.getClassLoader().getResource("resources/sounds/stone.wav"));
    errorSound = Applet.newAudioClip(GoClient.class.getClassLoader().getResource("resources/sounds/error.wav"));
    cuckooSound = Applet.newAudioClip(GoClient.class.getClassLoader().getResource("resources/sounds/cuckoo.wav"));

    turnImageView = new ImageView(blackStoneImage);
	  
    timedUpdateText = new Text("update Off");
	timedUpdateText.setFont(Font.font("Serif", 18));
	gameStatusText = new Text("");
	gameStatusText.setFont(Font.font("Serif", 20));
	gameStatusText.setFill(Color.RED);
    
    initiallizeMoveMap();
    captured[BLACK]=0;
    captured[WHITE]=0;
      
   
    
     mainBox = new HBox();
     mainBox.setPadding(new Insets(3, 3, 3, 3));
     mainBox.setSpacing(5);
    //mainBox.setStyle("-fx-background-color: DAE6F3; ");
     mainBox.setStyle("-fx-background-color:  DAE6F3; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work

    
     horizLabelView=new ImageView(horizLabelsOff);
     vertLabelView=new ImageView(vertLabelsOff);
     
    
     /*
     String style = "-fx-border-color: red;"
             + "-fx-border-width: 1;"
             + "-fx-border-style: dotted;";
   
     horizLabelView.setStyle(style);
     */
     
     /*
     final Menu menu1 = new Menu("File");
     final Menu menu2 = new Menu("Options");
     final Menu menu3 = new Menu("Help");
     
     MenuBar menuBar = new MenuBar();
     menuBar.getMenus().addAll(menu1, menu2, menu3);
     
     vBox.getChildren().add(menuBar);
     */
    
     mainVBox.getChildren().add(horizLabelView);
     
     setupBoardGroup();
     mainVBox.getChildren().add(boardGroup);
     
    
     HBox hBox = new HBox();
     hBox.getChildren().add(vertLabelView);
     hBox.getChildren().add(mainVBox);
    
     setupDeleteLastMoveButton();
     setupReviewBackwardButton();
     setupReviewBackward_10_Button();
     setupRefreshButton();
     setupReviewForward_10_Button();
     setupReviewForwardButton();
     setupCommitButton();
     setupMinMaxButton();
     setupPassButton();
     setupResignButton();
     setupLabelsButton();
     setupUserButton();
     setupFileButton();
     setupStoneNumberButton();
     
     //setupInfoGroup();
     setupFeedbackLabel();
     setupFeedbackArea();
     setupMessageLabel();
     setupSendMessageArea();
     
    // setupButtonBox();
   //  setupButtonBox2();
     setupIdentBox();
     setupRightPane();
     setupMinSizeBottomBox();
     
     mainBox.getChildren().add(hBox);
     mainBox.getChildren().add(rightPane);
   //  setupSmallRightPane();  
   mainBox.widthProperty().addListener(new ChangeListener<Number>() 
   {
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
       {
        if (rightPaneOn)
        {
        	if (screenHeight>maxStageHeight)
          stage.setHeight(maxStageHeight);
        	else
        	stage.setHeight(screenHeight-30);
        	
          stage.setWidth(maxStageWidth);
        }
        else
        {
          if (screenHeight>minStageHeight)
             stage.setHeight(minStageHeight);
          else
        	  stage.setHeight(screenHeight-30);  
          
          stage.setWidth(minStageWidth);
        }	
       }
   });

     
     
    
   
     
     scrollPane.setContent(mainBox);
    // scrollPane.setFitToHeight(true);
    // scrollPane.setFitToWidth(true);
    
     
     screenHeight= primaryScreenBounds.getHeight();
     if (screenHeight<windowHeight) windowHeight=(int)screenHeight-30;
     
    
     
     final Scene scene = new Scene(scrollPane, windowWidth, windowHeight);
    // scene.setCursor(new ImageCursor(blackStoneImage, blackStoneImage.getWidth()/2, blackStoneImage.getHeight()/2));
   //  boardGroup.setCursor(new ImageCursor(black_cursor, black_cursor.getWidth()/2, black_cursor.getHeight()/2));
      
    
     scene.setFill(null);
  
     
     
     
    //   stage.initStyle(StageStyle.TRANSPARENT);
    stage.setScene(scene);  
    
    stage.getIcons().add(smallerBlackStoneImage);
    stage.setTitle("DGS Pal");
  
    stage.show();  
    
    /*
    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
        public void handle(KeyEvent ke) {
            System.out.println("Key Pressed: " + ke.getCode().toString());
        }
    });
   */
    
    scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
        public void handle(KeyEvent ke) 
        {
            if (ke.getCode()==KeyCode.F9) toggleScore();
        }

		
    });
  
	
    if (!"noset".equals(password)) refreshStartup();
    else
    {
		feedbackArea.insertText(0, "User ID and password not set. Click on user icon in upper right corner.");
		return;
	}
      
  } // end of start method

 
  
  private void toggleScore() 
  {
	 System.out.println("toggle score");
	 int boardPos=0;
	 BoardPosition bp;
	 int piece=0;
	 
	 for(int x=0; x<19; x++)
	 {
	   for(int y=0; y<19; y++)
	   {
		 piece=moveMap[x][y];  
		// System.out.println("Board Position: "+boardPos+" piece: "+moveMap[x][y]);
		 bp = (BoardPosition)grid.getChildren().get(boardPos);
		 if (piece==0) bp.toggleCircle();
		 boardPos++;
	   }
	 }
		
  }
  
  private void setupMinSizeBottomBox() 
  {
	minSizeBottomBox = new HBox();
	messageLabel.setFont(Font.font("Serif", 18));
	minFeedbackText.setFont(Font.font("Serif", 18));
	minFeedbackText.setWrappingWidth(580);
	minSizeBottomBox.getChildren().add(messageLabel);
	minSizeBottomBox.getChildren().add(minFeedbackText);
}



public void stop()
  {
	//.cancel();
    // .purge();

  }
  
  private void refreshTimed()
  {
	//boolean gameFound=false; 
	Date now = new Date();
	long elapsedMillis=0;
	minFeedbackText.setFill(Color.BLACK);
	
	// check for rapid refresh calls because computer has been in sleep mode
	if (lastTimedUpdate!=null)
	{
	  elapsedMillis=now.getTime()-lastTimedUpdate.getTime();
	  if (elapsedMillis<3000) // 3 sec
	  {
		// System.out.println("Stopping autoRefresh") ; 
		stopAutoRefresh();
		lastTimedUpdate=null;
		feedbackArea.insertText(0, "restarting auto refresh\n");
		
		// System.out.println("Starting autoRefresh") ; 
		startAutoRefresh();
		return;
	  }
	}
		 
	int status = dragonAccess.checkForMove(timeStr[level]);
	boolean excessive_usage=dragonAccess.isExcessive_usage();
	if (excessive_usage) 
	{
	  level++;
	  feedbackArea.insertText(0, "DGS server complaining about excessive usage\n");
	 
	}

  
	//if (status==GAME_FOUND) enableCommit(); else commitButton.setDisable(true);
	refreshString = new StringBuffer();
	totalCycleCount++;
	refreshString.append("refresh rate: "+timeStr[level]+" ");
	 for(int i=0; i<totalCycleCount; i++)
	 {
	   refreshString.append(".");	 // dots show how many refreshes.
	 }
	 timedUpdateText.setText(refreshString.toString());
	 
	if (status==GAME_OVER) gameOver=true; 
    if (status==GAME_FOUND)
    {
      //System.out.println("timed refresh: game found "+gameNo);
      stopAutoRefresh();
      //deleteLastMoveButton.setDisable(true);
     // reviewForwardButton.setDisable(true);
      clear();
      if (getSgfFile(FROM_SERVER))
      {
  	    playAllSgfMoves();
         //feedbackArea.insertText(0, df.format(new Date())+" "+lastSgfMove.getColorStr()+": "+lastSgfMove.getBoardPosition()+"\n");
         minFeedbackText.setText(dragonAccess.message);
        // feedbackArea.insertText(0, dragonAccess.message);
        /*
        if (lastSgfMove.color==BLACK)  
	  { 
	    colorToPlay=WHITE; 
	    turnImageView.setImage(whiteStoneImage);
	    stage.getIcons().add(smallerWhiteStoneImage);
	  } 
	  else 
	  {
	    colorToPlay=BLACK;
	    turnImageView.setImage(blackStoneImage);
	    stage.getIcons().add(smallerBlackStoneImage);
	  }
        */
    }
      //System.out.println("timed refresh: currentMessage "+dragonAccess.currentMessage);
      if (dragonAccess.currentMessage) 
      {  
    	  cuckooSound.play();
    	  minFeedbackText.setFill(Color.RED);
    	  feedbackArea.insertText(0, "*"+lastSgfMove.getBoardPosition()+"\t"+dragonAccess.message);
      }
      else minFeedbackText.setFill(Color.BLACK);
  }
    
    updateControls();  
  lastTimedUpdate=new Date();  
}
  
  private boolean login()
  {
	dragonAccess=new DragonAccess(userId, password);
	if (dragonAccess.isTestMode()) resourcesFileName="test_resources.properties";
	return dragonAccess.login(); 
  }
  
  private void refreshStartup()
  {
	//boolean gameFound=false;
	//long gameNo=0;
	 // System.out.println("startup refresh");
	int status=NOT_LOGGED_IN;
	boolean loginSuccess=false;
	boolean debug=false;
	long runningGameNo=0;
	

	String loginSuccessStr="";
	String excessiveUsageStr="";
	String loadFromStr="";
	String commentsStr="";
	
	loginSuccess=login();
	if (loginSuccess)
	{
	   if (debug) System.out.println("login -ok");	
	   status=LOGGED_IN;
	   runningGameNo=dragonAccess.getRunningGame();
	   if (runningGameNo!=0) 
	   { 
		 currentGameNo=""+runningGameNo; 
		 status=GAME_FOUND; 
	   }
 	}
		
	gameNoVal.setText(currentGameNo);
	
	boolean success=false;
	if ((status==GAME_FOUND)||(status==NO_MOVE_WAITING)) 
	{
		if (debug) System.out.println("getting sgf file from server");
		success=getSgfFile(FROM_SERVER);
		loadFromStr="game loaded from server"+getErrorString(status)+"\n";
	}
	
	
	if (success)
	{	 
	commentsStr=getComments();
	playAllSgfMoves();
	
    if (colorToPlay!=thisPlayerColor) startAutoRefresh();
     
    if (!loginSuccess) loginSuccessStr="LOGIN FAILED - network down?"+"\n";
	}
	
	updateControls();
	//showVars("refreshStartup");
	feedbackArea.insertText(0, commentsStr);
	feedbackArea.insertText(0, loginSuccessStr);
	feedbackArea.insertText(0, loadFromStr);
	feedbackArea.insertText(0, excessiveUsageStr);
	minFeedbackText.setText(dragonAccess.message);
	//feedbackArea.insertText(0, dragonAccess.message);
	//System.out.println("startup refresh: currentMessage "+dragonAccess.currentMessage);
	if (dragonAccess.currentMessage)
	{	
	  cuckooSound.play();
	  minFeedbackText.setFill(Color.RED);
	}
	else minFeedbackText.setFill(Color.BLACK);
  }
  
  
  private String getErrorString(int status) 
  {
	  if (status==MIN_RETRY_TIME) return ": too soon for server refresh";
	  if (status==NOT_LOGGED_IN) return ": not logged in";
	  if (status==NO_MOVE_WAITING) return ": no move waiting";
	  if (status==GAME_FOUND) return ": game found";
	  if (status==EXCEPTION) return ": unknown exception";
	  if (status==LOGGED_IN) return ": logged in";
	return ": unhandled error";
  }



private String getComments()
  {
	  StringBuffer commentsBuffer = new StringBuffer();
	  ArrayList<String> comments = dragonAccess.getComments();
		ListIterator<String> it = comments.listIterator(comments.size());
		String comment;
		while(it.hasPrevious())
		{
		  comment=(String)it.previous();
		  commentsBuffer.append(comment+"\n");
		}
	 return "\n"+commentsBuffer.toString();
  }
  
  private void refreshLocal()
  {
	//boolean gameFound=false;
	String excessiveUsageStr="";
	String loadFromStr="";
	String dragonFeedback="";
	
	/*
	 * Always stop the  on a local refresh.
	 * Will start again if no game was found.
	 */
	stopAutoRefresh();
	
	if ("noset".equals(currentGameNo)) 
	{	
	  dragonFeedback=dragonAccess.getFeedback()+"\n";
	  return;
	}
	
	int status= dragonAccess.checkForMove();
	//long gameNo=0;
	//if (status>0) gameNo=status;
	
	boolean excessive_usage=dragonAccess.isExcessive_usage();
	if (excessive_usage) excessiveUsageStr="DGS server complaining about excessive usage\n";
	
	
	clear();
		
	boolean success=false;
	
	if (status==GAME_OVER) gameOver=true;
	if ((status==MIN_RETRY_TIME)||(status==NOT_LOGGED_IN)||(status==EXCEPTION))
	{
	  success= getSgfFile(FROM_LOCAL_FILE); 
	  loadFromStr="game loaded from local file"+getErrorString(status)+"\n";
	}
	else if ((status==GAME_FOUND)||(status==NO_MOVE_WAITING))
	{
	  success=getSgfFile(FROM_SERVER);
	  loadFromStr="game loaded from server"+getErrorString(status)+"\n";
	}
	/*
	 * (above) I want to get the game from the server unless
	 * there is an error condition. This will get the most recent version
	 */
		
		if (success)
        {
		  playAllSgfMoves();
		  
		  if (!(colorToPlay==thisPlayerColor)) startAutoRefresh();
		  /*
	      if (lastSgfMove.color==BLACK)  
	      { 
	        colorToPlay=WHITE; 
	        turnImageView.setImage(whiteStoneImage);
	        stage.getIcons().add(smallerWhiteStoneImage);
	      } 
	      else 
	      {
	        colorToPlay=BLACK;
	        turnImageView.setImage(blackStoneImage);
	        stage.getIcons().add(smallerBlackStoneImage);
	      }
	      //if (!gameFound) startAutoRefresh();
	      
	      if (colorToPlay==thisPlayerColor)
	  	{
	  	  enableCommit();
	  	  passButton.setDisable(false);
	  	  resignButton.setDisable(false);
	  	if (dragonAccess.isLoggedIn()) startAutoRefresh();
	  	}
	  	else 
	  	{
	  	  commitButton.setDisable(true);
	  	  passButton.setDisable(true);
	  	  resignButton.setDisable(true);
	  	}

	      
	     
	      deleteLastMoveButton.setDisable(true);
	      reviewForwardButton.setDisable(true);
	      reviewBackwardButton.setDisable(false);
	      */
        }
		
		  
		feedbackArea.insertText(0, dragonFeedback);
		feedbackArea.insertText(0, excessiveUsageStr);
		feedbackArea.insertText(0, loadFromStr);
		
		updateControls();
	//	showVars("refreshLocal");
  }
  
  void showVars(String call)
  {
	System.out.println("============ call: "+call);
	System.out.println("lastSgfMoveNumber: "+lastSgfMoveNumber);
	System.out.println("sgfMoves.size(): "+sgfMoves.size());
	System.out.println("thisPlayerColor: "+thisPlayerColor);
	System.out.println("colorToPlay: "+colorToPlay);
	System.out.println("localMoves: "+localMoves);
	System.out.println("reviewPosition: "+reviewPosition);
	System.out.println("moveNumber: "+moveNumber);
	System.out.println("consecutivePasses: "+consecutivePasses);
	System.out.println("gameOver: "+gameOver);
  }
  
  private boolean getSgfFile(int location)
  {
	boolean success=false;
	//System.out.println("Setting moveNumber to 0");
	moveNumber=0;
	if (location==FROM_LOCAL_FILE) success=dragonAccess.getLocalSgfFile(currentGameNo);
		else success= dragonAccess.getSgf(currentGameNo); 
	
	if (success)
	{	
	  updateVariables();
	}
	else
	{
	   gameLabel.setText("no game found");
	   feedbackArea.insertText(0, "No game SGF file Found\n");
	}
	return success;
  }
 
  
 private void updateVariables()
 {
	 sgfMoves=dragonAccess.getSgfMoves();
	  lastSgfMove=dragonAccess.getLastSgfMove();
	  lastSgfMoveNumber=dragonAccess.getLastSgfMoveNumber();
	  handicap=dragonAccess.getHandicap();
	  gameLabel.setText(dragonAccess.getPlayerBlack()+" vs "+dragonAccess.getPlayerWhite());
	  localFile=dragonAccess.isLocalFile();
	  gameNoVal.setText(""+dragonAccess.getCurrentGame());
	  if (userId!=null)
	  {
		if (userId.equals(dragonAccess.getLoginNameBlack())) thisPlayerColor=BLACK;
		else thisPlayerColor=WHITE;
	  }
 }
  
 
private void getResources() 
{
  File resourceFile=null;
  File directory = new File (".");
  try 
  {
	resourceFile= new File(directory.getCanonicalPath()+"\\"+resourcesFileName);
  } catch (IOException e) { e.printStackTrace(); }

  if (resourceFile!=null)
  {
	if (resourceFile.exists())
    {
	  try 
	  {
	    InputStream in = new FileInputStream(resourceFile);
	    InputStreamReader isr = new InputStreamReader(in);
	    BufferedReader br = new BufferedReader(isr);
	    String line;
		    
	    userId="noset";
	    password="noset";
	    currentGameNo="no game found";
	    while ((line = br.readLine()) != null) 
	    { 
	      if (line.startsWith("username: ")) userId = line.substring(10);
	      if (line.startsWith("password: ")) password = line.substring(10);
	    //  if (line.startsWith("gamenumb: ")) currentGameNo = line.substring(10);
	    }
	    
	    //System.out.println("username: "+userId);
	   // System.out.println("password: "+password);
	   // System.out.println("gameno: "+currentGameNo);
	  } catch (IOException io) { System.out.println("Ooops"); }
    }
	
  }
}

private void writeResources() 
{
	File resourceFile=null;
	  File directory = new File (".");
	  try 
	  {
		resourceFile= new File(directory.getCanonicalPath()+"\\"+resourcesFileName);
	  } catch (IOException e) { e.printStackTrace(); }

	FileWriter fstream;
	try 
	{
	  fstream = new FileWriter(resourceFile);
	  BufferedWriter out = new BufferedWriter(fstream);
	  out.write("username: "+userId+"\n");
	  out.write("password: "+password+"\n");
	  out.write("gamenumb: "+currentGameNo+"\n");
	  out.close();
	} catch (IOException e) { e.printStackTrace();}
}

private void setupRightPane() 
{
  rightPane = new VBox(); 
  rightPane.setPadding(new Insets(5,5,5,5)); // the border around the whole thing
  rightPane.setSpacing(4); // the spacing between rows
  
  setupButtonBox();
  setupButtonBox2();
  setupInfoGroup();
  
  rightPane.getChildren().add(buttonBox);
  rightPane.getChildren().add(buttonBox2);
  rightPane.getChildren().add(identBox);
  rightPane.getChildren().add(infoGroup);
  rightPane.getChildren().add(gameStatusText);
  rightPane.getChildren().add(timedUpdateText);
  rightPane.getChildren().add(feedbackLabelGroup);
  rightPane.getChildren().add(feedbackArea);
  rightPane.getChildren().add(messageLabelGroup);
 // rightPane.getChildren().add(getReceiveMessageBox());
  rightPane.getChildren().add(sendMessageArea);
  
}

private void setupSmallRightPane() 
{
  VBox vBox = new VBox(); 
  vBox.setPadding(new Insets(5,5,5,5)); // the border around the whole thing
  vBox.setSpacing(4); // the spacing between rows
    
  vBox.getChildren().add(deleteLastMoveButton);
  vBox.getChildren().add(reviewBackward_10_Button);
  vBox.getChildren().add(reviewBackwardButton);
  vBox.getChildren().add(refreshButton);
  vBox.getChildren().add(reviewForwardButton);
  vBox.getChildren().add(reviewForward_10_Button);
  vBox.getChildren().add(commitButton);
  vBox.getChildren().add(minMaxButton);
  vBox.getChildren().add(pieceNumberButton);
  //vBox.getChildren().add(getTestButton());
 // vBox.getChildren().add(turnImageView);
  turnColorBar.setArcHeight(20);
  turnColorBar.setArcWidth(20);
  vBox.getChildren().add(turnColorBar);
  
 // vBox.getChildren().add(commitButton);
  
  smallRightPane=vBox;
}

/*
private GridPane getRightPane() 
{
	GridPane gridPane = new GridPane(); 
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    gridPane.setVgap(2);
    gridPane.setHgap(5);
    
    gridPane.add(getButtonBox(), 0, 0);
    gridPane.add(getIdentBox(), 0, 1);
    gridPane.add(getInfoGroup(), 0, 2);
    gridPane.add(timedUpdateText, 0, 3);
    
    gridPane.add(getFeedbackLabel(), 0, 4);
    gridPane.add(getFeedbackBox(), 0, 5);
    
    gridPane.add(getMessageLabel(), 0, 6);
    gridPane.add(getReceiveMessageBox(), 0, 7);
    gridPane.add(getSendMessageBox(), 0, 8);
    
	return gridPane;
}
*/
  void setupFeedbackLabel()
  {
	  
	  Rectangle bx = new Rectangle();
	    bx.setWidth(300);
	    bx.setHeight(30);
	    bx.setArcWidth(10);
	    bx.setArcHeight(10); 
	    Color c =  Color.web("DAE6F3");
	    bx.setFill(c);
	   // new Color("DAE6F3")
	    feedbackLabelGroup = new Group();
	    
	    Text dragonInfoLabel = new Text("Dragon Info");
	    dragonInfoLabel.setFont(Font.font("Serif", 20));
	    dragonInfoLabel.setX(10);
	    dragonInfoLabel.setY(25);
	    
	    feedbackLabelGroup.getChildren().add(bx);
	    feedbackLabelGroup.getChildren().add(dragonInfoLabel);
	    
  }
  
  void setupMessageLabel()
  {
	  
	  Rectangle bx = new Rectangle();
	    bx.setWidth(300);
	    bx.setHeight(25);
	    bx.setArcWidth(10);
	    bx.setArcHeight(10); 
	    Color c =  Color.web("DAE6F3");
	    bx.setFill(c);
	   // new Color("DAE6F3")
	    messageLabelGroup = new Group();
	    
	    Text dragonInfoLabel = new Text("Message");
	    dragonInfoLabel.setFont(Font.font("Serif", 20));
	    dragonInfoLabel.setX(10);
	    dragonInfoLabel.setY(30);
	    
	    messageLabelGroup.getChildren().add(bx);
	    messageLabelGroup.getChildren().add(dragonInfoLabel);
	    
  }

  
  private void setupBoardGroup()
  {
	  boardGroup = new Group();
	  Group board = getBoardBackground();
	  getGrid();
	  visibleMoves = new Group();
	  boardGroup.getChildren().add(board);
	  boardGroup.getChildren().add(grid);
	  boardGroup.getChildren().add(visibleMoves);  
	  setupMouse(boardGroup);
	  
  }
  
  private void setupButtonBox() 
  {
	buttonBox = new HBox(); 
	buttonBox.setPadding(new Insets(3, 3, 3, 3));
	buttonBox.setSpacing(3);
 
	buttonBox.getChildren().add(deleteLastMoveButton);
	buttonBox.getChildren().add(reviewBackward_10_Button);
	buttonBox.getChildren().add(reviewBackwardButton);
	buttonBox.getChildren().add(refreshButton);
	buttonBox.getChildren().add(reviewForwardButton);
	buttonBox.getChildren().add(reviewForward_10_Button);
	buttonBox.getChildren().add(commitButton);
	
	buttonBox.getChildren().add(minMaxButton);
	
	//buttonBox.getChildren().add(getUserButton());
	
	//
  }
  
  private void setupButtonBox2() 
  {
	buttonBox2 = new HBox(); 
	buttonBox2.setPadding(new Insets(3, 3, 3, 3));
	buttonBox2.setSpacing(3);
	
	buttonBox2.getChildren().add(passButton);
	buttonBox2.getChildren().add(resignButton);
	buttonBox2.getChildren().add(labelsButton);
	buttonBox2.getChildren().add(userButton);
	buttonBox2.getChildren().add(fileButton);
	buttonBox2.getChildren().add(pieceNumberButton);
  }
 


  private void setupIdentBox() 
  {
    identBox = new HBox();
    identBox.setPadding(new Insets(3, 3, 3, 3));
    identBox.setSpacing(5);
   
    gameLabel = new Text("");
    gameLabel.setFont(Font.font("Serif", 18));
   
    identBox.getChildren().add(gameLabel);
  }
   
  private void setupInfoGroup()
  {
	infoGroup = new Group();
	Rectangle bx = new Rectangle();
	bx.setWidth(300);
	bx.setHeight(200);
	bx.setArcWidth(20);
	bx.setArcHeight(20); 
//	kjkj
	bx.setFill(Color.AZURE);
	// bx.setStyle("-fx-background-color: DAE6F3;");
	    
	Text gameNoLabel = new Text("Game #:");
	gameNoLabel.setFont(Font.font("Serif", 20));
	gameNoVal.setFont(Font.font("Serif", 20));
	    
	Text moveNoLabel = new Text("Move #:");
	moveNoLabel.setFont(Font.font("Serif", 20));
	moveNoVal.setFont(Font.font("Serif", 20));
	      
	Text handicapLabel = new Text("Handicap:");
	handicapLabel.setFont(Font.font("Serif", 20));
    handicapVal.setFont(Font.font("Serif", 20));
	    
	Text capturedBlackLabel = new Text("Captured Black:");
	capturedBlackLabel.setFont(Font.font("Serif", 20));
    capturedBlackVal.setFont(Font.font("Serif", 20));
	    
    Text capturedWhiteLabel = new Text("Captured White:");
    capturedWhiteLabel.setFont(Font.font("Serif", 20));
    capturedWhiteVal.setFont(Font.font("Serif", 20));
	    
    Text localMovesLabel = new Text("Local Moves:");
    localMovesLabel.setFont(Font.font("Serif", 20));
    localMovesVal.setFont(Font.font("Serif", 20));
	    
    Text turnLabel = new Text("Turn: ");
    turnLabel.setFont(Font.font("Serif", 20));
	    
    GridPane gridPane = new GridPane();
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    gridPane.setVgap(2);
    gridPane.setHgap(15);
        
    int row=0;
    gridPane.add(gameNoLabel, 0, row);
    gridPane.add(gameNoVal, 1, row++);
        
    gridPane.add(handicapLabel, 0, row);
    gridPane.add(handicapVal, 1, row++);
        
    gridPane.add(capturedBlackLabel, 0, row);
    gridPane.add(capturedBlackVal, 1, row++);
        
    gridPane.add(capturedWhiteLabel, 0, row);
    gridPane.add(capturedWhiteVal, 1, row++);
        
    gridPane.add(moveNoLabel, 0, row);
    gridPane.add(moveNoVal, 1, row++);
        
    gridPane.add(localMovesLabel, 0, row);
    gridPane.add(localMovesVal, 1, row++);
        
    gridPane.add(turnLabel,0,row);
    gridPane.add(turnImageView,1,row++);
        
    GridPane.setHalignment(handicapLabel, HPos.RIGHT);
    GridPane.setHalignment(capturedBlackLabel, HPos.RIGHT);
    GridPane.setHalignment(capturedWhiteLabel, HPos.RIGHT);
    GridPane.setHalignment(localMovesLabel, HPos.RIGHT);
    GridPane.setHalignment(moveNoLabel, HPos.RIGHT);
    GridPane.setHalignment(gameNoLabel, HPos.RIGHT);
    GridPane.setHalignment(turnLabel, HPos.RIGHT);
        
    infoGroup.getChildren().add(bx);
	infoGroup.getChildren().add(gridPane);
	    
  }

 private void commit()
 {
   boolean success=false;  
   unmarkGroup();
   Move firstLocalMove = moves.get(lastSgfMoveNumber);  
   
   //success=dragonAccess.makeMove(currentGameNo, 
   //  		                      lastSgfMove.sgfPosition,
    // 		                      firstLocalMove.getSgfPosition(), 
   //  		                      firstLocalMove.color,
    // 		                      sendMessageArea.getText()
    // 		                      );
   
   if (resignPlayed)
   {
   success=dragonAccess.resign(currentGameNo, 
            lastSgfMoveNumber,
            "resign", 
            sendMessageArea.getText()
            );
   }
   else if (passPlayed)
   {
   success=dragonAccess.makeMove2(currentGameNo, 
            lastSgfMoveNumber,
            "pass", 
            sendMessageArea.getText()
            );
     if (success) consecutivePasses++;
   }
   else
   {
	   success=dragonAccess.makeMove2(currentGameNo, 
	            lastSgfMoveNumber,
	            firstLocalMove.getSgfPosition(), 
	            sendMessageArea.getText()
	            );
	   }  
     
   if (success) 
   { 
     writeMoveToLocalSgfFile(firstLocalMove);
     
     //feedbackArea.insertText(0, df.format(new Date())+" "+firstLocalMove.getColorStr()+": "+firstLocalMove.getBoardPosition()+"\n");
     //commitButton.setDisable(true); 
    
	  
	 //gameStatusText.setText(colorStr(firstLocalMove.color)+" GAME OVER");
     if (sendMessageArea.getText()!=null)
     {	
       if (sendMessageArea.getText().length()>0) feedbackArea.insertText(0, "* "+sendMessageArea.getText()+"\n");
     }
     
     sendMessageArea.setText(""); 
        
     removeMoveImageFromLastSgfMove();
     putMoveImageOnCommittedStone();
     //deleteLastMoveButton.setDisable(true);
    // reviewBackwardButton.setDisable(false);
     getSgfFile(FROM_LOCAL_FILE);
     stoneSoundActive=false;
     String commentsStr=getComments();
     playAllSgfMoves();
     feedbackArea.clear();
     feedbackArea.setText(commentsStr);
     minFeedbackText.setText(dragonAccess.message);
     //feedbackArea.insertText(0, dragonAccess.message);
    // feedbackArea.setText(value));
     passPlayed=false;
     resignPlayed=false;
     //updateControls();
    
         
       /* 
     if (firstLocalMove.color==BLACK)  
     { 
       colorToPlay=WHITE; 
       turnImageView.setImage(whiteStoneImage);
       stage.getIcons().add(smallerWhiteStoneImage);
     } 
     else 
     {
       colorToPlay=BLACK;
       turnImageView.setImage(blackStoneImage);
       stage.getIcons().add(smallerBlackStoneImage);
     }
        */
     
     lastSgfMoveNumber++;
     sgfMoves.add(firstLocalMove);
     //System.out.println("commit, lastSgfMoveNumber: "+lastSgfMoveNumber);
     lastSgfMove=firstLocalMove;
     
     /*
     if (localMoves>1)
     {
       for(int i=0; i<localMoves; i++)
       {
    	  deleteLastStone();
       }
     }
    	*/ 
     localMoves=0;
    // localMovesVal.setText("0");
     startAutoRefresh();
     updateControls();
   }
   else feedbackArea.insertText(0, dragonAccess.getFeedback()+"\n");
	 
 }

  private void setupCommitButton() 
  {
    commitButton = new Button("");
    Image checkImage  = new Image(GoClient.class.getResourceAsStream("/resources/images/check.png"));
    
    commitButton.setGraphic(new ImageView(checkImage));
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event)  { commit(); }};
	  
     commitButton.setOnMouseClicked(bHandler);
     commitButton.setTooltip(new Tooltip("Commit Move"));
     commitButton.setPrefHeight(28);
  }
  
  private void setupFileButton() 
  {
	
    fileButton = new Button("File");
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event)  
	          { 
	        	  //System.out.println("file button"); 
	        	  File directory = new File (".");
	        	  FileChooser fileChooser = new FileChooser();
	                
	                //Set extension filter
	        	  
	        	    File dir2=null;
					try {
						dir2 = new File(directory.getCanonicalPath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	    fileChooser.setInitialDirectory(dir2);
	                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SGF files (*.sgf)", "*.sgf");
	                fileChooser.getExtensionFilters().add(extFilter);
	                
	                //Show save file dialog
	                File file = fileChooser.showOpenDialog(stage);
	                if(file != null)
	                {
	                   dragonAccess.getLocalSgfFile(file);
	                   updateVariables();
	                   playAllSgfMoves();
	                   updateControls();
	                   
	                }
	          }};
	  
	          fileButton.setOnMouseClicked(bHandler);
	          fileButton.setTooltip(new Tooltip("Commit Move"));
	          fileButton.setPrefHeight(28);
  }
  
  private void writeMoveToLocalSgfFile(Move move)
  {
	ArrayList<String>  sgfFileLine=dragonAccess.getSgfFileLine();
	String colorLetter="";
	String moveLine="";
	String commentLine=null;
	boolean comment=false;
	
	if (move.color==1) colorLetter="B"; else colorLetter="W";
	if (move.isPass()) moveLine=";"+colorLetter+"[]\n";
	else moveLine=";"+colorLetter+"["+move.getSgfPosition()+"]\n";
	
	if (sendMessageArea.getText().length()>0) 
	{	
		comment=true;
		if (move.color==1)
		commentLine="C["+dragonAccess.getPlayerBlack()+" ("+dragonAccess.getLoginNameBlack()+"): "+sendMessageArea.getText()+"]\n";
		else
			commentLine="C["+dragonAccess.getPlayerWhite()+" ("+dragonAccess.getLoginNameWhite()+"): "+sendMessageArea.getText()+"]\n";	
	}
	File resourceFile=null;
	  File directory = new File (".");
	try{
		resourceFile= new File(directory.getCanonicalPath()+"\\"+currentGameNo+".sgf");
	    FileWriter fstream=null;
	
		fstream = new FileWriter(resourceFile);
	    BufferedWriter out = new BufferedWriter(fstream);
	    
	    if (sgfFileLine!=null)
	    {
	    Iterator it = sgfFileLine.iterator();
	    
	    while(it.hasNext())
	    {
	    	out.write((String)it.next()+"\n");
	    }
	    out.write(moveLine);
	    if (comment) out.write(commentLine);
	    out.write(")\n");  // add the last bracket
	    }
	    else System.out.println("sgfFileLine is null!");
	    	
	    out.close();
 } catch (IOException e) { e.printStackTrace();	}
  }
  
  
  
  private Button getTestButton() 
  {
    Button commitTestButton = new Button("Test");
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event) 
	          {
	        	System.out.println("TEST BUTTON");
	          }

			};

   
	  
     commitTestButton.setOnMouseClicked(bHandler);
     return commitTestButton;
  }
  
  private void setupStoneNumberButton() 
  {
    pieceNumberButton = new Button("1,2,3");
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event) 
	          {
	        	if (pieceNumbering==OFF) pieceNumbering=SUBSET;
	        	else if (pieceNumbering==SUBSET) pieceNumbering=ON;
	        	else pieceNumbering=OFF;
	        	
	        	//System.out.println("piece Numbering: "+pieceNumbering);
	        	if (pieceNumbering==ON) { removeStoneNumbers(); showStoneNumbers(); }
	        	else if (pieceNumbering==SUBSET) { removeStoneNumbers(); showSubsetStoneNumbers(); }
	        	else removeStoneNumbers();
	          }

			};
	  
			pieceNumberButton.setOnMouseClicked(bHandler);
			pieceNumberButton.setPrefHeight(28);
			pieceNumberButton.setTooltip(new Tooltip("Show piece numbers"));
  }
  
  
  
  
    
  private void setupMinMaxButton() 
  {
    minMaxButton = new Button("Min");
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event) 
	          {
	        	if (rightPaneOn)  
	        	{
	        	  mainVBox.getChildren().add(minSizeBottomBox);	
	        	  setupSmallRightPane();
	        	  mainBox.getChildren().remove(rightPane);
	        	  mainBox.getChildren().add(smallRightPane);
	        	  
	        	  messageLabel.setText("Message: ");
	        	  if (dragonAccess!=null) 
	        	  {
	        		  minFeedbackText.setText(dragonAccess.message);
	        		 // feedbackArea.insertText(0, dragonAccess.message);
	        	  }
	        	  minMaxButton.setText("Max");
	        	  minMaxButton.setTooltip(new Tooltip("Maximum Window"));
	        	  maximized=false;
	        	}
	        	else
	        	{
	        	  //setupButtonBox();
	        	  //setupInfoGroup();
	        	  setupRightPane();
	        	  
	        	  mainVBox.getChildren().remove(minSizeBottomBox);
	        	  messageLabel.setText("");
	        	  minFeedbackText.setText("");
	        	  
	        	  mainBox.getChildren().remove(smallRightPane);
		          mainBox.getChildren().add(rightPane);
		          
		          minMaxButton.setText("Min");
		          minMaxButton.setTooltip(new Tooltip("Minimum Window"));
		          maximized=true;
		          
		        }	
	        	rightPaneOn=!rightPaneOn;
	        	
	          } };
	  
     minMaxButton.setOnMouseClicked(bHandler);
     minMaxButton.setPrefHeight(28);
     
  }
  
  private void setupUserButton() 
  {
    userButton = new Button("");
    Image userImage  = new Image(GoClient.class.getResourceAsStream("/resources/images/user.png"));
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
    	public void handle(MouseEvent event) {
            final Stage myDialog = new Stage();
         myDialog.initModality(Modality.APPLICATION_MODAL);
            Button okButton = new Button("SAVE");
            okButton.setOnAction(new EventHandler<ActionEvent>(){

                public void handle(ActionEvent arg0) {
               	  userId=userIdField.getText();
               	  password=passwordField.getText();
        			//  System.out.println("userId: "+userId);
        			//  System.out.println("password: "+password);
        			  writeResources();
                     myDialog.close();
                     login();
                }
            });
      
            userIdField = new TextField();
            userIdField.setText(userId);
            userIdField.setPrefColumnCount(30);
            Text userIdLabel = new Text("User ID: ");
            userIdLabel.setFont(Font.font("Serif", 20));
            Text passwordLabel = new Text("Password: ");
            passwordLabel.setFont(Font.font("Serif", 20));
            passwordField = new TextField();
            passwordField.setText(password);
            passwordField.setPrefColumnCount(30);
            
            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.setVgap(2);
            gridPane.setHgap(5);
            gridPane.add(userIdLabel, 0, 0);
            gridPane.add(userIdField, 1, 0);
            gridPane.add(passwordLabel, 0, 1);
            gridPane.add(passwordField, 1, 1);
            gridPane.add(okButton, 1, 2);
            
            Scene myDialogScene = new Scene(gridPane, 300, 100);
            myDialog.setScene(myDialogScene);
            myDialog.show();
        }
	          
    
    };
	  
     userButton.setOnMouseClicked(bHandler);
     userButton.setGraphic(new ImageView(userImage));
     userButton.setPrefHeight(28);
     userButton.setTooltip(new Tooltip("Set DGS username and password"));
  }
  

  private void setupRefreshButton() 
  {
    refreshButton = new Button("*");
    // b.setLayoutX(10);
    // b.setLayoutY(10);
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event) 
	          {
	        	reviewPosition=0;
	        	//commitButton.setDisable(true);
	        	//reviewForwardButton.setDisable(true);
	        	refreshLocal();
	          } };
	  
     refreshButton.setOnMouseClicked(bHandler);
     refreshButton.setTooltip(new Tooltip("Go to Current Game Position"));
     refreshButton.setPrefHeight(28);
  }

  private void setupMouse(Group boardGroup) 
  {
	
    boardGroup.setOnMouseClicked(new EventHandler<MouseEvent>() {public void handle(MouseEvent t) 
    { 
      // de-bounce	
      long thisMouseClick=new Date().getTime();	
      if (thisMouseClick<lastMouseClick+500) return;
      lastMouseClick=new Date().getTime();
      
      MouseButton mb= t.getButton();
      
      if (mb==MouseButton.PRIMARY)
      {
        int thisMoveColor=0;
        if (lastMoveColor==BLACK) thisMoveColor=WHITE;  else thisMoveColor=BLACK;
        Move move = new Move(t.getX(),t.getY(), thisMoveColor);
        move.setMoveNumber(moveNumber);
        if (moveMap[move.x][move.y]!=OPEN) 
        { 
      	  StoneGroup stoneGroup = new StoneGroup(move.x, move.y, moveMap);
          feedbackArea.insertText(0,"group liberties: "+stoneGroup.liberties+"\n");
          markGroup(stoneGroup.groupPositions);
  	      return;        
        }
      
        if (gameOver) return;
      
        boolean testForIllegalMoves=true;

        if (testForIllegalMoves)
        {
          BoardMap savedBoardMap = new BoardMap(moveMap);
      
          moveMap[move.x][move.y]=move.color;	
          boolean captures = checkLibertiesOfNeighbors(move.x, move.y, TRIAL);
            
        //System.out.println("captures: "+captures);
          if (captures==false)
          {
        	StoneGroup stoneGroup = new StoneGroup(move.x, move.y, moveMap);         
    	    if (stoneGroup.liberties==0) 
    	    { 
    		  // System.out.println("liberties 0");  
    	      errorSound.play(); 
    	      restoreMoveMap(savedBoardMap.get());
    	      feedbackArea.insertText(0, "illegal move\n");
    	      return;  // can't move here... no liberties and nothing captured.
    	    }
          }
          else 
          {  
            if (checkForKo()) 
            {
      	      errorSound.play(); 
              restoreMoveMap(savedBoardMap.get());
    	      feedbackArea.insertText(0, "ko. can't move here.\n");
    	      return;  // can't move here... ko fight. 
            }
            // System.out.println("no ko");
          }
          restoreMoveMap(savedBoardMap.get());  // return moveMap to original state.
          //System.out.println("");
        }  // end of testing illegal moves
      
        placeStone(move);
	    localMoves++;
	    stoneSound.play();
	    markLocalMove();
	    updateControls();
      }
      
      if (mb==MouseButton.SECONDARY) 
      {	  
    	  String sgfPosition=Move.getSgfPosition(t.getX(),t.getY());
    	  System.out.println("right click: "+sgfPosition);
    	  
    	  BoardPosition bp;
    	  Iterator it = grid.getChildren().iterator();
    	  while(it.hasNext())
    	  {
    		bp=(BoardPosition)it.next();
    		if (bp.getSgfPosition().equals(sgfPosition)) bp.toggleCircle();
    	  }
      }
    }
    });
     
  }
  
  private void markGroup(List<SimplePosition> groupPositions) 
  {
	unmarkGroup();  
	Iterator<SimplePosition> it = groupPositions.iterator();
	SimplePosition position;
	Stone stone;
	
	while(it.hasNext())
	{
	  position=(SimplePosition)it.next();
	  stone=getStone(position);
	  if (stone!=null) 
	  {	  
		stone.setMarkImage();
		markedStones.add(position);
	  }
	}
		
  }
	
  private void unmarkGroup()
  {
	if (markedStones.size()==0) return;
	
	Iterator<SimplePosition> it = markedStones.iterator();
	SimplePosition position;
	Stone stone;
	
	while(it.hasNext())
	{
	  position=(SimplePosition)it.next();
	  stone=getStone(position);
	  if (stone!=null) 
	  {	  
		stone.setRegularImage();
	  }
	}
	
	markedStones = new ArrayList<>();
	//markLastSgfStone();
	markLastSgfStone();
  }
  
  @SuppressWarnings("rawtypes")
  private Stone getStone(SimplePosition position) 
  {
	ObservableList moveList  = visibleMoves.getChildren();
	ListIterator it = moveList.listIterator(moveList.size());
	              
	   Stone stone;
	   while(it.hasPrevious())
	   {
	     stone=(Stone)it.previous();
	     if ((stone.x==position.x)&&(stone.y==position.y)) 
	     { 
	       return stone;
	     }
	   }
	   return null;
  } 
  
  @SuppressWarnings("rawtypes")
  private void showStoneNumbers() 
  {
	ObservableList moveList  = visibleMoves.getChildren();
	ListIterator it = moveList.listIterator();
	
	   Stone stone;
	   Move move;
	   while(it.hasNext())
	   {
	     stone=(Stone)it.next();
	     stone.getStoneNumberLabel().setVisible(true);
	   }
  } 
  
  @SuppressWarnings("rawtypes")
  private void removeStoneNumbers() 
  {
	ObservableList moveList  = visibleMoves.getChildren();
	ListIterator it = moveList.listIterator();
	
	   Stone stone;
	   Move move;
	   while(it.hasNext())
	   {
	     stone=(Stone)it.next();
	     stone.getStoneNumberLabel().setVisible(false);
	     stone.getSubsetStoneNumberLabel().setVisible(false);
	   }
  } 
  
  
  
  @SuppressWarnings("rawtypes")
  private void showSubsetStoneNumbers() 
  {
	ObservableList moveList  = visibleMoves.getChildren();
	int numberOfMoves =moveList.size();
	int listCount=0;
	int subsetNumber=0;
	String numberString="";
	int numberLength=0;
	Label subsetStoneNumberLabel;
	
	ListIterator it = moveList.listIterator();
	
	Stone stone;
	
	while(it.hasNext())
	{
	  listCount++;  
	  if (listCount>numberOfMoves-9) subsetNumber++;
	  stone=(Stone)it.next();
	  
	  numberString=""+subsetNumber;
	  numberLength=numberString.length();
	  
	  subsetStoneNumberLabel=stone.getSubsetStoneNumberLabel();
	  subsetStoneNumberLabel.setText(numberString);
	  
	 
	  
	  
	  if (subsetNumber>0) subsetStoneNumberLabel.setVisible(true);
	}
	 
  } 

private Label getStoneNumber(Move move, int number)
{
   int stoneNumber=0;
   if (number>0) stoneNumber=number;
   else stoneNumber=move.getMoveNumber();
   
   Label stoneNumberLabel = new Label(""+stoneNumber);
   stoneNumberLabel.setFont(Font.font("Serif", 15));
   
   
   if (move.color==WHITE) stoneNumberLabel.setTextFill(Color.BLACK);
   else stoneNumberLabel.setTextFill(Color.WHITE);
   
   int numberLength=(""+stoneNumber).length();
   double x=0;
   
   if (numberLength==3) stoneNumberLabel.setLayoutX(move.sceneX+5);
   else if (numberLength==2) stoneNumberLabel.setLayoutX(move.sceneX+9);
   else  stoneNumberLabel.setLayoutX(move.sceneX+13);
   
   stoneNumberLabel.setLayoutY(move.sceneY+9);
 //  System.out.println("move: "+move.getMoveNumber()+" "+move.sceneX+" - "+move.sceneY);
   return stoneNumberLabel;
}


void restoreMoveMap(int[][] savedMoveMap)
  {
	  for(int j=0; j<19; j++)
		{
		  for(int i=0; i<19; i++)
		  {
			moveMap[i][j]=savedMoveMap[i][j];
		  }
		}
	  
  }

  
  private void setupDeleteLastMoveButton() 
  {
    deleteLastMoveButton = new Button("< x");
    EventHandler bHandler2 = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
      unmarkGroup();
      if (resignPlayed)
      {
    	  moves.remove(moves.size()-1);
    	  localMoves--;
    	  resignPlayed=false;
    	  gameStatusText.setText("");
      }
      if (passPlayed)
      {
    	  moves.remove(moves.size()-1);
    	  localMoves--;
    	  passPlayed=false;
    	  gameStatusText.setText("");
      }
      else
      deleteLastStone(); 
      updateControls();
    }};
    deleteLastMoveButton.setOnAction(bHandler2);
    deleteLastMoveButton.setPrefHeight(28);
    deleteLastMoveButton.setTooltip(new Tooltip("Delete Last Move"));
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void setupLabelsButton() 
  {
    labelsButton = new Button("a/1");
    EventHandler bHandler2 = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
      toggleLabels(); 
    }};
    labelsButton.setOnAction(bHandler2);
    labelsButton.setPrefHeight(28);
    labelsButton.setTooltip(new Tooltip("Toggle board coordinates on/off"));
  }
  
  private void setupPassButton() 
  {
    passButton = new Button("pass");
    EventHandler bHandler2 = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
     // System.out.println("pass button "+thisPlayerColor);
      playPass(new Move(thisPlayerColor));
      localMoves++;
      passPlayed=true;
     // deleteLastMoveButton.setDisable(false);
     // localMovesVal.setText(""+localMoves); 
      updateControls();
    }};
    passButton.setOnAction(bHandler2);
    passButton.setPrefHeight(28);
    passButton.setTooltip(new Tooltip("Pass"));
  }
  
  private void setupResignButton() 
  {
    resignButton = new Button("resign");
    EventHandler bHandler2 = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
     // System.out.println("resign button");
     // System.out.println("pass button "+thisPlayerColor);
      Move m = new Move(thisPlayerColor);
     
      playResign(m);
      localMoves++;
      resignPlayed=true;
      updateControls();
    }};
    resignButton.setOnAction(bHandler2);
    resignButton.setPrefHeight(28);
    resignButton.setTooltip(new Tooltip("resign"));
  }
  
  private void setupReviewForwardButton() 
  {
    reviewForwardButton = new Button(">");
    reviewForwardButton.setFont(Font.font("Serif", 12));
    EventHandler bHandler2 = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
      // review forward
      
      playNextStone();
     // System.out.println("reviewPosition: "+reviewPosition);
      updateControls();
      /*
     
      */
    }};
    reviewForwardButton.setOnAction(bHandler2);
    reviewForwardButton.setPrefHeight(28);
    reviewForwardButton.setTooltip(new Tooltip("Play Moves Forward"));
  }
  
  private void setupReviewForward_10_Button() 
  {
    reviewForward_10_Button = new Button(">>");
    reviewForward_10_Button.setFont(Font.font("Serif", 12));
    EventHandler bHandler2 = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
      int maxForward=10;
      if (reviewPosition>(-10))
    	  maxForward=-reviewPosition; 
      for(int i=0; i<maxForward; i++)
      {
        playNextStone();
      }
      // System.out.println("reviewPosition: "+reviewPosition);
      updateControls();
      /*
     
      */
    }};
    reviewForward_10_Button.setOnAction(bHandler2);
    reviewForward_10_Button.setPrefHeight(28);
    reviewForward_10_Button.setTooltip(new Tooltip("Play Moves Forward 10"));
  }
  
  private void setupReviewBackwardButton() 
  {
    reviewBackwardButton = new Button("<");
    reviewBackwardButton.setFont(Font.font("Serif", 12));
    EventHandler bHandler2 = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
      rewindLastStone();
      updateControls();
    }};
    reviewBackwardButton.setOnAction(bHandler2);
    reviewBackwardButton.setTooltip(new Tooltip("Rewind Moves"));
    reviewBackwardButton.setPrefHeight(28);
  }
  
  private void setupReviewBackward_10_Button() 
  {
    reviewBackward_10_Button = new Button("<<");
    reviewBackward_10_Button.setFont(Font.font("Serif", 12));
    EventHandler bHandler2 = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
      int maxMove=10;
      if (moveNumber<10) maxMove=moveNumber;
      
      for(int i=0; i<maxMove; i++)
      {
        rewindLastStone();
      }
 
      updateControls();
    }};
    reviewBackward_10_Button.setOnAction(bHandler2);
    reviewBackward_10_Button.setTooltip(new Tooltip("Rewind Moves 10"));
    reviewBackward_10_Button.setPrefHeight(28);
  }
  
  void toggleLabels()
  {
	 if ("off".equals(labelsStatus))
	 {
	   horizLabelView.setImage(horizRegularLabelsImage);
	   vertLabelView.setImage(vertRegularLabelsImage);
	   labelsStatus="reg";
	 }
	// else if ("reg".equals(labelsStatus))
	// {
	//	   horizLabelView.setImage(horizSgfLabelsImage);
	//	   vertLabelView.setImage(vertSgfLabelsImage);
	//	   labelsStatus="sgf";
	//}
	else 
	 {
	   horizLabelView.setImage(horizLabelsOff);
	   vertLabelView.setImage(vertLabelsOff);
	   labelsStatus="off";
	 }
  }
  
  boolean checkForKo()
  {
	int historySize=positionHistory.size();
	
	if (historySize>2)
	{
	  BoardMap bm = positionHistory.get(historySize-2);
	  if (bm.equals(moveMap)) return true;
	}
	return false;
  }
  
  private void initiallizeMoveMap() 
  {
    for(int i=0; i<19; i++)
    {
      for(int j=0; j<19; j++)
      {
	    moveMap[i][j]=OPEN;
      }
    }
  }
  
 
   private void importImages()
   {
	 String src="/resources";  
	 board_ul_corner_image = new Image(GoClient.class.getResourceAsStream(src+"/images/wood4_ul.gif"));
	 board_ur_corner_image = new Image(GoClient.class.getResourceAsStream(src+"/images/wood4_ur.gif"));
	 board_ll_corner_image = new Image(GoClient.class.getResourceAsStream(src+"/images/wood4_dl.gif"));
	 board_lr_corner_image = new Image(GoClient.class.getResourceAsStream(src+"/images/wood4_dr.gif"));
	 
	 board_top_image = new Image(GoClient.class.getResourceAsStream(src+"/images/wood4_u.gif"));
	 board_left_image = new Image(GoClient.class.getResourceAsStream(src+"/images/wood4_l.gif"));
	 board_right_image = new Image(GoClient.class.getResourceAsStream(src+"/images/wood4_r.gif"));
	 board_bottom_image = new Image(GoClient.class.getResourceAsStream(src+"/images/wood4_d.gif"));
	
	 board_fill_image = new Image(GoClient.class.getResourceAsStream(src+"/images/wood4.gif"));
	 black_cursor = new Image(GoClient.class.getResourceAsStream(src+"/images/black_cursor.png"));
	 white_cursor = new Image(GoClient.class.getResourceAsStream(src+"/images/white_cursor.png"));
	 
	 
	 /*
	 grid_ul_corner_image = new Image(GoClient.class.getResourceAsStream(src+"/images/ul.gif"));
	 grid_ur_corner_image = new Image(GoClient.class.getResourceAsStream(src+"/images/ur.gif"));
	 grid_ll_corner_image = new Image(GoClient.class.getResourceAsStream(src+"/images/dl.gif"));
	 grid_lr_corner_image = new Image(GoClient.class.getResourceAsStream(src+"/images/dr.gif"));
	 
	 grid_top_image = new Image(GoClient.class.getResourceAsStream(src+"/images/u.gif"));
	 grid_left_image = new Image(GoClient.class.getResourceAsStream(src+"/images/el.gif"));
	 grid_right_image = new Image(GoClient.class.getResourceAsStream(src+"/images/er.gif"));
	 grid_bottom_image = new Image(GoClient.class.getResourceAsStream(src+"/images/d.gif"));
	 
	 grid_cross_image = new Image(GoClient.class.getResourceAsStream(src+"/images/e.gif"));
	 grid_hoshi_image = new Image(GoClient.class.getResourceAsStream(src+"/images/h.gif"));
	 */
	    
	    blackStoneImage = new Image(Stone.class.getResourceAsStream(src+"/images/b.gif")); 
	    whiteStoneImage = new Image(Stone.class.getResourceAsStream(src+"/images/w.gif")); 
	    smallerBlackStoneImage = new Image(Stone.class.getResourceAsStream(src+"/images/smaller_b.gif")); 
	    smallerWhiteStoneImage = new Image(Stone.class.getResourceAsStream(src+"/images/smaller_w.gif")); 
	   
	    horizSgfLabelsImage = new Image(Stone.class.getResourceAsStream(src+"/images/horizLabels.png")); 
	    horizLabelsOff = new Image(Stone.class.getResourceAsStream(src+"/images/horizLabelsOff.png")); 
	    vertSgfLabelsImage = new Image(Stone.class.getResourceAsStream(src+"/images/vertSgflabels.png")); 
	    vertLabelsOff = new Image(Stone.class.getResourceAsStream(src+"/images/vertLabelsOff.png")); 
		 
	    vertRegularLabelsImage = new Image(Stone.class.getResourceAsStream(src+"/images/vertNumbLabels.png")); 
	    horizRegularLabelsImage = new Image(Stone.class.getResourceAsStream(src+"/images/horizRegLabels.png")); 
   }
   
   private void setupFeedbackArea()
   {
	   feedbackArea = TextAreaBuilder.create()
               .prefWidth(200)
               .prefHeight(145)
               .wrapText(true)
               .build();
       
   }
   
   private void setupSendMessageArea()
   {
	   sendMessageArea = TextAreaBuilder.create()
               .prefWidth(200)
               .prefHeight(70)
               .wrapText(true)
               .build();
       
   }
   
   private TextArea getReceiveMessageBox()
   {
	   receiveMessageArea = TextAreaBuilder.create()
               .prefWidth(200)
               .prefHeight(70)
               .wrapText(true)
               .build();
	   
	   Color c =  Color.web("DAE6F3");
	   receiveMessageArea.setStyle("-fx-background-color: lightgoldenrodyellow;");
	   receiveMessageArea.setEditable(false);
	   return receiveMessageArea;
   }
   
   private void playAllSgfMoves()
   {
     clear();
     consecutivePasses=0;
     Iterator it = sgfMoves.iterator();
     String sgfPosition="";
     String moveLine="";
     Move move;
	 //  System.out.println("play all sgf moves");
	   int count=1;
     while(it.hasNext())
     {
       move=(Move)it.next();
      // System.out.println("move "+count+move.getSgfPosition()+", pass: "+move.isPass());
       count++;
       if (move.isPass()) 
       {
    	   playPass(move);
    	   consecutivePasses++;
       }
       else placeStone(move);
     }
		      
     handicapVal.setText(""+handicap);
     moveNoVal.setText(""+lastSgfMoveNumber);
     markLastStone();
     if (stoneSoundActive) stoneSound.play();
     stoneSoundActive=true;
     lastSgfMove = dragonAccess.getLastSgfMove();
     
     if (lastSgfMove.color==BLACK)  
	      colorToPlay=WHITE; 
	    else 
	      colorToPlay=BLACK;
     
     if (consecutivePasses==2) gameOver=true;
    // System.out.println("play all lastSgfMoveNumber: "+lastSgfMoveNumber);
   }
   
   public String colorStr(int color)
   {
	  String colorStr="OPEN";
	  if (color==BLACK) colorStr="BLACK";
	  if (color==WHITE) colorStr="WHITE";
	  return colorStr;
   }
   
   /*
   private void readTestSgfFile()
   {
     try 
     {
       InputStream in = GoClient.class.getResourceAsStream("/sgf/test.sgf"); 
       InputStreamReader isr = new InputStreamReader(in);
       BufferedReader br = new BufferedReader(isr);
       String line;
	      
       while ((line = br.readLine()) != null) { moveLine.add(line); }
     } catch (IOException io) { System.out.println("Ooops"); }
   }
   */
   
   /*
   private Group getGrid()
   {
	 Group grid = new Group();
	 
	 grid.getChildren().add(getImageView(grid_ul_corner_image,  0,   0));
	 grid.getChildren().add(getImageView(grid_ur_corner_image,630,   0));
	 grid.getChildren().add(getImageView(grid_ll_corner_image,  0, 630));
	 grid.getChildren().add(getImageView(grid_lr_corner_image,630, 630));
	 
	 for(int i=0; i<17; i++)
	 {
	   grid.getChildren().add(getImageView(grid_top_image,((i+1)*35),0)); 
	 }
	 
	 for(int i=0; i<17; i++)
	 {
	   grid.getChildren().add(getImageView(grid_left_image,0,((i+1)*35))); 
	 }
	 
	 for(int j=0; j<17; j++)
	 {
	   for(int i=0; i<17; i++)
	   {
	     grid.getChildren().add(getImageView(grid_cross_image,((i+1)*35),((j+1)*35))); 
	   }
	 }
	 
	 for(int i=0; i<17; i++)
	 {
	   grid.getChildren().add(getImageView(grid_right_image,630,((i+1)*35))); 
	 }
	 
	 for(int i=0; i<17; i++)
	 {
	   grid.getChildren().add(getImageView(grid_bottom_image,((i+1)*35),630)); 
	 }
	 
	 grid.getChildren().add(getImageView(grid_hoshi_image,105,105));
	 grid.getChildren().add(getImageView(grid_hoshi_image,315,105));
	 grid.getChildren().add(getImageView(grid_hoshi_image,525,105));
	 grid.getChildren().add(getImageView(grid_hoshi_image,105,315));
	 grid.getChildren().add(getImageView(grid_hoshi_image,315,315));
	 grid.getChildren().add(getImageView(grid_hoshi_image,525,315));
	 grid.getChildren().add(getImageView(grid_hoshi_image,105,525));
	 grid.getChildren().add(getImageView(grid_hoshi_image,315,525));
	 grid.getChildren().add(getImageView(grid_hoshi_image,525,525));
	 
	// grid.getChildren().add(getImageView(black_stone_image,grid_origin.x+35,grid_origin.y+40));
	// grid.getChildren().add(getImageView(black_stone_image,grid_origin.x+(2*35),grid_origin.y+40));
	// grid.getChildren().add(getImageView(black_stone_image,grid_origin.x+(3*35),grid_origin.y+40));
	 
	 return grid;
   }
   */
   
   /*
   private Group getGrid()
   {
	 
	 char sgfX = 'b';
	 char sgfY = 'b';
	 String sgfPosition;
	 
	 grid.getChildren().add(new BoardPosition("aa", BoardPosition.UL_CORNER, 0, 0));
	 grid.getChildren().add(new BoardPosition("sa", BoardPosition.UR_CORNER, 630, 0));
	 grid.getChildren().add(new BoardPosition("as", BoardPosition.LL_CORNER, 0, 630));
	 grid.getChildren().add(new BoardPosition("ss", BoardPosition.LR_CORNER, 630, 630));
	 
	// grid.getChildren().add(getImageView(grid_ul_corner_image,  0,   0));
	 //grid.getChildren().add(getImageView(grid_ur_corner_image,630,   0));
	 //grid.getChildren().add(getImageView(grid_ll_corner_image,  0, 630));
	 //grid.getChildren().add(getImageView(grid_lr_corner_image,630, 630));
	 
	 
	 sgfX = 'b';
	 for(int i=0; i<17; i++)
	 {
	   sgfPosition=sgfX+"a";
	   //System.out.println("SgfPosition: "+sgfPosition);
	   grid.getChildren().add(new BoardPosition(sgfPosition, BoardPosition.TOP, ((i+1)*35), 0));
	   sgfX++;
	   // grid.getChildren().add(getImageView(grid_top_image,((i+1)*35),0)); 
	 }
	 
	 sgfX = 'b';
	 for(int i=0; i<17; i++)
	 {
	   sgfPosition=sgfX+"s";
	   grid.getChildren().add(new BoardPosition(sgfPosition, BoardPosition.BOTTOM, ((i+1)*35), 630)); 
	   //grid.getChildren().add(getImageView(grid_bottom_image,((i+1)*35),630)); 
	   sgfX++;
	 }
	 
	 sgfY='b';
	 for(int i=0; i<17; i++)
	 {
	   sgfPosition="a"+sgfY;
	   grid.getChildren().add(new BoardPosition(sgfPosition, BoardPosition.LEFT, 0, ((i+1)*35)));
	  // grid.getChildren().add(getImageView(grid_left_image,0,((i+1)*35))); 
	   sgfY++;
	 }
	 
	 sgfY='b';
	 for(int i=0; i<17; i++)
	 {
	   sgfPosition="s"+sgfY;
	   grid.getChildren().add(new BoardPosition(sgfPosition, BoardPosition.RIGHT, 630, ((i+1)*35))); 
	   // grid.getChildren().add(getImageView(grid_right_image,630,((i+1)*35))); 
	   sgfY++;
	 }

	 
	 sgfY='b';
	 sgfX = 'b';
	 for(int j=0; j<17; j++)
	 {
	   for(int i=0; i<17; i++)
	   {
		 sgfPosition =""+sgfX+""+sgfY;  
		 grid.getChildren().add(new BoardPosition(sgfPosition, BoardPosition.FILL, ((i+1)*35), ((j+1)*35)));
		 sgfX++;
	    // grid.getChildren().add(getImageView(grid_cross_image,((i+1)*35),((j+1)*35))); 
	   }
	   sgfX = 'b';
	   sgfY++;
	 }
	 
	 grid.getChildren().add(new BoardPosition("dd", BoardPosition.HOSHI, 105, 105));
	 grid.getChildren().add(new BoardPosition("jd", BoardPosition.HOSHI, 315, 105));
	 grid.getChildren().add(new BoardPosition("pd", BoardPosition.HOSHI, 525, 105));
	 grid.getChildren().add(new BoardPosition("dj", BoardPosition.HOSHI, 105, 315));
	 grid.getChildren().add(new BoardPosition("jj", BoardPosition.HOSHI, 315, 315));
	 grid.getChildren().add(new BoardPosition("pj", BoardPosition.HOSHI, 525, 315));
	 grid.getChildren().add(new BoardPosition("dp", BoardPosition.HOSHI, 105, 525));
	 grid.getChildren().add(new BoardPosition("jp", BoardPosition.HOSHI, 315, 525));
	 grid.getChildren().add(new BoardPosition("pp", BoardPosition.HOSHI, 525, 525));
	 
	 return grid;
	
   }
   */
   
   private void getGrid()
   {
	 ArrayList<BoardPosition> gridArray = new ArrayList<BoardPosition>();
	 char sgfX = 'b';
	 char sgfY = 'b';
	 String sgfPosition;
	 
	 gridArray.add(new BoardPosition("aa", BoardPosition.UL_CORNER, 0, 0));
	 gridArray.add(new BoardPosition("sa", BoardPosition.UR_CORNER, 630, 0));
	 gridArray.add(new BoardPosition("as", BoardPosition.LL_CORNER, 0, 630));
	 gridArray.add(new BoardPosition("ss", BoardPosition.LR_CORNER, 630, 630));
	 
	 
	 sgfX = 'b';
	 for(int i=0; i<17; i++)
	 {
	   sgfPosition=sgfX+"a";
	   gridArray.add(new BoardPosition(sgfPosition, BoardPosition.TOP, ((i+1)*35), 0));
	   sgfX++;
	 }
	 
	 sgfX = 'b';
	 for(int i=0; i<17; i++)
	 {
	   sgfPosition=sgfX+"s";
	   gridArray.add(new BoardPosition(sgfPosition, BoardPosition.BOTTOM, ((i+1)*35), 630)); 
	   sgfX++;
	 }
	 
	 sgfY='b';
	 for(int i=0; i<17; i++)
	 {
	   sgfPosition="a"+sgfY;
	   gridArray.add(new BoardPosition(sgfPosition, BoardPosition.LEFT, 0, ((i+1)*35)));
	   sgfY++;
	 }
	 
	 sgfY='b';
	 for(int i=0; i<17; i++)
	 {
	   sgfPosition="s"+sgfY;
	   gridArray.add(new BoardPosition(sgfPosition, BoardPosition.RIGHT, 630, ((i+1)*35))); 
	   sgfY++;
	 }
	 
	 sgfY='b';
	 sgfX = 'b';
	 for(int j=0; j<17; j++)
	 {
	   for(int i=0; i<17; i++)
	   {
		 sgfPosition =""+sgfX+""+sgfY;  
		 gridArray.add(new BoardPosition(sgfPosition, BoardPosition.FILL, ((i+1)*35), ((j+1)*35)));
		 sgfX++;
	   }
	   sgfX = 'b';
	   sgfY++;
	 }
	 
	 bubbleSort(gridArray);
	 
	 BoardPosition bp;
	 Iterator it = gridArray.iterator();
	 while(it.hasNext())
	 {
		bp=(BoardPosition)it.next(); 
		grid.getChildren().add(bp);
		//System.out.println("position: "+bp.getPosition()+" sgfPosition: "+bp.getSgfPosition());
	 }
	 
	 
   }
   
   
   
   static int bubbleSort(ArrayList <BoardPosition> list)
   {
     int count = 0;
     for (int outer = 0; outer < list.size() - 1; outer++)
     {
       for (int inner = 0; inner < list.size()-outer-1; inner++)
       {
         if (list.get(inner).getPosition() > list.get(inner + 1).getPosition())
         {
           swapEm(list, inner);
           count = count + 1;
         }
       }
     }
     return count;
   }

   static void swapEm(ArrayList<BoardPosition>list, int inner)
   {
         BoardPosition temp = list.get(inner);
         list.set(inner, list.get(inner + 1));
         list.set(inner + 1, temp);
   }
   
   private Group getBoardBackground()
   {
	 Group board = new Group();
	 
	 board.getChildren().add(getImageView(board_ul_corner_image,0,0));
	 board.getChildren().add(getImageView(board_ur_corner_image,660,0));
	 board.getChildren().add(getImageView(board_ll_corner_image,0,660));
	 board.getChildren().add(getImageView(board_lr_corner_image,660,660));
	 
	 board.getChildren().add(getImageView(board_left_image,0,10));
	 board.getChildren().add(getImageView(board_left_image,0,110));
	 board.getChildren().add(getImageView(board_left_image,0,210));
	 board.getChildren().add(getImageView(board_left_image,0,310));
	 board.getChildren().add(getImageView(board_left_image,0,410));
	 board.getChildren().add(getImageView(board_left_image,0,510));
	 board.getChildren().add(getImageView(board_left_image,0,560));
	// board.getChildren().add(getImageView(board_left_image,board_origin.x,460));
	 
	 board.getChildren().add(getImageView(board_right_image,660,10));
	 board.getChildren().add(getImageView(board_right_image,660,110));
	 board.getChildren().add(getImageView(board_right_image,660,210));
	 board.getChildren().add(getImageView(board_right_image,660,310));
	 board.getChildren().add(getImageView(board_right_image,660,410));
	 board.getChildren().add(getImageView(board_right_image,660,510));
	 board.getChildren().add(getImageView(board_right_image,660,560));
	// board.getChildren().add(getImageView(board_right_image,660,660));
	 		 
	 board.getChildren().add(getImageView(board_top_image,10,0));
	 board.getChildren().add(getImageView(board_top_image,160,0));
	 board.getChildren().add(getImageView(board_top_image,310,0));
	 board.getChildren().add(getImageView(board_top_image,460,0));
	 board.getChildren().add(getImageView(board_top_image,510,0));
	
	 board.getChildren().add(getImageView(board_bottom_image,10,660));
	 board.getChildren().add(getImageView(board_bottom_image,160,660));
	 board.getChildren().add(getImageView(board_bottom_image,310,660));
	 board.getChildren().add(getImageView(board_bottom_image,460,660));
	 board.getChildren().add(getImageView(board_bottom_image,510,660));
	
	 board.getChildren().add(getImageView(board_fill_image,10,10));
	 board.getChildren().add(getImageView(board_fill_image,160,10));
	 board.getChildren().add(getImageView(board_fill_image,310,10));
	 board.getChildren().add(getImageView(board_fill_image,460,10));
	 board.getChildren().add(getImageView(board_fill_image,510,10));
	 
	 
	 board.getChildren().add(getImageView(board_fill_image,10,160));
	 board.getChildren().add(getImageView(board_fill_image,160,160));
	 board.getChildren().add(getImageView(board_fill_image,310,160));
	 board.getChildren().add(getImageView(board_fill_image,460,160));
	 board.getChildren().add(getImageView(board_fill_image,510,160));
	 
	 board.getChildren().add(getImageView(board_fill_image,10,310));
	 board.getChildren().add(getImageView(board_fill_image,160,310));
	 board.getChildren().add(getImageView(board_fill_image,310,310));
	 board.getChildren().add(getImageView(board_fill_image,460,310));
	 board.getChildren().add(getImageView(board_fill_image,510,310));
	 
	 board.getChildren().add(getImageView(board_fill_image,10,460));
	 board.getChildren().add(getImageView(board_fill_image,160,460));
	 board.getChildren().add(getImageView(board_fill_image,310,460));
	 board.getChildren().add(getImageView(board_fill_image,460,460));
	 board.getChildren().add(getImageView(board_fill_image,510,460));
	 
	 board.getChildren().add(getImageView(board_fill_image,10,510));
	 board.getChildren().add(getImageView(board_fill_image,160,510));
	 board.getChildren().add(getImageView(board_fill_image,310,510));
	 board.getChildren().add(getImageView(board_fill_image,460,510));
	 board.getChildren().add(getImageView(board_fill_image,510,510));
	 
	 return board;
   }
   
   private ImageView getImageView(Image image, float x, float y)
   {
	 ImageView imageView = new ImageView(image);
	 imageView.setX(x);
	 imageView.setY(y);
	 return imageView;
   }

private void setQuit() 
{
  quit = new ImageView(new Image(GoClient.class.getResourceAsStream("/resources/images/x1.png"))); 
  quit.setFitHeight(25);
  quit.setFitWidth(25);
  quit.setX(970);
  quit.setY(15);
	
  quit.setOnMouseClicked(new EventHandler<MouseEvent>() {public void handle(MouseEvent t) { System.exit(0);}});
}  


  private void placeStone(Move move) 
  {
    gameStatusText.setText("");
    consecutivePasses=0;
    
    
    if (!move.isPass())
    {
      moveMap[move.x][move.y]=move.color;	
      Stone stone = new Stone(move);
      if (pieceNumbering==ON) stone.getStoneNumberLabel().setVisible(true);
      if (pieceNumbering==SUBSET) 
      {
    	  int stoneLabelNumber=9+reviewPosition;
    	  if (stoneLabelNumber>0)
    	  {
    	    Label subsetStoneNumberLabel=stone.getSubsetStoneNumberLabel();
    	    subsetStoneNumberLabel.setText(""+(stoneLabelNumber));
    	    subsetStoneNumberLabel.setVisible(true);
    	  }
      }
      visibleMoves.getChildren().add(stone);
	  checkLibertiesOfNeighbors(move.x, move.y);
    }
			
	lastMoveColor=move.color;
	moves.add(move);
	moveNumber++;
	positionHistory.add(new BoardMap(moveMap));
  }

  private void playPass(Move move) 
  {
	
	
	  move.setPass(true);
      if ((localMoves==1)&&(thisPlayerColor==move.color)) enableCommit();
	      reviewBackwardButton.setDisable(true);
	lastMoveColor=move.color;
	moves.add(move);
	moveNumber++;
	positionHistory.add(new BoardMap(moveMap));
  }
  
  private void playResign(Move move) 
  {
	
	
	  move.setPass(true);
	  move.setResign(true);
      if ((localMoves==1)&&(thisPlayerColor==move.color)) enableCommit();
	      reviewBackwardButton.setDisable(true);
	lastMoveColor=move.color;
	moves.add(move);
	moveNumber++;
	positionHistory.add(new BoardMap(moveMap));
  }

  
  void testHistory()
  {
	Iterator it = positionHistory.iterator();
	
	BoardMap bm;
	int counter=0;
	while(it.hasNext())
	{
	   bm=(BoardMap)it.next();
	   System.out.println(counter+": "+bm.lastRow());
	   counter++;
	}
  }


  @SuppressWarnings("rawtypes")
  void removeMoveImageFromPreviousMove()
  {
  if (moveNumber==0) return;
  if (moveNumber>=moves.size()) return; 
  
  Move m = (Move) moves.get(moveNumber-1);
  ObservableList moveList  =visibleMoves.getChildren();
  ListIterator it = moveList.listIterator(moveList.size());
  int color=0;
              
   Stone stone;
   int i=moveList.size()-1;
   while(it.hasPrevious())
   {
     stone=(Stone)it.previous();
    // bp=stone.getBoardPosition();
     if ((stone.x==m.x)&&(stone.y==m.y)) 
     { 
       stone.setRegularImage();
       break; 
     }
     i--;
    }
}

void removeMoveImageFromLastSgfMove()
{
  if (moveNumber<2) return;
  ObservableList moveList  =visibleMoves.getChildren();
  ListIterator it = moveList.listIterator(moveList.size());
  int color=0;
              
   Stone stone;
   int i=moveList.size()-1;
   while(it.hasPrevious())
   {
     stone=(Stone)it.previous();
    // bp=stone.getBoardPosition();
     if ((stone.x==lastSgfMove.x)&&(stone.y==lastSgfMove.y)) 
     { 
       stone.setRegularImage();
       break; 
     }
     i--;
    }
}

void restoreMoveImageToPrevousStone(Move m)
{
    ObservableList moveList  =visibleMoves.getChildren();
  ListIterator it = moveList.listIterator(moveList.size());
  int color=0;
              
   Stone stone;
   int i=moveList.size()-1;
   while(it.hasPrevious())
   {
     stone=(Stone)it.previous();
    // bp=stone.getBoardPosition();
     if ((stone.x==m.x)&&(stone.y==m.y)) 
     { 
       stone.setMoveImage();;
       break; 
     }
     i--;
    }
}

void putMoveImageOnLastStone()
{
  if (moves==null) return;	
  //System.out.println("moves: "+moves.size());
  Move m = (Move)moves.get(moves.size()-1);
  ObservableList moveList  = visibleMoves.getChildren();
  ListIterator it = moveList.listIterator(moveList.size());
  int color=0;
              
   Stone stone;
   int i=moveList.size()-1;
   while(it.hasPrevious())
   {
     stone=(Stone)it.previous();
   //  bp=stone.getBoardPosition();
     if ((stone.x==m.x)&&(stone.y==m.y)) 
     { 
       stone.setMoveImage();;
       break; 
     }
     i--;
    }
}

void markLocalMove()
{
  if (moves==null) return;	
  //System.out.println("moves: "+moves.size());
  Move m = (Move)moves.get(moves.size()-1);
  ObservableList moveList  = visibleMoves.getChildren();
  ListIterator it = moveList.listIterator(moveList.size());
  int color=0;
              
   Stone stone;
   int i=moveList.size()-1;
   while(it.hasPrevious())
   {
     stone=(Stone)it.previous();
   //  bp=stone.getBoardPosition();
     if ((stone.x==m.x)&&(stone.y==m.y)) 
     { 
       if (localMoves==1) stone.setCheckImage(); else stone.setMarkImage();
       //markedStones.add(m.getSimplePosition());
       break; 
     }
     i--;
    }
}





void markLastSgfStone()
{
  if (moves==null) return;	
  //System.out.println("moves: "+moves.size());
  //Move m = (Move)moves.get(moves.size()-1);
  ObservableList moveList  = visibleMoves.getChildren();
  ListIterator it = moveList.listIterator(moveList.size());
  int color=0;
              
   Stone stone;
   int i=moveList.size()-1;
   while(it.hasPrevious())
   {
     stone=(Stone)it.previous();
     if ((stone.x==lastSgfMove.x)&&(stone.y==lastSgfMove.y)) 
     { 
       stone.setMoveImage();
       markedStones.add(lastSgfMove.getSimplePosition());
       break; 
     }
     i--;
    }
}

void markLastStone()
{
  int numberOfMoves=moves.size()-1;
  if (numberOfMoves<0) return;
  Move lastMove=moves.get(numberOfMoves);
  ObservableList visibleStoneList  = visibleMoves.getChildren();
  ListIterator it = visibleStoneList.listIterator(visibleStoneList.size());
  
  int i=visibleStoneList.size()-1;
  Stone stone;
  while(it.hasPrevious())
  {
    stone=(Stone)it.previous();
  //  bp=stone.getBoardPosition();
    if ((stone.x==lastMove.x)&&(stone.y==lastMove.y)) 
    { 
      stone.setMoveImage();
      //markedStones.add(lastSgfMove.getSimplePosition());
      break; 
    }
    i--;
   }
  
  
}

void unmarkPreviousStone()
{
	  int moveSize=moves.size()-2;
	  if (moveSize<0) return;
	  Move lastMove=moves.get(moveSize);
	  ObservableList visibleStoneList  = visibleMoves.getChildren();
	  ListIterator it = visibleStoneList.listIterator(visibleStoneList.size());
	  
	  int i=visibleStoneList.size()-1;
	  Stone stone;
	  while(it.hasPrevious())
	  {
	    stone=(Stone)it.previous();
	  //  bp=stone.getBoardPosition();
	    if ((stone.x==lastMove.x)&&(stone.y==lastMove.y)) 
	    { 
	      stone.setRegularImage();
	      //markedStones.add(lastSgfMove.getSimplePosition());
	      break; 
	    }
	    i--;
	   }
	  
	  
}



void putMoveImageOnCommittedStone()
{
  if (moves==null) return;	
  //System.out.println("moves: "+moves.size());
  Move firstLocalMove = moves.get(lastSgfMoveNumber);
  ObservableList moveList  = visibleMoves.getChildren();
  ListIterator it = moveList.listIterator(moveList.size());
  int color=0;
              
   Stone stone;
   int i=moveList.size()-1;
   while(it.hasPrevious())
   {
     stone=(Stone)it.previous();
   //  bp=stone.getBoardPosition();
     if ((stone.x==firstLocalMove.x)&&(stone.y==firstLocalMove.y)) 
     { 
       stone.setMoveImage();;
       break; 
     }
     i--;
    }
}
  
void playNextStone()
{
  // System.out.println("*** lastSgfMoveNumber: "+lastSgfMoveNumber);
  // System.out.println("reviewPosition: "+reviewPosition);
  // int moveToReplay=(lastSgfMoveNumber+1)+reviewPosition;
  // System.out.println("move to replay: "+moveToReplay);
   //System.out.println("moveNumber: "+moveNumber);
	
   int numberOfMoves=sgfMoves.size();
  // System.out.println("*** sgfMoves.size: "+numberOfMoves);
   int moveToReplay=numberOfMoves+1+reviewPosition;
   
  // System.out.println("move to replay: "+moveToReplay);
   Iterator it = sgfMoves.iterator();
   
   String sgfPosition="";
   String moveLine="";
   Move move;
	   
   reviewPosition++;
   int counter=1;
   while(it.hasNext())
   {
     move=(Move)it.next();
     if (counter==moveToReplay)
     {
       //System.out.println("playing: "+move.getBoardPosition());
       placeStone(move);
       break;
     }
     counter++;
   }
	
   unmarkPreviousStone();
   markLastStone();
}


  void deleteLastStone()  // NOT capture
  {
    removeLastStone();
	
    moveNoVal.setText(""+moveNumber);
    if (localMoves>0)
    {
      localMoves--;
      localMovesVal.setText(""+localMoves);
      if (localMoves==0) 
      {	
        reviewBackwardButton.setDisable(false);
        deleteLastMoveButton.setDisable(true);
        commitButton.setDisable(true);
      }
    }
    //showMoves();
  }
  
  void showMoves()
  {
	Iterator it = moves.iterator();
	Move m;
	int i=0;
	while(it.hasNext())
	{
	  m = (Move)it.next();
	  System.out.print(m.getBoardPosition()+" ");
	  i++;
	}
	System.out.println();
  }

  void rewindLastStone()  // NOT capture
  {
	reviewPosition--;	
	//System.out.println("reviewPosition: "+reviewPosition);
    removeLastStone();	
    moveNoVal.setText(""+moveNumber);
    if (reviewPosition<=0) deleteLastMoveButton.setDisable(true);
    markLastStone();
  }

  void removeLastStone()
  {
	int color;
	int size = moves.size();
	if (moveNumber==0) return;
	gameStatusText.setText("");
	if (size>0)
	{
	  Move m = (Move) moves.get(size-1);
	    
	 // System.out.println("removing: "+((lastSgfMoveNumber+1)+reviewPosition)+" "+m.getBoardPosition());
	 // System.out.println("r -lastSgfMoveNumber: "+lastSgfMoveNumber);
	 // color = moveMap[m.x][m.y];
	  color=m.getColor();
	  
	  if (color==BLACK) lastMoveColor=WHITE;
	  else lastMoveColor=BLACK;
	  
	  if (!m.isPass())
	  {
	    moveMap[m.x][m.y]=OPEN;
	    removeStone(m.x, m.y);
	  }
	  
	 
	  moves.remove(size-1);
	}
	  
	size = moves.size();
	moveNumber--;
	restoreCapturedPieces();
	
  }

  @SuppressWarnings("rawtypes")
  void removeStone(int x, int y)  // remove a stone... NOT capture
  {
    ObservableList moveList  =visibleMoves.getChildren();
    ListIterator it = moveList.listIterator(moveList.size());
              
    Stone stone;
    int i=moveList.size()-1;
    while(it.hasPrevious())
    {
      stone=(Stone)it.previous();
      if ((stone.x==x)&&(stone.y==y)) 
      { 
        moveMap[x][y]=OPEN; 
        moveList.remove(i); 
        int historySize=positionHistory.size();
        if (historySize>0) positionHistory.remove(historySize-1);
        break; 
      }
      i--;
    }
  }
  
   public static void main(final String[] arguments)  
   {  
      Application.launch(arguments);  
   }

    private void checkLibertiesOfNeighbors(int x, int y) 
    {
      checkLibertiesOfNeighbors(x, y, LIVE);
    }

    private boolean checkLibertiesOfNeighbors(int x, int y, boolean trial) 
    {
      boolean capture = false;
      boolean nCapture=false;
      boolean sCapture=false;
      boolean eCapture=false;
      boolean wCapture=false;
      
      nCapture=checkDirection(x,y,NORTH, trial);
      sCapture=checkDirection(x,y,SOUTH, trial);
      eCapture=checkDirection(x,y,EAST, trial);
      wCapture=checkDirection(x,y,WEST, trial);
            
      if (nCapture||sCapture||eCapture||wCapture) capture=true;
      
      return capture;
    }
    
    
     boolean checkDirection(int x, int y, int direction, boolean trial)
     {
       int checkX=x, checkY=y, color=0, startColor=0;
       String directionString="";
       boolean capture=false;
       
       startColor = moveMap[x][y];
            
       if (direction==NORTH) { if (y==0) { directionString="EDGE";  } else { checkY=y-1; directionString="NORTH"; } }
       if (direction==SOUTH) { if (y==18) { directionString="EDGE"; } else { checkY=y+1; directionString="SOUTH"; } }
       if (direction==EAST)  { if (x==18) { directionString="EDGE"; } else {checkX=x+1; directionString="EAST";  } }
       if (direction==WEST)  { if (x==0)  { directionString="EDGE"; } else { checkX=x-1; directionString="WEST";  } }
       
      // System.out.println("For stone: "+x+"-"+y+" "+directionString+" "+checkX+"-"+checkY+" start color: "+startColor+" color: "+color);
       
       if ("EDGE".equals(directionString)) return false;
       
       color=moveMap[checkX][checkY];
       
       if ((color!=OPEN)&&(color!=startColor))
       {
         StoneGroup stoneGroup = new StoneGroup(checkX, checkY, moveMap);
         if (stoneGroup.liberties==0) 
         { 
          captureGroup(stoneGroup.groupPositions, trial);
          //System.out.println("zero liberties For stone: "+x+"-"+y+" "+directionString);
          capture= true;
         }
       }
       
       return capture;
     }

     /*
     boolean checkDirection(int x, int y, int direction, boolean trial)
     {
       boolean debug=false;
       int checkX=x, checkY=y, color=0, startColor=0;
       String directionString="";
       
       startColor = moveMap[x][y];
       boolean capture=false;
            
       if (direction==NORTH) { if (y==0) return false;  checkY=y-1; directionString="NORTH"; }
       if (direction==SOUTH) { if (y==18) return false; checkY=y+1; directionString="SOUTH"; }
       if (direction==EAST)  { if (x==18) return false; checkX=x+1; directionString="EAST";  }
       if (direction==WEST)  { if (x==0) return false;  checkX=x-1; directionString="WEST";  }
       
       
       color = moveMap[checkX][checkY];
       if ((color!=OPEN)&&(color!=startColor))
       {
         Neighbors n = countLiberties(checkX, checkY, color, START);
         if (n.liberties==0) 
         { 
       	   capture = true;
           captureGroup(n.groupPositions, trial);
         }
         if (debug) System.out.println("Group to the "+directionString+" has "+n.liberties+" liberties");
       }
      
       return capture;
     }

     */
     
    private void captureGroup(List<SimplePosition> groupPositions, boolean trial) 
    {
       // System.out.println("CAPTURE GROUP");
      Iterator it=groupPositions.iterator();
      SimplePosition sp;
      while(it.hasNext())
      {
         sp=(SimplePosition)it.next(); 
      //   System.out.println("Capture Position: "+sp.x+"-"+sp.y);
         capturePiece(sp.x, sp.y, trial);
         
      }
    }

    private void capturePiece(int x, int y, boolean trial) 
    {
      ObservableList moveList  = visibleMoves.getChildren();
      Iterator it = moveList.iterator();
      int color=0;
      Stone stone;
      int i=0;
      while(it.hasNext())
      {
        stone=(Stone)it.next();
        if ((stone.x==x)&&(stone.y==y)) 
        { 
          if (!trial)
          {
            stone.setCaptureMoveNumber(moveNumber);  
            capturedStonesArray.add(stone);
            color=moveMap[x][y];
            captured[color]++;
            moveList.remove(i);
          }
          moveMap[x][y]=OPEN; 
          break; 
        }
        i++;
      }
     
      if (!trial)
      {
      capturedBlackVal.setText(""+captured[BLACK]);
      capturedWhiteVal.setText(""+captured[WHITE]);
      }
    }
    
    void listMoveGroup()
    {
    	ObservableList moveList  = visibleMoves.getChildren();
        Iterator it = moveList.iterator();
        Stone stone;
        while(it.hasNext())
        {
          stone=(Stone)it.next();
         // System.out.print(stone.x+"-"+stone.y+", ");
        }
       // System.out.println();
    }

    private void restoreCapturedPieces() 
    {
      Iterator it = capturedStonesArray.iterator();
      ArrayList <Stone>tempCapturedStonesArray = new ArrayList<>();
      Stone s;
      boolean capturedStonesFound=false;
      
      while(it.hasNext())
      {
        s = (Stone)it.next();
        if (s.getCaptureMoveNumber()==moveNumber)
        {
          visibleMoves.getChildren().add(s);
          moveMap[s.x][s.y]=s.getStoneColor();
          capturedStonesFound=true;
        }
        else
        {
          tempCapturedStonesArray.add(s);
        }
      }
      
      if (capturedStonesFound)	capturedStonesArray=new ArrayList<>(tempCapturedStonesArray);
    }
    
    private void clear() 
   {
	 if (visibleMoves==null) return;  
     ObservableList list=visibleMoves.getChildren();  
     while (list.size() > 0)
     {
       list.remove(list.size()-1);
     }
     
     initiallizeMoveMap();
     moves = new ArrayList();
     capturedStonesArray = new ArrayList();
     positionHistory = new ArrayList<>();
     markedStones = new ArrayList<>();
     moveNumber=0;
     captured[BLACK]=0;
     captured[WHITE]=0;
     handicapVal.setText("0");
     moveNoVal.setText("0");
     capturedBlackVal.setText("0");
     capturedWhiteVal.setText("0");
     localMoves=0;
     localMovesVal.setText("0");
   }
   
     
   
   @SuppressWarnings({ "rawtypes", "unchecked" })
   void startAutoRefresh()
   {
	 System.out.println("Starting Auto Refresh"); 
	 secondCounter=0;
	 if (!dragonAccess.isLoggedIn())  return;
	 level=0; 
	 cycleCount=0;
	 totalCycleCount=0;
	 
	
	 timedUpdateText.setText("refresh rate: "+timeStr[level]);
	 timeline = new Timeline();
     timeline.setCycleCount(Timeline.INDEFINITE);
     keyFrame= new KeyFrame(Duration.seconds(1), new EventHandler() 
     {
       public void handle(Event event) 
       {
         if (secondCounter>=interval[level]) 
         {
           secondCounter=0;
           cycleCount++;
           if (cycleCount==cycles[level])
           {
             level++;
             if (level>4) level=4;
             else
             {
            	 timedUpdateText.setText("refresh rate: "+timeStr[level]);
             }
             cycleCount=0;
           }
           
           refreshTimed();	
         }
         secondCounter++;  
       }});
      
       timeline.getKeyFrames().add(keyFrame);
       timeline.playFromStart();
    }
      
   void stopAutoRefresh()
   {
	 System.out.println("Stop Auto Refresh");  
	 timedUpdateText.setText("update OFF ");  
	 level=0;
	 cycleCount=0;
	 if (timeline!=null) timeline.stop();
   }
   
   
   private void enableCommit()
   {
	 if (dragonAccess.isLoggedIn()&&(thisPlayerColor==colorToPlay)) commitButton.setDisable(false);
	 else commitButton.setDisable(true);
   }
   
   private void enableEndgameControls()
   {
	   //System.out.println("end game controls ");
	   if (colorToPlay==thisPlayerColor) 
	   {
	     passButton.setDisable(false);
	     resignButton.setDisable(false);
	   }
	   else
	   {
		 passButton.setDisable(true);
		 resignButton.setDisable(true);
	   }
   }
   
   private void gameOverControls()
   {
	   String gameStatusStr="GAME OVER";
	   passButton.setDisable(true); 
		resignButton.setDisable(true);
		 commitButton.setDisable(true);
		 deleteLastMoveButton.setDisable(true);
		 
		 if ((reviewPosition==0))
	     {
	       reviewForwardButton.setDisable(true);
	       reviewBackwardButton.setDisable(false);
	     }
		 
		 if (reviewPosition<0)
	     {
	       reviewForwardButton.setDisable(false);
	       reviewBackwardButton.setDisable(false);
	     }
		 
		 if (consecutivePasses==2) gameStatusStr=colorStr(lastSgfMove.color)+" PASSED, GAME OVER";
			
			gameStatusText.setText(gameStatusStr);
   }
   
   private void updateControls()
   {
     //System.out.println("UPDATE CONTROLS");
	 String gameStatusStr="";
	 gameStatusText.setText(""); 
	 if (lastSgfMove==null)  // for first move, no handicap
	 {
	   // System.out.println("UpdateControls(): lastSgfMove==null");
	   thisPlayerColor=BLACK;
	   colorToPlay=BLACK;
	   turnImageView.setImage(blackStoneImage);
	   turnColorBar.setFill(Color.BLACK);
	   stage.getIcons().add(smallerBlackStoneImage);
	 }
	 
	  //showVars("updateControls()");
	 
	 if (gameOver) { gameOverControls(); return; }
	 
	 if (localMoves==0)
	 {
	   commitButton.setDisable(true);
	   deleteLastMoveButton.setDisable(true);
	   enableEndgameControls();
	 }
	 if (localMoves>0)
	 {
	   enableCommit(); 
	   deleteLastMoveButton.setDisable(false);
	   passButton.setDisable(true);
	   resignButton.setDisable(true);
	   reviewBackwardButton.setDisable(true);
	   reviewForwardButton.setDisable(true);
	 }
	 
	 if (localMoves>0)
     {
       reviewForwardButton.setDisable(true);
       reviewBackwardButton.setDisable(true);
       reviewForward_10_Button.setDisable(true);
       reviewBackward_10_Button.setDisable(true);
     }
	 
	 if ((reviewPosition==0)&&(localMoves==0))
     {
       reviewForwardButton.setDisable(true);
       reviewBackwardButton.setDisable(false);
       
       reviewForward_10_Button.setDisable(true);
       reviewBackward_10_Button.setDisable(false);
     }
	 
	 if (reviewPosition<0)
     {
       reviewForwardButton.setDisable(false);
       reviewBackwardButton.setDisable(false);
       reviewBackward_10_Button.setDisable(false);
       reviewForward_10_Button.setDisable(false);
     }
		 
	 if (lastSgfMove!=null)  // for first move, no handicap
	 {
	   if (lastSgfMove.isPass())	// an actual committed pass 
	   {
		 if (consecutivePasses==1) gameStatusStr=colorStr(lastSgfMove.color)+" PASSED";
		 gameStatusText.setText(gameStatusStr);
	   }
	
	   if (lastSgfMove.isResign())	// an actual committed pass 
	   {
		 gameStatusStr=colorStr(lastSgfMove.color)+" RESIGNED";
		 gameStatusText.setText(gameStatusStr);
	   }
	
	   if (lastSgfMove.color==BLACK)  
	   { 
	     colorToPlay=WHITE; 
	     turnImageView.setImage(whiteStoneImage);
	     turnColorBar.setFill(Color.WHITE);
	     stage.getIcons().add(smallerWhiteStoneImage);
	   } 
	   else 
	   {
	     colorToPlay=BLACK;
	     turnImageView.setImage(blackStoneImage);
	     turnColorBar.setFill(Color.BLACK);
	     stage.getIcons().add(smallerBlackStoneImage);
	   }
	 }
	 
	 if (passPlayed) //this local message overrides the one above
	 {
	   gameStatusStr="Pass Played, Commit?"; 
	   gameStatusText.setText(gameStatusStr);
	 }
	 
	 if (resignPlayed) //this local message overrides the one above
	 {
	   gameStatusStr="Resign, Commit?"; 
	   gameStatusText.setText(gameStatusStr);
	 }
	 
	 localMovesVal.setText(""+localMoves);
	 moveNoVal.setText(""+moveNumber);
	 
	 if ("no game found".equals(currentGameNo)) 
	 {	 
	   commitButton.setDisable(true);
	   passButton.setDisable(true);
	   resignButton.setDisable(true);
	   reviewBackwardButton.setDisable(true);
	 }
   }
   
   
    
}  