package util.jsoup;

import java.util.ArrayList;
import java.util.Iterator;

import tangodj2.Db;

public class TestDbRead 
{
	public static void main(String[] args) 
	{
		 ArrayList<TDJDb> rows = new ArrayList<>();
		 TDJDb t;
		try {
			Db.databaseConnect(true);
			rows = Db.getTdjRecords();
			Db.databaseDisconnect();
			
			Iterator<TDJDb> it = rows.iterator();
			while(it.hasNext())
			{			
				t=it.next();
				t.print();
			}
			
		} catch (Exception e) {
			e.printStackTrace();}
	}

}
