# Chess Implementation

Two-player chess game in Java with full rule implementation.

## Structure
All classes in `chess/` package. Main methods:
- `Chess.start()` - Initialize/reset game
- `Chess.play(String move)` - Execute move, returns `ReturnPlay` object

## Features
- All piece movements (Pawn, Rook, Knight, Bishop, Queen, King)
- Special moves: castling, en passant, pawn promotion
- Check and checkmate detection
- Illegal move prevention
- Resign and draw support

## Testing
```bash
javac chess/*.java
java chess.PlayChess
```
