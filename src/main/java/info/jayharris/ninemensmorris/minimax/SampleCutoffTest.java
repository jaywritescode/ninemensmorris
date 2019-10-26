package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.CutoffTest;
import info.jayharris.minimax.Node;

/**
 * A simple cutoff test that terminates a search when it queries a node at least
 * three ancestors from the root.
 */
public class SampleCutoffTest extends CutoffTest<MinimaxState, MinimaxAction> {
    @Override
    public boolean test(Node<MinimaxState, MinimaxAction> node) {
        return node.getDepth() >= 3;
    }
}
