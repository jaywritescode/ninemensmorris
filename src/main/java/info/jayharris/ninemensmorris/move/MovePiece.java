package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.Piece;

import java.util.Objects;
import java.util.StringJoiner;

public final class MovePiece extends BaseMove implements InitialMove {

    public static final String EMPTY_POINT_TEMPLATE = "No piece on %s to move.",
            OPPONENT_PIECE_TEMPLATE = "Cannot move opponent's piece on %s.",
            DESTINATION_OCCUPIED_TEMPLATE = "Cannot move to occupied point at %s.",
            POINTS_NOT_ADJACENT_TEMPLATE = "Points %s and %s are not adjacent.";
    private final boolean canFly;
    private final Point initial, destination;

    private MovePiece(Piece piece, Point initial, Point destination, boolean canFly) {
        super(piece);
        this.initial = initial;
        this.destination = destination;
        this.canFly = canFly;
    }

    @Override
    public void perform() throws IllegalMoveException {
        validateLegal();
        RemovePieceAction.create().perform(initial);
        AddPieceAction.create(piece).perform(destination);
    }

    @Override
    public Point getUpdatedPoint() {
        return destination;
    }

    @Override
    public void validateLegal() {
        if (initial.getPiece() != piece) {
            throw initial.isUnoccupied() ? emptyPoint(initial) : wrongColor(initial);
        }

        if (!destination.isUnoccupied()) {
            throw destinationOccupied(destination);
        }

        if (!canFly && !initial.getNeighbors().contains(destination)) {
            throw pointsNotAdjacent(initial, destination);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovePiece movePiece = (MovePiece) o;
        return Objects.equals(initial, movePiece.initial) &&
               Objects.equals(destination, movePiece.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initial, destination);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MovePiece.class.getSimpleName() + "[", "]")
                .add("initial=" + initial)
                .add("destination=" + destination)
                .add("piece=" + piece)
                .toString();
    }

    public String pretty() {
        return initial.algebraicNotation() + "-" + destination.algebraicNotation();
    }

    /**
     * Creates a (possibly illegal) MovePiece.
     *
     * @param piece the piece
     * @param initial the point the piece is being moved from
     * @param destination the point the piece is being moved to
     * @param canFly true iff there are exactly three {@code piece}s on the board
     * @return the move
     */
    public static MovePiece create(Piece piece, Point initial, Point destination, boolean canFly) {
        return new MovePiece(piece, initial, destination, canFly);
    }

    /**
     * Creates a legal MovePiece.
     *
     * @param piece the piece
     * @param initial the point the piece is being moved from
     * @param destination the point the piece is being moved to
     * @param canFly true iff there are exactly three {@code piece}s on the board
     * @return the move
     * @throws IllegalMoveException if the move is illegal
     */
    public static MovePiece createLegal(Piece piece, Point initial, Point destination, boolean canFly) {
        MovePiece move = create(piece, initial, destination, canFly);

        move.validateLegal();
        return move;
    }

    private static IllegalMoveException emptyPoint(Point point) {
        String msg = String.format(EMPTY_POINT_TEMPLATE, point.algebraicNotation());
        return new IllegalMoveException(msg);
    }

    private static IllegalMoveException wrongColor(Point point) {
        String msg = String.format(OPPONENT_PIECE_TEMPLATE, point.algebraicNotation());
        return new IllegalMoveException(msg);
    }

    private static IllegalMoveException destinationOccupied(Point point) {
        String msg = String.format(DESTINATION_OCCUPIED_TEMPLATE, point.algebraicNotation());
        return new IllegalMoveException(msg);
    }

    private static IllegalMoveException pointsNotAdjacent(Point initial, Point destination) {
        String msg = String.format(POINTS_NOT_ADJACENT_TEMPLATE,
                initial.algebraicNotation(), destination.algebraicNotation());
        return new IllegalMoveException(msg);
    }
}
