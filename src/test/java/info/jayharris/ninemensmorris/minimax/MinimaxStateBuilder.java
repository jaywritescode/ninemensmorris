package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Piece;

import java.lang.reflect.Constructor;

public class MinimaxStateBuilder {

    Board board;
    Piece toMove;
    int playerPieces = 0;

    public MinimaxStateBuilder withBoard(Board board) {
        this.board = board;
        return this;
    }

    public MinimaxStateBuilder withToMove(Piece toMove) {
        this.toMove = toMove;
        return this;
    }

    public MinimaxStateBuilder withPlayerPieces(int playerPieces) {
        this.playerPieces = playerPieces;
        return this;
    }

    public MinimaxState build() throws Exception {
        Constructor<MinimaxState> ctor = MinimaxState.class.getDeclaredConstructor(Board.class, Piece.class, Integer.TYPE);
        ctor.setAccessible(true);

        return ctor.newInstance(board, toMove, playerPieces);
    }

    public static MinimaxStateBuilder create() {
        return new MinimaxStateBuilder();
    }
}
