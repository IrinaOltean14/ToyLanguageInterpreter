package model.expression;

import model.ADT.IMyDictionary;
import model.ADT.IMyHeap;
import model.exception.MyException;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;

public class HeapReadingExp implements Exp{
    Exp expression;

    public HeapReadingExp(Exp e){
        this.expression = e;
    }

    @Override
    public Value eval(IMyDictionary<String, Value> table, IMyHeap heap) throws MyException {
        Value expValue = expression.eval(table, heap);
        if (!(expValue.getType() instanceof RefType)){
            throw new MyException("Expression not evaluated to RefType");
        }
        RefValue val = (RefValue) expValue;
        int address = val.getAddr();
        if (!heap.containsKey(address)){
            throw new MyException("The address is not a key in the heap");
        }
        return heap.lookup(address);
    }

    @Override
    public Exp deepCopy() {
        return null;
    }

    @Override
    public Type typecheck(IMyDictionary<String, Type> typeEnv) throws MyException {
        Type typ = expression.typecheck(typeEnv);
        if (typ instanceof RefType){
            RefType reft = (RefType) typ;
            return reft.getInner();
        }
        else
            throw new MyException("the rH argument is not a Ref Type");
    }

    @Override
    public String toString(){
        return "rH(" + expression + ")";
    }
}
