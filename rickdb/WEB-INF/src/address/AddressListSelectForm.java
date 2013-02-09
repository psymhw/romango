package address;

import org.apache.struts.action.ActionForm;

public class AddressListSelectForm extends ActionForm
{
   /**
   * 
   */
  private static final long serialVersionUID = -8644967383307136028L;
  String sort="last";
   String letter;
   String category="all";
  public String getCategory()
  {
    return category;
  }
  public void setCategory(String category)
  {
    this.category = category;
  }
  public String getLetter()
  {
    return letter;
  }
  public void setLetter(String letter)
  {
    this.letter = letter;
  }
  public String getSort()
  {
    return sort;
  }
  public void setSort(String sort)
  {
    this.sort = sort;
  }
}
