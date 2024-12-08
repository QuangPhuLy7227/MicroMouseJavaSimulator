package utility;

import Maze.MazeNode;

import java.util.Stack;

public class DisjointSet {
    public void makeSet(MazeNode node) {
        node.parent = node;
        node.rank = 0;
    }

    public MazeNode find(MazeNode vertex) {
//        Stack<MazeNode> stack = new Stack<MazeNode>();
//        stack.push( vertex );
//        while( vertex.parent != null ) {
//            /* find set representative */
//            vertex = vertex.parent;
//            stack.push( vertex );
//        }
//        MazeNode root = stack.pop();
//
//        while( !stack.empty() ) {
//            /* path compression */
//            vertex = stack.pop();
//            vertex.parent = root;
//            vertex.rank = 0;
//        }
//        return root;
        MazeNode root = vertex;

        // Find the root of the set
        while (root.parent != root) {
            root = root.parent;
        }

        // Path compression
        MazeNode current = vertex;
        while (current != root) {
            MazeNode next = current.parent;
            current.parent = root;
            current = next;
        }

        return root;
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
            if (xRoot.rank == yRoot.rank) {
                //Union of tree of equal height
                yRoot.rank = xRoot.rank + 1;
            }
        }
    }

    public boolean inSameSet(MazeNode x, MazeNode y) {
        return find(x) == find(y);
    }
}
