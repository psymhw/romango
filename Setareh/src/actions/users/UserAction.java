package actions.users;

import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;

import data.User;
import actions.base.BaseAction;

public class UserAction extends BaseAction
{
  private String mode="list";
  private int id;
  List<User> users;
  User user;
  String userName;
  String userPassword;
  
  public List<User> getUsers() { return users; }
  
  public String execute() 
  {
   // Map session = ActionContext.getContext().getSession();
   // String uname = (String)session.get("logged-in");
   // System.out.println("found = " + uname  );
  //  if (uname==null) return "notLoggedIn";
    
    switch(mode)
    {
      case "list":
        users = services.getUsers();
        return "userList";
      case "edit":
        user=services.getUserById(id);
        return "userEdit";
      case "update":
        services.updateUser(user);
        return redirect("UserAction.action?mode=list");
      case "new":
        return "newUser";
      case "insert":
        services.insertUser(userName, userPassword);
        return redirect("UserAction.action?mode=list");
      case "delete":
        services.deleteUser(id);
        return redirect("UserAction.action?mode=list");
      default:
        return "unsupportedForward";
    }
    
  }
  
  
  public String getMode()
  {
    return mode;
  }
  public void setMode(String mode)
  {
    this.mode = mode;
  }
  public int getId()
  {
    return id;
  }
  public void setId(int id)
  {
    this.id = id;
  }

  public User getUser()
  {
    return user;
  }

  public void setUser(User user)
  {
    this.user = user;
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
  
}
