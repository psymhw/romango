package tangodj;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

public class TaskCellFactory implements Callback<TableColumn, TableCell> 
{

@Override
public TableCell call(TableColumn p) {

   TableCell cell = new TableCell() 
		   {

        @Override
        public void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);
            if(item != null) {
                setText(item.toString());}
         //  this.setStyle("-fx-background-color: linear-gradient(to bottom right, #a1c517, #009045, #0082b6, #151f77, #db004f);");
           this.setStyle("-fx-background-color: skyblue;");
         //  this.setStyle("palegreen");
         //   getStyleClass().add("-fx-control-inner-background: skyblue;");
          //  setStyle("lowPriority");
            
        }

        
       

        

    };


    return cell;
} }