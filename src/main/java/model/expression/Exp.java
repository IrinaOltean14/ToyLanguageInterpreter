package model.expression;

import model.ADT.IMyDictionary;
import model.ADT.IMyHeap;
import model.exception.MyException;
import model.type.Type;
import model.value.Value;

public interface Exp {
    Value eval(IMyDictionary<String, Value> table, IMyHeap heap) throws MyException;

    Exp deepCopy();

    Type typecheck(IMyDictionary<String, Type> typeEnv) throws MyException;
}
