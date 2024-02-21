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


public class NewStmt implements IStmt{
    private String varName;
    private Exp expression;

    public NewStmt(String name, Exp e){
        varName = name;
        expression = e;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IMyDictionary<String, Value> symTable = state.getDictionary();
        IMyHeap heap = state.getHeap();
        // check if the name is a valid name in the symtable
        if (!symTable.isDefined(varName)){
            throw new MyException("Variable is not defined!");
        }
        // check if it is of RefType
        Value val = symTable.lookup(varName);
        if (!(val.getType() instanceof RefType)){
            throw new MyException("The type of the variable is not RefType");
        }

        // Evaluate the expression to a value
        Value expValue = expression.eval(symTable, heap);
        Type locationType =((RefType) val.getType()).getInner();

        if (!(expValue.getType().equals(locationType))) {
            throw new MyException("Reference location type does not match expression type!");
        }
        int heapAddr = heap.getFreeAddress();
        heap.put(heapAddr, expValue);
        RefValue refV = (RefValue) val;
        RefValue newRef = new RefValue(heapAddr, ((RefType) refV.getType()).getInner());
        symTable.update(varName, newRef);
        return null;

    }

    @Override
    public IStmt deepCopy() {
        return new NewStmt(varName, expression.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(varName);
        Type typexp = expression.typecheck(typeEnv);
        if (typevar.equals(new RefType(typexp)))
            return typeEnv;
        else
            throw new MyException("NEW stmt: right hand side and left hand side have different types");
    }

    @Override
    public String toString(){
        return "new(" + varName + "," + expression.toString() + ")";
    }
}
