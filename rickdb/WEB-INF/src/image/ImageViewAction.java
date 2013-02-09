package image;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.ImageRefDb;
import db.PasswordDb;

public class ImageViewAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
    ActionForward success = mapping.findForward("success"); 
    ActionForward noAccess = mapping.findForward("noAccess");   
    HttpSession session = request.getSession();
    String user = (String)session.getAttribute("user");
    if (user==null) return noAccess;
    

    String id = request.getParameter("id");
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    if (!getUser(broker).equals(user)) { broker.close(); return noAccess; }
    
    ImageDataForm idf = getImageDataForm(broker, id);
    request.setAttribute("ImageDataForm", idf);
    broker.close();
    
    return success;
  }

  private ImageDataForm getImageDataForm(PersistenceBroker broker, String id)
  {
    Criteria criteria = new Criteria();
    criteria.addEqualTo("id", id);
    QueryByCriteria query = new QueryByCriteria(ImageRefDb.class, criteria);
    ImageDataForm idf = null;
    ImageRefDb irdb = (ImageRefDb)broker.getObjectByQuery(query);
    try 
    {
      idf= new ImageDataForm();
      PropertyUtils.copyProperties(idf, irdb); 
    }
    catch (Exception e) 
    { System.out.println("ImageViewAction - cannot copy properties from ImageRefDb to ImageDataFOrm"); return null; }
    idf.setMode("update");
    return idf;
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
