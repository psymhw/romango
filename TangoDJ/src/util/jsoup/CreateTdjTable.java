package util.jsoup;

import java.sql.SQLException;

import tangodj2.CreateDatabase;
import tangodj2.Db;

public class CreateTdjTable {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try {
			Db.databaseConnect(true);
			Db.createTDJTable();
			Db.databaseDisconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
