package Algorithms;

import Maze.Maze;
import Maze.MazeNode;
import utility.PQNode;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

public class Dijkstra {
    private final Maze maze;
    private LinkedList<MazeNode> dijkstraPath = new LinkedList<MazeNode>();

    public Dijkstra(Maze maze) {
        this.maze = maze;
    }

    public LinkedList<MazeNode> findPath(MazeNode startVertex, MazeNode endVertex) {
        if (startVertex == null || endVertex == null) {
            System.err.println("Invalid starting or ending vertex for Dijkstra.");
            return null;
        }
        // Initialize all nodes
        for (MazeNode node : maze) {
            node.setDistance(Integer.MAX_VALUE);
            node.setPrev(null);
            node.setVisited(false);
        }
        PriorityQueue<PQNode<MazeNode>> pq = new PriorityQueue<>();
        startVertex.setDistance(0);
        pq.add(new PQNode<>(0, startVertex));

        while (!pq.isEmpty()) {
            PQNode<MazeNode> pqNode = pq.poll();
            MazeNode currentNode = pqNode.getData();
            int distance = currentNode.getDistance();

            if (currentNode.visited == false) {
                currentNode.setVisited(true);
                LinkedList<MazeNode> neighborList = currentNode.getNeighborList();

                for (MazeNode neighbor : neighborList) {
                    int weight = 1; // Assuming uniform cost for all edges
                    int cost = distance + weight;

                    if (cost < neighbor.getDistance()) {
                        neighbor.setDistance(cost);
                        neighbor.setPrev(currentNode);
                        pq.add(new PQNode<>(cost, neighbor));
                    }
                }
            }
        }

        dijkstraPath.clear();
        Stack<MazeNode> pathStack = new Stack<MazeNode>();
        MazeNode currentNode = endVertex;

        while (currentNode.getPrev() != null) {
            /* traversing optimal path backwards */
            pathStack.push(currentNode);
            currentNode = currentNode.getPrev();
        }
        /* pushing starting vertex */
        pathStack.push(currentNode);

        while (!pathStack.empty()) {
            /* dijkstra path : startVertex to endVertex */
            currentNode = pathStack.pop();
            dijkstraPath.addLast(currentNode);
        }
        return dijkstraPath;
    }
}