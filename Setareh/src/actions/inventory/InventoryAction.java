package actions.inventory;

import data.Inventory;
import actions.base.BaseAction;

public class InventoryAction  extends BaseAction
{
  private String mode="home";
  private int item;
  private Inventory product;

  public String execute() 
  {
    switch(mode)
    {
      case "show":
        product = services.getProduct(item);
        System.out.println("Item: "+product.getProduct_name());
        return "inventoryShow";
      case "home":
        //user=services.getUserById(id);
        return "inventoryHome";
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
    System.out.println("mode: "+mode);
  }


  public int getItem()
  {
    return item;
    
  }


  public void setItem(int item)
  {
    this.item = item;
    System.out.println("item: "+item);
  }


  public Inventory getProduct()
  {
    return product;
  }


  public void setProduct(Inventory product)
  {
    this.product = product;
  }

}
