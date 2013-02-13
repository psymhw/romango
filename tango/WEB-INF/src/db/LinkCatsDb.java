package db;

import org.apache.struts.action.ActionForm;

public class LinkCatsDb extends ActionForm
{ 
	private static final long serialVersionUID = 3421828104844223445L;
	private int id;
	private int seq;
	private String category;
	private String mode;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}

}
