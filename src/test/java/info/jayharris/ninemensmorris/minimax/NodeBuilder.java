package info.jayharris.ninemensmorris.minimax;

import info.jayharris.minimax.search.Node;

import java.lang.reflect.Field;

public class NodeBuilder {

    private MinimaxState state;
    private int depth = 0;

    private final Field depthField;

    private NodeBuilder() throws NoSuchFieldException {
        depthField = Node.class.getDeclaredField("depth");
        depthField.setAccessible(true);
    }

    public NodeBuilder withState(MinimaxState state) {
        this.state = state;
        return this;
    }

    public NodeBuilder withDepth(int depth) {
        this.depth = depth;
        return this;
    }

    public Node<MinimaxState, MinimaxAction> build() throws IllegalAccessException {
        Node<MinimaxState, MinimaxAction> node = Node.createRootNode(state, n -> 0.0);
        if (depth != 0) {
            depthField.set(node, depth);
        }
        return node;
    }

    public static NodeBuilder create() throws NoSuchFieldException {
        return new NodeBuilder();
    }
}
