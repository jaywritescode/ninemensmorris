package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Piece;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ComparativeMobilityHeuristicFunctionTest {

    @Test
    void applyAsDouble() throws Exception {
        Board board = BoardBuilder.create()
                .withPiece("d7", Piece.BLACK)
                .withPiece("d5", Piece.BLACK)
                .withPiece("e5", Piece.WHITE)
                .withPiece("b4", Piece.BLACK)
                .withPiece("c4", Piece.WHITE)
                .withPiece("e3", Piece.WHITE)
                .build();
        MinimaxState state = MinimaxStateBuilder.create()
                .withBoard(board)
                .withPlayerPieces(6)
                .withToMove(Piece.BLACK)
                .build();
        /*
        7  + --------------- ● --------------- +
           |                 |                 |
           |                 |                 |
        6  |     + --------- + --------- +     |
           |     |           |           |     |
           |     |           |           |     |
        5  |     |     + --- ● --- ○     |     |
           |     |     |           |     |     |
           |     |     |           |     |     |
        4  + --- ● --- ○           + --- + --- +
           |     |     |           |     |     |
           |     |     |           |     |     |
        3  |     |     + --- + --- ○     |     |
           |     |           |           |     |
           |     |           |           |     |
        2  |     + --------- + --------- +     |
           |                 |                 |
           |                 |                 |
        1  + --------------- + --------------- +

           a     b     c     d     e     f     g
         */

        ComparativeMobilityHeuristicFunction fn = new ComparativeMobilityHeuristicFunction(Piece.BLACK);
        assertThat(fn.applyAsDouble(state)).isEqualTo(8.0/5.0);
    }
}