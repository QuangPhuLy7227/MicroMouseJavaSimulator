import Maze.Maze;
import Maze.MazeNode;
import Algorithms.DFS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DFSHelperTestPath {
    private Maze maze;
    private DFS dfs;
    private MazeNode start;
    private MazeNode end;

    @BeforeEach
    public void init(){
        maze = new Maze(10);
        dfs = new DFS(maze);
    }

    /**
     * Path 1 - startVertex is the same node as endVertex
     * Path: (2) -> (3) -> (4) -> End (refer to the CFD)
     * Input: ((1,1), (1,1), path)
     * Expected: true
     */
    @Test
    public void testSameStartAndEndVertex(){
        start = maze.at(1,1);
        end = maze.at(1,1);

        LinkedList<MazeNode> path = dfs.findPath(start, end);

        assertNotNull(path, "Path should not be null");
        assertTrue(path.contains(start), "Path should contain the start node");
        assertTrue(path.contains(end), "Path should contain the end node");

        // Print the path found
        System.out.print("Path found: ");
        for (MazeNode node : path) {
            System.out.print(node + " ");
        }
        System.out.println();
    }

    /**
     * Path 2 - enter the recursive calls with backtracking and a path was found
     * Path: (2) -> (3) -> (5) -> (6) -> (7) -> (6) -> (7) -> (8) -> (4) -> End (refer to the CFD)
     * Input: ((2,0), (2,1), path)
     * Expected: Path found
     */
    @Test
    public void testBacktrackingWithPathFound() {
        start = maze.at(2, 0);              // A at (2,0)
        MazeNode node2 = maze.at(3, 0);     // D at (3,0)
        end = maze.at(2, 1);                // B at (2,1)
        MazeNode node3 = maze.at(4, 0);     // C at (4,0)
        MazeNode node4 = maze.at(4, 1);     // E at (4,1)

        // Ensure A connects to D first
        maze.addEdge(start, node2);      // A -> D
        maze.addEdge(node2, start);      // D -> A
        maze.addEdge(start, end);        // A -> B

        maze.addEdge(node2, node3);      // D -> C
        maze.addEdge(node3, node2);      // C -> D

        maze.addEdge(node3, node4);      // C -> E
        maze.addEdge(node4, node3);      // E -> C

        // Print the neighbor list of the starting node
        System.out.print("Neighbors of Start Node " + start + ": ");
        LinkedList<MazeNode> startNeighbors = start.getNeighborList();
        for (MazeNode neighbor : startNeighbors) {
            System.out.print(neighbor + " ");
        }
        System.out.println(); // New line for better readability

        LinkedList<MazeNode> path = dfs.findPath(start, end);

        assertNotNull(path, "Path should not be null, indicating a path was found");
        assertTrue(path.contains(start), "Path should contain the start node");
        assertTrue(path.contains(end), "Path should contain the end node");

        // Print the path found
        System.out.print("Path found: ");
        for (MazeNode node : path) {
            System.out.print(node + " -> ");
        }
        System.out.println("End");
    }

    /**
     * Path 3 - enter the recursive calls with backtracking but no path was found
     * Path: (2) -> (3) -> (5) -> (6) -> (7) -> (8) -> (6) -> (9) -> (10) -> End (refer to the CFD)
     * Input: ((2,0), (9,0), path)
     * Expected: No path found from start to end vertex using DFS
     */
//    @Test
//    public void testBackTrackingWithNoPathFound() {
//
//        MazeNode startNode = maze.at(2, 0);
//        MazeNode node3 = maze.at(3, 0);
//        MazeNode node8 = maze.at(8, 0);
//        MazeNode endNode = maze.at(9, 0);
//
//        maze.addEdge(start, node3);
//        maze.addEdge(node3, node8);
//
//        LinkedList<MazeNode> path = dfs.findPath(startNode, endNode);
//
//        assertNull(path, "Path should be null, indicating no path was found");
//    }

    /**
     * Path 4 - startVertex has no neighbor
     * Path: (2) -> (3) -> (5) -> (6) -> (9) -> (10) -> End (refer to the CFD)
     * Input: ((2,0), (9,0), path)
     * Expected: No path found from start to end vertex using DFS
     */
//    @Test
//    public void testWithNoNeighbor() {
//        MazeNode startNode = maze.at(2, 0);
//        MazeNode node2 = maze.at(7, 0);
//        MazeNode node3 = maze.at(8, 0);
//        MazeNode endNode = maze.at(9, 0);
//
//        maze.addEdge(startNode, node3);
//        maze.addEdge(node3, endNode);
//
//        LinkedList<MazeNode> path = dfs.findPath(node2, endNode);
//
//        assertNull(path, "Path should be null, indicating no path found from start to end vertex using DFS");
//    }
}
