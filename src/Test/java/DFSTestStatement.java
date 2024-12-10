import Maze.Maze;
import Maze.MazeNode;
import Algorithms.DFS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DFSTestStatement {
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
     * Path: (3) -> (4) -> (5) -> (2) -> (6) -> End (refer to the CFD)
     * Input: (null, (4, 4)) || ((0,0), null)
     * Expected: Invalid starting or ending vertex for DFS
     */
    @Test
    public void testNullStartOrNullEnd() {
        assertNull(dfs.findPath(null, maze.at(4, 4)));
        assertNull(dfs.findPath(maze.at(0, 0), null));
    }

    /**
     * Path 2 - a path exists
     * Path: (3) -> (4) -> (7) -> (8) -> (7) -> (9) -> (10) -> (11) -> End (refer to the CFD)
     * Input: ((0, 0)), ((1,0))
     * Expected: Path found
     */
    @Test
    public void testValidPath() {
        start = new MazeNode(0, 0);
        end = new MazeNode(1, 0);

        maze.addEdge(start, end);

        LinkedList<MazeNode> result = dfs.findPath(start, end);

        assertNotNull(result);
        assertTrue(result.contains(start));
        assertTrue(result.contains(end));
    }

    /**
     * Path 3 - no path found
     * Path: (3) -> (4) -> (7) -> (8) -> (7) -> (9) -> (10) -> (12) -> End (refer to the CFD)
     * Input: ((0, 0), (9,0))
     * Expected: No path found from start to end vertex using DFS
     */
    @Test
    public void testNoPath() {
        start = new MazeNode(0, 0);
        end = new MazeNode(9, 0);

        LinkedList<MazeNode> result = dfs.findPath(start, end);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
