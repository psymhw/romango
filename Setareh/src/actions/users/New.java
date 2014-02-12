package actions.users;

import actions.base.BaseAction;

public class New extends BaseAction {
  
  public String execute() {
    if (name!=null && name.length()>0)
    {
      services.createUser(name);
      return redirect("Listing.action");
    }
    return "success";
  }
  
    String name;
    public String getName() {return name;}
    public void setName(String value) {name = value;}
    
    String password;
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
}