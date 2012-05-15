    package main;
      

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
   
   
   
   public void start(final Stage stage) throws Exception  
   {  
	   
	  setQuit();
	  importImages();
	  readTestSgfFile();
	  
	  
	  Point2D board_origin = new Point2D(0,0);
	  Point2D grid_origin = new Point2D(0,0);
	  
     // final Circle circ = new Circle(40, 40, 30);  
      
      Rectangle r = new Rectangle();
      r.setFill(Color.YELLOW);
      r.setX(10);
      r.setY(10);
      r.setWidth(100);
      r.setHeight(330);
      r.setArcWidth(20);
      r.setArcHeight(20);
      
      
     // final Group root = new Group(circ, r, quit, board_ul_corner, board_top ); 
      
      Group boardGroup = new Group();
            
      Group board = getBoardBackground(board_origin);
      
      Group grid = getGrid(grid_origin);
      
      final  Group moves = getMoves();
      
      FlowPane flowPane = new FlowPane();
      flowPane.setPadding(new Insets(5, 5, 5, 5));
      flowPane.setVgap(4);
      flowPane.setHgap(4);
      
      flowPane.setPrefWrapLength(170); // preferred width allows for two columns
      flowPane.setStyle("-fx-background-color: DAE6F3;");
      
      
      boardGroup.getChildren().add(board);
      boardGroup.getChildren().add(grid);
     
      
      flowPane.getChildren().add(r);
      flowPane.getChildren().add(boardGroup); 
     
      
     
      
   //   final TextField gameNumber = new TextField();
   //   gameNumber.setLayoutX(850);
  //    gameNumber.setLayoutY(100);

      
  //    Button b = new Button("Click Me");
  //    b.setLayoutX(850);
  //    b.setLayoutY(50);
      
  //    EventHandler bHandler = new EventHandler<ActionEvent>() {
   //       public void handle(ActionEvent event) {
  //            System.out.println("Hello World!"+
  //            gameNumber.getText());
   //       }
  //    };
      
   //   b.setOnAction(bHandler);
      
      
  //    boardGroup.getChildren().addAll(b, gameNumber);
      
      final Scene scene = new Scene(flowPane, 1020, 680);
     
      boardGroup.setOnMouseClicked(new EventHandler<MouseEvent>() {public void handle(MouseEvent t) 
      { 
    	 //String boardPosition= getSgfCoord(t.getX(),t.getY());
    	  BoardPosition bp = new BoardPosition(t.getX(),t.getY());
    	  System.out.println("x: "+bp.x+" y: "+bp.y+" sgf: "+bp.getSgfPosition());
    	// findStone(moves, boardPosition);
      }

	});
      scene.setFill(null);
      
  
   //   stage.initStyle(StageStyle.TRANSPARENT);
      stage.setScene(scene);  
      stage.show();  
      
      boardGroup.getChildren().add(moves);
      
  //    ImageView bsi=getImageView(black_stone_image,grid_origin.x+35,grid_origin.y+40);
  //    grid.getChildren().add(bsi);
 // 	 grid.getChildren().add(new Stone(black_stone_image,BLACK,"aa"));
 // 	 grid.getChildren().add(getImageView(black_stone_image,grid_origin.x+(3*35),grid_origin.y+40));
  //	 bsi.setVisible(false);
  	 
  	 
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
	  int moveNumber=0;
	  int lastMove=0;
	  String sgfPosition="";
	  
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
				 moveGroup.getChildren().add(new Stone(black_stone_image, BLACK, new BoardPosition(sgfPosition)));
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
			 moveGroup.getChildren().add(new Stone(white_stone_image, WHITE, new BoardPosition(sgfPosition)));
			 counter++;
			 continue;
		   }
		   
		   
		//   if (test) continue;
		   if (moveLine.startsWith(";B"))
		   {
			 sgfPosition=moveLine.substring(3,5);
			 moveGroup.getChildren().add(new Stone(black_stone_image, BLACK, new BoardPosition(sgfPosition)));
			 counter++;
			 moveNumber++;
			 lastMove=BLACK;
			 continue;
		   }
		   if (moveLine.startsWith(";W"))
		   {
			   sgfPosition=moveLine.substring(3,5);
			 moveGroup.getChildren().add(new Stone(white_stone_image, WHITE,new BoardPosition(sgfPosition)));
			 counter++;
			 moveNumber++;
			 lastMove=WHITE;
			 continue;
		   }
		   
		  // if (counter>10) break;
	   }
	   
	   System.out.println("Board Position: "+sgfPosition);
	   if (lastMove==BLACK)
		   moveGroup.getChildren().add(new Stone(black_move_image, BLACK,new BoardPosition(sgfPosition))); 
	   else
		   moveGroup.getChildren().add(new Stone(white_move_image, WHITE,new BoardPosition(sgfPosition)));
	   
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
   
   private Group getGrid(Point2D grid_origin)
   {
	 Group grid = new Group();
	 
	 grid.getChildren().add(getImageView(grid_ul_corner_image,grid_origin.x,grid_origin.y));
	 grid.getChildren().add(getImageView(grid_ur_corner_image,grid_origin.x+630,grid_origin.y));
	 grid.getChildren().add(getImageView(grid_ll_corner_image,grid_origin.x,grid_origin.y+630));
	 grid.getChildren().add(getImageView(grid_lr_corner_image,grid_origin.x+630,grid_origin.y+630));
	 
	 for(int i=0; i<17; i++)
	 {
	   grid.getChildren().add(getImageView(grid_top_image,grid_origin.x+((i+1)*35),grid_origin.y)); 
	 }
	 
	 for(int i=0; i<17; i++)
	 {
	   grid.getChildren().add(getImageView(grid_left_image,grid_origin.x,grid_origin.y+((i+1)*35))); 
	 }
	 
	 for(int j=0; j<17; j++)
	 {
	   for(int i=0; i<17; i++)
	   {
	     grid.getChildren().add(getImageView(grid_cross_image,grid_origin.x+((i+1)*35),grid_origin.y+((j+1)*35))); 
	   }
	 }
	 
	 for(int i=0; i<17; i++)
	 {
	   grid.getChildren().add(getImageView(grid_right_image,grid_origin.x+630,grid_origin.y+((i+1)*35))); 
	 }
	 
	 for(int i=0; i<17; i++)
	 {
	   grid.getChildren().add(getImageView(grid_bottom_image,grid_origin.x+((i+1)*35),grid_origin.y+630)); 
	 }
	 
	 grid.getChildren().add(getImageView(grid_hoshi_image,grid_origin.x+105,grid_origin.y+105));
	 grid.getChildren().add(getImageView(grid_hoshi_image,grid_origin.x+315,grid_origin.y+105));
	 grid.getChildren().add(getImageView(grid_hoshi_image,grid_origin.x+525,grid_origin.y+105));
	 grid.getChildren().add(getImageView(grid_hoshi_image,grid_origin.x+105,grid_origin.y+315));
	 grid.getChildren().add(getImageView(grid_hoshi_image,grid_origin.x+315,grid_origin.y+315));
	 grid.getChildren().add(getImageView(grid_hoshi_image,grid_origin.x+525,grid_origin.y+315));
	 grid.getChildren().add(getImageView(grid_hoshi_image,grid_origin.x+105,grid_origin.y+525));
	 grid.getChildren().add(getImageView(grid_hoshi_image,grid_origin.x+315,grid_origin.y+525));
	 grid.getChildren().add(getImageView(grid_hoshi_image,grid_origin.x+525,grid_origin.y+525));
	 
	// grid.getChildren().add(getImageView(black_stone_image,grid_origin.x+35,grid_origin.y+40));
	// grid.getChildren().add(getImageView(black_stone_image,grid_origin.x+(2*35),grid_origin.y+40));
	// grid.getChildren().add(getImageView(black_stone_image,grid_origin.x+(3*35),grid_origin.y+40));
	 
	 return grid;
   }
   
   private Group getBoardBackground(Point2D board_origin)
   {
	 Group board = new Group();
	 
	 board.getChildren().add(getImageView(board_ul_corner_image,board_origin.x,board_origin.y));
	 board.getChildren().add(getImageView(board_ur_corner_image,board_origin.x+660,board_origin.y));
	 board.getChildren().add(getImageView(board_ll_corner_image,board_origin.x,board_origin.y+660));
	 board.getChildren().add(getImageView(board_lr_corner_image,board_origin.x+660,board_origin.y+660));
	 
	 board.getChildren().add(getImageView(board_left_image,board_origin.x,board_origin.y+10));
	 board.getChildren().add(getImageView(board_left_image,board_origin.x,board_origin.y+110));
	 board.getChildren().add(getImageView(board_left_image,board_origin.x,board_origin.y+210));
	 board.getChildren().add(getImageView(board_left_image,board_origin.x,board_origin.y+310));
	 board.getChildren().add(getImageView(board_left_image,board_origin.x,board_origin.y+410));
	 board.getChildren().add(getImageView(board_left_image,board_origin.x,board_origin.y+510));
	 board.getChildren().add(getImageView(board_left_image,board_origin.x,board_origin.y+560));
	// board.getChildren().add(getImageView(board_left_image,board_origin.x,board_origin.y+460));
	 
	 board.getChildren().add(getImageView(board_right_image,board_origin.x+660,board_origin.y+10));
	 board.getChildren().add(getImageView(board_right_image,board_origin.x+660,board_origin.y+110));
	 board.getChildren().add(getImageView(board_right_image,board_origin.x+660,board_origin.y+210));
	 board.getChildren().add(getImageView(board_right_image,board_origin.x+660,board_origin.y+310));
	 board.getChildren().add(getImageView(board_right_image,board_origin.x+660,board_origin.y+410));
	 board.getChildren().add(getImageView(board_right_image,board_origin.x+660,board_origin.y+510));
	 board.getChildren().add(getImageView(board_right_image,board_origin.x+660,board_origin.y+560));
	// board.getChildren().add(getImageView(board_right_image,board_origin.x+660,board_origin.y+660));
	 		 
	 board.getChildren().add(getImageView(board_top_image,board_origin.x+10,board_origin.y));
	 board.getChildren().add(getImageView(board_top_image,board_origin.x+160,board_origin.y));
	 board.getChildren().add(getImageView(board_top_image,board_origin.x+310,board_origin.y));
	 board.getChildren().add(getImageView(board_top_image,board_origin.x+460,board_origin.y));
	 board.getChildren().add(getImageView(board_top_image,board_origin.x+510,board_origin.y));
	
	 board.getChildren().add(getImageView(board_bottom_image,board_origin.x+10,board_origin.y+660));
	 board.getChildren().add(getImageView(board_bottom_image,board_origin.x+160,board_origin.y+660));
	 board.getChildren().add(getImageView(board_bottom_image,board_origin.x+310,board_origin.y+660));
	 board.getChildren().add(getImageView(board_bottom_image,board_origin.x+460,board_origin.y+660));
	 board.getChildren().add(getImageView(board_bottom_image,board_origin.x+510,board_origin.y+660));
	
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+10,board_origin.y+10));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+160,board_origin.y+10));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+310,board_origin.y+10));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+460,board_origin.y+10));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+510,board_origin.y+10));
	 
	 
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+10,board_origin.y+160));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+160,board_origin.y+160));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+310,board_origin.y+160));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+460,board_origin.y+160));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+510,board_origin.y+160));
	 
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+10,board_origin.y+310));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+160,board_origin.y+310));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+310,board_origin.y+310));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+460,board_origin.y+310));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+510,board_origin.y+310));
	 
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+10,board_origin.y+460));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+160,board_origin.y+460));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+310,board_origin.y+460));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+460,board_origin.y+460));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+510,board_origin.y+460));
	 
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+10,board_origin.y+510));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+160,board_origin.y+510));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+310,board_origin.y+510));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+460,board_origin.y+510));
	 board.getChildren().add(getImageView(board_fill_image,board_origin.x+510,board_origin.y+510));
	 
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