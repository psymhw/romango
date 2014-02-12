package util.jsoup;

public class TDJDb 
{
  public int id;
  public int tdj_id;
  public String search_type;
  public String search_string;
  public String track;
  public String title;
  public String artist;
  public String date;
  public String genre;
  public String length;
  public String rating;
  public String album;
  public String publisher;
  public String comment;
  
  public void print()
  {
	System.out.println("id:\t\t"+id);
	System.out.println("tdj_id:\t\t"+tdj_id);
	System.out.println("type:\t\t"+search_type);
	System.out.println("search:\t\t"+search_string);
	System.out.println("title:\t\t"+title);
	System.out.println("track:\t\t"+track);
	System.out.println("artist:\t\t"+artist);
	System.out.println("date:\t\t"+date);
	System.out.println("genre:\t\t"+genre);
	System.out.println("length:\t\t"+length);
	System.out.println("rating:\t\t"+rating);
	System.out.println("album:\t\t"+album);
	System.out.println("publisher:\t\t"+publisher);
	System.out.println("comment:\t\t"+comment);
	System.out.println("");
	
  }
}
