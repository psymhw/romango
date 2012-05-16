    package main;
      

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.javafx.geom.Point2D;

import javafx.application.Application;  
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;  
import javafx.scene.Scene;  
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;  
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;  
import javafx.stage.StageStyle;
  


public class GoClient extends Application  
{  
   final static int OPEN = 0;
   final static int BLACK = 1;
   final static int WHITE = 2;
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
   
   Image black_stone_image;
   Image white_stone_image;
   Image black_move_image;
   Image white_move_image;
   
   
   ArrayList move = new ArrayList();
   //Group moves;
   
   int[][] moveMap = new int[19][19];
   int lastMove=WHITE;
   int moveNumber=1;
  // SoundClip stoneSound;
   AudioClip stoneSound;
   AudioClip errorSound;
   
   public void start(final Stage stage) throws Exception  
   {  
	   
	  setQuit();
	  importImages();
	  stoneSound = Applet.newAudioClip(GoClient.class.getClassLoader().getResource("resources/sounds/stone.wav"));
	  errorSound = Applet.newAudioClip(GoClient.class.getClassLoader().getResource("resources/sounds/error.wav"));

	  //stoneSound = new SoundClip("stone.wav");
	//  readTestSgfFile();
	  
	  initiallizeMoveMap();
      
      Rectangle r = new Rectangle();
      r.setFill(Color.YELLOW);
      r.setX(0);
      r.setY(0);
      r.setWidth(335);
      r.setHeight(670);
      r.setArcWidth(20);
      r.setArcHeight(20);
           
      final Group boardGroup = new Group();
      Group board = getBoardBackground();
      final Group grid = getGrid();
    //  final  Group moves = getMoves();
      final  Group moves = new Group();
    
      
      FlowPane flowPane = new FlowPane();
      flowPane.setPadding(new Insets(5, 5, 5, 5));
      flowPane.setVgap(4);
      flowPane.setHgap(4);
      flowPane.setPrefWrapLength(170); // preferred width allows for two columns
      flowPane.setStyle("-fx-background-color: DAE6F3;");
      
      boardGroup.getChildren().add(board);
      boardGroup.getChildren().add(grid);
      
      boardGroup.getChildren().add(moves);
     
      Button b = new Button("Play All");
      b.setLayoutX(10);
      b.setLayoutY(10);
      
      // BUTTON
      EventHandler bHandler = new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
              System.out.println("Hello World!");
              //boardGroup.getChildren().removeAll();
       //       boardGroup.getChildren().add(moves);
          //    grid.getChildren().add(getImageView(black_stone_image,(3*35),40));
            //  +gameNumber.getText());
          }
      };
      
      b.setOnAction(bHandler);
      
      Button b2 = new Button("Clear");
      b2.setLayoutX(10);
      b2.setLayoutY(40);
      
      // BUTTON
      EventHandler bHandler2 = new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
              System.out.println("Hello World!");
    //          removeLastStone(moves);
            //  boardGroup.getChildren().removeAll();
           //   boardGroup.getChildren().add(moves);
          //    grid.getChildren().add(getImageView(black_stone_image,(3*35),40));
            //  +gameNumber.getText());
          }
      };
      
      b2.setOnAction(bHandler2);
      
      Group controlGroup = new Group();
      controlGroup.getChildren().add(r);
      controlGroup.getChildren().add(b);
      controlGroup.getChildren().add(b2);
      
     
      flowPane.getChildren().add(boardGroup); 
      flowPane.getChildren().add(controlGroup);
     
      
     
      
   //   final TextField gameNumber = new TextField();
   //   gameNumber.setLayoutX(850);
  //    gameNumber.setLayoutY(100);

      
     
      
  //    boardGroup.getChildren().addAll(b, gameNumber);
      
      final Scene scene = new Scene(flowPane, 1020, 680);
     
      // MOUSE
      boardGroup.setOnMouseClicked(new EventHandler<MouseEvent>() {public void handle(MouseEvent t) 
      { 
    	 //String boardPosition= getSgfCoord(t.getX(),t.getY());
    	  BoardPosition bp = new BoardPosition(t.getX(),t.getY());
    	  System.out.println("x: "+bp.x+" y: "+bp.y+" sgf: "+bp.getSgfPosition());
    	  placeStone(moves, bp);
    	//  boardGroup.getChildren().add(moves);
    	// findStone(moves, boardPosition);
      }


	});
      scene.setFill(null);
      
  
   //   stage.initStyle(StageStyle.TRANSPARENT);
      stage.setScene(scene);  
      stage.show();  
      
 //     boardGroup.getChildren().add(moves);
    //  printMoveMap();
     
  //    ImageView bsi=getImageView(black_stone_image,grid_origin.x+35,grid_origin.y+40);
  //    grid.getChildren().add(bsi);
 // 	 grid.getChildren().add(new Stone(black_stone_image,BLACK,"aa"));
	// grid.getChildren().add(getImageView(black_stone_image,(3*35),40));
  //	 bsi.setVisible(false);
  	 
  	 
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

static void removeLastStone(Group moves)
{
	  List movesList = moves.getChildren();
	  
	  int size = moves.getChildren().size();
	  Stone s = (Stone) moves.getChildren().get(size-1);
	 // s.setVisible(false);
	  moves.getChildren().remove(size-1);
}
   
   
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
	 
	 black_stone_image = new Image(GoClient.class.getResourceAsStream("/images/b.gif"));
	 white_stone_image = new Image(GoClient.class.getResourceAsStream("/images/w.gif"));
	 black_move_image = new Image(GoClient.class.getResourceAsStream("/images/bm.gif"));
	 white_move_image = new Image(GoClient.class.getResourceAsStream("/images/wm.gif"));
	 
	 
	// File sgfFile = new File(GoClient.class.getResourceAsStream("/sgf/test.sgf"));
   }
   
   private Group getMoves()
   {
	  Group moveGroup = new Group();
	  Iterator it = move.iterator();
	  float x=0, y=0;
	  int xInt=0;
	  int yInt=0;
	  int handicap=0;
	  boolean test=true;
	  
	  
	  String sgfPosition="";
	  BoardPosition bp= new BoardPosition(0,0);
	  
	   String moveLine="";
	   int counter=0;
	   while(it.hasNext())
	   {
		   moveLine=(String)it.next();
		   if (moveLine.startsWith("HA"))
		   {
			 handicap=moveLine.charAt(3)-48;  
			 System.out.println("Handicap: "+handicap);
			 continue;
		   }
		      
		   if (moveLine.startsWith("AB"))
		   {
			 if (handicap>0)
			 {
			   int xPos=3;
			   int yPos=5;
			   for(int i=0; i<handicap; i++)
			   {
				 sgfPosition=  moveLine.substring(xPos,yPos);
				 bp=new BoardPosition(sgfPosition);
				 moveMap[bp.x][bp.y]=BLACK;
				 moveGroup.getChildren().add(new Stone(black_stone_image, BLACK, bp ));
			     xPos+=4;
			     yPos+=4;
			   }
			 }
			 continue;
		   }
		  
		   if (moveLine.startsWith(";MN"))
		   {
			 moveNumber=moveLine.charAt(4)-48;
			 System.out.println("Move Number: "+moveNumber);
			 
			 sgfPosition = moveLine.substring(8,10);
			 bp=new BoardPosition(sgfPosition);
			 moveMap[bp.x][bp.y]=WHITE;
			 moveGroup.getChildren().add(new Stone(white_stone_image, WHITE, bp));
			 counter++;
			 continue;
		   }
		   
		   
		//   if (test) continue;
		   if (moveLine.startsWith(";B"))
		   {
			 sgfPosition=moveLine.substring(3,5);
			 bp=new BoardPosition(sgfPosition);
			 moveMap[bp.x][bp.y]=BLACK;
			 moveGroup.getChildren().add(new Stone(black_stone_image, BLACK, bp));
			 counter++;
			 moveNumber++;
			 lastMove=BLACK;
			 continue;
		   }
		   if (moveLine.startsWith(";W"))
		   {
			 sgfPosition=moveLine.substring(3,5);
			 bp=new BoardPosition(sgfPosition);
			 moveMap[bp.x][bp.y]=WHITE;
			 moveGroup.getChildren().add(new Stone(white_stone_image, WHITE,bp));
			 counter++;
			 moveNumber++;
			 lastMove=WHITE;
			 continue;
		   }
		   
		  // if (counter>10) break;
	   }
	   
	   System.out.println("Board Position: "+sgfPosition);
	   if (lastMove==BLACK)
		   moveGroup.getChildren().add(new Stone(black_move_image, BLACK, bp)); 
	   else
		   moveGroup.getChildren().add(new Stone(white_move_image, WHITE,bp));
	   
	  return moveGroup;
   }
   
   private void readTestSgfFile()
   {
	   try {
		     InputStream in = GoClient.class.getResourceAsStream("/sgf/test.sgf"); 
		     InputStreamReader isr = new InputStreamReader(in);
		      BufferedReader br = new BufferedReader(isr);
		     // StringWriter sw = new StringWriter();
		     // PrintWriter pw = new PrintWriter(sw);
		      String line;
		      
		       while ((line = br.readLine()) != null) {
		    	   move.add(line);
		      }
		    //  System.out.println(sw.toString());
		    //  move.add(sw.toString());
		    } catch (IOException io) {
		    	System.out.println("Ooops");
		    }
	
   }
   
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


private void placeStone(Group moves, BoardPosition bp) 
{
	if (moveMap[bp.x][bp.y]!=OPEN)
	{
		errorSound.play();
		return;
	}
	if (lastMove==BLACK)
	{	
	  moveMap[bp.x][bp.y]=WHITE;	
	  moves.getChildren().add(new Stone(white_stone_image, WHITE, bp));
	  lastMove=WHITE;
	  
	}
	else
	{
	  moveMap[bp.x][bp.y]=BLACK;	
	  moves.getChildren().add(new Stone(black_stone_image, BLACK, bp));
	  lastMove=BLACK;
	}
	stoneSound.play();
	moveNumber++;
	
	// if (moveNumber==6) printMoveMap();
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
}  