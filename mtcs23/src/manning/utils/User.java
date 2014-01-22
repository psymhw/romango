package manning.utils;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/*
 * This class represents a user of the ArtistPortfolio system.  This is one of 
 * our applications main domain objects.
 */

@Entity
public class User {
	
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;

	@OneToMany ( cascade={CascadeType.ALL }, mappedBy="owner")
	private Set<Portfolio> portfolios = new HashSet<Portfolio>();
	
	@Id @GeneratedValue
	private Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
			this.username = username;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Set getPortfolios()
	{
		return portfolios;
	}
	public void setPortfolios( Set portfolios ) {
		this.portfolios = portfolios;
	}
	public void addPortfolio ( Portfolio portfolio ){
		portfolio.setOwner( this );
		portfolios.add( portfolio );
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
