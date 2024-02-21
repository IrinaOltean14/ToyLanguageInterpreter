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

public class unlockStmt implements IStmt{
    private String var;
    private Lock lock = new ReentrantLock();

    public unlockStmt(String var){
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        IMyDictionary<String, Value> symTable = state.getDictionary();
        IMyLockTable lockTable = state.getLockTable();
        if (symTable.isDefined(var) && symTable.lookup(var).getType().equals(new IntType())){
            IntValue foundIndexValue = (IntValue) symTable.lookup(var);
            int foundIndex = foundIndexValue.getVal();
            if (lockTable.containsKey(foundIndex)){
                if (lockTable.get(foundIndex) == state.getId())
                    lockTable.update(foundIndex, -1);
            }
            else
                throw new MyException("foundIndex is not an index in the lock table");
        }
        else
            throw new MyException("Var is not in symbol table or has not an int type");
        lock.unlock();
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new unlockStmt(var);
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws MyException {
        if (typeEnv.lookup(var).equals(new IntType()))
            return typeEnv;
        else
            throw new MyException("Var does not have the type int");
    }

    @Override
    public String toString(){
        return "unlock("+var+")";
    }
}
