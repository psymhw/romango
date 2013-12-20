package tangodj2;

public class TrackLoaderStats
{
  public int runType=-1;
  public String root="";
  public int fileCount=0;
  public int dirCount=0;
  public int successCount=0;
  public int duplicateCount=0;
  //public int errorCount=0;
  public int farngCount=0;
  public int farngErrors=0;
  public int mediaPlayerErrorCount=0;
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("TrackLoader3 results\n");
    sb.append("Root: "+root+"\n");
    sb.append("Directorys: "+dirCount+"\n");
    sb.append("Files: "+fileCount+"\n");
    sb.append("Success: "+successCount+"\n");
    sb.append("Duplicates: "+duplicateCount+"\n");
    sb.append("Farng Errors: "+farngErrors+"\n");
    sb.append("MediaPlayer Errors: "+mediaPlayerErrorCount+"\n");
    return sb.toString();
  }
}
