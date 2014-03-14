package data;

import javax.persistence.*;

@Entity
@Table(name="product")
public class Product
{
  @Id @GeneratedValue
  Long id;
  
  public String item;
  public String product_name;
  
  
  public String cas;
  public String mdl;
  public double mwt;
  public String mol_formula;
  public double qty;
  public String storage_prod;
  public String appearance;
  public String unit_size;
  public double unit_price;
  public String cost_to_make;
  public double cost_invt;
  public String pdt_line;
  public String analytical;
  public String application;
  public String refs;
  public String sort_name;
  public double pkgd;
  public double unpkgd;
  
  public Long getId()
  {
    return id;
  }
  public void setId(Long id)
  {
    this.id = id;
  }
  public String getItem()
  {
    return item;
  }
  public void setItem(String item)
  {
    this.item = item;
  }
  public String getProduct_name()
  {
    return product_name;
  }
  
  
  public void setProduct_name(String product_name)
  {
    this.product_name = product_name;
  }
  
  
  public String getCas()
  {
    return cas;
  }
  public void setCas(String cas)
  {
    this.cas = cas;
  }
  public String getMdl()
  {
    return mdl;
  }
  public void setMdl(String mdl)
  {
    this.mdl = mdl;
  }
 
  
  public double getQty()
  {
    return qty;
  }
  public void setQty(double qty)
  {
    this.qty = qty;
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
  
  public String getAppearance()
  {
    return appearance;
  }
  public void setAppearance(String appearance)
  {
    this.appearance = appearance;
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
  public String getMol_formula()
  {
    return mol_formula;
  }
  public void setMol_formula(String mol_formula)
  {
    this.mol_formula = mol_formula;
  }
  public String getStorage_prod()
  {
    return storage_prod;
  }
  public void setStorage_prod(String storage_prod)
  {
    this.storage_prod = storage_prod;
  }
  public double getCost_invt()
  {
    return cost_invt;
  }
  public void setCost_invt(double cost_invt)
  {
    this.cost_invt = cost_invt;
  }
  public double getMwt()
  {
    return mwt;
  }
  public void setMwt(double mwt)
  {
    this.mwt = mwt;
  }
  
  public  String getFormatFormula()
  {
    if (mol_formula.equals(""))  //Check for empty string.
      return mol_formula;
    if (!Character.isLetterOrDigit(mol_formula.charAt(0)))//For those stupid approximate weights stuck in mol_formulas!
      return mol_formula;
    StringBuffer formBuf = new StringBuffer();
    for (int i=0; i<mol_formula.length(); i++)
    {
      if (Character.isDigit(mol_formula.charAt(i)))
      formBuf.append("<sub>"+mol_formula.charAt(i)+"</sub>");
      else
      formBuf.append(mol_formula.charAt(i));
    }
   return formBuf.toString();
  }
  public double getPkgd()
  {
    return pkgd;
  }
  public void setPkgd(double pkgd)
  {
    this.pkgd = pkgd;
  }
  public double getUnpkgd()
  {
    return unpkgd;
  }
  public void setUnpkgd(double unpkgd)
  {
    this.unpkgd = unpkgd;
  }

}
