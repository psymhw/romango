package tangodj2.test;

import tangodj2.Tanda;
import tangodj2.TandaTrack;
import test.derby.TableViewSample.Person;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
 
public class TandaTableViewTest extends Application 
{
 
    private TableView<Tanda> table = new TableView<Tanda>();
    private final ObservableList<Tanda> data =
            FXCollections.observableArrayList(
            new Tanda("Canaro", "Vals"),
            new Tanda("Di Sarli", "Tango"),
            new Tanda("Biagi", "Tango"),
            new Tanda("D'Arienzo", "Milonga"),
            new Tanda("Fresedo", "Vals"));
    
 
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(450);
        stage.setHeight(550);
 
        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));
 
        table.setEditable(false);
     
 
        TableColumn mainCol = new TableColumn("Tandas");
        mainCol.setMinWidth(100);
        mainCol.setCellValueFactory(
                new PropertyValueFactory<Tanda,VBox>("tanda"));
       
        
        Tanda t = data.get(1);
        t.addTrack(new TandaTrack("Por Una Cabeza"));
        
        table.setItems(data);
        table.getColumns().add(mainCol);
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        stage.setScene(scene);
        stage.show();
    }
 
    
}