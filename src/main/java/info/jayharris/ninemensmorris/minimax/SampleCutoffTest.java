package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.search.Node;
import info.jayharris.minimax.search.cutoff.CutoffTest;

/**
 * A simple cutoff test that terminates a search when it queries a node at least
 * three ancestors from the root.
 */
public class SampleCutoffTest extends CutoffTest<MinimaxState, MinimaxAction> {
    @Override
    public boolean cutoffSearch(Node<MinimaxState, MinimaxAction> node) {
        return node.getDepth() >= 3;
    }
}
