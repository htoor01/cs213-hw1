package chess;

public class Pawn extends Piece {
    public boolean hasMoved = false;
    public boolean justMovedTwo = false; // for en passant
    
    public Pawn(Chess.Player color) {
        super(color);
    }

    public boolean validMove(Board board, int startRow, int startCol, int endRow, int endCol) {
        int direction = (color == Chess.Player.white) ? -1 : 1;
        int rowDiff = endRow - startRow;
        int colDiff = Math.abs(endCol - startCol);
        
        // Moving forward
        if (colDiff == 0) {
            // One square forward
            if (rowDiff == direction && board.checkEmpty(endRow, endCol)) {
                return true;
            }
            // Two squares forward from starting position
            if (!hasMoved && rowDiff == 2 * direction) {
                int middleRow = startRow + direction;
                if (board.checkEmpty(middleRow, endCol) && board.checkEmpty(endRow, endCol)) {
                    return true;
                }
            }
            return false;
        }
        
        // Diagonal capture
        if (colDiff == 1 && rowDiff == direction) {
            if (board.checkEnemy(endRow, endCol, color)) {
                return true;
            }
            // En passant
            int captureRow = startRow;
            if (board.checkPos(captureRow, endCol) && 
                board.grid[captureRow][endCol] != null &&
                board.grid[captureRow][endCol] instanceof Pawn) {
                Pawn adjacentPawn = (Pawn) board.grid[captureRow][endCol];
                if (adjacentPawn.color != color && adjacentPawn.justMovedTwo) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
