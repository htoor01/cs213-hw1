package chess;

public class King extends Piece {
    //inherits color from Piece
    public King(Chess.Player color) {
        super(color);
    }

    public boolean validMove(Board board, int startRow, int startCol, int endRow, int endCol) {

        int diffRow = Math.abs(endRow - startRow);
        int diffCol = Math.abs(endCol - startCol);

        //King can only move exactly one square
        if (diffRow > 1 || diffCol > 1) 
            return false;

        //end square must be empty or enemy
        if (!(board.checkEmpty(endRow, endCol) || board.checkEnemy(endRow, endCol, color))) {
            return false;
        }

        return true;
    }

}