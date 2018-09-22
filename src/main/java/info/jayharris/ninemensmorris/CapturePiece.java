package info.jayharris.ninemensmorris;

import info.jayharris.ninemensmorris.Board.Point;

import static com.google.common.base.Preconditions.checkState;

public class CapturePiece extends BaseMove {

    final Point point;

    public CapturePiece(BasePlayer player, Point point) {
        super(player);
        this.point = point;
    }

    @Override
    public void perform() throws IllegalStateException {
        checkState(point.getPiece() == player.getPiece().opposite(), "Expected a %s piece on point %s, instead found %s", player.getPiece().opposite(), point.id, point.getPiece().toString());

        RemovePieceAction.create().perform(point);
    }

    public static CapturePiece create(BasePlayer player, Point point) {
        return new CapturePiece(player, point);
    }
}
