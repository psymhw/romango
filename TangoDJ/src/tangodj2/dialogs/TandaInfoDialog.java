package tangodj2.dialogs;

import java.util.Iterator;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tangodj2.ArtistX;
import tangodj2.Db;
import tangodj2.Playlist;
import tangodj2.SharedValues;
import tangodj2.TandaDb;
import tangodj2.PlaylistTree.PlaylistTreeItem;
import tangodj2.PlaylistTree.TandaTreeItem;

public class TandaInfoDialog 
{
  public final static int NEW = 0;
  public final static int EDIT = 1;
  private Playlist playlist;
  private int type;
  private TandaDb tandaDb;
  private long tandaId=0;
  final RadioButton rb1 = new RadioButton("Tango");
	 final RadioButton rb2 = new RadioButton("Vals");
	 final RadioButton rb3 = new RadioButton("Milonga");
	 final RadioButton rb4 = new RadioButton("Alternative");
	 final RadioButton rb5 = new RadioButton("Mixed");
	 final RadioButton rb6 = new RadioButton("Cleanup");
 private TandaTreeItem tandaTreeItem;
  
  
  public TandaInfoDialog(Playlist playlist)
  {
	this.playlist=playlist;
	type=NEW;
	show();
  }
  
  public TandaInfoDialog(Playlist playlist, long tandaId, TandaTreeItem tandaTreeItem)
  {
	this.playlist=playlist;
	this.tandaId=tandaId;
	this.tandaTreeItem=tandaTreeItem;
	try 
	{
	  tandaDb = Db.getTanda(tandaId);
	} catch (Exception e) { e.printStackTrace(); }
	
	type=EDIT;
	show();
  }
  
  private void show()
  {
	final ComboBox comboBox = new ComboBox();
	final Text alist = new Text("A List");
	Text blist =   new Text("B List");
	Text clist =   new Text("C List");
	 
	alist.setFill(Color.BLACK);
	alist.setFont(Font.font("Serif", 18));
	blist.setFill(Color.BLACK);
	blist.setFont(Font.font("Serif", 18));
	clist.setFill(Color.BLACK);
	clist.setFont(Font.font("Serif", 18));
	 
	final TextField artistOverride = new TextField("");
	    
	comboBox.getItems().add(alist);
	comboBox.getItems().addAll(SharedValues.artistsA);
	comboBox.getItems().add(blist);
	comboBox.getItems().addAll(SharedValues.artistsB);
	comboBox.getItems().add(clist);
	comboBox.getItems().addAll(SharedValues.artistsC);
	   
	comboBox.setOnAction(new EventHandler<ActionEvent>() 
	{
	  public void handle(ActionEvent actionEvent) 
	  {
	    //System.out.println("Combobox action");
	    ArtistX ax = (ArtistX)comboBox.getValue();
	    artistOverride.setText(ax.getLeader()); 
	  }});
	
	
	 
	 
	 rb1.setId(""+SharedValues.TANGO);
	 rb2.setId(""+SharedValues.VALS);
	 rb3.setId(""+SharedValues.MILONGA);
	 rb4.setId(""+SharedValues.ALTERNATIVE);
	 rb5.setId(""+SharedValues.MIXED);
	 rb6.setId(""+SharedValues.CLEANUP);
	 
	 final ToggleGroup styleGroup = new ToggleGroup();
	 
	 rb1.setToggleGroup(styleGroup);
	 rb2.setToggleGroup(styleGroup);
	 rb3.setToggleGroup(styleGroup);
	 rb4.setToggleGroup(styleGroup);
	 rb5.setToggleGroup(styleGroup);
	 rb6.setToggleGroup(styleGroup);
	    
	 rb1.setSelected(true);
	
	 final VBox styleBox = new VBox();
	 styleBox.getChildren().addAll(rb1,rb2,rb3,rb4,rb5,rb6);
	 final TextField comment = new TextField();
	     
      final Stage myDialog = new Stage();
      myDialog.initModality(Modality.APPLICATION_MODAL);
      
      Button okButton = new Button("SAVE");
      okButton.setOnAction(new EventHandler<ActionEvent>()
      {
        public void handle(ActionEvent arg0) 
        {
	           //String artist = comboBox.getSelectionModel().getSelectedItem().toString();
	           int styleId = 0;
	           String selectedStr=styleGroup.getSelectedToggle().toString();
	           int i = selectedStr.indexOf("id=");
	           String numStr = selectedStr.substring(i+3, i+4);
	           try 
	           {
	             styleId= Integer.parseInt(numStr);
	           } catch (Exception e) {}
	           if (type==NEW)
	           {
	             playlist.addTanda(artistOverride.getText(), styleId, comment.getText());
	           }
	           else
	           {
	        	 playlist.updateTanda(tandaId, artistOverride.getText(), styleId, comment.getText(),tandaTreeItem);
	           }
	             myDialog.close();
        }});
    
      Text tandaLabel = new Text("Orchestra: ");
      tandaLabel.setFont(Font.font("Serif", 20));
      
      
      GridPane gridPane = new GridPane();
      //gridPane.setGridLinesVisible(true);
      gridPane.setPadding(new Insets(10, 10, 10, 10));
      gridPane.setVgap(2);
      gridPane.setHgap(5);
      gridPane.add(tandaLabel, 0, 0);
      gridPane.add(comboBox, 1, 0);
      gridPane.add(artistOverride, 1, 1);
      //gridPane.add(new Text("Style"), 0, 1);
      gridPane.add(styleBox, 0, 1);
      gridPane.add(new Label("Comment: "), 0, 3);
      gridPane.add(comment, 1, 3);
      gridPane.add(okButton, 1, 4);
      
      Scene myDialogScene = new Scene(gridPane, 300, 200);
      myDialog.setScene(myDialogScene);
      
      Object ax;
      if (type==EDIT)
      {
    	 boolean regularArtist=false;
    	 Iterator it = comboBox.getItems().iterator();
    	 while(it.hasNext())
    	 {
    		ax=it.next();
    		if (ax instanceof ArtistX)
    		if (((ArtistX)ax).getLeader().equals(tandaDb.getArtist())) 
    		{
    			comboBox.setValue(ax);
    			regularArtist=true;
    		}
    	 }
    	 
    	 if (tandaDb.getStyle().equals("Tango")) rb1.setSelected(true);
         else if (tandaDb.getStyle().equals("Vals")) rb2.setSelected(true);
         else if (tandaDb.getStyle().equals("Milonga")) rb3.setSelected(true);
         else if (tandaDb.getStyle().equals("Alternative")) rb4.setSelected(true);
         else if (tandaDb.getStyle().equals("Mixed")) rb5.setSelected(true);
         else if (tandaDb.getStyle().equals("Cleanup")) rb6.setSelected(true);
    	 
    	 if (!regularArtist) artistOverride.setText(tandaDb.getArtist());
    	 comment.setText(tandaDb.getComment());
      }
      
      
      
      
      myDialog.show();
    }

}
