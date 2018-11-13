package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Piece;

public abstract class Feature {

    private final Piece piece;

    public Feature(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public abstract double apply(MinimaxState state);
}
