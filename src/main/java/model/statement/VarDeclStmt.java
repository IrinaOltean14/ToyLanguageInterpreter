package model.statement;

import model.ADT.IMyDictionary;
import model.ADT.IMyStack;
import model.PrgState;
import model.exception.MyException;
import model.type.Type;
import model.value.Value;

public class VarDeclStmt implements IStmt{
    String name;
    Type typ;

    public VarDeclStmt(String name, Type typ){
        this.name = name;
        this.typ = typ;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IMyStack<IStmt> stack = state.getStack();
        IMyDictionary<String, Value> table = state.getDictionary();
        if (table.isDefined(name))
            throw new MyException("The variable " + name + " is already defined");
        else{
            table.add(name, typ.defaultValue());
        }
        state.setExecutionStack(stack);
       state.setSymTable(table);
       return null;
    }

    @Override
    public IStmt deepCopy() {
        return new VarDeclStmt(new String(name), typ.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws MyException {
        typeEnv.add(name, typ);
        return typeEnv;

    }


    @Override
    public String toString(){
        return typ + " " + name;
    }
}
