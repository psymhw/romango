package db;

import org.apache.struts.action.ActionForm;

public class MediaDb  extends ActionForm
{
  private static final long serialVersionUID = 1431980311263419635L;
  int id;
  String title="";
  String description="";
  String embed="";
  String category="";
  int member_id;
  String mode;
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
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getEmbed() {
	return embed;
}
public void setEmbed(String embed) {
	this.embed = embed;
}
public String getCategory() {
	return category;
}
public void setCategory(String category) {
	this.category = category;
}
public int getMember_id() {
	return member_id;
}
public void setMember_id(int member_id) {
	this.member_id = member_id;
}
public String getMode() {
	return mode;
}
public void setMode(String mode) {
	this.mode = mode;
}
}
