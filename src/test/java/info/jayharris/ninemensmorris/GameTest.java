package info.jayharris.ninemensmorris;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;
import info.jayharris.ninemensmorris.player.BasePlayer;
import info.jayharris.ninemensmorris.player.PlayerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Deque;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class GameTest {

    @Nested
    class NonStalemate {

        // a game that doesn't end in a stalemate
        final Deque<String> moves = Lists.newLinkedList(Splitter.on(' ').split(
                "d2 f4 d6 b4 d1 d3 c4 d7 e4 d5 g4 e5 " +
                        "c5 a4 a1 f6 f2 g7 g4-g1xd7 g7-g4 d2-b2 g4-g7 " +
                        "d1-d2xd3 g7-g4 d2-d1xf4 f6-f4 d1-d2xb4 a4-b4 " +
                        "d2-d1xf4 b4-a4 d6-f6 d5-d6 d1-d2xe5 a4-e3 e4-f4xg4"));
        BasePlayer black, white;

        @BeforeEach
        void setUp() throws Exception {
            black = new ListPlayer(Piece.BLACK, moves);
            white = new ListPlayer(Piece.WHITE, moves);
        }

        @Test
        @DisplayName("the game ends when it's over")
        void play() throws Exception {
            Game game = new Game(black, white);

            // assert that the game stops once there's a winner
            assertThatCode(game::play).doesNotThrowAnyException();

            // assert that the game doesn't end before it's over
            assertThat(moves).isEmpty();
        }

        @Test
        @DisplayName("it returns the correct winner")
        void winner() throws Exception {
            Game game = new Game(black, white);
            assertThat(game.play()).isSameAs(black);
        }
    }

    @Nested
    class Stalemate {

        final Deque<String> moves = Lists.newLinkedList(Splitter.on(' ').split(
                "b4 f4 d2 a4 d6 d5 e4 c3 d3 d1 c4 d7 g4 " +
                        "e5 c5 e3 f6 b2 b4-b6xf4 a4-a7 g4-g7 b2-b4 " +
                        "g7-g4 b4-b2 g4-g7 b2-b4 g7-g4 b4-b2"));
        BasePlayer black, white;

        @BeforeEach
        void setUp() throws Exception {
            black = new ListPlayer(Piece.BLACK, moves);
            white = new ListPlayer(Piece.WHITE, moves);
        }

        @Test
        @DisplayName("the game ends in a stalemate")
        void play() throws Exception {
            Game game = new Game(black, white);

            // assert that the game stops once there's a winner
            assertThatCode(game::play).doesNotThrowAnyException();

            // assert that the game doesn't end before it's over
            assertThat(moves).isEmpty();
        }

        @Test
        @DisplayName("there is no winner")
        void winner() throws Exception {
            Game game = new Game(black, white);
            assertThat(game.play()).isNull();
        }
    }

    /**
     * This player just runs through a pre-determined set of moves.
     */
    class ListPlayer extends PlayerAdapter {

        Deque<String> moves;

        ListPlayer(Piece piece, Deque<String> moves) throws NoSuchFieldException {
            super(piece);
            this.moves = moves;
        }

        @Override
        protected PlacePiece placePiece(Board board) {
            String m = moves.getFirst();

            // the stuff below is more or less shared with the terminal player and should be moved to a utility class
            Coordinate c = Coordinate.get(m.substring(0, 2));
            PlacePiece p = PlacePiece.create(piece, board.getPoint(c));

            if (m.length() == 2) {
                moves.remove();
            }

            return p;
        }

        @Override
        protected MovePiece movePiece(Board board) {
            String m = moves.getFirst();
            Coordinate from = Coordinate.get(m.substring(0, 2));
            Coordinate to = Coordinate.get(m.substring(3, 5));
            MovePiece p = MovePiece.create(piece, board.getPoint(from), board.getPoint(to), true);

            if (m.length() == 5) {
                moves.remove();
            }

            return p;
        }

        @Override
        protected CapturePiece capturePiece(Board board) {
            String m = moves.getFirst();
            Coordinate c = Coordinate.get(StringUtils.substring(m, -2));
            CapturePiece p = CapturePiece.create(piece, board.getPoint(c));

            moves.remove();

            return p;
        }
    }
}