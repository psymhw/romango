package actions.login;



import java.util.Map;

import actions.base.BaseAction;

import com.googlecode.sslplugin.annotation.Secured;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import data.User;

@Secured
public class LoginAction extends BaseAction
{
  private String mode;
  private String userName;
  private String userPassword;
  
  public String execute() 
  {
    System.out.println("authenticating "+userName);
    Map session = ActionContext.getContext().getSession();
    session.put("logged-in", userName);
   
    return "success";
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName(String userName)
  {
    this.userName = userName;
  }

  public String getUserPassword()
  {
    return userPassword;
  }

  public void setUserPassword(String userPassword)
  {
    this.userPassword = userPassword;
  }

  public void validate()
  {
    User user = services.getUserByName(userName);
    if (user==null) addFieldError("userName", "User Not Found");
    else if (!user.getPassword().equals(userPassword)) addFieldError("userPassword", "Incorrect Password");
  }
}