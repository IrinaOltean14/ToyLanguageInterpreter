package model.statement;

import model.ADT.IMyDictionary;
import model.ADT.IMyStack;
import model.PrgState;
import model.exception.MyException;
import model.type.Type;

public class CompStmt implements IStmt{
    IStmt first;
    IStmt second;

    public CompStmt(IStmt first, IStmt second){
        this.first = first;
        this.second = second;
    }

    public IStmt getFirst(){
        return this.first;
    }

    public IStmt getSecond(){
        return this.second;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IMyStack<IStmt> stack = state.getStack();
        stack.push(second);
        stack.push(first);
        state.setExecutionStack(stack);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new CompStmt(first.deepCopy(), second.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws MyException {
        //MyIDictionary<String,Type> typEnv1 = first.typecheck(typeEnv);
        //MyIDictionary<String,Type> typEnv2 = second.typecheck(typEnv1);
        //return typEnv2;
        return second.typecheck(first.typecheck(typeEnv));
    }

    @Override
    public String toString(){
        return "(" + first.toString() + ";" + second.toString() + ")";
    }
}
