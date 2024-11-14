package Test.StatementCoverageTest;
import Maze.Maze;
import Maze.MazeNode;
import Algorithms.DFS;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class DFS_Helper_Test {
    private Maze maze;
    private DFS dfs;
    private MazeNode start;
    private MazeNode end;

    @Before
    public void init() {
        maze = new Maze(10);
        dfs = new DFS(maze);
    }

//    @Test
    // This might be redundant
//    /**
//     * Path 1 - startVertex is the same node as endVertex
//     * Path: (2) -> (3) -> (4) -> End (refer to the CFD)
//     * Input: ((1,1), (1,1), path)
//     * Expected: true
//     */
//    public void testSameStartAndEndVertex(){
//        start = maze.at(1,1);
//        end = maze.at(1,1);
//
//        LinkedList<MazeNode> path = dfs.findPath(start, end);
//
//        assertNotNull("Path should not be null", path);
//        assertTrue("Path should contain the start node", path.contains(start));
//        assertTrue("Path should contain the end node", path.contains(end));
//    }

    @Test
    /**
     * Path 1 - enter the recursive calls and a path was found
     * Path: (2) -> (3) -> (5) -> (6) -> (7) -> (8 included 9 & 10 in recursion) -> (4) -> End (refer to the CFD)
     * Input: ((1,1), (1,1), path)
     * Expected: true
     */
    public void testSameStartA(){
        start = maze.at(1,1);
        end = maze.at(1,1);

        LinkedList<MazeNode> path = dfs.findPath(start, end);

        assertNotNull("Path should not be null", path);
        assertTrue("Path should contain the start node", path.contains(start));
        assertTrue("Path should contain the end node", path.contains(end));

        // Print the path found
        System.out.print("Path found: ");
        for (MazeNode node : path) {
            System.out.print(node + " ");
        }
        System.out.println();
    }
}
