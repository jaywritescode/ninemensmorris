package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Piece;

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

    public MinimaxState build() {
        return new MinimaxState(board, toMove, playerPieces);
    }
}
