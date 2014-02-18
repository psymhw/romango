package actions.product;

import data.Product;
import actions.base.BaseAction;

public class UpdateProductAction extends BaseAction
{
  private static final long serialVersionUID = -2606944698916932343L;
  
  private Product product;
  private String item;
  
  public String execute() 
  {
    item=product.getItem();
    services.updateProduct(product);
    //System.out.println("Product: "+product.getItem());
    return "show";
  }

 
  public Product getProduct()
  {
    return product;
  }

  public void setProduct(Product product)
  {
    this.product = product;
  }


  public String getItem()
  {
    return item;
  }


  public void setItem(String item)
  {
    this.item = item;
  }
}
