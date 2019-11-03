package info.jayharris.ninemensmorris;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.function.Predicate;

public class StalemateChecker {
    
    private final Multiset<Board> states;
    private final Predicate<Board> isStalemate;

    public StalemateChecker() {
        states = HashMultiset.create();
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
}
