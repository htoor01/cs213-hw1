# CS213 - Software Methodology

Course projects for Rutgers CS213.

---

## Chess Implementation

Two-player chess game in Java with full rule implementation.

### Structure
All classes in `chess/` package. Main methods:
- `Chess.start()` - Initialize/reset game
- `Chess.play(String move)` - Execute move, returns `ReturnPlay` object

### Features
- All piece movements (Pawn, Rook, Knight, Bishop, Queen, King)
- Special moves: castling, en passant, pawn promotion
- Check and checkmate detection
- Illegal move prevention
- Resign and draw support

### Testing
```bash
javac chess/*.java
java chess.PlayChess
```

---

## UML Design - Online Discussion Forums

Object-oriented design for a website hosting discussion forums.

### Overview
Design includes:
- **User management**: Registration, forum membership, roles (owner, moderator)
- **Forum system**: Creation, ownership, moderation, membership
- **Discussion threads**: Message posting, threading, approval workflow
- **Filtering & sorting**: By author, thread, date (TODO)
- **Invitations**: Member, moderator, owner invitations (TODO)

### Core Classes
- `Website` - Hosts forums and manages registered users
- `User` - Registered users who can create/join forums
- `Forum` - Discussion forum with members, owner, and moderator
- `Thread` - Discussion thread containing related messages
- `Message` - Individual post with author, content, timestamp

### Key Relationships
- Users can create forums (become owner + first member)
- Forums have one owner and one moderator (initially creator)
- Members can post messages and read filtered/sorted content
- Messages require moderator approval before being visible
- Owners can invite users, transfer ownership/moderation, delete forum

### Deliverable
UML class diagram with multiplicities, access levels, and supplementary data structure table showing Java collections for associations.

**Location**: `uml-design/uml-design.md`
