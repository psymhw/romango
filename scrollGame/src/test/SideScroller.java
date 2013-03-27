package test;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(SideScroller.class, args);
    }
    double SUNX = 100;
    double SUNY = 300;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX, Side Scrolling");
        Group root = new Group();
        Scene scene = new Scene(root, width, height, Color.TRANSPARENT);

        Rectangle sky = new Rectangle(width, height);
        sky.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0.0, Color.LIGHTBLUE), new Stop(0.7, Color.LIGHTYELLOW), new Stop(1.0, Color.YELLOW)));
        root.getChildren().add(sky);

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
        root.getChildren().add(sun);

        RotateTransition rotateTransition = new RotateTransition(Duration.millis(100 * 360), sun);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(360);        
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.setCycleCount(Timeline.INDEFINITE);
        rotateTransition.play();

        Cloud[] clouds = new Cloud[]{new Cloud(100, 100), new Cloud(150, 20), new Cloud(220, 150), new Cloud(260, 200), new Cloud(310, 40), new Cloud(390, 150), new Cloud(450, 30), new Cloud(550, 100)};
        Sliding cloudSlider = new Sliding(clouds, width);
        root.getChildren().addAll(cloudSlider);

        Rectangle ground = new Rectangle(width, 100);

        ground.setTranslateY(300);
        ground.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0.2, Color.OLIVE), new Stop(0.1, Color.DARKOLIVEGREEN)));
        root.getChildren().add(ground);

        Tree[] trees = new Tree[]{new Tree(20, 320), new Tree(80, 280), new Tree(120, 330), new Tree(140, 280), new Tree(180, 310), new Tree(220, 320),
            new Tree(260, 280), new Tree(280, 320), new Tree(300, 300), new Tree(400, 320), new Tree(500, 280), new Tree(500, 320)
        };
        Sliding forest = new Sliding(trees, width);
        root.getChildren().add(forest);
        scene.setCamera(new PerspectiveCamera());
        primaryStage.setScene(scene);
        
        primaryStage.show();
    }

    /* a cloud is like an ellipse */
    class Cloud extends Ellipse {

        public Cloud(double centerX, double centerY) {
            super(0, 0, 50, 25);
            this.setTranslateX(centerX);
            this.setTranslateY(centerY);
            this.setFill(Color.WHITESMOKE);
            this.setOpacity(0.5);
        }
    }

    class Tree extends Polygon {

        public Tree(double x, double y) {
            super(0, 0, 10, 30, -10, 30);
            this.setTranslateX(x);
            this.setTranslateY(y);
        }
    }
    
    class Sliding extends Group {
    
        final Node[] content;
        Timeline anim = new Timeline();

        public Sliding(final Node[] content,final int width) {
            this.content = content;
            this.getChildren().addAll(content);
            anim.setCycleCount(Timeline.INDEFINITE);
            @SuppressWarnings("rawtypes")
			EventHandler onFinished = new EventHandler() {
                public void handle(Event t) {
                    for(Node n : content) {
                        n.setTranslateX(n.getTranslateX() - 1.0);
                        if(n.getLayoutX() + n.getTranslateX() + n.getBoundsInLocal().getWidth() <= 0) {
                            n.setTranslateX(width - n.getLayoutX());
                        }
                    }
                }

				
            };
            KeyValue keyValueX = new KeyValue(Sliding.this.rotateProperty(),0);
            KeyFrame keyFrame = new KeyFrame(new Duration(50), onFinished , keyValueX); //, keyValueY);
            anim.getKeyFrames().add(keyFrame);
            anim.play();
        }
       }
}