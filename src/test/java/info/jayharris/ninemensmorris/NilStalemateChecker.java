package info.jayharris.ninemensmorris;

public class NilStalemateChecker extends StalemateChecker {

    private NilStalemateChecker() {
        super();
    }

    @Override
    public boolean hasStalemateState() {
        return false;
    }

    @Override
    public boolean isStalemateState(Board board) {
        return false;
    }

    public static NilStalemateChecker create() {
        return new NilStalemateChecker();
    }
}

