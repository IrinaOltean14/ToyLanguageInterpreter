package model.statement;

import model.ADT.IMyDictionary;
import model.ADT.IMyLockTable;
import model.PrgState;
import model.exception.MyException;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class newLockStmt implements IStmt{
    private String var;
    public static Lock lock = new ReentrantLock();

    public newLockStmt(String var){
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        IMyLockTable lockTable = state.getLockTable();
        IMyDictionary<String, Value> symTable = state.getDictionary();
        int freeAddress = lockTable.getFreeValue();
        lockTable.put(freeAddress, -1);
        //System.out.println(lockTable);
        if (symTable.isDefined(var)&&symTable.lookup(var).getType().equals(new IntType()))
            symTable.update(var, new IntValue(freeAddress));
        else{
            throw new MyException("Variable not declared!");
        }
        lock.unlock();
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new newLockStmt(var);
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws MyException {
        if (typeEnv.lookup(var).equals(new IntType())) {
            return typeEnv;
        }
        else {
            throw new MyException("Var must be of type int!");
        }
    }

    @Override
    public String toString(){
        return "newLock(" + var + ")";
    }
}
