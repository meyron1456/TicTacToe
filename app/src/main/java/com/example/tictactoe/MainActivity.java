package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView boardImageView;
    BoardLogic boardLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardLogic = new BoardLogic();
        boardImageView = findViewById(R.id.boardImageView);

        boardImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int row = calculateRow(event.getY());
                    int col = calculateColumn(event.getX());
                    if (boardLogic.playTurn(row, col)) {
                        if (boardLogic.hasWinner()) {
                            // TODO: announce winner by the current player
                            // TODO: draw "play again" button
                            boardLogic.getCurrentPlayer();
                        } else {
                            if (boardLogic.isTie()) {
                                // TODO: announce tie
                                // TODO: draw "play again" button
                            }
                            // TODO draw board
                            // TODO draw player turn
                            boardLogic.changePlayerTurn();
                        }
                    }
                }

                return true;
            }
        });
    }

    private int calculateRow(float y) {
        float boardHeight = boardImageView.getHeight();
        float cellHeightSize = boardHeight / boardLogic.BOARD_SIZE;

        return (int) (y / cellHeightSize);
    }

    private int calculateColumn(float x) {
        float boardWidth = boardImageView.getWidth();
        float cellWidthSize = boardWidth / boardLogic.BOARD_SIZE;

        return (int) (x / cellWidthSize);
    }
}