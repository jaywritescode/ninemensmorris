package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.player.BasePlayer;
import info.jayharris.ninemensmorris.Board.Point;

import static com.google.common.base.Preconditions.checkState;

public final class PlacePiece extends BaseMove implements InitialMove {

    private final Point point;

    private PlacePiece(BasePlayer player, Point point) {
        super(player);
        this.point = point;
    }

    @Override
    public void perform() throws IllegalStateException {
        checkState(point.isUnoccupied(), "Expected %s to be empty.", point.getId());
        AddPieceAction.create(player.getPiece()).perform(point);
    }

    @Override
    public Point getUpdatedPoint() {
        return point;
    }

    public String pretty() {
        return point.getId();
    }

    public static PlacePiece create(BasePlayer player, Point point) {
        return new PlacePiece(player, point);
    }
}
