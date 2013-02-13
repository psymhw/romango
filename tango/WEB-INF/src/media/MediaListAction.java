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

public class MediaListAction extends Action
{
	private final static int PAGE_SIZE =6;
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{ 
	  ActionForward list = mapping.findForward("list");  
	 
	    
	  
	  String mode=(String)request.getParameter("mode");
	  String page=(String)request.getParameter("page");
	  //System.out.println("MediaListAction - page: "+page);
	  if (page==null) page="1";
	  int iPage=1;
	  try { iPage=Integer.parseInt(page); } catch(Exception e) {}
	  //System.out.println("MediaListAction - iPage: "+iPage);
	  String id = request.getParameter("id");
	  if (mode==null) mode="list";
	  PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	  CategoryForm cf = (CategoryForm)form;
	  if (cf==null) 
	  {	  
		  cf = new CategoryForm();
		  String category = request.getParameter("category");
		  if (category==null) category = "-- All --";
		  cf.setSelectedValue(category);
	  }
	  Collection media = getMedia(broker, cf.getSelectedValue(), iPage, PAGE_SIZE); 
	  int categorySize = (int)getCategorySize(broker, cf.getSelectedValue());
	  String pageWidget = getPageWidget(iPage, categorySize, PAGE_SIZE, cf.getSelectedValue());
	  //System.out.println("MediaListAction - size: "+categorySize);
	  request.setAttribute("Media", media);
	  request.setAttribute("CategoryForm", cf);
	  request.setAttribute("PageWidget", pageWidget);	 
	  
	  
	  return list;
	}
	private Collection getMedia(PersistenceBroker broker, String category, int page, int pageSize) 
	{
		Criteria criteria = new Criteria();
		if (category!=null)
		if (!"-- All --".equals(category)) 
		{	
			//System.out.println("MediaListAction - category: "+category);
			criteria.addEqualTo("category", category);
		}
		QueryByCriteria query = new QueryByCriteria(MediaDb.class, criteria);
		query.addOrderByDescending("id");
		int startIndex = (int)((page-1)*pageSize);
		int endIndex=startIndex+pageSize;
        //System.out.println("MediaListAction - startIndex: "+startIndex);
        //System.out.println("MediaListAction - endIndex: "+endIndex);
        
		
		query.setStartAtIndex(startIndex);
		query.setEndAtIndex(endIndex);
		Collection media=broker.getCollectionByQuery(query);
		
		
		return media;
	}
	
	private long getCategorySize(PersistenceBroker broker, String category) 
	{
		Criteria criteria = new Criteria();
		if (category!=null)
		if (!"-- All --".equals(category)) 
		{	
			//System.out.println("MediaListAction - category: "+category);
			criteria.addEqualTo("category", category);
		}
		QueryByCriteria query = new QueryByCriteria(MediaDb.class, criteria);
		return broker.getCount(query);
	}
	
	private String getPageWidget(int pageRequest, int collectionSize, int pageSize, String category)
	{
      StringBuffer widget = new StringBuffer();	
      if (collectionSize<=pageSize) return "1";
      int numberOfPages=(int)(collectionSize/pageSize);
      long leftOver = (collectionSize % pageSize);
	  if (category==null) category="-- All --";
	  if (leftOver>0) numberOfPages++;
	  
	  for(int i=1; i<(numberOfPages+1); i++)
	  {
		if (i==pageRequest) widget.append(" "+i);
		else widget.append(" <a href=\"media.do?page="+i+"&category="+category+"\">"+i+"</a>");
	  }
      
      return widget.toString();
	}
	
	
	
	
}
