package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import tangodj2.Db;

public class ImportRatings
{
   public ImportRatings()
   {
     BufferedReader br=null;
     String delims = "[,]";
    
     
    try
    {
      br = new BufferedReader(new FileReader("ratings.txt"));
      String line;
      String pathHash;
      String artistTitleHash;
      String strRating;
      String[] tokens;
       
      Db.databaseConnect(true);
      
      while (true) 
      {
        line = br.readLine();
        if (line==null) break;
        tokens = line.split(delims);
        artistTitleHash=tokens[0];
        pathHash=tokens[1];
        strRating=tokens[2];
        String stars="";
        switch (strRating)
        {
          case "20":
          {
            stars="*";
            break;
          }
          case "40":
          {
            stars="**";
            break;
          }
          case "60":
          {
            stars="***";
            break;
          }
          case "80":
          {
            stars="****";
            break;
          }
          case "100":
          {
            stars="*****";
            break;
          }
          
        }
        System.out.println(pathHash+"-"+stars);
        Db.insertiTunesRating(artistTitleHash, pathHash, stars);
       // Db.applyRating(pathHash, stars);
      }
      br.close();
      Db.databaseDisconnect();
      
    } catch (Exception e) { e.printStackTrace();  }
  }
  

  public static void main(String args[])
  {
    new ImportRatings();
  }
}
