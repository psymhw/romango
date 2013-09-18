package tangodj2.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
 
public class TreeViewSample2 extends Application 
{
  private final Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/images/small_flags.png")));
  private final Image depIcon = new Image(getClass().getResourceAsStream("/resources/images/small_flags.png"));
  List<Employee> employees = Arrays.<Employee>asList(
            new Employee("Ethan Williams", "Sales Department"),
            new Employee("Emma Jones", "Sales Department"),
            new Employee("Michael Brown", "Sales Department"),
            new Employee("Anna Black", "Sales Department"),
            new Employee("Rodger York", "Sales Department"),
            new Employee("Susan Collins", "Sales Department"),
            new Employee("Mike Graham", "IT Support"),
            new Employee("Judy Mayer", "IT Support"),
            new Employee("Gregory Smith", "IT Support"),
            new Employee("Jacob Smith", "Accounts Department"),
            new Employee("Isabella Johnson", "Accounts Department"));
    
    
    TextTreeItem rootNode;
 
    public static void main(String[] args) 
    {
      Application.launch(args);
    }
 
    @Override
    public void start(Stage stage) 
    {
    	Text nodeText = new Text("MyCompany Human Resources");
    	nodeText.setFont(Font.font("Serif", 20));
    	rootNode = new TextTreeItem(nodeText, rootIcon);
      rootNode.setExpanded(true);
        
      for (Employee employee : employees) 
      {
        TextTreeItem empLeaf = new TextTreeItem(new Text(employee.getName()));
        boolean found = false;
        
        ObservableList<TreeItem<Text>> ol = rootNode.getChildren();
        Iterator<TreeItem<Text>> it = ol.iterator();
        TextTreeItem deptNode;
        while(it.hasNext())
        {
        	deptNode=(TextTreeItem)it.next();
        
        //for (TextTreeItem depNode : (ObservableList<TreeItem<Text>>)rootNode.getChildren()) 
       // {
        	//String text = depNode.getValue().toString();
            
        	//TreeItem<Text> temp = depNode;
        	//System.out.println("temp: "+temp.getValue());
        	
        	       	
        	//String s = temp.getValue().getText().toString();
        	//System.out.println("text: "+text.getText());
        	//
        	//Text test = new Text("TESTTEXT");
        	//System.out.println("test text: "+test.getText());
        	
        	if (deptNode.getName().equals(employee.getDepartment()))
           {
             deptNode.getChildren().add(empLeaf);
             found = true;
             break;
           }
        }
        
        if (!found) 
        {
          TextTreeItem depNode = new TextTreeItem(new Text(employee.getDepartment()), new ImageView(depIcon));
          rootNode.getChildren().add(depNode);
          depNode.getChildren().add(empLeaf);
        }
      }
 
      stage.setTitle("Tree View Sample");
      VBox box = new VBox();
      final Scene scene = new Scene(box, 400, 300);
      scene.setFill(Color.LIGHTGRAY);
 
      TreeView<Text> treeView = new TreeView<Text>(rootNode);
      treeView.setEditable(false);
      treeView.setCellFactory(new Callback<TreeView<Text>,TreeCell<Text>>()
      {
         @Override
         public TreeCell<Text> call(TreeView<Text> p) 
         {
           return new TextFieldTreeCellImpl();
         }
      });
 
      box.getChildren().add(treeView);
      stage.setScene(scene);
      stage.show();
    }
 
    private final class TextFieldTreeCellImpl extends TreeCell<Text> 
    {
      private ContextMenu addMenu = new ContextMenu();
 
      public TextFieldTreeCellImpl() 
      {
        MenuItem addMenuItem = new MenuItem("Add Employee");
        MenuItem addMenuItem2 = new MenuItem("Move up");
        MenuItem addMenuItem3 = new MenuItem("Move down");
        MenuItem addMenuItem4 = new MenuItem("Delete");
        MenuItem addMenuItem5 = new MenuItem("Fungus");
        addMenu.getItems().addAll(addMenuItem, addMenuItem2,addMenuItem3,addMenuItem4,addMenuItem5);
        addMenuItem.setOnAction(new EventHandler() 
        {
          public void handle(Event t) 
          {
            TextTreeItem newEmployee = new TextTreeItem(new Text("New Employee"));
            getTreeItem().getChildren().add(newEmployee);
          }
        });
        
        addMenuItem2.setOnAction(new EventHandler() 
        {
          public void handle(Event t) 
          {
            System.out.println("Move Up");
          }
        });
        
        addMenuItem3.setOnAction(new EventHandler() 
        {
          public void handle(Event t) 
          {
        	  System.out.println("Move down");
          }
        });
        
        addMenuItem4.setOnAction(new EventHandler() 
        {
          public void handle(Event t) 
          {
        	  System.out.println("delete");
          }
        });
        
        addMenuItem5.setOnAction(new EventHandler() 
        {
          public void handle(Event t) 
          {
        	  System.out.println("fungus");
          }
        });
        
      }
 
      /*
        @Override
      public void startEdit() 
      {
        super.startEdit();
 
        if (textField == null) { createTextField(); }
        setText(null);
        setGraphic(textField);
        textField.selectAll();
      }
 
      @Override
      public void cancelEdit() 
      {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(getTreeItem().getGraphic());
      }
      */
      public void updateItem(Text item, boolean empty) 
      {
        super.updateItem(item, empty);
 
        if (empty) 
        {
          setText(null);
          setGraphic(null);
        } 
        else 
        {
          
            setText(getString());
            setFont(Font.font("Serif", 20));
            setGraphic(getTreeItem().getGraphic());
            if (!getTreeItem().isLeaf()&&getTreeItem().getParent()!= null)
            {
              setContextMenu(addMenu);
            }
          
        }
      }
 
      /*
      @Override
      public void updateItem(String item, boolean empty) 
      {
        super.updateItem(item, empty);
 
        if (empty) 
        {
          setText(null);
          setGraphic(null);
        } 
        else 
        {
          if (isEditing()) 
          {
            if (textField != null) 
            {
              textField.setText(getString());
            }
            setText(null);
            setGraphic(textField);
          } 
          else 
          {
            setText(getString());
            setGraphic(getTreeItem().getGraphic());
            if (!getTreeItem().isLeaf()&&getTreeItem().getParent()!= null)
            {
              setContextMenu(addMenu);
            }
          }
        }
      }
        
      private void createTextField() 
      {
        textField = new TextField(getString());
        textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
 
                @Override
        public void handle(KeyEvent t) 
        {
          if (t.getCode() == KeyCode.ENTER) 
          {
            commitEdit(textField.getText());
          } 
          else if (t.getCode() == KeyCode.ESCAPE) 
          {
            cancelEdit();
          }
        }
      });  
    }
 */
    private String getString() 
    {
     return getItem() == null ? "" : getItem().getText();
    }
  }
 
  public static class Employee 
  {
    private final SimpleStringProperty name;
    private final SimpleStringProperty department;
 
    private Employee(String name, String department) 
    {
      this.name = new SimpleStringProperty(name);
      this.department = new SimpleStringProperty(department);
    }
 
    public String getName() 
    {
      return name.get();
    }
 
    public void setName(String fName) 
    {
      name.set(fName);
    }
 
    public String getDepartment() 
    {
            return department.get();
        }
 
        public void setDepartment(String fName) {
            department.set(fName);
        }
    }
}