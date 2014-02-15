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
  
/*
 * validate always wants to go back to result "input" if there is a validation problem
   This may be a reason to split up action classes. So I could specify a different "input"
   to return to on say update vs new record.
 */
  public void validate()
  {
	  if ("show".equals(mode))
	  {
		  if (getSearchStr()==null)
			  addFieldError("searchStr", "Enter Item Number or part of product name");
		  else if (getSearchStr().length()==0)
			  addFieldError("searchStr", "Enter Item Number or part of product name");
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
