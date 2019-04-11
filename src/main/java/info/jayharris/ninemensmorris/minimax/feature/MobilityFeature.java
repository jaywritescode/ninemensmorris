package info.jayharris.ninemensmorris.minimax.feature;

import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.minimax.MinimaxState;

/**
 * This feature counts the number (`from`, `to`) pairs where `from` is occupied
 * by `piece`, `to` is unoccupied, and `from` and `to` are adjacent to each other.
 */
public class MobilityFeature extends Feature {

    public MobilityFeature(Piece piece) {
        super(piece);
    }

    @Override
    public double apply(MinimaxState state) {
        return state.getBoard().getOccupiedPoints(piece).stream()
                .mapToLong(MobilityFeature::countUnoccupiedNeighbors)
                .sum();
    }

    private static long countUnoccupiedNeighbors(Point point) {
        return point.getNeighbors().stream()
                .filter(Point::isUnoccupied)
                .count();
    }
}
