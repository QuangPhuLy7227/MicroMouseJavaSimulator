import Maze.Maze;
import Maze.MazeNode;
import Algorithms.DFS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DFSHelperTestStatement {
    private Maze maze;
    private DFS dfs;

    @BeforeEach
    public void init() {
        maze = new Maze(10);
        dfs = new DFS(maze);
    }

    /**
     * Path 1 - enter the recursive calls and a path was found
     * Path: (2) -> (3) -> (5) -> (6) -> (7) -> (8 included 9 & 10 in recursion) -> (4) -> End (refer to the CFD)
     * Input: ((1,1), (1,1), path)
     * Expected: true
     */
    @Test
    public void testSameStartA() {
        MazeNode start = maze.at(1, 1);
        MazeNode end = maze.at(1, 1);

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
}
