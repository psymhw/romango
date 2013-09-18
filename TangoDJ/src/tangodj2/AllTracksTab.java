package tangodj2;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;



import tangodj2.PlaylistTree.BaseTreeItem;
import tangodj2.PlaylistTree.PlaylistTree;
import tangodj2.PlaylistTree.TandaTreeItem;
import tangodj2.test.TrackTreeItem;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AllTracksTab 
{
  Tab tab;
  SharedValues sharedValues = new SharedValues();
  private TableView<Track> allTracksTable = new TableView<Track>();
  private final static ObservableList<Track> allTracksData = FXCollections.observableArrayList();
  TrackLoader2 trackLoader = new TrackLoader2();
  private Button newTandaButton;
  PlaylistTree playlistTree;
  TreeView<String> treeView;
	 	
   public AllTracksTab()
   {
	 tab = new Tab();
	 tab.setText("All Tracks");
	 setupAllTracksTable();
	 loadData();
	      
	 final Button addButton = new Button("Add Tango Tracks");
	 addButton.setOnAction(new EventHandler<ActionEvent>() 
	 {
	   public void handle(ActionEvent e) 
	   {
	     DirectoryChooser directoryChooser = new DirectoryChooser();
	     directoryChooser.setInitialDirectory(new File("C:\\music\\tango"));  // temporary 
	     File selectedDirectory = 
	     directoryChooser.showDialog(TangoDJ2.primaryStage);
	              
	     if(selectedDirectory == null) { System.out.println("No Directory selected"); } 
	     else
	     {
	       try
	       {
	         trackLoader.process(selectedDirectory.toPath().toString());
	       } catch (Exception ex) {ex.printStackTrace();}
	     }
	   }
	 });

	 final Label label = new Label("All Tracks");
	 label.setFont(new Font("Arial", 20));
	      
	 final VBox vbox = new VBox();
	 vbox.setPadding(new Insets(10, 10, 10, 10));
	 vbox.setSpacing(20);
	 vbox.setStyle("-fx-background-color: DAE6F3; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work

	 
	 vbox.getChildren().addAll(label, getTable(), addButton);
	      
	 HBox hbox =  new HBox();
	 hbox.setPadding(new Insets(10, 10, 10, 10));
	 hbox.setSpacing(20);
	 hbox.getChildren().add(vbox);
	 hbox.setStyle("-fx-background-color: CC99CC; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 3px;"); // border doesn't work

	      
	 playlistTree = new PlaylistTree(SharedValues.currentPlaylist);
	 
    // tanda = new Tanda("Canaro", "Vals");
     
     VBox playlistBox = new VBox();
     playlistBox.setPadding(new Insets(10, 10, 10, 10));
     playlistBox.setSpacing(20);
     
     
	 
	 setupTandaButton();
     
	 playlistBox.getChildren().add(newTandaButton);
	 
	 treeView = playlistTree.getTreeView();
	 treeView.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>()
	 {
	   @Override
	   public TreeCell<String> call(TreeView<String> p) 
	   {
		 return new MyCellFactory();
	   }
	 });
	 
	 /* 
	  * Detects treen item selected
	  */
	 ChangeListener<TreeItem<String>> cl = new ChangeListener<TreeItem<String>>() 
	 {
	   public void changed(ObservableValue<? extends TreeItem<String>> observableValue, TreeItem<String> oldItem, TreeItem<String> newItem) 
	   {
		 if (newItem!=null)
		 {
		   BaseTreeItem bti = (BaseTreeItem)newItem;
		   BaseTreeItem parent = (BaseTreeItem)bti.getParent();
		   if ("tanda".equals(bti.getTreeType())) SharedValues.selectedTanda=bti.getPosition();
		   int parentPos=-1;
		   if (parent!=null) parentPos=parent.getPosition();
		   
		   System.out.println("Selected: " + newItem.getValue()+" Position: "+bti.getPosition()+" Type: "+bti.getTreeType()+" parent pos: "+parentPos); 
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
	 
	 playlistBox.getChildren().add(treeView);
     hbox.getChildren().add(playlistBox);
     hbox.setHgrow(treeView, Priority.ALWAYS);
     setupListeners();
     tab.setContent(hbox);
   }
	
   private final class MyCellFactory extends TreeCell<String> {
	   
       private TextField textField;
       private ContextMenu addMenu = new ContextMenu();

       public MyCellFactory() 
       {
         MenuItem addMenuItem = new MenuItem("Add Employee");
         addMenu.getItems().add(addMenuItem);
         addMenuItem.setOnAction(new EventHandler() 
         {
            public void handle(Event t) {
                   TreeItem newEmployee = 
                       new TreeItem<String>("New Employee");
                           getTreeItem().getChildren().add(newEmployee);
               }
           });
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
           if ("tanda".equals(bti.getTreeType())) setFont(Font.font("Serif", 16));
           setGraphic(getTreeItem().getGraphic());
           if (!getTreeItem().isLeaf())
           {
             setContextMenu(addMenu);
           }
         }
       }
      
       private String getString() 
       {
         return getItem() == null ? "" : getItem().toString();
       }
   }
   
   private void setupTandaButton() 
   {
     newTandaButton = new Button("New Tanda");
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
     
     EventHandler <MouseEvent>bHandler = new EventHandler<MouseEvent>() 
     {
       public void handle(MouseEvent event) 
       {
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
             TandaTreeItem tandaTreeItem = new TandaTreeItem(artist, styleId, playlistTree.getTandaCounter(), true);
         	 playlistTree.addTanda(tandaTreeItem);
             myDialog.close();
           }
         });
       
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
     };
     
     newTandaButton.setOnMouseClicked(bHandler);
   }
   
   public Tab getTab()
   {
	 return tab;
   }
	
   private void setupListeners() 
   {
	 ChangeListener cl = new ChangeListener() 
	 {
	   public void changed(ObservableValue observable, Object oldValue, Object newValue) 
	   {
	     
	   }
	 };   
	 sharedValues.playlistTrackAdd.addListener(cl);
   }
	
	
   private void setupAllTracksTable() 
   {
	 double tableWidth=setupAllTracksColumns()+43;
	    
	 allTracksTable.setItems(allTracksData);
	 allTracksTable.setPrefWidth(tableWidth);
	 allTracksTable.setMinWidth(tableWidth);
	 allTracksTable.setMaxWidth(tableWidth);
	 allTracksTable.setEditable(false);
	    
	 allTracksTable.setOnKeyReleased(keyEvent);
	    
	 allTracksTable.getSelectionModel().selectedItemProperty()
	                 .addListener(new ChangeListener() 
	                 {
	              	    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
	              		Track selectedTrack = (Track)newValue;
	              		if (selectedTrack!=null)
	              		{
	              		  System.out.println("selected: "+selectedTrack.getTitle());
	              		//tanda.addTrack(new TandaTrack(selectedTrack.getTitle()));
	              		  sharedValues.title.set(selectedTrack.getTitle());
	              		  sharedValues.selectedPathHash.set(selectedTrack.getPathHash());
	              		}
	              	}
	             });
	    
   }
	
	  
   private double setupAllTracksColumns()
   {
	 TableColumn titleCol = new TableColumn("Title");
	      titleCol.setMinWidth(100);
	      titleCol.setPrefWidth(150);
	      titleCol.setCellValueFactory(
	          new PropertyValueFactory<Track, String>("title"));
	      titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
	      titleCol.setOnEditCommit(
	          new EventHandler<CellEditEvent<Track, String>>() {
	              @Override
	              public void handle(CellEditEvent<Track, String> t) {
	                  ((Track) t.getTableView().getItems().get(
	                          t.getTablePosition().getRow())
	                          ).setTitle(t.getNewValue());
	              }
	          }
	      );

	      
	       
	  TableColumn artistCol = new TableColumn("Artist");
	      artistCol.setMinWidth(50);
	      artistCol.setPrefWidth(100);
	      artistCol.setCellValueFactory(
	          new PropertyValueFactory<Track, String>("artist"));
	      artistCol.setCellFactory(TextFieldTableCell.forTableColumn());
	      artistCol.setOnEditCommit(
	          new EventHandler<CellEditEvent<Track, String>>() {
	              @Override
	              public void handle(CellEditEvent<Track, String> t) {
	                  ((Track) t.getTableView().getItems().get(
	                      t.getTablePosition().getRow())
	                      ).setArtist(t.getNewValue());
	              }
	          }
	      );
	      
	  TableColumn albumCol = new TableColumn("Album");
	      albumCol.setMinWidth(50);
	      albumCol.setPrefWidth(150);
	      albumCol.setCellValueFactory(
	          new PropertyValueFactory<Track, String>("album"));
	      albumCol.setCellFactory(TextFieldTableCell.forTableColumn());
	      albumCol.setOnEditCommit(
	          new EventHandler<CellEditEvent<Track, String>>() {
	              @Override
	              public void handle(CellEditEvent<Track, String> t) {
	                  ((Track) t.getTableView().getItems().get(
	                      t.getTablePosition().getRow())
	                      ).setAlbum(t.getNewValue());
	              }
	          }
	      );
	      
	  TableColumn genreCol = new TableColumn("Genre");
	      genreCol.setMinWidth(30);
	      genreCol.setPrefWidth(50);
	      genreCol.setCellValueFactory(
	          new PropertyValueFactory<Track, String>("genre"));
	      genreCol.setCellFactory(TextFieldTableCell.forTableColumn());
	      genreCol.setOnEditCommit(
	          new EventHandler<CellEditEvent<Track, String>>() {
	              @Override
	              public void handle(CellEditEvent<Track, String> t) {
	                  ((Track) t.getTableView().getItems().get(
	                      t.getTablePosition().getRow())
	                      ).setGenre(t.getNewValue());
	              }
	          }
	      );
	      
	  TableColumn commentCol = new TableColumn("Comment");
	      commentCol.setMinWidth(50);
	      commentCol.setPrefWidth(100);
	      commentCol.setCellValueFactory(
	          new PropertyValueFactory<Track, String>("comment"));
	      commentCol.setCellFactory(TextFieldTableCell.forTableColumn());
	      commentCol.setOnEditCommit(
	          new EventHandler<CellEditEvent<Track, String>>() {
	              @Override
	              public void handle(CellEditEvent<Track, String> t) {
	                  ((Track) t.getTableView().getItems().get(
	                      t.getTablePosition().getRow())
	                      ).setComment(t.getNewValue());
	              }
	          }
	      );
	      
	      TableColumn durationCol = new TableColumn("Length");
	      durationCol.setMinWidth(50);
	      durationCol.setPrefWidth(50);
	     
	      durationCol.setCellValueFactory(
	          new PropertyValueFactory<Track, String>("duration"));
	      durationCol.setCellFactory(TextFieldTableCell.forTableColumn());
	      
	     
	  allTracksTable.getColumns().addAll(titleCol, durationCol, artistCol, albumCol, genreCol, commentCol);
	      
	  return titleCol.getWidth()+artistCol.getWidth()+albumCol.getWidth()+genreCol.getWidth()+commentCol.getWidth();
  }
	  
	//KEY EVENTS
	 	EventHandler<KeyEvent>keyEvent = new EventHandler<KeyEvent>() 
	     {
	 	  public void handle(KeyEvent ke) 
	 	  {
	 	    if (ke.getCode()==KeyCode.ESCAPE) 
	 	    {
	 	    	System.out.println("Esc Pressed");
	 	    	
	 	    }
	 	    if (ke.getCode()==KeyCode.UP) 
	 	    {
	 	    	System.out.println("UP Pressed");
	 	    }
	 	    if (ke.getCode()==KeyCode.DOWN) 
	 	    {
	 	    	System.out.println("DOWN Pressed");
	 	    }
	 	    if (ke.getCode()==KeyCode.RIGHT) 
	 	    {
	 	      sharedValues.playlistTrackAdd.set(sharedValues.playlistTrackAdd.get()+1);  
	 	      System.out.println("RIGHT Pressed: "+sharedValues.playlistTrackAdd.get());
	 	      playlistTree.addTrack();
	 	      
	 	    }
	 	  }
	 	};
	 	
	 	public TableView<Track> getTable() {
			return allTracksTable;
		}
	 	
	 	public static void reloadData()
	 	{
	 		allTracksData.clear();
	 		loadData();
	 	}
	 	
	 	public static void loadData()
	 	  {
	 	    String title;
	 	    String artist;
	 	    String album;
	 	    String genre;
	 	    String comment;
	 	    String pathHash;
	 	    String path;
	 	    int cortina;
	 	    int duration=0;
	 	    
	 	    try
	 	    {
	 			Connection connection = DriverManager.getConnection("jdbc:derby:tango_db;create=false");
	 			Statement statement = connection.createStatement();
	 			ResultSet resultSet = statement.executeQuery("select * from tracks order by artist, album, title");
	 			while(resultSet.next())
	 			{
	 			  title=resultSet.getString("title");
	 			  artist = resultSet.getString("artist");
	 			  album = resultSet.getString("album");
	 			  genre = resultSet.getString("genre");
	 			  comment = resultSet.getString("comment");
	 			  pathHash = resultSet.getString("pathHash");
	 			  path = resultSet.getString("path");
	 			  duration=resultSet.getInt("duration");
	 			  cortina=resultSet.getInt("cortina");
	 			  allTracksData.add(new Track(title, artist, album, genre, comment, pathHash, path, duration, cortina));
	 			  
	 			  //System.out.println("added: "+title);
	 			}
	 			if (statement!=null) statement.close();
	 			if (connection!=null) connection.close();
	 	    } catch (Exception e) { e.printStackTrace();}
	 	  }
	 	
	 	
	  
	}
