package manning.utils;


import java.io.File;
import java.io.IOException;
import java.util.Collection;

/*
 * This interface provides a clean extension point for the implementation of the service object.
 * In Chapter Nine we will provide a Hibernate implementation of this interface.  Handling interfaces
 * instead of the direct implementation follows a well known best practice of design that keeps
 * our code from being too tightly coupled with the specific implementation.  Many well known, and well
 * documented, benefits follow from this habit.
 */

public interface PortfolioServiceInterface {
	
	
	public boolean userExists ( String username );
	
	public void updateUser( User user ) ; 
	
	public void addImage ( File file, String fileName, String portfolioId ) throws IOException ;
	
	public User authenticateUser(String username, String password) ;	

	public Collection getUsers();	
		
	public Collection getAllPortfolios();
	
	public User getUser( String username );
	
	public User getUser( Long id );
	
	public Portfolio getPortfolio ( Long id );
	
	public String getDefaultUser() ;
	
	public void persistUser ( User user );
	
	public boolean contains ( User user );
	
	public void updatePortfolio( Portfolio port );
	
	public void setFileSystemHome ( String homePath );
	
	public String getFileSystemHome ();
		
}
