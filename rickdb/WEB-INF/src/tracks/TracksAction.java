package tracks;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import trans.TransData;
import db.PasswordDb;
import db.TransDb;

public class TracksAction extends Action
{
  Hashtable<String, String> acctHash;
  Hashtable<String, String> trackHash;

  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
    ActionForward trackList = mapping.findForward("trackList"); 
    ActionForward showTrack = mapping.findForward("showTrack"); 
    ActionForward acctSummary = mapping.findForward("acctSummary"); 
    ActionForward acctSummaryXLS = mapping.findForward("acctSummaryXLS");
    ActionForward noAccess = mapping.findForward("noAccess");   
    HttpSession session = request.getSession();
    String user = (String)session.getAttribute("user");
    if (user==null) return noAccess;
    if (!getUser().equals(user)) { return noAccess; }

    
    acctHash = (Hashtable<String, String>)request.getSession().getServletContext().getAttribute("AcctHash");
    trackHash = (Hashtable<String, String>)request.getSession().getServletContext().getAttribute("TrackHash");

    String mode = request.getParameter("mode");
    String archiveSet = request.getParameter("archiveSet");
    if (archiveSet==null) 
      archiveSet=(String)request.getSession().getAttribute("ArchiveSet");
    if (archiveSet==null) archiveSet="Current";
    
    //System.out.println("TracksAction - setting session ArchiveSet: "+archiveSet);
    request.getSession().setAttribute("ArchiveSet", archiveSet);
    //System.out.println("TracksAction -  archiveSet = "+archiveSet);
    
    if (mode==null) mode = "trackList";
    
    if ("summary".equals(mode))
    {
      String trackId=request.getParameter("trackId");
      TrackData td = getTrackData(trackId, archiveSet);
      request.setAttribute("TrackData", td);
      return acctSummary;
    }
    
    if ("summaryXLS".equals(mode))
    {
      String trackId=request.getParameter("trackId");
      TrackData td = getTrackData(trackId, archiveSet);
      request.setAttribute("TrackData", td);
      return acctSummaryXLS;
    
    }
    
    if ("trackList".equals(mode))
    {
      request.setAttribute("CurrentMenuItem", "tracks");
      return trackList;
    }
    
    if ("showTrack".equals(mode))
    {
      String trackId=request.getParameter("trackId");
      getTrack(request, trackId, null, archiveSet);
      return showTrack;
    }
   
    if ("showTrackAccount".equals(mode))
    {
      String trackId=request.getParameter("trackId");
      String acctId=request.getParameter("acctId");
      getTrack(request, trackId, acctId, archiveSet);
      return showTrack;
    }
   
    
    request.setAttribute("CurrentMenuItem", "tracks");
    return trackList;
  }

  
  TrackData getTrackData(String trackId, String archiveSet)
  {
    String sql;
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    Date max_date=null, min_date=null;
    try
    {
      Connection db = broker.serviceConnectionManager().getConnection();
      Statement stmt = db.createStatement();
      
      if ("Current".equals(archiveSet))
      sql="select max(trans_date) as max_date from trans where track_id = "
        +trackId
        +" and archived=0";
      else
        sql="select max(trans_date) as max_date from trans where track_id = "
          +trackId
          +" and archived=1 and archive_set='"+archiveSet+"'";
     
      //System.out.println("TracksAction summary sql: "+sql);
      ResultSet rs = stmt.executeQuery(sql);
      if (rs != null) { if (rs.next()) { max_date=rs.getDate("max_date"); } }
       rs.close();
       stmt.close();
       
       stmt = db.createStatement();
       if ("Current".equals(archiveSet))
       sql="select min(trans_date) as min_date from trans where track_id = "
         +trackId+" and archived=0";
       else
         sql="select min(trans_date) as min_date from trans where track_id = "
           +trackId
           +" and archived=1 and archive_set='"+archiveSet+"'";
       
       //System.out.println("TracksAction summary sql2: "+sql);     
       rs = stmt.executeQuery(sql);
        if (rs != null) { if (rs.next()) { min_date=rs.getDate("min_date"); } }
        rs.close();
       stmt.close();

       
       
    } catch (Exception e) { e.printStackTrace();}
    
    
    TrackData trackData = new TrackData();
    
    trackData.setTrackName(trackHash.get(trackId));
    trackData.setTrackId(trackId);
    
    Criteria criteria = new Criteria();
    criteria.addEqualTo("track_id", trackId);
    if ("Current".equals(archiveSet))
        criteria.addEqualTo("archived", 0);
    else
    {
      criteria.addEqualTo("archived", 1);
      criteria.addEqualTo("archive_set", archiveSet);
    }
    
    ReportQueryByCriteria query = QueryFactory.newReportQuery(TransDb.class, criteria, true);
    query.setAttributes(new String[] { "acct_id", "sum(debit)", "sum(credit)" });
    query.addGroupBy("acct_id");
    query.addOrderByAscending("sum(debit)");
    ArrayList acctList = new ArrayList();
    Iterator it = broker.getReportQueryIteratorByQuery(query);
    DecimalFormat nf = new DecimalFormat("##.00");
    AccountSummary as=null;
    Integer acct_id;
    while(it.hasNext())
    {
      Object[] tmp = (Object[])it.next();
      //tdb=(TransDb)it.next();
      Double t1 = (Double)tmp[1];
      
      as = new AccountSummary();
      as.setType("year");
      acct_id=0;
     // acctIdStr=(String)tmp[0];
      acct_id = (Integer)tmp[0];
      
      as.setAcct_id(acct_id.longValue());
      as.setAcct_desc(acctHash.get(""+as.getAcct_id()));
      as.setDebit(((Double)tmp[1]).doubleValue());
      as.setCredit(((Double)tmp[2]).doubleValue());
      acctList.add(as);
      
      trackData.addToCreditTotal(as.getCredit());
      trackData.addToDebitTotal(as.getDebit());
      //System.out.println(tmp[0]+" * "+as.getAcct_id()+": "+nf.format(t1)+" -- "+as.getAcct_desc());
    }
    
    trackData.setTransList(acctList);
    trackData.setMax_date(max_date);
    trackData.setMin_date(min_date);
    broker.close();
    return trackData;
  }
  
  
  

  

  private void getTrack(HttpServletRequest request, String trackId, String acctId, String archiveSet)
  {
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    
    TrackData trackData = new TrackData();
    
    trackData.setTrackName(trackHash.get(trackId));
    trackData.setTrackId(trackId);
    
    ArrayList<TransData> transList = new ArrayList<TransData>();
    
    Criteria criteria = new Criteria();
    criteria.addEqualTo("track_id", trackId);
    if ("Current".equals(archiveSet))
      criteria.addEqualTo("archived", 0);
    else
    {
      criteria.addEqualTo("archived", 1);
      criteria.addEqualTo("archive_set", archiveSet);
    }
    if (acctId!=null) criteria.addEqualTo("acct_id", acctId);
    QueryByCriteria query = new QueryByCriteria(TransDb.class, criteria);
    query.addOrderByDescending("trans_id");
   // query.addOrderByAscending("description");
    Collection trans =broker.getCollectionByQuery(query);
    
    Iterator it =trans.iterator();
    
    TransData td;
    while(it.hasNext())
    {
      td=getTransData((TransDb)it.next());
      trackData.addToCreditTotal(td.getCredit());
      trackData.addToDebitTotal(td.getDebit());
      transList.add(td);
    }
    
    trackData.setTransList(transList);
    request.setAttribute("TrackData", trackData);
    
    broker.close();
  }

  

  private TransData getTransData(TransDb tdb)
  {
    TransData td = new TransData();
    td.setTransId(tdb.getTransId());
    td.setTrack(trackHash.get(""+tdb.getTrackId()));
    td.setAccount(acctHash.get(""+tdb.getAcctId()));
    td.setDebit(tdb.getDebit());
    td.setCredit(tdb.getCredit());
    td.setTransDate(tdb.getTransDate());
    td.setDescription(tdb.getDescription());
    td.setComment(tdb.getComment());
    td.setTrackId(tdb.getTrackId());
    td.setAcctId(tdb.getAcctId());
    td.setCust_id(tdb.getCust_id());
    td.setOrder_id(tdb.getOrder_id());
    return td;
  }
  
  String getUser()
  {
	  PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
	  Criteria criteria = new Criteria();
      criteria.addEqualTo("id", 1);
      QueryByCriteria query = new QueryByCriteria(PasswordDb.class, criteria);
      PasswordDb pdb = (PasswordDb)broker.getObjectByQuery(query);
      broker.close();
      return pdb.getUser();
  }
  
}
