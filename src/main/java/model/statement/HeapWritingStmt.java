package model.statement;

import model.ADT.IMyDictionary;
import model.ADT.IMyHeap;
import model.PrgState;
import model.exception.MyException;
import model.expression.Exp;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;

public class HeapWritingStmt implements IStmt{
    private String varName;
    private Exp expression;
    public HeapWritingStmt(String name, Exp e){
        varName = name;
        expression = e;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IMyDictionary<String, Value> symTable = state.getDictionary();
        IMyHeap heap = state.getHeap();
        if (!(symTable.isDefined(varName))){
            throw new MyException("The variable is not defined in the symbol table");
        }

        Value val = symTable.lookup(varName);
        if (!(val.getType() instanceof RefType)){
            throw new MyException("The variable type is not Ref Type");
        }
        RefValue varValue = (RefValue) val;
        int address = varValue.getAddr();

        if (!heap.containsKey(address)){
            throw new MyException("The address is not in the heap");
        }
        Value expValue = expression.eval(symTable, heap);
        RefType t = (RefType) varValue.getType();
        if (! expValue.getType().equals(t.getInner())){
            throw new MyException("Variable and reference do not have the same type");
        }
        heap.update(address, expValue);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new HeapWritingStmt(varName, expression.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(varName);
        Type typexp = expression.typecheck(typeEnv);
        if (!(typevar instanceof  RefType)){
            throw new MyException("heap writing stmt: the variable is not of type ref");
        }
        if (!typevar.equals(new RefType(typexp)))
            throw new MyException("heap writing stmt: inner ref type does not match the expression type");
        return typeEnv;
    }

    @Override
    public String toString(){
        return "wh("+varName+","+expression.toString()+")";
    }
}
