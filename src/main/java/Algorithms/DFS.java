package Algorithms;

import Maze.Maze;
import Maze.MazeNode;

import java.util.LinkedList;

public class DFS {
    private final Maze maze;

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
        LinkedList<MazeNode> path = new LinkedList<>();
        if (dfsHelper(startVertex, endVertex, path)) {
            return path;
        }else {
            System.err.println("No path found from start to end vertex using DFS.");
            return null;
        }
    }

    private boolean dfsHelper(MazeNode currentVertex, MazeNode endVertex, LinkedList<MazeNode> path) {
        currentVertex.setVisited(true);
        path.add(currentVertex);
        if (currentVertex == endVertex) {
            return true;
        }
        LinkedList<MazeNode> neighbor_list = currentVertex.getNeighborList();
        for (MazeNode neighbor : neighbor_list) {
            if (!neighbor.getVisited()){
                if (dfsHelper(neighbor, endVertex, path)) {
                    return true;
                }
            }
        }
        path.removeLast();
        return false;
    }
}
