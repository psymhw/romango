package links;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.util.ObjectModification;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.LinkCatsDb;
import db.LinksDb;

public class LinkCatsAction extends Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{ 
	 
	  ActionForward list = mapping.findForward("list");
	  String mode = request.getParameter("mode");
	  if (mode==null) mode="list";
	  String id = request.getParameter("id");
	  LinkCatsDb lcdb = (LinkCatsDb)form;
	  
	  if (lcdb==null) lcdb = new LinkCatsDb();
	  if (lcdb.getMode()==null) lcdb.setMode("");
	  //System.out.println("LinkCatsAction: mode: "+mode);
	  if ("delete".equals(mode))
	  {
		 deleteLinkCategory(id);
		 lcdb = new LinkCatsDb();
			lcdb.setMode("insert"); 
			request.setAttribute("LinkCatsDb", lcdb);
			Collection links = getLinkCats();
			request.setAttribute("Links", links);
			return list;
	  }
	  
	  if ("insert".equals(lcdb.getMode()))
	  {
		insertLink(lcdb); 
		lcdb = new LinkCatsDb();
		lcdb.setMode("insert"); 
		request.setAttribute("LinkCatsDb", lcdb);
		Collection links = getLinkCats();
		request.setAttribute("Links", links);
		return list;
	  }
	  
	  if ("list".equals(mode))
	  {
		lcdb = new LinkCatsDb();
		lcdb.setMode("insert"); 
		request.setAttribute("LinkCatsDb", lcdb);
		Collection links = getLinkCats();
		request.setAttribute("Links", links);
		return list;
	  }
	  
	  return list;
	}

	private void deleteLinkCategory(String id) {
		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		  Criteria criteria = new Criteria();
		  criteria.addEqualTo("id", id);
		  QueryByCriteria query = new QueryByCriteria(LinkCatsDb.class, criteria);
		  broker.beginTransaction();
		 broker.deleteByQuery(query);  
		 broker.commitTransaction();
		 broker.clearCache();
		  broker.close();
		 // System.out.println("LinkCatsAction: deleting link cat");
	}

	private Collection getLinkCats() 
	{
	  PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	  Criteria criteria = new Criteria();
	  QueryByCriteria query = new QueryByCriteria(LinkCatsDb.class, criteria);
	  query.addOrderByAscending("seq");
	  Collection links =(Collection)broker.getCollectionByQuery(query);  
	  
	  broker.close();
	  //System.out.println("LinkCatsAction: getting link cats");
	  return links;
	}

	private void insertLink(LinkCatsDb lcdb) 
	{
	  PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	  broker.beginTransaction();
	  broker.store(lcdb, ObjectModification.INSERT);
	  broker.commitTransaction();
	  broker.clearCache();
	  broker.close();
	  //System.out.println("LinkCatsAction: category inserted: "+lcdb.getCategory());
	}
}
