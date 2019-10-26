package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.minimax.feature.MobilityFeature;

import java.util.function.ToDoubleFunction;

/**
 * A heuristic function that prefers states with greater player mobility.
 */
public class SampleHeuristicFunction implements ToDoubleFunction<MinimaxState> {

    private final Piece myPiece;

    public SampleHeuristicFunction(Piece myPiece) {
        this.myPiece = myPiece;
    }

    @Override
    public double applyAsDouble(MinimaxState state) {
        return new MobilityFeature(myPiece).apply(state);
    }
}
