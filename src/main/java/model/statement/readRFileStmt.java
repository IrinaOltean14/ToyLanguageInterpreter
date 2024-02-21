package model.statement;

import model.ADT.IFileTable;
import model.ADT.IMyDictionary;
import model.ADT.IMyHeap;
import model.PrgState;
import model.exception.MyException;
import model.expression.Exp;
import model.type.IntType;
import model.type.StringType;
import model.type.Type;
import model.value.IntValue;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class readRFileStmt implements IStmt{
    Exp exp;
    private String varName;

    public readRFileStmt(Exp exp, String varName){
        this.exp = exp;
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IMyDictionary<String, Value> symTable = state.getDictionary();
        IFileTable<StringValue, BufferedReader> fileTable = state.getFileTable();
        IMyHeap heap = state.getHeap();
        if (symTable.isDefined(varName)){
            if (symTable.lookup(varName).getType().equals(new IntType())){
                Value val = exp.eval(symTable, heap);
                if (val.getType().equals(new StringType())){
                    StringValue stringVal = (StringValue) val;
                    if (fileTable.isDefined(stringVal)){
                        BufferedReader bufferedReader = fileTable.lookup(stringVal);
                        try{
                            String line = bufferedReader.readLine();
                            Value intVal;
                            IntType type = new IntType();
                            if (line == null){
                                intVal = type.defaultValue();
                            }
                            else{
                                intVal = new IntValue(Integer.parseInt(line));
                            }
                            symTable.update(varName, intVal);
                        }
                        catch (IOException e) {
                            throw new MyException(e.getMessage());
                        }
                    }
                    else{
                        throw new MyException("The file " + stringVal.getValue()+ " is not in the file table");
                    }
                }
                else {
                    throw new MyException("The value could not be evaluated to a string value");
                }
            }
            else{
                throw new MyException(varName + " is not of type int");
            }
        }
        else{
            throw new MyException(varName + " is not defined in the Sym Table");
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new readRFileStmt(exp.deepCopy(), new String(varName));
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(varName);
        if (!typevar.equals(new IntType()))
            throw new MyException("read file stmt: " + varName + " is not an integer");
        Type typ = exp.typecheck(typeEnv);
        if (!typ.equals(new StringType()))
            throw new MyException("read file stmt: the file path is not a string");
        return typeEnv;
    }

    @Override
    public String toString(){
        return "Read from " + exp + " into " + varName;
    }
}
