package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.BasePlayer;
import info.jayharris.ninemensmorris.Board.Point;

import static com.google.common.base.Preconditions.checkState;

public class MovePiece extends FlyPiece {

    public MovePiece(BasePlayer player, Point initial, Point destination) {
        super(player, initial, destination);
    }

    @Override
    public void perform() throws IllegalStateException {
        checkState(initial.getNeighbors().contains(destination), "Points %s and %s are not adjacent", initial.getId(), destination.getId());

        super.perform();
    }

    public static MovePiece create(BasePlayer player, Point initial, Point destination) {
        return new MovePiece(player, initial, destination);
    }
}
