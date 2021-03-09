/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitnatriedazdatabazy;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author vsa
 */
public class EntitnaTriedaZDatabazy {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EntitnaTriedaZDatabazyPU");
        EntityManager em = emf.createEntityManager();
        
        //TypedQuery<Osoba> tq = em.createNamedQuery("pokus", Osoba.class); // zavola TypedQuery z triedy Osoba.java
        //TypedQuery<Osoba> tq = em.createQuery("SELECT o FROM Osoba o WHERE o.narodena != null", Osoba.class); //=>alternativa ^
        /**
         ** Metody
         * flush() podobne ako commit
         * refresh() ak sa v pamati zmenia hodnoty, tak refresh obnovi hodnoty v pamati na tie, ktore su ulozene v databaze
         * remove() odstrani zaznam z databazy (az po commite), ale ponechava tento zaznam v pamati
         * merge() mergne pamat a databazu, vyhlada v databaze zaznam a aktualizuje jeho stav *PRIKLAD MERGE()*
         **/
        //tq.setParameter("id", 2);
        /*List<Osoba> l = tq.getResultList();
        for(Osoba o: l){
            System.out.println(o);
        }*/
        
        /**PRIKLAD MERGE()**/
        em.getTransaction().begin();
        Osoba obj704 = new Osoba();
        obj704.setId(1L);
        obj704.setMeno("Shooter");
        
        Osoba jagdtiger =  em.merge(obj704);  // merge() = find(), setAttribute(), commit()
        System.out.println(obj704);
        System.out.println(jagdtiger);
        em.getTransaction().commit();
    }

    public void persist(Object object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EntitnaTriedaZDatabazyPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
}
