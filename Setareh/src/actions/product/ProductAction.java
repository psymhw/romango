package actions.product;


import java.util.List;

import data.Product;
import actions.base.BaseAction;

public class ProductAction  extends BaseAction
{
  private static final long serialVersionUID = 1L;
  private String mode="home";
  private String searchStr;
  private int item=0;
  public String getSearchStr() {
  return searchStr;
}


public void setSearchStr(String searchStr) {
	this.searchStr = searchStr;
}


private Product product;
private List<Product> productList;

  public List<Product> getProductList() {
	return productList;
}


public void setProductList(List<Product> productList) {
	this.productList = productList;
}


public String execute() 
  {
	try {item = Integer.parseInt(searchStr);} catch (NumberFormatException e) {}
	
	  
    switch(mode)
    {
      case "show":
    	if (item>0)  
    	{
          product = services.getProduct(item);
          System.out.println("Item: "+product.getProduct_name());
          return "productShow";
    	}
    	else
    	{
    	  productList = services.getProductList(searchStr);
    	  return "productList";
    	}
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


 


  public Product getProduct()
  {
    return product;
  }


  public void setProduct(Product product)
  {
    this.product = product;
  }

}
