package tangodj2.test;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This demo illustrate some basic functions of the {@link TreeView} component.
 *
 * The goal of this demo is to illustrate the following:
 * <ol>
 *   <li>Lazy loading of children. </li>
 *   <li>Using the {@link ChangeListener} the catch change events and print item text for the selected item.</li>
 *   <li>Adding new items to the tree.</li>
 * </ol>
 * @author Allitico
 */
public class TreeViewDemo extends Application implements EventHandler<ActionEvent>, ChangeListener<TreeItem<String>>  {

    /** Label used to print the text of the current selected item in the tree.*/
    private Label labelSelectedItem;
    /** Label used to print error messages to the user.*/
    private Label labelErrorText;
    /** The {@link TreeView} used in this demo. */
    private TreeView<String> treeView;
    /** The input field for the name of the new item to create.*/
    private TextField inputNewItem;
    /** Button used to insert a new item into the tree. */
    private Button buttonNewItem;

    @Override
    public void start(Stage stage) throws Exception {
        initComponents();

        stage.setTitle("Demo of TreeView");

        BorderPane mainPane = new BorderPane();

        mainPane.setLeft(treeView);

        VBox centerPane = new VBox();
        centerPane.setSpacing(3);
        centerPane.setPadding(new Insets(3));
        centerPane.getChildren().addAll(labelSelectedItem, labelErrorText, inputNewItem, buttonNewItem);
        mainPane.setCenter(centerPane);

        Scene scene = new Scene(mainPane, 600, 400);

        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    /** Create and setup all the components used in this demo. */
    private void initComponents() {
        LazyTreeItem rootItem = new LazyTreeItem("Root", 0);
        rootItem.setExpanded(true);
        treeView = new TreeView<String>(rootItem);
        treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        treeView.getSelectionModel().selectedItemProperty().addListener(this);

        labelSelectedItem = new Label("Selected:");

        labelErrorText = new Label("Error: ");

        inputNewItem = new TextField();

        buttonNewItem = new Button("Insert item");
        buttonNewItem.setOnAction(this);
    }

    /**
     * Returns the selected item from the {@link TreeView} or null if there is no selected item.
     *
     * @return The selected item or null if no item is selected.
     */
    private LazyTreeItem getSelectedItem() {
        return (LazyTreeItem) treeView.getSelectionModel().getSelectedItem();
    }

    /** Handles the action which is triggered by the button. */
    @Override
    public void handle(ActionEvent event) {
        LazyTreeItem selectedItem = getSelectedItem();

        if (selectedItem == null) {
            labelErrorText.setText("Error: You have to select a item in the tree.");
            return;
        }

        if (inputNewItem.getText().length() <= 0) {
            labelErrorText.setText("Error: You have input a item name.");
            return;
        }

        labelErrorText.setText("Error:");
        selectedItem.getChildren().add(new LazyTreeItem(inputNewItem.getText(), selectedItem.getDepth()));
    }

    /** Handles change event from the {@link TreeView}. */
    @Override
    public void changed(ObservableValue<? extends TreeItem<String>> observableValue, TreeItem<String> oldItem, TreeItem<String> newItem) {
        labelSelectedItem.setText("Selected: " + newItem.getValue());
    }

    /**
     * Lazy implementation of the {@link TreeItem}.
     *
     * @author Allitico
     */
    private class LazyTreeItem extends TreeItem<String> {
        /** The depth of this tree item in the {@link TreeView}. */
        private final int depth;
        /** Control if the children of this tree item has been loaded. */
        private boolean hasLoadedChildren = false;

        public LazyTreeItem(String itemText, int depth) {
            super(itemText);
            this.depth = depth;
        }

        @Override
        public ObservableList<TreeItem<String>> getChildren() {
            if (hasLoadedChildren == false) {
                loadChildren();
            }
            return super.getChildren();
        }

        @Override
        public boolean isLeaf() {
            if (hasLoadedChildren == false) {
                loadChildren();
            }
            return super.getChildren().isEmpty();
        }

        /** Create some dummy children for this item. */
        @SuppressWarnings("unchecked") // Safe to ignore since we know that the types are correct.
        private void loadChildren() {
            hasLoadedChildren = true;
            int localDeepth = depth + 1;
            LazyTreeItem child1 = new LazyTreeItem("Child 1 (deepth = " + localDeepth + ")", localDeepth);
            LazyTreeItem child2 = new LazyTreeItem("Child 2 (deepth = " + localDeepth + ")", localDeepth);
            super.getChildren().setAll(child1, child2);
        }

        /** Return the depth of this item within the {@link TreeView}.*/
        public int getDepth() {
            return depth;
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}