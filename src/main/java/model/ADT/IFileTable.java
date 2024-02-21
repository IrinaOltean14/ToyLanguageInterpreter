package model.ADT;

import model.exception.MyException;

import java.util.Map;
public interface IFileTable<K, V> {
    public V lookup(K key);
    public void update(K key, V value);
    public void remove(K key);
    boolean isDefined(K key);
    void add(K key, V value) throws MyException;
    public Map<K, V> getContent();
}
