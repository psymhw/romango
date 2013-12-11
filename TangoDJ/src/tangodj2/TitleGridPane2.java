package tangodj2;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TitleGridPane2 extends GridPane
{
  private Font titleFont=Font.font("Arial", FontWeight.BOLD, 20);
  private Font labelFont=Font.font("Arial", FontWeight.BOLD, 18);
  private Font valueFont=Font.font("Arial", FontWeight.NORMAL, 18);
  private int row=1;
  private final String LABEL_STYLE = "-fx-text-fill: black; -fx-font-size: 18;"
                                   + "-fx-effect: dropshadow(one-pass-box, maroon, 5, 0, 1, 1);";
  private String title;
  
  public TitleGridPane2(String title)
  {
    this.title=title;
    setVgap(5);
    setHgap(5);
    setPadding(new Insets(5, 5, 5, 5));
    
    
   // setStyle("-fx-background-color: plum; -fx-border-color: RED; -fx-border-style: SOLID; -fx-border-width: 1px;");
    //setGridLinesVisible(true);
    setStyle("-fx-background-color: gainsboro;");
    
    add(getTitlePane(),0,0);
    //setHgrow(titleLabel, Priority.ALWAYS);
    ColumnConstraints column1 = new ColumnConstraints();
    column1.setPercentWidth(30);
    ColumnConstraints column2 = new ColumnConstraints();
    column2.setPercentWidth(70);
    getColumnConstraints().addAll(column1, column2);
  }
  
  public void addRow(String label, Label value)
  {
    Label labelLabel = new Label(label+":");
    labelLabel.setFont(labelFont);
    value.setFont(valueFont);
    labelLabel.setStyle(LABEL_STYLE);
    GridPane.setHalignment(labelLabel, HPos.RIGHT);
    GridPane.setHalignment(value, HPos.LEFT);
   
    add(labelLabel, 0, row);
    add(value, 1, row);
    row++;
  }
  
  public void addTimeRow(String label, Label value)
  {
    Label labelLabel = new Label(label+":");
    labelLabel.setFont(labelFont);
    value.setFont(valueFont);
    labelLabel.setStyle(LABEL_STYLE);
    GridPane.setHalignment(labelLabel, HPos.RIGHT);
    GridPane.setHalignment(value, HPos.LEFT);
   
    add(labelLabel, 0, row);
    add(getTimePane(value), 1, row);
    row++;
  }
  
  private Pane getTimePane(Label value)
  {
    StackPane stackPane = new StackPane();
    stackPane.setAlignment(Pos.CENTER_RIGHT); 
    //stackPane.setStyle("-fx-border-color: RED; -fx-border-style: SOLID; -fx-border-width: 1px;");
    
    GridPane.setHalignment(value, HPos.LEFT);
    stackPane.getChildren().add(value);
    stackPane.setMaxWidth(65);
    return stackPane;
  }
  
  private Pane getTitlePane()
  {
    StackPane stackPane = new StackPane();
    
    // stackPane.setPrefHeight(height);
    stackPane.setAlignment(Pos.CENTER); 
    stackPane.setStyle("-fx-background-color: mediumslateblue; -fx-effect: dropshadow(one-pass-box, black, 5, 0, 1, 1);");
    Label titleLabel = new Label(title);
    titleLabel.setFont(titleFont);
    
    GridPane.setHalignment(titleLabel, HPos.CENTER);
    stackPane.getChildren().add(titleLabel);
    GridPane.setColumnSpan(stackPane, 2);
    return stackPane;
  }
}
