package iTunes.run;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.codec.net.URLCodec;

import iTunes.data.PlaylistData;
import iTunes.data.TrackData;
import iTunes.data.iTunesData;
import iTunes.data.iTunesParser;

public class ExportPlaylists
{
  String[] orchestra=
  {
    "Biagi", 
    "Calo", 
    "Canaro",
    "D'Agostino",
    "D'Arienzo",
    "DeAngeles",
    "Demare",
    "DiSarli",
    "Donato",
    "Fresedo",
    "Garcia",
    "Laurenz",
    "Lomuto",
    "OTV",
    "Pugliese",
    "QP",
    "Rodriguez",
    "Tanturi",
    "Troilo"
  };
  private static Hasher hasher = new Hasher();
  
  public ExportPlaylists()
  {
    iTunesData data = new iTunesData();
    iTunesParser parser = new iTunesParser(data);
    parser.parseFile();
    
    PlaylistData pd=null;
    String playlist="";
    
    TrackData td = null;
    String path=null;
    URLCodec ucd = new URLCodec();
    
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
      fstream = new FileWriter("favorites.txt");
      out = new BufferedWriter(fstream);
    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    for(int i=0; i<data.playlists.length; i++)
    { 
      pd =  data.playlists[i];
      playlist= pd.name;
      if (isFavorite(playlist))
      {
        for(int j=0; j<pd.tracks.length; j++)
        {
          td=data.tracks[pd.tracks[j]];
          path=td.path;
          path=path.substring(17);
          try
          {
            path =ucd.decode(path);
          } catch (Exception e) {e.printStackTrace();}
          File file = new File(path);
          if (file.exists())
          {  
            artistTitle=td.artist+td.name;
            artistTitleHash=hasher.getMd5Hash(artistTitle.getBytes());
            pathHash = hasher.getMd5Hash(file.getPath().toString().getBytes());
            
            try
            {
              out.write(outLine);
              out.newLine();
            } catch(Exception e) { e.printStackTrace(); }
            outLine=playlist.replace('-', ',')+","+artistTitleHash+","+pathHash;
            
            System.out.println(outLine);
          }
        }
      }
    }
    
    try
    { out.close(); } catch(Exception e) { e.printStackTrace(); }
  }
  
  private boolean isFavorite(String playlist)
  {
    for(int i=0; i<orchestra.length; i++)
    {
      if ((orchestra[i]+"-Instrumental").equals(playlist)) return true;
      if ((orchestra[i]+"-Milonga").equals(playlist)) return true;
      if ((orchestra[i]+"-Vals").equals(playlist)) return true;
      if ((orchestra[i]+"-Vocals").equals(playlist)) return true;
      if ((orchestra[i]+"-Derrick").equals(playlist)) return true;
    }
    return false;
  }

  public static void main(String args[])
  {
    new ExportPlaylists();
  }
}
