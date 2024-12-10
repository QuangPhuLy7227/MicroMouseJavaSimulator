import Maze.Maze;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import Maze.MazeGenerator;
import Maze.Maze;

public class MazeGeneratorBVATesting {

    private Maze maze;
    private MazeGenerator mazeGenerator;

    //Domain 1: dim < MIN
    //Create five data points, (infinity, infinity + 1, -50, 2, 3)
//    @Test
//    public void testDomain1() {
//        // Test: dimension < MIN (should return error message for invalid dimension)
//        maze = new Maze(Integer.MIN_VALUE);
//        mazeGenerator = new MazeGenerator(maze);
//        String output = mazeGenerator.createRandomMaze();
//        assertEquals("Invalid Dimension for Maze Generation. Minimum dimension is 3.", output);
//
//        maze = new Maze(Integer.MIN_VALUE + 1);
//        output = mazeGenerator.createRandomMaze();
//        assertEquals("Invalid Dimension for Maze Generation. Minimum dimension is 3.", output);
//
//        maze = new Maze(-50);
//        output = mazeGenerator.createRandomMaze();
//        assertEquals("Invalid Dimension for Maze Generation. Minimum dimension is 3.", output);
//
//        maze = new Maze(2);
//        output = mazeGenerator.createRandomMaze();
//        assertEquals("Invalid Dimension for Maze Generation. Minimum dimension is 3.", output);
//
//        maze = new Maze(3);
//        output = mazeGenerator.createRandomMaze();
//        assertEquals("Normal Maze Dimension Generated.", output);
//    }

    //Domain 2: dim > MAX
    //Create five data points, (33, 34, 200, infinity - 1, infinity)
//    @Test
//    public void testDomain2() {
//        maze = new Maze(33);
//        mazeGenerator = new MazeGenerator(maze);
//        String output = mazeGenerator.createRandomMaze();
//        assertEquals("Invalid Dimension for Maze Generation. Maximum dimension is 32.", output);
//
//        maze = new Maze(34);
//        output = mazeGenerator.createRandomMaze();
//        assertEquals("Invalid Dimension for Maze Generation. Maximum dimension is 32.", output);
//
//        maze = new Maze(200);
//        output = mazeGenerator.createRandomMaze();
//        assertEquals("Invalid Dimension for Maze Generation. Maximum dimension is 32.", output);
//
//        maze = new Maze(Integer.MAX_VALUE - 1);
//        output = mazeGenerator.createRandomMaze();
//        assertEquals("Invalid Dimension for Maze Generation. Maximum dimension is 32.", output);
//
//        maze = new Maze(Integer.MAX_VALUE);
//        output = mazeGenerator.createRandomMaze();
//        assertEquals("Invalid Dimension for Maze Generation. Maximum dimension is 32.", output);
//    }


    //Domain 3: dim > BIG && dim % 2 == 0 => Maze Dimension Divided into Four Quadrants
    // Create five data points (18, 20, 32, 50, 64)
    @Test
    public void testDomain3() {
        maze = new Maze(18);
        mazeGenerator = new MazeGenerator(maze);
        String output = mazeGenerator.createRandomMaze();
        assertEquals("Maze Dimension Divided into Four Quadrants", output);

        maze = new Maze(20);
        output = mazeGenerator.createRandomMaze();
        assertEquals("Maze Dimension Divided into Four Quadrants", output);

        maze = new Maze(32);
        output = mazeGenerator.createRandomMaze();
        assertEquals("Maze Dimension Divided into Four Quadrants", output);

        maze = new Maze(50);
        output = mazeGenerator.createRandomMaze();
        assertEquals("Maze Dimension Divided into Four Quadrants", output);

        maze = new Maze(64);
        output = mazeGenerator.createRandomMaze();
        assertEquals("Maze Dimension Divided into Four Quadrants", output);
    }

    //Domain 4: dim <= BIG => Normal Maze Dimension Generated.
    // Create five data points (3, 4, 6, 15, 16)
    @Test
    public void testDomain4() {

        maze = new Maze(3);
        mazeGenerator = new MazeGenerator(maze);
        String output = mazeGenerator.createRandomMaze();
        assertEquals("Normal Maze Dimension Generated.", output);

        maze = new Maze(4);
        output = mazeGenerator.createRandomMaze();
        assertEquals("Normal Maze Dimension Generated.", output);

        maze = new Maze(6);
        output = mazeGenerator.createRandomMaze();
        assertEquals("Normal Maze Dimension Generated.", output);

        maze = new Maze(15);  // Valid dimension within the range
        output = mazeGenerator.createRandomMaze();
        assertEquals("Normal Maze Dimension Generated.", output);

        maze = new Maze(16);  // Maximum valid dimension <= BIG_DIM
        output = mazeGenerator.createRandomMaze();
        assertEquals("Normal Maze Dimension Generated.", output);
    }

    //Domain 5: dim > BIG && dim % 2 != 0 => Maze Dimension Cannot be Divided into Four Quadrants
    // Create five data points (17, 19, 21, 25, 31)
    @Test
    public void testDomain5() {
        // Test: dimension > BIG_DIM and dimension is odd (should return "Maze Dimension Cannot be Divided into Four Quadrants")

        maze = new Maze(17);  // Just above BIG_DIM and odd
        mazeGenerator = new MazeGenerator(maze);
        String output = mazeGenerator.createRandomMaze();
        assertEquals("Maze Dimension Cannot be Divided into Four Quadrants", output);

        maze = new Maze(19);  // Odd dimension greater than BIG_DIM
        output = mazeGenerator.createRandomMaze();
        assertEquals("Maze Dimension Cannot be Divided into Four Quadrants", output);

        maze = new Maze(21);  // Another odd dimension greater than BIG_DIM
        output = mazeGenerator.createRandomMaze();
        assertEquals("Maze Dimension Cannot be Divided into Four Quadrants", output);

        maze = new Maze(25);  // Odd dimension greater than BIG_DIM
        output = mazeGenerator.createRandomMaze();
        assertEquals("Maze Dimension Cannot be Divided into Four Quadrants", output);

        maze = new Maze(31);  // Odd dimension at the maximum boundary above BIG_DIM
        output = mazeGenerator.createRandomMaze();
        assertEquals("Maze Dimension Cannot be Divided into Four Quadrants", output);
    }
}
