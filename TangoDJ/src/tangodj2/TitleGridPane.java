package tangodj2;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TitleGridPane extends VBox
{
   private GridPane grid = new GridPane();
   private Font titleFont=Font.font("Arial", FontWeight.BOLD, 20);
   private Font labelFont=Font.font("Arial", FontWeight.BOLD, 14);
   private Font valueFont=Font.font("Arial", FontWeight.NORMAL, 14);
   
   private final Paint background = RadialGradientBuilder.create()
                     .stops(new Stop(0d, Color.TURQUOISE), new Stop(1, Color.web("3A5998")))
                   .centerX(0.5d).centerY(0.5d).build();
             private final String LABEL_STYLE = "-fx-text-fill: black; -fx-font-size: 14;"
                     + "-fx-effect: dropshadow(one-pass-box, maroon, 5, 0, 1, 1);";
   
   private int row=0;
   
   public TitleGridPane(String title)
   {
     
     Label titleLabel = new Label(title);
     titleLabel.setFont(titleFont);
     getChildren().add(titleLabel);
     getChildren().add(grid);
     grid.setVgap(5);
     grid.setHgap(5);
     grid.setPadding(new Insets(5, 5, 5, 5));
     ColumnConstraints col0 = new ColumnConstraints();
     col0.setHalignment(HPos.RIGHT);
     grid.getColumnConstraints().add(col0);
     grid.setGridLinesVisible(true);
     //setStyle("-fx-background-color: navajowhite; -fx-border-color: BLACK; -fx-border-style: SOLID; -fx-border-width: 1px;");
     
     grid.setStyle("-fx-background-color: plum; -fx-border-color: RED; -fx-border-style: SOLID; -fx-border-width: 1px;");
     setPadding(new Insets(5, 5, 5, 5));
     setSpacing(5);
   }
   
   public void addRow(String label, Label value)
   {
     Label labelLabel = new Label(label+":");
     labelLabel.setFont(labelFont);
     labelLabel.setStyle(LABEL_STYLE);
     value.setFont(valueFont);
     grid.add(labelLabel, 0, row);
     grid.add(value, 1, row);
     row++;
   }
}
