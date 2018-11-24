package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.minimax.feature.MobilityFeature;

public class SampleHeuristicFunction extends BaseHeuristicFunction {

    public SampleHeuristicFunction(Piece piece) {
        super(piece);
    }

    @Override
    public double apply(MinimaxState state) {
        double val = super.apply(state);
        if (val != 0) {
            return val;
        }

        return new MobilityFeature(piece).apply(state);
    }
}
