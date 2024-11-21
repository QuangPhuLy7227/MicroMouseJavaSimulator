package utility;

import Maze.MazeNode;

import java.util.Stack;

public class DisjointSet {
    public void makeSet(MazeNode node) {
        node.parent = node;
        node.rank = 0;
    }

    public MazeNode find(MazeNode vertex) {
        if (vertex.parent != vertex) {
            vertex.parent = find(vertex.parent); // Path compression
        }
        return vertex.parent;
    }


    public void union(MazeNode x, MazeNode y) {
        MazeNode xRoot = find(x);
        MazeNode yRoot = find(y);
        if (xRoot == yRoot) {
            return;
        }
        if (xRoot.rank < yRoot.rank) {
            xRoot.parent = yRoot;
        } else if (xRoot.rank > yRoot.rank) {
            yRoot.parent = xRoot;
        } else {
            //xRoot is a smaller tree than yRoot
            xRoot.parent = yRoot;
            //Union of tree of equal height
            yRoot.rank = xRoot.rank + 1;
        }
    }

    public boolean inSameSet(MazeNode x, MazeNode y) {
        return find(x) == find(y);
    }
}
