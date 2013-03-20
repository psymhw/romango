package home;

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
import db.ImageRefDb;
import db.NotesDb;
import db.PasswordDb;

public class HomeAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
  HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
    ActionForward home = mapping.findForward("home");   
    
    
    
    ActionForward noAccess = mapping.findForward("noAccess");   
    
    getRecentAddressList(request);
    
    
    HttpSession session = request.getSession();
    String user = (String)session.getAttribute("user");
    if (user==null) { return noAccess; }
    
    
   // PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    
    if (!getUser().equals(user)) {  return noAccess; }
    
    getRecentAddressList(request);
    getRecentNotesList(request);
    getImageList(request);
    
    request.setAttribute("CurrentMenuItem", "home");
    return home;
  }
  
  private void getRecentAddressList(HttpServletRequest request)
  {
	PersistenceBroker broker=null;
	try
	{
  	  broker= PersistenceBrokerFactory.defaultPersistenceBroker();
      Criteria criteria = new Criteria();
      QueryByCriteria query = new QueryByCriteria(AddressDb.class, criteria);
      query.addOrderByDescending("last_touch");
      query.setEndAtIndex(4);
      Collection addressList =broker.getCollectionByQuery(query);
      request.setAttribute("AddressList", addressList);
	}
	finally 
	{
	  if (broker!=null) broker.close();
	}
  }
  
  private void getRecentNotesList(HttpServletRequest request)
  {
	PersistenceBroker broker=null;
	try
	{
	  broker= PersistenceBrokerFactory.defaultPersistenceBroker();
      Criteria criteria = new Criteria();
      QueryByCriteria query = new QueryByCriteria(NotesDb.class, criteria);
      query.addOrderByDescending("note_date");
      query.setEndAtIndex(4);
      Collection notesList =broker.getCollectionByQuery(query);
      request.setAttribute("NotesList", notesList);
    }
	finally 
	{
	  if (broker!=null) broker.close();
	}
   }
  
  private void getImageList(HttpServletRequest request)
  {
	PersistenceBroker broker=null;
    try
    {
      broker= PersistenceBrokerFactory.defaultPersistenceBroker();
      Criteria criteria = new Criteria();
      QueryByCriteria query = new QueryByCriteria(ImageRefDb.class, criteria);
      query.addOrderByDescending("id");
      query.setEndAtIndex(8);
      Collection images =(Collection)broker.getCollectionByQuery(query);
      request.setAttribute("ImageList", images);
      request.setAttribute("ImageLayout", "home");
	}
	finally 
	{
	  if (broker!=null) broker.close();
	}
  }
  
  String getUser()
  {
	PersistenceBroker broker=null;
	try
	{
	  broker= PersistenceBrokerFactory.defaultPersistenceBroker();		
	  Criteria criteria = new Criteria();
      criteria.addEqualTo("id", 1);
      QueryByCriteria query = new QueryByCriteria(PasswordDb.class, criteria);
      PasswordDb pdb = (PasswordDb)broker.getObjectByQuery(query);
      return pdb.getUser();
	}
	finally 
	{
	  if (broker!=null) broker.close();
	}
  }
}
