package info.jayharris.ninemensmorris.move;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.player.BasePlayer;

public final class MovePiece extends BaseMove implements InitialMove {

    private final Board board;
    private final Point initial, destination;

    private MovePiece(BasePlayer player, Board board, Point initial, Point destination) {
        super(player);
        this.board = board;
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
    public void validateLegal() {
        if (initial.getPiece() != player.getPiece()) {
            throw IllegalMoveException.create(
                    initial.isUnoccupied() ? "No piece on %s to move." : "Cannot move opponent's piece on %s.",
                    initial.getId());
        }
        if (!destination.isUnoccupied()) {
            throw IllegalMoveException.create("Cannot move to occupied point at %s.", destination.getId());
        }


        if (board.getOccupiedPoints(getPiece()).size() > 3 && !initial.getNeighbors().contains(destination)) {
            throw IllegalMoveException.create("Points %s and %s are not adjacent.", initial.getId(),
                                              destination.getId());
        }
    }

    public String pretty() {
        return initial.getId() + "-" + destination.getId();
    }

    public static MovePiece create(BasePlayer player, Board board, Point initial, Point destination) {
        return new MovePiece(player, board, initial, destination);
    }

    public static MovePiece createLegal(BasePlayer player, Board board, Point initial, Point destination) {
        MovePiece move = create(player, board, initial, destination);

        move.validateLegal();
        return move;
    }
}
