package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.search.MinimaxDecision;
import info.jayharris.minimax.search.UnknownUtilityException;
import info.jayharris.minimax.search.cutoff.CutoffTest;
import info.jayharris.ninemensmorris.Piece;

import java.util.function.ToDoubleFunction;

public class NineMensMorrisMinimaxDecision extends MinimaxDecision<MinimaxState, MinimaxAction> {

    private final Piece myPiece;

    public NineMensMorrisMinimaxDecision(
            CutoffTest<MinimaxState, MinimaxAction> cutoffTest,
            ToDoubleFunction<MinimaxState> heuristicFn,
            Piece myPiece) {
        super(heuristicFn, cutoffTest);
        this.myPiece = myPiece;
    }

    @Override
    public double utility(MinimaxState state) throws UnknownUtilityException {
        return Utility.create(myPiece).apply(state);
    }
}
