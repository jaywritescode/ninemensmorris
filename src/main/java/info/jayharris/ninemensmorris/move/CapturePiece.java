package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.player.BasePlayer;
import info.jayharris.ninemensmorris.Board.Point;

import static com.google.common.base.Preconditions.checkState;

public class CapturePiece extends BaseMove {

    private final Point point;

    private CapturePiece(BasePlayer player, Point point) {
        super(player);
        this.point = point;
    }

    @Override
    public void perform() throws IllegalStateException {
        checkState(point.getPiece() == player.getPiece().opposite(), "Expected a %s piece on point %s, instead found %s", player.getPiece().opposite(),
                   point.getId(), point.getPiece().toString());

        RemovePieceAction.create().perform(point);
    }

    public String pretty() {
        return "x" + point.getId();
    }

    public static CapturePiece create(BasePlayer player, Point point) {
        return new CapturePiece(player, point);
    }
}
