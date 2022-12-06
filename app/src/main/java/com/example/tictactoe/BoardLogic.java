package com.example.tictactoe;

public class BoardLogic {
    public final int BOARD_SIZE = 3;
    private CellType [][] board;
    private Player currentPlayer;
    private int movesCounter;


    public BoardLogic() {
        this.board = new CellType[BOARD_SIZE][BOARD_SIZE];
        this.currentPlayer = Player.X;
        this.movesCounter = 0;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.board[i][j] = CellType.EMPTY;
            }
        }
    }

    public CellType[][] getBoard() {
        return this.board;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public boolean playTurn(int row, int col) {
        if (this.board[row][col] != CellType.EMPTY) {
            return false;
        }
        this.board[row][col] = getPlayerCellType(this.currentPlayer);
        this.movesCounter++;

        return true;
    }

    public void changePlayerTurn() {
        this.currentPlayer = this.currentPlayer == Player.X ? Player.O : Player.X;
    }

    public boolean hasWinner() {
        return isRowWin() || isColWin() || isDiagonalWin();
    }

    public boolean isTie() {
        return movesCounter == BOARD_SIZE * BOARD_SIZE;
    }

    private boolean isRowWin() {
        int counter;
        CellType player;
        int row = 0;
        int col;

        while (row < BOARD_SIZE) {
            player = this.board[row][0];
            if (player != CellType.EMPTY) {
                counter = col = 0;
                while (col < BOARD_SIZE) {
                    if (this.board[row][col] == player)
                        counter++;
                    col++;
                }

                if (counter == BOARD_SIZE)
                    return true;
            }
            row++;
        }

        return false;
    }

    private boolean isColWin() {
        int counter;
        CellType player;
        int row;
        int col = 0;

        while (col < BOARD_SIZE) {
            player = this.board[0][col];
            if (player != CellType.EMPTY) {
                counter = row = 0;
                while (row < BOARD_SIZE) {
                    if (this.board[row][col] == player)
                        counter++;
                    row++;
                }

                if (counter == BOARD_SIZE)
                    return true;
            }
            col++;
        }

        return false;
    }

    private boolean isDiagonalWin() {
        return isRegularDiagonalWin() || isOppositeDiagonalWin();
    }

    private boolean isRegularDiagonalWin() {
        CellType player = this.board[0][0];
        int counter = 0;

        if (player != CellType.EMPTY) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (this.board[i][i] == player)
                    counter++;
            }

            if (counter == BOARD_SIZE)
                return true;
        }

        return false;
    }

    private boolean isOppositeDiagonalWin() {
        CellType player = this.board[0][BOARD_SIZE - 1];
        int counter = 0;

        if (player != CellType.EMPTY) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (this.board[i][BOARD_SIZE - i - 1] == player)
                    counter++;
            }

            if (counter == BOARD_SIZE)
                return true;
        }

        return false;
    }

    private CellType getPlayerCellType(Player player) {
        return player == Player.X ? CellType.X : CellType.O;
    }
}
