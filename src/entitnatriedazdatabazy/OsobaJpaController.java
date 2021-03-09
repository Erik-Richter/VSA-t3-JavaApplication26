/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitnatriedazdatabazy;

import entitnatriedazdatabazy.exceptions.NonexistentEntityException;
import entitnatriedazdatabazy.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author vsa
 */
public class OsobaJpaController implements Serializable {

    public OsobaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Osoba osoba) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(osoba);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOsoba(osoba.getId()) != null) {
                throw new PreexistingEntityException("Osoba " + osoba + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Osoba osoba) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            osoba = em.merge(osoba);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = osoba.getId();
                if (findOsoba(id) == null) {
                    throw new NonexistentEntityException("The osoba with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Osoba osoba;
            try {
                osoba = em.getReference(Osoba.class, id);
                osoba.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The osoba with id " + id + " no longer exists.", enfe);
            }
            em.remove(osoba);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Osoba> findOsobaEntities() {
        return findOsobaEntities(true, -1, -1);
    }

    public List<Osoba> findOsobaEntities(int maxResults, int firstResult) {
        return findOsobaEntities(false, maxResults, firstResult);
    }

    private List<Osoba> findOsobaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Osoba.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Osoba findOsoba(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Osoba.class, id);
        } finally {
            em.close();
        }
    }

    public int getOsobaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Osoba> rt = cq.from(Osoba.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
