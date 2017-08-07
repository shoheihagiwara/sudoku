package com.shoheihagiwara.sudokusolver.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Sudoku {

    private static final int ROW_SIZE = 9;
    private static final int COL_SIZE = 9;
    
    private List<List<Cell>> sudoku = new ArrayList<List<Cell>>();
    
    
    public Cell getCell(int x, int y) {
        
        return sudoku.get(y).get(x);
            
    }
    
    public Set<Integer> getVals(int x, int y) {
        return this.getCell(x, y).getVals();
    }
    
    public boolean isFixed(int x, int y) {
        return sudoku.get(y).get(x).size() == 1 ? true : false;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        
        List<List<Cell>> rows = this.getRows();
        for (List<Cell> row : rows) {
            for (Cell cell : row) {
                sb.append(cell.getStringVal());
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    public void importFromString(String sudokuString) {

        String[] rowsInStr = sudokuString.split("\n");
        if(rowsInStr.length != 9){
            throw new IllegalArgumentException("Argument row length is not 9. Needs to be 9.");
        }
        
        for(int rowNum=0; rowNum < rowsInStr.length; rowNum++) {

            if(rowsInStr[rowNum].length() != 9){
                throw new IllegalArgumentException("Argument column length is not 9. Needs to be 9.");
            }

            String[] tmpNumsInRow = rowsInStr[rowNum].split("");
            String[] numsInRow = new String[9];
            for (int i=0; i< numsInRow.length; i++) {
                numsInRow[i] = String.valueOf(rowsInStr[rowNum].charAt(i));
            }


            List<Cell> row = new ArrayList<Cell>();
            for(int colNum=0; colNum < numsInRow.length; colNum++) {

                int int_num = Integer.parseInt(numsInRow[colNum]);
                if(int_num < 0 || 9 < int_num) {
                    throw new IllegalArgumentException("Number is not between 0 and 9, inclusive.");
                }
                
                Cell cell = new Cell();
                cell.setX(colNum);
                cell.setY(rowNum);
                
                if (int_num == 0) {
                    cell.addAll(1,2,3,4,5,6,7,8,9);
                } else if (1 <= int_num && int_num <= 9 ) {
                    cell.add(int_num);
                    
                }
                row.add(cell);
            }
            
            this.addRow(row);
        }

        return;
    }
    
    public boolean addRow(List<Cell> row) {
        
        return this.sudoku.add(row);
        
    }
    
    public List<Cell> getRow(Cell cell) {
        return this.getRow(cell.getY());
    }
    
    public List<Cell> getRow(int y) {
        
        List<Cell> row = new ArrayList<Cell>();
        for (int x = 0; x < Sudoku.COL_SIZE; x++) {
            row.add(this.getCell(x, y));
        }
        
        return row;
    }
    
    public List<Cell> getColumn(Cell cell) {
        return getColumn(cell.getX());
    }
    
    public List<Cell> getColumn(int x) {
        List<Cell> col = new ArrayList<Cell>();
        for (int y = 0; y < Sudoku.ROW_SIZE; y++) {
            col.add(this.getCell(x, y));
        }
        
        return col;        
    }
    
    public List<List<Cell>> getColumns() {
        List<List<Cell>> cols = new ArrayList<List<Cell>>();
        
        for (int x = 0; x < Sudoku.COL_SIZE; x++) {
            cols.add(this.getColumn(x));
        }
        
        return cols;
    }    
    
    public List<List<Cell>> getRows() {
        List<List<Cell>> rows = new ArrayList<List<Cell>>();
        
        for (int y = 0; y < Sudoku.ROW_SIZE; y++) {
            rows.add(this.getRow(y));
        }
        
        return rows;
    }
    
    public List<Cell> getCells() {
        List<Cell> cells = new ArrayList<Cell>();
        
        for (int y = 0; y < Sudoku.ROW_SIZE; y++) {
            cells.addAll(this.getRow(y));
        }
        return cells;
    }
    
    public List<Cell> getNine(Cell paramCell) {
        
        // 該当セルをsudoku内から探す
        Cell foundCell = null;
        for (Cell cell: this.getCells()) {
            if (cell == paramCell) {
                foundCell = cell;
            }
        }
        
        // 見つからない場合、終わり。
        if (foundCell == null) {
            return null;
        }
        
        // 見つかったセルを基準に、3x3を取得する
        int x = (foundCell.getX()/3)*3;
        int y = (foundCell.getY()/3)*3;
        
        List<Cell> nine = new ArrayList<Cell>();
        nine.add(this.getCell(x,   y));
        nine.add(this.getCell(x,   y+1));
        nine.add(this.getCell(x,   y+2));
        nine.add(this.getCell(x+1, y));
        nine.add(this.getCell(x+1, y+1));
        nine.add(this.getCell(x+1, y+2));
        nine.add(this.getCell(x+2, y));
        nine.add(this.getCell(x+2, y+1));
        nine.add(this.getCell(x+2, y+2));    
        return nine;
    }
    
    public List<List<Cell>> getNines() {
        
        List<List<Cell>> nines = new ArrayList<List<Cell>>();
        
        for (int y = 0; y < 9; y+=3 ) {
            for (int x = 0; x < 9; x+=3) {
                
                Cell cell = this.getCell(x, y);
                nines.add(this.getNine(cell));
            }
        }
        
        return nines;
    }

    
    public boolean solve() {
        
        while (this.solveByLogic()) {
            
        }
        
        this.solveByBruteForce(this.getCell(0, 0));
   
        return true;
    }
    
    private boolean solveByBruteForce(Cell cell) {
        
        // 今、セルに入っている数を保持
        int[] cellVals = cell.getValsInIntArray();
        
        for (int cellVal : cellVals) {
            // 1つの数字が入ったとしたら、という仮定で他のセルを見ていくが、
            // まずは、その数が入れられるかを見る。
            if (!this.canSetValOnCell(cellVal, cell)) {
                continue;
            }
            
            cell.set(cellVal);
            
            // 最終セルか
            if (this.isLastCell(cell)) {
                // 最終セルの場合
                if (this.isSolved()) {
                    return true;
                }
            } else {
                // 最終セルでない場合
                if (this.solveByBruteForce(this.getNextCell(cell))) {
                    return true;
                }
            }
        }
        
        cell.setAll(cellVals);
        
        return false;
    }
    
    private boolean isSolved() {
        
        // たて
        if (!this.hasEveryNumOnlyOnceForColsRowsOrNines(this.getColumns())) {
            return false;
        }
        
        // よこ
        if (!this.hasEveryNumOnlyOnceForColsRowsOrNines(this.getRows())) {
            return false;
        }
        
        // ナイン
        if (!this.hasEveryNumOnlyOnceForColsRowsOrNines(this.getNines())) {
            return false;
        }
        
        return true;
    }
    
    private boolean hasEveryNumOnlyOnceForColsRowsOrNines(List<List<Cell>> colsRowsOrNines) {
        for (List<Cell> colRowOrNine : colsRowsOrNines) {
            if (!this.hasEveryNumOnlyOnce(colRowOrNine)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean hasEveryNumOnlyOnce(List<Cell> colRowOrNine) {
        int[] countOfNum = new int[9];
        for (Cell cell : colRowOrNine) {
            assert 1 <= cell.getVal() && cell.getVal() <= 9; 
            countOfNum[cell.getVal()-1] += 1; 
        }
        for (int val : countOfNum) {
            if (val != 1) {
                return false;
            }
        }
        return true;
    }
    
    
    private boolean canSetValOnCell(int val, Cell cell) {
        
        // 縦
        if (this.existsCellWithVal(val, this.getColumn(cell), cell)) {
            return false;
        }
        
        // 横
        if (this.existsCellWithVal(val, this.getRow(cell), cell)) {
            return false;
        }
        
        // ナイン
        if (this.existsCellWithVal(val, this.getNine(cell), cell)) {
            return false;
        }
        
        return true;
    }
    
    private boolean existsCellWithVal(int val, List<Cell> cells, Cell cellToExclude) {
        
        for (Cell tmpCell : cells) {
            if (tmpCell == cellToExclude) {
                continue;
            }
            
            if (tmpCell.size() == 1 && tmpCell.getVal() == val) {
                return true;
            }
        }
        
        return false;
        
    }
    
    
    public boolean isLastCell(Cell cell) {
        Cell lastCell = this.getCell(Sudoku.COL_SIZE-1, Sudoku.ROW_SIZE-1);
        return lastCell == cell;
    }
    
    public Cell getNextCell(Cell cell) {
        
        int curr_x = cell.getX();
        int next_x = (curr_x + 1) % Sudoku.COL_SIZE;
        int next_y = (curr_x + 1) / Sudoku.COL_SIZE == 1 ? cell.getY() + 1 : cell.getY();
        
        try {
            return this.getCell(next_x, next_y);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    
    private boolean solveByLogic() {

        // まず、数字が１つに決まっているマスのタテ・ヨコ・ナインには、同じ数字は入らないので候補から消す。
        boolean sudokuChanged = false;
        
        // 各マスをループ
        for (Cell cell : this.getCells()) {
            
            // １つに決まってないセルはスキップ
            if (cell.size() != 1) {
                continue;
            }
            
            // １つにきまっている数字を取り出す
            int val = cell.getVal();
                
            // タテから数字を消す。
            List<Cell> col = this.getColumn(cell);
            sudokuChanged = this.removeValFromCells(val, col, cell) || sudokuChanged;
            
            // ヨコから数字を消す。
            List<Cell> row = this.getRow(cell);
            sudokuChanged = this.removeValFromCells(val, row, cell) || sudokuChanged;
            
            // ナインから数字を消す。
            List<Cell> nine = this.getNine(cell);
            sudokuChanged = this.removeValFromCells(val, nine, cell) || sudokuChanged;
        }
        
        // 各タテ・ヨコ・ナインに対して、1～9が1度しか出現しないかチェック。
        // 1度のみであれば、その数字が確定する。
        
        for (int num = 1; num <= 9; num++) {
            
            // タテ
            sudokuChanged = this.deduceAndConfirmValOnColsRowsOrNines(num, this.getColumns()) || sudokuChanged;
            
            // ヨコ
            sudokuChanged = this.deduceAndConfirmValOnColsRowsOrNines(num, this.getRows()) || sudokuChanged;
            
            // ナイン
            sudokuChanged = this.deduceAndConfirmValOnColsRowsOrNines(num, this.getNines()) || sudokuChanged;
        }
        
        return sudokuChanged;     
    }
    
    private boolean deduceAndConfirmValOnColsRowsOrNines(int val, List<List<Cell>> colsRowsOrNines) {
        boolean changed = false;
        for (List<Cell> cells: colsRowsOrNines) {
            changed = this.deduceAndConfirmVal(val, cells) || changed;
        }
        
        return changed;
    }
    
    private boolean deduceAndConfirmVal(int val, List<Cell> cells) {
        Cell cellToClean = null;
        int matchCount = 0;
        for (Cell cell : cells) {
            // 数がセルに入っているか
            if (cell.contains(val)) {
                // 入っているセルがあったことを記録
                cellToClean = cell;
                matchCount++;
            }
        }
        
        // 数が入っているセルが1でない場合
        if (matchCount != 1) {
            // 確定できないので何もしない
            return false;
        }
        // マッチした数だけがセルにある場合
        if (cellToClean.size() == 1) {
            // 何もしない
            return false;
        }
        // 数を確定する
        cellToClean.set(val);
        return true;
    }

    private boolean removeValFromCells(int val, List<Cell> cells, Cell exceptionCell) {
        boolean removed = false;
        for (Cell cell : cells) {
            if (exceptionCell == cell) {
                continue;
            }
            removed = cell.remove(val) || removed; // 1回以上成功と、0回成功を区別するためOR条件で結ぶ。removeメソッドは先にこないと実行されないので注意。
        }
        return removed;
    }

    
    public static void main(String[] args) {
        
        StringBuffer sudokuString = new StringBuffer();
        // 初級
//        sudokuString.append("014608270\n");
//        sudokuString.append("702409108\n");
//        sudokuString.append("680201035\n");
//        sudokuString.append("953000742\n");
//        sudokuString.append("467305891\n");
//        sudokuString.append("000000000\n");
//        sudokuString.append("870902016\n");
//        sudokuString.append("201503907\n");
//        sudokuString.append("096107520");
        // 中級
//        sudokuString.append("067204100\n");
//        sudokuString.append("800060002\n");
//        sudokuString.append("050070340\n");
//        sudokuString.append("001032000\n");
//        sudokuString.append("472001500\n");
//        sudokuString.append("506700000\n");
//        sudokuString.append("000000000\n");
//        sudokuString.append("123456789\n");
//        sudokuString.append("640000000");
        // 上級1
//        sudokuString.append("050000090\n");
//        sudokuString.append("007000000\n");
//        sudokuString.append("600402000\n");
//        sudokuString.append("090000305\n");
//        sudokuString.append("000801000\n");
//        sudokuString.append("062000001\n");
//        sudokuString.append("700108040\n");
//        sudokuString.append("300000000\n");
//        sudokuString.append("000600980");
        // 上級2
//        sudokuString.append("010402300\n");
//        sudokuString.append("607053002\n");
//        sudokuString.append("002670010\n");
//        sudokuString.append("831000000\n");
//        sudokuString.append("264007000\n");
//        sudokuString.append("000010000\n");
//        sudokuString.append("000000003\n");
//        sudokuString.append("009000450\n");
//        sudokuString.append("000000789");
        // 世界一難しい数独
        sudokuString.append("005300000\n");
        sudokuString.append("800000020\n");
        sudokuString.append("070010500\n");
        sudokuString.append("400005300\n");
        sudokuString.append("010070006\n");
        sudokuString.append("003200080\n");
        sudokuString.append("060500009\n");
        sudokuString.append("004000030\n");
        sudokuString.append("000009700");
        
        Sudoku sudoku = new Sudoku();
        
        
        sudoku.importFromString(sudokuString.toString());
        sudoku.solve();
        System.out.println(sudoku.toString());
        
    }
    
    
}
