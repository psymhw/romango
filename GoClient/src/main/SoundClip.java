package main;

import javax.sound.sampled.*;
import java.io.*;
import java.net.*;

public class SoundClip {
    //the source for audio data
    private AudioInputStream sample;

    //sound clip property is read-only here
    private Clip clip;
    public Clip getClip() { return clip; }

    //looping property for continuous playback
    private boolean looping = false;
    public void setLooping(boolean _looping) { looping = _looping; }
    public boolean getLooping() { return looping; }

    //repeat property used to play sound multiple times
    private int repeat = 0;
    public void setRepeat(int _repeat) { repeat = _repeat; }
    public int getRepeat() { return repeat; }

    //filename property
    private String filename = "";
    public void setFilename(String _filename) { filename = _filename; }
    public String getFilename() { return filename; }

    //property to verify when sample is ready
    public boolean isLoaded() {
        return (boolean)(sample != null);
    }

    //constructor
    public SoundClip() {
    	
        try {
            //create a sound buffer
            clip = AudioSystem.getClip();

        } catch (LineUnavailableException e) { }
    }

    //overloaded constructor accepts a filename
    public SoundClip(String filename) {
        //call the default constructor first
        this();
        //load the sound file
        load(filename);
    } 
    
     

  

    
	//load sound file
    public boolean load(String audiofile) 
    {
      URL url = null;
      try 
      {
    	  url=this.getClass().getResource("/resources/sounds/"+audiofile);  
        //url = new URL(codeBase+"resources\\sounds\\"+audiofile);
        //File tempFile=new File(url.toURI());
        //if (!tempFile.exists()) System.out.println("MidiSequence - "+url.toURI()+" does not exist");
      } catch (Exception e) { e.printStackTrace(); }
        try {

            //prepare the input stream for an audio file
            setFilename(audiofile);
            sample = AudioSystem.getAudioInputStream(url);
            clip.open(sample);
            return true;

        } catch (IOException e) {
            return false;
        } catch (UnsupportedAudioFileException e) {
            return false;
        } catch (LineUnavailableException e) {
            return false;
        }
    }

    public void play() {
        //exit if the sample hasn't been loaded
        if (!isLoaded()) { System.out.println(filename+" is not loaded"); return; }

        //reset the sound clip
        clip.setFramePosition(0);

        //play sample with optional looping
        if (looping)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        else
            clip.loop(repeat);
    }

    public void stop() {
        clip.stop();
    }

}
