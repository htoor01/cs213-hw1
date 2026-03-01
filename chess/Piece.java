package chess;

public abstract class Piece {
    Chess.Player color;

    Piece(Chess.Player color) {
        this.color = color;
    }

    abstract boolean validMove(Board board, int startRow, int startCol, int endRow, int endCol);

}