package db;

import java.sql.Date;

import org.apache.struts.action.ActionForm;

public class LinksDb extends ActionForm
{
  private static final long serialVersionUID = 8787765278421576343L;
  private int id;
  private String title="";
  private String category=""; 
  private String description="";
  private String url="";
  private Date post_date;
  private String mode;
  private int seq=0;
  
  
  public String getMode() {
	return mode;
	
}
public void setMode(String mode) {
	this.mode = mode;
}

  
  public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getCategory() {
	return category;
}
public void setCategory(String category) {
	this.category = category;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public Date getPost_date() {
	return post_date;
}
public void setPost_date(Date post_date) {
	this.post_date = post_date;
}
public int getMember_id() {
	return member_id;
}
public void setMember_id(int member_id) {
	this.member_id = member_id;
}
int member_id;


public int getSeq() {
	return seq;
}
public void setSeq(int seq) {
	this.seq = seq;
}
}
