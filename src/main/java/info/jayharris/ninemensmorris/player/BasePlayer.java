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

    public abstract Turn takeTurn(Board board);

    public int getStartingPieces() {
        return startingPieces;
    }

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
