package tangodj2.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3v1;
import org.farng.mp3.id3.AbstractID3v2;
import org.tritonus.share.sampled.TAudioFormat;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import tangodj2.Hasher;
import tangodj2.TangoDJ2;
import tangodj2.TrackLoader;


public class MP3LengthCheck extends Application
{
	File file = new File("C:\\music\\tango\\Carabelli\\Cuatro Palabras\\Alma.mp3");
	Media media=null;
	ChangeListener cl2;
	MediaPlayer mp;
	boolean changed=false;
	String str="NOT SET";
	static int errors=0;
	static int counter=0;
	private static Hasher hasher = new Hasher();
	
	public static void main(String[] args) 
    {
        launch(args);
    }
 
	 public void start(Stage stage) 
	 {
		// getInfo(); 
		//getLength2();
		processDirectory();
		Group root = new Group();
		
		final Button addButton = new Button("Check");
		 addButton.setOnAction(new EventHandler<ActionEvent>() 
		 {
		   public void handle(ActionEvent e) 
		   {
		     System.out.println("TimeGetters: "+TimeGetter.counter);
		     
		   }
		 });
		
		root.getChildren().add(addButton);
		Scene scene = new Scene(root, 950, 550, Color.WHITE);
		stage.setScene(scene);
		stage.show();
	 }
	 
	 
	 
	 void processDirectory()
	 {
		try {
			new TrackLoader2("C:\\music\\tango\\Carabelli\\Cuatro Palabras");
		} catch(Exception e) { e.printStackTrace(); }
		
	 }
	 
	void getLength2()
	{
	  if (file.exists()) System.out.println("File Found"); else System.out.println("File NOT Found");
	  changed=false;
	  str="NOT SET";
		  
	  cl2 = new ChangeListener() 
	  {
		 public void changed(ObservableValue observable, Object oldValue, Object newValue) 
		 {
		  	Duration duration = media.durationProperty().get(); 
		  	str=formatIntoHHMMSS(duration.toSeconds());
		    System.out.println("Duration: "+str);
		    media.durationProperty().removeListener(cl2);
		    mp=null;
		    changed=true;
		 }
	   };
	
	  media = new Media(file.toURI().toString());
	  media.durationProperty().addListener(cl2);
      mp = new MediaPlayer(media);
 	}
	
	
	
	static String formatIntoHHMMSS(double secsIn)
	{

	int hours = (int)secsIn / 3600,
	remainder = (int)secsIn % 3600,
	minutes = remainder / 60,
	seconds = remainder % 60;

	return ( (hours < 10 ? "0" : "") + hours
	+ ":" + (minutes < 10 ? "0" : "") + minutes
	+ ":" + (seconds< 10 ? "0" : "") + seconds );

	}
	
	public void getLength3()
	{
	  final int BUFFER_SIZE = 176400; // 44100 x 16 x 2 / 8
      byte[]  buffer = new byte[BUFFER_SIZE];
      try
      {
	  AudioInputStream in = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, AudioSystem.getAudioInputStream(file));
	  AudioFormat audioFormat = in.getFormat();
	  
	  float durationInMillis = 1000 * in.getFrameLength() / in.getFormat().getFrameRate();
	     System.out.println("Duration: "+durationInMillis/1000);

	} catch (Exception e) { e.printStackTrace(); }
      
	}
	
	public void testPlay()
	{
	  try {
	    
	    AudioInputStream in= AudioSystem.getAudioInputStream(file);
	    AudioInputStream din = null;
	    float durationInMillis = 1000 * in.getFrameLength() / in.getFormat().getFrameRate();
	     System.out.println("Duration: "+durationInMillis);

	    AudioFormat baseFormat = in.getFormat();
	    AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
	                                                                                  baseFormat.getSampleRate(),
	                                                                                  16,
	                                                                                  baseFormat.getChannels(),
	                                                                                  baseFormat.getChannels() * 2,
	                                                                                  baseFormat.getSampleRate(),
	                                                                                  false);
	    din = AudioSystem.getAudioInputStream(decodedFormat, in);
	    // Play now.
	    rawplay(decodedFormat, din);
	    in.close();
	  } catch (Exception e)
	    {
	        //Handle exception.
	    }
	}

	
	private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException,                                                                                                LineUnavailableException
	{
	  byte[] data = new byte[4096];
	  SourceDataLine line = getLine(targetFormat);
	  if (line != null)
	  {
	    // Start
	    line.start();
	    int nBytesRead = 0, nBytesWritten = 0;
	    while (nBytesRead != -1)
	    {
	        nBytesRead = din.read(data, 0, data.length);
	        if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
	    }
	    // Stop
	    line.drain();
	    line.stop();
	    line.close();
	    din.close();
	  }
	}

	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
	{
	  SourceDataLine res = null;
	  DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
	  res = (SourceDataLine) AudioSystem.getLine(info);
	  res.open(audioFormat);
	  return res;
	} 
	
	void getInfo()
	{
		
		AudioFileFormat baseFileFormat = null;
		AudioFormat baseFormat = null;
		try {
			baseFileFormat = AudioSystem.getAudioFileFormat(file);
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		baseFormat = baseFileFormat.getFormat();
		// TAudioFileFormat properties
		if (baseFileFormat instanceof TAudioFileFormat)
		{
		    Map properties = ((TAudioFileFormat)baseFileFormat).properties();
		    String key = "author";
		    String val = (String) properties.get(key);
		    key = "mp3.id3tag.v2";
		    InputStream tag= (InputStream) properties.get(key);
		    System.out.println("size: "+properties.size());
		   // for(int i=0; i<properties.size(); i++)
		   // {
		   // 	System.out.println(properties.get(i));
		  //  }
		    
		    key = "title";
		   val = (String) properties.get(key);
		    key = "mp3.id3tag.v2";
		    tag= (InputStream) properties.get(key);
		}
		// TAudioFormat properties
		if (baseFormat instanceof TAudioFormat)
		{
		     Map properties = ((TAudioFormat)baseFormat).properties();
		     String key = "bitrate";
		     Integer val = (Integer) properties.get(key);
		     System.out.println("Bitrate: "+val);
		     
		     System.out.println("size: "+properties.size());
		     
		}
		
		
		

	}
	
	void getLength()
	{
	   try
	   {
	     AudioInputStream ais = AudioSystem.getAudioInputStream(file);
	     float durationInMillis = 1000 * ais.getFrameLength() / ais.getFormat().getFrameRate();
	     System.out.println("Duration: "+durationInMillis/1000);
	   }
	   catch(Exception ex)
	   { 
		 System.out.println("Error with playing sound.");
	     ex.printStackTrace();
	    }
	}
}
