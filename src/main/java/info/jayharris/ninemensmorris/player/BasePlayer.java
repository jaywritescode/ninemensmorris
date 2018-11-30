package info.jayharris.ninemensmorris.player;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Game;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.Turn;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;

public abstract class BasePlayer {

    public final static Piece FIRST_PLAYER = Piece.BLACK;

    private final Piece piece;
    private int startingPieces = 9;

    BasePlayer(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public final Turn takeTurn(Board board) {
        Turn turn = Turn.initialize(this, board);

        if (startingPieces > 0) {
            turn.doInitialMove(placePiece(board));
            --startingPieces;
        }
        else {
            turn.doInitialMove(movePiece(board));
        }

        if (board.isCompleteMill(turn.getUpdatedPoint())) {
            turn.doCaptureMove(capturePiece(board));
        }

        return turn;
    }

    public int getStartingPieces() {
        return startingPieces;
    }

    public abstract PlacePiece placePiece(Board board);

    public abstract MovePiece movePiece(Board board);

    public abstract CapturePiece capturePiece(Board board);

    public void begin(Game game) { }

    public void done(Game game) { }
}
