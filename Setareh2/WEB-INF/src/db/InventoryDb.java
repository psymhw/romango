package db;

public class InventoryDb
{
  public int id;
  public String item_no;
  public String product_name;
  public String cas_no;
  public String mdl_no;
  public double mol_wt;
  public String formula;
  public double qty;
  public String storage;
  public String appearance;
  public String unit_size;
  public double unit_price;
  public String cost_to_make;
  public double invt_cost;
  public String pdt_line;
  public String analytical;
  public String application;
  public String refs;
  public String sort_name;
  
  public int getId()
  {
    return id;
  }
  public void setId(int id)
  {
    this.id = id;
  }
  public String getItem_no()
  {
    return item_no;
  }
  public void setItem_no(String item_no)
  {
    this.item_no = item_no;
  }
  public String getProduct_name()
  {
    return product_name;
  }
  public void setProduct_name(String product_name)
  {
    this.product_name = product_name;
  }
  public String getCas_no()
  {
    return cas_no;
  }
  public void setCas_no(String cas_no)
  {
    this.cas_no = cas_no;
  }
  public String getMdl_no()
  {
    return mdl_no;
  }
  public void setMdl_no(String mdl_no)
  {
    this.mdl_no = mdl_no;
  }
  public double getMol_wt()
  {
    return mol_wt;
  }
  public void setMol_wt(double mol_wt)
  {
    this.mol_wt = mol_wt;
  }
  public String getFormula()
  {
    return formula;
  }
  public void setFormula(String formula)
  {
    this.formula = formula;
  }
  public double getQty()
  {
    return qty;
  }
  public void setQty(double qty)
  {
    this.qty = qty;
  }
  public String getStorage()
  {
    return storage;
  }
  public void setStorage(String storage)
  {
    this.storage = storage;
  }
  public String getAppearance()
  {
    return appearance;
  }
  public void setAppearance(String appearance)
  {
    this.appearance = appearance;
  }
  public String getUnit_size()
  {
    return unit_size;
  }
  public void setUnit_size(String unit_size)
  {
    this.unit_size = unit_size;
  }
  public double getUnit_price()
  {
    return unit_price;
  }
  public void setUnit_price(double unit_price)
  {
    this.unit_price = unit_price;
  }
  public String getCost_to_make()
  {
    return cost_to_make;
  }
  public void setCost_to_make(String cost_to_make)
  {
    this.cost_to_make = cost_to_make;
  }
  public double getInvt_cost()
  {
    return invt_cost;
  }
  public void setInvt_cost(double invt_cost)
  {
    this.invt_cost = invt_cost;
  }
  public String getPdt_line()
  {
    return pdt_line;
  }
  public void setPdt_line(String pdt_line)
  {
    this.pdt_line = pdt_line;
  }
  public String getAnalytical()
  {
    return analytical;
  }
  public void setAnalytical(String analytical)
  {
    this.analytical = analytical;
  }
  public String getApplication()
  {
    return application;
  }
  public void setApplication(String application)
  {
    this.application = application;
  }
  public String getRefs()
  {
    return refs;
  }
  public void setRefs(String refs)
  {
    this.refs = refs;
  }
  public String getSort_name()
  {
    return sort_name;
  }
  public void setSort_name(String sort_name)
  {
    this.sort_name = sort_name;
  }
  
  public  String formatFormula()
  {
    if (formula.equals(""))  //Check for empty string.
      return formula;
    if (!Character.isLetterOrDigit(formula.charAt(0)))//For those stupid approximate weights stuck in formulas!
      return formula;
    StringBuffer formBuf = new StringBuffer();
    for (int i=0; i<formula.length(); i++)
    {
      if (Character.isDigit(formula.charAt(i)))
      formBuf.append("<sub>"+formula.charAt(i)+"</sub>");
      else
      formBuf.append(formula.charAt(i));
    }
   return formBuf.toString();
  }
  
  public String stock()
  {
    return qty+" x "+unit_size;
  }
}
