package run;
import java.awt.Button;
import java.awt.Container;
import java.awt.FlowLayout;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JFrame;

import org.apache.commons.codec.net.URLCodec;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.MediaFile;
import org.blinkenlights.jid3.v1.ID3V1_0Tag;
import org.blinkenlights.jid3.v2.ID3V2_3_0Tag;

import data.*;



public class iTunesTransfer extends JFrame
{
	/*
	 * by Rick Roman
	 * Parts by Robbie Hanson http://www.robbiehanson.com/iTunesJava.html
	 * and Paul Grebenc http://jid3.blinkenlights.org/
	 */
	
	public iTunesTransfer(String sourcePlaylist, String baseTargetDir, String newPlaylistName)
	{
		/*
		System.out.println("Hello World");
	       this.setSize(300, 300);
	       this.setVisible(true);
	       Container cp =getContentPane();
	       cp.setLayout(new FlowLayout());
	       Button b = new Button("Hello");
	       b.setSize(20, 100);
	       cp.add(b);
	       this.setContentPane(cp); 
	       */
		
		PlaylistData pd=null;
		ItunesTrackData td = null;
		String path=null;
		String destFileName;
		DecimalFormat nf = new DecimalFormat("00");
		
		String playlist="";
		iTunesData data = new iTunesData();
		iTunesParser parser = new iTunesParser(data);
		parser.parseFile();
		
		boolean playlistFound=false;
		for(int i=0; i<data.playlists.length; i++)
		{	
		  pd = 	data.playlists[i];
		  playlist=	pd.name;
	 	  
	 	  if (sourcePlaylist.equals(playlist))
	 	  {
	 		playlistFound=true;
	 		System.out.println("playlist: "+playlist);
	 		File playlistDir = new File(baseTargetDir+"\\"+newPlaylistName);
	 		if (!playlistDir.exists()) playlistDir.mkdir();
	 		URLCodec ucd = new URLCodec();
	 		for(int j=0; j<pd.tracks.length; j++)
	 		{
	 		  td=data.tracks[pd.tracks[j]];
	 		 path=td.path;
	 		 path=path.substring(17);
	 		  try{
	 			 path =ucd.decode(path);
	 			 //path=b.toString();
	 		  } catch (Exception e) {e.printStackTrace();}
	 		 
	 		
	 		  //path=td.path.replaceAll("%20", " ");
	 		
	 		//  System.out.println("  Track: "+path);
	 		  File trackFile = new File(path);
	 		  if (trackFile.exists())
	 		  {	  
	 			 // System.out.println("   EXISTS");
	 			  td.name=td.name.replace('?', '-');
	 			 td.name=td.name.replace('#', '-');
	 			td.name=td.name.replace('+', '-');
	 			td.name=td.name.replace('/', '-');
	 			td.name=td.name.replace('\\', '-');
	 			td.name=td.name.replace('\'', '-');
	 			  destFileName=baseTargetDir+"\\"+newPlaylistName+"\\"+nf.format(j+1)+"_"+td.name+".mp3";
	 			  System.out.println("dest file name: "+destFileName);
	 			
	 			  copyfile(trackFile, destFileName);
	 			  File destFile = new File(destFileName);
	 			  MediaFile oMediaFile = new MP3File(destFile);
	 			  ID3V1_0Tag oID3V1_0Tag = new ID3V1_0Tag();
	 	       //   oID3V1_0Tag.setAlbum(newPlaylistName);  /* !!!!!!!!!!!!!!!!*/
	 	          String artist=oID3V1_0Tag.getArtist();
	 	      //   System.out.println("Artist 1: "+oID3V1_0Tag.getArtist());
	 	      // set this v1.0 tag in the media file object
	 	          
	 	         oMediaFile.setID3Tag(oID3V1_0Tag);


	 	          
	 	          ID3V2_3_0Tag oID3V2_3_0Tag = new ID3V2_3_0Tag();
	 	         try{
	 	          oID3V2_3_0Tag.setAlbum(newPlaylistName);
	 	         oID3V2_3_0Tag.setTrackNumber(j+1);
	 	        oID3V2_3_0Tag.setArtist(td.artist);
	 	        // set this v2.3.0 tag in the media file object
	 	        oMediaFile.setID3Tag(oID3V2_3_0Tag);
	 	       
	 	        // update the actual file to reflect the current state of our object 
	 	        oMediaFile.sync();

	 	         } catch(Exception e){e.printStackTrace();}
	 	        
	 		  }
	 		  else System.out.println("   Can't find: "+path);
	 		}
	 	  }
		}
		
		if (!playlistFound) System.out.println("Playlist: <"+sourcePlaylist+"> not found");
		
	}

	private static void copyfile(File f1, String dtFile) {
	    try{
	      //File f1 = new File(srFile);
	      File f2 = new File(dtFile);
	      InputStream in = new FileInputStream(f1);
	      
	      //For Append the file.
//	      OutputStream out = new FileOutputStream(f2,true);

	      //For Overwrite the file.
	      OutputStream out = new FileOutputStream(f2);

	      byte[] buf = new byte[1024];
	      int len;
	      while ((len = in.read(buf)) > 0){
	        out.write(buf, 0, len);
	      }
	      in.close();
	      out.close();
	      System.out.println("File copied.");
	    }
	    catch(FileNotFoundException ex){
	      System.out.println(ex.getMessage() + " in the specified directory.");
	      System.exit(0);
	    }
	    catch(IOException e){
	      System.out.println(e.getMessage());      
	    }
	  }
	public static void main(String args[])
	{
		
		String sourcePlaylist="";
		String baseTargetDir;
		String newPlaylistName;
		System.out.println();
		if (args.length<2)
		{
	      System.out.println("Usage: java -jar transfer \"playlist name\" base_target_dir");
	      return;
		}
		
		  sourcePlaylist=args[0];
		  baseTargetDir=args[1];
		  newPlaylistName=args[2];
		  
		  if (newPlaylistName==null) newPlaylistName=sourcePlaylist;
		  
		  File baseTargetDirFile = new File(baseTargetDir);
		  if (!baseTargetDirFile.exists())
		  {
			  System.out.println(baseTargetDirFile+" doesn't exist");
			  return;
		  }
		  System.out.println(baseTargetDirFile+" found");
		new iTunesTransfer(sourcePlaylist,baseTargetDir,newPlaylistName);
	}

}