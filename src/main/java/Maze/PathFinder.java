package Maze;

import Algorithms.AStar;
import Algorithms.DFS;
import Algorithms.Dijkstra;

import java.util.LinkedList;

public class PathFinder {
    private final Maze maze;

    public PathFinder(Maze maze) {
        this.maze = maze;
    }

    public LinkedList<MazeNode> findPathUsingDijkstra(MazeNode startVertex, MazeNode endVertex) {
        Dijkstra dijkstra = new Dijkstra(maze);
        LinkedList<MazeNode> path = dijkstra.findPath(startVertex, endVertex);
        return path != null ? path : new LinkedList<>(); // Return an empty list if null
    }

    public LinkedList<MazeNode> findPathUsingDFS(MazeNode startVertex, MazeNode endVertex) {
        DFS dfs = new DFS(maze);
        LinkedList<MazeNode> path = dfs.findPath(startVertex, endVertex);
        return path != null ? path : new LinkedList<>(); // Return an empty list if null
    }

    public LinkedList<MazeNode> findPathUsingAStar(MazeNode startVertex, MazeNode endVertex) {
        AStar aStar = new AStar(maze);
        LinkedList<MazeNode> path = aStar.findPath(startVertex, endVertex);
        return path != null ? path : new LinkedList<>(); // Return an empty list if null
    }
}
