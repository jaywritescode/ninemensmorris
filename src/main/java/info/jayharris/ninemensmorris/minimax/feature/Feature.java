package info.jayharris.ninemensmorris.minimax.feature;

import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.minimax.MinimaxState;

/**
 * A quantitative feature of the board, or more precisely, of the state.
 *
 * Some examples might be the number of legal moves that the (state's) current
 * player has available or the total point value of the current player's pieces
 * on a chessboard, or a number representing the quality of the current player's
 * defense, etc.
 */
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
