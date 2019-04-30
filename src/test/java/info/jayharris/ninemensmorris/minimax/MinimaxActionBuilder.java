package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Coordinate;

import java.lang.reflect.Constructor;

public class MinimaxActionBuilder {

    Coordinate placePiece;
    Coordinate movePieceFrom;
    Coordinate movePieceTo;
    Coordinate capturePiece;

    public MinimaxActionBuilder withPlacePiece(Coordinate placePiece) {
        this.placePiece = placePiece;
        return this;
    }

    public MinimaxActionBuilder withMovePieceFrom(Coordinate movePieceFrom) {
        this.movePieceFrom = movePieceFrom;
        return this;
    }

    public MinimaxActionBuilder withMovePieceTo(Coordinate movePieceTo) {
        this.movePieceTo = movePieceTo;
        return this;
    }

    public MinimaxActionBuilder withCapturePiece(Coordinate capturePiece) {
        this.capturePiece = capturePiece;
        return this;
    }

    public MinimaxAction build() {
        if (placePiece != null) {
            return MinimaxAction.fromPlacePiece(placePiece).withCapture(capturePiece);
        }

        return MinimaxAction.fromMovePiece(movePieceFrom, movePieceTo).withCapture(capturePiece);
    }

    public static MinimaxActionBuilder create() {
        return new MinimaxActionBuilder();
    }
}
