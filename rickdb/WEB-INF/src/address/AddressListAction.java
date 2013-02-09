package address;

import java.util.Collection;

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

import db.AddressDb;
import db.PasswordDb;


public class AddressListAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
    ActionForward addressList = mapping.findForward("addressList");   
    
    ActionForward noAccess = mapping.findForward("noAccess");   
    HttpSession session = request.getSession();
    String user = (String)session.getAttribute("user");
    if (user==null) return noAccess;
   
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    
    if (!getUser(broker).equals(user)) { broker.close(); return noAccess; }
    
    AddressListSelectForm alsf = (AddressListSelectForm)form;
    
    if (alsf==null)
    {
      System.out.println("AddressListAction - no form found");
      alsf=new AddressListSelectForm();
    }
    
    
   System.out.println("AddressListAction - button: "+alsf.getLetter());
    
    getAddressList(request, broker, alsf);
    
    broker.close();
    return addressList;
  }

  private void getAddressList(HttpServletRequest request, PersistenceBroker broker, AddressListSelectForm alsf)
  {
    Criteria criteria = new Criteria();
    if (alsf.getLetter()!=null)
    {
      if (!"Clear".equals(alsf.getLetter()))
      {
        if ("last".equals(alsf.getSort())) criteria.addGreaterOrEqualThan("lastname", alsf.getLetter());
        if ("first".equals(alsf.getSort())) criteria.addGreaterOrEqualThan("firstname", alsf.getLetter());
        if ("company".equals(alsf.getSort())) criteria.addGreaterOrEqualThan("company", alsf.getLetter());
      }
    }
    if (alsf.getCategory()!=null)
    {
      if ("customer".equals(alsf.getCategory())) criteria.addEqualTo("customer", 1);
      else if ("personal".equals(alsf.getCategory())) criteria.addEqualTo("personal", 1);
      else if ("business".equals(alsf.getCategory())) criteria.addEqualTo("business", 1);
      else if ("provider".equals(alsf.getCategory())) criteria.addEqualTo("provider", 1);
      else if ("family".equals(alsf.getCategory())) criteria.addEqualTo("family", 1);
    }
    QueryByCriteria query = new QueryByCriteria(AddressDb.class, criteria);
    if ("last".equals(alsf.getSort())) query.addOrderByAscending("lastname");
    if ("first".equals(alsf.getSort())) query.addOrderByAscending("firstname");
    if ("company".equals(alsf.getSort())) query.addOrderByAscending("company");
    
    
    Collection addressList =broker.getCollectionByQuery(query);
    request.setAttribute("AddressList", addressList);
    request.setAttribute("CurrentMenuItem", "address");
    request.setAttribute("AddressListSelectForm", alsf);
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
