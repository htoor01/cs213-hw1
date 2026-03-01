package chess;

public class Queen extends Piece {
    //inherits color from Piece
    public Queen(Chess.Player color) {
        super(color);
    }

    public boolean validMove(Board board, int startRow, int startCol, int endRow, int endCol) {
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);
        
        // Queen moves like rook (straight) or bishop (diagonal)
        boolean isStraight = (startRow == endRow || startCol == endCol);
        boolean isDiagonal = (rowDiff == colDiff && rowDiff > 0);
        
        if (!isStraight && !isDiagonal) {
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