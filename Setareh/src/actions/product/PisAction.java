package actions.product;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import data.Product;

import actions.base.BaseAction;

public class PisAction extends BaseAction
{
  HttpServletRequest request = ServletActionContext.getRequest();
  private Product product;
  private int item;
  
  public String execute() 
  {
    product = services.getProduct(item);
    System.out.println("getting: "+item);
    request.setAttribute("Product", product);
    return "success";
  }

  public int getItem()
  {
    return item;
  }

  public void setItem(int item)
  {
    this.item = item;
  }

  
}
