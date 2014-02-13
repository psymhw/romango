package actions.product;


import data.Product;
import actions.base.BaseAction;

public class ProductAction  extends BaseAction
{
  private String mode="home";
  private int item;
  private Product product;

  public String execute() 
  {
    switch(mode)
    {
      case "show":
        product = services.getProduct(item);
        System.out.println("Item: "+product.getProduct_name());
        return "productShow";
      case "home":
        //user=services.getUserById(id);
        return "productHome";
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


  public Product getProduct()
  {
    return product;
  }


  public void setProduct(Product product)
  {
    this.product = product;
  }

}
