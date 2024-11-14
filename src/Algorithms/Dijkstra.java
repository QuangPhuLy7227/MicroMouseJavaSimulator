package Algorithms;

import Maze.Maze;
import Maze.MazeNode;
import utility.PQNode;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class Dijkstra {
    private Maze maze;

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
        pq.add(new PQNode<MazeNode>(0, startVertex));

//        while (!pq.isEmpty()) { //Phu
//            PQNode<MazeNode> pqNode = pq.poll();
//            MazeNode currentNode = pqNode.getData();
//            int distance =currentNode.getDistance();
//
//            if (currentNode.visited == false){
//                currentNode.setVisited(true);
//                LinkedList<MazeNode> neighbor_list = currentNode.getNeighborList();
//                for (MazeNode neighbor : neighbor_list){
//                    int weight = 1; //since this is an unweighted graph
//                    int cost = distance + weight;
//                    if (cost < neighbor.getDistance()){
//                        neighbor.setDistance(cost);
//                        neighbor.setPrev(currentNode);
//                        pq.add(new PQNode<>(cost, neighbor));
//                    }
//                }
//            }
//        }

        while (!pq.isEmpty()) {
            PQNode<MazeNode> pqNode = pq.poll();
            MazeNode currentNode = pqNode.getData();
            int distance = currentNode.getDistance();

//            System.out.printf("Visiting Node: %s with distance: %d%n", currentNode, distance); // Debug output

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
//                        System.out.printf("Updating Neighbor: %s with new cost: %d%n", neighbor, cost); // Debug output
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
            System.err.println("No path found from start to end vertex using Dijkstra.");
            return null;
        }
        return path;
    }
}
