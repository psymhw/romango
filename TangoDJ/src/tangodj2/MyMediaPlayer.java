package tangodj2;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MyMediaPlayer 
{
	MediaPlayer mp;
	public MyMediaPlayer(String path, boolean autoPlay)
	{
	   mp = new MediaPlayer(new Media(path));
	   mp.setAutoPlay(autoPlay);
	}

}
