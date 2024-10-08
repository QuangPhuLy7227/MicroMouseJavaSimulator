package Maze;

import Algorithms.AStar;
import Algorithms.DFS;
import Algorithms.Dijkstra;

import java.util.LinkedList;

public class PathFinder {
    private Maze maze;

    public PathFinder(Maze maze) {
        this.maze = maze;
    }

    public LinkedList<MazeNode> findPathUsingDijkstra(MazeNode startVertex, MazeNode endVertex) {
        Dijkstra dijkstra = new Dijkstra(maze);
        return dijkstra.findPath(startVertex, endVertex);
    }

    public LinkedList<MazeNode> findPathUsingDFS(MazeNode startVertex, MazeNode endVertex) {
        DFS dfs = new DFS(maze);
        return dfs.findPath(startVertex, endVertex);
    }

    public LinkedList<MazeNode> findPathUsingAStar(MazeNode startVertex, MazeNode endVertex) {
        AStar aStar = new AStar(maze);
        return aStar.findPath(startVertex, endVertex);
    }

    // Optimizes a given path by averaging coordinates for smoother transitions for the mouse in diagonal directions.
    public LinkedList<MazeNode> optimize(LinkedList<MazeNode> path){
        LinkedList<MazeNode> bestPath = new LinkedList<>();
        if (path == null || path.isEmpty()) {
            return bestPath;
        }
        MazeNode startVertex = path.getFirst();
        MazeNode endVertex = path.getLast();
        bestPath.addLast(startVertex);

        LinkedList<MazeNode> tempPath = new LinkedList<>(path);

        while (tempPath.size() > 1) {
            MazeNode currentNode = tempPath.removeFirst();
            MazeNode nextNode = tempPath.peekFirst();
            double row_bar = 0.5 * (currentNode.row + nextNode.row);
            double column_bar = 0.5 * (currentNode.column + nextNode.column);
            bestPath.addLast(new MazeNode(row_bar, column_bar));
        }
        bestPath.addLast(endVertex);
        return bestPath;
    }
}
