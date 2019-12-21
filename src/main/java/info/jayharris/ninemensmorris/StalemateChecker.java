package info.jayharris.ninemensmorris;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/**
 * The stalemate checker tracks the history of all the board states since the
 * "move piece" phase of the game began. If we are in a board state that has
 * already appeared in the game history, then the game is a draw.
 */
public class StalemateChecker {
    
    private final Multiset<Board> stateHistory;

    private boolean isStalemate;

    protected StalemateChecker() {
        stateHistory = HashMultiset.create();
        isStalemate = false;
    }

    private StalemateChecker(StalemateChecker original) {
        stateHistory = HashMultiset.create(original.stateHistory);
        isStalemate = original.isStalemate;
    }

    /**
     * Takes the given {@code Board} and checks if its state already appears in the
     * game history.
     *
     * We always add the given {@code Board} to the history.
     *
     * @param board the board
     */
    public void accept(Board board) {
        if(stateHistory.contains(board)) {
            isStalemate = true;
        }

        stateHistory.add(board);
    }

    public boolean isStalemate() {
        return isStalemate;
    }

    public static StalemateChecker create() {
        return new StalemateChecker();
    }

    public static StalemateChecker copy(StalemateChecker original) {
        return new StalemateChecker(original);
    }
}
