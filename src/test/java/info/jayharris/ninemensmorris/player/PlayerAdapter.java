package info.jayharris.ninemensmorris.player;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;

import java.lang.reflect.Field;

/**
 * Adapter for the abstract TwoStageTurnPlayer, with default null-returning methods
 * for `#placePiece`, `#movePiece`, and `#capturePiece`.
 */
public class PlayerAdapter extends TwoStageTurnPlayer {

    Field startingPiecesField;

    public PlayerAdapter(Piece piece) throws NoSuchFieldException {
        super(piece);

        startingPiecesField = BasePlayer.class.getDeclaredField("startingPieces");
        startingPiecesField.setAccessible(true);
    }

    @Override
    protected PlacePiece placePiece(Board board) {
        return null;
    }

    @Override
    protected MovePiece movePiece(Board board) {
        return null;
    }

    @Override
    protected CapturePiece capturePiece(Board board) {
        return null;
    }

    public void setStartingPieces(int startingPieces) throws IllegalAccessException {
        startingPiecesField.setInt(this, startingPieces);
    }
}
