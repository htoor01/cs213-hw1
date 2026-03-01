package chess;

public class Knight extends Piece {
    
    public Knight(Chess.Player color) {
        super(color);
    }

    public boolean validMove(Board board, int startRow, int startCol, int endRow, int endCol) {
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);
        
        // Knight moves in L-shape: 2 squares in one direction, 1 in perpendicular
        if (!((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2))) {
            return false;
        }
        
        // End square must be empty or enemy
        return board.checkEmpty(endRow, endCol) || board.checkEnemy(endRow, endCol, color);
    }
}
