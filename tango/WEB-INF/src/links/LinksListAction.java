package links;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import media.CategoryForm;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import db.LinkCatsDb;
import db.LinksDb;

public class LinksListAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
	ActionForward list = mapping.findForward("list"); 
	 PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		
    LinksCategoryForm cf = (LinksCategoryForm)form;
	if (cf==null) 
	{	  
	  cf = new LinksCategoryForm();
	  String category = request.getParameter("category");
	  if (category==null) category = "-- All --";
	  cf.setSelectedValue(category);
	}
	String category = request.getParameter("category");
	  if (category!=null)  cf.setSelectedValue(category);
	  request.setAttribute("LinksCategoryForm", cf);
	  Collection linkCats = getLinkCats(broker);
	  
	  ArrayList linkCatsList = getLinkCatsList(linkCats);
	  request.setAttribute("LinkCats", linkCatsList);
	  
	  Iterator it = linkCats.iterator();
	  LinkCatsDb lcdb=null;
	  ArrayList links = new ArrayList();
	  
	  if (!"-- All --".equals(cf.getSelectedValue())) 
	  {
		  links = getLinks(broker, cf.getSelectedValue(), links); 
	  }
	  else
	  {
	    while(it.hasNext())
	    {
	  	  lcdb= (LinkCatsDb)it.next();
		  links = getLinks(broker, lcdb.getCategory(), links); 
	    }
	  }
	  
	  request.setAttribute("Links", links);
	  broker.close();
		
	return list;
  }
  
  private ArrayList getLinkCatsList(Collection linkCats) 
  {
	ArrayList linkCatsList = new ArrayList();
	linkCatsList.add(new LabelValueBean( "-- All --", "-- All --"));
	Iterator it = linkCats.iterator();
	LinkCatsDb lcdb=null;
	while(it.hasNext())
	{
		lcdb= (LinkCatsDb)it.next();
		linkCatsList.add(new LabelValueBean( lcdb.getCategory(), lcdb.getCategory()));
	}
	return linkCatsList;
}

private ArrayList getLinks(PersistenceBroker broker, String category, ArrayList links) 
	{
		Criteria criteria = new Criteria();
		if (category!=null)
		if (!"-- All --".equals(category)) 
		{	
			//System.out.println("LinksListAction - category: "+category);
			criteria.addEqualTo("category", category);
		}
		QueryByCriteria query = new QueryByCriteria(LinksDb.class, criteria);
		query.addOrderByAscending("category");
		query.addOrderByAscending("seq");
		
		Collection sublinks=broker.getCollectionByQuery(query);
		Iterator it = sublinks.iterator();
		LinksDb ldb=null;
		while(it.hasNext())
		{
		  ldb=(LinksDb)it.next();
		  links.add(ldb);
		}
		
		
		return links;
	}
  
    private Collection getLinkCats(PersistenceBroker broker) 
	{
	  Criteria criteria = new Criteria();
	  QueryByCriteria query = new QueryByCriteria(LinkCatsDb.class, criteria);
	  query.addOrderByAscending("seq");
	  Collection links =(Collection)broker.getCollectionByQuery(query);  
	  return links;
	}
	
}
