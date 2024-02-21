package model.ADT;

import model.exception.MyException;

import java.util.List;
import java.util.Stack;

public interface IMyStack<T> {
    T pop() throws MyException;
    void push (T v);

    boolean isEmpty();
    public Stack<T> getContent();
    List<T> getReversed();

}
