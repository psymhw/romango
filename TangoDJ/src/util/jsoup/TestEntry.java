package util.jsoup;

import java.sql.SQLException;

import tangodj2.Db;

public class TestEntry 
{
	public static void main(String[] args) 
	{
		TDJDb t = new TDJDb();
		t.tdj_id=315485;
		t.track="2";
		t.title="Canción desesperada";
		t.artist="Orquesta Francisco Canaro con Nelly Omar";
		t.date="28.01.1946";
		t.genre="Tango";
		t.length="2:55";
		t.rating="60";
		t.album="Sentimiento Gaucho";
		t.publisher="";
		t.comment="";
		
		try {
			Db.databaseConnect(true);
			Db.insertTdjRecord(t);
			Db.databaseDisconnect();
			
		} catch (Exception e) {
			e.printStackTrace();}
		
	}

}
