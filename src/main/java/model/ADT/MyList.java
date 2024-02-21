package model.ADT;

import model.exception.MyException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
public class MyList<T> implements IMyList<T> {

    private List<T> list;
    public MyList(){
        this.list = new ArrayList<T>();
    }

    @Override
    public void add(T item) {
        this.list.add(item);
    }

    @Override
    public void remove(T item) throws MyException {
        try{
            this.list.remove(item);
        }
        catch (NoSuchElementException e){
            throw new MyException("No such element");
        }
    }

    @Override
    public List<T> getContent() {
        return list;
    }

    @Override
    public String toString() {
        String representation = "";
        for(T elem: list){
            representation += (elem.toString() + "\n");
        }

        return representation;
    }
}
