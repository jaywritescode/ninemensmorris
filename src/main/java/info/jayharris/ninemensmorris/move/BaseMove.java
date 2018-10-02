package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.Piece;

abstract class BaseMove implements Move {

    Piece piece;

    BaseMove(Piece piece) {
        this.piece = piece;
    }

    public abstract void validateLegal() throws IllegalMoveException;

    public Piece getPiece() {
        return piece;
    }
}
