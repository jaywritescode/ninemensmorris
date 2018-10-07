package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.Action;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.InitialMove;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.StringJoiner;

public class MinimaxAction implements Action<MinimaxState, MinimaxAction> {

    private InitialMove initialMove;
    private Optional<CapturePiece> captureMove;

    private MinimaxAction(Builder builder) {
        this.initialMove = builder.initialMove;
        this.captureMove = Optional.ofNullable(builder.capturePiece);
    }

    @Override
    public MinimaxState apply(MinimaxState initialState) {
        return null;
    }

    public InitialMove getInitialMove() {
        return initialMove;
    }

    public Optional<CapturePiece> getCaptureMove() {
        return captureMove;
    }

    public String pretty() {
        return initialMove.pretty() + getCaptureMove().map(CapturePiece::pretty).orElse(StringUtils.EMPTY);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MinimaxAction.class.getSimpleName() + "[", "]")
                .add("initialMove=" + initialMove)
                .add("captureMove=" + captureMove)
                .toString();
    }

    public static MinimaxAction.Builder createWithInitialMove(InitialMove initialMove) {
        return new Builder().withInitialMove(initialMove);
    }

    public static class Builder {

        InitialMove initialMove;
        CapturePiece capturePiece;

        private Builder() { }

        public Builder withInitialMove(InitialMove initialMove) {
            this.initialMove = initialMove;
            return this;
        }

        public MinimaxAction withNoCapture() {
            return new MinimaxAction(this);
        }

        public MinimaxAction withCapture(CapturePiece capturePiece) {
            this.capturePiece = capturePiece;
            return new MinimaxAction(this);
        }
    }
}
