package bau.kokany.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class JPAExpertDAO implements ExpertDAO {

    final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("br.com.fredericci.pu");
    final EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public void saveExpert(Expert e) {
        entityManager.getTransaction().begin();
        entityManager.persist(e);
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteExpert(Expert e) {
        entityManager.getTransaction().begin();
        entityManager.remove(e);
        entityManager.getTransaction().commit();
    }

    @Override
    public void updateExpert(Expert e) {
        saveExpert(e);
    }

    @Override
    public List<Expert> getExperts() {
        TypedQuery<Expert> query = entityManager.createQuery("SELECT e FROM Expert e", Expert.class);
        List<Expert> experts = query.getResultList();
        return experts;
    }

    @Override
    public void close() throws Exception {
        entityManager.close();
        entityManagerFactory.close();
    }
}
