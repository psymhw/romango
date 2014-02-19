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
  
  public String execute() 
  {
   //System.out.println("Show Product action Item: "+item);
   product=services.getProduct(Integer.parseInt(item));
    return "productShow";
  }

 
  
  public String getItem() {
	return item;
}



public void setItem(String item) {
	this.item = item;
}


 // private String searchStr;
  
  

  public Product getProduct()
  {
    return product;
  }

  public void setProduct(Product product)
  {
    this.product = product;
  }



  
}
