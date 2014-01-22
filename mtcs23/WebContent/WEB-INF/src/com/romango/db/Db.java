package com.romango.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Db
{
  
  public static Connection getConnection()
  {
    Connection connection=null;
    try
    {
      Context ctx = new InitialContext();
      if (ctx == null ) return null;

      DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/TestDB");
      if (ds==null) return null;
      
      connection = ds.getConnection();
     
    }catch(Exception e) { e.printStackTrace(); }
    
    return connection;
  }
  
  /*
  public static void databaseConnect()  throws SQLException, ClassNotFoundException
  {
   
    try
    {
      Context ctx = new InitialContext();
      if(ctx == null ) throw new Exception("Boom - No Context");

      DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/TestDB");

      if (ds != null) 
      {
        connection = ds.getConnection();
      }
    }catch(Exception e) {
      e.printStackTrace();
    }
  }
  */
  
  public static void test()
  {
    String foo = "Not Connected";
    int bar = -1;
    
    Connection connection=getConnection();
    
    DbString testStr = new DbString("hello world's", 20);
    System.out.println("DbString: "+testStr.getSqlStr());
    
    try
    {
    if(connection != null)  
    {
       Statement stmt = connection.createStatement();
       ResultSet rst = stmt.executeQuery("select id, foo, bar from testdata");
       
       if(rst.next()) 
       {
         int id = rst.getInt(1);
           foo=rst.getString(2);
           bar=rst.getInt(3);
           System.out.println("Id: "+id+" Foo: "+foo+" Bar: "+bar);
        }
       
       rst.close();
       stmt.close();
    }
    
    connection.close();
    } catch(Exception e) { e.printStackTrace(); }
  }
  
  

}
