package util.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrapeTest 
{
	
	public ScrapeTest()
	{
		try {
			Document doc = Jsoup.connect("http://www.tango-dj.at/database/index.htm?sortby1=Tracknri&albumsearch=Sentimiento+Gaucho&advsearch=Search")
					  .data("query", "Java")
					  .userAgent("Mozilla")
					  .cookie("auth", "token")
					  .timeout(3000)
					  .post();
			String cadena="";
			Element tablaRegistros = doc.getElementById("searchresult");
			
            for (Element row : tablaRegistros.select("tr")) 
            {
                for (Element column : row.select("td")) 
                {
                    // Elements tds = row.select("td");
                    // cadena += tds.get(0).text() + "->" +
                    // tds.get(1).text()
                    // + " \n";
                    cadena += column.text() + ",";
                }
                cadena += "\n";
            }
	        
      System.out.println(cadena);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	  {
		new ScrapeTest();
	  }

}
