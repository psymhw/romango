package admin;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.util.ObjectModification;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.AccountsDb;
import db.PasswordDb;
import db.TracksDb;


public class AdminAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
    ActionForward show = mapping.findForward("show");   
    ActionForward trackEdit = mapping.findForward("trackEdit"); 
    ActionForward accountEdit = mapping.findForward("accountEdit"); 
    
    ActionForward noAccess = mapping.findForward("noAccess");   
    HttpSession session = request.getSession();
    String user = (String)session.getAttribute("user");
    if (user==null) return noAccess;
   
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    if (!getUser(broker).equals(user)) { broker.close(); return noAccess; }
    broker.close();
    
    String mode = request.getParameter("mode");
    if (mode==null) mode="show";
    
    if ("editTrack".equals(mode)) 
    {  
      getTrack(request);
      return trackEdit;
    }
    
    if ("updateTrack".equals(mode)) 
    {  
      updateTrack(request);
      getAdminTables(request);
      return show;
    }
    
    if ("editAccount".equals(mode)) 
    {  
      getAccount(request);
      return accountEdit;
    }
    
    if ("updateAccount".equals(mode)) 
    {  
      updateAccount(request);
      getAdminTables(request);
      return show;
    }
    
    if ("show".equals(mode))
    {  
      getAdminTables(request);
      return show;
    }
    
    if ("insertTrack".equals(mode))
    {  
      insertTrack(request);
      getAdminTables(request);
      return show;
    }
    
    if ("insertAccount".equals(mode))
    {  
      insertAccount(request);
      getAdminTables(request);
      return show;
    }
    
    return show;
  }

  private void getTrack(HttpServletRequest request)
  {
    // TODO Auto-generated method stub
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    
    Criteria criteria = new Criteria();
    criteria.addEqualTo("track_id", request.getParameter("trackId"));
    QueryByCriteria query = new QueryByCriteria(TracksDb.class, criteria);
    TracksDb tdb = (TracksDb)broker.getObjectByQuery(query);
    request.setAttribute("TracksDb", tdb);
    broker.close();
  }

  private void updateTrack(HttpServletRequest request)
  {
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    
    Criteria criteria = new Criteria();
    criteria.addEqualTo("track_id", request.getParameter("trackId"));
    QueryByCriteria query = new QueryByCriteria(TracksDb.class, criteria);
    TracksDb tdb = (TracksDb)broker.getObjectByQuery(query);
    tdb.setDescription(request.getParameter("description"));
    broker.beginTransaction();
    broker.store(tdb,ObjectModification.UPDATE);
    broker.commitTransaction();
    broker.close();
  }
  
  private void getAccount(HttpServletRequest request)
  {
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    
    Criteria criteria = new Criteria();
    criteria.addEqualTo("acct_id", request.getParameter("acctId"));
    QueryByCriteria query = new QueryByCriteria(AccountsDb.class, criteria);
    AccountsDb adb = (AccountsDb)broker.getObjectByQuery(query);
    request.setAttribute("AccountsDb", adb);
    broker.close();
  }

  private void updateAccount(HttpServletRequest request)
  {
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    
    Criteria criteria = new Criteria();
    criteria.addEqualTo("acct_id", request.getParameter("acctId"));
    QueryByCriteria query = new QueryByCriteria(AccountsDb.class, criteria);
    AccountsDb adb = (AccountsDb)broker.getObjectByQuery(query);
    adb.setDescription(request.getParameter("description"));
    broker.beginTransaction();
    broker.store(adb,ObjectModification.UPDATE);
    broker.commitTransaction();
    broker.close();
  }

  
  private void getAdminTables(HttpServletRequest request)
  {
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    
    Criteria criteria = new Criteria();
    QueryByCriteria query = new QueryByCriteria(TracksDb.class, criteria);
    query.addOrderByAscending("description");
    Collection trackList =broker.getCollectionByQuery(query);
    request.setAttribute("TrackList", trackList);
    
    criteria = new Criteria();
    query = new QueryByCriteria(AccountsDb.class, criteria);
    query.addOrderByAscending("description");
    Collection accountList =broker.getCollectionByQuery(query);
    request.setAttribute("AccountList", accountList);
    request.setAttribute("CurrentMenuItem", "admin");
    broker.close();
    
    InitAction ia = new InitAction();
    ia.doInit(request.getSession().getServletContext());
    
   
  }
  
  private void insertTrack(HttpServletRequest request)
  {
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    
    TracksDb tdb = new TracksDb();
    tdb.setDescription(request.getParameter("description"));
    broker.beginTransaction();
    broker.store(tdb,ObjectModification.INSERT);
    broker.commitTransaction();
    broker.close();
  }
  
  private void insertAccount(HttpServletRequest request)
  {
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    
    AccountsDb adb = new AccountsDb();
    adb.setDescription(request.getParameter("description"));
    broker.beginTransaction();
    broker.store(adb,ObjectModification.INSERT);
    broker.commitTransaction();
    broker.close();
  }
  
 
  String getUser(PersistenceBroker broker)
  {
	  Criteria criteria = new Criteria();
      criteria.addEqualTo("id", 1);
      QueryByCriteria query = new QueryByCriteria(PasswordDb.class, criteria);
      PasswordDb pdb = (PasswordDb)broker.getObjectByQuery(query);
      return pdb.getUser();
  }
   
  

}
