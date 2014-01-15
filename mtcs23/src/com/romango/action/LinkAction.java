package com.romango.action;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.romango.db.Db;

public class LinkAction extends ActionSupport {

	private static final long serialVersionUID = -2613425890762568273L;

	public String welcome()
	{
		return "welcome";		
	}
	
	public String friends()
	{
	  System.out.println("a change");
	  
	  /*
	  EntityManagerFactory entityManagerFactory =  Persistence.createEntityManagerFactory("testjpa");
    EntityManager em = entityManagerFactory.createEntityManager();
    EntityTransaction userTransaction = em.getTransaction();
    
    userTransaction.begin();
    Customer customer = new Customer();
    customer.setFirstName("Charles");
    customer.setLastName("Dickens");
    customer.setCustType("RETAIL");
    customer.setStreet("10 Downing Street");
    customer.setAppt("1");
    customer.setCity("NewYork");
    customer.setZipCode("12345");
    em.persist(customer);
    userTransaction.commit();
    em.close();
    entityManagerFactory.close();
    */
	  
    Db.test();
    
		return "friends";		
		
	}
	
	public String office()
	{
		return "office";		
	}
}
