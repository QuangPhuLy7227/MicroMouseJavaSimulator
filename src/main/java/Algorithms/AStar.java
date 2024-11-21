package Algorithms;

import Maze.Maze;
import Maze.MazeNode;
import utility.PQNode;

import java.util.LinkedList;
import java.util.PriorityQueue;

import static java.lang.Math.*;

public class AStar {
    private final Maze maze;

    public AStar(Maze maze) {
        this.maze = maze;
    }

    public LinkedList<MazeNode> findPath(MazeNode startVertex, MazeNode endVertex) {
        if (startVertex == null || endVertex == null) {
            System.err.println("Invalid starting or ending vertex for AStar.");
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
            int distance =currentNode.getDistance();

            if (!currentNode.visited){
                currentNode.setVisited(true);
                LinkedList<MazeNode> neighbor_list = currentNode.getNeighborList();
                for (MazeNode neighbor : neighbor_list){
                    int weight = 1; //since this is an unweighted graph
                    int difference = (int) round(sqrt(pow(endVertex.x - currentNode.x,2)
                            + pow(endVertex.y - currentNode.y,2))); //  get distance from current to last
                    int cost = distance + weight + difference;
                    if (cost < neighbor.getDistance()){
                        neighbor.setDistance(cost);
                        neighbor.setPrev(currentNode);
                        pq.add(new PQNode<>(cost, neighbor));
                    }
                }
            }
        }

        //Reconstruct path
        LinkedList<MazeNode> path = new LinkedList<>();
        MazeNode currentNode = endVertex;
        while (currentNode != null){
            path.addFirst(currentNode);
            currentNode = currentNode.getPrev();
        }
        //Check if a path is found
        if (path.isEmpty() || path.getFirst() != startVertex){
            System.err.println("No path found from start to end vertex using AStar.");
            return null;
        }
        return path;
    }
}
