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

    protected final Piece piece;
    protected int startingPieces = 9;

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

    protected abstract PlacePiece placePiece(Board board);

    protected abstract MovePiece movePiece(Board board);

    protected abstract CapturePiece capturePiece(Board board);

    /**
     * Called before the turn proper starts.
     *
     * @param game the game
     */
    public void begin(Game game) { }

    /**
     * Called after the turn proper finishes, but before the next ply begins.
     *
     * @param game the game
     */
    public void done(Game game) { }
}
