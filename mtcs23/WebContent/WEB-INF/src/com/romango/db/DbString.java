package com.romango.db;

public class DbString
{
  private int maxLength=0;
  private String string;
  
  public DbString(String inStr, int maxLen)
  {
    string=inStr;
    maxLength=maxLen;
  }
  
  public String get()
  {
    if (string.length()<=maxLength) return string;
    else return string.substring(0,maxLength);
  }
  
  public String getSqlStr()
  {
    if (get()==null) return "";
    return get().replace("'","''");
  }

}
