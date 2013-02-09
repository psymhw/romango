package trans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.PasswordDb;
import db.TransDb;

public class TransHomeAction extends Action
{
  Hashtable<String, String> acctHash;
  Hashtable<String, String> trackHash;

  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    ActionForward success = mapping.findForward("success");
    ActionForward noAccess = mapping.findForward("noAccess");   
    HttpSession session = request.getSession();
    String user = (String)session.getAttribute("user");
    if (user==null) return noAccess;
   

    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    if (!getUser(broker).equals(user)) { broker.close(); return noAccess; }
    
    acctHash = (Hashtable<String, String>)request.getSession().getServletContext().getAttribute("AcctHash");
    trackHash = (Hashtable<String, String>)request.getSession().getServletContext().getAttribute("TrackHash");
    String sortBy = request.getParameter("sortBy");
    if (sortBy==null)
      sortBy=(String)request.getSession().getAttribute("lastTransMasterSort");
    if (sortBy==null)  sortBy="trandId";
    request.getSession().setAttribute("lastTransMasterSort", sortBy);
    getTrans(request, broker, sortBy);
    broker.close();
    return success;
  }

  private void getTrans(HttpServletRequest request, PersistenceBroker broker, String sortBy)
  {
    ArrayList<TransData> transList = new ArrayList<TransData>();

    Criteria criteria = new Criteria();
    criteria.addEqualTo("archived", 0);

    QueryByCriteria query = new QueryByCriteria(TransDb.class, criteria);
    if ("date".equals(sortBy))
    {
      query.addOrderByDescending("trans_date");
      query.addOrderByAscending("description");
    }
    if ("transId".equals(sortBy))
    {
      query.addOrderByDescending("trans_id");
      query.addOrderByAscending("description");
    }


    Collection trans =broker.getCollectionByQuery(query);

    Iterator it =trans.iterator();

    TransData td;
    while(it.hasNext())
    {
      td=getTransData((TransDb)it.next());
      transList.add(td);
    }


    request.setAttribute("TransList", transList);
    request.setAttribute("CurrentMenuItem", "transaction master");
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

  String getUser(PersistenceBroker broker)
  {
	  Criteria criteria = new Criteria();
      criteria.addEqualTo("id", 1);
      QueryByCriteria query = new QueryByCriteria(PasswordDb.class, criteria);
      PasswordDb pdb = (PasswordDb)broker.getObjectByQuery(query);
      return pdb.getUser();
  }


} 