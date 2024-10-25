package Test.PathCoverageTest;

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
     * Path: (3) -> (4) -> (5) -> (6) -> End (refer to the CFD)
     * Input: (null, (4, 4)) || ((0,0))
     * Expected: Invalid starting or ending vertex for DFS
     */
    public void testNullStartOrNullEnd() {
        assertNull(dfs.findPath(null, maze.at(4, 4)));
        assertNull(dfs.findPath(maze.at(0, 0), null));
    }

    @Test
    /**
     * Path 2 - path exists (straight-line: unidirectional movement)
     * Path: (3) -> (4) -> (7) -> (8) -> (7) -> (9) -> (10) -> 11 -> End (refer to the CFD)
     * Input:
     * Expected: Path found
     */
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
        assertTrue("Path should contain the end node", path.contains(end));


        System.out.print("Path found: ");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }

    @Test
    /**
     * Path 3 - path does not exist (connected random nodes in the maze)
     * Path: (3) -> (4) -> (7) -> (8) -> (7) -> (9) -> (10) -> 12 -> End (refer to the CFD)
     * Input:
     * Expected: No path found from start to end vertex using DFS
     */
    public void testNoPath() {
        start = maze.at(0, 0);
        MazeNode node1 = maze.at(1, 0);
        MazeNode node2 = maze.at(2, 0);
        MazeNode node3 = maze.at(3, 0);
        end = maze.at(4, 0);

        maze.addEdge(start, node1);
        maze.addEdge(node1, node2);
        maze.addEdge(node2, node3);

        LinkedList<MazeNode> path = dfs.findPath(start, end);

        String message = "No path found from start to end vertex using DFS";
        assertNull(message, path);
    }
}
