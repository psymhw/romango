package feet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.AddressDb;
import db.FeetDb;
import db.PasswordDb;



public class FeetListAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) throws Exception 
  {
    ActionForward feetList = mapping.findForward("feetList");  
    ActionForward noAccess = mapping.findForward("noAccess");   
    HttpSession session = request.getSession();
    String user = (String)session.getAttribute("user");
    if (user==null) return noAccess;
    

    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    if (!getUser(broker).equals(user)) { broker.close(); return noAccess; }
    
    ArrayList feet = getFeetList(broker);
    
    broker.close();
    request.setAttribute("FeetList", feet );
    request.setAttribute("CurrentMenuItem", "feet");
    return feetList;
  }
  
  ArrayList getFeetList(PersistenceBroker broker)
  {
    
    Criteria criteria = new Criteria();
    QueryByCriteria query = new QueryByCriteria(FeetDb.class, criteria);
    query.addOrderByAscending("rt_length");
    Collection feet =broker.getCollectionByQuery(query);
   
    
    ArrayList<FeetData> al = new ArrayList<FeetData>();
    
    FeetData fd=null;
    FeetDb fdb=null;
    Iterator it = feet.iterator();
    while(it.hasNext())
    {
      fdb=(FeetDb)it.next();
      try 
      {
        fd= new FeetData();
        PropertyUtils.copyProperties(fd, fdb); 
      }
      catch (Exception e) 
      { System.out.println("FeetListAction - cannot copy properties from FeetDb to FeetData"); return null; }
      fd = getCustName(fd, broker);
      al.add(fd);
    }
    
    return al;
  }

  private FeetData getCustName(FeetData fd,  PersistenceBroker broker)
  {
    Criteria criteria = new Criteria();
    criteria.addEqualTo("id", fd.getCust_id());
    QueryByCriteria query = new QueryByCriteria(AddressDb.class, criteria);
    AddressDb adb = (AddressDb)broker.getObjectByQuery(query);
    fd.setLastname(adb.getLastname());
    fd.setFirstname(adb.getFirstname());
    return fd;
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
