package info.jayharris.ninemensmorris;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {

    public static final List<String> ALGEBRAIC_NOTATIONS_FOR_POINTS = ImmutableList.of("a7", "d7", "g7", "b6", "d6", "f6", "c5", "d5", "e5", "a4", "b4", "c4", "e4", "f4", "g4", "c3", "d3", "e3", "b2", "d2", "f2", "a1", "d1", "g1");

    private final Map<String, Point> points;

    private static final Multimap<String, String> neighbors;
    static {
        ImmutableSetMultimap.Builder<String, String> builder = ImmutableSetMultimap.builder();

        Stream.<Pair<String, String>>builder()
                .add(Pair.of("a7","d7"))
                .add(Pair.of("d7","g7"))
                .add(Pair.of("b6","d6"))
                .add(Pair.of("d6","f6"))
                .add(Pair.of("c5","d5"))
                .add(Pair.of("d5","e5"))
                .add(Pair.of("a4","b4"))
                .add(Pair.of("b4","c4"))
                .add(Pair.of("e4","f4"))
                .add(Pair.of("f4","g4"))
                .add(Pair.of("c3","d3"))
                .add(Pair.of("d3","e3"))
                .add(Pair.of("b2","d2"))
                .add(Pair.of("d2","f2"))
                .add(Pair.of("a1","d1"))
                .add(Pair.of("d1","g1"))
                .add(Pair.of("a7","a4"))
                .add(Pair.of("a4","a1"))
                .add(Pair.of("b6","b4"))
                .add(Pair.of("b4","b2"))
                .add(Pair.of("c5","c4"))
                .add(Pair.of("c4","c3"))
                .add(Pair.of("d7","d6"))
                .add(Pair.of("d6","d5"))
                .add(Pair.of("d3","d2"))
                .add(Pair.of("d2","d1"))
                .add(Pair.of("e5","e4"))
                .add(Pair.of("e4","e3"))
                .add(Pair.of("f6","f4"))
                .add(Pair.of("f4","f2"))
                .add(Pair.of("g7","g4"))
                .add(Pair.of("g4","g1"))
                .build()
                .forEach(pair -> {
                    builder.put(pair.getLeft(), pair.getRight());
                    builder.put(pair.getRight(), pair.getLeft());
                });
        neighbors = builder.build();
    }

    private static final Multimap<String, Set<String>> mills;
    static {
        ImmutableSetMultimap.Builder<String, Set<String>> builder = ImmutableSetMultimap.builder();

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
                .map(stream -> stream.collect(Collectors.toSet()))
                .forEach(set -> set.forEach(point -> builder.put(point, set)));
        mills = builder.build();
    }

    public Board() {
        this.points = ALGEBRAIC_NOTATIONS_FOR_POINTS.stream().collect(Collectors.toMap(Function.identity(), Point::new));
    }

    public Set<Point> getOccupiedPoints(Piece piece) {
        return points.values().stream()
                .filter(point -> point.getPiece() == piece)
                .collect(Collectors.toSet());
    }

    public Set<Point> getUnoccupiedPoints() {
        return points.values().stream()
                .filter(point -> point.isUnoccupied())
                .collect(Collectors.toSet());
    }

    boolean isCompleteMill(Point point) {
        return point.getMills().stream().anyMatch(mill -> mill.isComplete(point.getPiece()));
    }

    public class Point {
        Piece piece;
        private String id;

        Point(String id) {
            this.id = id;
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
            return neighbors.get(id).stream()
                    .map(points::get)
                    .collect(Collectors.toSet());
        }

        public Set<Mill> getMills() {
            return mills.get(id).stream().map(Mill::new).collect(Collectors.toSet());
        }

        public String getId() {
            return id;
        }

        String pretty() {
            return piece == null ? "+" : piece.pretty();
        }
    }

    class Mill {
        Set<Point> points;

        Mill(Set<String> points) {
            this.points = points.stream().map(Board.this.points::get).collect(Collectors.toSet());
        }

        boolean isComplete(Piece piece) {
            return points.stream().allMatch(point -> point.getPiece() == piece);
        }
    }

    String pretty() {
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
            Iterator<Point> iter = ALGEBRAIC_NOTATIONS_FOR_POINTS.stream()
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
