package actions.product;

import data.Product;
import actions.base.BaseAction;

public class EditProductAction extends BaseAction
{
  private static final long serialVersionUID = -7807347115836517568L;
  private String mode ="edit";
  private int item;
  private int id;
  private Product product;
  
  public String execute() 
  {
    //System.out.println("item: "+item);
   // System.out.println("id: "+id);
    product = services.getProductById(id);
   // System.out.println("name: "+product.getProduct_name());
    return "showEditForm";
  }

  public String getMode()
  {
    return mode;
  }

  public void setMode(String mode)
  {
    this.mode = mode;
  }

  public int getItem()
  {
    return item;
  }

  public void setItem(int item)
  {
    this.item = item;
  }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
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
