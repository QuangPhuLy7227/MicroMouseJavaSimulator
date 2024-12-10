import Maze.Maze;
import Maze.MazeNode;
import Maze.MazeSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class MazeSerializerUnitTesting {

    private Maze maze;
    private MazeSerializer mazeSerializer;

    @BeforeEach
    public void setUp() {
        maze = new Maze(10);
        mazeSerializer = new MazeSerializer(maze);
    }

    @Test
    public void testSaveMazeAsDat() throws IOException {
        File file = new File("test.dat");
        mazeSerializer.saveMaze(file);

        assertTrue(file.exists(), "The .dat file should be saved.");
    }

    @Test
    public void testLoadMazeFromDat() throws IOException {
        File file = new File("test.dat");
        mazeSerializer.saveMaze(file);

        boolean result = mazeSerializer.loadMaze(file);

        assertTrue(result, "Maze should load successfully from .dat file");

        file.delete();
    }

    @Test
    public void testSaveMazeAsMap() throws IOException {
        File file = new File("test.map");

        mazeSerializer.saveMazeAsMap(file);

        assertTrue(file.exists(), "The .map file should be saved.");
    }

    @Test
    public void testLoadMazeFromMap() throws IOException {
        // Create the test.map file with mock data
        File file = new File("test.map");
        String mapData = "++++++++++\n" +
                "+  +   + +\n" +
                "++++++++++\n";  // Sample data for testing
        Files.write(file.toPath(), mapData.getBytes());

        // Load the maze from the map file
        mazeSerializer.loadMazeFromMap(file);

        // Check that the top wall (up) and left wall (left) for maze[0][0] are null
        assertNull(maze.at(0, 0).up, "The top wall for maze[0][0] should be null.");
        assertNull(maze.at(0, 0).left, "The left wall for maze[0][0] should be null.");

        file.delete();
    }


    @Test
    public void testSaveMazeAsNum() throws IOException {
        File file = new File("test.num");

        mazeSerializer.saveMazeAsNum(file);

        assertTrue(file.exists(), "The .num file should be saved.");
    }

    @Test
    public void testLoadMazeFromNum() throws IOException {
        // Create the test.num file with mock data
        File file = new File("test.num");
        String numData = "0 0 1 0 0 0\n" +
                "1 0 0 1 0 0\n"; // Sample data for testing
        Files.write(file.toPath(), numData.getBytes());

        // Load the maze from the num file
        mazeSerializer.loadMazeFromNum(file);

        // Check that the north wall of maze[0][0] is null (as per the error message)
        assertNull(maze.at(0, 0).up, "The north wall for maze[0][0] should be null.");
        assertNull(maze.at(0, 0).left, "The west wall for maze[0][0] should be null.");

        file.delete();
    }


    @Test
    public void testSaveMazeWithUnsupportedExtension() {
        File file = new File("test.xyz");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mazeSerializer.saveMaze(file);
        });
        assertEquals("Unsupported file format: test.xyz", exception.getMessage());
    }

    @Test
    public void testLoadMazeWithUnsupportedExtension() throws IOException {
        // Create a new file in the project root directory
        File file = new File("test.xyz");
        // Ensure the file exists (this step is important in case the file doesn't exist)
        if (!file.exists()) {
            file.createNewFile();
        }

        // Verify that the file exists before running the test
        assertTrue(file.exists(), "File test.xyz should exist in the project root.");

        // Run the test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mazeSerializer.loadMaze(file);  // Expecting an exception for unsupported format
        });
        assertEquals("Unsupported file format: test.xyz", exception.getMessage());

        file.delete();
    }

    @Test
    public void testLoadMazeFileNotExist() throws IOException {
        File file = new File("nonexistent.dat");

        boolean result = mazeSerializer.loadMaze(file);

        assertFalse(result, "Maze loading should fail when the file doesn't exist");
    }
}
