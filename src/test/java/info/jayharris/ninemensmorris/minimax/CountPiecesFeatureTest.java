package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.player.MinimaxPlayer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CountPiecesFeatureTest {

    @Test
    void apply() {
        Board board = BoardBuilder.create()
                .withPiece("d7", Piece.BLACK)
                .withPiece("b6", Piece.WHITE)
                .withPiece("d6", Piece.BLACK)
                .withPiece("f6", Piece.WHITE)
                .withPiece("c5", Piece.BLACK)
                .withPiece("d5", Piece.BLACK)
                .withPiece("e5", Piece.WHITE)
                .withPiece("a4", Piece.BLACK)
                .withPiece("b4", Piece.BLACK)
                .withPiece("f4", Piece.BLACK)
                .withPiece("g4", Piece.WHITE)
                .withPiece("b2", Piece.WHITE)
                .withPiece("d2", Piece.BLACK)
                .withPiece("f2", Piece.WHITE)
                .withPiece("a1", Piece.WHITE)
                .withPiece("g1", Piece.BLACK)
                .build();
        MinimaxState state = MinimaxState.create(board, new MinimaxPlayer(Piece.BLACK, b -> 0.0));

        assertThat(new CountPiecesFeature(Piece.WHITE).apply(state)).isEqualTo(7);
    }
}