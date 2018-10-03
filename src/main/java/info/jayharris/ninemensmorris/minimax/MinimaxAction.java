package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.Action;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.InitialMove;

import java.util.Optional;

public class MinimaxAction implements Action<MinimaxState, MinimaxAction> {

    public InitialMove initialMove;
    public Optional<CapturePiece> captureMove;

    public MinimaxAction(InitialMove initialMove) {
        this.initialMove = initialMove;
    }

    private MinimaxAction(InitialMove initialMove, CapturePiece captureMove) {
        this.initialMove = initialMove;
        this.captureMove = Optional.of(captureMove);
    }

    public MinimaxAction withNoCapture() {
        this.captureMove = Optional.empty();
        return this;
    }

    public MinimaxAction withCapture(CapturePiece capturePiece) {
        return new MinimaxAction(initialMove, capturePiece);
    }

    @Override
    public MinimaxState apply(MinimaxState initialState) {
        return null;
    }

    public static MinimaxAction createWithInitialMove(InitialMove initialMove) {
        return new MinimaxAction(initialMove);
    }
}
