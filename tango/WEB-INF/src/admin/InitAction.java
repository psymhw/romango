package admin;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

//import db.AccountsDb;
//import db.TracksDb;
public class InitAction
{
  
  public void doInit(ServletContext context)
  {    
	  /*
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    
    Hashtable<String, String> acctHash = getAcctHash(broker);
    Hashtable<String, String> trackHash = getTrackHash(broker);
    ArrayList archiveSets = getArchiveSets(broker);
    
    context.setAttribute("AcctHash", acctHash);
    context.setAttribute("TrackHash", trackHash);
    context.setAttribute("ArchiveSets", archiveSets);
      
    
    broker.close();
    */
  }
  /*
  private Hashtable<String, String> getTrackHash(PersistenceBroker broker)
  {
    Criteria criteria = new Criteria();
    QueryByCriteria query = new QueryByCriteria(TracksDb.class, criteria);
    Collection trackList =broker.getCollectionByQuery(query);
    Hashtable<String, String> trackHash = new Hashtable<String, String>();
    Iterator it = trackList.iterator();
    TracksDb tdb;
    while (it.hasNext())
    {
      tdb=(TracksDb)it.next();
      trackHash.put(""+tdb.getTrackId(), tdb.getDescription());
    }
    return trackHash;
  }

  private Hashtable<String, String> getAcctHash(PersistenceBroker broker)
  {
    Criteria criteria = new Criteria();
    QueryByCriteria query = new QueryByCriteria(AccountsDb.class, criteria);
    Collection accountList =broker.getCollectionByQuery(query);
  
    Hashtable<String, String> acctHash = new Hashtable<String, String>();
    Iterator it = accountList.iterator();
    AccountsDb adb;
    while (it.hasNext())
    {
      adb=(AccountsDb)it.next();
      acctHash.put(""+adb.getAcctId(), adb.getDescription());
    }
    return acctHash;
  }

  private ArrayList getArchiveSets(PersistenceBroker broker)
  {
    ArrayList archivedSets = new ArrayList();
    archivedSets.add("Current");
    String archive_set=null;
  
    try
    {
      Connection db = broker.serviceConnectionManager().getConnection();
      Statement stmt = db.createStatement();    
      ResultSet rs = stmt.executeQuery("select distinct(archive_set) as archive_set from trans where archived=1");
    
      if (rs != null) 
      { 
        while (rs.next()) { archive_set=rs.getString("archive_set"); } 
        archivedSets.add(archive_set);
       }
       rs.close();
       stmt.close();
       
    } catch (Exception e) { e.printStackTrace();}
  
    return archivedSets;
  }
  
  */
}
