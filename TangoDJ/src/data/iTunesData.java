package data;

/**
 * iTunesData stores the song data.
 * <br>
 * The iTunesParser requires a reference to an iTunesData instance.  
 * It uses the reference to fill the tracks and playlists.
 * <br>
 * The iTunesSearcher requires a reference to an iTunesData instance.
 * It uses the reference to alter the table contents during searching,
 * and to switch the table when the playlist is changed.
 *
 * @author	Robbie Hanson
 * @version 1.0
 */
public class iTunesData
{
	/**
	 * Array containing all songs
	 */
	public ItunesTrackData tracks[];
	
    /**
	 * Array containing all playlists
	 */
	public PlaylistData playlists[];

	/**
	 * Array containing current table information
	 * This array contains the data to be displayed in the JTable
	 * Each int corresponds the the track index in <code>tracks[]</code>
	 */
	public int table[];
}
