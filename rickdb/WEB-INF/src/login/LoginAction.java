package login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import db.PasswordDb;


/**
 *
 * @author eswar@vaannila.com
 */
public class LoginAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";
    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LoginForm loginForm = (LoginForm) form;
        
        PersistenceBroker broker= PersistenceBrokerFactory.defaultPersistenceBroker();
       // broker.serviceConnectionManager();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id", 1);
        QueryByCriteria query = new QueryByCriteria(PasswordDb.class, criteria);
        PasswordDb pdb = (PasswordDb)broker.getObjectByQuery(query);
        
        broker.close();
        
        if (loginForm.getUserName().equals(pdb.getUser())&&(loginForm.getPassword().equals(pdb.getPassword()))) 
        {
        	HttpSession session = request.getSession();
        	session.setAttribute("user", pdb.getUser());
            return mapping.findForward(SUCCESS);
        } else {
            return mapping.findForward(FAILURE);
        }
    }
}

