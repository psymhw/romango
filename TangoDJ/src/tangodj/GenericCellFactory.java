package tangodj;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class GenericCellFactory implements Callback<TableColumn,TableCell> {
    
    
    EventHandler click;
    
    
    public GenericCellFactory(EventHandler click) {
        
        this.click = click;
    }

    public TableCell call(TableColumn p) {
    	TableCell cell=cell = new TableCell() 
         {
            @Override 
            protected void updateItem(Object item, boolean empty) {
                 // calling super here is very important - don't skip this!
                 super.updateItem(item, empty);
                 if(item != null) {
                     setText(item.toString());
                 }
            }
       };
      
	   cell.setOnMouseClicked(click);     
       return cell;
    }
}