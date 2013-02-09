package trans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.struts.util.LabelValueBean;
import db.AccountsDb;
import db.TracksDb;

public class Lookups
{
  public ArrayList <LabelValueBean>tracks = new ArrayList<LabelValueBean>();
  public ArrayList <LabelValueBean>accounts = new ArrayList<LabelValueBean>();
  public ArrayList archiveSets = new ArrayList();
  public Lookups()
  {
    LabelValueBean x = new LabelValueBean("fish", "bait");
   
    PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
    getAccounts(broker);
    getTracks(broker);
    //getArchiveSets(broker);
    
      broker.close();
  }

  private void getAccounts(PersistenceBroker broker)
  {
    Criteria criteria = new Criteria();
    QueryByCriteria query = new QueryByCriteria(AccountsDb.class, criteria);
    query.addOrderByAscending("description");
    Collection accts = broker.getCollectionByQuery(query);
    
    Iterator it = accts.iterator();
    AccountsDb adb;
    while(it.hasNext())
    { 
      adb=(AccountsDb)it.next();
      accounts.add(new LabelValueBean(  adb.getDescription(), ""+adb.getAcctId()));
    }
  }

  private void getTracks(PersistenceBroker broker)
  {
    Criteria criteria = new Criteria();
    QueryByCriteria query = new QueryByCriteria(TracksDb.class, criteria);
    query.addOrderByAscending("description");
    Collection tck = broker.getCollectionByQuery(query);
    
    Iterator it = tck.iterator();
    TracksDb tdb;
    while(it.hasNext())
    { 
      tdb=(TracksDb)it.next();
      tracks.add(new LabelValueBean(  tdb.getDescription(), ""+tdb.getTrackId()));
    }
  }
 
  private void getArchiveSets(PersistenceBroker broker)
  {
    //ArrayList archivedSets = new ArrayList();
    archiveSets.add("Current");
    String archive_set=null;
  
    try
    {
      Connection db = broker.serviceConnectionManager().getConnection();
      Statement stmt = db.createStatement();    
      ResultSet rs = stmt.executeQuery("select distinct(archive_set) as archive_set from trans");
    
      if (rs != null) 
      { 
        while (rs.next()) { archive_set=rs.getString("archive_set"); } 
        archiveSets.add(archive_set);
       }
       rs.close();
       stmt.close();
       
    } catch (Exception e) { e.printStackTrace();}
    
    Iterator it = archiveSets.iterator();
    while(it.hasNext())
    {
      System.out.println(">>>"+it.next());
    }
  }
  
  
  
}
