package order;

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
import org.apache.ojb.broker.util.ObjectModification;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;

import trans.TransData;

import db.AddressDb;
import db.ImageRefDb;
import db.NotesDb;
import db.OrdersDb;
import db.PasswordDb;
import db.TransDb;

import address.AddressForm;

public class OrderAction extends Action
{
	  Hashtable<String, String> acctHash;
	  Hashtable<String, String> trackHash;
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
    ActionForward showEditScreen = mapping.findForward("showEditScreen");   
    
    ActionForward orderListScreen = mapping.findForward("orderListScreen");  
    ActionForward showViewScreen = mapping.findForward("showViewScreen");
    ActionForward noAccess = mapping.findForward("noAccess");   
    HttpSession session = request.getSession();
    String user = (String)session.getAttribute("user");
    if (user==null) return noAccess;
       
    ActionForward actionForward=null;
    acctHash = (Hashtable<String, String>)request.getSession().getServletContext().getAttribute("AcctHash");
    trackHash = (Hashtable<String, String>)request.getSession().getServletContext().getAttribute("TrackHash");
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    if (!getUser(broker).equals(user)) { broker.close(); return noAccess; }
    
    OrderForm of = (OrderForm)form;
    
    
    String mode = request.getParameter("mode");
    String filter = request.getParameter("filter");
    
    if (filter==null) filter="incomplete";
    
    if (mode==null) mode = "orderList";
    
      
    
    
    if ("newOrder".equals(mode))
    {
      actionForward = showEditScreen;
      String id = request.getParameter("id");
      of = new OrderForm();
     
     long cust_id=0;
     
      try { cust_id=Long.parseLong(id); } catch(Exception e) {}
       of.setCust_id(cust_id);
       of.setMode("insert");
       of.setCustomer_name(getCustomerName(broker, cust_id));
      request.setAttribute("OrderForm", of);
    }
    
    if ("edit".equals(mode))
    {
      actionForward = showEditScreen;
      String id = request.getParameter("id");
      of = getOrderForm(broker, id);
     
     
       of.setMode("update");
       of.setCustomer_name(getCustomerName(broker, of.getCust_id()));
      request.setAttribute("OrderForm", of);
    }
    
    
    if ("orderView".equals(mode))
    {
      String id = request.getParameter("id");
      actionForward=showViewScreen;
      request.setAttribute("OrderHeader", getOrder(broker, id));
      request.setAttribute("OrderLines", getOrderLines(broker, id));
      Collection noteList = getNoteList(id, broker);
      request.setAttribute("NoteList", noteList);
    }
    
    
    
    
    if ("orderList".equals(mode))
    {
      request.setAttribute("orderList", getOrderList(broker, filter));
      actionForward=orderListScreen;
    }
    
    broker.close();
    return actionForward;
  }

  private ArrayList getOrderLines(PersistenceBroker broker, String id)
  {
	 ArrayList orderLines = new ArrayList();
	 Criteria criteria = new Criteria();
	 //criteria.addEqualTo("archived", 0);
	 criteria.addEqualTo("order_id", id);
	    
	 QueryByCriteria query = new QueryByCriteria(TransDb.class, criteria);
	 query.addOrderByAscending("transDate");
	 Collection trans =broker.getCollectionByQuery(query);
	    
	    Iterator it =trans.iterator();
	    
	    TransData td;
	    while(it.hasNext())
	    {
	      td=getTransData((TransDb)it.next());
	     orderLines.add(td);
	    }
	    System.out.println("OrderAction - orderLines: "+orderLines.size());
	 return orderLines;
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
  
  private OrderForm getOrderForm(PersistenceBroker broker, String id)
  {
    OrderForm of = new OrderForm();
    OrdersDb odb = getOrder(broker, id);
    
    of.setId(odb.getId());
    of.setCust_id(odb.getCust_id());
    of.setCustomer_name(getCustomerName(broker, odb.getCust_id()));
    of.setOrder_date(odb.getOrder_date());
    of.setEst_delivery(odb.getEst_delivery());
    of.setNotes(odb.getNotes());
    of.setPrice(odb.getPrice());
    of.setStatus(odb.getStatus());
    if (odb.getComplete()==1) of.setComplete(true); else of.setComplete(false);
   // of.setComplete(odb.getComplete());
    
    return of;
  }


  private OrdersDb getOrder(PersistenceBroker broker, String id)
  {
    Criteria criteria = new Criteria();
    criteria.addEqualTo("id", id);
    QueryByCriteria query = new QueryByCriteria(OrdersDb.class, criteria);
    OrdersDb odb = (OrdersDb)broker.getObjectByQuery(query);
    
    odb.setCustomer_name(getCustomerName(broker, odb.getCust_id()));
    return odb;
  }


  private Collection getOrderList(PersistenceBroker broker, String filter)
  {
    Criteria criteria = new Criteria();
    
    if ("incomplete".equals(filter)) criteria.addEqualTo("complete", 0);
    if ("complete".equals(filter)) criteria.addEqualTo("complete", 1);
    
    QueryByCriteria query = new QueryByCriteria(OrdersDb.class, criteria);
    query.addOrderByAscending("est_delivery");
    Collection orderList =(Collection)broker.getCollectionByQuery(query);
    
    OrdersDb odb;
    ArrayList orderList2 = new ArrayList();
    Iterator it = orderList.iterator();
    while(it.hasNext())
    {
      odb=(OrdersDb)it.next();
      odb.setCustomer_name(getCustomerName(broker, odb.getCust_id()));
      orderList2.add(odb);
    }
    
    return orderList2;
    
  }

  private String getCustomerName(PersistenceBroker broker, long cust_id)
  {
    Criteria criteria = new Criteria();
    criteria.addEqualTo("id", cust_id);
    QueryByCriteria query = new QueryByCriteria(AddressDb.class, criteria);
    AddressDb adrdb =(AddressDb)broker.getObjectByQuery(query);
    if (adrdb!=null)
    {
      return adrdb.getFirstname()+" "+adrdb.getLastname();
    }
    return " error - name not found" ;
  }
  
  String getUser(PersistenceBroker broker)
  {
	  Criteria criteria = new Criteria();
      criteria.addEqualTo("id", 1);
      QueryByCriteria query = new QueryByCriteria(PasswordDb.class, criteria);
      PasswordDb pdb = (PasswordDb)broker.getObjectByQuery(query);
      return pdb.getUser();
  }
  
  private Collection getNoteList(String id, PersistenceBroker broker)
  {
    Criteria criteria = new Criteria();
    criteria.addEqualTo("order_id", id);
    QueryByCriteria query = new QueryByCriteria(NotesDb.class, criteria);
    Collection notes =(Collection)broker.getCollectionByQuery(query);
    
    return notes;
  }
}