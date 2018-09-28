package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.player.BasePlayer;
import info.jayharris.ninemensmorris.Board.Point;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkState;

public final class CapturePiece extends BaseMove {

    private final Point point;

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
            throw IllegalMoveException.create("Expected a %s piece on point %s, instead found %s",
                                              player.getPiece().opposite().toString(),
                                              point.getId(), Objects.toString(point.getPiece(), "no piece"));
        }
    }

    public String pretty() {
        return "x" + point.getId();
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
