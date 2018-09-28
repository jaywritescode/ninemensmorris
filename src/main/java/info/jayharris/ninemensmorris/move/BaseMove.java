package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.player.BasePlayer;

abstract class BaseMove implements Move {

    BasePlayer player;

    BaseMove(BasePlayer player) {
        this.player = player;
    }

    public abstract void validateLegal() throws IllegalMoveException;

    public Piece getPiece() {
        return player.getPiece();
    }
}
