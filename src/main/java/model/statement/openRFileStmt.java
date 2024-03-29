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
import java.io.FileNotFoundException;
import java.io.FileReader;

public class openRFileStmt implements IStmt {
    private Exp exp;

    public openRFileStmt(Exp exp){
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IMyDictionary<String, Value> symTable = state.getDictionary();
        IMyHeap heap = state.getHeap();
        Value val = exp.eval(symTable, heap);
        if (val.getType().equals(new StringType())){
            IFileTable<StringValue, BufferedReader> fileTable = state.getFileTable();
            StringValue stringVal = (StringValue) val;
            if (!fileTable.isDefined(stringVal)){
                try{
                    BufferedReader fileBuffer = new BufferedReader(new FileReader(stringVal.getValue()));
                    fileTable.add(stringVal, fileBuffer);
                }
                catch (FileNotFoundException e){
                    throw new MyException(e.getMessage());
                }
            }
            else {
                throw new MyException("The file is already in use");
            }
        }
        else{
            throw new MyException("Expression can not be evaluated to a string");
        }
        return null;
    }

    @Override
    public String toString(){
        return "Open(" + exp + ")";
    }

    @Override
    public IStmt deepCopy() {
        return new openRFileStmt(exp.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws MyException {
        Type typ = exp.typecheck(typeEnv);
        if (typ.equals(new StringType()))
            return typeEnv;
        else
            throw new MyException("open RF stmt: expression could not be evaluated to a string type");
    }
}
