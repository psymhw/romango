package links;

import org.apache.struts.action.ActionForm;

public class LinksCategoryForm extends ActionForm
{
  private static final long serialVersionUID = 3912754773490626529L;
  String selectedValue;

public String getSelectedValue() {
	return selectedValue;
}

public void setSelectedValue(String selectedValue) {
	this.selectedValue = selectedValue;
}
}
