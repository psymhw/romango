package tangodj2.util;

public class DirectoryInfo 
{
  public String path;
  public int total;
  public int notFound;
  
  public DirectoryInfo(String path, int total, int notFound)
  {
	this.path=path;
	this.total=total;
	this.notFound=notFound;
	
	//System.out.println(total+"/"+notFound+" "+path);
  }
}
