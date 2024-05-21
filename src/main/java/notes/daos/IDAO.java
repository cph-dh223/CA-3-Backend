package notes.daos;

import java.util.List;

interface IDAO<T, K> {
    public List<T> getAll();
    public T getById(K id);
    public T create(T t);
    public T update(T t);
    public void delete(T t);
}