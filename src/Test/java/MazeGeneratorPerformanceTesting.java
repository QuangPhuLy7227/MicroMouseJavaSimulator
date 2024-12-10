import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import Maze.Maze;
import Maze.MazeGenerator;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class MazeGeneratorPerformanceTesting {

    @Test
    public void testMazeGenerationPerformance() throws IOException {
        int[] dimensions = {10, 50, 100, 200, 500};
        int[] nonTreeEdges = {5, 20, 50};

        for (int dimension : dimensions) {
            for (int nonTreeEdgeCount : nonTreeEdges) {
                Maze maze = new Maze(dimension);
                MazeGenerator generator = new MazeGenerator(maze);

                File datafile = new File("testMaze_" + dimension + "_" + nonTreeEdgeCount + ".dat");

                // Measure execution time
                long startTime = System.currentTimeMillis();
                generator.createRandomMaze(nonTreeEdgeCount, datafile);
                long endTime = System.currentTimeMillis();
                double timeTaken = (endTime - startTime) / 1000.0; // in seconds

                System.out.println("Maze of dimension " + dimension + " with " + nonTreeEdgeCount + " non-tree edges generated in: " + timeTaken + " seconds.");

                // Check if the maze file is generated correctly
                assertTrue(datafile.exists());

                // Measure memory usage
                Runtime runtime = Runtime.getRuntime();
                long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

                generator.createRandomMaze(nonTreeEdgeCount, datafile); // Re-run to measure memory usage

                long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
                long memoryUsed = memoryAfter - memoryBefore;

                System.out.println("Memory used for generating maze of dimension " + dimension + ": " + memoryUsed / (1024 * 1024) + " MB");

                // Cleanup (delete generated file after test)
                datafile.delete();
            }
        }
    }

    @Test
    public void testStressMazeGeneration() throws IOException {
        int dimension = 1000;
        int nonTreeEdgeCount = 5000;

        Maze maze = new Maze(dimension);
        MazeGenerator generator = new MazeGenerator(maze);

        File datafile = new File("testMaze_" + dimension + ".dat");

        // Measure execution time for a large maze
        long startTime = System.currentTimeMillis();
        generator.createRandomMaze(nonTreeEdgeCount, datafile);
        long endTime = System.currentTimeMillis();

        double timeTaken = (endTime - startTime) / 1000.0; // in seconds

        System.out.println("Large maze of dimension " + dimension + " with " + nonTreeEdgeCount + " non-tree edges generated in: " + timeTaken + " seconds.");

        assertTrue(datafile.exists());

        datafile.delete();
    }

    @Test
    public void testMemoryUsage() throws IOException {
        int dimension = 100;
        int nonTreeEdgeCount = 30;

        Maze maze = new Maze(dimension);
        MazeGenerator generator = new MazeGenerator(maze);

        File datafile = new File("testMaze_" + dimension + ".dat");

        // Measure initial memory usage
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        generator.createRandomMaze(nonTreeEdgeCount, datafile);

        // Measure memory usage after maze generation
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;

        System.out.println("Memory used for generating maze of dimension " + dimension + ": " + memoryUsed / (1024 * 1024) + " MB");

        datafile.delete();
    }
}
