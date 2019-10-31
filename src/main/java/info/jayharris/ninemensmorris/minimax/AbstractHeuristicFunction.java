package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Piece;

import java.util.function.ToDoubleFunction;

public abstract class AbstractHeuristicFunction implements ToDoubleFunction<MinimaxState> {

    Piece piece;

    AbstractHeuristicFunction(Piece piece) {
        this.piece = piece;
    }
}
