package iTunes.run;
import iTunes.data.PlaylistData;
import iTunes.data.TrackData;
import iTunes.data.iTunesData;
import iTunes.data.iTunesParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JFrame;

import org.apache.commons.codec.net.URLCodec;


public class GetRatingInfo extends JFrame
{
  /*
   * by Rick Roman
   * Parts by Robbie Hanson http://www.robbiehanson.com/iTunesJava.html
   * and Paul Grebenc http://jid3.blinkenlights.org/
   */
  private static Hasher hasher = new Hasher();
  public GetRatingInfo()
  {
    
    PlaylistData pd=null;
    TrackData td = null;
    String path=null;
    String destFileName;
    DecimalFormat nf = new DecimalFormat("00");
    
    String playlist="";
    iTunesData data = new iTunesData();
    iTunesParser parser = new iTunesParser(data);
    parser.parseFile();
    String pathHash="";
    String artistTitleHash;
    String artistTitle;
    String outLine="";
    
    BufferedWriter out=null;
    File outFile = new File("loader_error.txt");
    if (outFile.exists()) outFile.delete();
    FileWriter fstream;
    try
    {
      fstream = new FileWriter("ratings.txt");
      out = new BufferedWriter(fstream);
    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
   
    File file;
    URLCodec ucd = new URLCodec();
    
    try
    {
      for(int i=0; i<data.tracks.length; i++)
      { 
        td =  data.tracks[i];
        artistTitle=td.artist+td.name;
        artistTitleHash=hasher.getMd5Hash(artistTitle.getBytes());
        
        file = new File(ucd.decode(td.path.substring(17)));
       // if (file.exists()) System.out.println("exists");
       // else System.out.println("not exists");
        
        pathHash = hasher.getMd5Hash(file.getPath().toString().getBytes());
        if (td.rating>0)
        {  
          outLine=	i+", "+artistTitleHash+"'"+pathHash+","+td.rating+"'"+td.name;
          out.write(outLine);
          out.newLine();
          System.out.println(outLine);
        }
       
        //if (i>200) break;
      }
      
      out.close();
    } catch (Exception e) { e.printStackTrace(); }
  }

  
  public static void main(String args[])
  {
    
    
    System.out.println();
       
     
    new GetRatingInfo();
  }

}