package info.jayharris.ninemensmorris.minimax.feature;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.NilStalemateChecker;
import info.jayharris.ninemensmorris.minimax.MinimaxState;
import info.jayharris.ninemensmorris.player.PlayerAdapter;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static info.jayharris.ninemensmorris.Piece.BLACK;
import static info.jayharris.ninemensmorris.Piece.WHITE;

class MobilityFeatureTest {

    @Test
    void apply() throws Exception {
        Board board = BoardBuilder.create()
                .withPiece("d2", WHITE)
                .withPiece("f4", WHITE)
                .withPiece("f6", WHITE)
                .withPiece("a7", WHITE)
                .withPiece("b4", BLACK)
                .withPiece("d5", BLACK)
                .withPiece("d3", BLACK)
                .withPiece("c5", BLACK)
                .build();
        /*
            7  ○ --------------- + --------------- +
               |                 |                 |
               |                 |                 |
            6  |     + --------- + --------- ○     |
               |     |           |           |     |
               |     |           |           |     |
            5  |     |     ● --- ● --- +     |     |
               |     |     |           |     |     |
               |     |     |           |     |     |
            4  + --- ● --- +           + --- ○ --- +
               |     |     |           |     |     |
               |     |     |           |     |     |
            3  |     |     + --- ● --- +     |     |
               |     |           |           |     |
               |     |           |           |     |
            2  |     + --------- ○ --------- +     |
               |                 |                 |
               |                 |                 |
            1  + --------------- + --------------- +

               a     b     c     d     e     f     g
             */
        MinimaxState state = MinimaxState.create(board, new PlayerAdapter(BLACK), NilStalemateChecker.create());

        SoftAssertions softly = new SoftAssertions();

        /*
        a7 -> d7, a7 -> a4
        f6 -> d6
        f4 -> e4, f4 -> f2, f4 -> g4
        d2 -> f2, d2 -> d1, d2 -> c2
         */
        softly.assertThat(new MobilityFeature(WHITE).apply(state)).isEqualTo(9);

        /*
        d5 -> d6, d5 -> e5
        b4 -> a4, b4 -> b6, b4 -> b2, b4 -> c4
        c5 -> c4
        d3 -> c3, d3 -> e3
         */
        softly.assertThat(new MobilityFeature(BLACK).apply(state)).isEqualTo(9);

        softly.assertAll();
    }
}