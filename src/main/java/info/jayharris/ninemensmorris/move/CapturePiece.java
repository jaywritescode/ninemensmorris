package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.player.BasePlayer;
import info.jayharris.ninemensmorris.Board.Point;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkState;

public final class CapturePiece extends BaseMove {

    private final Point point;

    public final static String ILLEGAL_MOVE_TEMPLATE = "Expected a %s piece on point %s, instead found %s.",
            EMPTY_POINT = "no piece";

    private CapturePiece(BasePlayer player, Point point) {
        super(player);
        this.point = point;
    }

    @Override
    public void perform() throws IllegalStateException {
        validateLegal();
        RemovePieceAction.create().perform(point);
    }

    public void validateLegal() throws IllegalMoveException {
        if (point.getPiece() != player.getPiece().opposite()) {
            throw IllegalMoveException.create(ILLEGAL_MOVE_TEMPLATE,
                                              player.getPiece().opposite().toString(),
                                              point.getId(), Objects.toString(point.getPiece(), EMPTY_POINT));
        }
    }

    public String pretty() {
        return "x" + point.getId();
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

    public static CapturePiece create(BasePlayer player, Point point) {
        return new CapturePiece(player, point);
    }

    public static CapturePiece createLegalMove(BasePlayer player, Point point) {
        CapturePiece move = create(player, point);

        move.validateLegal();
        return move;
    }
}
