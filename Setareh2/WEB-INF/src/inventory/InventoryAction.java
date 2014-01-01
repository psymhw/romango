package inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;



import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import db.InventoryDb;



public class InventoryAction extends Action
{
  FileVisitor<Path> fileProcessor = new ProcessFile();
  ArrayList<FileInfo> fileDetails = new ArrayList();
  
  public ActionForward execute(ActionMapping mapping, ActionForm form,
  HttpServletRequest request, HttpServletResponse response) throws Exception 
  { 
    ActionForward list = mapping.findForward("list");  
    ActionForward showProduct = mapping.findForward("showProduct");
    ActionForward unsupportedForward = mapping.findForward("unsupportedForward");
    
    String mode = (String)request.getParameter("mode");
    if (mode==null) mode="list";
    
    if ("list".equals(mode))
    {
      PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
      Criteria criteria = new Criteria();
    //criteria.addEqualTo("item", "7188");
      QueryByCriteria query = new QueryByCriteria(InventoryDb.class, criteria);
      query.addOrderByAscending("item");
      Collection<InventoryDb> items=broker.getCollectionByQuery(query);
      request.setAttribute("InventoryList", items);
      broker.close();
      return list;
    }
    
    if ("showProduct".equals(mode))
    {
      String idStr = (String)request.getParameter("id");
      int id = 0;
      try { id=Integer.parseInt(idStr); } catch (Exception e) {}
      PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
      Criteria criteria = new Criteria();
      criteria.addEqualTo("id", id);
      QueryByCriteria query = new QueryByCriteria(InventoryDb.class, criteria);
      InventoryDb product = (InventoryDb)broker.getObjectByQuery(query);
      request.setAttribute("Product",product);
      broker.close();
      listFiles();
      request.setAttribute("FileDetails",fileDetails);
      return showProduct;
    }
    
    
    return unsupportedForward;
  }
  
  private void listFiles()
  {
   
    try
    {
      Files.walkFileTree(Paths.get("c:\\Setareh\\documents\\7188"), fileProcessor);
    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  void processDirectory()
  {
    FileVisitor<Path> fileProcessor = new ProcessFile();
  }
  
  private  final class ProcessFile extends SimpleFileVisitor<Path> 
  {
    public FileVisitResult visitFile(Path path, BasicFileAttributes aAttrs) throws IOException 
    {
      try {
      return processSingleFile(path);
      } catch (Exception e) { e.printStackTrace(); }
      return null;
    }
  }
        
    public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException 
    {
    System.out.println("Processing directory:" + aDir);
    return FileVisitResult.CONTINUE;
    }
 
    public  FileVisitResult processSingleFile(Path path) throws Exception
    {
      String pathStr = path.toString().trim().toLowerCase();
      String pathStr2="";
      File file = path.toFile();
      pathStr2=path.toString();
      
      
      int slashPos=pathStr2.lastIndexOf('\\');
      String wholeParent=pathStr2.substring(0, slashPos);
      int slashPos2=wholeParent.lastIndexOf('\\');
      
      int dotPos=pathStr2.lastIndexOf('.');
      
      String parentDir="";
      if (slashPos2+1<wholeParent.length())
        parentDir= wholeParent.substring(slashPos2+1);
      String fileName=pathStr2.substring(slashPos+1);
      String extension = pathStr2.substring(dotPos+1);
      
      System.out.println("fileName: "+fileName);
      System.out.println("parent Dir: "+parentDir);
      System.out.println("Ext: "+extension);
      
      System.out.println(") "+wholeParent);
      
      FileInfo fi = new FileInfo();
      fi.category=parentDir;
      fi.docType=extension;
      fi.path=pathStr2;
      fi.fileName=fileName;
      
      fileDetails.add(fi);
      
      return FileVisitResult.CONTINUE;
    }
} 
