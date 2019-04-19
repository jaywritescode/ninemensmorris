package info.jayharris.ninemensmorris.minimax.feature;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.minimax.MinimaxState;
import info.jayharris.ninemensmorris.player.PlayerAdapter;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static info.jayharris.ninemensmorris.Piece.BLACK;
import static info.jayharris.ninemensmorris.Piece.WHITE;

class CountMillsFeatureTest {

    @Test
    void apply() throws Exception {
        Board board = BoardBuilder.create()
                .withPiece("a7", WHITE)
                .withPiece("d7", BLACK)
                .withPiece("g7", BLACK)
                .withPiece("f6", BLACK)
                .withPiece("c5", WHITE)
                .withPiece("d5", BLACK)
                .withPiece("a4", WHITE)
                .withPiece("b4", WHITE)
                .withPiece("c4", WHITE)
                .withPiece("f4", BLACK)
                .withPiece("c3", WHITE)
                .withPiece("d3", BLACK)
                .withPiece("e3", BLACK)
                .withPiece("d2", WHITE)
                .withPiece("a1", WHITE)
                .build();
        /*
            7  ○ --------------- ● --------------- ●
               |                 |                 |
               |                 |                 |
            6  |     + --------- + --------- ●     |
               |     |           |           |     |
               |     |           |           |     |
            5  |     |     ○ --- ● --- +     |     |
               |     |     |           |     |     |
               |     |     |           |     |     |
            4  ○ --- ○ --- ○           + --- ● --- +
               |     |     |           |     |     |
               |     |     |           |     |     |
            3  |     |     ○ --- ● --- ●     |     |
               |     |           |           |     |
               |     |           |           |     |
            2  |     + --------- ○ --------- +     |
               |                 |                 |
               |                 |                 |
            1  ○ --------------- + --------------- +

               a     b     c     d     e     f     g
         */

        MinimaxState state = MinimaxState.create(board, new PlayerAdapter(BLACK));

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(new CountMillsFeature(WHITE).apply(state)).isEqualTo(3);
        softly.assertThat(new CountMillsFeature(BLACK).apply(state)).isEqualTo(0);
        softly.assertAll();
    }
}