package tangodj2.test;


import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import projmon.FXControlLoader;
 
/**
* A {@link TreeCell} implementation that delegates it's view setup to an
* instance of {@link WorkHierarchy}.
*
* @author atill
*
*/
public class HierarchyTreeCell extends TreeCell<WorkHierarchy> {
private enum WorkDropType { DROP_INTO, REORDER }
/**
* Using a static here, it's just too convenient.
*/
private static TreeItem<WorkHierarchy> draggedTreeItem;
private static WorkDropType workDropType;
private final FXControlLoader controlLoader;
private Object controller;
public HierarchyTreeCell(FXControlLoader aControlLoader, final ClickListeners<WorkHierarchy> clickListeners) {
controlLoader = aControlLoader;
 
getStyleClass().add("tree-cell");
setOnMouseClicked(new EventHandler<MouseEvent>() {
@Override
public void handle(MouseEvent event) {
if(event.getButton() == MouseButton.PRIMARY)
clickListeners.leftClickListener.get().itemSelected(getItem());
else
clickListeners.rightClickListener.get().itemSelected(getItem());
}});
setOnDragOver(new EventHandler<DragEvent>() {
@Override
public void handle(DragEvent event) {
if(isDraggableToParent() && isNotAlreadyChildOfTarget(HierarchyTreeCell.this.getTreeItem()) && draggedTreeItem.getParent() != getTreeItem()) {
Point2D sceneCoordinates = HierarchyTreeCell.this.localToScene(0d, 0d);
double height = HierarchyTreeCell.this.getHeight();
// get the y coordinate within the control
double y = event.getSceneY() - (sceneCoordinates.getY());
 
// if the drop is three quarters of the way down the control
// then the drop will be a sibling and not into the tree item
// set the dnd effect for the required action
if (y > (height * .75d)) {
setEffect(null);
getStyleClass().add("dnd-below");
workDropType = WorkDropType.REORDER;
}
else {
getStyleClass().remove("dnd-below");
InnerShadow shadow;
shadow = new InnerShadow();
shadow.setOffsetX(1.0);
shadow.setColor(Color.web("#666666"));
shadow.setOffsetY(1.0);
setEffect(shadow);
 
workDropType = WorkDropType.DROP_INTO;
}
event.acceptTransferModes(TransferMode.MOVE);
}
}
});
setOnDragDetected(new EventHandler<MouseEvent>() {
@Override
public void handle(MouseEvent event) {
ClipboardContent content;
content = new ClipboardContent();
content.putString("TROLOLOL");
Dragboard dragboard;
dragboard = getTreeView().startDragAndDrop(TransferMode.MOVE);
dragboard.setContent(content);
draggedTreeItem = getTreeItem();
event.consume();
}});
setOnDragDropped(new EventHandler<DragEvent>() {
@Override
public void handle(DragEvent event) {
boolean dropOK = false;
if(draggedTreeItem != null) {
TreeItem<WorkHierarchy> draggedItemParent = draggedTreeItem.getParent();
WorkHierarchy draggedWork = draggedTreeItem.getValue();
if(workDropType == WorkDropType.DROP_INTO) {
 
if(isDraggableToParent() && isNotAlreadyChildOfTarget(HierarchyTreeCell.this.getTreeItem()) && draggedTreeItem.getParent() != getTreeItem()) {
draggedWork.removeLinkFrom(draggedItemParent.getValue());
draggedWork.addLinkTo(getTreeItem().getValue());
draggedItemParent.getValue().getChildren().remove(draggedWork);
getTreeItem().getValue().getChildren().add(draggedWork);
getTreeItem().setExpanded(true);
clickListeners.leftClickListener.get().itemSelected(draggedWork);
}
}
else if(workDropType == WorkDropType.REORDER) {
}
 
dropOK = true;
draggedTreeItem = null;
}
 
event.setDropCompleted(dropOK);
event.consume();
}});
setOnDragExited(new EventHandler<DragEvent>() {
@Override
public void handle(DragEvent event) {
// remove all dnd effects
setEffect(null);
getStyleClass().remove("dnd-below");
}});
}
 
protected boolean isDraggableToParent() {
return draggedTreeItem.getValue().isDraggableTo(getTreeItem().getValue());
}
 
protected boolean isNotAlreadyChildOfTarget(TreeItem<WorkHierarchy> treeItemParent) {
if(draggedTreeItem == treeItemParent)
return false;
if(treeItemParent.getParent() != null)
return isNotAlreadyChildOfTarget(treeItemParent.getParent());
else
return true;
}
protected void updateItem(WorkHierarchy item, boolean empty) {
// if a tree cell is showing the text of another value that was
// selected, it may be that the properties are still bound to a form control
if (getItem() != null) {
getItem().unbind(this);
}
super.updateItem(item, empty);
if(item != null) {
item.updateTreeCell(this, controlLoader);
}
}
 
public Object getController() {
return controller;
}
 
public void setController(Object controller) {
this.controller = controller;
}
}
