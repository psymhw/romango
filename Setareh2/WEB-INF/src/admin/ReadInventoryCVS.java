package admin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import db.InventoryDb;

import au.com.bytecode.opencsv.CSVReader;


public class ReadInventoryCVS
{
  ArrayList<InventoryDb> records = new ArrayList<InventoryDb>();
  ArrayList<String> sqlLines = new ArrayList<String>();
  int maxProdLen=0;
  int maxPdtLen=0;
  int maxAnalytical=0;
  int maxApp=0;
  int maxRef=0;
  
  public static void main(String[] args)
  {
    new ReadInventoryCVS();

  }
  
  public ReadInventoryCVS()
  {
    CSVReader reader=null;
    double tdub=0;
    try
    {
      reader = new CSVReader(new FileReader("e:\\inventory.csv"));
    } catch (FileNotFoundException e1)
    {
      e1.printStackTrace();
    }
    String [] nextLine;
    try{
    while ((nextLine = reader.readNext()) != null) 
    {
      InventoryDb idb = new InventoryDb();
      for(int i=0; i<nextLine.length; i++)
      {
        int j=0;
        
        idb.id=-1;
        idb.item_no                  =nextLine[j++];
        idb.product_name             =nextLine[j++];
        idb.cas_no                   =nextLine[j++];
        idb.mdl_no                   =nextLine[j++];
        tdub=0;
        try { tdub=Double.parseDouble(nextLine[j++]); } catch (Exception e) {}
        idb.mol_wt=tdub;
        
        idb.formula                  =nextLine[j++];
        tdub=0;
        try { tdub=Double.parseDouble(nextLine[j++]); } catch (Exception e) {}
        idb.qty   =tdub;
        idb.storage                  =nextLine[j++];
        idb.appearance               =nextLine[j++];
        idb.unit_size                =nextLine[j++];
        
        tdub=0;
        try { tdub=Double.parseDouble(nextLine[j++]); } catch (Exception e) {}
        idb.unit_price=tdub;
        idb.cost_to_make                =nextLine[j++];
        
        tdub=0;
        try { tdub=Double.parseDouble(nextLine[j++]); } catch (Exception e) {}
        idb.invt_cost=tdub;
        idb.pdt_line =nextLine[j++];
        idb.analytical =nextLine[j++];
        idb.application =nextLine[j++];
        idb.refs =nextLine[j++];
        
        
      }
    //System.out.println("len: "+idb.product_name.length());
      if (maxProdLen<idb.product_name.length()) maxProdLen=idb.product_name.length();
      if (maxPdtLen<idb.pdt_line.length()) maxPdtLen=idb.pdt_line.length();
      if (maxAnalytical<idb.analytical.length()) maxAnalytical=idb.analytical.length();
      if (maxApp<idb.application.length())  maxApp=idb.application.length();
      if (maxRef<idb.refs.length())  maxRef=idb.refs.length();
      records.add(idb);
    }
    } catch (Exception e) { e.printStackTrace(); }
    
    System.out.println("Max Prod: "+maxProdLen);
    System.out.println("Max Pdt: "+maxPdtLen);
    System.out.println("Max Analy: "+maxAnalytical);
    System.out.println("Max App: "+maxApp);
    System.out.println("Max Ref: "+maxRef);
    System.out.println("Total Lines: "+records.size());
    
    Iterator<InventoryDb> it = records.iterator();
    
    InventoryDb idbx;
    String sql;
    String utf8String="";
    while (it.hasNext())
    {
      idbx=it.next();
      sql="insert into inventory (item, product_name, cas, mdl, mwt, mol_formula, qty, storage_prod, "
          +"appearance, unit_size, unit_price, cost_to_make, cost_invt, pdt_line, analytical, "
          +"application, refs) values('"
          + idbx.item_no+"', '"
          + sqlReadyString(idbx.product_name)+"', '"
          + idbx.cas_no+"', '"
          + idbx.mdl_no+"', "
          + idbx.mol_wt+", '"
          + idbx.formula+"', "
          + idbx.qty+", '"
          + idbx.storage+"', '"
          + idbx.appearance+"', '"
          + idbx.unit_size+"', "
          + idbx.unit_price+", '"
          + idbx.cost_to_make+"', "
          + idbx.invt_cost+", '"
          + sqlReadyString(idbx.pdt_line)+"', '"
          + sqlReadyString(idbx.analytical)+"', '"
          + sqlReadyString(idbx.application)+"', '"
          + sqlReadyString(idbx.refs)+"');";
      
         // try
         // {
         //   utf8String = new String(sql.getBytes("UTF-8"), "UTF-8");
         // } catch (UnsupportedEncodingException e)
         // {
            // TODO Auto-generated catch block
         //   e.printStackTrace();
        //  }
          sqlLines.add(sql);
    }

    Iterator<String> itx = sqlLines.iterator();
    BufferedWriter out;
    FileWriter fstream=null;
    try
    {
      File outFile = new File("inventory.sql");
      if (outFile.exists()) outFile.delete();
      fstream = new FileWriter("inventory.sql");
      out = new BufferedWriter(fstream);
      while(itx.hasNext())
      {
        out.write(itx.next());
        out.newLine();
      }
      out.close();
    } catch (IOException e) {e.printStackTrace();  }
    
  }
  
  public void setMax(int maxVal, int currentVal)
  {
    if (currentVal>maxVal) maxVal=currentVal;
  }
  
  public static String sqlReadyString(String inStr)
  {
     if (inStr==null) return "";
     String returnStr = inStr.replace("'","''");
     char tChar=0;
     returnStr = removeChar(returnStr, tChar);
     
     return returnStr;
  }
  
  public static String sqlReadyString(String inStr, int maxLength)
  {
    String retStr = sqlReadyString(inStr);
    if (retStr.length()>=maxLength) retStr=retStr.substring(0, maxLength-1);
    return retStr;
  }
  
  public static String removeChar(String s, char c) 
  {
  String r = "";
  for (int i = 0; i < s.length(); i ++) 
  {
    if (s.charAt(i) != c) r += s.charAt(i);
  }
  return r;
  }
  
  
  

}
