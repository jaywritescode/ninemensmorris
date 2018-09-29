package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.player.BasePlayer;

public final class PlacePiece extends BaseMove implements InitialMove {

    private final Point point;

    public final static String ILLEGAL_MOVE_TEMPLATE = "Can't place a piece on occupied point %s.";

    private PlacePiece(BasePlayer player, Point point) {
        super(player);
        this.point = point;
    }

    @Override
    public void perform() {
        validateLegal();
        AddPieceAction.create(player.getPiece()).perform(point);
    }

    @Override
    public Point getUpdatedPoint() {
        return point;
    }

    @Override
    public void validateLegal() throws IllegalMoveException {
        if (!point.isUnoccupied()) {
            throw IllegalMoveException.create(ILLEGAL_MOVE_TEMPLATE, point.getId());
        }
    }

    public String pretty() {
        return point.getId();
    }

    public static PlacePiece create(BasePlayer player, Point point) {
        return new PlacePiece(player, point);
    }

    public static PlacePiece createLegal(BasePlayer player, Point point) throws IllegalMoveException {
        PlacePiece move = create(player, point);

        move.validateLegal();
        return move;
    }
}
