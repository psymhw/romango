package actions.login;

import java.util.Map;

import actions.base.BaseAction;

import com.googlecode.sslplugin.annotation.Secured;
import com.opensymphony.xwork2.ActionContext;

@Secured
public class LoginForm extends BaseAction
{
  public String execute() 
  {
    Map session = ActionContext.getContext().getSession();
    String userName= (String)(session.get("logged-in"));
    if (userName!=null)
    {  
      System.out.println("already logged in: "+userName);
      return "home";
    }
    return "input";
  }
  
}