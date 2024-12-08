package Maze;

import Algorithms.AStar;
import Algorithms.DFS;
import Algorithms.Dijkstra;

import java.util.LinkedList;

public class PathFinder {
    private final Maze maze;
    private LinkedList<MazeNode> dijkstraPath = new LinkedList<MazeNode>();
    private LinkedList<MazeNode> dfsPath = new LinkedList<MazeNode>();

    public PathFinder(Maze maze) {
        this.maze = maze;
    }

    public LinkedList<MazeNode> findPathUsingDijkstra(MazeNode startVertex, MazeNode endVertex) {
        Dijkstra dijkstra = new Dijkstra(maze);
        dijkstraPath.clear();
        dijkstraPath = dijkstra.findPath(startVertex, endVertex);
        return dijkstraPath;
    }

    public LinkedList<MazeNode> findPathUsingDFS(MazeNode startVertex, MazeNode endVertex) {
        DFS dfs = new DFS(maze);
        dfsPath = dfs.findPath(startVertex, endVertex);
        return dfsPath;
    }

    public LinkedList<MazeNode> findPathUsingAStar(MazeNode startVertex, MazeNode endVertex) {
        AStar aStar = new AStar(maze);
        LinkedList<MazeNode> path = aStar.findPath(startVertex, endVertex);
        return path != null ? path : new LinkedList<>(); // Return an empty list if null
    }

    /**
     * Path optimization with regards to the mouse's ability to move in
     * diagonal directions.
     * @param path linked list path to be optimized.
     * @return Nothing.
     */
    public LinkedList<MazeNode> optimize( LinkedList<MazeNode> path ) {
        LinkedList<MazeNode> bestPath = new LinkedList<MazeNode>();
        MazeNode startVertex = path.peekFirst();
        MazeNode endVertex = path.peekLast();

        bestPath.addLast( startVertex );

        while( path.size() > 1 ) {
            /* smoothen sharp turns by averaging direction */
            MazeNode currentNode = path.removeFirst();
            MazeNode nextNode = path.peekFirst();
            double row_bar = 0.5 * ( currentNode.row + nextNode.row );
            double column_bar = 0.5 * ( currentNode.column + nextNode.column );
            bestPath.addLast( new MazeNode(row_bar, column_bar) );
        }

        bestPath.addLast( endVertex );
        return bestPath;
    }

    public LinkedList<MazeNode> getDijkstraPath() {
        return dijkstraPath;
    }

    public LinkedList<MazeNode> getDfsPath() {
        return dfsPath;
    }
}
