package address;

import image.ImageUploadForm;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

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

import db.AddressDb;
import db.FeetDb;
import db.ImageRefDb;
import db.NotesDb;
import db.PasswordDb;


public class AddressEditAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
    ActionForward showEditScreen = mapping.findForward("showEditScreen");   
    ActionForward showRecScreen = mapping.findForward("showRecScreen");  
    ActionForward showRecScreenNewImage = mapping.findForward("showRecScreenNewImage");  
    
    ActionForward addressList = mapping.findForward("addressList"); 
    
    ActionForward noAccess = mapping.findForward("noAccess");   
    HttpSession session = request.getSession();
    String user = (String)session.getAttribute("user");
    if (user==null) return noAccess;
       
    ActionForward actionForward=null;
    
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    
    if (!getUser(broker).equals(user)) { broker.close(); return noAccess; }
    
    
    String mode = request.getParameter("mode");
    if (mode==null) mode = "new";
    
    //System.out.println("AddressEditAction - mode: "+mode);
    
    if ("edit".equals(mode))
    {
      actionForward = showEditScreen;
      String id = request.getParameter("id");
      AddressForm af = getAddressForm(broker, id);
      request.setAttribute("AddressForm", af);
    }
    
    if ("new".equals(mode))
    {
      actionForward = showEditScreen;
      AddressForm af = new AddressForm();
      request.setAttribute("AddressForm", af);
    }
    
    
    if ("show".equals(mode))
    {
      actionForward = showRecScreen;
      String id = request.getParameter("id");
      AddressForm af = getAddressForm(broker, id);
      Collection feetMeasurements = getFeetMeasurements(broker, id);
      Collection imageList = getImageList(id, broker);
      Collection noteList = getNoteList(id, broker);
      request.setAttribute("AddressForm", af);
      request.setAttribute("FeetMeasurements", feetMeasurements);
      request.setAttribute("ImageList", imageList);
      request.setAttribute("NoteList", noteList);
    }
    
    if ("addImage".equals(mode))
    {
      actionForward = showRecScreenNewImage;
      String id = request.getParameter("id");
      AddressForm af = getAddressForm(broker, id);
      Collection feetMeasurements = getFeetMeasurements(broker, id);
      request.setAttribute("AddressForm", af);
      request.setAttribute("FeetMeasurements", feetMeasurements);
      ImageUploadForm iuf = new ImageUploadForm();
      iuf.setCust_id(af.getId());
      request.setAttribute("ImageUploadForm", iuf);
    }
    
    if ("update".equals(mode))
    {
      actionForward = addressList;
      AddressForm af = (AddressForm)form;
      updateAddressRecord(af, broker);
      broker.clearCache();
     // System.out.println("AddressEditAction - id: "+af.getId());
      
      af = getAddressForm(broker, ""+af.getId());
      
      if (af==null)  System.out.println("AddressEditAction - AddressForm is null: ");
     // else System.out.println("AddressEditAction - AddressForm is NOT null: ");
      
      request.setAttribute("AddressForm", af);
    }
    
    if ("insert".equals(mode))
    {
      actionForward = addressList;
      AddressForm af = (AddressForm)form;
      insertAddressRecord(af, broker);
      broker.clearCache();
      af = getAddressForm(broker, ""+af.getId());
      request.setAttribute("AddressForm", af);
    }
    
    broker.close();
    return actionForward;
  }

  private Collection getNoteList(String id, PersistenceBroker broker)
  {
    Criteria criteria = new Criteria();
    criteria.addEqualTo("cust_id", id);
    QueryByCriteria query = new QueryByCriteria(NotesDb.class, criteria);
    Collection notes =(Collection)broker.getCollectionByQuery(query);
    
    return notes;
  }

  private Collection getImageList(String id, PersistenceBroker broker)
  {
    Criteria criteria = new Criteria();
    criteria.addEqualTo("cust_id", id);
    QueryByCriteria query = new QueryByCriteria(ImageRefDb.class, criteria);
    Collection images =(Collection)broker.getCollectionByQuery(query);
    
    return images;
  }

  private Collection getFeetMeasurements(PersistenceBroker broker, String id)
  {
   
    Criteria criteria = new Criteria();
    criteria.addEqualTo("cust_id", id);
    QueryByCriteria query = new QueryByCriteria(FeetDb.class, criteria);
    Collection feet =(Collection)broker.getCollectionByQuery(query);
    return feet;
  }

  private void insertAddressRecord(AddressForm af, PersistenceBroker broker)
  {
    
    AddressDb adb = getAddressDb(af);
    
    broker.beginTransaction();
    broker.store(adb, ObjectModification.INSERT);
    broker.commitTransaction();
  }

  private AddressDb getAddressDb(AddressForm af)
  {
    AddressDb adb = new AddressDb();
    adb.setId(af.getId());
    adb.setLastname(af.getLastname());
    adb.setFirstname(af.getFirstname());
    adb.setCell_phone(af.getCell_phone());
    adb.setCsz(af.getCsz());
    adb.setEmail(af.getEmail());
    adb.setHome_phone(af.getHome_phone());
    adb.setNotes(af.getNotes());
    adb.setStreet1(af.getStreet1());
    adb.setStreet2(af.getStreet2());
    adb.setType(af.getType());
    adb.setWork_phone(af.getWork_phone());
    adb.setCompany(af.getCompany());
    adb.setPersonal(boolToInt(af.isPersonal()));
    adb.setBusiness(boolToInt(af.isBusiness()));
    adb.setProvider(boolToInt(af.isProvider()));
    adb.setCustomer(boolToInt(af.isCustomer()));
    adb.setFamily(boolToInt(af.isFamily()));
    adb.setFeet(boolToInt(af.isFeet()));
    adb.setLast_touch(new Timestamp(new Date().getTime()));
    adb.setWebsite(af.getWebsite());
    
    return adb;
  }

  private int boolToInt(boolean b)
  {
    if (b) return 1;
    return 0;
  }

  private void updateAddressRecord(AddressForm af, PersistenceBroker broker)
  {
    AddressDb adb = getAddressDb(af);
    adb.setLast_touch(new Timestamp(new Date().getTime()));
    broker.beginTransaction();
    broker.store(adb, ObjectModification.UPDATE);
    broker.commitTransaction();
  }
  
  private void touchAddressRecord(AddressDb adb, PersistenceBroker broker)
  {
    adb.setLast_touch(new Timestamp(new Date().getTime()));
    broker.beginTransaction();
    broker.store(adb, ObjectModification.UPDATE);
    broker.commitTransaction();
  }

  private AddressForm getAddressForm(PersistenceBroker broker, String id)
  {
    AddressForm af = new AddressForm();
    Criteria criteria = new Criteria();
    criteria.addEqualTo("id", id);
    QueryByCriteria query = new QueryByCriteria(AddressDb.class, criteria);
    AddressDb adb =(AddressDb)broker.getObjectByQuery(query);
 
    if (adb!=null)
    {
      af.setMode("update");
      af.setId(adb.getId());
      af.setLastname(adb.getLastname());
      af.setFirstname(adb.getFirstname());
      af.setCell_phone(adb.getCell_phone());
      af.setCsz(adb.getCsz());
      af.setEmail(adb.getEmail());
      af.setHome_phone(adb.getHome_phone());
      af.setNotes(adb.getNotes());
      af.setStreet1(adb.getStreet1());
      af.setStreet2(adb.getStreet2());
      af.setType(adb.getType());
      af.setCompany(adb.getCompany());
      af.setWork_phone(adb.getWork_phone());
      af.setPersonal(adb.getPersonal()==1);
      af.setBusiness(adb.getBusiness()==1);
      af.setProvider(adb.getProvider()==1);
      af.setCustomer(adb.getCustomer()==1);
      af.setFamily(adb.getFamily()==1);
      af.setFeet(adb.getFeet()==1);
      af.setWebsite(adb.getWebsite());
      touchAddressRecord(adb, broker);
    }
    
    return af;
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
