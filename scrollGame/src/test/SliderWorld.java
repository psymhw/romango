package test;
	 
import javafx.application.Application;
import javafx.stage.Stage;
 
/**
 * The main driver of the game.
 * @author cdea
 */
public class SliderWorld extends Application {
 
    GameWorld gameWorld = new GameComponents(59, "Slider World");
    /**
     * @param args the command line arguments
     */
 public static void main(String[] args) {
	        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) {
        // setup title, scene, stats, controls, and actors.
        gameWorld.initialize(primaryStage);
 
        // kick off the game loop
        gameWorld.beginGameLoop();
 
        // display window
        primaryStage.show();
       
    }
 
}