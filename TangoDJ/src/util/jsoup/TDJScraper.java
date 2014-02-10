package util.jsoup;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import tangodj2.Db;

public class TDJScraper 
{
  //ArrayList<TDJDb> rows = new ArrayList<>();
  String album="Campo+Afuera";	
  public TDJScraper()
  {
	try
	{
	  Db.databaseConnect(false);	
	  Document doc = Jsoup.connect("http://www.tango-dj.at/database/index.htm?sortby1=Tracknri&albumsearch="+album+"&advsearch=Search")
					  .data("query", "Java")
					  .userAgent("Mozilla")
					  .cookie("auth", "token")
					  .timeout(6000)
					  .post();
      Element tablaRegistros = doc.getElementById("searchresult");
			
	  int columnCount=0;
	  TDJDb tdjdb;			
      for (Element row : tablaRegistros.select("tr")) 
      {
       	columnCount=0;
       	tdjdb = new TDJDb();
        for (Element column : row.select("td")) 
        {
          switch (columnCount)
          {
            case 0:
            	tdjdb.tdj_id=Integer.parseInt(column.text());
            	break;
            case 1:
            	tdjdb.track=column.text();
            	break;
            case 2:
            	tdjdb.title=column.text();
            	break;
            case 3:
            	tdjdb.artist=column.text();
            	break;
            case 4:
            	tdjdb.date=column.text();
            	break;
            case 5:
            	tdjdb.genre=column.text();
            	break;
            case 6:
            	tdjdb.length=column.text();
            	break;
            case 7:
            	tdjdb.rating=column.text();
            	break;
            case 8:
            	tdjdb.album=column.text().trim();
            	break;
            case 9:
            	tdjdb.publisher=column.text().trim();
            	break;
            case 10:
            	tdjdb.comment=column.text();
            	break;
          }
          
          columnCount++;
        }
        
        tdjdb.print();
        
        try 
        {
          Db.insertTdjRecord(tdjdb); }
          catch (SQLIntegrityConstraintViolationException v)
        {
          System.out.println("Duplicate: "+tdjdb.title);
        }
      }
	    //printRows();
	    Db.databaseDisconnect();
	} 
		 catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  
  /*
  private void printRows()
  {
	Iterator<TDJDb> it = rows.iterator();
	TDJDb tdjdb;
	while(it.hasNext())
	{
	  tdjdb=it.next();
	  tdjdb.print();
	}
  }
  */
  
  public static void main(String[] args) 
  {
	new TDJScraper();
  }

}
