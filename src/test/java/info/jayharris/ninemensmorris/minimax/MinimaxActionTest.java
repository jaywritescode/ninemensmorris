package info.jayharris.ninemensmorris.minimax;

import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Coordinate;
import info.jayharris.ninemensmorris.Piece;
import org.assertj.core.api.Condition;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

class MinimaxActionTest {

    @Test
    @DisplayName("place piece — black, no capture")
    void placePieceBlackNoCapture() throws Exception {
        Board board = BoardBuilder.create()
                .withPiece("b4", Piece.BLACK)
                .withPiece("d6", Piece.WHITE)
                .withPiece("c3", Piece.BLACK)
                .withPiece("a7", Piece.WHITE)
                .build();
        /*
            7  ○ --------------- + --------------- +
               |                 |                 |
               |                 |                 |
            6  |     + --------- + --------- +     |
               |     |           |           |     |
               |     |           |           |     |
            5  |     |     + --- ○ --- +     |     |
               |     |     |           |     |     |
               |     |     |           |     |     |
            4  + --- ● --- +           + --- + --- +
               |     |     |           |     |     |
               |     |     |           |     |     |
            3  |     |     ● --- + --- +     |     |
               |     |           |           |     |
               |     |           |           |     |
            2  |     + --------- + --------- +     |
               |                 |                 |
               |                 |                 |
            1  + --------------- + --------------- +

               a     b     c     d     e     f     g
             */
        MinimaxState currentState = MinimaxStateBuilder.create()
                .withBoard(board)
                .withToMove(Piece.BLACK)
                .withPlayerPieces(7)
                .build();

        Coordinate coordinate = Coordinate.get("g1");
        MinimaxAction action = MinimaxAction.fromPlacePiece(coordinate);

        MinimaxState nextState = action.perform(currentState);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(nextState.getBoard()).isNotSameAs(currentState.getBoard());
        softly.assertThat(nextState.getToMove()).isEqualTo(Piece.WHITE);
        softly.assertThat(nextState.getPlayerPieces()).isEqualTo(7);
        softly.assertThat(nextState.getBoard()).is(matchingPieceOnPoint(Piece.BLACK, coordinate));

        softly.assertAll();
    }

    @Test
    @DisplayName("place piece — white, no capture")
    void placePieceWhiteNoCapture() throws Exception {
        Board board = BoardBuilder.create()
                .withPiece("b4", Piece.BLACK)
                .withPiece("d6", Piece.WHITE)
                .withPiece("c3", Piece.BLACK)
                .withPiece("a7", Piece.WHITE)
                .withPiece("g1", Piece.BLACK)
                .build();
        /*
            7  ○ --------------- + --------------- +
               |                 |                 |
               |                 |                 |
            6  |     + --------- + --------- +     |
               |     |           |           |     |
               |     |           |           |     |
            5  |     |     + --- ○ --- +     |     |
               |     |     |           |     |     |
               |     |     |           |     |     |
            4  + --- ● --- +           + --- + --- +
               |     |     |           |     |     |
               |     |     |           |     |     |
            3  |     |     ● --- + --- +     |     |
               |     |           |           |     |
               |     |           |           |     |
            2  |     + --------- + --------- +     |
               |                 |                 |
               |                 |                 |
            1  + --------------- + --------------- ●

               a     b     c     d     e     f     g
             */
        MinimaxState currentState = MinimaxStateBuilder.create()
                .withBoard(board)
                .withToMove(Piece.WHITE)
                .withPlayerPieces(7)
                .build();

        Coordinate coordinate = Coordinate.get("f6");
        MinimaxAction action = MinimaxAction.fromPlacePiece(coordinate);

        MinimaxState nextState = action.perform(currentState);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(nextState.getBoard()).isNotSameAs(currentState.getBoard());
        softly.assertThat(nextState.getToMove()).isEqualTo(Piece.BLACK);
        softly.assertThat(nextState.getPlayerPieces()).isEqualTo(6);
        softly.assertThat(nextState.getBoard()).is(matchingPieceOnPoint(Piece.WHITE, coordinate));

        softly.assertAll();
    }

    @Test
    @DisplayName("place piece — with capture")
    void placePieceWithCapture() throws Exception {
        Board board = BoardBuilder.create()
                .withPiece("a7", Piece.WHITE)
                .withPiece("d6", Piece.BLACK)
                .withPiece("d5", Piece.WHITE)
                .withPiece("b4", Piece.WHITE)
                .withPiece("c4", Piece.BLACK)
                .withPiece("f4", Piece.WHITE)
                .withPiece("d3", Piece.BLACK)
                .withPiece("e3", Piece.BLACK)
                .build();
        /*
            7  ○ --------------- + --------------- +
               |                 |                 |
               |                 |                 |
            6  |     + --------- ● --------- +     |
               |     |           |           |     |
               |     |           |           |     |
            5  |     |     + --- ○ --- +     |     |
               |     |     |           |     |     |
               |     |     |           |     |     |
            4  + --- ○ --- ●           + --- ○ --- +
               |     |     |           |     |     |
               |     |     |           |     |     |
            3  |     |     + --- ● --- ●     |     |
               |     |           |           |     |
               |     |           |           |     |
            2  |     + --------- + --------- +     |
               |                 |                 |
               |                 |                 |
            1  + --------------- + --------------- +

               a     b     c     d     e     f     g
             */
        MinimaxState currentState = MinimaxStateBuilder.create()
                .withBoard(board)
                .withToMove(Piece.BLACK)
                .withPlayerPieces(5)
                .build();

        Coordinate place = Coordinate.get("c3");
        Coordinate capture = Coordinate.get("b4");
        MinimaxAction action = MinimaxAction.fromPlacePiece(place).withCapture(capture);

        MinimaxState nextState = action.perform(currentState);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(nextState.getToMove()).isEqualTo(Piece.WHITE);
        softly.assertThat(nextState.getPlayerPieces()).isEqualTo(5);
        softly.assertThat(nextState.getBoard()).is(matchingPieceOnPoint(Piece.BLACK, place));
        softly.assertThat(nextState.getBoard()).is(matchingPieceOnPoint(null, capture));

        softly.assertAll();
    }

    @Test
    @DisplayName("move piece - no capture")
    void movePieceNoCapture() throws Exception {
        Board board = BoardBuilder.create()
                .withPiece("a7", Piece.WHITE)
                .withPiece("d6", Piece.WHITE)
                .withPiece("c5", Piece.BLACK)
                .withPiece("d5", Piece.BLACK)
                .withPiece("a4", Piece.BLACK)
                .withPiece("b4", Piece.BLACK)
                .withPiece("c4", Piece.BLACK)
                .withPiece("e4", Piece.WHITE)
                .withPiece("f4", Piece.BLACK)
                .withPiece("g4", Piece.WHITE)
                .withPiece("c3", Piece.BLACK)
                .withPiece("d3", Piece.WHITE)
                .withPiece("b2", Piece.WHITE)
                .withPiece("d2", Piece.BLACK)
                .withPiece("a1", Piece.WHITE)
                .withPiece("d1", Piece.BLACK)
                .build();
        /*
        7  ○ --------------- + --------------- +
           |                 |                 |
           |                 |                 |
        6  |     + --------- ○ --------- +     |
           |     |           |           |     |
           |     |           |           |     |
        5  |     |     ● --- ● --- +     |     |
           |     |     |           |     |     |
           |     |     |           |     |     |
        4  ● --- ● --- ●           ○ --- ● --- ○
           |     |     |           |     |     |
           |     |     |           |     |     |
        3  |     |     ● --- ○ --- +     |     |
           |     |           |           |     |
           |     |           |           |     |
        2  |     ○ --------- ● --------- +     |
           |                 |                 |
           |                 |                 |
        1  ○ --------------- ● --------------- +

           a     b     c     d     e     f     g
         */
        MinimaxState currentState = MinimaxStateBuilder.create()
                .withBoard(board)
                .withPlayerPieces(0)
                .withToMove(Piece.BLACK)
                .build();

        Coordinate from = Coordinate.get("b4");
        Coordinate to = Coordinate.get("b6");

        MinimaxAction action = MinimaxAction.fromMovePiece(from, to);

        MinimaxState nextState = action.perform(currentState);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(nextState.getToMove()).isEqualTo(Piece.WHITE);
        softly.assertThat(nextState.getPlayerPieces()).isZero();
        softly.assertThat(nextState.getBoard()).is(matchingPieceOnPoint(null, from));
        softly.assertThat(nextState.getBoard()).is(matchingPieceOnPoint(Piece.BLACK, to));
        softly.assertThat(nextState)
                .extracting("stalemateChecker.stateHistory")
                .contains(nextState.getBoard());
    }

    @Test
    @DisplayName("move piece - with capture")
    @org.junit.jupiter.api.Disabled
    public void movePieceWithCapture() {
        
    }

    Condition<Board> matchingPieceOnPoint(Piece piece, Coordinate coordinate) {
        Predicate<Board> predicate = board -> board.getPoint(coordinate).getPiece() == piece;

        return new Condition<>(predicate,
                "point [%s] is %s", coordinate.pretty(), piece);
    }
}