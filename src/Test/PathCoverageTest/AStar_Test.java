package Test.PathCoverageTest;

import Algorithms.AStar;
import Maze.Maze;
import Maze.MazeNode;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class AStar_Test {
    private Maze maze;
    private AStar aStar;
    private MazeNode nodeA;
    private MazeNode nodeB;

    @Before
    public void init() {
        maze = new Maze(5);
        aStar = new AStar(maze);
    }

    @Test
    /**
     * Test 1 - startVertex is null or endVertex is null
     * Path: (1) -> (2) -> (4) -> End (refer to the CFD)
     * Input: (null, (4,4) || (0,0), null)
     * Expected: Invalid starting or ending vertex for DFS
     */
    public void testNullStartOrNullEnd() {
        assertNull(aStar.findPath(null, maze.at(4, 4)));
        assertNull(aStar.findPath(maze.at(0, 0), null));
    }

    @Test
    /**
     * Test 2 - A valid path is found using AStar
     * Path: (1) -> (2) -> (5) -> (6) -> (5) -> (7) -> (8) -> (14) -> (15) -> (16) -> (17) -> (18) -> (19) -> (20)
     * -> (17) -> (8) -> (9) -> (10) -> (11) -> (10) -> (12) -> (13) -> End (refer to the CFD)
     * Input:
     * Expected:
     */
    public void testPathFound() {
        // Connect (0,0) - (0,1) - (0,2) - (0,3) - (0,4)
        for (int i = 0; i < 4; i++){
            nodeA = maze.at(0, i);
            nodeB = maze.at(0, i + 1);
            maze.addEdge(nodeA, nodeB);
        }

        // Connect (0,4) - (1,4) - (2,4) - (3,4), (4,4)
        for (int i = 1; i < 5; i++){
            nodeA = maze.at(i,4);
            nodeB = maze.at(i - 1,4);
            maze.addEdge(nodeA, nodeB);
        }

        // Connect (0,0) - (1,0) - (2,0)
        for (int i = 0; i < 2; i++){
            maze.addEdge(maze.at(i, 0), maze.at(i + 1, 0));
        }

        // Connect (2,0) - (2,1) - (2,2)
        for (int i = 0; i < 2; i++){
            maze.addEdge(maze.at(2, i), maze.at(2, i + 1));
        }

        // Connect (2,2) - (3,2) - (3,1) - (3,0)
        for (int i = 0; i < 3; i++){
            nodeA = null;
            nodeB = null;

            if (i == 0){
                nodeA = maze.at(2,2);
                nodeB = maze.at(3,2);
            } else if (i == 1) {
                nodeA = maze.at(3,2);
                nodeB = maze.at(3,1);
            } else if (i == 2) {
                nodeA = maze.at(3,1);
                nodeB = maze.at(3,0);
            }

            if (nodeA != null && nodeB != null){
                maze.addEdge(nodeA, nodeB);
            }
        }

        // Connect (3,0) - (4,0) - (4,1) - (4,2) - (4,3) - (4,4)
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                nodeA = maze.at(3, 0);
                nodeB = maze.at(4, 0);
            } else if (i == 1) {
                nodeA = maze.at(4, 0);
                nodeB = maze.at(4, 1);
            } else if (i == 2) {
                nodeA = maze.at(4, 1);
                nodeB = maze.at(4, 2);
            } else if (i == 3) {
                nodeA = maze.at(4, 2);
                nodeB = maze.at(4, 3);
            } else if (i == 4) {
                nodeA = maze.at(4, 3);
                nodeB = maze.at(4, 4);
            }

            if (nodeA != null && nodeB != null) {
                maze.addEdge(nodeA, nodeB);
            }
        }

        nodeA = maze.at(0, 0);
        nodeB = maze.at(4, 4);
        LinkedList<MazeNode> path = aStar.findPath(nodeA, nodeB);

        assertNotNull(path);
        assertEquals(nodeA, path.getFirst());
        assertEquals(nodeB, path.getLast());
    }


    @Test
    /**
     * Test 3 - No path is found using AStar
     * Path: (1) -> (2) -> (5) -> (6) -> (5) -> (7) -> (8) -> (14) -> (15) -> (16) -> (17) -> (18) -> (19) -> (20)
     * -> (17) -> (8) -> (9) -> (10) -> (11) -> (10) -> (12) -> (14) -> (4) -> End (refer to the CFD)
     * Input:((0,0), (4,4))
     * Expected: No path found from start to end vertex using AStar
     */
    public void testNoPathFound() {
        // Connect (0,0) - (0,1) - (0,2) - (0,3) - (0,4)
        for (int i = 0; i < 4; i++){
            nodeA = maze.at(0, i);
            nodeB = maze.at(0, i + 1);
            maze.addEdge(nodeA, nodeB);
        }

        // Connect (0,0) - (1,0) - (2,0) - (3,0) - (4,0)
        for (int i = 0; i < 4; i++){
            nodeA = maze.at(i, 0);
            nodeB = maze.at(i + 1, 0);
            maze.addEdge(nodeA, nodeB);
        }

        nodeA = maze.at(0,0);
        nodeB = maze.at(4,4);

        LinkedList<MazeNode> path = aStar.findPath(nodeA, nodeB);
        assertNull("Path should be null when end node is disconnected", path);
//        assertTrue("No path found from start to end vertex using AStar", path == null);
    }


    @Test
    /**
     * Test 4 - startVertex does not have any neighbor and the for-loop inside will not get executed
     * Path: (1) -> (2) -> (5) -> (6) -> (5) -> (7) -> (8) -> (14) -> (15) -> (16) -> (17) -> (8) -> (9)
     * -> (10) -> (11) -> (10) -> (12) -> (14) -> (4) -> End (refer to the CFD)
     * Input:((0,0), (4,0))
     * Expected: No path found from start to end vertex using AStar
     */
    public void testStartVertexWithNoNeighbor() {
        // Connect (1,0) - (2,0) - (3,0) - (4,0)
        for (int i = 0; i < 3; i++){
            nodeA = maze.at(1, 0);
            nodeB = maze.at(i + 1, 0);
            maze.addEdge(nodeA, nodeB);
        }

        nodeA = maze.at(0,0);
        nodeB = maze.at(4,0);

        LinkedList<MazeNode> path = aStar.findPath(nodeA, nodeB);
        assertNull("Path should be null when end node is disconnected", path);
    }

}
