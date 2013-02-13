package media;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

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


import db.MediaDb;

public class MediaAction extends Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{ 
	 
	  ActionForward edit = mapping.findForward("edit");
	  ActionForward show = mapping.findForward("show");
	  ActionForward forward = null;	    
	  
	  String mode=(String)request.getParameter("mode");
	  String id = request.getParameter("id");
	  if (mode==null) mode="list";
	  PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	 
	  boolean admin = true;
	  
	  //getMediaPages(broker);
	  if ("show".equals(mode))
	  {
		  MediaDb mdb = getSingleMedia(broker, id);
		  request.setAttribute("MediaDb", mdb);
		  forward=show;
	  }
	  
	  
	  if ("new".equals(mode))
	  {
		MediaDb mdb = new MediaDb();
		mdb.setMode("insert");
		request.setAttribute("MediaDb", mdb);
		forward = edit;
	  }
	  
	  if ("edit".equals(mode))
	  {
		MediaDb mdb = getSingleMedia(broker, id);
		
		mdb.setMode("update");
		request.setAttribute("MediaDb", mdb);
		forward = edit;
	  }
	  
	  if ("insert".equals(mode))
	  {
		 MediaDb mdb = (MediaDb)form;
		 
		 broker.beginTransaction();
		 broker.store(mdb, ObjectModification.INSERT);
		 broker.commitTransaction();
		 broker.clearCache();
		 long lastId = getLastMediaId(broker);
		 
		  mdb = getSingleMedia(broker, ""+lastId);
		  request.setAttribute("MediaDb", mdb);
		  
		  setCatagories(broker, request);
		  
		  forward=show;
	  }
	  
	  if ("update".equals(mode))
	  {
		 MediaDb mdb = (MediaDb)form;
		// System.out.println("MusicAction - active: "+vdb.getActive());
		 broker.beginTransaction();
		 broker.store(mdb, ObjectModification.UPDATE);
		 
		 broker.commitTransaction();
		 broker.clearCache();
		 request.setAttribute("MediaDb", mdb);
		 
		 setCatagories(broker, request);
		 
		 forward = show;
	  }
	  
	  broker.close();
	  return forward;
	}

	private MediaDb getSingleMedia(PersistenceBroker broker, String id) 
	{
	  Criteria criteria = new Criteria();
	  criteria.addEqualTo("id", id);
	  QueryByCriteria query = new QueryByCriteria(MediaDb.class, criteria);
	  MediaDb vdb=(MediaDb)broker.getObjectByQuery(query);  
	  return vdb;
	}
	
	

	
	
	long getLastMediaId(PersistenceBroker broker)
	{
		long currval=0;
		try
		{
			Connection db = broker.serviceConnectionManager().getConnection();
			Statement stmt = db.createStatement();    
		  ResultSet rs = stmt.executeQuery("select max(id) as currval from media"); 
			if (rs != null) { if (rs.next()) { currval=rs.getLong("currval"); } }
		   rs.close();
		   stmt.close();
		} catch (Exception e) {	e.printStackTrace();}
		
		return currval;
	}
	
	void setCatagories(PersistenceBroker broker, HttpServletRequest request)
	{
		ServletContext context = request.getSession().getServletContext();
		String category=null;
		ArrayList <LabelValueBean>categories = new ArrayList<LabelValueBean>();
		categories.add(new LabelValueBean("-- All --","-- All --"));
		try
		{
			Connection db = broker.serviceConnectionManager().getConnection();
			Statement stmt = db.createStatement();    
		  ResultSet rs = stmt.executeQuery("select distinct(category) from media"); 
		  if (rs != null) { while (rs.next()) { category=rs.getString("category"); categories.add(new LabelValueBean(category, category)); } }
		   rs.close();
		   stmt.close();
		} catch (Exception e) {	e.printStackTrace();}
		context.setAttribute("Categories", categories);
		
		
	}
}
