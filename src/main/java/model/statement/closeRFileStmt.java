package model.statement;

import model.ADT.IFileTable;
import model.ADT.IMyDictionary;
import model.ADT.IMyHeap;
import model.PrgState;
import model.exception.MyException;
import model.expression.Exp;
import model.type.StringType;
import model.type.Type;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class closeRFileStmt implements IStmt{
    Exp exp;

    public closeRFileStmt(Exp exp){
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IMyDictionary<String, Value> symTable = state.getDictionary();
        IMyHeap heap = state.getHeap();
        Value val = exp.eval(symTable, heap);
        if (val.getType().equals(new StringType())){
            StringValue stringVal = (StringValue) val;
            IFileTable<StringValue, BufferedReader> fileTable = state.getFileTable();
            if (fileTable.isDefined(stringVal)){
                BufferedReader bufferedReader = fileTable.lookup(stringVal);
                try{
                    bufferedReader.close();
                }
                catch (IOException e){
                    throw new MyException(e.getMessage());
                }
                fileTable.remove(stringVal);
            }
            else{
                throw new MyException("The file is not in the file table");
            }
        }
        else{
            throw new MyException("Expression could not be evaluated to a string");
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new closeRFileStmt(exp.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws MyException {
        Type typ = exp.typecheck(typeEnv);
        if (typ.equals(new StringType()))
            return typeEnv;
        else
            throw new MyException("CloseRF stmt: the expresion could not be evaluated to a string");
    }

    @Override
    public String toString(){
        return "close(" + exp + ")";
    }
}
