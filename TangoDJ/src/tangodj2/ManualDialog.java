package tangodj2;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class ManualDialog extends Stage
{
  public ManualDialog()
  {
	TextArea textArea = new TextArea();
	textArea.setEditable(false);
	textArea.setStyle("-fx-font-size: 16pt;");
	textArea.setWrapText(true);
	
	StringBuffer mt = new StringBuffer("TangoDJ Manual\n\n");
	
	mt.append("TangoDJ is basically an MP3 player with special features:\n"
			+"* Tango aware organization of music tracks.\n"
			+"* Easy creation and manipulation of playlists.\n"
			+"* Cortina creation without external editors.\n"
			+"* Playlists with a sense of Tandas and Cortinas.\n"
			+"* Ability to reorder Tandas without risk of interrupting the music.\n"
			+"* Second screen to show dances what's playing and what's next.\n" 
			);
	
	textArea.setText(mt.toString());
	Scene myDialogScene = new Scene(textArea, 500, 300);
    setScene(myDialogScene);
    show();
  }
}
