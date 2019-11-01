package info.jayharris.ninemensmorris;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {

    private final Map<Coordinate, Point> points;
    private final Supplier<Set<Mill>> getMillsMemoized = new Supplier<Set<Mill>>() {

        Set<Mill> mills = null;

        @Override
        public Set<Mill> get() {
            if (mills == null) {
                ImmutableSet.Builder<Mill> builder = ImmutableSet.builder();

                Stream.<Stream<String>>builder()
                        .add(Stream.of("a7", "d7", "g7"))
                        .add(Stream.of("b6", "d6", "f6"))
                        .add(Stream.of("c5", "d5", "e5"))
                        .add(Stream.of("a4", "b4", "c4"))
                        .add(Stream.of("e4", "f4", "g4"))
                        .add(Stream.of("c3", "d3", "e3"))
                        .add(Stream.of("b2", "d2", "f2"))
                        .add(Stream.of("a1", "d1", "g1"))
                        .add(Stream.of("a7", "a4", "a1"))
                        .add(Stream.of("b6", "b4", "b2"))
                        .add(Stream.of("c5", "c4", "c3"))
                        .add(Stream.of("d7", "d6", "d5"))
                        .add(Stream.of("d3", "d2", "d1"))
                        .add(Stream.of("e5", "e4", "e3"))
                        .add(Stream.of("f6", "f4", "f2"))
                        .add(Stream.of("g7", "g4", "g1"))
                        .build()
                        .map(stream -> stream.map(Coordinate::get))
                        .map(stream -> stream.collect(Collectors.toSet()))
                        .map(Mill::new)
                        .forEach(builder::add);
                mills = builder.build();
            }
            return mills;
        }
    };

    public Board() {
        this.points = Coordinate.valid().stream()
                .collect(Collectors.toMap(Function.identity(), Point::new));
    }

    public Point getPoint(String point) {
        return getPoint(Coordinate.get(point));
    }

    public Point getPoint(Coordinate point) {
        return points.get(point);
    }

    public Set<Point> getOccupiedPoints(Piece piece) {
        return points()
                .filter(point -> point.getPiece() == piece)
                .collect(Collectors.toSet());
    }

    public Set<Point> getUnoccupiedPoints() {
        return points()
                .filter(Point::isUnoccupied)
                .collect(Collectors.toSet());
    }

    public Set<Mill> getMills() {
        return getMillsMemoized.get();
    }

    public Set<Mill> getMillsAt(Coordinate coordinate) {
        return getMillsAt(getPoint(coordinate));
    }

    public Set<Mill> getMillsAt(Point point) {
        return point.getMills();
    }

    public Stream<Point> points() {
        return points.values().stream();
    }

    public boolean isCompleteMill(Point point) {
        return point.getMills().stream().anyMatch(mill -> mill.isComplete(point.getPiece()));
    }

    public class Point {
        Piece piece;
        private Coordinate coordinate;

        Point(Coordinate coordinate) {
            this.coordinate = coordinate;
        }

        public Piece getPiece() {
            return piece;
        }

        public void setPiece(Piece piece) {
            this.piece = piece;
        }

        public boolean isUnoccupied() {
            return piece == null;
        }

        public Set<Point> getNeighbors() {
            return Coordinate.neighbors.get(coordinate).stream()
                    .map(points::get)
                    .collect(Collectors.toSet());
        }

        Set<Mill> getMills() {
            return Coordinate.mills.get(coordinate).stream()
                    .map(Mill::new)
                    .collect(Collectors.toSet());
        }

        public String algebraicNotation() { return coordinate.pretty(); }

        public Coordinate getCoordinate() {
            return coordinate;
        }

        String pretty() {
            return piece == null ? "+" : piece.pretty();
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Point.class.getSimpleName() + "[", "]")
                    .add("piece=" + piece)
                    .add("coordinate='" + coordinate + "'")
                    .toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return Objects.equals(coordinate, point.coordinate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(coordinate);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;

        return Coordinate.COORDINATES.stream()
                .allMatch(c -> this.getPoint(c).getPiece() == board.getPoint(c).getPiece());
    }

    public class Mill {
        Set<Point> points;

        Mill(Set<Coordinate> points) {
            this.points = points.stream().map(Board.this.points::get).collect(Collectors.toSet());
        }

        boolean isComplete(Piece piece) {
            return points.stream().allMatch(point -> point.getPiece() == piece);
        }

        public Set<Point> getPoints() {
            return points;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(points.entrySet()
                .stream()
                .filter(e -> e.getValue().getPiece() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getPiece())));
    }

    public static Board copy(Board original) {
        Board copy = new Board();

        original.points().forEach(point -> copy.getPoint(point.coordinate).setPiece(point.getPiece()));

        return copy;
    }

    public String pretty() {
        return prettyPrinterSupplier.get().print();
    }

    private Supplier<PrettyPrinter> prettyPrinterSupplier = Suppliers.memoize(PrettyPrinter::new);

    private class PrettyPrinter {

        String template = "\n" +
                          "7  x --------------- x --------------- x\n" +
                          "   |                 |                 |\n" +
                          "   |                 |                 |\n" +
                          "6  |     x --------- x --------- x     |\n" +
                          "   |     |           |           |     |\n" +
                          "   |     |           |           |     |\n" +
                          "5  |     |     x --- x --- x     |     |\n" +
                          "   |     |     |           |     |     |\n" +
                          "   |     |     |           |     |     |\n" +
                          "4  x --- x --- x           x --- x --- x\n" +
                          "   |     |     |           |     |     |\n" +
                          "   |     |     |           |     |     |\n" +
                          "3  |     |     x --- x --- x     |     |\n" +
                          "   |     |           |           |     |\n" +
                          "   |     |           |           |     |\n" +
                          "2  |     x --------- x --------- x     |\n" +
                          "   |                 |                 |\n" +
                          "   |                 |                 |\n" +
                          "1  x --------------- x --------------- x\n\n" +
                          "   a     b     c     d     e     f     g";
        Pattern pattern = Pattern.compile("x");

        String print() {
            StringBuffer sb = new StringBuffer();
            Iterator<Point> iter = Coordinate.ALGEBRAIC_NOTATIONS_FOR_COORDINATES.stream()
                    .map(Coordinate::get)
                    .map(points::get)
                    .iterator();

            Matcher matcher = pattern.matcher(template);
            while (matcher.find()) {
                matcher.appendReplacement(sb, iter.next().pretty());
            }
            matcher.appendTail(sb);
            return sb.toString();
        }
    }
}
