package info.jayharris.ninemensmorris.minimax;

import com.google.common.collect.ImmutableSet;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class MinimaxStateTest {

    Comparator<MinimaxAction> minimaxActionComparator = new MinimaxActionComparator();

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
                    .add(actionWithPlacePiece(toMove, "a7"))
                    .add(actionWithPlacePiece(toMove, "d7"))
                    .add(actionWithPlacePiece(toMove, "b6"))
                    .add(actionWithPlacePiece(toMove, "d6"))
                    .add(actionWithPlacePiece(toMove, "f6"))
                    .add(actionWithPlacePiece(toMove, "c5"))
                    .add(actionWithPlacePiece(toMove, "d5"))
                    .add(actionWithPlacePiece(toMove, "e5"))
                    .add(actionWithPlacePiece(toMove, "a4"))
                    .add(actionWithPlacePiece(toMove, "b4"))
                    .add(actionWithPlacePiece(toMove, "e4"))
                    .add(actionWithPlacePiece(toMove, "e3", "g7"))
                    .add(actionWithPlacePiece(toMove, "e3", "c4"))
                    .add(actionWithPlacePiece(toMove, "e3", "g4"))
                    .add(actionWithPlacePiece(toMove, "e3", "f2"))
                    .add(actionWithPlacePiece(toMove, "e3", "a1"))
                    .add(actionWithPlacePiece(toMove, "b2"))
                    .add(actionWithPlacePiece(toMove, "d1", "g7"))
                    .add(actionWithPlacePiece(toMove, "d1", "c4"))
                    .add(actionWithPlacePiece(toMove, "d1", "g4"))
                    .add(actionWithPlacePiece(toMove, "d1", "f2"))
                    .add(actionWithPlacePiece(toMove, "d1", "a1"))
                    .build();

            assertThat(actual)
                    .usingElementComparator(minimaxActionComparator)
                    .containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        @DisplayName("#getMovePieceActions — > 3 pieces")
        void getMovePieceActions() throws Exception {
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
                    .add(actionWithMovePiece(toMove, "c4", "c3", "a7"))
                    .add(actionWithMovePiece(toMove, "c4", "c3", "d7"))
                    .add(actionWithMovePiece(toMove, "c4", "c3", "b6"))
                    .add(actionWithMovePiece(toMove, "c4", "c3", "d6"))
                    .add(actionWithMovePiece(toMove, "c4", "c3", "c5"))
                    .add(actionWithMovePiece(toMove, "c4", "c3", "b4"))
                    .add(actionWithMovePiece(toMove, "c4", "c3", "b2"))
                    .add(actionWithMovePiece(toMove, "e4", "e5"))
                    .add(actionWithMovePiece(toMove, "f4", "f6"))
                    .add(actionWithMovePiece(toMove, "f4", "f2"))
                    .add(actionWithMovePiece(toMove, "g4", "g1"))
                    .add(actionWithMovePiece(toMove, "d3", "c3"))
                    .add(actionWithMovePiece(toMove, "d3", "d2"))
                    .add(actionWithMovePiece(toMove, "d1", "a1"))
                    .add(actionWithMovePiece(toMove, "d1", "d2"))
                    .add(actionWithMovePiece(toMove, "d1", "g1", "a7"))
                    .add(actionWithMovePiece(toMove, "d1", "g1", "d7"))
                    .add(actionWithMovePiece(toMove, "d1", "g1", "b6"))
                    .add(actionWithMovePiece(toMove, "d1", "g1", "d6"))
                    .add(actionWithMovePiece(toMove, "d1", "g1", "c5"))
                    .add(actionWithMovePiece(toMove, "d1", "g1", "b4"))
                    .add(actionWithMovePiece(toMove, "d1", "g1", "b2"))
                    .build();

            assertThat(actual)
                    .usingElementComparator(minimaxActionComparator)
                    .containsExactlyInAnyOrderElementsOf(expected);
        }

        MinimaxAction actionWithPlacePiece(Piece toMove, String point) {
            return actionWithPlacePiece(toMove, point, null);
        }

        MinimaxAction actionWithPlacePiece(Piece toMove, String point, String capture) {
            PlacePiece p = PlacePiece.create(toMove, board.getPoint(point));

            if (capture == null) {
                return MinimaxAction.createWithInitialMove(p).withNoCapture();
            }

            CapturePiece c = CapturePiece.create(toMove, board.getPoint(capture));
            return MinimaxAction.createWithInitialMove(p).withCapture(c);
        }

        MinimaxAction actionWithMovePiece(Piece toMove, String init, String dest) {
            return actionWithMovePiece(toMove, init, dest, null);
        }

        MinimaxAction actionWithMovePiece(Piece toMove, String init, String dest, String capture) {
            MovePiece m = MovePiece.create(toMove, board, board.getPoint(init), board.getPoint(dest));

            if (capture == null) {
                return MinimaxAction.createWithInitialMove(m).withNoCapture();
            }

            CapturePiece c = CapturePiece.create(toMove, board.getPoint(capture));
            return MinimaxAction.createWithInitialMove(m).withCapture(c);
        }
    }
    
    class MinimaxActionComparator implements Comparator<MinimaxAction> {

        @Override
        public int compare(MinimaxAction o1, MinimaxAction o2) {
            return Objects.equals(o1.getInitialMove(), o2.getInitialMove()) &&
                   Objects.equals(o1.getCaptureMove(), o2.getCaptureMove()) ? 0 : 1;
        }
    }
}