package test.derby;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import data.StateDb;

public class DerbyTest 
{
	 //private EntityManager entityManager = EntityManager.getEntityManager();
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("HibernateApp");  
    EntityManager entityManager = emf.createEntityManager();  
    public static void main(String[] args) 
    {
      new DerbyTest();
    }
    
    public DerbyTest()
    {
      listState();	
    }
    
    public void listState() {
        try {
          entityManager.getTransaction().begin();
          @SuppressWarnings("unchecked")
          List<StateDb> StateDbs = entityManager.createQuery("from State").getResultList();
          for (Iterator<StateDb> iterator = StateDbs.iterator(); iterator.hasNext();) {
        	  StateDb state = (StateDb) iterator.next();
            System.out.println(state.getName());
          }
          entityManager.getTransaction().commit();
        } catch (Exception e) {
          entityManager.getTransaction().rollback();
        }
      }
}
