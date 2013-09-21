package tangodj2;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.util.Callback;

import tangodj2.PlaylistTree.BaseTreeItem;
import tangodj2.PlaylistTree.PlaylistTreeItem;
import tangodj2.PlaylistTree.TandaTreeItem;
import tangodj2.PlaylistTree.TrackTreeItem;

public class Playlist 
{
	private PlaylistTreeItem playlistTreeItem;
	private TreeView<String> treeView;
	private TrackTreeItem previouslySelectedTrack=null;
	
	public Playlist()
	{
		setupTreeView();	
	}
	
	public TreeView<String> getTreeView()
	{
	   return treeView;	
	}
	
	public void addTanda(String artist, int styleId)
	{
		 playlistTreeItem.addTanda(artist, styleId);
	}
	
	public int getTandaCount()
	{
		return playlistTreeItem.getTandaCount();
	}
	
	public TandaTreeItem  getTanda(int index)
	{
	  return (TandaTreeItem)playlistTreeItem.getChildren().get(index);
	}
	
  private void setupTreeView()
	{
	  playlistTreeItem =  Db.getPlaylist(SharedValues.currentPlaylist);
	  treeView = new TreeView<String>(playlistTreeItem);
	 	treeView.getSelectionModel().select(playlistTreeItem);
		treeView.setShowRoot(true);
		ArrayList<TandaTreeItem> tandaTreeItems=null;
		try 
		{
		  tandaTreeItems = Db.getTandaTreeItems(SharedValues.currentPlaylist);
		} catch (ClassNotFoundException | SQLException e) { e.printStackTrace();}
			
		 Iterator<TandaTreeItem> it = tandaTreeItems.iterator();
			
		 if (tandaTreeItems.size()>0)
		 {
		   playlistTreeItem.getChildren().addAll(tandaTreeItems);
		 }
			 
		 treeView.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>()
		 {
		   @Override
		   public TreeCell<String> call(TreeView<String> p) 
		   {
			 return new MyCellFactory();
		   }
		  });
			 
		 /* 
		  * Detect tree item selected
		  */
		 ChangeListener<TreeItem<String>> cl = new ChangeListener<TreeItem<String>>() 
		 {
		   public void changed(ObservableValue<? extends TreeItem<String>> observableValue, TreeItem<String> oldItem, TreeItem<String> newItem) 
		   {
			 if (newItem!=null)
			 {
			  	 
			   BaseTreeItem bti = (BaseTreeItem)newItem;
			   //bti.setValue("TOAST");
			   
			   //bti.setSelected(true);
			   //bti.setValue("TOAST");
        // BaseTreeItem bti2 = (BaseTreeItem)treeView.getSelectionModel().getSelectedItem();
        // System.out.println("SELECTION INDEX: "+treeView.getSelectionModel().getSelectedIndex());
        // System.out.println("SELECTION ITEM: "+bti2.getTreeType());
			   
         
         SharedValues.selectedTrack=-1;
			   if ("tanda".equals(bti.getTreeType())) 
			   {
			     SharedValues.selectedTanda=playlistTreeItem.getTandaPosition((TandaTreeItem)bti);
			   }
			   else if ("track".equals(bti.getTreeType())) 
			   {
			     if (previouslySelectedTrack!=null) previouslySelectedTrack.setPlayingImage(false);
			     TrackTreeItem trackTreeItem = (TrackTreeItem)bti;
				   TandaTreeItem parent = (TandaTreeItem)trackTreeItem.getParent();
				   trackTreeItem.setPlayingImage(true);
				   previouslySelectedTrack = trackTreeItem;
				   //String tValue = bti.getValue();
				   //bti.setValue("*"+tValue);
				  // bti.setValue(tValue);
				   SharedValues.selectedTrack=parent.getTrackPosition(bti);

				   int parentPos=-1;
				   if (parent!=null) parentPos=playlistTreeItem.getTandaPosition((TandaTreeItem)parent);
				   SharedValues.selectedTanda=parentPos;
			   }
			   else
			   {
				   SharedValues.selectedTanda=0; 
			   }
			   System.out.println("Tanda/Track: "
			            +SharedValues.selectedTanda+"/"+SharedValues.selectedTrack+" - "
					   + newItem.getValue()); 
			 }
		   }
						
		   public void handle(ActionEvent event) 
		   {
			 TreeItem selectedItem = getSelectedItem();
	  	     if (selectedItem == null) 
			 {
			   System.out.println("Error: You have to select a item in the tree.");
			   return;
			 }
		   }
						
		   private TreeItem<String> getSelectedItem() 
		   {
			 return (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
		   }
		 };
							 
		 treeView.getSelectionModel().selectedItemProperty().addListener(cl);
		 
	   }
			
	   private final class MyCellFactory extends TreeCell<String> 
	   {
	     private ContextMenu tandaContextMenu =  new ContextMenu();
	     private ContextMenu trackContextMenu =  new ContextMenu();

	     public MyCellFactory() 
	     {
	       setupTandaContextMenu(tandaContextMenu);
	       setupTrackContextMenu(trackContextMenu);
	      
	     }
	       
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
	         setText(getString());
	         BaseTreeItem bti = (BaseTreeItem)getTreeItem();
	         if ("playlist".equals(bti.getTreeType())) setFont(Font.font("Serif", 20));
	         else if ("tanda".equals(bti.getTreeType())) 
	         {	   
	           if (playlistTreeItem.getTandaPosition((TandaTreeItem)bti)==0) tandaContextMenu.getItems().get(0).setDisable(true);  // disable move up
	           if (playlistTreeItem.getTandaPosition((TandaTreeItem)bti)==playlistTreeItem.getTandaCount()-1) tandaContextMenu.getItems().get(1).setDisable(true); // disable move down
	           setFont(Font.font("Serif", 16));
	           //Kludge alert
	           tandaContextMenu.setId(""+playlistTreeItem.getTandaPosition((TandaTreeItem)bti));
	           setContextMenu(tandaContextMenu);
	          // setGraphic(getTreeItem().getGraphic());
	         }
	         else if ("track".equals(bti.getTreeType()))
	         {
	           setFont(Font.font("Serif", 16));
	           TrackTreeItem trackTreeItem = (TrackTreeItem)bti;
	         
	           int trackCount=((TandaTreeItem)trackTreeItem.getParent()).getTrackCount();
	           int trackPosition=trackTreeItem.getTrackPosition(trackTreeItem);
	           
	           if (trackPosition==0) trackContextMenu.getItems().get(0).setDisable(true);  // disable move up
	           if (trackPosition==trackCount-1) trackContextMenu.getItems().get(1).setDisable(true); // disable move down
	           //Kludge alert
	           trackContextMenu.setId(trackTreeItem.getTandaAndTrackPosition(trackTreeItem));
	           setContextMenu(trackContextMenu);
	           //System.out.println("track update: "+trackTreeItem.getValue());
	           //trackTreeItem.setSelected(true);
	          // if (trackTreeItem.isSelected()) setGraphic(new ImageView(TrackTreeItem.greenCheckBallImage));
	          // else setGraphic(new ImageView(TrackTreeItem.dimBallImage));
	         }
	         setGraphic(getTreeItem().getGraphic());
	       }
	     }
	      
	     private String getString() 
	     {
	       return getItem() == null ? "" : getItem().toString();
	     }
	   }
	   
	   private void setupTandaContextMenu(final ContextMenu tandaContextMenu)
	   {
		 MenuItem moveUp = new MenuItem("Move Tanda Up"); 
	     MenuItem moveDown = new MenuItem("Move Tanda Down");
	     MenuItem delete = new MenuItem("Delete Tanda" );
	     tandaContextMenu.setOnShowing(new EventHandler() 
	     {
	       public void handle(Event e) 
	       {
	         SharedValues.selectedTanda= Integer.parseInt(tandaContextMenu.getId());
	       }
	     });
	     tandaContextMenu.getItems().addAll(moveUp, moveDown, delete);
	     moveUp.setOnAction(new EventHandler() 
	     {
	       public void handle(Event t) 
	       {
	    	  
	         playlistTreeItem.moveTandaUp(SharedValues.selectedTanda);   
	        
	       }
	     });
	     moveDown.setOnAction(new EventHandler() 
	     {
	       public void handle(Event t) 
	       {
	         playlistTreeItem.moveTandaDown(SharedValues.selectedTanda);   
	         
	       }
	     });
	     delete.setOnAction(new EventHandler() 
	     {
	       public void handle(Event t) 
	       {
	         playlistTreeItem.deleteTanda(SharedValues.selectedTanda);   
	       }
	     });
	   } 
	   
	   private void setupTrackContextMenu(final ContextMenu trackContextMenu)
	   {
		   MenuItem moveUp = new MenuItem("Move Track Up"); 
	     MenuItem moveDown = new MenuItem("Move Track Down");
	     MenuItem delete = new MenuItem("Remove Track" );
	     trackContextMenu.setOnShowing(new EventHandler() 
	     {
	       public void handle(Event e) 
	       {
	    	 String trackId = trackContextMenu.getId();  
	    	 String[] tokens = trackId.split(",");
	    	 
	    	 for (int i = 0; i < tokens.length; i++)
	    	 {
	    	   if (i==0) SharedValues.selectedTanda=Integer.parseInt(tokens[i]);
	    	   if (i==1) SharedValues.selectedTrack=Integer.parseInt(tokens[i]);
	         }
	    	 
	       }
	     });
	     trackContextMenu.getItems().addAll(moveUp, moveDown, delete);
	     moveUp.setOnAction(new EventHandler() 
	     {
	       public void handle(Event t) 
	       {
	    	   playlistTreeItem.getTanda(SharedValues.selectedTanda).moveTrackUp(SharedValues.selectedTrack);
	       }
	     });
	     moveDown.setOnAction(new EventHandler() 
	     {
	       public void handle(Event t) 
	       {
	    	   playlistTreeItem.getTanda(SharedValues.selectedTanda).moveTrackDown(SharedValues.selectedTrack);
	       }
	     });
	     delete.setOnAction(new EventHandler() 
	     {
	       public void handle(Event t) 
	       {
	    	   playlistTreeItem.getTanda(SharedValues.selectedTanda).deleteTrack(SharedValues.selectedTrack);
	       }
	     });
	   } 

	   
	 
			 	 	
}
