package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.CutoffTest;
import info.jayharris.minimax.search.MinimaxDecision;
import info.jayharris.minimax.search.UnknownUtilityException;
import info.jayharris.ninemensmorris.Piece;

import java.util.function.ToDoubleFunction;

public class NineMensMorrisMinimaxDecision extends MinimaxDecision<MinimaxState, MinimaxAction> {

    private final Piece myPiece;

    public NineMensMorrisMinimaxDecision(
            CutoffTest<MinimaxState, MinimaxAction> cutoffTest,
            ToDoubleFunction<MinimaxState> heuristicFn,
            Piece myPiece) {
        super(cutoffTest, heuristicFn);
        this.myPiece = myPiece;
    }

    @Override
    public double utility(MinimaxState state) throws UnknownUtilityException {
        return Utility.create(myPiece).apply(state);
    }
}
