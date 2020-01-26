package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.search.AlphaBetaPruningSearch;
import info.jayharris.minimax.search.UnknownUtilityException;
import info.jayharris.minimax.search.cutoff.CutoffTest;
import info.jayharris.ninemensmorris.Piece;

import java.util.function.ToDoubleFunction;

public class NineMensMorrisAlphaBetaPruningSearch extends AlphaBetaPruningSearch<MinimaxState, MinimaxAction> {

    private final Piece myPiece;

    public NineMensMorrisAlphaBetaPruningSearch(
            CutoffTest<MinimaxState, MinimaxAction> cutoffTest,
            ToDoubleFunction<MinimaxState> heuristic,
            Piece myPiece) {
        super(cutoffTest, heuristic);
        this.myPiece = myPiece;
    }

    @Override
    public double utility(MinimaxState state) throws UnknownUtilityException {
        return Utility.create(myPiece).apply(state);
    }
}
