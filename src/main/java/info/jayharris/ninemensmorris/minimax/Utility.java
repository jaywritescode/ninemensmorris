package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.search.UnknownUtilityException;
import info.jayharris.ninemensmorris.BoardUtils;
import info.jayharris.ninemensmorris.Piece;

public class Utility {

    final Piece myPiece;

    private Utility(Piece myPiece) {
        this.myPiece = myPiece;
    }

    double apply(MinimaxState state) {
        if (state.isStalemate()) {
            return 0.0;
        }

        Piece winner = BoardUtils.getWinner(state.getBoard())
                .orElseThrow(UnknownUtilityException::new);

        return winner == myPiece ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
    }

    public static Utility create(Piece myPiece) {
        return new Utility(myPiece);
    }
}