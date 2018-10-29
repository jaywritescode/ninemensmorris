package info.jayharris.ninemensmorris.minimax;

import com.google.common.collect.ImmutableSet;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Coordinate;
import info.jayharris.ninemensmorris.Piece;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class MinimaxStateTest {

    @Nested
    class Actions {

        Board board;

        @Test
        @DisplayName("#getPlacePieceActions")
        void getPlacePieceActions() throws Exception {
            Piece toMove = Piece.BLACK;
            board = BoardBuilder.create()
                    .withPiece("g7", Piece.WHITE)
                    .withPiece("c4", Piece.WHITE)
                    .withPiece("f4", Piece.BLACK)
                    .withPiece("g4", Piece.WHITE)
                    .withPiece("c3", Piece.BLACK)
                    .withPiece("d3", Piece.BLACK)
                    .withPiece("d2", Piece.BLACK)
                    .withPiece("f2", Piece.WHITE)
                    .withPiece("a1", Piece.WHITE)
                    .withPiece("g1", Piece.BLACK)
                    .build();

            /*
            7  + --------------- + --------------- ○
               |                 |                 |
               |                 |                 |
            6  |     + --------- + --------- +     |
               |     |           |           |     |
               |     |           |           |     |
            5  |     |     + --- + --- +     |     |
               |     |     |           |     |     |
               |     |     |           |     |     |
            4  + --- + --- ○           + --- ● --- ○
               |     |     |           |     |     |
               |     |     |           |     |     |
            3  |     |     ● --- ● --- +     |     |
               |     |           |           |     |
               |     |           |           |     |
            2  |     + --------- ● --------- ○     |
               |                 |                 |
               |                 |                 |
            1  ○ --------------- + --------------- ●

               a     b     c     d     e     f     g
             */
            Collection<MinimaxAction> actual = new MinimaxStateBuilder()
                    .withBoard(board)
                    .withPlayerPieces(4)
                    .withToMove(toMove)
                    .build()
                    .actions();

            Collection<MinimaxAction> expected = ImmutableSet.<MinimaxAction>builder()
                    .add(actionWithPlacePiece("a7"))
                    .add(actionWithPlacePiece("d7"))
                    .add(actionWithPlacePiece("b6"))
                    .add(actionWithPlacePiece("d6"))
                    .add(actionWithPlacePiece("f6"))
                    .add(actionWithPlacePiece("c5"))
                    .add(actionWithPlacePiece("d5"))
                    .add(actionWithPlacePiece("e5"))
                    .add(actionWithPlacePiece("a4"))
                    .add(actionWithPlacePiece("b4"))
                    .add(actionWithPlacePiece("e4"))
                    .add(actionWithPlacePiece("e3", "g7"))
                    .add(actionWithPlacePiece("e3", "c4"))
                    .add(actionWithPlacePiece("e3", "g4"))
                    .add(actionWithPlacePiece("e3", "f2"))
                    .add(actionWithPlacePiece("e3", "a1"))
                    .add(actionWithPlacePiece("b2"))
                    .add(actionWithPlacePiece("d1", "g7"))
                    .add(actionWithPlacePiece("d1", "c4"))
                    .add(actionWithPlacePiece("d1", "g4"))
                    .add(actionWithPlacePiece("d1", "f2"))
                    .add(actionWithPlacePiece("d1", "a1"))
                    .build();

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        @DisplayName("#getMovePieceActions — > 3 pieces")
        void getMovePieceActions_tryMovePieceToNeighbor() throws Exception {
            Piece toMove = Piece.BLACK;
            board = BoardBuilder.create()
                    .withPiece("a7", Piece.WHITE)
                    .withPiece("d7", Piece.WHITE)
                    .withPiece("g7", Piece.BLACK)
                    .withPiece("b6", Piece.WHITE)
                    .withPiece("d6", Piece.WHITE)
                    .withPiece("c5", Piece.WHITE)
                    .withPiece("b4", Piece.WHITE)
                    .withPiece("c4", Piece.BLACK)
                    .withPiece("e4", Piece.BLACK)
                    .withPiece("f4", Piece.BLACK)
                    .withPiece("g4", Piece.BLACK)
                    .withPiece("d3", Piece.BLACK)
                    .withPiece("e3", Piece.BLACK)
                    .withPiece("b2", Piece.WHITE)
                    .withPiece("d1", Piece.BLACK)
                    .build();
            /*
            7  ○ --------------- ○ --------------- ●
               |                 |                 |
               |                 |                 |
            6  |     ○ --------- ○ --------- +     |
               |     |           |           |     |
               |     |           |           |     |
            5  |     |     ○ --- + --- +     |     |
               |     |     |           |     |     |
               |     |     |           |     |     |
            4  + --- ○ --- ●           ● --- ● --- ●
               |     |     |           |     |     |
               |     |     |           |     |     |
            3  |     |     + --- ● --- ●     |     |
               |     |           |           |     |
               |     |           |           |     |
            2  |     ○ --------- + --------- +     |
               |                 |                 |
               |                 |                 |
            1  + --------------- ● --------------- +

               a     b     c     d     e     f     g
             */
            Collection<MinimaxAction> actual = new MinimaxStateBuilder()
                    .withBoard(board)
                    .withPlayerPieces(0)
                    .withToMove(toMove)
                    .build()
                    .actions();

            Collection<MinimaxAction> expected = ImmutableSet.<MinimaxAction>builder()
                    .add(actionWithMovePiece("c4", "c3", "a7"))
                    .add(actionWithMovePiece("c4", "c3", "d7"))
                    .add(actionWithMovePiece("c4", "c3", "b6"))
                    .add(actionWithMovePiece("c4", "c3", "d6"))
                    .add(actionWithMovePiece("c4", "c3", "c5"))
                    .add(actionWithMovePiece("c4", "c3", "b4"))
                    .add(actionWithMovePiece("c4", "c3", "b2"))
                    .add(actionWithMovePiece("e4", "e5"))
                    .add(actionWithMovePiece("f4", "f6"))
                    .add(actionWithMovePiece("f4", "f2"))
                    .add(actionWithMovePiece("g4", "g1"))
                    .add(actionWithMovePiece("d3", "c3"))
                    .add(actionWithMovePiece("d3", "d2"))
                    .add(actionWithMovePiece("d1", "a1"))
                    .add(actionWithMovePiece("d1", "d2"))
                    .add(actionWithMovePiece("d1", "g1", "a7"))
                    .add(actionWithMovePiece("d1", "g1", "d7"))
                    .add(actionWithMovePiece("d1", "g1", "b6"))
                    .add(actionWithMovePiece("d1", "g1", "d6"))
                    .add(actionWithMovePiece("d1", "g1", "c5"))
                    .add(actionWithMovePiece("d1", "g1", "b4"))
                    .add(actionWithMovePiece("d1", "g1", "b2"))
                    .build();

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        @DisplayName("#getMovePieceActions — 3 pieces")
        void getMovePieceActions_tryMovePieceAnywhere() throws Exception {
            Piece toMove = Piece.WHITE;
            board = BoardBuilder.create()
                    .withPiece("g7", Piece.BLACK)
                    .withPiece("b6", Piece.WHITE)
                    .withPiece("d6", Piece.WHITE)
                    .withPiece("d5", Piece.WHITE)
                    .withPiece("a4", Piece.BLACK)
                    .withPiece("b4", Piece.BLACK)
                    .withPiece("c4", Piece.BLACK)
                    .withPiece("f4", Piece.BLACK)
                    .withPiece("d3", Piece.BLACK)
                    .withPiece("b2", Piece.BLACK)
                    .withPiece("f2", Piece.BLACK)
                    .build();
            /*
            7  + --------------- + --------------- ●
               |                 |                 |
               |                 |                 |
            6  |     ○ --------- ○ --------- +     |
               |     |           |           |     |
               |     |           |           |     |
            5  |     |     + --- ○ --- +     |     |
               |     |     |           |     |     |
               |     |     |           |     |     |
            4  ● --- ● --- ●           + --- ● --- +
               |     |     |           |     |     |
               |     |     |           |     |     |
            3  |     |     + --- ● --- +     |     |
               |     |           |           |     |
               |     |           |           |     |
            2  |     ● --------- + --------- ●     |
               |                 |                 |
               |                 |                 |
            1  + --------------- + --------------- +

               a     b     c     d     e     f     g
             */
            Collection<MinimaxAction> actual = new MinimaxStateBuilder()
                    .withBoard(board)
                    .withPlayerPieces(0)
                    .withToMove(toMove)
                    .build()
                    .actions();

            Collection<MinimaxAction> expected = ImmutableSet.<MinimaxAction>builder()
                    .add(actionWithMovePiece("b6", "a7"))
                    .add(actionWithMovePiece("b6", "d7", "g7"))
                    .add(actionWithMovePiece("b6", "d7", "a4"))
                    .add(actionWithMovePiece("b6", "d7", "b4"))
                    .add(actionWithMovePiece("b6", "d7", "c4"))
                    .add(actionWithMovePiece("b6", "d7", "f4"))
                    .add(actionWithMovePiece("b6", "d7", "d3"))
                    .add(actionWithMovePiece("b6", "d7", "b2"))
                    .add(actionWithMovePiece("b6", "d7", "f2"))
                    .add(actionWithMovePiece("b6", "f6"))
                    .add(actionWithMovePiece("b6", "c5"))
                    .add(actionWithMovePiece("b6", "e5"))
                    .add(actionWithMovePiece("b6", "e4"))
                    .add(actionWithMovePiece("b6", "g4"))
                    .add(actionWithMovePiece("b6", "c3"))
                    .add(actionWithMovePiece("b6", "e3"))
                    .add(actionWithMovePiece("b6", "d2"))
                    .add(actionWithMovePiece("b6", "a1"))
                    .add(actionWithMovePiece("b6", "d1"))
                    .add(actionWithMovePiece("b6", "g1"))
                    .add(actionWithMovePiece("d6", "a7"))
                    .add(actionWithMovePiece("d6", "d7"))
                    .add(actionWithMovePiece("d6", "f6"))
                    .add(actionWithMovePiece("d6", "c5"))
                    .add(actionWithMovePiece("d6", "e5"))
                    .add(actionWithMovePiece("d6", "e4"))
                    .add(actionWithMovePiece("d6", "g4"))
                    .add(actionWithMovePiece("d6", "c3"))
                    .add(actionWithMovePiece("d6", "e3"))
                    .add(actionWithMovePiece("d6", "d2"))
                    .add(actionWithMovePiece("d6", "a1"))
                    .add(actionWithMovePiece("d6", "d1"))
                    .add(actionWithMovePiece("d6", "g1"))
                    .add(actionWithMovePiece("d5", "a7"))
                    .add(actionWithMovePiece("d5", "d7"))
                    .add(actionWithMovePiece("d5", "f6","g7"))
                    .add(actionWithMovePiece("d5", "f6","a4"))
                    .add(actionWithMovePiece("d5", "f6","b4"))
                    .add(actionWithMovePiece("d5", "f6","c4"))
                    .add(actionWithMovePiece("d5", "f6","f4"))
                    .add(actionWithMovePiece("d5", "f6","d3"))
                    .add(actionWithMovePiece("d5", "f6","b2"))
                    .add(actionWithMovePiece("d5", "f6","f2"))
                    .add(actionWithMovePiece("d5", "c5"))
                    .add(actionWithMovePiece("d5", "e5"))
                    .add(actionWithMovePiece("d5", "e4"))
                    .add(actionWithMovePiece("d5", "g4"))
                    .add(actionWithMovePiece("d5", "c3"))
                    .add(actionWithMovePiece("d5", "e3"))
                    .add(actionWithMovePiece("d5", "d2"))
                    .add(actionWithMovePiece("d5", "a1"))
                    .add(actionWithMovePiece("d5", "d1"))
                    .add(actionWithMovePiece("d5", "g1"))
                    .build();

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }

        MinimaxAction actionWithPlacePiece(String point) {
            return actionWithPlacePiece(point, null);
        }

        MinimaxAction actionWithPlacePiece(String point, String capture) {
            MinimaxAction action = MinimaxAction.fromPlacePiece(Coordinate.get(point));

            return capture == null ? action : action.withCapture(Coordinate.get(capture));
        }

        MinimaxAction actionWithMovePiece(String init, String dest) {
            return actionWithMovePiece(init, dest, null);
        }

        MinimaxAction actionWithMovePiece(String init, String dest, String capture) {
            MinimaxAction action = MinimaxAction.fromMovePiece(Coordinate.get(init), Coordinate.get(dest));
            
            return capture == null ? action : action.withCapture(Coordinate.get(capture));
        }
    }
}