package tangodj2;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.beans.value.ChangeListener;

public class Tanda 
{
   String style;
   String artist;
   VBox vbox = new VBox();
   
   private final  ObservableList<TandaTrack> tandaTracksData = FXCollections.observableArrayList();
   private TableView<TandaTrack> tandaTracksTable = new TableView<TandaTrack>();
   
   public Tanda(String artist, String style)
   {
	 this.style=style;
	 this.artist=artist;
	 
	 //vbox.setPadding(new Insets(10, 0, 0, 10));
	 final Label label = new Label(artist+" - "+style);
	 label.setFont(new Font("Arial", 20));
	 
	 vbox.getChildren().addAll(label, tandaTracksTable);
	 
	 TableColumn titleCol = new TableColumn("Title");
     titleCol.setMinWidth(100);
     titleCol.setPrefWidth(150);
     titleCol.setCellValueFactory(
         new PropertyValueFactory<TandaTrack, String>("title"));
    // titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
     titleCol.setOnEditCommit(
         new EventHandler<CellEditEvent<TandaTrack, String>>() {
             @Override
             public void handle(CellEditEvent<TandaTrack, String> t) {
                 ((TandaTrack) t.getTableView().getItems().get(
                         t.getTablePosition().getRow())
                         ).setTitle(t.getNewValue());
             }
         }
     );
     tandaTracksTable.getColumns().add(titleCol);
     tandaTracksTable.setItems(tandaTracksData);
     tandaTracksTable.setPrefHeight(100);
     
     
     tandaTracksTable.widthProperty().addListener(new ChangeListener<Number>() {
         public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
             // Get the table header
             Pane header = (Pane)tandaTracksTable.lookup("TableHeaderRow");
             if(header!=null && header.isVisible()) {
               header.setMaxHeight(0);
               header.setMinHeight(0);
               header.setPrefHeight(0);
               header.setVisible(false);
               header.setManaged(false);
             }
         }
     });
   }
   
   public void addTrack(TandaTrack tandaTrack)
   {
	 tandaTracksData.add(tandaTrack);
   }
      
   public VBox getTanda()
   {
	 return vbox;
   }
   
   
   
   
}
