package sandboxfx;
	 
	import javafx.animation.Animation;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
 
public class SandboxFX2 extends Application {
 
    private static final Image IMAGE = new Image(SandboxFX.class.getResourceAsStream("/resources/images/newboy2.png"));
 
    private static final int COLUMNS  =   2;
    private static final int COUNT    =  2;
    private static final int OFFSET_X =  0;
    private static final int OFFSET_Y =  0;
    private static final int WIDTH    = 33;
    private static final int HEIGHT   = 42;
 
    public static void main(String[] args) {
        launch(args);
    }
 
    public void start(Stage primaryStage) {
       primaryStage.setTitle("New Boy 2 Test");
 
        final ImageView imageView = new ImageView(IMAGE);
        imageView.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
 
        final Animation animation = new SpriteAnimation(
                imageView,
                Duration.millis(1000),
                COUNT, COLUMNS,
                OFFSET_X, OFFSET_Y,
                WIDTH, HEIGHT
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
 
        primaryStage.setScene(new Scene(new Group(imageView)));
        primaryStage.show();
    }
}