package info.jayharris.ninemensmorris.minimax.feature;

import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.minimax.MinimaxState;

public abstract class Feature {

    protected final Piece piece;

    public Feature(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public abstract double apply(MinimaxState state);
}
