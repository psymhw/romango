package test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;



import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


public class MediaTest extends Application
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
	    	
		Application.launch();

	}

	@Override
	public void start(Stage arg0) throws Exception 
	{
		String path ="C:\\music\\tango\\ClaroDeLuna.mp3";
		File file = new File(path);
		Media media = new Media(file.toURI().toString());
		
		
		  MediaPlayer mp = new MediaPlayer(media);
		 // mp.play();
		  System.out.println("duration: "+media.getDuration().toMillis());
		
		

			  
			  
	}

}
