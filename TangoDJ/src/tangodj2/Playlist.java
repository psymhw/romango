package tangodj2;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import tangodj2.PlaylistTree.BaseTreeItem;
import tangodj2.PlaylistTree.PlaylistTreeItem;
import tangodj2.PlaylistTree.TandaTreeItem;
import tangodj2.PlaylistTree.TrackTreeItem;

public class Playlist 
{
	private PlaylistTreeItem playlistTreeItem;
	private TreeView<String> treeView;
	private TrackTreeItem previouslyPlayingTrack=null;
	private TrackTreeItem previouslySelectedTrack=null;
  private int nextTrackIndex=0; 
  private int nowPlayingIndex=-1;
	private ArrayList<PlaylistTrack> flatPlaylistTracks =  new ArrayList<PlaylistTrack>();
	
	
	public Playlist() throws SQLException, ClassNotFoundException
	{
    //thisPlaylist=this;
	  setupTreeView();	
	}
	
	public void printTracks()
	{
	  int i=0;
	  while( true)
	  {
	    
	    BaseTreeItem ti = (BaseTreeItem)treeView.getTreeItem(i);
	    if (ti==null) break;
	    System.out.println(i+") "+ti.getTreeType());
	    i++;
	  }
	}
	
	public PlaylistTrack getNextTrack()
	{
	  if (nextTrackIndex==flatPlaylistTracks.size()) 
	  {  
	    System.out.println("End Of Playlist");
	    return null;
	  }
	  nowPlayingIndex=nextTrackIndex;
	  PlaylistTrack playlistTrack=flatPlaylistTracks.get(nextTrackIndex);
	  playlistTrack.trackTreeItem.setPlayingImage(true);
	  if (previouslyPlayingTrack!=null) previouslyPlayingTrack.setPlayingImage(false);
	  previouslyPlayingTrack=playlistTrack.trackTreeItem;
	  nextTrackIndex++;
	  return playlistTrack;
	}
	
	public void setPrevious()
  {
	  nextTrackIndex-=2;
	  if (nextTrackIndex<0) nextTrackIndex=0;
  }
	
	public void setNextTrackToPlay(int tanda, int trackInTanda)
	{
	  PlaylistTrack playlistTrack;
    Iterator<PlaylistTrack> it = flatPlaylistTracks.iterator();
    
    int index=0;
    while(it.hasNext())
    {
      playlistTrack = it.next();
      if ((playlistTrack.tandaNumber==tanda)&&(playlistTrack.trackInTanda==trackInTanda))
      {
        if (playlistTrack.trackTreeItem.getStatus()!=TrackTreeItem.PLAYING)
        {  
          playlistTrack.trackTreeItem.setNextPlayImage(true);
          nextTrackIndex=index;
          if (previouslySelectedTrack!=null)  previouslySelectedTrack.setNextPlayImage(false);
          previouslySelectedTrack=playlistTrack.trackTreeItem;
        }
        break;
      }
      index++;
    }
	  
	}
	
	public void generateFlatList()
  {
    int i=0;
    flatPlaylistTracks.clear();
    int tandaCounter=-1;
    int tandaTrackCounter=0;
    int numberOfTracksInTanda=0;
    String tandaName=null;
    PlaylistTrack playlistTrack;
    int numberOfTandas=0;
    
    PlaylistTreeItem playlistTreeItem=null;
    TandaTreeItem tandaTreeItem=null;
    TrackTreeItem trackTreeItem=null;
    
    while( true)
    {
      BaseTreeItem ti = (BaseTreeItem)treeView.getTreeItem(i);
      if (ti==null) break;
      if ("playlist".equals(ti.getTreeType()))
      {
        playlistTreeItem = (PlaylistTreeItem)ti;
        numberOfTandas=playlistTreeItem.getChildren().size();
      }
      else if ("tanda".equals(ti.getTreeType()))
      {
        tandaTrackCounter=0;
        tandaCounter++;
        tandaTreeItem = (TandaTreeItem)ti;
        tandaName = tandaTreeItem.getArtistAndStyle();
        numberOfTracksInTanda=tandaTreeItem.getChildren().size();
      }
      else if ("track".equals(ti.getTreeType()))
      {
        trackTreeItem = (TrackTreeItem)ti;
        playlistTrack = new PlaylistTrack();
        playlistTrack.title=trackTreeItem.getValue();
        playlistTrack.tandaName=tandaName;
        if ((tandaCounter+1)<numberOfTandas)
        {
          playlistTrack.nextTandaName=((TandaTreeItem)playlistTreeItem.getChildren().get(tandaCounter+1)).getArtistAndStyle();
        }
        else playlistTrack.nextTandaName="Good Night";
        playlistTrack.album=trackTreeItem.getAlbum();
        playlistTrack.path=trackTreeItem.getPath();
        playlistTrack.tandaNumber=tandaCounter;
        playlistTrack.trackInTanda=tandaTrackCounter;
        playlistTrack.numberOfTracksInTanda=numberOfTracksInTanda;
        playlistTrack.cortina=false;
        playlistTrack.trackTreeItem=trackTreeItem;
        flatPlaylistTracks.add(playlistTrack);
        tandaTrackCounter++;
      }
      i++;
    }
    
    /*
    i=0;
    Iterator<PlaylistTrack> it = flatPlaylistTracks.iterator();
    while(it.hasNext())
    {
      playlistTrack = it.next();
      System.out.println(i+") "
      +playlistTrack.tandaName+" "
          +playlistTrack.trackInTanda
          +" of "+playlistTrack.numberOfTracksInTanda+", "
          +playlistTrack.title+", "
          +playlistTrack.album+", "
          +playlistTrack.path
          );
      i++;
    }
    */
  }
	
	public TreeView<String> getTreeView()
	{
	   return treeView;	
	}
	
	public void addTanda(String artist, int styleId)
	{
		playlistTreeItem.addTanda(artist, styleId);
		generateFlatList();
	}
	
	public int getTandaCount()
	{
		return playlistTreeItem.getTandaCount();
	}
	
	public TandaTreeItem  getTanda(int index)
	{
	  
	  TandaTreeItem tandaTreeItem = (TandaTreeItem)playlistTreeItem.getChildren().get(index);
	  System.out.println("Playlist tanda: "+tandaTreeItem.getArtist());
	  return tandaTreeItem;
	}
	
  private void setupTreeView() throws SQLException, ClassNotFoundException
	{
	  playlistTreeItem =  Db.getPlaylist(SharedValues.currentPlaylist);
	  treeView = new TreeView<String>(playlistTreeItem);
	  treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	 
	  treeView.getStyleClass().add("playlistTree");
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
			   
         
         SharedValues.selectedPlaylistTrack=-1;
         
			   if ("tanda".equals(bti.getTreeType())) 
			   {
			     SharedValues.selectedTanda=playlistTreeItem.getTandaPosition((TandaTreeItem)bti);
			   }
			   else if ("track".equals(bti.getTreeType())) 
			   {
			     //if (previouslySelectedTrack!=null) previouslySelectedTrack.setPlayingImage(false);
			     TrackTreeItem trackTreeItem = (TrackTreeItem)bti;
				   TandaTreeItem parent = (TandaTreeItem)trackTreeItem.getParent();
				   //trackTreeItem.setPlayingImage(true);
				   //previouslySelectedTrack = trackTreeItem;
				   //String tValue = bti.getValue();
				   //bti.setValue("*"+tValue);
				  // bti.setValue(tValue);
				   SharedValues.selectedPlaylistTrack=parent.getTrackPosition(bti);

				   int parentPos=-1;
				   if (parent!=null) parentPos=playlistTreeItem.getTandaPosition((TandaTreeItem)parent);
				   SharedValues.selectedTanda=parentPos;
			   }
			   else
			   {
				   SharedValues.selectedTanda=0; 
			   }
			  // treeView.getFocusModel().focus(SharedValues.selectedTanda+SharedValues.selectedPlaylistTrack+1);
			   SharedValues.playlistFocus.set(SharedValues.playlistFocus.get()+1);
			   //SharedValues.playlist=thisPlaylist;
			   System.out.println("Tanda/Track: "
			            +SharedValues.selectedTanda+"/"+SharedValues.selectedPlaylistTrack+" - "
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
		 generateFlatList();
	   }
			
	   private final class MyCellFactory extends TreeCell<String> 
	   {
	     private ContextMenu tandaContextMenu =  new ContextMenu();
	     private ContextMenu trackContextMenu =  new ContextMenu();
	     private ContextMenu playlistContextMenu =  new ContextMenu();

	     public MyCellFactory() 
	     {
	       setupTandaContextMenu(tandaContextMenu);
	       setupTrackContextMenu(trackContextMenu);
         setupPlaylistContextMenu(playlistContextMenu);
	      
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
	         if (isSelected()) 
	         {
	           TreeItem treeItem = (TreeItem)bti;
	           System.out.println("selected tree cell: "+treeItem.getValue());
	         }
	         if ("playlist".equals(bti.getTreeType())) 
	         {  
	           setFont(Font.font("Serif", 20));
	           setContextMenu(playlistContextMenu);
	         }
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
	         generateFlatList();
	       }
	     });
	     moveDown.setOnAction(new EventHandler() 
	     {
	       public void handle(Event t) 
	       {
	         playlistTreeItem.moveTandaDown(SharedValues.selectedTanda);   
	         generateFlatList();
	       }
	     });
	     delete.setOnAction(new EventHandler() 
	     {
	       public void handle(Event t) 
	       {
	         playlistTreeItem.deleteTanda(SharedValues.selectedTanda); 
	         generateFlatList();
	       }
	     });
	   } 
	   
	   private void setupPlaylistContextMenu(final ContextMenu playlistContextMenu)
     {
       MenuItem newTanda = new MenuItem("New Tanda"); 
     //  playlistContextMenu.setOnShowing(new EventHandler() 
      // {
     //    public void handle(Event e) 
      //   {
      //     SharedValues.selectedTanda= Integer.parseInt(tandaContextMenu.getId());
      //   }
     //  });
       playlistContextMenu.getItems().add(newTanda);
       newTanda.setOnAction(new EventHandler() 
       {
         public void handle(Event t) 
         {
           newTandaDialog();   
         }
       });
      
     } 
     
	   
	   private void setupTrackContextMenu(final ContextMenu trackContextMenu)
	   {
		   MenuItem moveUp = new MenuItem("Move Track Up"); 
	     MenuItem moveDown = new MenuItem("Move Track Down");
	     MenuItem delete = new MenuItem("Remove Track");
	     MenuItem playNext = new MenuItem("Play Next");
	     
	     
	     trackContextMenu.setOnShowing(new EventHandler() 
	     {
	       public void handle(Event e) 
	       {
	    	 String trackId = trackContextMenu.getId();  
	    	 String[] tokens = trackId.split(",");
	    	 
	    	 for (int i = 0; i < tokens.length; i++)
	    	 {
	    	   if (i==0)
	    	   {
	    	     SharedValues.selectedTanda=Integer.parseInt(tokens[i]);
	    	   }
	    	   if (i==1) 
	    	   {
	    	     SharedValues.selectedPlaylistTrack=Integer.parseInt(tokens[i]);
	    	   }
	       }
       }
	   });
	   trackContextMenu.getItems().addAll(moveUp, moveDown, delete, playNext);
	   
	   playNext.setOnAction(new EventHandler() 
     {
       public void handle(Event t) 
       {
         setNextTrackToPlay(SharedValues.selectedTanda, SharedValues.selectedPlaylistTrack);
        }
     });
	   
	   
	   moveUp.setOnAction(new EventHandler() 
	   {
	     public void handle(Event t) 
	     {
	       playlistTreeItem.getTanda(SharedValues.selectedTanda).moveTrackUp(SharedValues.selectedPlaylistTrack);
	       generateFlatList();
	     }
	   });
	   
	   moveDown.setOnAction(new EventHandler() 
	   {
	     public void handle(Event t) 
	     {
	       playlistTreeItem.getTanda(SharedValues.selectedTanda).moveTrackDown(SharedValues.selectedPlaylistTrack);
	       generateFlatList();
	     }
	   });
	   
	   delete.setOnAction(new EventHandler() 
	   {
	     public void handle(Event t) 
	     {
	       playlistTreeItem.getTanda(SharedValues.selectedTanda).deleteTrack(SharedValues.selectedPlaylistTrack);
	       generateFlatList();
	     }
	   });
	 } 

	   private void newTandaDialog() 
	   {
	     final ComboBox comboBox = new ComboBox();
	     //Separator separator = new Separator();
	     
	     final TextBuilder seperatorBuilder = TextBuilder.create()
	            .fill(Color.BLACK)
	            .font(Font.font("Serif", 18));
	     
	     final Text alist = seperatorBuilder.text("A List").build();
	     Text blist =  seperatorBuilder.text("B List").build();
	     Text clist =  seperatorBuilder.text("C List").build();
	    
	     comboBox.getItems().add(alist);
	     comboBox.getItems().addAll(SharedValues.artistsA);
	     comboBox.getItems().add(blist);
	     comboBox.getItems().addAll(SharedValues.artistsB);
	     comboBox.getItems().add(clist);
	     comboBox.getItems().addAll(SharedValues.artistsC);
	    
	     final RadioButton rb1 = new RadioButton("Tango");
	     final RadioButton rb2 = new RadioButton("Vals");
	     final RadioButton rb3 = new RadioButton("Milonga");
	     final RadioButton rb4 = new RadioButton("Alternative");
	     final RadioButton rb5 = new RadioButton("Mixed");
	     final RadioButton rb6 = new RadioButton("Cleanup");
	     
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
	    
	     
	     final TextField tandaName = new TextField();
	     
	     
	         final Stage myDialog = new Stage();
	         myDialog.initModality(Modality.APPLICATION_MODAL);
	         Button okButton = new Button("SAVE");
	         okButton.setOnAction(new EventHandler<ActionEvent>()
	         {
	           public void handle(ActionEvent arg0) 
	           {
	           String artist = comboBox.getSelectionModel().getSelectedItem().toString();
	           int styleId = 0;
	           String selectedStr=styleGroup.getSelectedToggle().toString();
	           int i = selectedStr.indexOf("id=");
	           String numStr = selectedStr.substring(i+3, i+4);
	           try 
	           {
	             styleId= Integer.parseInt(numStr);
	           } catch (Exception e) {}
	           addTanda(artist, styleId);
	             myDialog.close();
	           }});
	       
	         Text tandaLabel = new Text("Orchestra: ");
	         tandaLabel.setFont(Font.font("Serif", 20));
	         
	         
	         GridPane gridPane = new GridPane();
	         gridPane.setPadding(new Insets(10, 10, 10, 10));
	         gridPane.setVgap(2);
	         gridPane.setHgap(5);
	         gridPane.add(tandaLabel, 0, 0);
	         gridPane.add(comboBox, 1, 0);
	         //gridPane.add(new Text("Style"), 0, 1);
	         gridPane.add(styleBox, 0, 1);
	         gridPane.add(okButton, 1, 2);
	         
	         Scene myDialogScene = new Scene(gridPane, 300, 200);
	         myDialog.setScene(myDialogScene);
	         myDialog.show();
	   }
	 
	  
}
