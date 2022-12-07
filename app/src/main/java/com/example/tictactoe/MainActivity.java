package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView boardImageView;
    BoardLogic boardLogic;
    List<ImageView> turnsViews;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardLogic = new BoardLogic();
        turnsViews = new ArrayList<ImageView>();
        boardImageView = findViewById(R.id.boardImageView);

        boardImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int row = calculateRow(event.getY());
                    int col = calculateColumn(event.getX());


                    if (boardLogic.getCurrentWinningLines().size() == 0 && boardLogic.playTurn(row, col)) {
                        GameStatus gameStatus = null;
                        Player currentPlayer = boardLogic.getCurrentPlayer();

                        drawTurn(col, row, boardLogic.getCurrentPlayer());
                        final List<WinType> currentWinningLines = boardLogic.getCurrentWinningLines();
                        if (currentWinningLines.size() != 0) {
                            switch (currentPlayer){
                                case X:
                                    gameStatus = GameStatus.X_WIN;
                                    break;
                                case O:
                                    gameStatus = GameStatus.O_WIN;
                                    break;
                            }
                            drawWinningLines(currentWinningLines, row, col);
                            addPlayAgainButton();
                        } else {
                            if (boardLogic.isTie()) {
                                gameStatus = GameStatus.NO_WIN;
                                addPlayAgainButton();
                            } else {
                                switch (currentPlayer){
                                    case X:
                                        gameStatus = GameStatus.X_PLAY;
                                        break;
                                    case O:
                                        gameStatus = GameStatus.O_PLAY;
                                        break;
                                }


                                boardLogic.changePlayerTurn();
                            }
                        }

                        drawGameStatus(gameStatus);
                    }
                }

                return true;
            }
        });

        findViewById(R.id.playAgainButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });
    }

    private void newGame() {
        boardLogic = new BoardLogic();

        switch (boardLogic.getCurrentPlayer()){
            case X:
                drawGameStatus(GameStatus.X_PLAY);
                break;
            case O:
                drawGameStatus(GameStatus.O_PLAY);
                break;
        }

        clearLayouts();
    }

    private void drawWinningLines(List<WinType> winTypes, int row, int col) {
        winTypes.forEach(winType -> drawWinningLine(winType, row,col));
    }

    private void drawWinningLine(WinType winType, int row, int col) {

        int imageId = 0;

        switch (winType) {
            case COL:
                final int[] colIds = {R.id.leftcol, R.id.midcol, R.id.rightcol};
                imageId = colIds[col];
                break;
            case ROW:
                final int[] rowIds = {R.id.toprow, R.id.midrow, R.id.bottomrow};
                imageId = rowIds[row];
                break;
            case REGULAR_DIAGONAL:
                imageId=R.id.diagonal;
                break;
            case OPPOSITE_DIAGONAL:
                imageId=R.id.opositediagonal;
                break;
        }

        ImageView img = (ImageView) findViewById(imageId);
        img.setVisibility(View.VISIBLE);
    }

    private void clearWinningLines() {
        final List<Integer> lineIds = new ArrayList<Integer>(Arrays.asList(
                R.id.leftcol, R.id.midcol, R.id.rightcol,
                R.id.toprow, R.id.midrow, R.id.bottomrow,
                R.id.diagonal,
                R.id.opositediagonal
        ));
        lineIds.forEach(lineId -> findViewById(lineId).setVisibility(View.INVISIBLE));
    }

    private void drawGameStatus(GameStatus gameStatus) {
        int imageResource = 0;

        switch (gameStatus) {
            case O_PLAY:
                imageResource = R.drawable.oplay;
                break;
            case X_PLAY:
                imageResource = R.drawable.xplay;
                break;
            case O_WIN:
                imageResource = R.drawable.owin;
                break;
            case X_WIN:
                imageResource = R.drawable.xwin;
                break;
            case NO_WIN:
                imageResource = R.drawable.nowin;
                break;
        }

        ImageView imageView=new ImageView(this);
        imageView.setImageResource(imageResource);

        ImageView img = (ImageView) findViewById(R.id.currentTurnView);
        if(img.getVisibility() != View.VISIBLE) img.setVisibility(View.VISIBLE);
        img.setImageResource(imageResource);
    }

    private void addViewToLayout(ImageView view) {
        FrameLayout layout = (FrameLayout)findViewById(R.id.root);
        layout.addView(view);
    }

    private void clearLayouts() {
        FrameLayout layout = (FrameLayout)findViewById(R.id.root);
        turnsViews.forEach(layout::removeView);

        turnsViews.clear();

        removePlayAgainButton();
        clearWinningLines();
    }

    private void addPlayAgainButton() {
        findViewById(R.id.playAgainButton).setVisibility(View.VISIBLE);
    }

    private void removePlayAgainButton() {
        findViewById(R.id.playAgainButton).setVisibility(View.INVISIBLE);
    }

    private void drawTurn(int x, int y, Player player) {
        final int IMAGE_SIZE = boardImageView.getHeight()/boardLogic.BOARD_SIZE - 50;
        final int X_BASE_OFFSET = 175;
        final int Y_BASE_OFFSET = 185;
        final int STEP_OFFSET = boardImageView.getHeight()/boardLogic.BOARD_SIZE;

        int imageResource = 0;

        switch (player) {
            case O:
                imageResource = R.drawable.o;
                break;
            case X:
                imageResource = R.drawable.x;
                break;
        }

        ImageView imageView=new ImageView(this);
        imageView.setImageResource(imageResource);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(IMAGE_SIZE,IMAGE_SIZE);
        params.setMargins(X_BASE_OFFSET + x * STEP_OFFSET,Y_BASE_OFFSET + y * STEP_OFFSET,0,0); // setting the margin in the layout
        imageView.setLayoutParams(params);
        turnsViews.add(imageView);
        addViewToLayout(imageView);
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