package tangodj2;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AboutDialog extends Stage
{
  public AboutDialog()
  {
	TextArea textArea = new TextArea();
	textArea.setEditable(false);
	textArea.setStyle("-fx-font-size: 16pt;");
	textArea.setWrapText(true);
	//textArea.setFont(Font.font("Serif", 16));
	textArea.setText("TangoDJ Version 1.01\n"
	                 +"By Rick Roman\n"
	                 +"Copyright 2014\n"
	                 +"http://www.romangoshoes.com/TangoDJ\n\n"
	                 +"This program has no copy protection. "
	                 +"If you are using it and have not paid, "
	                 +"I encourage you to do so."
	                 +" This was an incredible amount of work with a very limited user demographic.\n\n"
			         +"Check the web site above for more information.\n\n"
	                 +"Tango On!"
			
			);
	Scene myDialogScene = new Scene(textArea, 400, 300);
    setScene(myDialogScene);
    show();
  }
}
