package info.jayharris.ninemensmorris.player;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;

import java.lang.reflect.Field;

public class PlayerAdapter extends BasePlayer {

    Field startingPiecesField;

    public PlayerAdapter(Piece piece) throws NoSuchFieldException {
        super(piece);

        startingPiecesField = BasePlayer.class.getDeclaredField("startingPieces");
        startingPiecesField.setAccessible(true);
    }

    @Override
    public PlacePiece placePiece(Board board) {
        return null;
    }

    @Override
    public MovePiece movePiece(Board board) {
        return null;
    }

    @Override
    public CapturePiece capturePiece(Board board) {
        return null;
    }

    public void setStartingPieces(int startingPieces) throws IllegalAccessException {
        startingPiecesField.setInt(this, startingPieces);
    }
}
