package main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Paint;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
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
   
  final static int GROUP_MEMBER=33;
  final static int LIBERTY=43;
  
  final static int STYLE_REGULAR=0;
  final static int STYLE_NUMBERED_1=1;
  final static int STYLE_NUMBERED_2=2;
  final static int STYLE_NUMBERED_3=3;
  final static int STYLE_NUMBERED_4=4;
  final static int STYLE_NUMBERED_5=5;
  final static int STYLE_NUMBERED_6=6;
  final static int STYLE_NUMBERED_7=7;
  final static int STYLE_NUMBERED_8=8;
  final static int STYLE_NUMBERED_9=9;
  final static int STYLE_NUMBERED_10=10;
  final static int STYLE_LAST_MOVE=11;
  
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
   
  int[][] moveMap = new int[19][19];
  int[][] groupMap = new int[19][19];
  int lastMoveColor=WHITE;
  int moveNumber=0;

  AudioClip stoneSound;
  AudioClip errorSound;
   
  Group movesGroup;
   
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
  
  DragonAccess dragonAccess;
  Move lastSgfMove;
  int lastSgfMoveNumber;
  int colorToPlay=WHITE;
  int localMoves=0;
  int handicap=0;
  
  
  boolean makeMove=false;
  String currentGameNo="noset";
  
  Button commitButton;

  TextArea feedbackArea;
  TextArea sendMessageArea;
  TextArea receiveMessageArea;
  
  ImageView turnImageView;
  Image blackStoneImage;
  Image whiteStoneImage;
  
  
  private Timeline timeline;
  private KeyFrame keyFrame;
  
 //  ;
//  IntegerProperty Count= new SimpleIntegerProperty(0);
 // Refresh refresh;
  int cycleCount=0;
////	int min=60000;
	//int min=6000; // for testing
	int level=0;
	//long interval[] = new long[]{         min/2,        2*min,      5*min,        10*min,       30*min,   60*min};
	//String[] timeStr = new String[]{"30 seconds", "2 minutes", "5 minutes", "10 minutes", "30 minutes", "1 hour"};
	//long interval[] = new long[]{         30,        60,      120,        300,       600,   1800};
//	String[] timeStr = new String[]{"30 seconds", "1 minute", "2 minutes", "10 minutes", "30 minutes"};
	long interval[] = new long[]{         10,        30,      60,        120,       300,   600};
	String[] timeStr = new String[]{"10 seconds", "30 seconds", "1 minute", "2 minutes", "10 minutes", "30 minutes"};


  Text timedUpdateText = new Text("Update Off");
  
  public void start(final Stage stage) throws Exception  
  {  
    setQuit();
    importImages();
    getResources();
    
    
    stoneSound = Applet.newAudioClip(GoClient.class.getClassLoader().getResource("resources/sounds/stone.wav"));
    errorSound = Applet.newAudioClip(GoClient.class.getClassLoader().getResource("resources/sounds/error.wav"));
    
    blackStoneImage = new Image(Stone.class.getResourceAsStream("/images/b.gif")); 
    whiteStoneImage = new Image(Stone.class.getResourceAsStream("/images/w.gif")); 
    turnImageView = new ImageView(blackStoneImage);
	  
    initiallizeMoveMap();
    captured[BLACK]=0;
    captured[WHITE]=0;
      
    FlowPane flowPane = new FlowPane();
    flowPane.setPadding(new Insets(5, 5, 5, 5));
    flowPane.setVgap(4);
    flowPane.setHgap(4);
    flowPane.setPrefWrapLength(170); // preferred width allows for two columns
    flowPane.setStyle("-fx-background-color: DAE6F3;");
      
    flowPane.getChildren().add(getBoardGroup()); 
    flowPane.getChildren().add(getRightPane());
      
    final Scene scene = new Scene(flowPane, 1020, 680);
     
    scene.setFill(null);
  
    //   stage.initStyle(StageStyle.TRANSPARENT);
    stage.setScene(scene);  
    stage.show();  
    dragonAccess=new DragonAccess(userId, password);
	 dragonAccess.login();
    
    
    if (!"noset".equals(password)) startupRefresh();
    
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
  
  private void timedRefresh()
  {
	boolean gameFound=false;
		 
	long gameNo= dragonAccess.checkForMove();
	feedbackArea.insertText(0, dragonAccess.getFeedback()+"\n"); 

	if (gameNo>0) gameFound=true;	
  
	if (gameFound) commitButton.setDisable(false); else commitButton.setDisable(true);
	
    if (gameFound)
    {
      stopAutoRefresh();
      
      sgfMoves=dragonAccess.getSgf(currentGameNo);
 	  lastSgfMove=dragonAccess.getLastSgfMove();
 	  lastSgfMoveNumber=dragonAccess.getLastSgfMoveNumber();
 	  handicap=dragonAccess.getHandicap();
 	  receiveMessageArea.setText(dragonAccess.getMessage());
 	   clear();
  	    playAllSgfMoves();
 	  /*
 	  if (localMoves>1) // could just roll back local moves here.
      {
   	    clear();
   	    playAllSgfMoves();
      }
      else
      {
        playNewMove();
      }
 	  */
 	  if (lastSgfMove.color==BLACK)  
	  { 
	    colorToPlay=WHITE; 
	    turnImageView.setImage(whiteStoneImage);
	  } 
	  else 
	  {
	    colorToPlay=BLACK;
	    turnImageView.setImage(blackStoneImage);
	  }
    }
  }
  
  private void startupRefresh()
  {
	boolean gameFound=false;
		 
	long gameNo= dragonAccess.checkForMove();
	feedbackArea.insertText(0, dragonAccess.getFeedback()+"\n");
	
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
	  
	  gameNoVal.setText(currentGameNo);
	  if ("noset".equals(currentGameNo))  return;
	}
	
	/*
	 * for startup, I don't care if a game was found. 
	 * Unless there is no game number set
	 * I'll get the current game.
	 * 
	 */
	sgfMoves=dragonAccess.getSgf(currentGameNo);
	lastSgfMove=dragonAccess.getLastSgfMove();
	lastSgfMoveNumber=dragonAccess.getLastSgfMoveNumber();
	handicap=dragonAccess.getHandicap();
	receiveMessageArea.setText(dragonAccess.getMessage());
	
	playAllSgfMoves();
    if (lastSgfMove.color==BLACK)  
    { 
      colorToPlay=WHITE; 
      turnImageView.setImage(whiteStoneImage);
    } 
    else 
    {
      colorToPlay=BLACK;
      turnImageView.setImage(blackStoneImage);
    }
    
    if (!gameFound) startAutoRefresh();
  }
  
  private void localRefresh()
  {
	boolean gameFound=false;
	
	/*
	 * Always stop the  on a local refresh.
	 * Will start again if no game was found.
	 */
	stopAutoRefresh();
	
	long gameNo= dragonAccess.checkForMove();
	feedbackArea.insertText(0, dragonAccess.getFeedback()+"\n");
		
		if (gameNo>0) gameFound=true;	
		  
		if (gameFound) commitButton.setDisable(false); else commitButton.setDisable(true);
		
		
		
		/*
		 * for local refresh, I don't care if a game was found. 
		 * Unless there is no game number set
		 * I'll get the current game.
		 * 
		 */
		sgfMoves=dragonAccess.getSgf(currentGameNo);
		lastSgfMove=dragonAccess.getLastSgfMove();
		lastSgfMoveNumber=dragonAccess.getLastSgfMoveNumber();
		handicap=dragonAccess.getHandicap();
		receiveMessageArea.setText(dragonAccess.getMessage());
		
		clear();
		playAllSgfMoves();
	    if (lastSgfMove.color==BLACK)  
	    { 
	      colorToPlay=WHITE; 
	      turnImageView.setImage(whiteStoneImage);
	      
	    } 
	    else 
	    {
	      colorToPlay=BLACK;
	      turnImageView.setImage(blackStoneImage);
	    }
	    
	    if (!gameFound) startAutoRefresh();
	  
  }
  
 
  /*
  private boolean checkForGame(int type)
  {
	
	 boolean gameFound=false;
	 
	 long gameNo= dragonAccess.checkForMove();
	 feedbackArea.insertText(0, dragonAccess.getFeedback()+"\n");
	 
	 if (gameNo>0)
	 {
		gameFound=true;	
		stopRefresh();
		//System.out.println("game found");
		if (!currentGameNo.equals(""+gameNo))  // if the current game number is not equal to that number, make it so and save it
		{
		  currentGameNo=""+gameNo;
		  writeResources();
		}
		 
	 }
	 
	 // if we got to here, there is either currentGameNo has a game number or it's still 'noset'	
	 
	 gameNoVal.setText(currentGameNo);
	 
	 if ("noset".equals(currentGameNo))  return false;
	 
	 sgfMoves=dragonAccess.getSgf(currentGameNo);
	 lastSgfMove=dragonAccess.getLastSgfMove();
	 lastSgfMoveNumber=dragonAccess.getLastSgfMoveNumber();
	 handicap=dragonAccess.getHandicap();
	 receiveMessageArea.setText(dragonAccess.getMessage());
	 
	 if (type==REFRESH_STARTUP||type==REFRESH_LOCAL) 
	 {	 
	    playAllSgfMoves();
	    if (lastSgfMove.color==BLACK)  
	    { 
	      colorToPlay=WHITE; 
	      turnImageView.setImage(whiteStoneImage);
	    } 
	    else 
	    {
	      colorToPlay=BLACK;
	      turnImageView.setImage(blackStoneImage);
	    }
	 }
	 
	 if (type==REFRESH_TIMED)
	 {
	    if (gameFound) 
	    {
	      if (localMoves>1) // could just roll back local moves here.
	      {
	    	 clear();
	    	 playAllSgfMoves();
	      }
	      else
	      {
	        playNewMove();
	      }
	    }
	 }
	 
	 if (gameFound) commitButton.setDisable(false); else commitButton.setDisable(true);
	 
	 if (gameFound) return true; else return false;
  }
*/
  
  private void playNewMove()  
  {
	placeStone(lastSgfMove, STYLE_LAST_MOVE);
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
    
 //   gridPane.add(getControlButtons2(), 0, 5);
  //  gridPane.add(getRefreshButton(), 0, 5);
  //  gridPane.add(getCommitButton(), 0, 6);
  //  gridPane.add(getCommitTestButton(), 0, 7);
	return gridPane;
}

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

  /*
  HBox getControlButtons2()
  {
	  HBox buttonBox = new HBox();
      buttonBox.setPadding(new Insets(3, 3, 3, 3));
      buttonBox.setSpacing(5);
 
      buttonBox.getChildren().add(getRefreshButton());
      buttonBox.getChildren().add(getCommitButton());
    
	return buttonBox;  
  }
   */
  private Group getBoardGroup()
  {
	  Group boardGroup = new Group();
	  Group board = getBoardBackground();
	  Group grid = getGrid();
	  movesGroup = new Group();
	  boardGroup.getChildren().add(board);
	  boardGroup.getChildren().add(grid);
	  boardGroup.getChildren().add(movesGroup);  
	  setupMouse(boardGroup);
	  return boardGroup;
  }
  
private HBox getButtonBox() {
	HBox buttonBox = new HBox();
      buttonBox.setPadding(new Insets(3, 3, 3, 3));
      buttonBox.setSpacing(5);
 
   //   buttonBox.getChildren().add(getClearButton());
   //   buttonBox.getChildren().add(getSgfButton());
      buttonBox.getChildren().add(getDeleteLastMoveButton());
   //   buttonBox.getChildren().add(getPreviousMoveButton());
   //   buttonBox.getChildren().add(getNextMoveButton());
      buttonBox.getChildren().add(getRefreshButton());
      buttonBox.getChildren().add(getCommitButton());
      buttonBox.getChildren().add(getTestButton());
	return buttonBox;
}

private HBox getIdentBox() 
{
   HBox identBox = new HBox();
   identBox.setPadding(new Insets(3, 3, 3, 3));
   identBox.setSpacing(5);
   Label userIdLinkLabel = new Label("User ID: ");
   userIdLinkLabel.setFont(Font.font("Serif", 18));
   userIdLinkLabel.setPrefHeight(30);
   userIdLinkLabel.setAlignment(Pos.CENTER_RIGHT);
     
   identBox.getChildren().add(userIdLinkLabel);
   userNameLink=loginButton();
   userNameLink.setPrefHeight(30);
   userNameLink.setAlignment(Pos.CENTER_RIGHT);
   identBox.getChildren().add(userNameLink);
  // identBox.getChildren().add(getCommitButton());
   return identBox;
}
   
private Group getInfoGroup()
{
	Group infoGroup = new Group();
	  //  infoGroup.getChildren().add(r);
	 //   infoGroup.getChildren().add(buttonFlowPane);
	  //  infoGroup.getChildren().add(identityFlowPane);
	//    
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
	    
	    //handicapVal.setText(""+handicapInt);
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
        
        gridPane.setHalignment(handicapLabel, HPos.RIGHT);
        gridPane.setHalignment(capturedBlackLabel, HPos.RIGHT);
        gridPane.setHalignment(capturedWhiteLabel, HPos.RIGHT);
        gridPane.setHalignment(localMovesLabel, HPos.RIGHT);
        gridPane.setHalignment(moveNoLabel, HPos.RIGHT);
        gridPane.setHalignment(gameNoLabel, HPos.RIGHT);
        gridPane.setHalignment(turnLabel, HPos.RIGHT);
        
        
        
        infoGroup.getChildren().add(bx);
	    infoGroup.getChildren().add(gridPane);
	    
	    
	    return infoGroup;
}

/*
  private Button getSgfButton() 
  {
    Button b = new Button("Get SGF");
    // b.setLayoutX(10);
    // b.setLayoutY(10);
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event) {
	            //  System.out.println("Hello World!"); 
	        	  
	        	  
	        	  playAllSgfMoves();
	          } };
	  
     b.setOnMouseClicked(bHandler);
     return b;
  }
  */

  private Button getCommitButton() 
  {
    commitButton = new Button("Commit");
    // b.setLayoutX(10);
    // b.setLayoutY(10);
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event) 
	          {
	        	boolean success=false;  
	        	Move firstLocalMove = moves.get(lastSgfMoveNumber);   
	         //   System.out.println("Commit, last move: "+lastSgfMove.sgfPosition+", this move: "+firstLiveMove.getSgfPosition());
	          //  System.out.println("Commit, last move#: "+lastSgfMoveNumber+", movelist size: "+moves.size());
	            
	            success=dragonAccess.makeMove(currentGameNo, 
	            		                      lastSgfMove.sgfPosition,
	            		                      firstLocalMove.getSgfPosition(), 
	            		                      firstLocalMove.color,
	            		                      sendMessageArea.getText()
	            		                      );
	            feedbackArea.insertText(0, dragonAccess.getFeedback());
	            if (success) 
	            { 
	            	commitButton.setDisable(true); 
	                if (sendMessageArea.getText()!=null)
	                {	
	            	  if (sendMessageArea.getText().length()>0) 
	                	feedbackArea.insertText(0, "message sent:\n"+sendMessageArea.getText()+"\n");
	                }
	                sendMessageArea.setText(""); 
	               
	                
	                startAutoRefresh();  
	                
	               
	               if (firstLocalMove.color==BLACK)  
	               { 
	                 colorToPlay=WHITE; 
	                 turnImageView.setImage(whiteStoneImage);
	               } 
	               else 
	               {
	                 colorToPlay=BLACK;
	                 turnImageView.setImage(blackStoneImage);
	               }
	            }
	             
	          } };
	  
     commitButton.setOnMouseClicked(bHandler);
     return commitButton;
  }
  
  private Button getTestButton() 
  {
    Button commitTestButton = new Button("Test");
    // b.setLayoutX(10);
    // b.setLayoutY(10);
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event) 
	          {
	        	dragonAccess.login();  
	        	dragonAccess.getSgf(currentGameNo);
	          } };
	  
     commitTestButton.setOnMouseClicked(bHandler);
     return commitTestButton;
  }

  private Button getRefreshButton() 
  {
    Button refreshButton = new Button("Refresh");
    // b.setLayoutX(10);
    // b.setLayoutY(10);
    EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event) 
	          {
	        	localRefresh();
	          } };
	  
     refreshButton.setOnMouseClicked(bHandler);
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
      localMoves++;
      placeStone(move, localMoves);
      localMovesVal.setText(""+localMoves);  
    }});
     
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
    Button deleteLastMove = new Button("<X");
    EventHandler bHandler2 = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
      removeLastStone(); 
    }};
    deleteLastMove.setOnAction(bHandler2);
    return deleteLastMove; 
  }
  
  private Button getPreviousMoveButton() 
  {
    Button b = new Button("<");
    EventHandler bHandler = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
      rewindMove(); 
    }};
    b.setOnAction(bHandler);
    return b; 
  }
  
  
  // replay rewound moves
  private Button getNextMoveButton() 
  {
    Button b = new Button(">");
    EventHandler bHandler = new EventHandler<ActionEvent>() { public void handle(ActionEvent event) 
    {
      nextMove(); 
    }

        };
    b.setOnAction(bHandler);
    return b; 
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
  
  private void clearGroupMap() 
  {
    for(int i=0; i<19; i++)
    {
      for(int j=0; j<19; j++)
      {
	groupMap[i][j]=OPEN;
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
	 
	// black_stone_image = new Image(GoClient.class.getResourceAsStream("/images/b.gif"));
	// white_stone_image = new Image(GoClient.class.getResourceAsStream("/images/w.gif"));
	// black_move_image = new Image(GoClient.class.getResourceAsStream("/images/bm.gif"));
	// white_move_image = new Image(GoClient.class.getResourceAsStream("/images/wm.gif"));
	 
	 
	// File sgfFile = new File(GoClient.class.getResourceAsStream("/sgf/test.sgf"));
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
    // readTestSgfFile();
     Iterator it = sgfMoves.iterator();
     String sgfPosition="";
     String moveLine="";
     Move move;
	   
     while(it.hasNext())
     {
       move=(Move)it.next();
       placeStone(move, STYLE_REGULAR);
       
     }
		      
     handicapVal.setText(""+handicap);
     moveNoVal.setText(""+lastSgfMoveNumber);
     putMoveImageOnLastStone();
     stoneSound.play();
     
     lastSgfMove = dragonAccess.getLastSgfMove();
     
     System.out.println("Last Move: "+lastSgfMoveNumber+", "+colorStr(lastSgfMove.color)+" position: "+lastSgfMove.getSgfPosition());
     
     makeMove=true;
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
/*
private void placeStone(int color, String sgfPosition, int style) 
{
  Stone s = new Stone(color, sgfPosition, style);
  if (moveMap[s.x][s.y]!=OPEN) return;
  
  moveMap[s.x][s.y]=color;
  movesGroup.getChildren().add(s);
  lastMoveColor=color;
  moves.add(new Move(s.x, s.y, color));
 // if (s.style==STYLE_LAST_MOVE) removeMoveImageFromPreviousMove();
  moveNumber++;
  movenoVal.setText(""+moveNumber);
  checkLibertiesOfNeighbors(s.x, s.y);
//  if (s.style==STYLE_LAST_MOVE) stoneSound.play();
}


private void placeStone(int color, int x, int y) 
{
  if (moveMap[x][y]!=OPEN) return;
  
  moveMap[x][y]=color;	
  movesGroup.getChildren().add(new Stone(color, x, y, STYLE_LAST_MOVE));
  lastMoveColor=color;
  moves.add(new Move(x, y, color));
  removeMoveImageFromPreviousMove();
  moveNumber++;
  movenoVal.setText(""+moveNumber);
  checkLibertiesOfNeighbors(x, y);
  stoneSound.play();
}
*/

/*
private void placeStone(Stone s) 
{
  if (moveMap[s.x][s.y]!=OPEN) return;
  
  moveMap[s.x][s.y]=s.getStoneColor();	
  movesGroup.getChildren().add(s);
  lastMoveColor=s.getStoneColor();
  Move move = new Move(s.x, s.y, s.getStoneColor());
  moves.add(move);
  if (s.style==STYLE_LAST_MOVE) removeMoveImageFromPreviousMove();
  moveNumber++;
  movenoVal.setText(""+moveNumber);
  checkLibertiesOfNeighbors(s.x, s.y);
  if (s.style!=STYLE_REGULAR)stoneSound.play();
 // if (makeMove)
 // {	  
 //   dragonAccess.login();
  //  dragonAccess.makeMove(lastMove.getSgfPosition(), move.getSgfPosition(), s.getStoneColor());
//  }
// makeMove=false;
}

*/

private void placeStone(Move move, int style) 
{
  if (moveMap[move.x][move.y]!=OPEN) return;
  
  moveMap[move.x][move.y]=move.color;	
  
  
  movesGroup.getChildren().add(new Stone(move, style));
  lastMoveColor=move.color;
  moves.add(move);
 // if (style==STYLE_LAST_MOVE) removeMoveImageFromPreviousMove();
  moveNumber++;
  moveNoVal.setText(""+moveNumber);
  checkLibertiesOfNeighbors(move.x, move.y);
  if (style!=STYLE_REGULAR)stoneSound.play();

}




void removeMoveImageFromPreviousMove()
{
  if (moveNumber==0) return;
  if (moveNumber>=moves.size()) return; 
  
  Move m = (Move) moves.get(moveNumber-1);
  ObservableList moveList  =movesGroup.getChildren();
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
       stone.setRegularImage();;
       break; 
     }
     i--;
    }
}

void restoreMoveImageToPrevousStone(Move m)
{
    ObservableList moveList  =movesGroup.getChildren();
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
  System.out.println("moves: "+moves.size());
  Move m = (Move)moves.get(moves.size()-1);
  ObservableList moveList  = movesGroup.getChildren();
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
  
void removeLastStone()  // NOT capture
{
  int color;
  
  int size = moves.size();
  if (size<=lastSgfMoveNumber) return;
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
  if (size>0)
  {
    restoreMoveImageToPrevousStone((Move)moves.get(size-1));
  }
  
  restoreCapturedPieces();
  moveNumber--;
  moveNoVal.setText(""+moveNumber);
  localMoves--;
  localMovesVal.setText(""+localMoves);
}



void removeStone(int x, int y)  // remove a stone... NOT capture
{
  ObservableList moveList  =movesGroup.getChildren();
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
       break; 
     }
     i--;
    }
  }
   /** 
    * Main function used to run JavaFX 2.0 example. 
    *  
    * @param arguments Command-line arguments: none expected. 
    */  
   public static void main(final String[] arguments)  
   {  
      Application.launch(arguments);  
   }

    private Neighbors countLiberties(int x, int y, int color, int noCheck) 
    {
        boolean debug=false;
        
        Neighbors n = new Neighbors(x, y, color);
        if (noCheck==START) clearGroupMap();
        if (debug) 
        { 
          if (noCheck==START) 
          {
            System.out.println(); 
          }
          System.out.println("Stone: "+x+"-"+y); 
        }
        
        // check if been here already
       if (groupMap[x][y]==GROUP_MEMBER) return null;
       groupMap[x][y]=GROUP_MEMBER;
        
       
       if (noCheck!=NORTH) directionCheck(x,y,color, n,NORTH); 
       if (noCheck!=SOUTH) directionCheck(x,y,color, n,SOUTH);
       if (noCheck!=WEST)  directionCheck(x,y,color, n,WEST);
       if (noCheck!=EAST)  directionCheck(x,y,color, n,EAST);
        
       return n;
    }
    
    void directionCheck(int x, int y, int color, Neighbors n, int direction)
    {
      boolean debug=false;  
      int checkX=x, checkY=y;
      int oppositeDirection=0;
      
      if (direction==NORTH) { if (y==0) return;  checkY=y-1; oppositeDirection=SOUTH; }
      if (direction==SOUTH) { if (y==18) return; checkY=y+1; oppositeDirection=NORTH; }
      if (direction==EAST)  { if (x==18) return; checkX=x+1; oppositeDirection=WEST;  }
      if (direction==WEST)  { if (x==0) return;  checkX=x-1; oppositeDirection=EAST;  }
      
      if (moveMap[checkX][checkY]==OPEN) 
      {
        if (groupMap[checkX][checkY]!=LIBERTY)
        {    
          groupMap[checkX][checkY]=LIBERTY;   
          n.liberties++;
          n.libertyPositions.add(new SimplePosition(checkX,checkY));
          if (debug) System.out.println("  liberty Soutn");
        }
      }
      else if (moveMap[checkX][checkY]==color) 
      {
        if (debug) System.out.println("  moving South");
        n.accumulate(countLiberties(checkX, checkY, color, oppositeDirection));
      }
            
    }

    private Neighbors checkNorth(int color) 
    {
        Neighbors n = new Neighbors(color);
        return n;
    }

    private void checkLibertiesOfNeighbors(int x, int y) 
    {
      boolean debug=false;
      int startColor=0;
      
      startColor = moveMap[x][y];
      if (debug) System.out.println("Start Color is "+startColor);
      
      //CHECK NORTH
      checkDirectionForNeighbors(x,y,NORTH);
      checkDirectionForNeighbors(x,y,SOUTH);
      checkDirectionForNeighbors(x,y,EAST);
      checkDirectionForNeighbors(x,y,WEST);
      
      if (debug) System.out.println();
    }

     void checkDirectionForNeighbors(int x, int y, int direction)
     {
       boolean debug=false;
       int checkX=x, checkY=y, color=0, startColor=0;
       String directionString="";
       startColor = moveMap[x][y];
            
       if (direction==NORTH) { if (y==0) return;  checkY=y-1; directionString="NORTH"; }
       if (direction==SOUTH) { if (y==18) return; checkY=y+1; directionString="SOUTH"; }
       if (direction==EAST)  { if (x==18) return; checkX=x+1; directionString="EAST";  }
       if (direction==WEST)  { if (x==0) return;  checkX=x-1; directionString="WEST";  }
       
       
         color = moveMap[checkX][checkY];
         if (debug) System.out.println(directionString+" Color is: "+color);
         if ((color!=OPEN)&&(color!=startColor))
         {
           Neighbors n = countLiberties(checkX, checkY, color, START);
           if (n.liberties==0) captureGroup(n.groupPositions);
           if (debug) System.out.println("Group to the "+directionString+" has "+n.liberties+" liberties");
         }
         else 
         {
           if (debug) System.out.println("No Group or non-enemy to the "+directionString);   
         }
       }

    private void captureGroup(List<SimplePosition> groupPositions) 
    {
       // System.out.println("CAPTURE GROUP");
      Iterator it=groupPositions.iterator();
      SimplePosition sp;
      while(it.hasNext())
      {
         sp=(SimplePosition)it.next(); 
       //  System.out.println("Position: "+sp.x+"-"+sp.y);
         capturePiece(sp.x, sp.y);
         
      }
    }

    private void capturePiece(int x, int y) 
    {
      ObservableList moveList  =movesGroup.getChildren();
      Iterator it = moveList.iterator();
      int color=0;
              
      Stone stone;
      int i=0;
      while(it.hasNext())
      {
        stone=(Stone)it.next();
        if ((stone.x==x)&&(stone.y==y)) 
        { 
          stone.setCaptureMoveNumber(moveNumber);  
          capturedStonesArray.add(stone);
          color=moveMap[x][y];
          captured[color]++;
          moveMap[x][y]=OPEN; 
          moveList.remove(i); break; 
        }
        i++;
      }
      
      capturedBlackVal.setText(""+captured[BLACK]);
      capturedWhiteVal.setText(""+captured[WHITE]);
    }

    private void restoreCapturedPieces() 
    {
      Iterator it = capturedStonesArray.iterator();
      Stone s;
      boolean capturedStonesFound=false;
      
      while(it.hasNext())
      {
        s = (Stone)it.next();
        if ((s.getCaptureMoveNumber()==moveNumber)&&(s.forget==false))
        {
          movesGroup.getChildren().add(s);
          moveMap[s.x][s.y]=s.getStoneColor();
          capturedStonesFound=true;
        }
      }
      
      if (capturedStonesFound)
      {
        Iterator it2 = capturedStonesArray.iterator();
        int i=0;
        while(it2.hasNext())
        {
          s = (Stone)it2.next();
         // bp=s.getBoardPosition();
          if (s.getCaptureMoveNumber()==moveNumber)
          {
            s.forget=true;
          }
          i++;
        }
      }
    }
    
    private void rewindMove() 
    {
      throw new UnsupportedOperationException("Not yet implemented");
    }
       
      
  private void nextMove() 
  {
    throw new UnsupportedOperationException("Not yet implemented");
  }
   
   private void clear() 
   {
	 if (movesGroup==null) return;  
     ObservableList list=movesGroup.getChildren();  
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
   
  /*
   void Test()
   {
	   = new ();
	  .schedule(new RemindTask((SimpleIntegerProperty) Count), 0, 5000);
   }
   */
   
   /*
   void startRefresh()
   {
	  level=0; 
	  cycleCounter=0;
	   = new ();
	  timedUpdateText.setText("Update every "+timeStr[level]);
	  .schedule(new RemindTask((SimpleIntegerProperty) Count), interval[level], interval[level]);
	  Count.addListener(new ChangeListener<Number>(){
	 
	  
		    public void changed(ObservableValue<? extends Number> arg0,  Number arg1, Number arg2) 
		       {
		              timedRefresh();
		    	      cycleCounter++;
		    	      if (cycleCounter==5)
		              {
		            	 level++;
		            	 if (level>5) level=5;
		            	  
		            	  System.out.println(timeStr[level]);
		            	  cycleCounter=0;
		            	  .cancel();
		            	  .purge();
		            	  = new ();
		            	  .schedule(new RemindTask((SimpleIntegerProperty) Count), interval[level], interval[level]);
		            	  timedUpdateText.setText("Update every "+timeStr[level]);
		              }
		           }
		    		
		    });   
	  
	  
	  
   }
   */
   
   int count=0;
   void startAutoRefresh()
   {
	  level=0; 
	  cycleCount=0;
	  
	  timeline = new Timeline();
      timeline.setCycleCount(Timeline.INDEFINITE);
      keyFrame= new KeyFrame(Duration.seconds(1), new EventHandler() 
    	                                              {
    	 				                                public void handle(Event event) 
    					                                {
    						                              System.out.println("level: "+timeStr[level]+" cycle: "+cycleCount+ " second: "+count++);
    						                              if (count>=interval[level]) 
    						                              {
    						                                count=0;
    						                                cycleCount++;
    						                                if (cycleCount==5)
    						                                {
    						                                  level++;
    						                                  if (level>5)  level=5; else System.out.println("LEVEL UP");
    						                                	  
    						                                  cycleCount=0;
    						                                }
    						                                timedRefresh();	
    						                                timedUpdateText.setText("Update every "+timeStr[level]);
    						                              }
    					                                }
    	                                              });
      
      timeline.getKeyFrames().add(keyFrame);
      timeline.playFromStart();
   }
      
   void stopAutoRefresh()
   {
	 timedUpdateText.setText("Update OFF ");  
	 level=0;
	 cycleCount=0;
	 if (timeline!=null) timeline.stop();
   }
    
}  