package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.Piece;

import java.util.Objects;

public final class MovePiece extends BaseMove implements InitialMove {

    public static final String EMPTY_POINT_TEMPLATE = "No piece on %s to move.",
            OPPONENT_PIECE_TEMPLATE = "Cannot move opponent's piece on %s.",
            DESTINATION_OCCUPIED_TEMPLATE = "Cannot move to occupied point at %s.",
            POINTS_NOT_ADJACENT_TEMPLATE = "Points %s and %s are not adjacent.";
    private final Board board;
    private final Point initial, destination;

    private MovePiece(Piece piece, Board board, Point initial, Point destination) {
        super(piece);
        this.board = board;
        this.initial = initial;
        this.destination = destination;
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
            throw IllegalMoveException.create(
                    initial.isUnoccupied() ? EMPTY_POINT_TEMPLATE : OPPONENT_PIECE_TEMPLATE,
                    initial.getId());
        }
        if (!destination.isUnoccupied()) {
            throw IllegalMoveException.create(DESTINATION_OCCUPIED_TEMPLATE, destination.getId());
        }

        if (board.getOccupiedPoints(getPiece()).size() > 3 && !initial.getNeighbors().contains(destination)) {
            throw IllegalMoveException.create(POINTS_NOT_ADJACENT_TEMPLATE, initial.getId(), destination.getId());
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

    public String pretty() {
        return initial.getId() + "-" + destination.getId();
    }

    public static MovePiece create(Piece piece, Board board, Point initial, Point destination) {
        return new MovePiece(piece, board, initial, destination);
    }

    public static MovePiece createLegal(Piece piece, Board board, Point initial, Point destination) {
        MovePiece move = create(piece, board, initial, destination);

        move.validateLegal();
        return move;
    }
}
