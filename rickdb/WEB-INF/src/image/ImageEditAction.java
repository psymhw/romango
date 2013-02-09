package image;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
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

import db.ImageRefDb;
import db.PasswordDb;

public class ImageEditAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
	    ActionForward noAccess = mapping.findForward("noAccess");   
	    HttpSession session = request.getSession();
	    String user = (String)session.getAttribute("user");
	    if (user==null) return noAccess;
	    

    String mode = request.getParameter("mode");
    System.out.println("ImageEditAction - mode: "+mode);
    
     ActionRedirect success = new ActionRedirect(mapping.findForward("success")); 
     //ActionRedirect addImage = new ActionRedirect(mapping.findForward("addImage")); 
     ActionRedirect redirect=null;
    
     //ImageDataForm idf = (ImageDataForm)form;
    ImageDataForm idf = (ImageDataForm) form;
    
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    if (!getUser(broker).equals(user)) { broker.close(); return noAccess; }
   
  /* 
    if ("addImage".equals(mode))
    {
      System.out.println("ImageEditAction - mode2: "+mode);
      
      //updateImageData(broker, idf);
     // success.addParameter("mode", "show");
     // success.addParameter("id", idf.getCust_id());
      redirect=addImage;
    }
 */   
    if ("update".equals(mode))
    {
      updateImageData(broker, idf);
      success.addParameter("mode", "show");
      success.addParameter("id", idf.getCust_id());
      redirect=success;
    }
    
    if ("delete".equals(mode))
    {
      delete(broker, idf);
      success.addParameter("mode", "show");
      success.addParameter("id", idf.getCust_id());
      redirect=success;
    }
    
    
    broker.close();
    
    return redirect;
  }

  private void updateImageData(PersistenceBroker broker, ImageDataForm idf)
  {
    
    ImageRefDb irdb = new ImageRefDb();
    try 
    {
      PropertyUtils.copyProperties(irdb, idf); 
    }
    catch (Exception e) 
    { System.out.println("ImageEditAction - cannot copy properties from ImageDataFOrm");  }
    System.out.println("ImageEditAction - irdb: "+irdb.toString());
    System.out.println("ImageEditAction - idf: "+idf.toString());
    broker.store(irdb, ObjectModification.UPDATE);
    
  }
  private void delete(PersistenceBroker broker, ImageDataForm idf)
  {
    
    ImageRefDb irdb = new ImageRefDb();
    try 
    {
      PropertyUtils.copyProperties(irdb, idf); 
    }
    catch (Exception e) 
    { System.out.println("ImageEditAction - cannot copy properties from ImageDataFOrm");  }
   // System.out.println("ImageEditAction - irdb: "+irdb.toString());
   // System.out.println("ImageEditAction - idf: "+idf.toString());
    broker.delete(irdb);
    
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
