package iTunes.data;

/**
 * TrackData stores information about a song track.
 *
 * @author	Robbie Hanson
 * @version	1.0
 */
public class TrackData
{
	/**
	 * Name of the song (song title)
	 */
	public String name;

	/**
	 * Artist of the song
	 */
	public String artist;
	public String album;
	
	/**
	 * File path of the song
	 */
	public String path;
	
	/**
	 * Total time of the song (in milliseconds)
	 */
	public int time;
	
	public int rating;

	/**
	 * Initializes empty values for name and artist.
	 *
	 * I have come across times where the iTunes xml file does not
	 * contain certain keys.  I have noticed that sometimes when no
	 * name or artist is set, these keys are absent from the file.
	 * Thus, a constructor is needed to avoid null references.
	 * I have never seen a case where path or time is missing.
	 * Thus I avoid adding defaults for these in the interest of performance.
	 */
	public TrackData()
	{
		name = "";
		artist = "";
	}
}
