package actions.users;

import actions.base.BaseAction;

public class Edit extends BaseAction {

  public String execute() {
    if (isPostBack)
    {
      services.getUserById(id);
      return redirect("Listing.action");
    }
    return "success";
  }
  
  int id;
  public void setId(int value) {id = value;}
  public int getId() {return id;  }

  boolean isPostBack;
  public void setIsPostBack(boolean value) {isPostBack = value;}
  
    public String getName() { return services.getUserById(id).getName(); }
}