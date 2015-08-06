package mx.gob.conavi.sniiv.sqlite;


/**
 * Created by admin on 03/08/15.
 */
public interface Repository<T> {
    void saveAll(T[] elementos);
    void deleteAll();
    T[] loadFromStorage();
}
