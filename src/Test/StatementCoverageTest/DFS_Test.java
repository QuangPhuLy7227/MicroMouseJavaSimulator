package Test.StatementCoverageTest;
import Maze.Maze;
import Maze.MazeNode;
import Algorithms.DFS;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class DFS_Test {
    private Maze maze;
    private DFS dfs;
    private MazeNode start;
    private MazeNode end;

    @Before
    public void init() {
        maze = new Maze(10);
        dfs = new DFS(maze);
    }

    @Test
    /**
     * Path 1 - startVertex is null or endVertex is null
     * Path: (3) -> (4) -> (5) -> (2) -> (6) -> End (refer to the CFD)
     * Input: (null, (4, 4)) || ((0,0), null)
     * Expected: Invalid starting or ending vertex for DFS
     */
    public void testNullStartOrNullEnd() {
        assertNull(dfs.findPath(null, maze.at(4, 4)));
        assertNull(dfs.findPath(maze.at(0, 0), null));
    }

    @Test
    /**
     * Path 2 - a path exists
     * Path: (3) -> (4) -> (7) -> (8) -> (7) -> (9) -> (10) -> (11) -> End (refer to the CFD)
     * Input: ((0, 0)), ((1,0))
     * Expected: Path found
     */
    public void testValidPath() {
        start = new MazeNode(0, 0);
        end = new MazeNode(1, 0);

        maze.addEdge(start, end);

        LinkedList<MazeNode> result = dfs.findPath(start, end);

        assertNotNull(result);
        assertTrue(result.contains(start));
        assertTrue(result.contains(end));
    }

    @Test
    /**
     * Path 3 - no path found
     * Path: (3) -> (4) -> (7) -> (8) -> (7) -> (9) -> (10) -> (12) -> End (refer to the CFD)
     * Input: ((0, 0), (9,0))
     * Expected: No path found from start to end vertex using DFS
     */
    public void testNoPath() {
        start = new MazeNode(0, 0);
        end = new MazeNode(9, 0);

        LinkedList<MazeNode> result = dfs.findPath(start, end);

        assertNull(result);
    }
}
