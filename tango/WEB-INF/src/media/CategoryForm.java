package media;

import org.apache.struts.action.ActionForm;

public class CategoryForm extends ActionForm
{
  private static final long serialVersionUID = -2365038987011433639L;
  String selectedValue;

public String getSelectedValue() {
	return selectedValue;
}

public void setSelectedValue(String selectedValue) {
	this.selectedValue = selectedValue;
}
}
