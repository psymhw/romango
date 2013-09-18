package tangodj2.test;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextTreeItem extends TreeItem<Text>
{
	private String name;
	public TextTreeItem(Text text)
	{
		super(text);
		name=text.getText();
	}
	
	public TextTreeItem(Text text, Node graphic)
	{
		super(text, graphic);
		name=text.getText();
	}
	public String getName() {
		return name;
	}

}
