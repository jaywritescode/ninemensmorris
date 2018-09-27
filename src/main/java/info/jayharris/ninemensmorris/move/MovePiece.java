package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.player.BasePlayer;

public class MovePiece extends FlyPiece {

    private MovePiece(BasePlayer player, Point initial, Point destination) {
        super(player, initial, destination);
    }

    @Override
    public void perform() throws IllegalStateException {
        validateLegal();
        super.perform();
    }

    @Override
    public void validateLegal() {
        super.validateLegal();
        if (initial.getNeighbors().contains(destination)) {
            throw IllegalMoveException.create("Points %s and %s are not adjacent.", initial.getId(),
                                              destination.getId());
        }
    }

    public static MovePiece create(BasePlayer player, Point initial, Point destination) {
        return new MovePiece(player, initial, destination);
    }

    public static MovePiece createLegal(BasePlayer player, Point initial, Point destination) {
        MovePiece move = create(player, initial, destination);

        move.validateLegal();
        return move;
    }
}
