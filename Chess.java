package chess;

import java.util.ArrayList;

public class Chess {

    enum Player { white, black }
    static Board board;
    static Player turn;
    
	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for details of
	 *         the contents of the returned ReturnPlay instance.
	 */
	public static ReturnPlay play(String move) {

		/* FILL IN THIS METHOD */
		
		/* FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY */
		/* WHEN YOU FILL IN THIS METHOD, YOU NEED TO RETURN A ReturnPlay OBJECT */
		return null;
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		//starting positions of board and pieces
        board = new Board();
        turn = Player.white;

        for (int c = 0; c < 8; c++) {
            board.grid[1][c] = new Pawn(Player.black); 
            board.grid[6][c] = new Pawn(Player.white);
        }

        board.grid[0][3] = new Queen(Player.black);
        board.grid[7][3] = new Queen(Player.white);

        board.grid[0][4] = new King(Player.black);
        board.grid[7][4] = new King(Player.white);

        board.grid[0][0] = new Rook(Player.black);
        board.grid[0][7] = new Rook(Player.black);
        board.grid[7][0] = new Rook(Player.white);
        board.grid[7][7] = new Rook(Player.white);

        board.grid[0][1] = new Knight(Player.black);
        board.grid[0][6] = new Knight(Player.black);
        board.grid[7][1] = new Knight(Player.white);
        board.grid[7][6] = new Knight(Player.white);

        board.grid[0][2] = new Bishop(Player.black);
        board.grid[0][5] = new Bishop(Player.black);
        board.grid[7][2] = new Bishop(Player.white);
        board.grid[7][5] = new Bishop(Player.white);
	}
}