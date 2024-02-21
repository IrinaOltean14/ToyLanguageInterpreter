package model.statement;

import model.ADT.IMyDictionary;
import model.PrgState;
import model.exception.MyException;
import model.type.Type;

public interface IStmt {
    PrgState execute(PrgState state) throws MyException;
    IStmt deepCopy();

    IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws MyException;
}
