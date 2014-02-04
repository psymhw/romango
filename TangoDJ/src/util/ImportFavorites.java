package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import tangodj2.Db;

public class ImportFavorites
{
   public ImportFavorites()
   {
     BufferedReader br=null;
     String delims = "[,]";
    
     
    try
    {
      br = new BufferedReader(new FileReader("favorites.txt"));
      String line;
      String pathHash;
      String albumTitleHash;
      String[] tokens;
      String name1;
      String name2;
       
      Db.databaseConnect(true);
      
      while (true) 
      {
        line = br.readLine();
        if (line==null) break;
        tokens = line.split(delims);
        name1=tokens[0];
        name1=name1.replace("'","''");
        name2=tokens[1];
        albumTitleHash=tokens[2];
        pathHash=tokens[3];
       
          
       
        System.out.println(name1+"-"+name2+", "+albumTitleHash+", "+pathHash);
        Db.insertiTunesFavorites(name1+"-"+name2, albumTitleHash, pathHash);
       // Db.applyRating(pathHash, stars);
      }
      br.close();
      Db.databaseDisconnect();
      
    } catch (Exception e) { e.printStackTrace();  }
  }
  

  public static void main(String args[])
  {
    new ImportFavorites();
  }
}
