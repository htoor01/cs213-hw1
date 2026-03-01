package chess;

public class Rook extends Piece {
    public boolean hasMoved = false; // for castling
    
    public Rook(Chess.Player color) {
        super(color);
    }

    public boolean validMove(Board board, int startRow, int startCol, int endRow, int endCol) {
        // Must move in straight line (same row or same column)
        if (startRow != endRow && startCol != endCol) {
            return false;
        }
        
        // Check if path is clear
        int rowStep = (endRow > startRow) ? 1 : (endRow < startRow) ? -1 : 0;
        int colStep = (endCol > startCol) ? 1 : (endCol < startCol) ? -1 : 0;
        
        int currentRow = startRow + rowStep;
        int currentCol = startCol + colStep;
        
        while (currentRow != endRow || currentCol != endCol) {
            if (!board.checkEmpty(currentRow, currentCol)) {
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        
        // End square must be empty or enemy
        return board.checkEmpty(endRow, endCol) || board.checkEnemy(endRow, endCol, color);
    }
}
