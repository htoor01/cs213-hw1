package chess;

public class Bishop extends Piece {
    
    public Bishop(Chess.Player color) {
        super(color);
    }

    public boolean validMove(Board board, int startRow, int startCol, int endRow, int endCol) {
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);
        
        // Must move diagonally (equal row and column difference)
        if (rowDiff != colDiff || rowDiff == 0) {
            return false;
        }
        
        // Check if path is clear
        int rowStep = (endRow > startRow) ? 1 : -1;
        int colStep = (endCol > startCol) ? 1 : -1;
        
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
