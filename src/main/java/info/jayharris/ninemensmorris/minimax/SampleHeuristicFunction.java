package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.minimax.feature.MobilityFeature;

/**
 * A heuristic function that prefers states with greater player mobility.
 */
public class SampleHeuristicFunction extends AbstractHeuristicFunction {

    public SampleHeuristicFunction(Piece myPiece) {
        super(myPiece);
    }

    @Override
    public double applyAsDouble(MinimaxState state) {
        return new MobilityFeature(piece).apply(state);
    }
}
