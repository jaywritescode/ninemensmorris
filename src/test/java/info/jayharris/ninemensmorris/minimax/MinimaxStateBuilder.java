package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Piece;

import java.lang.reflect.Field;

public class MinimaxStateBuilder {

    Field boardField;
    Field playerPiecesField;

    Board board;
    Piece toMove;
    int playerPieces = 0;

    public MinimaxStateBuilder() throws NoSuchFieldException {
        boardField = MinimaxState.class.getDeclaredField("board");
        boardField.setAccessible(true);

        playerPiecesField = MinimaxState.class.getDeclaredField("playerPieces");
        playerPiecesField.setAccessible(true);
    }

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

    public MinimaxState build() throws IllegalAccessException {
        MinimaxState minimaxState = MinimaxState.initialState(toMove);

        boardField.set(minimaxState, this.board);
        playerPiecesField.setInt(minimaxState, this.playerPieces);

        return minimaxState;
    }
}
