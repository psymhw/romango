package feet;

import java.sql.Timestamp;
import java.util.Date;

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

import db.AddressDb;
import db.FeetDb;
import db.PasswordDb;

import address.AddressForm;

public class FeetEditAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
    ActionForward showEditScreen = mapping.findForward("showEditScreen");  
    ActionForward addressList = mapping.findForward("addressList");  
    ActionForward noAccess = mapping.findForward("noAccess");   
    HttpSession session = request.getSession();
    String user = (String)session.getAttribute("user");
    if (user==null) return noAccess;
        
    ActionForward actionForward=null;
    
    String mode = request.getParameter("mode");
    if (mode==null) mode = "new";
    
    String cust_id=request.getParameter("cust_id");
    int custId=0;
    try { custId=Integer.parseInt(cust_id); } catch(Exception e){}
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    if (!getUser(broker).equals(user)) { broker.close(); return noAccess; }
    
    if ("edit".equals(mode))
    {
      actionForward = showEditScreen;
      String id = request.getParameter("feet_id");
      FeetForm ff = getFeetForm(broker, id);
      request.setAttribute("FeetForm", ff);
    }
    
    
    if ("new".equals(mode))
    {
      actionForward = showEditScreen;
      FeetForm ff = new FeetForm();
      ff.setCust_id(custId);
      ff.setCustomer_name(getCustomerName(custId, broker));
      ff.setMode("insert");
      ff.setUnits("cm");
      ff.setMeasure_date(new Timestamp(new Date().getTime()));
      
      request.setAttribute("FeetForm", ff);
    }
    
    if ("insert".equals(mode))
    {
      actionForward = addressList;
      FeetForm ff = (FeetForm)form;
      insertFeetRecord(ff, broker);
      broker.clearCache();
     
    }
    
    if ("update".equals(mode))
    {
      actionForward = addressList;
      FeetForm ff = (FeetForm)form;
      updateFeetRecord(ff, broker);
      broker.clearCache();
     
    }
    
    broker.close();
    return actionForward;
  }

  private FeetForm getFeetForm(PersistenceBroker broker, String id)
  {
    Criteria criteria = new Criteria();
    criteria.addEqualTo("id", id);
    QueryByCriteria query = new QueryByCriteria(FeetDb.class, criteria);
    FeetDb fdb =(FeetDb)broker.getObjectByQuery(query);
 
    
    
    FeetForm ff = new FeetForm();
    ff.setMode("update");
    ff.setId(fdb.getId());
    ff.setCust_id(fdb.getCust_id());
    ff.setCust_dir(fdb.getCust_dir());
    ff.setMeasure_date(fdb.getMeasure_date());
    ff.setNotes(fdb.getNotes());
    ff.setShoe_size_1(fdb.getShoe_size_1());
    ff.setShoe_size_2(fdb.getShoe_size_2());
    ff.setStylus_allowance(fdb.getStylus_allowance());
    
    ff.setLf_ankle_height(fdb.getLf_ankle_height());
    ff.setRt_ankle_height(fdb.getRt_ankle_height());
    
    ff.setLf_b2b(fdb.getLf_b2b());
    ff.setRt_b2b(fdb.getRt_b2b());
    
    ff.setLf_bb_girth(fdb.getLf_bb_girth());
    ff.setRt_bb_girth(fdb.getRt_bb_girth());
    
    ff.setLf_bb_width(fdb.getLf_bb_width());
    ff.setRt_bb_width(fdb.getRt_bb_width());
    
    ff.setLf_heel_width(fdb.getLf_heel_width());
    ff.setRt_heel_width(fdb.getRt_heel_width());
    
    ff.setLf_instep_girth(fdb.getLf_instep_girth());
    ff.setRt_instep_girth(fdb.getRt_instep_girth());
    
    ff.setLf_length(fdb.getLf_length());
    ff.setRt_length(fdb.getRt_length());
    
    ff.setLf_short_heel(fdb.getLf_short_heel());
    ff.setRt_short_heel(fdb.getRt_short_heel());
    
    ff.setLf_waist_girth(fdb.getLf_waist_girth());
    ff.setRt_waist_girth(fdb.getRt_waist_girth());
    
    return ff;
  }

  private void insertFeetRecord(FeetForm ff, PersistenceBroker broker)
  {
    FeetDb fdb = getFeetDb(ff);
    //System.out.println("fdb.getCusst_id: "+fdb.getCust_id());
   // System.out.println("ff.getCusst_id: "+ff.getCust_id());
    
    broker.beginTransaction();
    broker.store(fdb, ObjectModification.INSERT);
    broker.commitTransaction();
  }
  
  private void updateFeetRecord(FeetForm ff, PersistenceBroker broker)
  {
    FeetDb fdb = getFeetDb(ff);
    broker.beginTransaction();
    broker.store(fdb, ObjectModification.UPDATE);
    broker.commitTransaction();
  }


  FeetDb getFeetDb(FeetForm ff)
  {
    FeetDb fdb = new FeetDb();
    
    fdb.setId(ff.getId());
    fdb.setCust_id(ff.getCust_id());
    fdb.setCust_dir(ff.getCust_dir());
    fdb.setMeasure_date(ff.getMeasure_date());
    fdb.setNotes(ff.getNotes());
    fdb.setShoe_size_1(ff.getShoe_size_1());
    fdb.setShoe_size_2(ff.getShoe_size_2());
    fdb.setStylus_allowance(ff.getStylus_allowance());
    
    fdb.setLf_ankle_height(ff.getLf_ankle_height());
    fdb.setRt_ankle_height(ff.getRt_ankle_height());
    
    fdb.setLf_b2b(ff.getLf_b2b());
    fdb.setRt_b2b(ff.getRt_b2b());
    
    fdb.setLf_bb_girth(ff.getLf_bb_girth());
    fdb.setRt_bb_girth(ff.getRt_bb_girth());
    
    fdb.setLf_bb_width(ff.getLf_bb_width());
    fdb.setRt_bb_width(ff.getRt_bb_width());
    
    fdb.setLf_heel_width(ff.getLf_heel_width());
    fdb.setRt_heel_width(ff.getRt_heel_width());
    
    fdb.setLf_instep_girth(ff.getLf_instep_girth());
    fdb.setRt_instep_girth(ff.getRt_instep_girth());
    
    fdb.setLf_length(ff.getLf_length());
    fdb.setRt_length(ff.getRt_length());
    
    fdb.setLf_short_heel(ff.getLf_short_heel());
    fdb.setRt_short_heel(ff.getRt_short_heel());
    
    fdb.setLf_waist_girth(ff.getLf_waist_girth());
    fdb.setRt_waist_girth(ff.getRt_waist_girth());
    
    return fdb;
  }
  
  private String getCustomerName(long custId, PersistenceBroker broker)
  {
    Criteria criteria = new Criteria();
    criteria.addEqualTo("id", custId);
    QueryByCriteria query = new QueryByCriteria(AddressDb.class, criteria);
    AddressDb adb =(AddressDb)broker.getObjectByQuery(query);
    if (adb==null) return "cust not found: "+custId;
    return adb.getFirstname()+" "+adb.getLastname();
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
