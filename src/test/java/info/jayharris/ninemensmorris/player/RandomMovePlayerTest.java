package info.jayharris.ninemensmorris.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import info.jayharris.ninemensmorris.Board.Point;
import info.jayharris.ninemensmorris.BoardBuilder;
import info.jayharris.ninemensmorris.Coordinate;
import info.jayharris.ninemensmorris.Piece;
import info.jayharris.ninemensmorris.move.CapturePiece;
import info.jayharris.ninemensmorris.move.MovePiece;
import info.jayharris.ninemensmorris.move.PlacePiece;
import org.apache.commons.lang3.RandomUtils;
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

        PlacePiece move = player.placePiece(builder.build());

        assertThat(move).extracting("point")
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
            while (iter.hasNext()) {
                builder.withPiece(iter.next(), i < 3 ? player.getPiece() : player.getPiece().opposite());
                ++i;
            }

            MovePiece move = player.movePiece(builder.build());

            assertThat(move)
                    .extracting("initial")
                    .first()
                    .hasFieldOrPropertyWithValue("piece", player.getPiece());
            assertThat(move)
                    .extracting("destination")
                    .first()
                    .hasFieldOrPropertyWithValue("piece", null);
        }

        @RepeatedTest(10)
        @DisplayName("player has more than three pieces on the board")
        void testMoveToNeighbor() {
            Iterator<String> iter = randomPoints(RandomUtils.nextInt(9, 15)).iterator();

            int i = 0;
            while (iter.hasNext()) {
                builder.withPiece(iter.next(), i < 9
                        ? (i % 2 == 0 ? Piece.BLACK : Piece.WHITE)
                        : (RandomUtils.nextBoolean() ? Piece.BLACK : Piece.WHITE));
                ++i;
            }

            // ensure that at least one legal move exists
            Point point = builder.findArbitraryPoint(player.getPiece());
            Point neighbor = point.getNeighbors().iterator().next();
            builder.withPiece(neighbor, null);

            MovePiece move = player.movePiece(builder.build());

            assertThat(move).extracting("initial")
                    .first()
                    .hasFieldOrPropertyWithValue("piece", player.getPiece());
            assertThat(move).extracting("destination")
                    .first()
                    .hasFieldOrPropertyWithValue("piece", null);
            assertThat(move).extracting("initial")
                    .first()
                    .isIn(move.getUpdatedPoint().getNeighbors());
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

        CapturePiece move = player.capturePiece(builder.build());

        assertThat(move)
                .extracting("point")
                .first()
                .hasFieldOrPropertyWithValue("piece", Piece.WHITE);
    }

    private Set<String> randomPoints(int count) {
        List<String> points = Lists.newArrayList(Coordinate.ALGEBRAIC_NOTATIONS_FOR_COORDINATES);
        Collections.shuffle(points);

        return Sets.newHashSet(points.subList(0, count - 1));
    }
}