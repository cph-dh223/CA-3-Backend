package notes.daos;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import notes.exceptions.EntityNotFoundException;
import notes.ressources.Note;
import notes.ressources.User;

public class NoteDAO extends ADAO<Note, Integer> {

    public NoteDAO(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public Note create(Note note) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = note.getUsers().parallelStream().findFirst().get();
            user = em.find(User.class, user.getEmail());
            if (user == null){
                em.getTransaction().rollback();
                //TODO: Some exception handling?
                return null;
            }
            user.addNote(note);
            em.merge(user);
            //em.persist(note);
            em.getTransaction().commit();
        }
        return note;
    }

    @Override
    public List<Note> getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    public List<Note> getAll(String email) {
        try (EntityManager em = emf.createEntityManager()) {
            var query = em.createQuery("SELECT n FROM User u JOIN u.notes n WHERE u.email = :email", Note.class);
            query.setParameter("email", email);
            List<Note> notes = query.getResultList();
            return notes;
        }
    }

    @Override
    public Note getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Note.class, id);
        }
    }

    @Override
    public Note update(Note note) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(note);
            em.getTransaction().commit();
        }
        return note;
    }

}
