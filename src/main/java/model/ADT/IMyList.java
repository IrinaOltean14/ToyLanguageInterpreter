package model.ADT;
import model.exception.MyException;

import java.util.List;

public interface IMyList<T> {
    void add(T item);
    void remove(T item) throws MyException;

    List<T> getContent();
}
