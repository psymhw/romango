package tangodj2.test;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.*;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EngineSound extends Application {
  private static final String MEDIA_URL = 
    "http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv";

  private static final Duration FADE_DURATION = Duration.seconds(2.0);

  public static void main(String[] args) { launch(args); }

  @Override public void start(Stage stage) throws Exception {
    final MediaPlayer mediaPlayer = new MediaPlayer(
      new Media(
        MEDIA_URL
      )
    );
    final MediaView mediaView = new MediaView(mediaPlayer);

    HBox layout = new HBox(5);
    layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
    layout.getChildren().addAll(
      createVolumeControls(mediaPlayer), 
      mediaView
    );
    stage.setScene(new Scene(layout, 650, 230));
    stage.show();           

    mediaPlayer.play();
  }

  public Region createVolumeControls(final MediaPlayer mediaPlayer) {
    final Slider volumeSlider = new Slider(0, 1, 0);
    volumeSlider.setOrientation(Orientation.VERTICAL);

    mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());

    final Timeline fadeInTimeline = new Timeline(
      new KeyFrame(
        FADE_DURATION,
        new KeyValue(mediaPlayer.volumeProperty(), 1.0)
      )
    );

    final Timeline fadeOutTimeline = new Timeline(
      new KeyFrame(
        FADE_DURATION,
        new KeyValue(mediaPlayer.volumeProperty(), 0.0)
      )
    );

    Button fadeIn = new Button("Fade In");
    fadeIn.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent t) {
        fadeInTimeline.play();
      }
    });
    fadeIn.setMaxWidth(Double.MAX_VALUE);

    Button fadeOut = new Button("Fade Out");
    fadeOut.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent t) {
        fadeOutTimeline.play();
      }
    });
    fadeOut.setMaxWidth(Double.MAX_VALUE);

    VBox controls = new VBox(5);
    controls.getChildren().setAll(
      volumeSlider,
      fadeIn,
      fadeOut
    );
    controls.setAlignment(Pos.CENTER);
    VBox.setVgrow(volumeSlider, Priority.ALWAYS);

    controls.disableProperty().bind(
      Bindings.or(
        Bindings.equal(Timeline.Status.RUNNING, fadeInTimeline.statusProperty()),
        Bindings.equal(Timeline.Status.RUNNING, fadeOutTimeline.statusProperty())
      )
    );

    return controls;
  }
}