package data;

//java imports
import java.io.*;
import java.nio.charset.*;
import java.util.Vector;
import java.util.Hashtable;
//xml imports
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;


/**
 * iTunesParser is the class in charge of parsing iTunes' xml File.  One
 * instance may be used to parse multiple files, or to parse the same file
 * multiple times.  The parsing is done using the SAX parser.
 *
 * @author	Robbie Hanson
 * @version	1.0
 */
public class iTunesParser extends DefaultHandler
{
	//Location of iTunes Music Library.xml
	private File xmlFile = new File("");
	//Parsing status
	private boolean isParsed = false;
	
	//String buffer to store data as it comes in
	private StringBuffer buffer = new StringBuffer();
	//Track ID mapping class
	private TrackMap mapper;
	//parsing trackers
	private boolean isKey = false;
	private boolean isTracks = false;
	private boolean isPlaylists = false;
	private boolean isID = false;
	private boolean isName = false;
	private boolean isTime = false;
	private boolean isPath = false;
	private boolean isArtist = false;
	private boolean isGrouping = false;
	//parsing ints
	private int numDicts = 0;
	private int numArrays = 0;
	//iTunesData reference
	private iTunesData data;
	//current track reference
	private ItunesTrackData track = new ItunesTrackData();
	//current playlist reference
	private PlaylistData playlist = new PlaylistData();
	//temporary vectors
	private Vector tracksVector = new Vector();
	private Vector playlistsVector = new Vector();
	private Vector plTracksVector  = new Vector();
		
	
	/** 
	 * Constructor
	 *
	 * Parses the iTunes xml file and dumps the information into the passed
	 * iTunesData reference.
	 *
	 * The default iTunes xml file path is used:
	 *   Mac: ~/Music/iTunes/iTunes Music Library.xml
	 *   Win: My Documents/My Music/iTunes/iTunes Music Library.xml
	 *
	 * Note: The position of the "My Documents" folder on Windows can be
	 * changed.  This program accounts for that by getting the registry
	 * entry for it's path.
	 *
	 * @param data	reference to iTunesData instance that is to be filled
	 */
	public iTunesParser(iTunesData data)
	{
		this.data = data;
		
		//use default iTunes xmlFile path
		
			String home = getRegPath();
			String path = "\\My Music\\iTunes\\iTunes Music Library.xml";
			
			xmlFile = new File(home + path);
		
	}
	
	
	/**
	 * Constructor
	 * 
	 * Parses the iTunes xml file and dumps the information into the passed
	 * iTunesData reference.
	 * 
	 * @param data		reference to iTunesData instance that is to be filled
	 *        xmlFile	the iTunes xml file that is to be parsed
	 */
	public iTunesParser(iTunesData data, File xmlFile)
	{
		this.data = data;
		this.xmlFile = xmlFile;
	}
	
	
	/**
	 * Sets the iTunes xml file to be parsed.
	 *
	 * @param xmlFile	the iTunes xml file that is to be parsed
	 */
	public synchronized void setXmlFile(File xmlFile)
	{
		if(!this.xmlFile.equals(xmlFile))
			isParsed = false;
		
		this.xmlFile = xmlFile;
	}
	
	/**
	 * Queries the Windows registry for the path to the users
	 * "My Documents" folder.
	 *
	 * @return The windows registry entry for the current users path
	**/
	private String getRegPath()
	{
		String result;
		
		String cmd[] = new String[5];
		cmd[0] = "reg";
		cmd[1] = "QUERY";
		cmd[2] = "\"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\"";
		cmd[3] = "/v";
		cmd[4] = "Personal";
		
		try
		{
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(cmd);
			InputStream iStream = p.getInputStream();
			
			StringBuffer buff = new StringBuffer();
			
			int c = iStream.read();
			while(c >= 0)
			{
				buff.append((char)c);
				
				c = iStream.read();
			}
			
			String output = buff.toString();
			
			int colonIndex = output.indexOf(':');
			int newlineIndex = output.indexOf("\r\n", colonIndex);
			
			result = output.substring(colonIndex-1, newlineIndex);
		}
		catch(Exception e)
		{
			result = System.getProperty("user.home");
		}
		
		return result;
	}
	
	
	/**
	 * Returns the parsing status.
	 * This will be false if the xml file has never been parsed,
	 * or if the specified xml file has been specified to be another file
	 * since last parse.
	 *
	 * @return boolean	whether the set xml file has been parsed.
	 */
	public synchronized boolean isParsed()
	{
		return isParsed;
	}
	
	
	/**
	 * Parses the set iTunes xml file and outputs the data into the 
	 * set iTunesData reference.
	 *
	 * This method is synchronous: it will not return until parsing has ended.
	 */
	public synchronized void parseFile()
	{
		try
		{
			//setup the TrackMap
			mapper = new TrackMap();
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(false);
			SAXParser saxParser = factory.newSAXParser();
			XMLReader parser = saxParser.getXMLReader();

			parser.setContentHandler(this);

			/* The iTunes XML File is in UTF-8 encoding.
			 * If you read it with a standard FileReader, the default system
			 * encoding will be used, and non-standard unicode characters will
			 * be garbled.
			 */
			Charset utf8 = Charset.forName("UTF-8");
			
			FileInputStream fileIn = new FileInputStream(xmlFile);
			InputStreamReader streamIn = new InputStreamReader(fileIn, utf8);
			XMLInputSource xmlIn = new XMLInputSource(streamIn);
			
			parser.parse(new InputSource(xmlIn));
			isParsed = true;
		}
		catch(SAXParseException spe)
		{
			System.out.print("Parse Exception: Line " + spe.getLineNumber());
			System.out.println(", Column " + spe.getColumnNumber());
			System.out.println("Exception: " + spe.getMessage());
			System.exit(1);
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			System.exit(1);
		}
	}

	
	/**
	 * Called from the Sax Parser whenever it encouters an element
	 * @see org.xml.sax.helpers.DefaultHandler
	 */
	public void startElement(String uri, String localName, String qName, Attributes attribs )
	{
		if(qName.equals("key"))
		{
            isKey = true;
        }
		else if(qName.equals("dict") && isTracks)
		{
			numDicts++;
		}
		else if(qName.equals("array") && isPlaylists)
		{
			numArrays++;
		}
	}
	

	/**
	 * Called from the Sax Parser whenever it encounters an elements text
	 * @see org.xml.sax.helpers.DefaultHandler
	 */
	public void characters( char[] data, int start, int length)
	{
		// only process text from elements we're interested in
        if(isKey || isID || isName || isTime || isPath || isArtist || isGrouping)
		{
			buffer.append(data, start, length);
        }
    }
	

	/**
	 * Called from the Sax Parser whenever it encounters the end of an element
	 * @see org.xml.sax.helpers.DefaultHandler
	 */
	public void endElement(String uri, String localName, String qName)
	{
		if(isKey)
		{
			String keyName = buffer.toString();
			
			if(keyName.equals("Tracks"))
				isTracks = true;
			else if(keyName.equals("Playlists"))
				isPlaylists = true;
			else if(isTracks)
			{
				if(keyName.equals("Track ID"))
					isID = true;
				else if(keyName.equals("Name"))
					isName = true;
				else if(keyName.equals("Artist"))
					isArtist = true;
				else if(keyName.equals("Location"))
					isPath = true;
				else if(keyName.equals("Total Time"))
					isTime = true;
				else if(keyName.equals("Grouping"))
				{
					isGrouping = true;
					//System.out.println("found grouping");
				}
			}
			else if(isPlaylists)
			{
				if(keyName.equals("Track ID"))
					isID = true;
				else if(keyName.equals("Name"))
					isName = true;
			}

			buffer.delete(0, buffer.length());
			isKey = false;
		}
		else if(qName.equals("dict") && isTracks)
		{
			numDicts--;
			if(numDicts == 0)
			{
				//end of all tracks
				data.tracks = new ItunesTrackData[tracksVector.size()];
				for(int i=0; i<tracksVector.size(); i++)
					data.tracks[i] = (ItunesTrackData)tracksVector.elementAt(i);
				isTracks = false;
			}
			else
			{
				//end of current track
				tracksVector.add(track);
				track = new ItunesTrackData();
			}
		}
		else if(qName.equals("array") && isPlaylists)
		{
			numArrays--;
			if(numArrays == 0)
			{
				//end of all playlists
				data.playlists = new PlaylistData[playlistsVector.size()];
				for(int i=0; i<playlistsVector.size(); i++)
					data.playlists[i] = (PlaylistData)playlistsVector.elementAt(i);
				isPlaylists = false;
			}
			else
			{
				//end of current playlist
				playlist.tracks = new int[plTracksVector.size()];
				for(int i=0; i<plTracksVector.size(); i++)
					playlist.tracks[i] = ((Integer)plTracksVector.elementAt(i)).intValue();
				plTracksVector.removeAllElements();
				
				playlistsVector.add(playlist);
				playlist = new PlaylistData();
			}
		}
		else if(isID)
		{
			if(isTracks)
				mapper.addTrackID(buffer.toString());
			else
				plTracksVector.add(mapper.getTrackIndex(buffer.toString()));

			buffer.delete(0, buffer.length());
			isID = false;
		}
		else if(isName)
		{
			if(isTracks)
				track.name = buffer.toString();
			else
				playlist.name = buffer.toString();

			buffer.delete(0, buffer.length());
			isName = false;
		}
		else if(isTime)
		{
			track.time = Integer.parseInt(buffer.toString());

			buffer.delete(0, buffer.length());
			isTime = false;
		}
		else if(isPath)
		{
			track.path = buffer.toString();
			
			buffer.delete(0, buffer.length());
			isPath = false;
		}
		else if(isArtist)
		{
			track.artist = buffer.toString();

			buffer.delete(0, buffer.length());
			isArtist = false;
		}
		else if(isGrouping)
		{
			track.grouping = buffer.toString();
            //System.out.println("group: "+track.grouping);
			buffer.delete(0, buffer.length());
			isGrouping = false;
		}
	}
	
	//{{{ class TrackMap
	/**
	 * Class to map iTunes' Track ID numbers to the song's
	 * index in the data array.
	 *
	 * The track IDs in the iTunes XML file begin with 34 for some reason.
	 * (At least they do in my xml file).
	 * Originally, I just set up the playlist id's to subtract 34 from each one,
	 * thus corresponding to the index of the track in the array.
	 * However, I kept running across problems, where iTunes would add new songs
	 * with track ID's that didn't necessarily correspond to the standard pattern.
	 * For example, in my iTunes XML file, it starts with 34 and goes to 2345.
	 * However, sometimes when I would add another song, iTunes would temporarily
	 * label it's ID something like 7127.  This caused numerous index out of bounds
	 * exceptions.  Thus, this class was developed.
	 */
	private class TrackMap
	{
		private int index;
		private Hashtable hash;
		
		public TrackMap()
		{
			index = 0;
			hash = new Hashtable(500);
		}
		
		public void addTrackID(String ID)
		{			
			hash.put(ID, new Integer(index));
			index++;
		}
		
		public Integer getTrackIndex(String ID)
		{		
			return (Integer)hash.get(ID);
		}
	}
	//}}}

	//{{{ class XMLInputSource
	/**
	 * Class to remove (filter out on input) the second line of the iTunes xml
	 * file.  The !DOCTYPE .... line.
	 *
	 * The iTunes XML file begins with :
	 *
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
	 *
	 * The SAX parser automatically goes out on the internet and fetches this DTD.
	 * This causes quite a delay, and isn't necessary nor desired for the purposes of this program
	 * I have tried to resolve the issue, by turning this fetching off, but it does not seem
	 * to work for me.  Apparently only certain SAX parsers support this feature.
	 *
	 * So until I figure out a way to turn it off, or find a different sax parser I can bundle
	 * I have slapped together this class.
	 */
	public class XMLInputSource extends FilterReader
	{
		private int line;
	
		public XMLInputSource(Reader in)
		{
			super(in);
			line = 0;
		}
		
		public int read() throws IOException
		{
			int character = super.read();
	
			if(line > 1)
			{
				return character;
			}
			else if(line < 1)
			{
				if(character == '\n')
					line++;
	
				return character;
			}
			else//line == 1
			{
				while((char)character != '\n')
					character = super.read();
	
				line++;
				return super.read();									
			}
		}
		
		public int read(char[] cbuf) throws IOException
		{
			return read(cbuf, 0, cbuf.length);
		}
		
		public int read(char[] cbuf, int off, int len) throws IOException
		{
			if(line > 1)
			{
				return super.read(cbuf, off, len);
			}
			else
			{
				//Read data into temporary buffer
				char[] tempBuf = new char[cbuf.length];
	
				int num = 0;
				int index = off;
				int length = super.read(tempBuf, off, len);
				
				//Copy data into cbuf, removing specific line
				for(int i=off; i < off+len; i++)
				{
					if(line < 1)
					{
						cbuf[index++] = tempBuf[i];
						num++;
						if(tempBuf[i] == '\n')
							line++;
					}
					else if(line > 1)
					{
						cbuf[index++] = tempBuf[i];
						num++;
					}
					else
					{
						if(tempBuf[i] == '\n')
							line++;
					}
				}
	
				return num;
			}
		}
	}
	//}}}
}
