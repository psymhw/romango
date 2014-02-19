package actions.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import actions.base.BaseAction;
import data.Product;

public class ShowProductAction extends BaseAction
{
  private String item;
  private Product product;
 // private String searchStr;
  
  public String execute() 
  {
   // try {item = Integer.parseInt(product.getItem());} catch (NumberFormatException e) {}
    
 //   System.out.println("ShowProductAction searchStr: "+searchStr);
    
  //  product = services.getProduct(item);
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpSession session = request.getSession();
    item=(String)session.getAttribute("item");
   System.out.println("Show Product action Item: "+item);
    return "productShow";
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
