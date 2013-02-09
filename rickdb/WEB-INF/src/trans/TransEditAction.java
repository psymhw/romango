package trans;


import java.text.SimpleDateFormat;

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
import org.apache.struts.action.ActionRedirect;

import db.PasswordDb;
import db.TransDb;

public class TransEditAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    ActionForward success = mapping.findForward("success");
    ActionForward editsuccess = mapping.findForward("editsuccess");

    ActionForward transEditScreen = mapping.findForward("transEditScreen");
    ActionForward noAccess = mapping.findForward("noAccess");   
    HttpSession session = request.getSession();
    String user = (String)session.getAttribute("user");
    if (user==null) return noAccess;
    


    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    if (!getUser(broker).equals(user)) { broker.close(); return noAccess; }
    
    ActionRedirect trackRedirect = new ActionRedirect(mapping.findForward("trackRedirect"));
    ActionRedirect orderRedirect = new ActionRedirect(mapping.findForward("order"));



    String order_id = request.getParameter("order_id");
    String mode = request.getParameter("mode");
    
    
    if (mode==null) mode = "newTrans";
    boolean delete = false;
    boolean showTrack = false;
    boolean showTransEditScreen = false;

    if ("newTrans".equals(mode))
    {
      showTransEditScreen = true;
      TransForm tf = getTransForm(request);
      request.setAttribute("TransForm", tf);
    }


    if ("insert".equals(mode))
    {
      showTrack = true;
      TransForm tf = (TransForm)form;
      trackRedirect.addParameter("mode", "showTrack");
      trackRedirect.addParameter("trackId", tf.getTrackId());
      insertTrans(tf, broker);
    }

    if ("edit".equals(mode))
    {
      showTransEditScreen = true;
      TransForm tf = getTransForm(request.getParameter("transId"), broker);

      request.setAttribute("TransForm", tf);
    }

    if ("update".equals(mode))
    {
      showTrack = true;
      TransForm tf = (TransForm)form;
      trackRedirect.addParameter("mode", "showTrack");
      trackRedirect.addParameter("trackId", tf.getTrackId());

      updateTrans(tf, broker);
    }

    if ("delete".equals(mode))
    {
      delete = true;
      TransForm tf = (TransForm)form;
      deleteTrans(tf, broker);
    }

    broker.close();
    
    if ("insert".equals(mode))
    {
    if (order_id!=null)
      {
    	int orderInt=0;
    	try { orderInt= Integer.parseInt(order_id); } catch(Exception e){}
    	if (orderInt!=0)
    	{
    	  orderRedirect.addParameter("mode", "orderView");
    	  orderRedirect.addParameter("id", order_id);
    	  return orderRedirect;
    	}
      }
    }

    if (showTransEditScreen) return transEditScreen;
    else if (showTrack) return trackRedirect;
    else if (delete) return editsuccess;
    else return success;
  }

  private TransForm getTransForm(String transId, PersistenceBroker broker)
  {

    Criteria criteria = new Criteria();
    criteria.addEqualTo("trans_id", transId);
    QueryByCriteria query = new QueryByCriteria(TransDb.class, criteria);
    TransDb tdb =(TransDb)broker.getObjectByQuery(query);
    TransForm tf = new TransForm();
    tf.setMode("update");
    tf.setTransId(tdb.getTransId());
    tf.setTransDate(tdb.getTransDate());
    tf.setAcctId(tdb.getAcctId());
    tf.setTrackId(tdb.getTrackId());
    tf.setDescription(tdb.getDescription());
    tf.setDebit(tdb.getDebit());
    tf.setCredit(tdb.getCredit());
    tf.setComment(tdb.getComment());
    tf.setCust_id(tdb.getCust_id());
    tf.setOrder_id(tdb.getOrder_id());

    return tf;
  }

  private void insertTrans(TransForm tf, PersistenceBroker broker)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    TransDb tdb = new TransDb();
    tdb.setAcctId(tf.getAcctId());
    tdb.setTrackId(tf.getTrackId());
    tdb.setCredit(tf.getCredit());
    tdb.setDebit(tf.getDebit());
    tdb.setTransDate(tf.getTransDate());
    tdb.setArchive_set(sdf.format(tf.getTransDate()));
    tdb.setDescription(tf.getDescription());
    tdb.setComment(tf.getComment());
    tdb.setCust_id(tf.getCust_id());
    tdb.setOrder_id(tf.getOrder_id());
    broker.beginTransaction();
    broker.store(tdb, ObjectModification.INSERT);
    broker.commitTransaction();
  }

  private void updateTrans(TransForm tf, PersistenceBroker broker)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    TransDb tdb = new TransDb();
    tdb.setTransId(tf.getTransId());
    tdb.setAcctId(tf.getAcctId());
    tdb.setTrackId(tf.getTrackId());
    tdb.setCredit(tf.getCredit());
    tdb.setDebit(tf.getDebit());
    tdb.setTransDate(tf.getTransDate());
    tdb.setArchive_set(sdf.format(tf.getTransDate()));
    tdb.setDescription(tf.getDescription());
    tdb.setComment(tf.getComment());
    tdb.setCust_id(tf.getCust_id());
    tdb.setOrder_id(tf.getOrder_id());
  //  System.out.println("TransEditAction - TransDb "+tdb.toString());
    broker.beginTransaction();
    broker.store(tdb, ObjectModification.UPDATE);
    broker.commitTransaction();

  }

  private void deleteTrans(TransForm tf, PersistenceBroker broker)
  {
    Criteria criteria = new Criteria();
    criteria.addEqualTo("trans_id", tf.getTransId());
    QueryByCriteria query = new QueryByCriteria(TransDb.class, criteria);

    broker.beginTransaction();
    broker.deleteByQuery(query);
    broker.commitTransaction();
  }

  private TransForm getTransForm(HttpServletRequest request)
  {
    String trackId = request.getParameter("trackId");
    long tid=0;
    try {tid=Long.parseLong(trackId); } catch(Exception e) {}
    
    String custId = request.getParameter("cust_id");
    long cust_id=0;
    try {cust_id=Long.parseLong(custId); } catch(Exception e) {}
    
    String orderId = request.getParameter("order_id");
    long order_id=0;
    try {order_id=Long.parseLong(orderId); } catch(Exception e) {}
    
    TransForm tf = new TransForm();
    tf.setMode("insert");
    if (tid!=0) tf.setTrackId(tid);
    if (cust_id!=0) tf.setCust_id(cust_id);
    if (order_id!=0) tf.setOrder_id(order_id);
    
    java.util.Date today = new java.util.Date();
    tf.setTransDate(new java.sql.Date(today.getTime()));
    return tf;
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