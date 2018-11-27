package info.jayharris.ninemensmorris.minimax.feature;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.minimax.MinimaxState;
import info.jayharris.ninemensmorris.player.PlayerAdapter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MobilityFeatureTest {

    @Test
    void apply() throws Exception {
        Board board = BoardBuilder.create()
                .withPiece("d2", Piece.BLACK)
                .withPiece("f4", Piece.WHITE)
                .withPiece("d5", Piece.BLACK)
                .withPiece("f6", Piece.WHITE)
                .withPiece("b4", Piece.BLACK)
                .withPiece("a7", Piece.WHITE)
                .build();
        MinimaxState state = MinimaxState.create(board, new PlayerAdapter(Piece.BLACK));

        assertThat(new MobilityFeature(Piece.WHITE).apply(state)).isEqualTo(6);
    }
}