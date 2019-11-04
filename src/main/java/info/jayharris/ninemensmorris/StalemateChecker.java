package info.jayharris.ninemensmorris;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.function.Predicate;

/**
 * The stalemate checker tracks the history of all the board states since the
 * "move piece" phase of the game began. If the same board state appears three
 * times in the history, then the game is a stalemate.
 */
public class StalemateChecker {
    
    private final Multiset<Board> states;
    private final Predicate<Board> isStalemate;

    protected StalemateChecker() {
        states = HashMultiset.create();
        isStalemate = board -> states.count(board) >= 3;
    }

    private StalemateChecker(StalemateChecker original) {
        states = HashMultiset.create(original.states);
        isStalemate = board -> states.count(board) >= 3;
    }

    public void accept(Board board) {
        states.add(board);
    }

    public boolean hasStalemateState() {
        return states.elementSet().stream().anyMatch(isStalemate);
    }

    public boolean isStalemateState(Board board) {
        return isStalemate.test(board);
    }

    public static StalemateChecker create() {
        return new StalemateChecker();
    }

    public static StalemateChecker copy(StalemateChecker original) {
        return new StalemateChecker(original);
    }
}
