package info.jayharris.ninemensmorris.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import info.jayharris.ninemensmorris.Board;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Coordinate;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.Move;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Condition;
import org.assertj.core.data.Index;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RandomMovePlayerTest {

    private RandomMovePlayer player;
    private BoardBuilder builder;

    @BeforeEach
    void setUp() {
        player = new RandomMovePlayer(Piece.BLACK);
        builder = BoardBuilder.create();
    }

    @RepeatedTest(10)
    @DisplayName("it always places a piece on an empty point")
    void placePiece() {
        randomPoints(RandomUtils.nextInt(8, 16)).stream()
                .forEach(point -> builder.withPiece(point, RandomUtils.nextBoolean() ? Piece.WHITE : Piece.BLACK));

        Board board = builder.build();
        PlacePiece move = player.placePiece(board);

        assertThat(move)
                .as(getDescription(board, move))
                .extracting("point")
                .first()
                .hasFieldOrPropertyWithValue("piece", null);
    }

    @Nested
    class MovePiecePhase {

        @RepeatedTest(10)
        @DisplayName("player only has three pieces on the board")
        void testFly() {
            Iterator<String> iter = randomPoints(RandomUtils.nextInt(6, 12)).iterator();

            int i = 0;
            for (; iter.hasNext(); ++i) {
                builder.withPiece(iter.next(), i < 3 ? player.getPiece() : player.getPiece().opposite());
            }

            Board board = builder.build();
            MovePiece move = player.movePiece(board);

            assertThat(move)
                    .as(getDescription(board, move))
                    .extracting("initial", "destination")
                    .has((Condition<? super Object>) playerMovesOwnPiece(move), Index.atIndex(0))
                    .has((Condition<? super Object>) playerMovesToEmptyPoint(move), Index.atIndex(1));
        }

        @RepeatedTest(10)
        @DisplayName("player has more than three pieces on the board")
        void testMoveToNeighbor() {
            Iterator<String> iter = randomPoints(RandomUtils.nextInt(9, 15)).iterator();

            int i = 0;
            for (; iter.hasNext(); ++i) {
                builder.withPiece(iter.next(), i < 9
                        ? (i % 2 == 0 ? Piece.BLACK : Piece.WHITE)
                        : (RandomUtils.nextBoolean() ? Piece.BLACK : Piece.WHITE));
            }

            // ensure that at least one legal move exists
            Point point = builder.findArbitraryPoint(player.getPiece());
            Point neighbor = point.getNeighbors().iterator().next();
            builder.withPiece(neighbor, null);

            Board board = builder.build();
            MovePiece move = player.movePiece(board);

            assertThat(move)
                    .as(getDescription(board, move))
                    .extracting("initial", "destination")
                    .has((Condition<? super Object>) playerMovesOwnPiece(move), Index.atIndex(0))
                    .has((Condition<? super Object>) playerMovesToNeighboringPoint(move), Index.atIndex(0))
                    .has((Condition<? super Object>) playerMovesToEmptyPoint(move), Index.atIndex(1));
        }

        private Condition<? super Point> playerMovesOwnPiece(MovePiece move) {
            return new Condition<>(p -> p.getPiece() == player.getPiece(),
                    "[%s] should start at a point with a %s piece.",
                    move.pretty(), player.getPiece());
        }

        private Condition<? super Point> playerMovesToNeighboringPoint(MovePiece move) {
            return new Condition<>(p -> p.getNeighbors().contains(move.getUpdatedPoint()),
                    "[%s] should start and end at neighboring points.", move.pretty());
        }

        private Condition<? super Point> playerMovesToEmptyPoint(MovePiece move) {
            return new Condition<>(p -> p.getPiece() == null,
                    "[%s] should end at an empty point", move.pretty());
        }
    }

    @RepeatedTest(10)
    @DisplayName("it always captures an opponent's piece")
    void capturePiece() {
        Iterator<String> iter = randomPoints(RandomUtils.nextInt(8, 12)).iterator();

        int i = 0;
        while (iter.hasNext()) {
            builder.withPiece(iter.next(), i < 6
                    ? (i % 2 == 0 ? Piece.BLACK : Piece.WHITE)
                    : (RandomUtils.nextBoolean() ? Piece.BLACK : Piece.WHITE));
            ++i;
        }

        Board board = builder.build();
        CapturePiece move = player.capturePiece(board);

        assertThat(move)
                .as(getDescription(board, move))
                .extracting("point")
                .first()
                .hasFieldOrPropertyWithValue("piece", Piece.WHITE);
    }

    private Set<String> randomPoints(int count) {
        List<String> points = Lists.newArrayList(Coordinate.ALGEBRAIC_NOTATIONS_FOR_COORDINATES);
        Collections.shuffle(points);

        return Sets.newHashSet(points.subList(0, count - 1));
    }

    private Description getDescription(Board board, Move move) {
        return new TextDescription("\nGiven board%s\nExpecting move [%s] to be legal.\n",
                board.pretty(), move.pretty());
    }
}