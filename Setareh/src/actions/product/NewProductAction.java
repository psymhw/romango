package actions.product;

import data.Product;
import actions.base.BaseAction;



public class NewProductAction extends BaseAction
{
  private Product product;
  public String execute() 
  {
    
    return "showNewForm";
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
