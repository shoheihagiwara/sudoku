package com.shoheihagiwara.sudokusolver.lib;

import android.app.Activity;
import android.content.res.Resources;

import com.shoheihagiwara.sudokusolver.MainActivity;
import com.shoheihagiwara.sudokusolver.R;

/**
 * Created by shohei on 2017/01/23.
 */
public class CellPosition {

    private int x;
    private int y;

    public CellPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void moveToNextCell(){

        // 最後のセルにいたら、このメソッドは呼んではいけないのでエラーとする
        if(this.x==8 && this.y==8) {
            throw new IllegalStateException(MainActivity.getMyResource().getString(R.string.cell_position_move_to_next_cell_error_message));
        }

        // 最後のセルではないので、1つ進める。
        this.x++;

        // もし8を超えたら、次の行に移る。yは
        if(this.x == 9) {
            this.x = 0;
            this.y++;
        }
    }

    public void moveToPreviousCell(){

        // 最初のセルにいたら、このメソッドは呼んではいけないのでエラーとする
        if(this.x==0 && this.y==0) {
            throw new IllegalStateException(MainActivity.getMyResource().getString(R.string.cell_position_move_to_previous_cell_error_message));
        }

        // 最初のセルではないので、1つ戻る。
        this.x--;

        // もし0を下回ったら、前の行に移る。
        if(this.x == -1) {
            this.x = 8;
            this.y--;
        }
    }


    public boolean hasNextCell() {
        if (x==8 && y==8) {
            return false;
        }
        return true;
    }
}
