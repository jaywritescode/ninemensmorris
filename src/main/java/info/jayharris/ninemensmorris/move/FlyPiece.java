package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.player.BasePlayer;
import info.jayharris.ninemensmorris.Board.Point;

import static com.google.common.base.Preconditions.checkState;

public class FlyPiece extends BaseMove implements InitialMove {

    final Point initial, destination;

    FlyPiece(BasePlayer player, Point initial, Point destination) {
        super(player);
        this.initial = initial;
        this.destination = destination;
    }

    @Override
    public void perform() throws IllegalStateException {
        validateLegal();
        RemovePieceAction.create().perform(initial);
        AddPieceAction.create(player.getPiece()).perform(destination);
    }

    @Override
    public Point getUpdatedPoint() {
        return destination;
    }

    @Override
    public void validateLegal() throws IllegalMoveException {
        if (initial.getPiece() != player.getPiece()) {
            throw IllegalMoveException.create(
                    initial.isUnoccupied() ? "No piece on %s to move." : "Cannot move opponent's piece on %s.",
                    initial.getId());
        }
        if (!destination.isUnoccupied()) {
            throw IllegalMoveException.create("Cannot move to occupied point at %s.", destination.getId());
        }
    }

    public String pretty() {
        return initial.getId() + "-" + destination.getId();
    }

    public static FlyPiece create(BasePlayer player, Point initial, Point destination) {
        return new FlyPiece(player, initial, destination);
    }

    public static FlyPiece createLegal(BasePlayer player, Point initial, Point destination) {
        FlyPiece move = create(player, initial, destination);

        move.validateLegal();
        return move;
    }
}
