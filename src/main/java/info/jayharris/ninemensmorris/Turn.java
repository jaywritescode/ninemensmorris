package info.jayharris.ninemensmorris;

import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.InitialMove;
import info.jayharris.ninemensmorris.player.BasePlayer;
import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkState;

/**
 * Represents a turn in the game.
 *
 * The turn is responsible for making sure a player only plays once per turn
 * and also handling whether or not the player should capture.
 */
public class Turn {

    private final BasePlayer player;
    private final Board board;

    private InitialMove initial;
    private CapturePiece capture;

    private Turn(BasePlayer player, Board board) {
        this.player = player;
        this.board = board;
    }

    /**
     * Performs the initial move, assuming one hasn't already been performed.
     */
    public Board doInitialMove(InitialMove move) {
        checkState(this.initial == null, "You can only move once per turn.");

        move.perform();

        this.initial = move;
        return board;
    }

    public Board doCaptureMove(CapturePiece move) {
        checkState(this.initial != null, "You must move before you can capture.");
        checkState(this.capture == null, "You can only capture one piece per turn.");
        checkState(board.isCompleteMill(initial.getUpdatedPoint()), "Point %s does not complete a mill", initial.getUpdatedPoint().toString());

        move.perform();

        this.capture = move;
        return board;
    }

    public Point getUpdatedPoint() {
        return initial.getUpdatedPoint();
    }

    public Class<? extends InitialMove> getMoveType() {
        return initial.getClass();
    }

    public String pretty() {
        return initial.pretty() + (capture == null ? StringUtils.EMPTY : capture.pretty());
    }

    public static Turn initialize(BasePlayer player, Board board) {
        return new Turn(player, board);
    }
}
