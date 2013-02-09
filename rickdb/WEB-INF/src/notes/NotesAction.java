package notes;

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
import org.apache.struts.action.ActionRedirect;

import db.AddressDb;
import db.ImageRefDb;
import db.NotesDb;
import db.PasswordDb;

public class NotesAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
  HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
    ActionForward fwd = null;
    ActionForward noteslist = mapping.findForward("noteslist");  
    ActionForward noteslistRedirect = mapping.findForward("noteslistRedirect");  
    ActionForward noteedit = mapping.findForward("noteedit");   
    ActionRedirect ar = new ActionRedirect(mapping.findForward("noteslistRedirect"));
    ActionForward noAccess = mapping.findForward("noAccess");   
    HttpSession session = request.getSession();
    String user = (String)session.getAttribute("user");
    if (user==null) return noAccess;
   

    // System.out.println("NotesAction ...");
    String track = request.getParameter("track");
    if (track==null) track="0";
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    if (!getUser(broker).equals(user)) { broker.close(); return noAccess; }
    
    String mode=request.getParameter("mode");
    String id=request.getParameter("id");
    String filter=request.getParameter("filter");
    
    if (filter==null) filter="none";
    
    if (mode==null) mode="list";
    if ("list".equals(mode))
    {
      getNotesList(request, broker, track, filter);
      fwd=noteslist;
    }
    if ("new".equals(mode))
    {
      NotesForm nf = getNoteForm(request, broker);
      request.setAttribute("NotesForm", nf);
      fwd=noteedit;
    }
    
    if ("insert".equals(mode))
    {
      NotesForm nf = (NotesForm)form;
      insertNote(nf, broker);
      fwd=noteslistRedirect;
      getNotesList(request, broker, track, filter);
      request.setAttribute("NotesForm", nf);
      ar.addParameter("track", track);
      ar.addParameter("mode","list");
      //fwd=noteslistRedirect;
      fwd=ar;
    }
    
    if ("edit".equals(mode))
    {
      NotesForm nf = getNotesForm(request, broker, id);
      //updateNote(nf, broker);
     // fwd=noteslistRedirect;
      //getNotesList(request, broker, track);
      if (nf.getCust_id()>0)
      {
        String customerName=getCustomerName(nf.getCust_id(), broker);
        request.setAttribute("CustomerName", customerName);
      }
      Collection imageList = getImageList(id, broker);
      request.setAttribute("NotesForm", nf);
      request.setAttribute("ImageList", imageList);
      fwd=noteedit;
    }
    
    if ("update".equals(mode))
    {
      NotesForm nf = (NotesForm)form;
      updateNote(nf, broker);
      fwd=noteslistRedirect;
      getNotesList(request, broker, track, filter);
      
      ar.addParameter("track", track);
      ar.addParameter("mode","list");
      //fwd=noteslistRedirect;
      fwd=ar;
    }
    
    if ("delete".equals(mode))
    {
      deleteNote(request, broker, id);
      fwd=noteslistRedirect;
      getNotesList(request, broker, track, filter);
      fwd=noteslistRedirect;
    }
    
    broker.close();
    return fwd;
  }
  
  private String getCustomerName(long cust_id, PersistenceBroker broker)
  {
    AddressDb adb;
    Criteria criteria = new Criteria();
    criteria.addEqualTo("id", cust_id);
    QueryByCriteria query = new QueryByCriteria(AddressDb.class, criteria);
    adb = (AddressDb)broker.getObjectByQuery(query);
    if (adb!=null) return 
    "<a href=\"addressEdit.do?mode=show&id="
    +adb.getId()+"\">"
    + adb.getFirstname()
    +" "+adb.getLastname()+"</a>";
    return null;
  }

  private void deleteNote(HttpServletRequest request, PersistenceBroker broker, String id)
  {
    Criteria criteria = new Criteria();
    criteria.addEqualTo("id", id);
    QueryByCriteria query = new QueryByCriteria(NotesDb.class, criteria);
    
    broker.beginTransaction();
    broker.deleteByQuery(query);
    broker.commitTransaction();
    broker.clearCache();
  }

  private void insertNote(NotesForm nf, PersistenceBroker broker)
  {
    NotesDb ndb = new NotesDb();
    ndb.setCust_id(nf.getCust_id());
    ndb.setNote(nf.getNote());
    ndb.setNote_date(nf.getNote_date());
    ndb.setSummary(nf.getSummary());
    ndb.setTrack(nf.getTrack());
    ndb.setPriority(nf.getPriority());
    ndb.setOrder_id(nf.getOrder_id());
    
    ndb.setWebsite(nf.getWebsite());
    if (ndb.getWebsite()!=null)
    {  
      if (!"".equals(ndb.getWebsite())) ndb.setBookmark(1); else ndb.setBookmark(0);
    } else ndb.setBookmark(0);
    
    broker.beginTransaction();
    broker.store(ndb, ObjectModification.INSERT);
    broker.commitTransaction();
    
  }
  
  private void updateNote(NotesForm nf, PersistenceBroker broker)
  {
    NotesDb ndb = new NotesDb();
    ndb.setCust_id(nf.getCust_id());
    ndb.setId(nf.getId());
    ndb.setNote(nf.getNote());
    ndb.setNote_date(nf.getNote_date());
    ndb.setSummary(nf.getSummary());
    ndb.setTrack(nf.getTrack());
    ndb.setPriority(nf.getPriority());
    ndb.setOrder_id(nf.getOrder_id());
    ndb.setWebsite(nf.getWebsite());
    if (ndb.getWebsite()!=null)
    {  
      if (!"".equals(ndb.getWebsite())) ndb.setBookmark(1); else ndb.setBookmark(0);
    } else ndb.setBookmark(0);
    
    broker.beginTransaction();
    broker.store(ndb, ObjectModification.UPDATE);
    broker.commitTransaction();
    broker.clearCache();
  }

  private NotesForm getNoteForm(HttpServletRequest request, PersistenceBroker broker)
  {
    NotesForm nf = new NotesForm();
    nf.setMode("insert");
    String track = request.getParameter("track");
    String order_id = request.getParameter("order_id");
    String cust_id = request.getParameter("cust_id");
    
    if (track==null) track = "0";
    long tlong=0;
    try {tlong=Long.parseLong(track); } catch(Exception e) {}
    nf.setTrack(tlong);
    
    if (order_id==null) order_id = "0";
    tlong=0;
    try {tlong=Long.parseLong(order_id); } catch(Exception e) {}
    nf.setOrder_id(tlong);
    
    if (cust_id==null) cust_id = "0";
    tlong=0;
    try {tlong=Long.parseLong(track); } catch(Exception e) {}
    nf.setCust_id(tlong);
    nf.setNote_date(new java.sql.Date(new java.util.Date().getTime()));
    
    
    return nf;
   
  }
  
  private NotesForm getNotesForm(HttpServletRequest request, PersistenceBroker broker, String id)
  {
    NotesDb ndb;
    Criteria criteria = new Criteria();
    criteria.addEqualTo("id", id);
    QueryByCriteria query = new QueryByCriteria(NotesDb.class, criteria);
    ndb = (NotesDb)broker.getObjectByQuery(query);
    NotesForm nf = new NotesForm();
    if (ndb!=null)
    {
      nf.setCust_id(ndb.getCust_id());
      nf.setTrack(ndb.getTrack());
      nf.setId(ndb.getId());
      nf.setNote(ndb.getNote());
      nf.setSummary(ndb.getSummary());
      nf.setWebsite(ndb.getWebsite());
      nf.setOrder_id(ndb.getOrder_id());
      nf.setNote_date(ndb.getNote_date());
    }
   
    nf.setMode("update");
    return nf;
  }

  private void getNotesList(HttpServletRequest request, PersistenceBroker broker, String track, String filter)
  {
    Criteria criteria = new Criteria();
    NotesSelectForm nsf = new NotesSelectForm();
    nsf.setTrack("0");
    nsf.setFilter(filter);
    if (!"0".equals(track))
    {
      criteria.addEqualTo("track", track);
      nsf.setTrack(track);
    }
    
    if ("bookmarks".equals(filter)) criteria.addEqualTo("bookmark", 1);
      
    QueryByCriteria query = new QueryByCriteria(NotesDb.class, criteria);
    query.addOrderByDescending("priority");
   
    query.addOrderByDescending("note_date");
    //query.setEndAtIndex(4);
    Collection notesList =broker.getCollectionByQuery(query);
    request.setAttribute("NotesList", notesList);
    request.setAttribute("NotesSelectForm", nsf);
    request.setAttribute("CurrentMenuItem", "notes");   
  }
  
  private Collection getImageList(String id, PersistenceBroker broker)
  {
    Criteria criteria = new Criteria();
    criteria.addEqualTo("note_id", id);
    QueryByCriteria query = new QueryByCriteria(ImageRefDb.class, criteria);
    Collection images =(Collection)broker.getCollectionByQuery(query);
    
    return images;
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
