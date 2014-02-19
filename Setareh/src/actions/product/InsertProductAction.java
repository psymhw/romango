package actions.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import data.Product;
import actions.base.BaseAction;

public class InsertProductAction extends BaseAction
{
  private Product product;
  private String item;
 // HttpServletRequest request = ServletActionContext.getRequest();
 // HttpSession session = request.getSession();
  
 
public String execute()
   {
    
     item=product.getItem();
     services.insertProduct(product);
   //  System.out.println("Insert: "+item);
  //   session.setAttribute("item", item);
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
