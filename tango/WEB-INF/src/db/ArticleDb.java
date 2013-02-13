package db;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.struts.action.ActionForm;

public class ArticleDb extends ActionForm
{
  private static final long serialVersionUID = -700881886322305365L;
  int id;
  String title;
  String summary;
  String strDate;
  Date article_date;
  String author;
  String category;
  String body;
  int active;
  String admin_notes;
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
public String getSummary() {
	return summary;
}
public void setSummary(String summary) {
	this.summary = summary;
}
public Date getArticle_date() {
	return article_date;
}
public void setArticle_date(Date article_date) {
	this.article_date = article_date;
}

public String getAuthor() {
	return author;
}
public void setAuthor(String author) {
	this.author = author;
}
public String getCategory() {
	return category;
}
public void setCategory(String category) {
	this.category = category;
}
public String getBody() {
	return body;
}
public void setBody(String body) {
	this.body = body;
}
public boolean getActiveBool() {
	if (this.active==1) return true;
	else return false;
}
public void setActiveBool(boolean active) {
	if (active)
	this.active = 1;
	else this.active=0;
}
public String getAdmin_notes() {
	return admin_notes;
}
public void setAdmin_notes(String admin_notes) {
	this.admin_notes = admin_notes;
}
public String getMode() {
	return mode;
}
public void setMode(String mode) {
	this.mode = mode;
}
public String getStrDate() {
	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	if (this.article_date!=null) strDate = df.format(this.article_date);
	return strDate;
}
public void setStrDate(String strDate) {
	this.strDate = strDate;
	java.util.Date jDate=null;
	java.sql.Date sDate;
	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	try{
	 jDate=df.parse(strDate);
	} catch (Exception e) { e.printStackTrace();}
	 sDate = new java.sql.Date(jDate.getTime());
	 this.article_date=sDate;
}
public int getActive() {
	return active;
}
public void setActive(int active) {
	this.active = active;
}
}
