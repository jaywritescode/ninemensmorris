package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.BasePlayer;
import info.jayharris.ninemensmorris.Board.Point;

import static com.google.common.base.Preconditions.checkState;

public final class PlacePiece extends BaseMove implements InitialMove {

    final Point point;

    public PlacePiece(BasePlayer player, Point point) {
        super(player);
        this.point = point;
    }

    @Override
    public void perform() throws IllegalStateException {
        checkState(!point.isOccupied(), "Expected %s to be empty.", point.getId());
        AddPieceAction.create(player.getPiece()).perform(point);
    }

    @Override
    public Point getUpdatedPoint() {
        return point;
    }

    public static PlacePiece create(BasePlayer player, Point point) {
        return new PlacePiece(player, point);
    }
}
