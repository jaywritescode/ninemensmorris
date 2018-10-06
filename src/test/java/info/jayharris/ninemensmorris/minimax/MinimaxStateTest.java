package info.jayharris.ninemensmorris.minimax;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class MinimaxStateTest {

    @Nested
    class Actions {

        @Test
        @Disabled
        @DisplayName("#getPlacePieceActions")
        void getPlacePieceActions() throws Exception {
            Board board = BoardBuilder.create()
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

            Set<Point> pointsThatWillCapture = ImmutableSet.of(board.getPoint("d1"), board.getPoint("e3"));
            Set<Point> pointsThatWontCapture = Sets.difference(board.getUnoccupiedPoints(), pointsThatWillCapture);

            Collection<MinimaxAction> actual = new MinimaxStateBuilder()
                    .withBoard(board)
                    .withPlayerPieces(4)
                    .withToMove(Piece.BLACK)
                    .build()
                    .actions();

            Collection<MinimaxAction> expected = Sets.newHashSet();

            expected.addAll(pointsThatWontCapture.stream()
                                    .map(point -> PlacePiece.create(Piece.BLACK, point))
                                    .map(MinimaxAction::createWithInitialMove)
                                    .map(MinimaxAction.Builder::withNoCapture)
                                    .collect(Collectors.toSet()));
            expected.addAll(pointsThatWillCapture.stream()
                                    .map(point -> PlacePiece.create(Piece.BLACK, point))
                                    .map(MinimaxAction::createWithInitialMove)
                                    .flatMap(action -> board.getOccupiedPoints(Piece.WHITE).stream()
                                            .map(point -> CapturePiece.create(Piece.BLACK, point))
                                            .map(action::withCapture))
                                    .collect(Collectors.toSet()));

            assertThat(actual).isEqualTo(expected);
        }
    }
}