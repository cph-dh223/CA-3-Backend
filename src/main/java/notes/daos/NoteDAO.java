package notes.daos;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import notes.ressources.Note;

public class NoteDAO extends ADAO<Note, String> {

    public NoteDAO(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public List<Note> getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }
    public List<Note> getAll(String email) {
        try(EntityManager em = emf.createEntityManager()){
            var query = em.createQuery("SELECT n FROM Note n JOIN User u WHERE u.email = :email",Note.class);
            query.setParameter("email", email);
            return query.getResultList();
        }
    }

    @Override
    public Note getById(String id) {
        try(EntityManager em = emf.createEntityManager()){
            return em.find(Note.class, id);
        }
    }

    @Override
    public Note update(Note note) {
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.merge(note);
            em.getTransaction().commit();
        }
        return note;
    }
    
}
