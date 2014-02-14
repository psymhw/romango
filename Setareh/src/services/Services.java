package services;

import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import data.*;
import java.util.List;

// This class is the business services tier in the application.
// @Transactional is needed so that a Hibernate transaction is set up,
//  otherwise updates won't have an affect
@Transactional
public class Services {
	// So Spring can inject the session factory
	SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory value) {
		sessionFactory = value;
	}

	// Shortcut for sessionFactory.getCurrentSession()
	public Session sess() {
		return sessionFactory.getCurrentSession();
	}

	
	
	public User getUserById(long id) {
    return (User) sess().load(User.class, id);
  }
	
	public Product getProduct(int item) 
	{
	  List<Product> list = (List<Product>) sess().createQuery("from Product where item="+item).list();
	  if (list.size()>0)
	  return list.get(0);
	  else return null;
	}
	
	public List<Product> getProductList(String searchStr) 
	{
	  List<Product> list = (List<Product>) sess().createQuery("from Product where lower(product_name) like '%"
	+searchStr.toLowerCase()+"%'").list();
	  return list;
	}
	
	public User getUserByName(String name) 
	{
	  List<User> list = (List<User>) sess().createQuery("from User where name='"+name+"'").list();
	  if (list.size()>0)
	  return list.get(0);
	  else return null;
  }

	public void updateUser(User user)
	{
	  sess().update(user);
	}
	
	public void insertUser(String name, String password) {
    User u = new User();
    u.setName(name);
    u.setPassword(password);
    sess().save(u);
  }
	
	
	
	public void deleteUser(long id) {
    sess().delete(getUserById(id));
  }

	
	
	public void createUser(String name) {
    User u = new User();
    u.setName(name);
    sess().save(u);
  }

	
	@SuppressWarnings("unchecked")
  public List<User> getUsers() {
    return sess().createQuery("from User").list();
  }

	
}
