package order;

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
import org.apache.struts.action.ActionRedirect;

import db.AddressDb;
import db.ImageRefDb;
import db.NotesDb;
import db.OrdersDb;
import db.PasswordDb;

import address.AddressForm;

public class OrderStoreAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    ActionForward orderList = mapping.findForward("orderList");
    ActionRedirect redirect = new ActionRedirect(mapping.findForward("orderList"));
    redirect.addParameter("mode", "orderList");
    ActionForward noAccess = mapping.findForward("noAccess");   
    HttpSession session = request.getSession();
    String user = (String)session.getAttribute("user");
    if (user==null) return noAccess;
    


    ActionForward actionForward=null;

    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    if (!getUser(broker).equals(user)) { broker.close(); return noAccess; }
    
    OrderForm of = (OrderForm)form;


    String mode = request.getParameter("mode");

    if (of!=null) mode=of.getMode();

    //System.out.println("OrderStoreAction - mode: "+mode);
   // System.out.println("OrderStoreAction - test: "+mode);

    String complete = request.getParameter("complete");
    //System.out.println("OrderStoreAction - complete param: "+complete);
    if (complete==null) of.setComplete(false);

    if ("insert".equals(mode))
    {

      insertOrder(broker, of);
      actionForward = redirect;
    }

    if ("update".equals(mode))
    {

      updateOrder(broker, of);
      actionForward = redirect;
    }

    if ("delete".equals(mode))
    {
      String id = request.getParameter("id");
      actionForward=redirect;
      deleteOrder(broker, id);
    }

    broker.close();
    return actionForward;
  }

  private void deleteOrder(PersistenceBroker broker, String id)
  {
    Criteria criteria = new Criteria();
    criteria.addEqualTo("id", id);
    QueryByCriteria query = new QueryByCriteria(OrdersDb.class, criteria);
    broker.deleteByQuery(query);
  }

  private void insertOrder(PersistenceBroker broker, OrderForm of)
  {
    OrdersDb odb = new OrdersDb();
    odb.setCust_id(of.getCust_id());
    odb.setOrder_date(of.getOrder_date());
    odb.setEst_delivery(of.getEst_delivery());
    odb.setPrice(of.getPrice());
    odb.setNotes(of.getNotes());
    odb.setStatus(of.getStatus());
    if (of.getComplete()) odb.setComplete(1); else odb.setComplete(0);
    //odb.setComplete(of.getComplete());

    broker.beginTransaction();
    broker.store(odb, ObjectModification.INSERT);
    broker.commitTransaction();
  }

  private void updateOrder(PersistenceBroker broker, OrderForm of)
  {
      System.out.println("OrderStoreAction - update");
    OrdersDb odb = new OrdersDb();
    odb.setId(of.getId());
    odb.setCust_id(of.getCust_id());
    odb.setOrder_date(of.getOrder_date());
    odb.setEst_delivery(of.getEst_delivery());
    odb.setPrice(of.getPrice());
    odb.setNotes(of.getNotes());
    odb.setStatus(of.getStatus());
    if (of.getComplete()) odb.setComplete(1); else odb.setComplete(0);
    //odb.setComplete(of.getComplete());
   // System.out.println("OrderStoreAction - complete: "+of.getComplete());
    broker.beginTransaction();
    broker.store(odb, ObjectModification.UPDATE);
    broker.commitTransaction();
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