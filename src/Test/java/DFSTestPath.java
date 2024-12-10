import Maze.Maze;
import Maze.MazeNode;
import Algorithms.DFS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DFSTestPath {
    private Maze maze;
    private DFS dfs;
    private MazeNode start;
    private MazeNode end;

    @BeforeEach
    public void init() {
        maze = new Maze(10);
        dfs = new DFS(maze);
    }

    /**
     * Path 1 - startVertex is null or endVertex is null
     * Path: (3) -> (4) -> (5) -> (6) -> End (refer to the CFD)
     * Input: (null, (4, 4)) || ((0,0))
     * Expected: Invalid starting or ending vertex for DFS
     */
    @Test
    public void testNullStartOrNullEnd() {
        assertNull(dfs.findPath(null, maze.at(4, 4)));
        assertNull(dfs.findPath(maze.at(0, 0), null));
    }

    /**
     * Path 2 - path exists (straight-line: unidirectional movement)
     * Path: (3) -> (4) -> (7) -> (8) -> (7) -> (9) -> (10) -> 11 -> End (refer to the CFD)
     * Input:
     * Expected: Path found
     */
    @Test
    public void testPathFound() {
        // Connect adjacent nodes
        start = maze.at(0,0);
        MazeNode node1 = maze.at(0, 1);
        MazeNode node2 = maze.at(0, 2);
        MazeNode node3 = maze.at(0, 3);
        end = maze.at(0,4);

        maze.addEdge(start, node1);
        maze.addEdge(node1, node2);
        maze.addEdge(node2, node3);
        maze.addEdge(node3, end);

        LinkedList<MazeNode> path = dfs.findPath(start, end);
        assertTrue(path.contains(end), "Path should contain the end node");


        System.out.print("Path found: ");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }

    /**
     * Path 3 - path does not exist (connected random nodes in the maze)
     * Path: (3) -> (4) -> (7) -> (8) -> (7) -> (9) -> (10) -> 12 -> End (refer to the CFD)
     * Input:
     * Expected: No path found from start to end vertex using DFS
     */
    @Test
    public void testNoPath() {
        start = maze.at(1, 0);
        MazeNode node1 = maze.at(1, 0);
        MazeNode node2 = maze.at(2, 0);
        MazeNode node3 = maze.at(3, 0);
        end = maze.at(4, 0);

        // Set up edges to simulate the path is incomplete
        maze.addEdge(start, node1);
        maze.addEdge(node1, node2);
        maze.addEdge(node2, node3);

        // Call DFS to find a path
        LinkedList<MazeNode> path = dfs.findPath(start, end);

        // Assert that no path was found (path should be empty)
        String message = "No path found from start to end vertex using DFS";
        assertNotNull(path, message);  // Ensure the result is not null
        assertTrue(path.isEmpty(), message);  // Ensure the path is empty
    }

}
