package actions.login;

import com.opensymphony.xwork2.ActionSupport;

public class LoginSuccess  extends ActionSupport
{
  public String execute()
  {
    //System.out.println("login success");
    return "success";
  }
}
