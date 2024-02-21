package model.expression;

import model.ADT.IMyDictionary;
import model.ADT.IMyHeap;
import model.exception.MyException;
import model.type.Type;
import model.value.Value;

public class VarExp implements Exp{
    private String id;

    public VarExp(String id){
        this.id = id;
    }
    @Override
    public Value eval(IMyDictionary<String, Value> table, IMyHeap heap) throws MyException {
        return table.lookup(id);
    }

    @Override
    public Exp deepCopy() {
        return new VarExp(new String(id));
    }

    @Override
    public Type typecheck(IMyDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv.lookup(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
