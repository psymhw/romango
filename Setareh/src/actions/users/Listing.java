package actions.users;

import actions.base.BaseAction;
import data.*;
import java.util.List;

public class Listing extends BaseAction 
{
  public String execute() 
  {
    users = services.getUsers();
    return "userlist";
  }
  
  List<User> users;
  public List<User> getUsers() { return users; }
}