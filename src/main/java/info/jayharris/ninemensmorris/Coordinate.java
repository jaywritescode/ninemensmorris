package info.jayharris.ninemensmorris;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Coordinate {

    // These need to be in order from top to bottom and left to right so the pretty-printer
    // can render the board.
    public static final List<String> ALGEBRAIC_NOTATIONS_FOR_COORDINATES =
            ImmutableList.of("a7", "d7", "g7", "b6", "d6", "f6", "c5", "d5", "e5", "a4", "b4", "c4", "e4",
                             "f4", "g4", "c3", "d3", "e3", "b2", "d2", "f2", "a1", "d1", "g1");

    static final Map<String, Coordinate> coordinates = ALGEBRAIC_NOTATIONS_FOR_COORDINATES.stream()
            .collect(Collectors.toMap(Function.identity(), Coordinate::new));

    public static final Collection<Coordinate> COORDINATES = coordinates.values();

    static final Multimap<Coordinate, Coordinate> neighbors;
    static {
        ImmutableSetMultimap.Builder<Coordinate, Coordinate> builder = ImmutableSetMultimap.builder();

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
                .map(pair -> Pair.of(get(pair.getLeft()), get(pair.getRight())))
                .forEach(pair -> {
                    builder.put(pair.getLeft(), pair.getRight());
                    builder.put(pair.getRight(), pair.getLeft());
                });
        neighbors = builder.build();
    }

    static final Multimap<Coordinate, Set<Coordinate>> mills;
    static {
        ImmutableSetMultimap.Builder<Coordinate, Set<Coordinate>> builder = ImmutableSetMultimap.builder();

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
                .forEach(set -> set.forEach(point -> builder.put(point, set)));
        mills = builder.build();
    }

    private final String id;

    private Coordinate(String id) {
        this.id = id;
    }

    public String pretty() {
        return id;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Coordinate.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Objects.equals(id, that.id);
    }

    public static Collection<Coordinate> valid() {
        return coordinates.values();
    }

    public static Coordinate get(String algebraicNotation) {
        return coordinates.get(algebraicNotation);
    }
}
