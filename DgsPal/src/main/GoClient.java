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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

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
   
  Group visibleMoves;
   
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
  Button reviewForwardButton;
  Button reviewBackwardButton;

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
  int level=0;
  int cycles[] = new int[]{                1,           5,        10,          1,        100 };
  long interval[] = new long[]{          70,          130,       250,         490,         970};
  String[] timeStr = new String[]{"1 minute", "2 minutes", "4 minutes", "8 minutes", "16 minutes" };

  SimpleDateFormat df = new SimpleDateFormat("h:mm:ss MM-dd-yy");
  Text timedUpdateText;
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

    turnImageView = new ImageView(blackStoneImage);
	  
    timedUpdateText = new Text("update Off");
	timedUpdateText.setFont(Font.font("Serif", 18));
	
    
    initiallizeMoveMap();
    captured[BLACK]=0;
    captured[WHITE]=0;
      
     
     HBox mainBox = new HBox();
     mainBox.setPadding(new Insets(3, 3, 3, 3));
     mainBox.setSpacing(5);
     mainBox.setStyle("-fx-background-color: DAE6F3;");

     horizLabelView=new ImageView(horizLabelsOff);
     vertLabelView=new ImageView(vertLabelsOff);
     
     VBox vBox = new VBox();
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
    
     vBox.getChildren().add(horizLabelView);
     vBox.getChildren().add(getBoardGroup());
     
    
     HBox hBox = new HBox();
     hBox.getChildren().add(vertLabelView);
     hBox.getChildren().add(vBox);
    
     mainBox.getChildren().add(hBox);
     mainBox.getChildren().add(getRightPane());
     
   
     
     
     ScrollPane scrollPane = new ScrollPane();
     scrollPane.setContent(mainBox);
     
     int height = 680;
     if (primaryScreenBounds.getHeight()<680) height=(int)(primaryScreenBounds.getHeight()-30);
     final Scene scene = new Scene(scrollPane, 1005, height);
    
     scene.setFill(null);
  
     
     
     
    //   stage.initStyle(StageStyle.TRANSPARENT);
    stage.setScene(scene);  
    
    stage.getIcons().add(smallerBlackStoneImage);
    stage.setTitle("DGS Pal");
    stage.show();  
    
    
	
    if (!"noset".equals(password)) refreshStartup();
    else
    {
		feedbackArea.insertText(0, "User ID and password not set. Click on user icon in upper right corner.");
		return;
	}
    
   // feedbackArea.setText(dragonAccess.getSgfFile());
    
   // feedbackArea.setText("HELLO WORLD\n\n"+feedbackArea.getText());
    //feedbackArea.scrollTopProperty();
    //feedbackArea.positionCaret(30);
      
  } // end of start method

 
  
  public void stop()
  {
	//.cancel();
    // .purge();

  }
  
  private void refreshTimed()
  {
	boolean gameFound=false; 
	Date now = new Date();
	long elapsedMillis=0;
	
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
		 
	long gameNo= dragonAccess.checkForMove(timeStr[level]);
	boolean excessive_usage=dragonAccess.isExcessive_usage();
	if (excessive_usage) feedbackArea.insertText(0, "DGS server complaining about excessive usage\n");


	if (gameNo>0) gameFound=true;	
  
	if (gameFound) commitButton.setDisable(false); else commitButton.setDisable(true);
	
    if (gameFound)
    {
      //System.out.println("timed refresh: game found "+gameNo);
      stopAutoRefresh();
      deleteLastMoveButton.setDisable(true);
      reviewForwardButton.setDisable(true);
      clear();
      if (refreshCommon(FROM_SERVER))
      {
  	    playAllSgfMoves();
        feedbackArea.insertText(0, df.format(new Date())+" "+lastSgfMove.getColor()+": "+lastSgfMove.getSgfPosition()+"\n");
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
    }
  }
  lastTimedUpdate=new Date();  
}
  
  private boolean login()
  {
	dragonAccess=new DragonAccess(userId, password);
	return dragonAccess.login(); 
  }
  
  private void refreshStartup()
  {
	boolean gameFound=false;
	long gameNo=0;
	boolean loginSuccess=login();
	
	if (loginSuccess)
	{
  	   gameNo= dragonAccess.checkForMove("startup");
	   boolean excessive_usage=dragonAccess.isExcessive_usage();
	   if (excessive_usage) feedbackArea.insertText(0, "DGS server complaining about excessive usage\n");
	}
	
	
	if (gameNo>0) gameFound=true;	
	  
	if (gameFound) commitButton.setDisable(false); else commitButton.setDisable(true);
	
	if (gameFound)
	{
	  stopAutoRefresh();
	  if (!currentGameNo.equals(""+gameNo))  // if the current game number is not equal to that number, make it so and save it
	  {
		currentGameNo=""+gameNo;
		writeResources();
	  }
	}
	
	if ("noset".equals(currentGameNo)) return;
		
	/*
	 * for startup, I don't care if a game was found. 
	 * Unless there is no game number set
	 * I'll get the current game.
	 * 
	 */
	
	gameNoVal.setText(currentGameNo);
	
	boolean success=false;
	if (gameFound) 
	{
		success=refreshCommon(FROM_SERVER);
		feedbackArea.insertText(0, "game loaded from server\n");
	}
	else
	{
		success= refreshCommon(FROM_LOCAL_FILE); 
		feedbackArea.insertText(0, "game loaded from local file\n");
	}
	
	if (success)
	{	 
	    ArrayList comments = dragonAccess.getComments();
		Iterator it = comments.iterator();
		String comment;
		while(it.hasNext())
		{
		  comment=(String)it.next();
		  feedbackArea.insertText(0, comment+"\n");
		}
	
	playAllSgfMoves();
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
    
    deleteLastMoveButton.setDisable(true);
    reviewForwardButton.setDisable(true);
    //System.out.println("local File: "+localFile);
    
     if ((!gameFound)&&(loginSuccess)) startAutoRefresh();
    // startAutoRefresh(); // temporary for testing
    if (!loginSuccess) feedbackArea.insertText(0, "LOGIN FAILED - network down?"+"\n");
	}
	
	//showVars("refreshStartup");
  }
  
  private void refreshLocal()
  {
	boolean gameFound=false;
	
	/*
	 * Always stop the  on a local refresh.
	 * Will start again if no game was found.
	 */
	stopAutoRefresh();
	
	if ("noset".equals(currentGameNo)) 
	{	
	  feedbackArea.insertText(0, dragonAccess.getFeedback()+"\n");
	  return;
	}
	long gameNo= dragonAccess.checkForMove();
	boolean excessive_usage=dragonAccess.isExcessive_usage();
	if (excessive_usage) feedbackArea.insertText(0, "DGS server complaining about excessive usage\n");

	
	feedbackArea.insertText(0, dragonAccess.getFeedback()+"\n");
		
		if (gameNo>0) gameFound=true;	
		  
		if (gameFound) commitButton.setDisable(false); else commitButton.setDisable(true);
		
		
		
		/*
		 * for local refresh, I don't care if a game was found. 
		 * Unless there is no game number set
		 * I'll get the current game.
		 * 
		 */
		clear();
		
		boolean success=false;
		if (gameFound) success=refreshCommon(FROM_SERVER);
		else success= refreshCommon(FROM_LOCAL_FILE);
		
		if (success)
        {
		  playAllSgfMoves();
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
	      if ((!gameFound)&&(dragonAccess.isLoggedIn())) startAutoRefresh();
	      deleteLastMoveButton.setDisable(true);
	      reviewForwardButton.setDisable(true);
	      reviewBackwardButton.setDisable(false);
        }
	//	showVars("refreshLocal");
  }
  
  void showVars(String call)
  {
	System.out.println("call: "+call);
	System.out.println("lastSgfMoveNumber: "+lastSgfMoveNumber);
	System.out.println("sgfMoves.size(): "+sgfMoves.size());
	System.out.println("colorToPlay: "+colorToPlay);
	System.out.println("reviewPosition: "+reviewPosition);
	System.out.println("moveNumber: "+moveNumber);
	
  }
  
  private boolean refreshCommon(int location)
  {
	boolean success=false;
	//System.out.println("Setting moveNumber to 0");
	moveNumber=0;
	if (location==FROM_LOCAL_FILE) success=dragonAccess.getLocalSgfFile(currentGameNo);
	else success= dragonAccess.getSgf(currentGameNo); 
	if (success)
	{	
	  sgfMoves=dragonAccess.getSgfMoves();
	  lastSgfMove=dragonAccess.getLastSgfMove();
	  lastSgfMoveNumber=dragonAccess.getLastSgfMoveNumber();
	  handicap=dragonAccess.getHandicap();
	  receiveMessageArea.setText(dragonAccess.getMessage());
	  gameLabel.setText(dragonAccess.getPlayerBlack()+" vs "+dragonAccess.getplayerWhite());
	  localFile=dragonAccess.isLocalFile();
	  if (userId!=null)
	  {
		if (userId.equals(dragonAccess.getLoginNameBlack())) thisPlayerColor=BLACK;
		else thisPlayerColor=WHITE;
	  }
	}
	else
	{
	   receiveMessageArea.setText(dragonAccess.getMessage());
	   gameLabel.setText("no game found");
	   feedbackArea.insertText(0, "No game SGF file Found\n");
	}
	return success;
  }
 
  
 
  
 
private void getResources() 
{
  File resourceFile=null;
  File directory = new File (".");
  try 
  {
	resourceFile= new File(directory.getCanonicalPath()+"\\resources.properties");
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
	    currentGameNo="noset";
	    while ((line = br.readLine()) != null) 
	    { 
	      if (line.startsWith("username: ")) userId = line.substring(10);
	      if (line.startsWith("password: ")) password = line.substring(10);
	      if (line.startsWith("gamenumb: ")) currentGameNo = line.substring(10);
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
		resourceFile= new File(directory.getCanonicalPath()+"\\resources.properties");
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

private VBox getRightPane() 
{
  VBox vBox = new VBox(); 
  vBox.setPadding(new Insets(10, 10, 10, 10));
  vBox.setPadding(new Insets(3,5,3,5));
    
  vBox.getChildren().add(getButtonBox());
  vBox.getChildren().add(getIdentBox());
  vBox.getChildren().add(getInfoGroup());
  vBox.getChildren().add(timedUpdateText);
  vBox.getChildren().add(getFeedbackLabel());
  vBox.getChildren().add(getFeedbackBox());
  vBox.getChildren().add(getMessageLabel());
  vBox.getChildren().add(getReceiveMessageBox());
  vBox.getChildren().add(getSendMessageBox());
  return vBox;
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
  Group getFeedbackLabel()
  {
	  
	  Rectangle bx = new Rectangle();
	    bx.setWidth(300);
	    bx.setHeight(30);
	    bx.setArcWidth(10);
	    bx.setArcHeight(10); 
	    Color c =  Color.web("DAE6F3");
	    bx.setFill(c);
	   // new Color("DAE6F3")
	    Group labelGroup = new Group();
	    
	    Text dragonInfoLabel = new Text("Dragon Info");
	    dragonInfoLabel.setFont(Font.font("Serif", 20));
	    dragonInfoLabel.setX(10);
	    dragonInfoLabel.setY(25);
	    
	    labelGroup.getChildren().add(bx);
	    labelGroup.getChildren().add(dragonInfoLabel);
	    
	   return labelGroup;
  }
  
  Group getMessageLabel()
  {
	  
	  Rectangle bx = new Rectangle();
	    bx.setWidth(300);
	    bx.setHeight(25);
	    bx.setArcWidth(10);
	    bx.setArcHeight(10); 
	    Color c =  Color.web("DAE6F3");
	    bx.setFill(c);
	   // new Color("DAE6F3")
	    Group labelGroup = new Group();
	    
	    Text dragonInfoLabel = new Text("Message");
	    dragonInfoLabel.setFont(Font.font("Serif", 20));
	    dragonInfoLabel.setX(10);
	    dragonInfoLabel.setY(30);
	    
	    labelGroup.getChildren().add(bx);
	    labelGroup.getChildren().add(dragonInfoLabel);
	    
	   return labelGroup;
  }

  
  private Group getBoardGroup()
  {
	  Group boardGroup = new Group();
	  Group board = getBoardBackground();
	  Group grid = getGrid();
	  visibleMoves = new Group();
	  boardGroup.getChildren().add(board);
	  boardGroup.getChildren().add(grid);
	  boardGroup.getChildren().add(visibleMoves);  
	  setupMouse(boardGroup);
	  return boardGroup;
  }
  
  private HBox getButtonBox() 
  {
	HBox buttonBox = new HBox(); 
	buttonBox.setPadding(new Insets(3, 3, 3, 3));
	buttonBox.setSpacing(3);
 
	buttonBox.getChildren().add(getDeleteLastMoveButton());
	buttonBox.getChildren().add(getReviewBackwardButton());
	buttonBox.getChildren().add(getRefreshButton());
	buttonBox.getChildren().add(getReviewForwardButton());
	buttonBox.getChildren().add(getCommitButton());
	buttonBox.getChildren().add(getLabelsButton());
	buttonBox.getChildren().add(getUserButton());
	return buttonBox;
  }


  private HBox getIdentBox() 
  {
    HBox identBox = new HBox();
    identBox.setPadding(new Insets(3, 3, 3, 3));
    identBox.setSpacing(5);
   
    gameLabel = new Text("");
    gameLabel.setFont(Font.font("Serif", 18));
   
    identBox.getChildren().add(gameLabel);
    return identBox;
  }
   
  private Group getInfoGroup()
  {
	Group infoGroup = new Group();
	Rectangle bx = new Rectangle();
	bx.setWidth(300);
	bx.setHeight(200);
	bx.setArcWidth(20);
	bx.setArcHeight(20); 
	bx.setFill(Color.GRAY);
	    
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
	    
	return infoGroup;
  }



  private Button getCommitButton() 
  {
	
    commitButton = new Button("Commit");
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event) 
	          {
	        	boolean success=false;  
	        	unmarkGroup();
	        	Move firstLocalMove = moves.get(lastSgfMoveNumber);   
	            success=dragonAccess.makeMove(currentGameNo, 
	            		                      lastSgfMove.sgfPosition,
	            		                      firstLocalMove.getSgfPosition(), 
	            		                      firstLocalMove.color,
	            		                      sendMessageArea.getText()
	            		                      );
	            
	            if (success) 
	            { 
	            	writeMoveToLocalSgfFile(firstLocalMove);
	            	feedbackArea.insertText(0, df.format(new Date())+" "+firstLocalMove.getColor()+": "+firstLocalMove.getBoardPosition()+"\n");
	            	commitButton.setDisable(true); 
	                if (sendMessageArea.getText()!=null)
	                {	
	            	  if (sendMessageArea.getText().length()>0) 
	                	feedbackArea.insertText(0, "message sent:\n"+sendMessageArea.getText()+"\n");
	                }
	                sendMessageArea.setText(""); 
	               
	                removeMoveImageFromLastSgfMove();
	                putMoveImageOnCommittedStone();
	             
	                
	                startAutoRefresh();  
	                
	               
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
	               
	               lastSgfMoveNumber++;
	               lastSgfMove=firstLocalMove;
	               localMoves=0;
	               localMovesVal.setText("0");
	            }
	            else feedbackArea.insertText(0, dragonAccess.getFeedback()+"\n");
	             
	          } };
	  
     commitButton.setOnMouseClicked(bHandler);
     commitButton.setTooltip(new Tooltip("Commit Move"));
     commitButton.setPrefHeight(28);
     return commitButton;
  }
  
  private void writeMoveToLocalSgfFile(Move move)
  {
	String sgfFile=dragonAccess.getSgfFile();
	String colorLetter="";
	if (move.color==1) colorLetter="B"; else colorLetter="W";
	sgfFile=sgfFile+";"+colorLetter+"["+move.getSgfPosition()+"]\n";
	
	File resourceFile=null;
	  File directory = new File (".");
	try{
		resourceFile= new File(directory.getCanonicalPath()+"\\"+currentGameNo+".sgf");
	    FileWriter fstream=null;
	
		fstream = new FileWriter(resourceFile);
	    BufferedWriter out = new BufferedWriter(fstream);
	    out.write(sgfFile);
	    out.close();
 } catch (IOException e) { e.printStackTrace();	}
  }
  
  
  private Button getTestButton() 
  {
    Button commitTestButton = new Button("Test");
    // b.setLayoutX(10);
    // b.setLayoutY(10);
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event) 
	          {
	        	dragonAccess.checkForMove2();
	          } };
	  
     commitTestButton.setOnMouseClicked(bHandler);
     return commitTestButton;
  }
  
  private Button getUserButton() 
  {
    Button userButton = new Button("");
    Image userImage  = new Image(GoClient.class.getResourceAsStream("/images/user.png"));
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
    	public void handle(MouseEvent event) {
            final Stage myDialog = new Stage();
         //   myDialog.initModality(Modality.WINDOW_MODAL);
         myDialog.initModality(Modality.APPLICATION_MODAL);
            Button okButton = new Button("SAVE");
            okButton.setOnAction(new EventHandler<ActionEvent>(){

                public void handle(ActionEvent arg0) {
               	  userId=userIdField.getText();
               	 // userNameLink.setText(userId);
               	  password=passwordField.getText();
        			  System.out.println("userId: "+userId);
        			  System.out.println("password: "+password);
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
     return userButton;
  }
  

  private Button getRefreshButton() 
  {
    Button refreshButton = new Button("*");
    // b.setLayoutX(10);
    // b.setLayoutY(10);
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event) 
	          {
	        	reviewPosition=0;
	        	commitButton.setDisable(true);
	        	reviewForwardButton.setDisable(true);
	        	refreshLocal();
	          } };
	  
     refreshButton.setOnMouseClicked(bHandler);
     refreshButton.setTooltip(new Tooltip("Go to Current Game Position"));
     refreshButton.setPrefHeight(28);
     return refreshButton;
  }

  private void setupMouse(Group boardGroup) 
  {
     
    boardGroup.setOnMouseClicked(new EventHandler<MouseEvent>() {public void handle(MouseEvent t) 
    { 
      int thisMoveColor=0;
      if (lastMoveColor==BLACK) thisMoveColor=WHITE;  else thisMoveColor=BLACK;
      Move move = new Move(t.getX(),t.getY(), thisMoveColor);
     // Stone s = new Stone(thisMoveColor, t.getX(),t.getY(), STYLE_LAST_MOVE);
      
      
      
      // show group liberties on occupied position
      if (moveMap[move.x][move.y]!=OPEN) 
      { 
      	StoneGroup stoneGroup = new StoneGroup(move.x, move.y, moveMap);
        System.out.println("Liberties of group: "+stoneGroup.liberties);
        markGroup(stoneGroup.groupPositions);
  	    return;        
      }
      
      
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
      deleteLastMoveButton.setDisable(false);
      localMovesVal.setText(""+localMoves);  
    }

	
    });
     
  }
  
  private void markGroup(List<SimplePosition> groupPositions) 
  {
	unmarkGroup();  
	Iterator it = groupPositions.iterator();
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
	
	Iterator it = markedStones.iterator();
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
  
  private Stone getStone(SimplePosition position) 
  {
	  ObservableList moveList  = visibleMoves.getChildren();
	  ListIterator it = moveList.listIterator(moveList.size());
	  int color=0;
	              
	   Stone stone;
	   int i=moveList.size()-1;
	   while(it.hasPrevious())
	   {
	     stone=(Stone)it.previous();
	     if ((stone.x==position.x)&&(stone.y==position.y)) 
	     { 
	       return stone;
	     }
	     i--;
	   }
	   return null;
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

  Hyperlink loginButton()
  {
    Hyperlink hyperlink = new Hyperlink();
    hyperlink.setFont(Font.font("Serif", 20));
    hyperlink.setText(userId);
     
    hyperlink.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent event) {
             final Stage myDialog = new Stage();
          //   myDialog.initModality(Modality.WINDOW_MODAL);
          myDialog.initModality(Modality.APPLICATION_MODAL);
             Button okButton = new Button("SAVE");
             okButton.setOnAction(new EventHandler<ActionEvent>(){

                 public void handle(ActionEvent arg0) {
                	  userId=userIdField.getText();
                	  userNameLink.setText(userId);
                	  password=passwordField.getText();
         			  System.out.println("userId: "+userId);
         			  System.out.println("password: "+password);
         			  writeResources();
                      myDialog.close();
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
            // passwordField.setText(userId);
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
     });
     return hyperlink;
}
  
  private Button getDeleteLastMoveButton() 
  {
    deleteLastMoveButton = new Button("< x");
    EventHandler bHandler2 = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
      unmarkGroup();
      deleteLastStone(); 
    }};
    deleteLastMoveButton.setOnAction(bHandler2);
    deleteLastMoveButton.setPrefHeight(28);
    deleteLastMoveButton.setTooltip(new Tooltip("Delete Last Move"));
    return deleteLastMoveButton; 
  }
  
  private Button getLabelsButton() 
  {
    Button button = new Button("a/1");
    EventHandler bHandler2 = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
      toggleLabels(); 
    }};
    button.setOnAction(bHandler2);
    button.setPrefHeight(28);
    button.setTooltip(new Tooltip("Toggle board coordinates on/off"));
    return button; 
  }
  
  private Button getReviewForwardButton() 
  {
    reviewForwardButton = new Button(">");
    EventHandler bHandler2 = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
      // review forward
      reviewPosition++;
      playNextStone();
     // System.out.println("reviewPosition: "+reviewPosition);
      if (reviewPosition==0)
      {
    	reviewForwardButton.setDisable(true);
    	
      }
    }};
    reviewForwardButton.setOnAction(bHandler2);
    reviewForwardButton.setPrefHeight(28);
    reviewForwardButton.setTooltip(new Tooltip("Play Moves Forward"));
    return reviewForwardButton; 
  }
  
  private Button getReviewBackwardButton() 
  {
    reviewBackwardButton = new Button("<");
    EventHandler bHandler2 = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
      // review backward 
      reviewPosition--;	
      reviewForwardButton.setDisable(false);
      rewindLastStone();
     
     // System.out.println("reviewPosition: "+reviewPosition);
    
    }};
    reviewBackwardButton.setOnAction(bHandler2);
    reviewBackwardButton.setTooltip(new Tooltip("Rewind Moves"));
    reviewBackwardButton.setPrefHeight(28);
    return reviewBackwardButton; 
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
	//System.out.println("position History: "+  historySize);
	//System.out.println("checking history: "+ (historySize-2));
	
	if (historySize>2)
	{
	  BoardMap bm = positionHistory.get(historySize-2);
	  if (bm.equals(moveMap)) return true;
	}
	return false;
  }
  
  
  
  private Button getClearButton() 
  {
    Button b = new Button("Clear");
    EventHandler bHandler = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
      clear(); 
    }

           

        };
    b.setOnAction(bHandler);
    return b; 
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
  
  
	
	private void printMoveMap() 
	   {
		for(int j=0; j<19; j++)
		{
		  for(int i=0; i<19; i++)
		  {
			System.out.print(moveMap[i][j]+" ");
		  }
		  System.out.println();
		}
    }
	
	
	
	

        /*
  static void findStone(Group moves, String boardPosition)
  {
	List movesList = moves.getChildren();
	Iterator it = movesList.iterator();
	Stone s;
	while(it.hasNext())
	{
	  s = (Stone)it.next();
	  if (s.getBoardPosition().equals(boardPosition))
	  {
		System.out.println("Found Stone... Color: "+s.getStoneColor());
		s.setVisible(false);
	  }
	}
  }

*/
   
   
   private void importImages()
   {
	 board_ul_corner_image = new Image(GoClient.class.getResourceAsStream("/images/wood4_ul.gif"));
	 board_ur_corner_image = new Image(GoClient.class.getResourceAsStream("/images/wood4_ur.gif"));
	 board_ll_corner_image = new Image(GoClient.class.getResourceAsStream("/images/wood4_dl.gif"));
	 board_lr_corner_image = new Image(GoClient.class.getResourceAsStream("/images/wood4_dr.gif"));
	 
	 board_top_image = new Image(GoClient.class.getResourceAsStream("/images/wood4_u.gif"));
	 board_left_image = new Image(GoClient.class.getResourceAsStream("/images/wood4_l.gif"));
	 board_right_image = new Image(GoClient.class.getResourceAsStream("/images/wood4_r.gif"));
	 board_bottom_image = new Image(GoClient.class.getResourceAsStream("/images/wood4_d.gif"));
	
	 board_fill_image = new Image(GoClient.class.getResourceAsStream("/images/wood4.gif"));
	 
	 grid_ul_corner_image = new Image(GoClient.class.getResourceAsStream("/images/ul.gif"));
	 grid_ur_corner_image = new Image(GoClient.class.getResourceAsStream("/images/ur.gif"));
	 grid_ll_corner_image = new Image(GoClient.class.getResourceAsStream("/images/dl.gif"));
	 grid_lr_corner_image = new Image(GoClient.class.getResourceAsStream("/images/dr.gif"));
	 
	 grid_top_image = new Image(GoClient.class.getResourceAsStream("/images/u.gif"));
	 grid_left_image = new Image(GoClient.class.getResourceAsStream("/images/el.gif"));
	 grid_right_image = new Image(GoClient.class.getResourceAsStream("/images/er.gif"));
	 grid_bottom_image = new Image(GoClient.class.getResourceAsStream("/images/d.gif"));
	 
	 grid_cross_image = new Image(GoClient.class.getResourceAsStream("/images/e.gif"));
	 grid_hoshi_image = new Image(GoClient.class.getResourceAsStream("/images/h.gif"));
	 
	    
	    blackStoneImage = new Image(Stone.class.getResourceAsStream("/images/b.gif")); 
	    whiteStoneImage = new Image(Stone.class.getResourceAsStream("/images/w.gif")); 
	    smallerBlackStoneImage = new Image(Stone.class.getResourceAsStream("/images/smaller_b.gif")); 
	    smallerWhiteStoneImage = new Image(Stone.class.getResourceAsStream("/images/smaller_w.gif")); 
	   
	    horizSgfLabelsImage = new Image(Stone.class.getResourceAsStream("/images/horizLabels.png")); 
	    horizLabelsOff = new Image(Stone.class.getResourceAsStream("/images/horizLabelsOff")); 
	    vertSgfLabelsImage = new Image(Stone.class.getResourceAsStream("/images/vertSgfLabels.png")); 
	    vertLabelsOff = new Image(Stone.class.getResourceAsStream("/images/vertLabelsOff")); 
		 
	    vertRegularLabelsImage = new Image(Stone.class.getResourceAsStream("/images/vertNumbLabels.png")); 
	    horizRegularLabelsImage = new Image(Stone.class.getResourceAsStream("/images/horizRegLabels.png")); 
		
	 
   }
   
   private TextArea getFeedbackBox()
   {
	   feedbackArea = TextAreaBuilder.create()
               .prefWidth(200)
               .prefHeight(140)
               .wrapText(true)
               .build();
       
	   return feedbackArea;
	   /*
       ScrollPane scrollPane = new ScrollPane();
       scrollPane.getStyleClass().add("noborder-scroll-pane");
       scrollPane.setContent(feedbackArea);
       scrollPane.setFitToWidth(true);
       scrollPane.setPrefWidth(200);
       scrollPane.setPrefHeight(100);
       
       return scrollPane;
       */
   }
   
   private TextArea getSendMessageBox()
   {
	   sendMessageArea = TextAreaBuilder.create()
               .prefWidth(200)
               .prefHeight(70)
               .wrapText(true)
               .build();
       
	   return sendMessageArea;
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
   // System.out.println("play all Sgf moves: "+sgfMoves.size());
     Iterator it = sgfMoves.iterator();
     String sgfPosition="";
     String moveLine="";
     Move move;
	   
     while(it.hasNext())
     {
       move=(Move)it.next();
   //    System.out.println(move.x+"-"+move.y);
       placeStone(move,  true);
       
     }
		      
     handicapVal.setText(""+handicap);
     moveNoVal.setText(""+lastSgfMoveNumber);
     //putMoveImageOnLastStone();
     //markLastSgfStone();
     markLastStone();
     stoneSound.play();
     
     lastSgfMove = dragonAccess.getLastSgfMove();
     
    //System.out.println("Last Move: "+lastSgfMoveNumber+", "+colorStr(lastSgfMove.color)+" position: "+lastSgfMove.getSgfPosition());
     
    //System.out.println("local moves: "+localMoves);
     
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
  quit = new ImageView(new Image(GoClient.class.getResourceAsStream("/images/x1.png"))); 
  quit.setFitHeight(25);
  quit.setFitWidth(25);
  quit.setX(970);
  quit.setY(15);
	
  quit.setOnMouseClicked(new EventHandler<MouseEvent>() {public void handle(MouseEvent t) { System.exit(0);}});
}  


  private void placeStone(Move move) 
  {
    placeStone(move, false);;
  }
  
  private void placeStone(Move move, boolean fromSgf) 
  {
	moveMap[move.x][move.y]=move.color;	
	Stone stone = new Stone(move);
	visibleMoves.getChildren().add(stone);
	lastMoveColor=move.color;
	moves.add(move);
	moveNumber++;
	moveNoVal.setText(""+moveNumber);
	checkLibertiesOfNeighbors(move.x, move.y);
	positionHistory.add(new BoardMap(moveMap));
	
    if (!fromSgf) 
    {	
      localMoves++;
      stoneSound.play();
      //unmarkStones();
      markLocalMove();
      if ((localMoves==1)&&(thisPlayerColor==move.color))
      commitButton.setDisable(false);
      reviewBackwardButton.setDisable(true);
    }
    
    
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
 // if (moveNumber==0) return;
 // if (moveNumber>=moves.size()) return; 
 // System.out.println("last SGF move: "+lastSgfMove.sgfPosition);
 // Move m = (Move) moves.get(moveNumber-1);
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
  Move lastMove=moves.get(moves.size()-1);
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
	  Move lastMove=moves.get(moves.size()-2);
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
   //System.out.println("number of moves: "+lastSgfMoveNumber);
   int moveToReplay=lastSgfMoveNumber+reviewPosition;
   //System.out.println("move to replay: "+moveToReplay);
   Iterator it = sgfMoves.iterator();
   String sgfPosition="";
   String moveLine="";
   Move move;
	   
   int counter=1;
   while(it.hasNext())
   {
     move=(Move)it.next();
     if (counter==moveToReplay)
     {
       placeStone(move,  true);
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
	/*
  int color;
  int size = moves.size();
  if (moveNumber==0) return;
  if (size>0)
  {
    Move m = (Move) moves.get(size-1);
    color = moveMap[m.x][m.y];
    moveMap[m.x][m.y]=OPEN;
    if (color==BLACK) lastMoveColor=WHITE;
    else lastMoveColor=BLACK;
    removeStone(m.x, m.y);
    moves.remove(size-1);
  }
  
  size = moves.size();
  restoreCapturedPieces();
  moveNumber--;
  */
  moveNoVal.setText(""+moveNumber);
  if (localMoves>0)
  {
    localMoves--;
    localMovesVal.setText(""+localMoves);
    if (localMoves==0) 
    {	
      reviewBackwardButton.setDisable(false);
      deleteLastMoveButton.setDisable(true);
    }
  }
}

void rewindLastStone()  // NOT capture
{
  removeLastStone();	
  /*
  int color;
  int size = moves.size();
  if (moveNumber==0) return;
  if (size>0)
  {
    Move m = (Move) moves.get(size-1);
    color = moveMap[m.x][m.y];
    moveMap[m.x][m.y]=OPEN;
    if (color==BLACK) lastMoveColor=WHITE;
    else lastMoveColor=BLACK;
    removeStone(m.x, m.y);
    moves.remove(size-1);
  }
  
  size = moves.size();
  restoreCapturedPieces();
  moveNumber--;
  */
  moveNoVal.setText(""+moveNumber);
  if (reviewPosition<=0) deleteLastMoveButton.setDisable(true);
  markLastStone();
}

void removeLastStone()
{
	int color;
	  int size = moves.size();
	  if (moveNumber==0) return;
	  if (size>0)
	  {
	    Move m = (Move) moves.get(size-1);
	    color = moveMap[m.x][m.y];
	    moveMap[m.x][m.y]=OPEN;
	    if (color==BLACK) lastMoveColor=WHITE;
	    else lastMoveColor=BLACK;
	    removeStone(m.x, m.y);
	    moves.remove(size-1);
	  }
	  
	  size = moves.size();
	  restoreCapturedPieces();
	  moveNumber--;
}

void removeStone(int x, int y)  // remove a stone... NOT capture
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
    // sp=bp.getSimplePosition();
     if ((stone.x==x)&&(stone.y==y)) 
     { 
        // System.out.println("found "+i);
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
      boolean debug=true;
      boolean capture = false;
      boolean nCapture=false;
      boolean sCapture=false;
      boolean eCapture=false;
      boolean wCapture=false;
      int startColor=0;
      
      startColor = moveMap[x][y];
      
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
          System.out.print(stone.x+"-"+stone.y+", ");
        }
        System.out.println();
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
        if ((s.getCaptureMoveNumber()==moveNumber)&&(s.forget==false))
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
   
     
   int count=0;
   @SuppressWarnings({ "rawtypes", "unchecked" })
void startAutoRefresh()
   {
	  level=0; 
	  cycleCount=0;
	  timedUpdateText.setText("update every "+timeStr[level]);
	 // feedbackArea.insertText(0, "refresh rate: "+timeStr[level]+"\n"); 
	  timedUpdateText.setText("refresh rate: "+timeStr[level]);
	  timeline = new Timeline();
      timeline.setCycleCount(Timeline.INDEFINITE);
      keyFrame= new KeyFrame(Duration.seconds(1), new EventHandler() 
              {
                public void handle(Event event) 
                {
                 // System.out.println("level: "+timeStr[level]+" cycle: "+cycleCount+ " second: "+count++);
                  //timedUpdateText.setText("  update: "+(interval[level]-count)+"     "+dragonAccess.getFeedback());
                  if (count>=interval[level]) 
                  {
                    count=0;
                    cycleCount++;
                    if (cycleCount==cycles[level])
                    {
                      level++;
                      if (level>4)
                    	level=4;
                      else
                      {
                        timedUpdateText.setText("refresh rate: "+timeStr[level]);
                      }
                      cycleCount=0;
                    }
                    refreshTimed();	
                    //timedUpdateText.setText("Update every "+timeStr[level]);
                  }
                count++;  
                }
              });
      
      timeline.getKeyFrames().add(keyFrame);
      timeline.playFromStart();
   }
      
   void stopAutoRefresh()
   {
	 timedUpdateText.setText("update OFF ");  
	 level=0;
	 cycleCount=0;
	 if (timeline!=null) timeline.stop();
   }
    
}  