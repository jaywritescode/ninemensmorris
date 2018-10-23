package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.Piece;

import java.util.Objects;
import java.util.StringJoiner;

public final class CapturePiece extends BaseMove {

    private final Point point;

    public final static String ILLEGAL_MOVE_TEMPLATE = "Expected a %s piece on point %s, instead found %s.",
            EMPTY_POINT = "no piece";

    private CapturePiece(Piece piece, Point point) {
        super(piece);
        this.point = point;
    }

    @Override
    public void perform() throws IllegalStateException {
        validateLegal();
        RemovePieceAction.create().perform(point);
    }

    public void validateLegal() throws IllegalMoveException {
        if (point.getPiece() != piece.opposite()) {
            throw IllegalMoveException.create(ILLEGAL_MOVE_TEMPLATE,
                                              piece.opposite().toString(),
                                              point.algebraicNotation(), Objects.toString(point.getPiece(), EMPTY_POINT));
        }
    }

    public String pretty() {
        return "x" + point.algebraicNotation();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CapturePiece.class.getSimpleName() + "[", "]")
                .add("point=" + point)
                .add("piece=" + piece)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CapturePiece that = (CapturePiece) o;
        return Objects.equals(point, that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point);
    }

    public static CapturePiece create(Piece piece, Point point) {
        return new CapturePiece(piece, point);
    }

    public static CapturePiece createLegalMove(Piece piece, Point point) {
        CapturePiece move = create(piece, point);

        move.validateLegal();
        return move;
    }
}
