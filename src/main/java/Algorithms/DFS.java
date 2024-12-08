package Algorithms;

import Maze.Maze;
import Maze.MazeNode;

import java.util.LinkedList;

public class DFS {
    private final Maze maze;
    private LinkedList<MazeNode> dfsPath = new LinkedList<MazeNode>();

    public DFS(Maze maze) {
        this.maze = maze;
    }

    public LinkedList<MazeNode> findPath(MazeNode startVertex, MazeNode endVertex) {
        if (startVertex == null || endVertex == null) {
            System.err.println("Invalid starting or ending vertex for DFS.");
            return null;
        }
        //Initialize all nodes
        for (MazeNode node : maze) {
            node.setVisited(false);
        }
        dfsPath.clear();
        dfsHelper(startVertex, endVertex);
        return dfsPath;
    }

    private void dfsHelper(MazeNode currentVertex, MazeNode endVertex) {
        currentVertex.setVisited(true);
        if (currentVertex == endVertex) {
            dfsPath.addFirst(currentVertex);
            return;
        }
        LinkedList<MazeNode> neighbor_list = currentVertex.getNeighborList();
        for (MazeNode neighbor : neighbor_list) {
            if( endVertex.getVisited() ) break;
            if( neighbor.getVisited() == false ) {
                /* visit every node exactly once */
                dfsHelper( neighbor, endVertex );
            }
        }
        if( endVertex.getVisited() ) {
            /* popping from RTS stack -- save sequence of nodes */
            dfsPath.addFirst( currentVertex );
        }
    }
}