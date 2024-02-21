package model.ADT;

import java.util.HashMap;

public class MyLockTable implements IMyLockTable{

    private HashMap<Integer, Integer> lockTable;
    private int freeLocation;

    public MyLockTable(){
        lockTable = new HashMap<>();
    }
    @Override
    public int getFreeValue() {
        synchronized (this){
            freeLocation++;
            return freeLocation;
        }

    }

    @Override
    public void put(int key, int value) {
        synchronized (this){
            this.lockTable.put(key, value);
        }

    }

    @Override
    public int get(int key) {
        synchronized (this){
            return lockTable.get(key);
        }
    }

    @Override
    public boolean containsKey(int key) {
        synchronized (this){
            if (this.lockTable.get(key) != null) {
                return true;
            }
            else {
                return false;
            }
        }

    }

    @Override
    public void update(int key, int value) {
        synchronized (this){
            this.lockTable.put(key, value);
        }

    }

    @Override
    public void setContent(HashMap<Integer, Integer> lockTable) {
        synchronized (this){
            this.lockTable = lockTable;
        }

    }

    @Override
    public HashMap<Integer, Integer> getContent() {

        synchronized (this){
            return lockTable;
        }

    }
}
