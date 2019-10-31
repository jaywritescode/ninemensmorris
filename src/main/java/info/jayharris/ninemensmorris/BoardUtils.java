package info.jayharris.ninemensmorris;

import info.jayharris.ninemensmorris.Board.Point;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BoardUtils {

    public static final int NUM_POINTS = Coordinate.ALGEBRAIC_NOTATIONS_FOR_COORDINATES.size();

    public static boolean isEmpty(Board board) {
        return board.getUnoccupiedPoints().size() == NUM_POINTS;
    }

    /**
     * Determine if {@code piece} has won the game, assuming we're in the "move piece" phase.
     *
     * @param board the board
     * @param piece the piece
     * @return true if piece has won the game
     */
    public static boolean isWinner(Board board, Piece piece) {
        Piece opponent = piece.opposite();

        Set<Point> occupiedPoints = board.getOccupiedPoints(opponent);

        // Opponent is left with only two piece, so we win.
        if (occupiedPoints.size() < 3) {
            return true;
        }

        // With three pieces, the opponent can play at any empty point. so there's always a legal move for him.
        if (occupiedPoints.size() == 3) {
            return false;
        }

        // If the opponent has any piece with an unoccupied neighbor point, the game can keep going.
        return !occupiedPoints.stream()
                .flatMap(point -> point.getNeighbors().stream())
                .anyMatch(Point::isUnoccupied);
    }

    public static Predicate<Piece> pieceIsWinner(Board board) {
        return piece -> isWinner(board, piece);
    }

    public static Optional<Piece> getWinner(Board board) {
        return Stream.of(Piece.BLACK, Piece.WHITE).filter(pieceIsWinner(board)).findAny();
    }
}
