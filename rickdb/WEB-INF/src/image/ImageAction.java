package image;

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
import org.apache.struts.action.ActionRedirect;

import db.PasswordDb;

public class ImageAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
    
    String mode = request.getParameter("mode");
    String type = request.getParameter("type");
    String id = request.getParameter("id");
    
    
    ActionForward addImage = mapping.findForward("addImage");
   
    ActionForward error = mapping.findForward("error");  
    ActionForward noAccess = mapping.findForward("noAccess");   
    HttpSession session = request.getSession();
    String user = (String)session.getAttribute("user");
    if (user==null) return noAccess;
    if (!getUser().equals(user)) { return noAccess; }

  
     ImageUploadForm iuf = new ImageUploadForm();
     if ("cust".equals(type)) 
     {  
       iuf.setCust_id(new Long(id).longValue());
       iuf.setType("cust");
     }
     if ("note".equals(type)) 
     {  
       iuf.setNote_id(new Long(id).longValue());
       iuf.setType("note");
     }
     if ("addImage".equals(mode))
     {
       //ttrt
       request.setAttribute("ImageUploadForm", iuf);
       request.setAttribute("SomeString", "Some String Found");
      // System.out.println("ImageAction - ImageUploadForm set");
       return addImage;
     }
     
     return error;
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
