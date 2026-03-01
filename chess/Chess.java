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
		ReturnPlay result = new ReturnPlay();
		
		// Handle resign
		if (move.trim().equals("resign")) {
			result.message = (turn == Player.white) ? ReturnPlay.Message.RESIGN_BLACK_WINS : ReturnPlay.Message.RESIGN_WHITE_WINS;
			result.piecesOnBoard = getBoardState();
			return result;
		}
		
		// Check for draw
		boolean drawRequested = false;
		if (move.contains("draw?")) {
			drawRequested = true;
			move = move.replace("draw?", "").trim();
		}
		
		// Parse the move
		String[] parts = move.trim().split("\\s+");
		if (parts.length < 2) {
			result.message = ReturnPlay.Message.ILLEGAL_MOVE;
			result.piecesOnBoard = getBoardState();
			return result;
		}
		
		String from = parts[0];
		String to = parts[1];
		String promotion = (parts.length > 2) ? parts[2] : "Q";
		
		// Convert algebraic notation to array indices
		int[] fromPos = parsePosition(from);
		int[] toPos = parsePosition(to);
		
		if (fromPos == null || toPos == null) {
			result.message = ReturnPlay.Message.ILLEGAL_MOVE;
			result.piecesOnBoard = getBoardState();
			return result;
		}
		
		int startRow = fromPos[0];
		int startCol = fromPos[1];
		int endRow = toPos[0];
		int endCol = toPos[1];
		
		// Check if there's a piece at starting position
		Piece piece = board.grid[startRow][startCol];
		if (piece == null || piece.color != turn) {
			result.message = ReturnPlay.Message.ILLEGAL_MOVE;
			result.piecesOnBoard = getBoardState();
			return result;
		}
		
		// Check if move is valid for this piece
		if (!piece.validMove(board, startRow, startCol, endRow, endCol)) {
			// Check for castling
			if (piece instanceof King && Math.abs(endCol - startCol) == 2) {
				if (!performCastling(startRow, startCol, endRow, endCol)) {
					result.message = ReturnPlay.Message.ILLEGAL_MOVE;
					result.piecesOnBoard = getBoardState();
					return result;
				}
				// Castling successful
				turn = (turn == Player.white) ? Player.black : Player.white;
				result.piecesOnBoard = getBoardState();
				result.message = drawRequested ? ReturnPlay.Message.DRAW : null;
				return result;
			}
			result.message = ReturnPlay.Message.ILLEGAL_MOVE;
			result.piecesOnBoard = getBoardState();
			return result;
		}
		
		// Save state to restore if move puts own king in check
		Piece capturedPiece = board.grid[endRow][endCol];
		Piece enPassantCapture = null;
		boolean wasEnPassant = false;
		
		// Special handling for en passant
		if (piece instanceof Pawn && startCol != endCol && capturedPiece == null) {
			wasEnPassant = true;
			enPassantCapture = board.grid[startRow][endCol];
		}
		
		// Execute the move temporarily
		board.grid[endRow][endCol] = piece;
		board.grid[startRow][startCol] = null;
		if (wasEnPassant) {
			board.grid[startRow][endCol] = null;
		}
		
		// Check if this move puts own king in check
		if (isInCheck(turn)) {
			// Undo the move
			board.grid[startRow][startCol] = piece;
			board.grid[endRow][endCol] = capturedPiece;
			if (wasEnPassant) {
				board.grid[startRow][endCol] = enPassantCapture;
			}
			result.message = ReturnPlay.Message.ILLEGAL_MOVE;
			result.piecesOnBoard = getBoardState();
			return result;
		}
		
		// Move is valid, finalize it
		// Update piece movement flags
		if (piece instanceof Pawn) {
			Pawn pawn = (Pawn) piece;
			// Clear all justMovedTwo flags for this color
			clearJustMovedTwoFlags(turn);
			if (Math.abs(endRow - startRow) == 2) {
				pawn.justMovedTwo = true;
			}
			pawn.hasMoved = true;
			
			// Handle pawn promotion
			if ((turn == Player.white && endRow == 0) || (turn == Player.black && endRow == 7)) {
				board.grid[endRow][endCol] = createPromotionPiece(promotion, turn);
			}
		} else if (piece instanceof Rook) {
			((Rook) piece).hasMoved = true;
		} else if (piece instanceof King) {
			((King) piece).hasMoved = true;
		}
		
		// Check if opponent is in check or checkmate
		Player opponent = (turn == Player.white) ? Player.black : Player.white;
		boolean opponentInCheck = isInCheck(opponent);
		boolean opponentInCheckmate = false;
		
		if (opponentInCheck) {
			opponentInCheckmate = isCheckmate(opponent);
		}
		
		// Switch turns
		turn = opponent;
		
		// Set result message
		if (opponentInCheckmate) {
			result.message = (opponent == Player.black) ? ReturnPlay.Message.CHECKMATE_WHITE_WINS : ReturnPlay.Message.CHECKMATE_BLACK_WINS;
		} else if (drawRequested) {
			result.message = ReturnPlay.Message.DRAW;
		} else if (opponentInCheck) {
			result.message = ReturnPlay.Message.CHECK;
		} else {
			result.message = null;
		}
		
		result.piecesOnBoard = getBoardState();
		return result;
	}
	
	// Helper method to parse position from algebraic notation
	private static int[] parsePosition(String pos) {
		if (pos.length() != 2) return null;
		char file = pos.charAt(0);
		char rank = pos.charAt(1);
		if (file < 'a' || file > 'h' || rank < '1' || rank > '8') return null;
		int col = file - 'a';
		int row = 8 - (rank - '0'); // Convert to 0-indexed array
		return new int[]{row, col};
	}
	
	// Helper method to get current board state
	private static ArrayList<ReturnPiece> getBoardState() {
		ArrayList<ReturnPiece> pieces = new ArrayList<>();
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				Piece piece = board.grid[r][c];
				if (piece != null) {
					ReturnPiece rp = new ReturnPiece();
					rp.pieceFile = ReturnPiece.PieceFile.values()[c];
					rp.pieceRank = 8 - r;
					rp.pieceType = getPieceType(piece);
					pieces.add(rp);
				}
			}
		}
		return pieces;
	}
	
	// Helper method to get piece type
	private static ReturnPiece.PieceType getPieceType(Piece piece) {
		boolean isWhite = (piece.color == Player.white);
		if (piece instanceof Pawn) return isWhite ? ReturnPiece.PieceType.WP : ReturnPiece.PieceType.BP;
		if (piece instanceof Rook) return isWhite ? ReturnPiece.PieceType.WR : ReturnPiece.PieceType.BR;
		if (piece instanceof Knight) return isWhite ? ReturnPiece.PieceType.WN : ReturnPiece.PieceType.BN;
		if (piece instanceof Bishop) return isWhite ? ReturnPiece.PieceType.WB : ReturnPiece.PieceType.BB;
		if (piece instanceof Queen) return isWhite ? ReturnPiece.PieceType.WQ : ReturnPiece.PieceType.BQ;
		if (piece instanceof King) return isWhite ? ReturnPiece.PieceType.WK : ReturnPiece.PieceType.BK;
		return null;
	}
	
	// Check if a player's king is in check
	private static boolean isInCheck(Player player) {
		// Find the king
		int kingRow = -1, kingCol = -1;
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				Piece piece = board.grid[r][c];
				if (piece instanceof King && piece.color == player) {
					kingRow = r;
					kingCol = c;
					break;
				}
			}
			if (kingRow != -1) break;
		}
		
		// Check if any opponent piece can attack the king
		Player opponent = (player == Player.white) ? Player.black : Player.white;
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				Piece piece = board.grid[r][c];
				if (piece != null && piece.color == opponent) {
					if (piece.validMove(board, r, c, kingRow, kingCol)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	// Check if a player is in checkmate
	private static boolean isCheckmate(Player player) {
		if (!isInCheck(player)) return false;
		
		// Try all possible moves for this player
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				Piece piece = board.grid[r][c];
				if (piece != null && piece.color == player) {
					// Try all possible destinations
					for (int endR = 0; endR < 8; endR++) {
						for (int endC = 0; endC < 8; endC++) {
							if (piece.validMove(board, r, c, endR, endC)) {
								// Try this move
								Piece captured = board.grid[endR][endC];
								Piece enPassantCapture = null;
								boolean wasEnPassant = false;
								
								if (piece instanceof Pawn && c != endC && captured == null) {
									wasEnPassant = true;
									enPassantCapture = board.grid[r][endC];
								}
								
								board.grid[endR][endC] = piece;
								board.grid[r][c] = null;
								if (wasEnPassant) {
									board.grid[r][endC] = null;
								}
								
								boolean stillInCheck = isInCheck(player);
								
								// Undo the move
								board.grid[r][c] = piece;
								board.grid[endR][endC] = captured;
								if (wasEnPassant) {
									board.grid[r][endC] = enPassantCapture;
								}
								
								if (!stillInCheck) {
									return false; // Found a legal move
								}
							}
						}
					}
				}
			}
		}
		return true; // No legal moves found
	}
	
	// Handle castling
	private static boolean performCastling(int startRow, int startCol, int endRow, int endCol) {
		King king = (King) board.grid[startRow][startCol];
		if (king.hasMoved || isInCheck(king.color)) {
			return false;
		}
		
		// Determine if king-side or queen-side castling
		boolean kingSide = (endCol > startCol);
		int rookCol = kingSide ? 7 : 0;
		int rookEndCol = kingSide ? 5 : 3;
		
		Piece rook = board.grid[startRow][rookCol];
		if (!(rook instanceof Rook) || ((Rook) rook).hasMoved) {
			return false;
		}
		
		// Check if path is clear
		int start = Math.min(startCol, rookCol) + 1;
		int end = Math.max(startCol, rookCol);
		for (int c = start; c < end; c++) {
			if (board.grid[startRow][c] != null) {
				return false;
			}
		}
		
		// Check if king passes through or ends on attacked square
		int step = kingSide ? 1 : -1;
		for (int c = startCol; c != endCol + step; c += step) {
			board.grid[startRow][c] = king;
			if (c != startCol) {
				board.grid[startRow][c - step] = null;
			}
			if (isInCheck(king.color)) {
				// Undo
				board.grid[startRow][startCol] = king;
				for (int cc = startCol + step; cc != c + step; cc += step) {
					board.grid[startRow][cc] = null;
				}
				return false;
			}
		}
		
		// Perform castling
		board.grid[startRow][startCol] = null;
		board.grid[startRow][endCol] = king;
		board.grid[startRow][rookCol] = null;
		board.grid[startRow][rookEndCol] = rook;
		king.hasMoved = true;
		((Rook) rook).hasMoved = true;
		
		return true;
	}
	
	// Clear justMovedTwo flags for all pawns of a color
	private static void clearJustMovedTwoFlags(Player player) {
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				Piece piece = board.grid[r][c];
				if (piece instanceof Pawn && piece.color == player) {
					((Pawn) piece).justMovedTwo = false;
				}
			}
		}
	}
	
	// Create a piece for pawn promotion
	private static Piece createPromotionPiece(String type, Player color) {
		switch (type.toUpperCase()) {
			case "R": return new Rook(color);
			case "N": return new Knight(color);
			case "B": return new Bishop(color);
			case "Q":
			default: return new Queen(color);
		}
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