package tangodj2.allPlaylistsTree;


import tangodj2.Db;
import tangodj2.PlaylistChoiceTab;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class EditPlaylistDialog extends Stage
{
  String mode;
  int treeType;
  AllPlaylistsBaseItem parentItem;
  
  public EditPlaylistDialog(String mode, int treeType, AllPlaylistsBaseItem parentItem)
  {
	final int col[] = {0,1,2,3,4,5,6,7,8,9,10};
    final int row[] = {0,1,2,3,4,5,6,7,8,9,10};
    this.mode=mode;
    this.parentItem=parentItem;
    this.treeType=treeType;
    
    Scene myDialogScene = new Scene(getEntryBox(), 300, 200);
    setScene(myDialogScene);
    show();
  } 
  
  private VBox getEntryBox()
  {
	VBox vbox = new VBox(5);
	vbox.setPadding(new Insets(10, 10, 10, 10));
	vbox.setSpacing(5);
	//vbox.setMaxWidth(350);
	
	vbox.setStyle("-fx-border-style: solid;"
            + "-fx-border-width: 1;"
            + "-fx-border-color: black");
	
	final Label panelLabel    = new Label("New Playlist");
	final Label nameLabel     = new Label(" Name: ");
	final Label locationLabel = new Label(" Location: ");
	
    panelLabel.setFont(   new Font("Arial", 20));
    nameLabel.setFont(    new Font("Arial", 18));
    locationLabel.setFont(new Font("Arial", 18));
    
    final TextField nameField = new TextField();
    final TextField locationField = new TextField("");
    
    GridPane gridPane = new GridPane();
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    gridPane.setVgap(2);
    gridPane.setHgap(15);
    
    int row=0;
    gridPane.add(nameLabel, 0, row);
    gridPane.add(nameField, 1, row++);
    
  //  gridPane.add(locationLabel, 0, row);
  //  gridPane.add(locationField, 1, row++);
    
    GridPane.setHalignment(nameLabel, HPos.RIGHT);
    GridPane.setHalignment(locationLabel, HPos.RIGHT);
 
    String buttonText="Save";
    if ("new".equals(mode)&&(treeType==PlaylistChoiceTab.FOLDER))
     buttonText="Save New Folder";
    if ("new".equals(mode)&&(treeType==PlaylistChoiceTab.PLAYLIST))
        buttonText="Save New Playlist";
    final Button addButton = new Button(buttonText);
    addButton.setOnAction(new EventHandler<ActionEvent>() 
    {
      public void handle(ActionEvent e) 
      {
    	AllPlaylistsBaseItem apbi = Db.insertPlaylistsItem(nameField.getText(), 
    			locationField.getText(), 
    			parentItem.getId(), 
    			parentItem.getLevel()+1, 
    			parentItem.getChildren().size(), 
    			treeType);
    	parentItem.getChildren().add(apbi);
    	close();
      }
    });
    
    vbox.getChildren().addAll(panelLabel, gridPane, addButton);
   
	return vbox;
  }
  
}
