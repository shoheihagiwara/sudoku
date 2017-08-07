package com.shoheihagiwara.sudokusolver.lib;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by shohei on 2017/01/24.
 */
public class SudokeSolver {

    public static String sudokuToString(int[][] sudoku) {
        StringBuffer sb = new StringBuffer();
        for (int y=0;y<=8;y++) {
            for (int x=0;x<=8;x++) {
                sb.append(Integer.toString(sudoku[y][x]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static boolean solve(int[][] sudoku, boolean[][] filled, CellPosition currCellPos) {

        int x = currCellPos.getX();
        int y = currCellPos.getY();

        // セルが埋まってる場合
        if(filled[y][x]) {
            return SudokeSolver.goFurtherOrCheckSolved(sudoku, filled, currCellPos);
        }

        // セルが空の場合
        for(int i=1; i <= 9; i++) {
            // 1-9を1つずつ試す。
            // ただし、入れる前に、もう同じ番号が縦、横、3x3に入っているかチェックする。
            if(!SudokeSolver.hasNumberInRowOrColOrNine(sudoku, currCellPos, i)) {
                // 入っていればスキップする
                continue;
            }

            // 同じ番号が縦、横、3x3に入っていないので、入れる。
            sudoku[y][x] = i;
            if(SudokeSolver.goFurtherOrCheckSolved(sudoku, filled, currCellPos)) {
                return true;
            }
        }
        sudoku[y][x] = 0;
        return false;
    }

    private static boolean hasNumberInRowOrColOrNine(int[][] sudoku, CellPosition currCellPos, int num) {

        int curr_x = currCellPos.getX();
        int curr_y = currCellPos.getY();

        for(int i=0; i <=8; i++) {

            // たて
            if(sudoku[curr_y][i] == num) {
                return true;
            }

            // よこ
            if(sudoku[i][curr_x] == num) {
                return true;
            }
        }

        // 3 x 3
        int x_for_nine = (curr_x/3)/3;
        int y_for_nine = (curr_y/3)/3;
        if (sudoku[y_for_nine][x_for_nine] == num
                || sudoku[y_for_nine][x_for_nine+1] == num
                || sudoku[y_for_nine][x_for_nine+2] == num
                || sudoku[y_for_nine+1][x_for_nine] == num
                || sudoku[y_for_nine+1][x_for_nine+1] == num
                || sudoku[y_for_nine+1][x_for_nine+2] == num
                || sudoku[y_for_nine+2][x_for_nine] == num
                || sudoku[y_for_nine+2][x_for_nine+1] == num
                || sudoku[y_for_nine+2][x_for_nine+2] == num ) {
            return true;
        }

        return false;
    }

    private static boolean goFurtherOrCheckSolved(int[][] sudoku, boolean[][] filled, CellPosition currCellPos) {

        if(currCellPos.hasNextCell()) {
            currCellPos.moveToNextCell();
            boolean solutionFound = solve(sudoku, filled, currCellPos);
            currCellPos.moveToPreviousCell();
            return solutionFound;

        }
        return SudokeSolver.isSolved(sudoku);
    }

    private static boolean isSolved(int[][] sudoku) {

        // たてをチェック
        for (int x=0; x <= 8; x++) {

            int total = 0;
            for (int y = 0; y <= 8; y++) {
                total += sudoku[y][x];
            }
            if(total != 45) {
                return false;
            }

        }

        // よこをチェック
        for (int y=0; y <= 8; y++) {

            int total = 0;
            for (int x = 0; x <= 8; x++) {
                total += sudoku[y][x];
            }
            if(total != 45) {
                return false;
            }

        }

        // 9個の 3x3 をチェック
        for (int y=0; y <= 8; y+=3) {
            for (int x = 0; x <= 8; x+=3) {
                int total = 0;
                total += sudoku[y][x];
                total += sudoku[y][x+1];
                total += sudoku[y][x+2];
                total += sudoku[y+1][x];
                total += sudoku[y+1][x+1];
                total += sudoku[y+1][x+2];
                total += sudoku[y+2][x];
                total += sudoku[y+2][x+1];
                total += sudoku[y+2][x+2];
                if(total != 45) {
                    return false;
                }
            }

        }

        return true;
    }

    public static int[][] StringToSudokuTable(String sudokuString) {

        if(sudokuString.length() != 9){
            throw new IllegalArgumentException("Argument row length is not 9. Needs to be 9.");
        }

        int[][] sudoku = new int[9][9];

        String[] rows = sudokuString.split("\n");
        for(int rowNum=0; rowNum<=8; rowNum++) {
            String[] nums = rows[rowNum].split("");
            if(nums.length != 9){
                throw new IllegalArgumentException("Argument column length is not 9. Needs to be 9.");
            }
            for(int colNum=0; colNum<=8; colNum++) {

                int int_num = Integer.parseInt(nums[colNum]);
                if(int_num < 0 || 9 < int_num) {
                    throw new IllegalArgumentException("Number is not between 0 and 9, inclusive.");
                }
                sudoku[rowNum][colNum] = int_num;
            }
        }

        return sudoku;
    }

    public static boolean[][] StringToFilledTable(String sudokuString) {

        if(sudokuString.length() != 9){
            throw new IllegalArgumentException("Argument row length is not 9. Needs to be 9.");
        }

        boolean[][] filled = new boolean[9][9];

        String[] rows = sudokuString.split("\n");
        for(int rowNum=0; rowNum<=8; rowNum++) {
            String[] nums = rows[rowNum].split("");
            if(nums.length != 9){
                throw new IllegalArgumentException("Argument column length is not 9. Needs to be 9.");
            }
            for(int colNum=0; colNum<=8; colNum++) {

                int int_num = Integer.parseInt(nums[colNum]);
                if(int_num < 0 || 9 < int_num) {
                    throw new IllegalArgumentException("Number is not between 0 and 9, inclusive.");
                }

                filled[rowNum][colNum] = int_num == 0 ? false: true;
            }
        }

        return filled;
    }

    public static void main(String[] args) {

        String sudokuTable = "014608270\n702409108\n680201035\n953000742\n000000000\n128000653\n870902016\n201503907\n096107520";

        int[][] sudoku = StringToSudokuTable(sudokuTable);
        boolean[][] filled = StringToFilledTable(sudokuTable);

        solve(sudoku, filled, new CellPosition(0,0));

        System.out.print(Arrays.deepToString(sudoku));

    }

}
