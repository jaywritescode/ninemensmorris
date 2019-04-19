package info.jayharris.ninemensmorris.minimax.feature;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.minimax.MinimaxState;

import java.util.function.Predicate;

/**
 * This feature counts the number of mills `piece` has on the board.
 */
public class CountMillsFeature extends Feature {

    public CountMillsFeature(Piece piece) {
        super(piece);
    }

    @Override
    public double apply(MinimaxState state) {
        Board board = state.getBoard();
        return board.getMills().stream()
                .filter(isCompleteMill(piece))
                .count();
    }

    private final static Predicate<Board.Mill> isCompleteMill(Piece piece) {
        return mill -> mill.getPoints().stream()
                .allMatch(point -> point.getPiece() == piece);
    }

}
