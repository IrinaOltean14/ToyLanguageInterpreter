package model.expression;

import model.ADT.IMyDictionary;
import model.ADT.IMyHeap;
import model.exception.MyException;
import model.type.Type;
import model.value.Value;

public class ValueExp implements Exp{

    private Value e;

    public ValueExp(Value e){
        this.e = e;
    }
    @Override
    public Value eval(IMyDictionary<String, Value> table, IMyHeap heap) throws MyException {
        return e;
    }

    @Override
    public Exp deepCopy() {
        return new ValueExp(e.deepCopy());
    }

    @Override
    public Type typecheck(IMyDictionary<String, Type> typeEnv) throws MyException {
        return e.getType();
    }

    @Override
    public String toString(){
        return e.toString();
    }
}
