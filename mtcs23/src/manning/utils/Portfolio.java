package manning.utils;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*
 * The Portfolio class represents a single portfolio of one of the users.
 * 
 */

@Entity
public class Portfolio {
	
	
	private String name;
	
	@ManyToOne ( targetEntity = manning.utils.User.class, optional=false )
	private User owner;
	
	@Id @GeneratedValue
	private Long id;
	
	@org.hibernate.annotations.CollectionOfElements
	@JoinTable (name="portfolio_image", joinColumns=@JoinColumn(name="portfolio_id"))
	private Set<String> pics = new HashSet<String>();
		
	public Set getPics() {
		return pics;
	}
	public void setPics(Set pics) {
		this.pics = pics;
	}
	
	public void addPic ( String pic ){
		pics.add( pic );
	}
	public Long getId() {
		return id;
	}
	private void setId(Long id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
