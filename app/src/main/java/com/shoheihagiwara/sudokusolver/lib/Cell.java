package com.shoheihagiwara.sudokusolver.lib;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Cell{

    private int x;
    private int y;
    
    private Set<Integer> cellVals;
    
    public Cell() {
        this.cellVals = new HashSet<Integer>();
        return;
    }
    
    public Cell(Collection<Integer> vals) {
        this();
        this.cellVals.addAll(vals);
    }
    
    public Set<Integer> getVals() {
        return this.cellVals;
    }
    
    public int[] getValsInIntArray() {
        Integer[] vals = this.getVals().toArray(new Integer[0]);
        
        int[] valsIntArray = new int[vals.length];
        
        for (int i=0; i<vals.length; i++) {
            valsIntArray[i] = vals[i];
        }
        
        return valsIntArray;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }

    public int size() {
        return this.cellVals.size();
    }
    
    public int getVal() {
        if (this.size() != 1) {
            throw new IllegalStateException("getVal works only when there is one number in a cell. There are more than one number.");
        }
        return (Integer)this.getVals().toArray()[0];
   }

    
    public boolean remove(int val) {
        return this.cellVals.remove(val);
    }
    
    public boolean contains(int val) {
        return this.cellVals.contains(val);
    }

    public void set(int val) {
        this.clear();
        this.add(val);
    }
    
    public void clear() {
        this.cellVals.clear();
    }
    
    public void setAll(int... vals) {
        this.clear();
        this.addAll(vals);
    }
    
    public void setAll(Collection<Integer> valCollection) {
        this.clear();
        for (int val : valCollection) {
            this.add(val);
        }
    }
    
    
    public boolean add(int val) {
        return this.cellVals.add(val);
    }
    
    public boolean addAll(int... vals) {
        boolean changed = false;
        for (int val : vals) {
            changed = this.cellVals.add(val) || changed;
        }
        return changed;
    }
    
    public String getStringVal() {
        
        if(this.size() == 1) {
            return String.valueOf(this.getVal());
        }
        
        return this.getVals().toString();
        
    }
}
