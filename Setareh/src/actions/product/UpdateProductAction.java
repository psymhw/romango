package actions.product;

import data.Product;
import actions.base.BaseAction;

public class UpdateProductAction extends BaseAction
{
  private static final long serialVersionUID = -2606944698916932343L;
  
  private Product product;
  
  public String execute() 
  {
    System.out.println("Product: "+product.getItem());
    return redirect("ProductAction.action?mode=list");
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
