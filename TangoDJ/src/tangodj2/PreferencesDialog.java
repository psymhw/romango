package tangodj2;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PreferencesDialog extends Stage
{
  TextField tangoDir = new TextField(TangoDJ2.prefs.tangoFolder);
  TextField cleanupDir = new TextField(TangoDJ2.prefs.cleanupFolder);
  public PreferencesDialog()
  {
	final int col[] = {0,1,2,3,4,5,6,7,8,9,10};
    final int row[] = {0,1,2,3,4,5,6,7,8,9,10};
    this.initModality(Modality.APPLICATION_MODAL); 

    Button okButton = new Button("OK");
	GridPane gridPane = new GridPane();
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    gridPane.setVgap(2);
    gridPane.setHgap(5);
    gridPane.add(new Label("Tango Tracks Folder: "), col[0], row[0]);
    gridPane.add(new Label("Cleanup Tracks Folder: "), col[0], row[1]);
   
    gridPane.add(tangoDir, col[1], row[0]);
    gridPane.add(cleanupDir, col[1], row[1]);
    gridPane.add(okButton, col[0], row[2]);
    
    okButton.setOnAction(new EventHandler<ActionEvent>()
    {
      public void handle(ActionEvent arg0) 
      {
    	 TangoDJ2.prefs.tangoFolder=tangoDir.getText(); 
    	 TangoDJ2.prefs.cleanupFolder=cleanupDir.getText(); 
    	 Db.updatePreferences(TangoDJ2.prefs);
        close();	
      }});
    
    Scene myDialogScene = new Scene(gridPane, 300, 200);
    setScene(myDialogScene);
    show();
  } 
}
