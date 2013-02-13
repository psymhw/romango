package links;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;
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
import org.apache.struts.util.LabelValueBean;


import db.LinkCatsDb;
import db.LinksDb;

public class LinksAction extends Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{ 
	 
	  ActionForward edit = mapping.findForward("edit");
	  ActionForward show = mapping.findForward("show");
	  ActionForward list = mapping.findForward("list");
	  ActionForward forward = null;	    
	  
	  String mode=(String)request.getParameter("mode");
	  String id = request.getParameter("id");
	  if (mode==null) mode="list";
	  PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	 
	  boolean admin = true;
	  
	  //getLinksPages(broker);
	  if ("show".equals(mode))
	  {
		  LinksDb ldb = getSingleLinks(broker, id);
		  request.setAttribute("LinksDb", ldb);
		  forward=show;
	  }
	  
	  
	  if ("new".equals(mode))
	  {
		LinksDb ldb = new LinksDb();
		ldb.setMode("insert");
		request.setAttribute("LinksDb", ldb);
		 Collection linkCats = getLinkCats(broker);
		  
		  ArrayList linkCatsList = getLinkCatsList(linkCats);
		  request.setAttribute("LinkCats", linkCatsList);
		forward = edit;
	  }
	  
	  if ("edit".equals(mode))
	  {
		LinksDb ldb = getSingleLinks(broker, id);
		
		ldb.setMode("update");
		request.setAttribute("LinksDb", ldb);
		 Collection linkCats = getLinkCats(broker);
		  
		  ArrayList linkCatsList = getLinkCatsList(linkCats);
		  request.setAttribute("LinkCats", linkCatsList);
		forward = edit;
	  }
	  
	  if ("insert".equals(mode))
	  {
		 LinksDb ldb = (LinksDb)form;
		 ldb.setPost_date(new java.sql.Date(new java.util.Date().getTime()));
		 broker.beginTransaction();
		 broker.store(ldb, ObjectModification.INSERT);
		 broker.commitTransaction();
		 broker.clearCache();
		 
		 long lastId = getLastLinksId(broker);
		 
		  ldb = getSingleLinks(broker, ""+lastId);
		  request.setAttribute("LinksDb", ldb);
		  
		//  setCatagories(broker, request);
		  
		  forward=list;
	  }
	  
	  if ("update".equals(mode))
	  {
		 LinksDb ldb = (LinksDb)form;
		// System.out.println("MusicAction - active: "+vdb.getActive());
		 broker.beginTransaction();
		 broker.store(ldb, ObjectModification.UPDATE);
		 
		 broker.commitTransaction();
		 broker.clearCache();
		 request.setAttribute("LinksDb", ldb);
		 
		// setCatagories(broker, request);
		 
		 forward = list;
	  }
	  
	  broker.close();
	  return forward;
	}

	private LinksDb getSingleLinks(PersistenceBroker broker, String id) 
	{
	  Criteria criteria = new Criteria();
	  criteria.addEqualTo("id", id);
	  QueryByCriteria query = new QueryByCriteria(LinksDb.class, criteria);
	  LinksDb vdb=(LinksDb)broker.getObjectByQuery(query);  
	  return vdb;
	}
	
	

	
	
	long getLastLinksId(PersistenceBroker broker)
	{
		long currval=0;
		try
		{
			Connection db = broker.serviceConnectionManager().getConnection();
			Statement stmt = db.createStatement();    
		  ResultSet rs = stmt.executeQuery("select max(id) as currval from links"); 
			if (rs != null) { if (rs.next()) { currval=rs.getLong("currval"); } }
		   rs.close();
		   stmt.close();
		} catch (Exception e) {	e.printStackTrace();}
		
		return currval;
	}
	
	  private Collection getLinkCats(PersistenceBroker broker) 
		{
		  Criteria criteria = new Criteria();
		  QueryByCriteria query = new QueryByCriteria(LinkCatsDb.class, criteria);
		  query.addOrderByAscending("seq");
		  Collection links =(Collection)broker.getCollectionByQuery(query);  
		  return links;
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
	
}
