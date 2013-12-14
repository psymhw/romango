package tangodj2;

import tangodj2.infoWindow.InfoWindow2;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdminTab extends Tab
{

  public AdminTab()
  {
    VBox vbox = new VBox();
    vbox.getChildren().add(new Label("SQL Command: "));
    final TextField command = new TextField();
    command.setMinWidth(400);
    vbox.getChildren().add(command);
    Button button = new Button("Execute");
    button.setOnAction(new EventHandler<ActionEvent>() 
    {
      public void handle(ActionEvent actionEvent) 
      {
        Db.execute(command.getText());  
      }
   });
    vbox.getChildren().add(button);
    setContent(vbox);
  }

}
