package Algorithms;

import Maze.Maze;
import Maze.MazeNode;

import java.util.LinkedList;

public class DFS {
    private Maze maze;

    public DFS(Maze maze) {
        this.maze = maze;
    }

    public LinkedList<MazeNode> findPath(MazeNode startVertex, MazeNode endVertex) {
        if (startVertex == null || endVertex == null) {
            System.err.println("Invalid starting or ending vertex for DFS.");
            return null;
        }
        // Initialize all nodes
        for (MazeNode node : maze) {
            node.setVisited(false);
        }

        LinkedList<MazeNode> path = new LinkedList<>();
        LinkedList<MazeNode> allVisitedNodes = new LinkedList<>();

        if (dfsHelper(startVertex, endVertex, path, allVisitedNodes)) {
            // Print all visited nodes
            System.out.print("All visited nodes: ");
            for (MazeNode node : allVisitedNodes) {
                System.out.print(node + " -> ");
            }
            System.out.println("End");

            return path;
        } else {
            System.err.println("No path found from start to end vertex using DFS.");
            return null;
        }
    }

    private boolean dfsHelper(MazeNode currentVertex, MazeNode endVertex, LinkedList<MazeNode> path, LinkedList<MazeNode> allVisitedNodes) {
        currentVertex.setVisited(true);
        path.add(currentVertex);
        allVisitedNodes.add(currentVertex);

        if (currentVertex == endVertex) {
            return true;
        }

        // Store the first vertex when entering the first iteration
        MazeNode firstVertex = allVisitedNodes.getFirst();

        LinkedList<MazeNode> neighborList = currentVertex.getNeighborList();
        boolean hasUnvisitedNeighbor = false;

        for (MazeNode neighbor : neighborList) {
            if (!neighbor.getVisited()) {
                hasUnvisitedNeighbor = true;
                if (dfsHelper(neighbor, endVertex, path, allVisitedNodes)) {
                    return true;
                }
            }
        }

        path.removeLast();

        if (!hasUnvisitedNeighbor && currentVertex == firstVertex) {
            allVisitedNodes.add(firstVertex);
        }

        return false;
    }
}
