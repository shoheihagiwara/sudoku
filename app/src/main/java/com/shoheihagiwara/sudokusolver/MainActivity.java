package com.shoheihagiwara.sudokusolver;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.shoheihagiwara.sudokusolver.lib.CellPosition;
import com.shoheihagiwara.sudokusolver.lib.SudokeSolver;
import com.shoheihagiwara.sudokusolver.lib.Sudoku;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static Resources myRes;
    private static long totalTry = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.myRes = getResources();
        setContentView(R.layout.activity_main);
    }

    public static Resources getMyResource() {
        return MainActivity.myRes;
    }

    public void onSolveSudokuButton(View view) {



        EditText input = (EditText) findViewById(R.id.edit_text_input);


        // 解く
        Sudoku sudoku = new Sudoku();
        try {
            sudoku.importFromString(input.getText().toString());
            sudoku.solve();
        } catch (IllegalArgumentException e) {
            input.setText(e.toString());
            return;
        }

        // TODO: キーボードが邪魔なので非表示にする。下のコードではなぜかうまく動かない
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        // ここでテキストに答えを埋め込む
        input.setText("Answer:\n");
        input.append(sudoku.toString());



    }


    private String getStacktrace(Exception e) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter( writer );
        e.printStackTrace(printWriter);
        printWriter.flush();

        return writer.toString();
    }

}
