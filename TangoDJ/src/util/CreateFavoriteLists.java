package util;

import tangodj2.Db;

public class CreateFavoriteLists
{
  
  String[] orchestra=
    {
      "Biagi", 
      "Calo", 
      "Canaro",
      "D''Agostino",
      "D''Arienzo",
      "DeAngeles",
      "Demare",
      "DiSarli",
      "Donato",
      "Fresedo",
      "Garcia",
      "Laurenz",
      "Lomuto",
      "OTV",
      "Pugliese",
      "QP",
      "Rodriguez",
      "Tanturi",
      "Troilo"
    };
  
  public CreateFavoriteLists()
  {
    try
    {
      Db.databaseConnect(true);
      Db.execute("delete from lists");
      for(int i=0; i<orchestra.length; i++)
      {
        Db.insertListHeader(orchestra[i]+"-Derrick");
        Db.insertListHeader(orchestra[i]+"-Instrumental");
        Db.insertListHeader(orchestra[i]+"-Milonga");
        Db.insertListHeader(orchestra[i]+"-Vals");
        Db.insertListHeader(orchestra[i]+"-Vocals");
      }
    
      Db.databaseDisconnect();
    } catch (Exception e) { e.printStackTrace(); }
  }
  
  public static void main(String args[])
  {
    new CreateFavoriteLists();
  }

}
