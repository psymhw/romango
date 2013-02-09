package image;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

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
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.upload.FormFile;

import db.PasswordDb;

public class ImageUploadAction extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
    //ActionForward test = mapping.findForward("test");   
	    ActionForward noAccess = mapping.findForward("noAccess");   
	    HttpSession session = request.getSession();
	    String user = (String)session.getAttribute("user");
	    if (user==null) return noAccess;
	    
    ImageUploadForm iuf = (ImageUploadForm)form;
    //byte[] imgBytes=iuf.getFilename().getInputStream();
    PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
    if (!getUser(broker).equals(user)) { broker.close(); return noAccess; }
    
    Connection db = null;
    db = broker.serviceConnectionManager().getConnection();
    FormFile ff = iuf.getFileForm();
    
    String sql = "insert into image_ref (filename, file_size, cust_id, note_id, description, image) values("
         +"'"+ff.getFileName()
         +"',"+ff.getFileSize()
         +", "+iuf.getCust_id()
         +", "+iuf.getNote_id()
         +",'"+iuf.getDescription()
         +"',?)";
    PreparedStatement stmt = db.prepareStatement(sql);
    byte[] imgBytes = ff.getFileData();
    ByteArrayInputStream inStream = new ByteArrayInputStream(imgBytes);
    
    
    //InputStream inStream = ff.getInputStream();
    stmt.setBinaryStream(1,inStream,inStream.available());
    inStream.close();
    //System.out.println("ImageUploadAction - filename: "+iuf.getFileForm().getFileName());
    //System.out.println("ImageUploadAction - file size: "+iuf.getFileForm().getFileSize());
//  execute statement
    stmt.executeUpdate();
    
    broker.close();
    
    if ("cust".equals(iuf.getType()))
    {  
      ActionRedirect custRedirect = new ActionRedirect(mapping.findForward("cust_success")); 
      custRedirect.addParameter("mode", "show");
      custRedirect.addParameter("id", iuf.getCust_id());
      return custRedirect;
    }
    
    ActionRedirect noteRedirect = new ActionRedirect(mapping.findForward("note_success")); 
    noteRedirect.addParameter("mode", "edit");
    noteRedirect.addParameter("id", iuf.getNote_id());
    return noteRedirect;
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
