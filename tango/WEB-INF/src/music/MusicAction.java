package music;

import java.util.ArrayList;
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


import db.ArticleDb;

public class MusicAction extends Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{ 
	  ActionForward list = mapping.findForward("list");  
	  ActionForward edit = mapping.findForward("edit");
	  ActionForward show = mapping.findForward("show");
	  ActionForward forward = null;	    
	  
	  String mode=(String)request.getParameter("mode");
	  String id = request.getParameter("id");
	  if (mode==null) mode="list";
	  PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	 
	  boolean admin = true;
	  
	  if ("list".equals(mode))
	  {
		 Collection articles = getArticles(broker, admin); 
		 request.setAttribute("Articles", articles);
		 forward = list; 
	  }
	  
	  if ("show".equals(mode))
	  {
		  ArticleDb adb = getArticle(broker, id);
		  request.setAttribute("ArticleDb", adb);
		  forward=show;
	  }
	  
	  
	  if ("new".equals(mode))
	  {
		ArticleDb adb = new ArticleDb();
		adb.setMode("insert");
		request.setAttribute("ArticleDb", adb);
		forward = edit;
	  }
	  
	  if ("edit".equals(mode))
	  {
		ArticleDb adb = getArticle(broker, id);
		
		adb.setMode("update");
		request.setAttribute("ArticleDb", adb);
		forward = edit;
	  }
	  
	  if ("insert".equals(mode))
	  {
		 ArticleDb adb = (ArticleDb)form;
		 
		 broker.beginTransaction();
		 broker.store(adb, ObjectModification.INSERT);
		 broker.commitTransaction();
		 forward = list;
	  }
	  
	  if ("update".equals(mode))
	  {
		 ArticleDb adb = (ArticleDb)form;
		// System.out.println("MusicAction - active: "+adb.getActive());
		 broker.beginTransaction();
		 broker.store(adb, ObjectModification.UPDATE);
		 
		 broker.commitTransaction();
		 request.setAttribute("ArticleDb", adb);
		 forward = show;
	  }
	  
	  broker.close();
	  return forward;
	}

	private ArticleDb getArticle(PersistenceBroker broker, String id) 
	{
	  Criteria criteria = new Criteria();
	  criteria.addEqualTo("id", id);
	  QueryByCriteria query = new QueryByCriteria(ArticleDb.class, criteria);
	  ArticleDb adb=(ArticleDb)broker.getObjectByQuery(query);  
	  return adb;
	}

	private Collection getArticles(PersistenceBroker broker, boolean admin) 
	{
		Criteria criteria = new Criteria();
		QueryByCriteria query = new QueryByCriteria(ArticleDb.class, criteria);
		query.addOrderByDescending("article_date");
		Collection articles=broker.getCollectionByQuery(query);
		return articles;
	}
}
