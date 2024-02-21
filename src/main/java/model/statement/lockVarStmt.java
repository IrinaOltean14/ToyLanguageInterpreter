package model.statement;

import model.ADT.IMyDictionary;
import model.ADT.IMyLockTable;
import model.ADT.IMyStack;
import model.PrgState;
import model.exception.MyException;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class lockVarStmt implements IStmt{
    private String var;
    private static Lock lock = new ReentrantLock();

    public lockVarStmt(String var){
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        IMyLockTable lockTable = state.getLockTable();
        IMyDictionary<String, Value> symTable = state.getDictionary();
        if (symTable.isDefined(var)) {
            if (symTable.lookup(var).getType().equals(new IntType())) {
                IntValue index = (IntValue) symTable.lookup(var);
                int foundIndex = index.getVal();
                //System.out.println(foundIndex);
                if (lockTable.containsKey(foundIndex)) {
                    if (lockTable.get(foundIndex) == -1) {
                        lockTable.update(foundIndex, state.getId());
                    }
                    else {
                        state.getStack().push(this);
                    }
                }
                else {
                    throw new MyException("Index is not in the lock table!");
                }
            }
            else {
                throw new MyException("Var must be of int type!");
            }
        }
        else {
            throw new MyException("Var is not defined!");
        }
        lock.unlock();
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new lockVarStmt(var);
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
        return "lock("+var+")";
    }
}
