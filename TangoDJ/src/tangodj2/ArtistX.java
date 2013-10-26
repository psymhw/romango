package tangodj2;

public class ArtistX
{
   public String first="";
   public String last="";
   public int length=0;
   public int index;
   
   public ArtistX(String first, String last, int index)
   {
	 this.first=first;
	 this.last=last;
	 this.index=index;
	 length = first.length()+last.length();
   }
   
   public String toString()
   {
	 return last;
   }
}
