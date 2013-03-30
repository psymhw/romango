package test;

//import sandboxfx.SandboxFX;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcBuilder;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Updated to JavaFX 2.0 syntax
 * Based on the 1.2 version by Silveira Neto at 
 * http://silveiraneto.net/2008/08/18/javafx-side-scrolling-gamming/
 * 
 * @author Wayne Young aka Billy Bob Bain
 */
public class SideScroller extends Application {

    protected int width = 500;
    protected int height = 400;
    private Boy boy;
    
    public static void main(String[] args) {
        Application.launch(SideScroller.class, args);
    }
    double SUNX = 100;
    double SUNY = 300;
    
    int FORWARD = 0;
    int REVERSE = 1;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX, Side Scrolling");
        Group root = new Group();
        Scene scene = new Scene(root, width, height, Color.TRANSPARENT);

        Rectangle sky = getSky();
        Group sun = getSun();
        Sliding cloudSlider = getClouds();
        Rectangle ground = getGround();
        Sliding forest = getTrees();
        
        root.getChildren().add(sky);
        root.getChildren().add(sun);
        root.getChildren().addAll(cloudSlider);
        root.getChildren().add(ground);
        root.getChildren().add(forest);
        
        boy=new Boy();
        root.getChildren().add(boy);
        
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) 
            {
              if (ke.getCode()==KeyCode.RIGHT) 
              {
                boy.setDirection(FORWARD);
                boy.makeRun();
                boy.setTranslateX(boy.getTranslateX()+5);	
                
              }
              if (ke.getCode()==KeyCode.LEFT) 
              {
                boy.setDirection(REVERSE);
                boy.makeRun();
                boy.setTranslateX(boy.getTranslateX()-5);	
              }
            }
        });     
        
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) 
            {
              if (ke.getCode()==KeyCode.RIGHT) 
              {
            	  
               boy.makeWalk();
                
              }
              if (ke.getCode()==KeyCode.LEFT) 
              {
                boy.makeWalk();
              }
            }
        });     
        
        
        
        
        scene.setCamera(new PerspectiveCamera());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

	private Sliding getTrees() {
		Tree[] trees = new Tree[]{new Tree(20, 320), new Tree(80, 280), new Tree(120, 330), new Tree(140, 280), new Tree(180, 310), new Tree(220, 320),
            new Tree(260, 280), new Tree(280, 320), new Tree(300, 300), new Tree(400, 320), new Tree(500, 280), new Tree(500, 320)
        };
        Sliding forest = new Sliding(trees, width, 50);
		return forest;
	}

	private Rectangle getGround() {
		Rectangle ground = new Rectangle(width, 100);

        ground.setTranslateY(300);
        ground.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0.2, Color.OLIVE), new Stop(0.1, Color.DARKOLIVEGREEN)));
		return ground;
	}

	private Sliding getClouds() {
		Cloud[] clouds = new Cloud[]{new Cloud(100, 100), new Cloud(150, 20), new Cloud(220, 150), new Cloud(260, 200), new Cloud(310, 40), new Cloud(390, 150), new Cloud(450, 30), new Cloud(550, 100)};
        Sliding cloudSlider = new Sliding(clouds, width, 100);
		return cloudSlider;
	}

	private Rectangle getSky() {
		Rectangle sky = new Rectangle(width, height);
        sky.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0.0, Color.LIGHTBLUE), new Stop(0.7, Color.LIGHTYELLOW), new Stop(1.0, Color.YELLOW)));
		return sky;
	}

	private Group getSun() {
		Group sun = new Group();
        Circle sunCircle = new Circle(SUNX, SUNY, 60);
        sunCircle.setFill(Color.YELLOW);
        sun.getChildren().add(sunCircle);
        ArcBuilder ab =  ArcBuilder.create()
                    .centerX(SUNX)
                    .centerY(SUNY)
                    .radiusX(500)
                    .radiusY(500)
                    .length(360 / 24).type(ArcType.ROUND).fill(Color.YELLOW).opacity(0.3);
        for (int i = 0; i < 12; i++) {
            Arc a = ab.startAngle(2 * i * (360 / 24)).build();
            sun.getChildren().add(a);
        }
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(100 * 360), sun);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(360);        
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.setCycleCount(Timeline.INDEFINITE);
        rotateTransition.play();

		return sun;
	}

 

    
}