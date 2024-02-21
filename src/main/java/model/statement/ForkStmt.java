package model.statement;

import model.ADT.IMyDictionary;
import model.ADT.IMyStack;
import model.ADT.MyStack;
import model.PrgState;
import model.exception.MyException;
import model.type.Type;

public class ForkStmt implements IStmt{
    private IStmt stmt;

    public ForkStmt(IStmt s){
        stmt = s;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IMyStack<IStmt> newStack = new MyStack<>();
        return new PrgState(newStack, state.getDictionary().deepCopy(), state.getList(), state.getFileTable(), state.getHeap(), stmt, state.getLockTable());
    }

    @Override
    public IStmt deepCopy() {
        return new ForkStmt(stmt.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws MyException {
        stmt.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString(){
        return "fork("+stmt.toString()+")";
    }
}
