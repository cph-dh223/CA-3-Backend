package notes.daos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;

@Getter
public abstract class ADAO<T, K> implements IDAO<T, K> {
    protected EntityManagerFactory emf;
    protected Map<K,T> entityMap;

    public ADAO(EntityManagerFactory emf) {
        this.emf = emf;
        entityMap = new HashMap<>();
    }

    @Override
    public T create(T t) {
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
        }
        return t;
    }
    
    @Override
    public void delete(T t) {
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.remove(t);
            em.getTransaction().commit();
        }
    }
}
