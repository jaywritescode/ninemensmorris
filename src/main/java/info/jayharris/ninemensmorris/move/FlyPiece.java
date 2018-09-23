package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.BasePlayer;
import info.jayharris.ninemensmorris.Board.Point;

import static com.google.common.base.Preconditions.checkState;

public class FlyPiece extends BaseMove implements InitialMove {

    final Point initial, destination;

    public FlyPiece(BasePlayer player, Point initial, Point destination) {
        super(player);
        this.initial = initial;
        this.destination = destination;
    }

    @Override
    public void perform() throws IllegalStateException {
        checkState(initial.getPiece() == player.getPiece(), "Expected a %s piece on point %s, instead found %s", player.getPiece().toString(), initial.getId(), initial.getPiece().toString());
        checkState(!destination.isOccupied(), "Expected point %s to be empty", destination.getId());

        RemovePieceAction.create().perform(initial);
        AddPieceAction.create(player.getPiece()).perform(destination);
    }

    @Override
    public Point getUpdatedPoint() {
        return destination;
    }

    public static FlyPiece create(BasePlayer player, Point initial, Point destination) {
        return new FlyPiece(player, initial, destination);
    }
}
