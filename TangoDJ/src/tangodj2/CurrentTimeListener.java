package tangodj2;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
/*
 * This type of listener (InvalidationListener) is lazy. Not so much CPU drain
 */
class CurrentTimeListener implements InvalidationListener 
{
	MediaPlayer player;
	Label currentTimeLabel;
	Slider progress;
	
	  @Override
	  public void invalidated(Observable observable) {
	    Platform.runLater(new Runnable() {
	      @Override
	      public void run() {
	        final Duration currentTime = player.getCurrentTime();
	        final Duration trackLength = player.getTotalDuration();
	        currentTimeLabel.setText(formatDuration(trackLength.toMillis()-currentTime.toMillis()));
	        progress.setValue(1.0 * player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis());
	      }
	    });
	  }
	  
	  public CurrentTimeListener(MediaPlayer player, Label currentTimeLabel, Slider progress)
	  {
		 this.player=player; 
		 this.currentTimeLabel=currentTimeLabel;
		 this.progress=progress;
	  }
	  
	  private String formatDuration(double time) {
		    double millis = time;
		    int seconds = (int) (millis / 1000) % 60;
		    int minutes = (int) (millis / (1000 * 60));
		    return String.format("%02d:%02d", minutes, seconds);
		  }
		
	}