package mileage;

import java.util.Collection;

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

import db.MileageDb;
import db.PasswordDb;

public class MileageAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) throws Exception 
      { 
        ActionForward fwd = null;
        ActionForward mileagelist = mapping.findForward("mileagelist");  
        ActionForward mileagelistRedirect = mapping.findForward("mileagelistRedirect");  
        ActionForward mileageedit = mapping.findForward("mileageedit");   
        ActionForward noAccess = mapping.findForward("noAccess");   
        HttpSession session = request.getSession();
        String user = (String)session.getAttribute("user");
        if (user==null) return noAccess;
        

        //ActionRedirect ar = new ActionRedirect(mapping.findForward("mileagelist"));
        // System.out.println("MileageAction ...");
        String track = request.getParameter("track");
        if (track==null) track="0";
        PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
        if (!getUser(broker).equals(user)) { broker.close(); return noAccess; }
        String mode=request.getParameter("mode");
        String id=request.getParameter("id");
        
        
        
        if (mode==null) mode="list";
        if ("list".equals(mode))
        {
          getMileageList(request, broker, track);
          fwd=mileagelist;
        }
        if ("new".equals(mode))
        {
          getMileageForm(request, broker);
          fwd=mileageedit;
        }
        
        if ("insert".equals(mode))
        {
          MileageForm mf = (MileageForm)form;
          insertMileage(mf, broker);
          fwd=mileagelistRedirect;
          getMileageList(request, broker, track);
          fwd=mileagelistRedirect;
        }
        
        if ("edit".equals(mode))
        {
          getMileageForm(request, broker, id);
          //updateMileage(mf, broker);
         // fwd=mileagelistRedirect;
          //getMileageList(request, broker, track);
          fwd=mileageedit;
        }
        
        if ("update".equals(mode))
        {
          MileageForm mf = (MileageForm)form;
          updateMileage(mf, broker);
          fwd=mileagelistRedirect;
          getMileageList(request, broker, track);
          fwd=mileagelistRedirect;
        }
        
        if ("delete".equals(mode))
        {
          deleteMileage(request, broker, id);
          fwd=mileagelistRedirect;
          getMileageList(request, broker, track);
          fwd=mileagelistRedirect;
        }
        
        broker.close();
        return fwd;
      }
      
      private void deleteMileage(HttpServletRequest request, PersistenceBroker broker, String id)
      {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id", id);
        QueryByCriteria query = new QueryByCriteria(MileageDb.class, criteria);
        
        broker.beginTransaction();
        broker.deleteByQuery(query);
        broker.commitTransaction();
        broker.clearCache();
      }

      private void insertMileage(MileageForm mf, PersistenceBroker broker)
      {
        MileageDb mdb = new MileageDb();
        mdb.setDescription(mf.getDescription());
        mdb.setMileage_date(mf.getMileage_date());
        mdb.setStart_mile(mf.getStart_mile());
       // mdb.setEnd_mile(mf.getEnd_mile());
        mdb.setEnd_mile(mf.getStart_mile()+mf.getMiles());
        
        mdb.setMiles(mf.getMiles());
        
        mdb.setTrack(mf.getTrack());
        
        broker.beginTransaction();
        broker.store(mdb, ObjectModification.INSERT);
        broker.commitTransaction();
        
      }
      
      private void updateMileage(MileageForm mf, PersistenceBroker broker)
      {
        MileageDb mdb = new MileageDb();
        mdb.setId(mf.getId());
        mdb.setDescription(mf.getDescription());
        mdb.setMileage_date(mf.getMileage_date());
        mdb.setTrack(mf.getTrack());
        mdb.setStart_mile(mf.getStart_mile());
       // mdb.setEnd_mile(mf.getEnd_mile());
        mdb.setEnd_mile(mf.getStart_mile()+mf.getMiles());
        
        mdb.setMiles(mf.getMiles());
    
        broker.beginTransaction();
        broker.store(mdb, ObjectModification.UPDATE);
        broker.commitTransaction();
        broker.clearCache();
      }

      private void getMileageForm(HttpServletRequest request, PersistenceBroker broker)
      {
        MileageForm mf = new MileageForm();
        mf.setMode("insert");
        request.setAttribute("MileageForm", mf);
      }
      
      private void getMileageForm(HttpServletRequest request, PersistenceBroker broker, String id)
      {
        MileageDb mdb;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id", id);
        QueryByCriteria query = new QueryByCriteria(MileageDb.class, criteria);
        mdb = (MileageDb)broker.getObjectByQuery(query);
        MileageForm mf = new MileageForm();
        if (mdb!=null)
        {
          mf.setTrack(mdb.getTrack());
          mf.setId(mdb.getId());
          mf.setMileage_date(mdb.getMileage_date());
          mf.setDescription(mdb.getDescription());
          mf.setStart_mile(mdb.getStart_mile());
          mf.setEnd_mile(mdb.getEnd_mile());
          mf.setMiles(mdb.getMiles());
        }
       
        mf.setMode("update");
        request.setAttribute("MileageForm", mf);
      }

      private void getMileageList(HttpServletRequest request, PersistenceBroker broker, String track)
      {
        Criteria criteria = new Criteria();
        MileageSelectForm msf = new MileageSelectForm();
        msf.setTrack("0");
        if (!"0".equals(track))
        {
          criteria.addEqualTo("track", track);
          msf.setTrack(track);
        }
        QueryByCriteria query = new QueryByCriteria(MileageDb.class, criteria);
        
        query.addOrderByDescending("mileage_date");
        //query.setEndAtIndex(4);
        Collection mileageList =broker.getCollectionByQuery(query);
        request.setAttribute("MileageList", mileageList);
        request.setAttribute("MileageSelectForm", msf);
        request.setAttribute("CurrentMenuItem", "mileage");
           
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
