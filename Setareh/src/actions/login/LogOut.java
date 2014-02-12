package actions.login;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class LogOut extends ActionSupport
{
  public String execute() 
  {
    Map session = ActionContext.getContext().getSession();
    session.remove("logged-in");
    return "input";
  }
}
