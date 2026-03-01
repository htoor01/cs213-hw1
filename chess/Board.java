package chess;

public class Board {
    Piece[][] grid = new Piece[8][8];

    //check if position is within board
    boolean checkPos(int r, int c) {
        return (0 <= r && r <= 7) && (0 <= c && c <= 7);
    }

    //check if position to move to is empty
    boolean checkEmpty(int r, int c) {
        return checkPos(r, c) && grid[r][c] == null;
    }

    //check if there's an enemy piece to capture
    boolean checkEnemy(int r, int c, Chess.Player color) {
        return checkPos(r, c) && grid[r][c] != null && grid[r][c].color != color;
    }
    
}