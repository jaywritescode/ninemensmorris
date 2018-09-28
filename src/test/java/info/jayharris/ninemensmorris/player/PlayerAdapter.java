package info.jayharris.ninemensmorris.player;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;

public class PlayerAdapter extends BasePlayer {

    public PlayerAdapter(Piece piece) {
        super(piece);
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
}
