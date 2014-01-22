package manning.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

/*
 * This implementation of the PortfolioServiceInterface uses Java Persistence API, 
 * injected with Spring 2.0 support, to provide the database persistence at the 
 * heart of these service methods.
 */

@Transactional 
public class PortfolioServiceJPAImpl implements PortfolioServiceInterface  {
	
	
	public boolean userExists ( String username ) {
		
		
		Query query = entityManager.createQuery ( "from User where username = :username" ).setParameter("username", username);
		List result = query.getResultList();
				
		return !result.isEmpty();  
		
	}
	
	public void updateUser( User user ){
		entityManager.merge( user );

	}
	
	public void updatePortfolio( Portfolio port ){
		entityManager.merge( port );

	}
	
	public void addImage ( File pic, String picName, String portfolioId ) throws IOException 
	{
       /* add image to the portfolio and save image file to local filesystem */
	   storeImageToFileSystem(picName, pic);
	   Portfolio port = getPortfolio(new Long (portfolioId));
	   port.addPic(picName );   
		
	}

	/* 
	 * Returns a user object if the username and password are valid.  Returns null otherwise.
	 */
	public User authenticateUser(String username, String password) {
		
		System.out.println("Authenticating: username = " + username );
	
		User validUser = null;
		
		
		Query query = entityManager.createQuery ( "from User where username = :username" ).setParameter("username", username);
		
		List result = query.getResultList();
		if ( !result.isEmpty() ) {
	    	
			User user = (User) result.get(0);

	    	/* If the username mapped to a real user, check password */
			if ( user != null && user.getPassword().equals( password )) {
				validUser = user;
			
			}
		}
		
		return validUser;
	}
	

	public Collection getUsers(){
		
		
		Query query = entityManager.createQuery ( "from User" );
		
		return query.getResultList();
				
	}
	
		
	public Collection getAllPortfolios() {
		
		ArrayList portfolios = new ArrayList();
		
		for (Iterator iter = getUsers().iterator(); iter.hasNext();) {
			User user = (User) iter.next();
			portfolios.addAll(user.getPortfolios() );
		}
		
		return portfolios;
			
	}
	
	public User getUser( String username ){
		
		
		Query query = entityManager.createQuery ( "from User where username = :username" );
		query.setParameter("username", username);
		
	    User user = (User) query.getSingleResult();
		return user;
		
	}
	
	public User getUser( Long id ){
		
		
		User user = entityManager.find(User.class, id );
		return user;
		
	}
	
	public Portfolio getPortfolio ( Long id ){

		Portfolio port = entityManager.find(Portfolio.class, id); 
		return port;
		
	}
	
	/* First one in the returned list gets to be default. */
	
	public String getDefaultUser() {
		
		
		Query query = entityManager.createQuery ( "from User" );
		
		String defaultUsername = null;
		
		if ( !query.getResultList().isEmpty() ) {
			User defaultUser = (User) query.getResultList().get( 0 );
			if ( defaultUser != null )
				defaultUsername = defaultUser.getUsername();
		}

		return defaultUsername;
	}

	public void persistUser ( User user ){
			
		entityManager.persist(user);
			
	}
	
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
	   this.entityManager = entityManager;
    }

	public boolean contains(User user) {
		return entityManager.contains( user );
	
	}

	/* This property accepts a parameter that defines the path to which the images
	 * will be saved.  Currently, this will be set by injection from a parameter value
	 * declared in applicationContext.xml
	 */
	private String fileSystemHomePath;
	
	public void setFileSystemHome(String homePath) {
		this.fileSystemHomePath = homePath;		
	}
	
	public String getFileSystemHome ( ) {
		return this.fileSystemHomePath;
	}
	
	private String storeImageToFileSystem ( String fileName, File pic ) throws IOException
	{
		  FileInputStream in = null;
	        FileOutputStream out = null;
	        
	        String directoryName = fileSystemHomePath;
	        File dir = new File ( directoryName);
	        if ( !dir.exists() )
	           dir.mkdir();
	        String targetPath =  dir.getPath() + dir.separator + fileName;
	        
	        File picDestination = new File ( targetPath);
	        try {
	            in = new FileInputStream( pic );
	            out = new FileOutputStream( picDestination );
	            int c;

	            while ((c = in.read()) != -1) {
	                out.write(c);
	            }

	        }finally {
	            if (in != null) {
	                in.close();
	            }
	            if (out != null) {
	                out.close();
	            }
	        }
	        
	        return picDestination.getAbsolutePath();
	}

}
